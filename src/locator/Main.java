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
import locator.core.Suspicious;
import locator.core.Tarantula;
import locator.core.run.Runner;
import locator.core.run.path.Collector;
import locator.core.run.path.Coverage;
import locator.core.run.path.ExecutionPathBuilder;
import locator.inst.Instrument;
import locator.inst.visitor.DeInstrumentVisitor;
import sun.security.x509.AlgorithmId;

public class Main {

	private final static String __name__ = "@Main ";

	private static void trainModel(Subject subject) {
		String modelPath = Constant.STR_ML_HOME + "/model/";
		File varModel = new File(modelPath + subject.getName() + "_" + subject.getId() + ".var_model.pkl");
		File exprModel = new File(modelPath + subject.getName() + "_" + subject.getId() + ".expr_model.pkl");
		if (varModel.exists() && exprModel.exists()) {
			LevelLogger.info("Models are already exist and will be used directly !");
			return;
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

		// train model
		try {
			LevelLogger.info(">>>>>> Begin Trainning ...");
			ExecuteCommand.executeTrain(subject);
			LevelLogger.info(">>>>>> End Trainning !");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void proceed(Subject subject) {
		
		LevelLogger.info("------------------ Begin : " + subject.getName() + "_" + subject.getId() + " ----------------");
		
		// remove auxiliary file
		String path = subject.getHome() + subject.getSsrc();
		String auxiliary = path + Constant.PATH_SEPARATOR + "auxiliary/Dumper.java";
		
		ExecuteCommand.deleteGivenFile(auxiliary);
		// preprocess : remove all instrument
		DeInstrumentVisitor deInstrumentVisitor = new DeInstrumentVisitor();
		Instrument.execute(subject.getHome() + subject.getSsrc(), deInstrumentVisitor);
		Instrument.execute(subject.getHome() + subject.getTsrc(), deInstrumentVisitor);

		// train predicate prediction model
		trainModel(subject);

		// copy auxiliary file to subject path
		LevelLogger.info("copying auxiliary file to subject path.");
		Configure.config_dumper(subject);

		LevelLogger.info("step 1: collect failed test and covered methods.");
		String testsPath = subject.getHome() + "/all-tests.txt";
		ExecuteCommand.deleteGivenFile(testsPath);
		Pair<Set<Integer>, Set<Integer>> failedTestsAndCoveredMethods = Collector.collectFailedTestAndCoveredMethod(subject);
		int totalFailed = failedTestsAndCoveredMethods.getFirst().size();
		int totalTestNum = JavaFile.readFileToStringList(testsPath).size();
		
		LevelLogger.info("step 2: compute original coverage information.");
		Map<String, CoverInfo> coverage = Coverage.computeOriginalCoverage(subject, failedTestsAndCoveredMethods);
		
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
		
		LevelLogger.info("step 4: compute predicate coverage information");
		Map<String, CoverInfo> predicateCoverage = Coverage.computePredicateCoverage(subject, allCoveredStatement,
				failedTestsAndCoveredMethods.getFirst());

		LevelLogger.info("output predicate coverage information to file : pred_coverage.csv");
		printCoverage(predicateCoverage, subject.getCoverageInfoPath() + "/pred_coverage.csv");

		LevelLogger.info("Compute suspicious for each statement and out put to file.");
		List<Algorithm> algorithms = new ArrayList<>();
		// add different computation algorithms
		algorithms.add(new Ochiai());
		algorithms.add(new Tarantula());
		algorithms.add(new DStar());
		algorithms.add(new Barinel());
		algorithms.add(new Op2());
		Suspicious.compute(subject, algorithms, totalFailed, totalTestNum - totalFailed);
		
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
	}

	private static void printCoverage(Map<String, CoverInfo> coverage, String filePath) {
		File file = new File(filePath);
		String header = "line" + "\t" + "fcover" + "\t" + "pcover" + "\n";
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

		double [] prob = {1.0, 1.0, 1.0, 5.0, 1.0, 1.0}; // select more math project
		List<Subject> allSubjects = ProjectSelector.randomSelect(prob, 30);
		recordSubjects(allSubjects);
		for(Subject subject: allSubjects) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy:MM:dd:HH:mm:ss");
			String begin = simpleDateFormat.format(new Date());
			LevelLogger.info("BEGIN : " + begin);

			Constant.PROJECT_HOME = "/home/jiajun/d4j/projects";

			proceed(subject);

			String end = simpleDateFormat.format(new Date());
			LevelLogger.info("BEGIN : " + begin + " - END : " + end);
		}
	}

}
