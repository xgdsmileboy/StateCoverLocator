/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import locator.core.Collector;
import locator.core.run.path.Coverage;
import locator.inst.Instrument;
import locator.inst.visitor.DeInstrumentVisitor;

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

		// train model
		try {
			LevelLogger.info(">>>>>> Begin Trainning ...");
			ExecuteCommand.executeTrain(subject);
			LevelLogger.info(">>>>>> End Trainning !");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void proceed() {

		List<Subject> allSubject = Configure.getSubjectFromXML(Constant.HOME + "/res/conf/project.xml");
		if (allSubject.size() < 1) {
			LevelLogger.error(__name__ + "#proceed no subjects !");
			return;
		}
		Subject subject = allSubject.get(0);
		// preprocess : remove all instrument
		DeInstrumentVisitor deInstrumentVisitor = new DeInstrumentVisitor();
		Instrument.execute(subject.getHome() + subject.getSsrc(), deInstrumentVisitor);
		Instrument.execute(subject.getHome() + subject.getTsrc(), deInstrumentVisitor);

		// train predicate prediction model
		trainModel(subject);

		// copy auxiliary file to subject path
		LevelLogger.info("copying auxiliary file to subject path.");
		Configure.config_dumper(subject);

		LevelLogger.info("step 1: collect all tests.");
		Pair<Set<Integer>, Set<Integer>> allTests = Collector.collectAllTestCases(subject);

		LevelLogger.info("step 2: compute original coverage information.");
		Map<String, CoverInfo> coverage = Coverage.computeCoverage(subject, allTests);

		LevelLogger.info("output original coverage information to file : ori_coverage.csv");
		printCoverage(coverage, Constant.STR_INFO_OUT_PATH + "/" + subject.getName() + "/" + subject.getName() + "_"
				+ subject.getId() + "/ori_coverage.csv");

		LevelLogger.info("step 3: compute statements covered by failed tests");
		Set<String> allCoveredStatement = Coverage.getAllCoveredStatement(subject, allTests.getFirst());

		LevelLogger.info("step 4: compute predicate coverage information");
		Map<String, CoverInfo> predicateCoverage = Coverage.computePredicateCoverage(subject, allCoveredStatement,
				allTests.getFirst());

		LevelLogger.info("output predicate coverage information to file : pred_coverage.csv");
		printCoverage(predicateCoverage, Constant.STR_INFO_OUT_PATH + "/" + subject.getName() + "/" + subject.getName()
				+ "_" + subject.getId() + "/pred_coverage.csv");

		LevelLogger.info("step 5: combine all coverage informaiton");
		for (Entry<String, CoverInfo> entry : predicateCoverage.entrySet()) {
			CoverInfo coverInfo = coverage.get(entry.getKey());
			if (coverInfo != null) {
				coverInfo.combine(entry.getValue());
			} else {
				coverage.put(entry.getKey(), entry.getValue());
			}
		}

		LevelLogger.info("step 6: output coverage information to file : coverage.csv");
		printCoverage(coverage, Constant.STR_INFO_OUT_PATH + "/" + subject.getName() + "/" + subject.getName() + "_"
				+ subject.getId() + "/coverage.csv");
	}

	private static void printCoverage(Map<String, CoverInfo> coverage, String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		for (Entry<String, CoverInfo> entry : coverage.entrySet()) {
			StringBuffer stringBuffer = new StringBuffer();
			String key = entry.getKey();
			String[] info = key.split("#");
			String methodString = Identifier.getMessage(Integer.parseInt(info[0]));
			stringBuffer.append(methodString);
			stringBuffer.append("#");
			stringBuffer.append(info[1]);
			stringBuffer.append("\t");
			stringBuffer.append(entry.getValue().getFailedCount());
			stringBuffer.append("\t");
			stringBuffer.append(entry.getValue().getPassedCount());
			stringBuffer.append("\n");
			// view coverage.csv file
			JavaFile.writeStringToFile(file, stringBuffer.toString(), true);
		}
	}

	public static void main(String[] args) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy:MM:dd:HH:mm:ss");
		String begin = simpleDateFormat.format(new Date());
		System.out.println("BEGIN : " + begin);

		Constant.PROJECT_HOME = "/home/jiajun/d4j/projects";

		proceed();

		String end = simpleDateFormat.format(new Date());
		System.out.println("BEGIN : " + begin + " - END : " + end);
	}

}
