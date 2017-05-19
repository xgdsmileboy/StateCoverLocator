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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;

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
import soot.coffi.constant_element_value;

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
		// run all test
		try {
			ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSuiteCmd(subject));
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#collectAllTestCases run test failed !", e);
			return null;
		}
		
		Set<Integer> failedTest = findFailedTestFromFile(Constant.STR_TMP_D4J_OUTPUT_FILE);
		String srcPath = subject.getHome() + subject.getSsrc();
		String tsrPath = subject.getHome() + subject.getTsrc();
		Set<Method> coveredMethods = collectCoveredMethod(subject, failedTest);
		
		MethodInstrumentVisitor methodInstrumentVisitor = new MethodInstrumentVisitor(coveredMethods);
		Instrument.execute(srcPath, methodInstrumentVisitor);
		
		// test case instrument
		Instrument.execute(tsrPath,
				new MethodInstrumentVisitor(Constant.INSTRUMENT_K_TEST));
		// run all test
		try {
			ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSuiteCmd(subject));
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#collectAllTestCases run test failed !", e);
			return null;
		}
		Instrument.execute(tsrPath, new DeInstrumentVisitor());
		Instrument.execute(srcPath, new DeInstrumentVisitor());

		
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
				if(methodInfo[0].endsWith(Constant.INSTRUMENT_K_TEST)){
					if(line != null && line.startsWith(sourceFlag)){
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
	
//	public static Map<Integer, Map<Integer, Set<String>>> collectAllPredicates(Subject subject, List<String> statements){
//		Map<Integer, Map<Integer, Set<String>>> allPredicates = new HashMap<>();
//		String srcPath = subject.getHome() + subject.getSsrc();
//		for(String stmt : statements){
//			String[] stmtInfo = stmt.split("#");
//			if(stmtInfo.length != 2){
//				LevelLogger.error(__name__ + "#collectAllPredicates statement information error : " + stmt);
//				System.exit(1);
//			}
//			int methodID = Integer.parseInt(stmtInfo[0]);
//			int line = Integer.parseInt(stmtInfo[1]);
//			String methodString = Identifier.getMessage(methodID);
//			
//		}
//		return allPredicates;
//	}
//	
//	private static class VarCollectorVisitor extends ASTVisitor{
//		private String _leftVarName = null;
//		private Set<String> _rightVarNames = null;
//		
//		public VarCollectorVisitor(){
//			_leftVarName = null;
//			_rightVarNames = new HashSet<>();
//		}
//		
//		public String getleftVarName(){
//			return _leftVarName;
//		}
//		
//		public Set<String> getRightVarNames(){
//			return _rightVarNames;
//		}
//	}
}
