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

import javax.annotation.Untainted;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.internal.e4.migration.InfoReader;

import java.util.Set;

import edu.pku.sei.conditon.simple.FeatureGenerator;
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
import locator.core.run.Runner;
import locator.core.run.path.Collector;
import locator.core.run.path.Coverage;
import locator.core.run.path.ExecutionPathBuilder;
import locator.inst.Instrument;
import locator.inst.visitor.DeInstrumentVisitor;
//import sun.security.x509.AlgorithmId;

public class Main {

	private final static String __name__ = "@Main ";

	private static void trainModel(Subject subject, boolean evaluate) {
		String modelPath = Constant.STR_ML_HOME + "/model/";
		if (Constant.TRAINING_MODEL.equals("dnn")){
			File varModel = new File(modelPath + subject.getName() + "_" + subject.getId() + "/var");
			File exprModel = new File(modelPath + subject.getName() + "_" + subject.getId() + "/expr");
			if (varModel.exists() && exprModel.exists()) {
				LevelLogger.info("Models are already exist and will be used directly !");
				return;
			}
		} else {
			File varModel = new File(modelPath + subject.getName() + "_" + subject.getId() + 
					"_" + Constant.TRAINING_MODEL + ".var_model.pkl");
			File exprModel = new File(modelPath + subject.getName() + "_" + subject.getId() +
					"_" + Constant.TRAINING_MODEL + ".expr_model.pkl");
			if (varModel.exists() && exprModel.exists()) {
				LevelLogger.info("Models are already exist and will be used directly !");
				return;
			}
		}
		// get train features
		String srcPath = subject.getHome() + subject.getSsrc();
		String outPath = Constant.STR_ML_VAR_OUT_FILE_PATH + "/" + subject.getName() + "/" + subject.getName() + "_"
				+ subject.getId();
		String targetVarPath = outPath + "/var/" + subject.getName() + "_" + subject.getId() + ".var.csv";
		String targetExprPath = outPath + "/expr/" + subject.getName() + "_" + subject.getId() + ".expr.csv";
		File file = new File(targetVarPath);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		file = new File(targetExprPath);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		FeatureGenerator.generateTrainFeature(srcPath, targetVarPath, targetExprPath);
		
		// create necessary directories
		String clusterDir = outPath + "/cluster";
		file = new File(clusterDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String predDir = outPath + "/pred";
		file = new File(predDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String predictResultDir = subject.getPredictResultDir();
		file = new File(predictResultDir);
		if (!file.exists()) {
			file.mkdirs();
		}

		if (evaluate) {
			// evaluate model
			try {
				LevelLogger.info(">>>>>> Begin Evaluating ...");
				ExecuteCommand.executeEvaluate(subject);
				LevelLogger.info(">>>>>> End Evaluating !");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// train model
			try {
				LevelLogger.info(">>>>>> Begin Trainning ...");
				ExecuteCommand.executeTrain(subject);
				LevelLogger.info(">>>>>> End Trainning !");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean proceed(Subject subject, boolean useStatisticalDebugging, boolean useSober) {
		
		LevelLogger.info("------------------ Begin : " + subject.getName() + "_" + subject.getId() + " ----------------");
		
		// remove auxiliary file
		String path = subject.getHome() + subject.getSsrc();
		String auxiliary = path + Constant.PATH_SEPARATOR + "auxiliary/Dumper.java";
		
		ExecuteCommand.deleteGivenFile(auxiliary);
		// deinstrument is useless
		backupSource(subject);
		Identifier.resetAll();
		
//		// preprocess : remove all instrument
//		DeInstrumentVisitor deInstrumentVisitor = new DeInstrumentVisitor();
//		Instrument.execute(subject.getHome() + subject.getSsrc(), deInstrumentVisitor);
//		Instrument.execute(subject.getHome() + subject.getTsrc(), deInstrumentVisitor);

		// train predicate prediction model
		if (!useStatisticalDebugging) {
			trainModel(subject, Constant.TRAINING_EVALUATION);
		}
		if (Constant.TRAINING_EVALUATION) {
			String mlOutput = JavaFile.readFileToString(Constant.STR_TMP_ML_LOG_FILE);
			JavaFile.writeStringToFile(Constant.STR_ML_EVALUATION + "/" + subject.getName() + "_" + subject.getId() + ".ml.out", mlOutput);
			return true;
		}

		// copy auxiliary file to subject path
		LevelLogger.info("copying auxiliary file to subject path.");
		Configure.config_dumper(subject);
		// fix bug for ast parser
		Configure.config_astlevel(subject);
//
		LevelLogger.info("step 1: collect failed test and covered methods.");
		Pair<Set<Integer>, Set<Integer>> failedTestsAndCoveredMethods = Collector.collectFailedTestAndCoveredMethod(subject);
		int totalFailed = failedTestsAndCoveredMethods.getFirst().size();
		
		LevelLogger.info("step 2: compute original coverage information.");
		String testsPath = subject.getHome() + "/all-tests.txt";
		ExecuteCommand.deleteGivenFile(testsPath);
		Map<String, CoverInfo> coverage = Coverage.computeOriginalCoverage(subject, failedTestsAndCoveredMethods);
		int totalTestNum = JavaFile.readFileToStringList(testsPath).size();
		
		LevelLogger.info("output original coverage information to file : ori_coverage.csv");
		File covInfoPath = new File(subject.getCoverageInfoPath());
		if (!covInfoPath.exists()) {
			covInfoPath.mkdirs();
		}
		printCoverage(coverage, subject.getCoverageInfoPath() + "/ori_coverage.csv");


		LevelLogger.info("step 3: compute statements covered by failed tests");
		Set<String> allCoveredStatement = new HashSet<>();
		for(Entry<String, CoverInfo> entry : coverage.entrySet()){
			if(entry.getValue().getFailedCount() > 0){
				allCoveredStatement.add(entry.getKey());
			}
		}
		
		Identifier.backup(subject);
		
		LevelLogger.info("step 4: compute predicate coverage information");
		Map<String, CoverInfo> predicateCoverage = Coverage.computePredicateCoverage(subject, allCoveredStatement,
				failedTestsAndCoveredMethods.getFirst(), useStatisticalDebugging, useSober);

		if(predicateCoverage == null && !useSober){
			return false;
		}
		
		if (!useSober) {
			String predCoverageFile = useStatisticalDebugging ? "pred_coverage_sd.csv" : "pred_coverage.csv";
			LevelLogger.info("output predicate coverage information to file : " + predCoverageFile);
			printCoverage(predicateCoverage, subject.getCoverageInfoPath() + "/" + predCoverageFile);
		}

		LevelLogger.info("Compute suspicious for each statement and out put to file.");
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
		Suspicious.compute(subject, algorithms, totalFailed, totalTestNum - totalFailed, useStatisticalDebugging, useSober);
		
//		LevelLogger.info("step 5: combine all coverage informaiton");
//		for (Entry<String, CoverInfo> entry : predicateCoverage.entrySet()) {
//			CoverInfo coverInfo = coverage.get(entry.getKey());
//			if (coverInfo != null) {
//				coverInfo.combine(entry.getValue());
//			} else {
//				coverage.put(entry.getKey(), entry.getValue());
//			}
//		}
//
//		LevelLogger.info("step 6: output coverage information to file : coverage.csv");
//		printCoverage(coverage, Constant.STR_INFO_OUT_PATH + "/" + subject.getName() + "/" + subject.getName() + "_"
//				+ subject.getId() + "/coverage.csv");
		return true;
	}

	private static void backupSource(Subject subject){
		String src = subject.getHome() + subject.getSsrc();
		File file = new File(src + "_ori");
		if(file.exists()){
			ExecuteCommand.copyFolder(src + "_ori", src);
		} else {
			ExecuteCommand.copyFolder(src, src + "_ori");
		}
		String test = subject.getHome() + subject.getTsrc();
		File tfile = new File(test + "_ori");
		if(tfile.exists()){
			ExecuteCommand.copyFolder(test + "_ori", test);
		} else {
			ExecuteCommand.copyFolder(test, test + "_ori");
		}
	}
	
	private static void printCoverage(Map<String, CoverInfo> coverage, String filePath) {
		File file = new File(filePath);
		String header = "line\tfcover\tpcover\tf_observed_cover\tp_observed_cover\n";
		JavaFile.writeStringToFile(file, header, false);
		for (Entry<String, CoverInfo> entry : coverage.entrySet()) {
			StringBuffer stringBuffer = new StringBuffer();
			String key = entry.getKey();
			String[] info = key.split("#");
			String methodString = Identifier.getMessage(Integer.parseInt(info[0]));
			stringBuffer.append(methodString);
			String moreInfo = key.substring(info[0].length() + 1);
			stringBuffer.append("#");
			stringBuffer.append(moreInfo);
			
			stringBuffer.append("\t");
			stringBuffer.append(entry.getValue().getFailedCount());
			stringBuffer.append("\t");
			stringBuffer.append(entry.getValue().getPassedCount());
			stringBuffer.append("\t");
			stringBuffer.append(entry.getValue().getFailedObservedCount());
			stringBuffer.append("\t");
			stringBuffer.append(entry.getValue().getPassedObservedCount());
			stringBuffer.append("\n");
			// view coverage.csv file
			JavaFile.writeStringToFile(file, stringBuffer.toString(), true);
		}
	}
	
	public static void recordSubjects(List<Subject> subjects) {
		String content = "name\tid\n";
		for(Subject subject : subjects) {
			content += subject.getName() + "\t" + Integer.toString(subject.getId()) + "\n";
		}
		JavaFile.writeStringToFile(Constant.HOME + "/logs/project.log", content);
	}

	public static void main(String[] args) {
//
//		double [] prob = {1.0, 1.0, 1.0, 5.0, 1.0, 1.0}; // select more math project
//		List<Subject> allSubjects = ProjectSelector.randomSelect(prob, 80);
//		recordSubjects(allSubjects);
//		for(int i = 53; i < allSubjects.size(); i++) {
//			try {
		List<Subject> allSubjects = null;
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
		} else {
			allSubjects = ProjectSelector.select("math");
		}
		//for debug
		System.out.println("---------------------------------\n");
		System.out.print(allSubjects.get(0).getName() + " : ");
		for(Subject subject : allSubjects) {
			System.out.print(subject.getId() + ", ");
		}
		System.out.println("\n---------------------------------\n");
		
//		Subject subject = ProjectSelector.select("math", 4);
		for(Subject subject : allSubjects) {
			try {
//				File file = new File(subject.getCoverageInfoPath() + "/predicates_backup.txt");
//				if (!file.exists()) continue;
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy:MM:dd:HH:mm:ss");
				Date startTime = new Date();
				String begin = simpleDateFormat.format(startTime);
				LevelLogger.info("BEGIN : " + begin);

				String subjectInfo = subject.getName() + "_" + subject.getId();
				if (!proceed(subject, Constant.USE_STATISTICAL_DEBUGGING, Constant.USE_SOBER)) {
					String d4jOutput = JavaFile.readFileToString(Constant.STR_TMP_D4J_OUTPUT_FILE);
					JavaFile.writeStringToFile(Constant.STR_ERROR_BACK_UP + "/" + subjectInfo + ".d4j.out", d4jOutput);
				}

				Date endTime = new Date();
				String end = simpleDateFormat.format(endTime);
				LevelLogger.info("BEGIN : " + begin + " - END : " + end);
				JavaFile.writeStringToFile(Constant.TIME_LOG, subjectInfo + "\t"
						+ Long.toString(endTime.getTime() - startTime.getTime()) + "\t" + startTime + "\n", true);
			} catch (Exception e) {
				e.printStackTrace();
//				JavaFile.writeStringToFile(Constant.HOME + "/logs/" +
//						subject.getName() + "_" + Integer.toString(subject.getId())
//						+ "_error.log");
			}
		}
	}

}
