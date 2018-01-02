/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import locator.common.config.Configure;
import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.CoverInfo;
import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.common.util.ProjectSelector;
import locator.common.util.Utils;
import locator.core.Algorithm;
import locator.core.Barinel;
import locator.core.DStar;
import locator.core.Ochiai;
import locator.core.Op2;
import locator.core.Simple;
import locator.core.Sober;
import locator.core.StatisticalDebugging;
import locator.core.Suspicious;
import locator.core.Tarantula;
import locator.core.model.DNN;
import locator.core.model.L2S;
import locator.core.model.Model;
import locator.core.model.XGBoost;
import locator.core.run.path.Collector;
import locator.core.run.path.Coverage;

public class Main {

	private final static String __name__ = "@Main ";

	private static void proceed(Subject subject, boolean useStatisticalDebugging, boolean useSober) {

		LevelLogger
				.info("------------------ Begin : " + subject.getName() + "_" + subject.getId() + " ----------------");

		// remove auxiliary file
		String path = subject.getHome() + subject.getSsrc();
		String auxiliary = path + Constant.PATH_SEPARATOR + "auxiliary/Dumper.java";

		ExecuteCommand.deleteGivenFile(auxiliary);
		
		Utils.backupSource(subject);
		Identifier.resetAll();

		Model model = null;
		if (Constant.TRAINING_MODEL.equals("dnn")) {
			model = new DNN();
		} else if (Constant.TRAINING_MODEL.equals("l2s")) {
			model = new L2S();
		} else {
			model = new XGBoost();
		}
		// train predicate prediction model
		if (!useStatisticalDebugging) {
			model.trainModel(subject);
		}

		// copy auxiliary file to subject path
		LevelLogger.info(__name__ + "copying auxiliary file to subject path.");
		Configure.config_dumper(subject);
		// fix bug for ast parser
		Configure.config_astlevel(subject);
		//
		LevelLogger.info(__name__ + "step 1: collect failed test and covered methods.");
		Pair<Set<Integer>, Set<Integer>> failedTestsAndCoveredMethods = Collector
				.collectFailedTestAndCoveredMethod(subject);
		int totalFailed = failedTestsAndCoveredMethods.getFirst().size();

		LevelLogger.info(__name__ + "step 2: compute original coverage information.");
		String testsPath = subject.getHome() + "/all-tests.txt";
		ExecuteCommand.deleteGivenFile(testsPath);
		Map<String, CoverInfo> coverage = Coverage.computeOriginalCoverage(subject, failedTestsAndCoveredMethods);
		int totalTestNum = JavaFile.readFileToStringList(testsPath).size();

		LevelLogger.info(__name__ + "output original coverage information to file : ori_coverage.csv");
		File covInfoPath = new File(subject.getCoverageInfoPath());
		if (!covInfoPath.exists()) {
			covInfoPath.mkdirs();
		}
		Utils.printCoverage(coverage, subject.getCoverageInfoPath() + "/ori_coverage.csv");

		LevelLogger.info(__name__ + "step 3: compute statements covered by failed tests");
		Set<String> allCoveredStatement = new HashSet<>();
		for (Entry<String, CoverInfo> entry : coverage.entrySet()) {
			if (entry.getValue().getFailedCount() > 0) {
				allCoveredStatement.add(entry.getKey());
			}
		}

		Identifier.backup(subject);

		LevelLogger.info(__name__ + "step 4: compute predicate coverage information");
		Map<String, CoverInfo> predicateCoverage = Coverage.computePredicateCoverage(subject, allCoveredStatement,
				model, failedTestsAndCoveredMethods.getFirst(), useStatisticalDebugging, useSober);

		if (predicateCoverage == null && !useSober) {
			return;
		}

		if (!useSober) {
			String predCoverageFile = useStatisticalDebugging ? "pred_coverage_sd.csv" : "pred_coverage.csv";
			LevelLogger.info(__name__ + "output predicate coverage information to file : " + predCoverageFile);
			Utils.printCoverage(predicateCoverage, subject.getCoverageInfoPath() + "/" + predCoverageFile);
		}

		LevelLogger.info(__name__ + "Compute suspicious for each statement and out put to file.");
		List<Algorithm> algorithms = new ArrayList<>();
		if (useSober) {
			algorithms.add(new Sober());
		} else {
			// add different computation algorithms
			algorithms.add(new Ochiai());
			algorithms.add(new Tarantula());
			algorithms.add(new DStar());
			algorithms.add(new Barinel());
			algorithms.add(new Op2());
			algorithms.add(new Simple());
			algorithms.add(new StatisticalDebugging());
		}
		Suspicious.compute(subject, algorithms, totalFailed, totalTestNum - totalFailed, useStatisticalDebugging,
				useSober);

		// LevelLogger.info("step 5: combine all coverage informaiton");
		// for (Entry<String, CoverInfo> entry : predicateCoverage.entrySet()) {
		// CoverInfo coverInfo = coverage.get(entry.getKey());
		// if (coverInfo != null) {
		// coverInfo.combine(entry.getValue());
		// } else {
		// coverage.put(entry.getKey(), entry.getValue());
		// }
		// }
		//
		// LevelLogger.info("step 6: output coverage information to file :
		// coverage.csv");
		// printCoverage(coverage, Constant.STR_INFO_OUT_PATH + "/" +
		// subject.getName() + "/" + subject.getName() + "_"
		// + subject.getId() + "/coverage.csv");
	}

	public static void main(String[] args) {
		//
		// double [] prob = {1.0, 1.0, 1.0, 5.0, 1.0, 1.0}; // select more math
		// project
		// List<Subject> allSubjects = ProjectSelector.randomSelect(prob, 80);
		// recordSubjects(allSubjects);
		// for(int i = 53; i < allSubjects.size(); i++) {
		// try {
		List<Subject> allSubjects = ProjectSelector.select("math");
		if(args.length > 0) {
			allSubjects = ProjectSelector.select(args[0]);
		} else {
			allSubjects = ProjectSelector.select("math");
		}
		
		// Subject subject = ProjectSelector.select("math", 4);
		for (Subject subject : allSubjects) {
			try {
				// File file = new File(subject.getCoverageInfoPath() +
				// "/predicates_backup.txt");
				// if (!file.exists()) continue;
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy:MM:dd:HH:mm:ss");
				String begin = simpleDateFormat.format(new Date());
				LevelLogger.info("BEGIN : " + begin);

				proceed(subject, Constant.USE_STATISTICAL_DEBUGGING, Constant.USE_SOBER);

				String end = simpleDateFormat.format(new Date());
				LevelLogger.info("BEGIN : " + begin + " - END : " + end);
			} catch (Exception e) {
				e.printStackTrace();
				// JavaFile.writeStringToFile(Constant.HOME + "/logs/" +
				// subject.getName() + "_" + Integer.toString(subject.getId())
				// + "_error.log");
			}
		}
	}

}
