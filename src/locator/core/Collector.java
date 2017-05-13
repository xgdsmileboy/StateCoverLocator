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
import java.util.Map;
import java.util.Set;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.Method;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.CmdFactory;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.core.run.Runner;
import locator.core.run.path.ExecutionPathBuilder;
import locator.inst.Instrument;
import locator.inst.visitor.DeInstrumentVisitor;
import locator.inst.visitor.MethodInstrumentVisitor;

/**
 * @author Jiajun
 * @date May 9, 2017
 */
public class Collector {

	private final static String __name__ = "@Collector ";

	/**
	 * collect all failed test cases and passed test cases
	 * 
	 * @param subject
	 *            : current considered subject
	 * @return a pair of test cases, the first element is a set of failed test
	 *         cases and the second element is a set of passed test cases
	 */
	public static Pair<Set<Integer>, Set<Integer>> collectAllTestCases(Subject subject) {
		Pair<Set<Integer>, Set<Integer>> allTests = new Pair<>();
		// test case instrument
		Instrument.execute(subject.getHome() + subject.getTsrc(),
				new MethodInstrumentVisitor(Constant.INSTRUMENT_K_TEST));
		// run all test
		try {
			ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSuiteCmd(subject));
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#collectAllTestCases run test failed !", e);
			return null;
		}
		Instrument.execute(subject.getHome() + subject.getTsrc(), new DeInstrumentVisitor());

		Set<Integer> failedTest = findFailedTestFromFile(Constant.STR_TMP_D4J_OUTPUT_FILE);
		Set<Integer> passedTest = collectAllPassedTestCases(Constant.STR_TMP_INSTR_OUTPUT_FILE, failedTest);
		// Set<Integer> passedTest = collectAllPassedTestCases(subject.getHome()
		// + "/all-tests.txt", failedTest);

		allTests.setFirst(failedTest);
		allTests.setSecond(passedTest);

		return allTests;
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
	private static Set<Integer> collectAllPassedTestCases(String outputFile, Set<Integer> failedTests) {
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

		String line = null;
		try {
			while ((line = bReader.readLine()) != null) {
				String[] methodInfo = line.split("#");
				if (methodInfo.length < 3) {
					LevelLogger.error(__name__ + "collectAllPassedTestCases output format error : " + line);
				}
				int methodID = Integer.parseInt(methodInfo[1]);
				if (!failedTests.contains(methodID)) {
					allPassedTestCases.add(methodID);
				}
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

	// /**
	// * collect all passed test cases by parsing the test output record file,
	// * during which excluding failed tests
	// *
	// * @param recordFilePath
	// * : file path containing all test information
	// * @param failedTests
	// * : filtered failed test cases
	// * @return
	// */
	// private static Set<Integer> collectAllPassedTestCases(String
	// recordFilePath, Set<Integer> failedTests) {
	// Set<Integer> allPassedTestCases = new HashSet<>();
	//
	// File file = new File(recordFilePath);
	// if (!file.exists()) {
	// LevelLogger.error(
	// __name__ + "#collectAllPassedTestCases file : " + file.getAbsolutePath()
	// + " does not exist !");
	// return allPassedTestCases;
	// }
	//
	// BufferedReader bReader = null;
	// try {
	// bReader = new BufferedReader(new FileReader(file));
	// } catch (FileNotFoundException e) {
	// LevelLogger.error(__name__ + "#collectAllPassedTestCases open file failed
	// !", e);
	// return allPassedTestCases;
	// }
	//
	// String line = null;
	// try {
	// while ((line = bReader.readLine()) != null) {
	// int indexOfLeftBracket = line.indexOf("(");
	// int indexOfRightBracket = line.indexOf(")");
	// if (indexOfLeftBracket < 0 || indexOfRightBracket < 0) {
	// LevelLogger.warn(__name__ + "#collectAllPassedTestCases find bracket
	// failed !");
	// continue;
	// }
	// String methodName = line.substring(0, indexOfLeftBracket);
	// String clazzName = line.substring(indexOfLeftBracket + 1,
	// indexOfRightBracket);
	// String methodString = clazzName + "#void#" + methodName + "#?";
	// int methodID = Identifier.getIdentifier(methodString);
	// if (!failedTests.contains(methodID)) {
	// allPassedTestCases.add(methodID);
	// }
	// }
	// bReader.close();
	// } catch (IOException e) {
	// LevelLogger.fatal(__name__ + "#collectAllPassedTestCases read file error
	// !", e);
	// } finally {
	// if(bReader != null){
	// try {
	// bReader.close();
	// } catch (IOException e) {
	// }
	// }
	// }
	// return allPassedTestCases;
	// }

	/**
	 * collect all failed test cases by parsing the d4j output information
	 * 
	 * @param outputFilePath
	 *            : defects4j output file path
	 * @return a set of method ids of failed test cases
	 */
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
	public static Set<Method> collectCoveredMethod(Subject subject, Set<Integer> testcases) {
		MethodInstrumentVisitor methodInstrumentVisitor = new MethodInstrumentVisitor();
		String subjectSourcePath = subject.getHome() + subject.getSsrc();
		Instrument.execute(subjectSourcePath, methodInstrumentVisitor);
		Set<Method> allMethods = new HashSet<>();
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
			Map<Method, Integer> pathMap = ExecutionPathBuilder
					.collectAllExecutedMethods(Constant.STR_TMP_INSTR_OUTPUT_FILE);

			if (pathMap != null) {
				allMethods.addAll(pathMap.keySet());
			}
		}

		Instrument.execute(subjectSourcePath, new DeInstrumentVisitor());
		return allMethods;
	}
}
