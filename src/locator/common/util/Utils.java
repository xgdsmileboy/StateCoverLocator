/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.JavaFile;
import locator.common.java.Subject;
import locator.core.CoverInfo;

/**
 * @author Jiajun
 * @date Dec 19, 2017
 */
public class Utils {

	public static void pathGuarantee(String ... paths) {
		File file = null;
		for(String string : paths) {
			file = new File(string);
			if(!file.exists()) {
				file.mkdirs();
			}
		}
	}
	
	public static void backupSource(Subject subject){
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
	
	public static void printCoverage(Map<String, CoverInfo> coverage, String filePath, String name) {
		Utils.pathGuarantee(filePath);
		File file = new File(filePath + "/" + name);
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
	
	public static void backupFailedTestsAndCoveredStmt(Subject subject, int totalTestNumber, Set<Integer> failedTest, Set<String> coveredStmt) {
		LevelLogger.debug("\n------------Begin backup failed tests & covered statement----------\n");
		StringBuffer buffer = new StringBuffer(totalTestNumber + ":" + failedTest.size());
		for(Integer integer : failedTest) {
			buffer.append("\n" + integer);
		}
		String fileName = Constant.STR_INFO_OUT_PATH + "/" + subject.getName() + "/" + subject.getNameAndId() + "/failedTest.txt" ;
		JavaFile.writeStringToFile(fileName, buffer.toString());
		
		buffer = new StringBuffer();
		for(String string : coveredStmt) {
			buffer.append(string + "\n");
		}
		fileName = Constant.STR_INFO_OUT_PATH + "/" + subject.getName() + "/" + subject.getNameAndId() + "/coveredstmt.txt" ;
		JavaFile.writeStringToFile(fileName, buffer.toString().trim());
	}
	
	public static int recoverFailedTestsAndCoveredStmt(Subject subject, Set<Integer> failedTests, Set<String> allCoveredStmt) {
		LevelLogger.debug("\n------------Begin recover failed tests & covered statement----------\n");
		String fileName = Constant.STR_INFO_OUT_PATH + "/" + subject.getName() + "/" + subject.getNameAndId() + "/failedTest.txt" ;
		List<String> content = JavaFile.readFileToStringList(fileName);
		boolean containIllegal = false;
		int totalNumberOfTests = -1;
		int failedTestsNumber = -1;
		if(content.size() > 0) {
			String numbers = content.get(0);
			try {
				totalNumberOfTests = Integer.parseInt(numbers.split(":")[0]);
				failedTestsNumber = Integer.parseInt(numbers.split(":")[1]);
			} catch (Exception e) {}
			for(int index = 1; index < content.size(); index ++) {
				String string = content.get(index);
				if(!Identifier.containsKey(string)) {
					containIllegal = true;
					break;
				}
				failedTests.add(Identifier.getIdentifier(string));
			}
		}
		if(containIllegal || failedTests.size() != failedTestsNumber) {
			JavaFile.writeStringToFile(fileName, "");
			return -1;
		}
		
		fileName = Constant.STR_INFO_OUT_PATH + "/" + subject.getName() + "/" + subject.getNameAndId() + "/coveredstmt.txt" ;
		allCoveredStmt.addAll(JavaFile.readFileToStringList(fileName));
		containIllegal = false;
		try {
			for(String string : allCoveredStmt) {
				String[] strs = string.split("#");
				Integer integer = Integer.parseInt(strs[0]);
				if(!Identifier.containKey(integer)) {
					containIllegal = true;
					break;
				}
			}
		} catch (Exception e) {
		}
		
		if(containIllegal || allCoveredStmt.size() == 0) {
			JavaFile.writeStringToFile(fileName, "");
			return -1;
		}
		
		return totalNumberOfTests;
	}
	
	public static void printPredicateInfo(Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates,
			Subject subject, String backupFile) {
		LevelLogger.debug("\n------------------------begin predicate info------------------------\n");

		StringBuffer contents = new StringBuffer();
		for (Entry<String, Map<Integer, List<Pair<String, String>>>> entry : file2Line2Predicates.entrySet()) {
			LevelLogger.debug("FILE NAME : " + entry.getKey());
			for (Entry<Integer, List<Pair<String, String>>> line2Preds : entry.getValue().entrySet()) {
				LevelLogger.debug("LINE : " + line2Preds.getKey());
				LevelLogger.debug("PREDICATES : ");
				for (Pair<String, String> pair : line2Preds.getValue()) {
					LevelLogger.debug(pair.getFirst() + ",");
					contents.append(entry.getKey() + "\t" + line2Preds.getKey() + "\t" + pair.getFirst() + "\t"
							+ pair.getSecond());
					contents.append("\n");
				}
				LevelLogger.debug("\n");
			}
		}
		LevelLogger.debug("\n------------------------end predicate info------------------------\n");
		String outputFile = subject.getCoverageInfoPath() + "/" + backupFile;
		JavaFile.writeStringToFile(outputFile, contents.toString(), true);
	}

	public static Map<String, Map<Integer, List<Pair<String, String>>>> recoverPredicates(Subject subject, String backupFile) {
		Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates = new HashMap<>();
		String inputFile = subject.getCoverageInfoPath() + "/" + backupFile;
		File file = new File(inputFile);
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		LevelLogger.info("Recover predicates from file.");
		List<String> contents = JavaFile.readFileToStringList(file);
		for (String content : contents) {
			if (contents.isEmpty()) {
				continue;
			}
			String parts[] = content.split("\t");
			// fix bug: predicates may contain anonymous class
			if (parts.length != 4) {
				continue;
			}
			Map<Integer, List<Pair<String, String>>> line2Predicates = file2Line2Predicates.get(parts[0]);
			if (line2Predicates == null) {
				line2Predicates = new HashMap<>();
				file2Line2Predicates.put(parts[0], line2Predicates);
			}
			List<Pair<String, String>> predicates = line2Predicates.get(Integer.valueOf(parts[1]));
			if (predicates == null) {
				predicates = new ArrayList<>();
				line2Predicates.put(Integer.valueOf(parts[1]), predicates);
			}
			predicates.add(new Pair<String, String>(parts[2], parts[3]));
		}
		return file2Line2Predicates;
	}
	
	/**
	 * transform time format
	 * 
	 * @param milliSecond
	 *            : milliseconds
	 * @return formated time string i.e., min:sec:milli
	 */
	public static String transformMilli2Time(long milliSecond) {
		long milli = milliSecond % 1000;
		milliSecond /= 1000;
		long second = milliSecond % 60;
		milliSecond /= 60;
		long minutes = milliSecond;
		return minutes + ":" + second + ":" + milli;
	}

	
	public static void recordSubjects(List<Subject> subjects) {
		String content = "name\tid\n";
		for(Subject subject : subjects) {
			content += subject.getName() + "\t" + Integer.toString(subject.getId()) + "\n";
		}
		JavaFile.writeStringToFile(Constant.HOME + "/logs/project.log", content);
	}
	
}
