/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.CmdFactory;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;

/**
 * @author Jiajun
 * @date May 9, 2017
 */
public class Collector {

	private final static String __name__ = "@Collector ";
	
	public static Pair<Set<Integer>, Set<Integer>> collectAllTestCases(Subject subject){
		Pair<Set<Integer>, Set<Integer>> allTests = new Pair<>();
		//run all test
		try {
			ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSuiteCmd(subject));
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#collectAllTestCases run test failed !", e);
			return null;
		} 
		Set<Integer> failedTest = findFailedTestFromFile(Constant.STR_TMP_D4J_OUTPUT_FILE);
		Set<Integer> passedTest = collectAllPassedTestCases(subject, failedTest);
		
		allTests.setFirst(failedTest);
		allTests.setSecond(passedTest);
		
		return allTests;
	}
	
	private static Set<Integer> collectAllPassedTestCases(Subject subject, Set<Integer> failedTests){
		Set<Integer> allPassedTestCases = new HashSet<>();
		
		File file = new File(subject.getHome() + "/all-tests.txt");
		if(!file.exists()){
			LevelLogger.error(__name__ + "#collectAllPassedTestCases file : " + file.getAbsolutePath() +" does not exist !");
			return allPassedTestCases;
		}
		
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			LevelLogger.error(__name__ + "#collectAllPassedTestCases open file failed !", e);
			return allPassedTestCases;
		}
		
		String line = null;
		try {
			while((line = bReader.readLine()) != null){
				int indexOfLeftBracket = line.indexOf("(");
				int indexOfRightBracket = line.indexOf(")");
				if(indexOfLeftBracket < 0 || indexOfRightBracket < 0){
					LevelLogger.warn(__name__ + "#collectAllPassedTestCases find bracket failed !");
					continue;
				}
				String methodName = line.substring(0, indexOfLeftBracket);
				String clazzName = line.substring(indexOfLeftBracket + 1, indexOfRightBracket);
				String methodString = clazzName + "#void#" + methodName + "#?";
				int methodID = Identifier.getIdentifier(methodString);
				if(!failedTests.contains(methodID)){
					allPassedTestCases.add(methodID);
				}
			}
		} catch (IOException e) {
			LevelLogger.fatal(__name__ + "#collectAllPassedTestCases read file error !", e);
		}
		return allPassedTestCases;
	}
	
	
	private static Set<Integer> findFailedTestFromFile(String outputFilePath) {
		if (outputFilePath == null) {
			LevelLogger.error(__name__ + "#findFailedTestFromFile OutputFilePath is null.");
			return null;
		}
		File file = new File(outputFilePath);
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			LevelLogger.error(__name__ + "@findFailedTestFromFile BufferedReader init failed.");
			return null;
		}

		String line = null;
		Set<Integer> failedTest = new HashSet<>();
		try {
			while ((line = bReader.readLine()) != null) {
				String trimed = line.trim();
				if (trimed.startsWith(Constant.ANT_BUILD_FAILED)) {
					LevelLogger.error(__name__ + "#findFailedTestFromFile Ant build failed.");
					break;
				}
				if (trimed.startsWith("Failing tests:")) {
					String count = trimed.substring("Failing tests:".length());
					int failingCount = Integer.parseInt(count.trim());
					while (failingCount > 0) {
						line = bReader.readLine();
						int index = line.indexOf("-");
						if (index > 0) {
							String testStr = line.substring(index + 2).trim();
							String[] testInfo = testStr.split("::");
							if(testInfo.length != 2){
								LevelLogger.error(__name__ + "#findFailedTestFromFile Failed test cases format error !");
								System.exit(0);
							}
							//convert "org.jfree.chart.Clazz::test" to "org.jfree.chart.Clazz#void#test#?"
							String identifierString = testInfo[0] + "#void#" + testInfo[1] + "#?";
							int methodID = Identifier.getIdentifier(identifierString);
							failedTest.add(methodID);
						}
						failingCount--;
					}
				}
			}
			bReader.close();
		} catch (IOException e) {
			LevelLogger.fatal(__name__ + "#findFailedTestFromFile Read line from file failed.", e);
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (IOException e) {
				}
			}
		}
		return failedTest;
	}
	
}
