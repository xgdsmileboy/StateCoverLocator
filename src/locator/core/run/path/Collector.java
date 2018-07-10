/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.run.path;

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
import locator.core.run.Runner;
import locator.inst.Instrument;
import locator.inst.visitor.MethodInstrumentVisitor;
import locator.inst.visitor.NewTestMethodInstrumentVisitor;

/**
 * @author Jiajun
 * @date May 9, 2017
 */
public class Collector {

	private final static String __name__ = "@Collector ";

	/**
	 * collect the failed test cases and the methods covered by them
	 * @param subject : the subject to be tested
	 * @return a pair that contains the <p>ids of failted test cases</p> and 
	 * the <p>ids of covered methods</p> 
	 */
	public static Pair<Set<Integer>, Set<Integer>> collectFailedTestAndCoveredMethod(Subject subject){
		// run all test
		try {
			ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSuiteCmd(subject));
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#collectAllTestCases run test failed !", e);
			return null;
		}
		Set<Integer> failedTest = findFailedTestFromFile(Constant.STR_TMP_D4J_OUTPUT_FILE);
		ExecuteCommand.copyFile(Constant.STR_TMP_D4J_OUTPUT_FILE, Constant.STR_INFO_OUT_PATH + "/" + subject.getName()
				+ "/" + subject.getNameAndId() + "_original_test.log");
		Set<Integer> coveredMethods = collectCoveredMethod(subject, failedTest);
		return new Pair<Set<Integer>, Set<Integer>>(failedTest, coveredMethods);
	}
	
	/**
	 * collect all passed test cases based on the output info, before which the
	 * test cases should be instrumented and we should run all the test cases
	 * 
	 * @param outputFile
	 *            : file containing the instrument information
	 * @param failedTests
	 *            : failed test cases to be filtered
	 * @return a set of method ids of passed test cases
	 */
	public static Set<Integer> collectAllPassedTestCases(String outputFile, Set<Integer> failedTests) {
		Set<Integer> allPassedTestCases = new HashSet<>();
		File file = new File(outputFile);
		if (!file.exists()) {
			LevelLogger.error(
					__name__ + "#collectAllPassedTestCases file : " + file.getAbsolutePath() + " does not exist !");
			return allPassedTestCases;
		}

		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			LevelLogger.error(__name__ + "#collectAllPassedTestCases open file failed !", e);
			return allPassedTestCases;
		}

		String lastLine = null;
		String line = null;
		String sourceFlag = Constant.INSTRUMENT_FLAG + Constant.INSTRUMENT_K_SOURCE;
		try {
			line = bReader.readLine();
			lastLine = line;
			while (lastLine != null) {
				line = bReader.readLine();
				String[] methodInfo = lastLine.split("#");
				if (methodInfo.length < 3) {
					LevelLogger.error(__name__ + "collectAllPassedTestCases output format error : " + line);
				}
				if (methodInfo[0].endsWith(Constant.INSTRUMENT_K_TEST)) {
					if (line != null && line.startsWith(sourceFlag)) {
						int methodID = Integer.parseInt(methodInfo[1]);
						if (!failedTests.contains(methodID)) {
							allPassedTestCases.add(methodID);
						}
					}
				}
				lastLine = line;
			}
			bReader.close();
		} catch (IOException e) {
			LevelLogger.fatal(__name__ + "#collectAllPassedTestCases read file error !", e);
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (IOException e) {
				}
			}
		}
		return allPassedTestCases;
	}

	/**
	 * collect all failed test cases by parsing the d4j output information
	 * 
	 * @param outputFilePath
	 *            : defects4j output file path
	 * @return a set of method ids of failed test cases
	 */
	public static Set<Integer> findFailedTestFromFile(String outputFilePath) {
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
							if (testInfo.length != 2) {
								LevelLogger
										.error(__name__ + "#findFailedTestFromFile Failed test cases format error !");
								System.exit(0);
							}
							// convert "org.jfree.chart.Clazz::test" to
							// "org.jfree.chart.Clazz#void#test#?"
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

	/**
	 * collect all executed methods for given test cases {@code testcases}
	 * 
	 * @param subject
	 *            : current subject, e.g., chart_1_buggy
	 * @param testcases
	 *            :a set of method ids of test cases to be collected
	 * @return a set of method ids covered by the given test cases
	 */
	public static Set<Integer> collectCoveredMethod(Subject subject, Set<Integer> testcases) {
		MethodInstrumentVisitor methodInstrumentVisitor = new MethodInstrumentVisitor();
		String subjectSourcePath = subject.getHome() + subject.getSsrc();
		Instrument.execute(subjectSourcePath, methodInstrumentVisitor);
		NewTestMethodInstrumentVisitor newTestMethodInstrumentVisitor = new NewTestMethodInstrumentVisitor(testcases, false);
		String subjectTestPath = subject.getHome() + subject.getTsrc();
		Instrument.execute(subjectTestPath, newTestMethodInstrumentVisitor);
		Set<Integer> allMethods = new HashSet<>();
		for (Integer methodID : testcases) {
			String methodString = Identifier.getMessage(methodID);
			String[] methodInfo = methodString.split("#");
			if (methodInfo.length < 4) {
				LevelLogger.error(__name__ + "#collectCoveredMethod method string format error : " + methodString);
				System.exit(1);
			}
			String clazzpath = methodInfo[0];
			String methodName = methodInfo[2];
			String singleTest = clazzpath + "::" + methodName;
			boolean success = Runner.testSingleCase(subject, singleTest);
			if (!success) {
				LevelLogger
						.error(__name__ + "#collectCoveredMethod build subject failed when running single test case.");
				System.exit(0);
			}
			Set<Integer> coveredMethodes = ExecutionPathBuilder
					.collectAllExecutedMethods(Constant.STR_TMP_INSTR_OUTPUT_FILE);
			
			if (coveredMethodes != null) {
				allMethods.addAll(coveredMethodes);
			}
		}

		ExecuteCommand.copyFolder(subjectSourcePath + "_ori", subjectSourcePath);
		ExecuteCommand.copyFolder(subjectTestPath + "_ori", subjectTestPath);
		return allMethods;
	}

}
