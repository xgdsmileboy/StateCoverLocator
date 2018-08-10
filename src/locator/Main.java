/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jdk7.wrapper.JCompiler;
import locator.aux.extractor.core.parser.Analyzer;
import locator.common.config.Configure;
import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.JavaFile;
import locator.common.java.Subject;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.common.util.Pair;
import locator.common.util.ProjectSelector;
import locator.common.util.Utils;
import locator.core.CoverInfo;
import locator.core.Suspicious;
import locator.core.alg.Algorithm;
import locator.core.alg.Barinel;
import locator.core.alg.DStar;
import locator.core.alg.Ochiai;
import locator.core.alg.Op2;
import locator.core.alg.Simple;
import locator.core.alg.Sober;
import locator.core.alg.StatisticalDebugging;
import locator.core.alg.Tarantula;
import locator.core.model.DNN;
import locator.core.model.Model;
import locator.core.model.SDModel;
import locator.core.model.XGBoost;
import locator.core.run.path.Collector;
import locator.core.run.path.Coverage;
import locator.inst.visitor.BranchInstrumentVisitor;
import locator.inst.visitor.StatementInstrumentVisitor;

public class Main {

	private static boolean proceed(Subject subject, boolean useStatisticalDebugging, boolean useSober) {
		
		LevelLogger.info("------------------ Begin : " + subject.getName() + "_" + subject.getId() + " ----------------");
		
		// remove auxiliary file
		String path = subject.getHome() + subject.getSsrc();
		String auxiliary = path + Constant.PATH_SEPARATOR + "auxiliary/Dumper.java";
		
		ExecuteCommand.deleteGivenFile(auxiliary);
		Utils.backupSource(subject);
		Identifier.resetAll();

		Model model = null;
		// train predicate prediction model
		if(useStatisticalDebugging) {
			model = new SDModel();
		} else {
			if(DNN.NAME.equals(Constant.TRAINING_MODEL)) {
				model = new DNN();
			} else if(XGBoost.NAME.equals(Constant.TRAINING_MODEL)) {
				model = new XGBoost();
			}
			if(Constant.TRAINING_EVALUATION) {
				model.evaluate(subject);
				String mlOutput = JavaFile.readFileToString(Constant.STR_TMP_ML_LOG_FILE);
				JavaFile.writeStringToFile(Constant.STR_ML_EVALUATION + "/" + subject.getNameAndId() + ".ml.out", mlOutput);
				return true;
			} else {
				model.trainModel(subject);
			}
		}

		// copy auxiliary file to subject path
		LevelLogger.info("copying auxiliary file to subject path.");
		Configure.config_dumper(subject);
		// fix bug for ast parser
		Configure.config_astlevel(subject);

		Identifier.restore(subject);
		Set<String> allCoveredStatement = new HashSet<>();
		Set<Integer> failedTests = new HashSet<>();
		int totalTestNum = -1;
		if(!Constant.BOOL_RECOMPUTE_ORI) {
			totalTestNum = Utils.recoverFailedTestsAndCoveredStmt(subject, failedTests, allCoveredStatement);
		}
		int stepRecord = 1;
		if(totalTestNum < 0) {
			LevelLogger.info("step " + (stepRecord ++) + " : collect failed test and covered methods.");
			Pair<Set<Integer>, Set<Integer>> failedTestsAndCoveredMethods = Collector.collectFailedTestAndCoveredMethod(subject);
//			int totalFailed = failedTestsAndCoveredMethods.getFirst().size();
			
			// output branch coverage information
			if(Constant.BOOL_OUT_BRANCH_COVERAGE) {
				LevelLogger.info("step " + (stepRecord ++) + " : compute branch coverage information.");
				String testsPath = subject.getHome() + "/all-tests.txt";
				ExecuteCommand.deleteGivenFile(testsPath);
				Map<String, CoverInfo> coverage = Coverage.computeOriginalCoverage(subject, failedTestsAndCoveredMethods, BranchInstrumentVisitor.class);
				LevelLogger.info(">>> output branch coverage information to file : branch_coverage.csv");
				Utils.printCoverage(coverage, subject.getCoverageInfoPath(), "branch_coverage.csv");
			}
			
			LevelLogger.info("step " + (stepRecord ++) + " : compute original coverage information.");
			String testsPath = subject.getHome() + "/all-tests.txt";
			ExecuteCommand.deleteGivenFile(testsPath);
			Map<String, CoverInfo> coverage = Coverage.computeOriginalCoverage(subject, failedTestsAndCoveredMethods, StatementInstrumentVisitor.class);
			totalTestNum = JavaFile.readFileToStringList(testsPath).size();
			
			LevelLogger.info("output original coverage information to file : ori_coverage.csv");
			Utils.printCoverage(coverage, subject.getCoverageInfoPath(), "ori_coverage.csv");
	
			failedTests = failedTestsAndCoveredMethods.getFirst();
			LevelLogger.info("step " + (stepRecord ++) + " : compute statements covered by failed tests");
			for(Entry<String, CoverInfo> entry : coverage.entrySet()){
				if(entry.getValue().getFailedCount() > 0){
					allCoveredStatement.add(entry.getKey());
				}
			}
			
			Identifier.backup(subject);
			Utils.backupFailedTestsAndCoveredStmt(subject, totalTestNum, failedTests, allCoveredStatement);
		}
		
		LevelLogger.info("step " + (stepRecord ++) + " : compute predicate coverage information");
		Map<String, CoverInfo> predicateCoverage = Coverage.computePredicateCoverage(subject, model, allCoveredStatement,
				failedTests, useSober);

		if(predicateCoverage == null && !useSober){
			return false;
		}
		
		if (!useSober) {
			String predCoverageFile = useStatisticalDebugging ? "pred_coverage_sd.csv" : "pred_coverage.csv";
			LevelLogger.info("output predicate coverage information to file : " + predCoverageFile);
			Utils.printCoverage(predicateCoverage, subject.getCoverageInfoPath(), predCoverageFile);
		}

		LevelLogger.info("step " + (stepRecord ++) + " : compute suspicious for each statement and out put to file.");
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
		Suspicious.compute(subject, algorithms, failedTests.size(), totalTestNum - failedTests.size(), useStatisticalDebugging, useSober);
		
		return true;
	}

	
	public static void main(String[] args) {
		
		List<Subject> allSubjects = getTestSubject(args);
		for(Subject subject : allSubjects) {
			// clear cache to enable re-analyzing source files for different subjects
			Analyzer.clearCache();
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy:MM:dd:HH:mm:ss");
				Date startTime = new Date();
				String begin = simpleDateFormat.format(startTime);
				LevelLogger.info("BEGIN : " + begin);

				String subjectInfo = subject.getName() + "_" + subject.getId();
				// set compile level : to refactor
				JCompiler.setSourceLevel(subject.getSourceLevel());
				JCompiler.setTargetLevel(subject.getTargetLevel());
				if (!proceed(subject, Constant.BOOL_USE_STATISTICAL_DEBUGGING, Constant.BOOL_USE_SOBER)) {
					String d4jOutput = JavaFile.readFileToString(Constant.STR_TMP_D4J_OUTPUT_FILE);
					JavaFile.writeStringToFile(Constant.STR_ERROR_BACK_UP + "/" + subjectInfo + ".d4j.out", d4jOutput);
				}

				Date endTime = new Date();
				String end = simpleDateFormat.format(endTime);
				LevelLogger.info("BEGIN : " + begin + " - END : " + end);
				JavaFile.writeStringToFile(Constant.STR_TIME_LOG, subjectInfo + "\t"
						+ Long.toString(endTime.getTime() - startTime.getTime()) + "\t" + startTime + "\n", true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static List<Subject> getTestSubject(String[] args) {
		List<Subject> allSubjects = new ArrayList<>();
		if(args.length > 0) {
			if(args.length == 1) {
				allSubjects = ProjectSelector.select(args[0]);
			} else {
				String[] idStrings = args[1].split(",");
				List<Integer> ids = new ArrayList<>();
				for(String string : idStrings) {
					if(string.contains("-")) {
						String[] range = string.split("-");
						int start = Integer.parseInt(range[0]);
						int end = Integer.parseInt(range[1]);
						for(; start <= end; start ++) {
							ids.add(start);
						}
					} else {
						ids.add(Integer.parseInt(string));
					}
				}
				allSubjects = ProjectSelector.select(args[0], ids);
			}
		}
		//for debug
		LevelLogger.debug("----------Current run bugs ------------");
		if(allSubjects.size() > 0) {
			StringBuffer stringBuffer = new StringBuffer(allSubjects.get(0).getName() + " : ");
			for(Subject subject : allSubjects) {
				stringBuffer.append(subject.getId() + ", ");
			}
			LevelLogger.debug(stringBuffer.toString());
		}
		LevelLogger.debug("---------------------------------\n");
		return allSubjects;
	}
	
}
