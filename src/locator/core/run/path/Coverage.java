/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.run.path;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.CoverInfo;
import locator.common.java.JavaFile;
import locator.common.java.Method;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.CmdFactory;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.core.Collector;
import locator.core.run.Runner;
import locator.inst.Instrument;
import locator.inst.predict.Predictor;
import locator.inst.visitor.DeInstrumentVisitor;
import locator.inst.visitor.MethodInstrumentVisitor;
import locator.inst.visitor.PredicateInstrumentVisitor;
import locator.inst.visitor.StatementInstrumentVisitor;
import locator.inst.visitor.feature.FeatureExtraction;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class Coverage {

	private final static String __name__ = "@Coverage ";

	/**
	 * compute coverage information for each statement
	 * 
	 * @param subject
	 *            : subject to be computed
	 * @param allTests
	 *            : contains all test cases, the first set should be failed test
	 *            cases and the second set should be the passed test cases
	 * @return a map for each statement with its coverage information
	 */
	public static Map<String, CoverInfo> computeCoverage(Subject subject, Pair<Set<Integer>, Set<Integer>> allTests) {
		// compute path for failed test cases
		Set<Method> failedPath = Collector.collectCoveredMethod(subject, allTests.getFirst());

		// //TODO : for debugging
		// for(Method method : failedPath){
		// System.out.println(method);
		// }

		// initialize coverage information
		Map<String, CoverInfo> coverage = new HashMap<>();

		// instrument those methods ran by failed tests
		StatementInstrumentVisitor statementInstrumentVisitor = new StatementInstrumentVisitor(failedPath);
		Instrument.execute(subject.getHome() + subject.getSsrc(), statementInstrumentVisitor);

		// run all failed test
		for (Integer failedTestID : allTests.getFirst()) {
			String testString = Identifier.getMessage(failedTestID);

			System.out.println("failed test : " + testString);

			String[] testInfo = testString.split("#");
			if (testInfo.length < 4) {
				LevelLogger.error(__name__ + "#computeCoverage test format error : " + testString);
				System.exit(0);
			}
			String testcase = testInfo[0] + "::" + testInfo[2];
			// run each test case and collect all test statements covered
			try {
				ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSingleCmd(subject, testcase));
			} catch (Exception e) {
				LevelLogger.fatal(__name__ + "#computeCoverage run test suite failed !", e);
			}

			Map<String, Integer> tmpCover = ExecutionPathBuilder
					.collectAllExecutedStatements(Constant.STR_TMP_INSTR_OUTPUT_FILE);
			for (Entry<String, Integer> entry : tmpCover.entrySet()) {
				String statement = entry.getKey();
				Integer coverCount = entry.getValue();
				CoverInfo coverInfo = coverage.get(statement);
				if (coverInfo == null) {
					coverInfo = new CoverInfo();
					coverInfo.failedAdd(coverCount);
					coverage.put(statement, coverInfo);
				} else {
					coverInfo.failedAdd(coverCount);
				}
			}
		}

		// run all passed test
		for (Integer passTestID : allTests.getSecond()) {
			String testString = Identifier.getMessage(passTestID);

			System.out.println("passed test : " + testString);

			String[] testInfo = testString.split("#");
			if (testInfo.length < 4) {
				LevelLogger.error(__name__ + "#computeCoverage test format error : " + testString);
				System.exit(0);
			}
			String testcase = testInfo[0] + "::" + testInfo[2];
			// run each test case and collect all test statements covered
			try {
				ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSingleCmd(subject, testcase));
			} catch (Exception e) {
				LevelLogger.fatal(__name__ + "#computeCoverage run test suite failed !", e);
			}

			Map<String, Integer> tmpCover = ExecutionPathBuilder
					.collectAllExecutedStatements(Constant.STR_TMP_INSTR_OUTPUT_FILE);
			for (Entry<String, Integer> entry : tmpCover.entrySet()) {
				String statement = entry.getKey();
				Integer coverCount = entry.getValue();
				CoverInfo coverInfo = coverage.get(statement);
				if (coverInfo == null) {
					coverInfo = new CoverInfo();
					coverInfo.passedAdd(coverCount);
					coverage.put(statement, coverInfo);
				} else {
					coverInfo.passedAdd(coverCount);
				}
			}
		}

		Instrument.execute(subject.getHome() + subject.getSsrc(), new DeInstrumentVisitor());

		return coverage;
	}

	/**
	 * get all statements covered by given test cases
	 * 
	 * @param subject
	 *            : subject to be tested
	 * @param testcases
	 *            : test cases to be computed
	 * @return a set of statements covered by the given test cases
	 */
	public static Set<String> getAllCoveredStatement(Subject subject, Set<Integer> testcases) {
		Set<String> coveredStatement = new HashSet<>();
		// Set<Method> coveredMethod = Collector.collectCoveredMethod(subject,
		// testcases);

		// instrument those methods ran by failed tests
		// StatementInstrumentVisitor statementInstrumentVisitor = new
		// StatementInstrumentVisitor(coveredMethod);
		StatementInstrumentVisitor statementInstrumentVisitor = new StatementInstrumentVisitor();
		Instrument.execute(subject.getHome() + subject.getSsrc(), statementInstrumentVisitor);

		for (Integer testID : testcases) {
			String testString = Identifier.getMessage(testID);

			System.out.println("test : " + testString);

			String[] testInfo = testString.split("#");
			if (testInfo.length < 4) {
				LevelLogger.error(__name__ + "#getAllCoveredStatement test format error : " + testString);
				System.exit(0);
			}
			String testcase = testInfo[0] + "::" + testInfo[2];
			// run each test case and collect all test statements covered
			try {
				ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSingleCmd(subject, testcase));
			} catch (Exception e) {
				LevelLogger.fatal(__name__ + "#getAllCoveredStatement run test suite failed !", e);
			}

			Map<String, Integer> tmpCover = ExecutionPathBuilder
					.collectAllExecutedStatements(Constant.STR_TMP_INSTR_OUTPUT_FILE);
			for (Entry<String, Integer> entry : tmpCover.entrySet()) {
				String statement = entry.getKey();
				coveredStatement.add(statement);
			}
		}
		Instrument.execute(subject.getHome() + subject.getSsrc(), new DeInstrumentVisitor());
		return coveredStatement;
	}

	/**
	 * 
	 * @param subject
	 * @param allStatements
	 * @param failedTests
	 * @return <statementString, coverageInformation>,
	 *         statementString:"MethodID#line"
	 */
	public static Map<String, CoverInfo> computePredicateCoverage(Subject subject, List<String> allStatements,
			Set<Integer> failedTests) {
		Map<String, CoverInfo> coverage = new HashMap<>();
		String srcPath = subject.getHome() + subject.getSsrc();
		String testPath = subject.getHome() + subject.getTsrc();
		MethodInstrumentVisitor methodInstrumentVisitor = new MethodInstrumentVisitor();
		Instrument.execute(testPath, methodInstrumentVisitor);

		for (String stmt : allStatements) {
			String[] stmtInfo = stmt.split("#");
			if (stmtInfo.length != 2) {
				LevelLogger.error(__name__ + "#computePredicateCoverage statement parse error : " + stmt);
				System.exit(0);
			}
			Integer methodID = Integer.valueOf(stmtInfo[0]);
			int line = Integer.parseInt(stmtInfo[1]);
			String methodString = Identifier.getMessage(methodID);
			String[] methodInfo = methodString.split("#");
			if (methodInfo.length < 4) {
				LevelLogger.error(__name__ + "#computePredicateCoverage method info parse error : " + methodString);
				System.exit(0);
			}
			String clazz = methodInfo[0].replace(".", Constant.PATH_SEPARATOR);
			int index = clazz.indexOf("$");
			if (index > 0) {
				clazz = clazz.substring(0, index);
			}
			String relJavaPath = clazz + ".java";
			List<String> varFeatures = FeatureExtraction.extractVarFeature(srcPath, relJavaPath, line);
			List<String> expFeatures = FeatureExtraction.extractExpFeature(srcPath, relJavaPath, line);
			Pair<Set<String>, Set<String>> allConditions = Predictor.predict(subject, varFeatures, expFeatures);
			// TODO : currently, only instrument predicates for left variables
			Set<String> conditionsForRightVars = allConditions.getSecond();
			// if predicted conditions are not empty for right variables,
			// instrument each condition one by one and compute coverage
			// information for each predicate
			if (conditionsForRightVars != null) {
				String javaFile = srcPath + relJavaPath;
				// the source file will instrumented iteratively, before which
				// the original source file should be saved
				ExecuteCommand.copyFile(javaFile, javaFile + ".bak");
				PredicateInstrumentVisitor predicateInstrumentVisitor = new PredicateInstrumentVisitor(null, line);
				// read original file once
				String source = JavaFile.readFileToString(javaFile);
				for (String condition : conditionsForRightVars) {
					// instrument one condition statement into source file
					CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(source,
							ASTParser.K_COMPILATION_UNIT);
					predicateInstrumentVisitor.setCondition(condition);
					compilationUnit.accept(predicateInstrumentVisitor);
					JavaFile.writeStringToFile(javaFile, compilationUnit.toString());
					// if the instrumented project builds success, and the test
					// result is the same with original project
					if (Runner.testSuite(subject) && isSameTestResult(failedTests, Constant.STR_TMP_D4J_OUTPUT_FILE)) {
						// TODO : save current instrumented condition for future
						// study
						// collect predicate coverage information :
						// <TestMethodID, <StatementString, coveredTimes>>
						Map<Integer, Map<String, Integer>> coveInfo = ExecutionPathBuilder
								.collectPredicateCoverageInfo(Constant.STR_TMP_INSTR_OUTPUT_FILE);
						for (Entry<Integer, Map<String, Integer>> entry : coveInfo.entrySet()) {
							Integer currMethodID = entry.getKey();
							if (failedTests.contains(currMethodID)) {
								for (Entry<String, Integer> stmtCover : entry.getValue().entrySet()) {
									String statementString = stmtCover.getKey();
									CoverInfo coverInfo = coverage.get(statementString);
									if (coverInfo != null) {
										coverInfo.failedAdd(stmtCover.getValue());
									} else {
										coverInfo = new CoverInfo();
										coverInfo.failedAdd(stmtCover.getValue());
										coverage.put(statementString, coverInfo);
									}
								} // end of "for(Entry<String, Integer>
							} else {
								for (Entry<String, Integer> stmtCover : entry.getValue().entrySet()) {
									String statementString = stmtCover.getKey();
									CoverInfo coverInfo = coverage.get(statementString);
									if (coverInfo != null) {
										coverInfo.passedAdd(stmtCover.getValue());
									} else {
										coverInfo = new CoverInfo();
										coverInfo.passedAdd(stmtCover.getValue());
										coverage.put(statementString, coverInfo);
									}
								} // end of "for(Entry<String, Integer>
									// stmtCover : entry.getValue().entrySet())"
							} // end else

						} // end of "for(Entry<Integer, Map<String, Integer>>
					} // end of "Runner.testSuite"
				} // end of "condition"
					// restore original source file
				ExecuteCommand.moveFile(javaFile + ".bak", javaFile);
			} // end of "conditionsForRightVars != null"

		} // end of "for(String stmt : allStatements)"

		Instrument.execute(testPath, new DeInstrumentVisitor());
		return coverage;
	}

	/**
	 * test result should have the same failed test cases with the given failed
	 * test cases
	 * 
	 * @param failedTest
	 * @param outputFile
	 * @return
	 */
	private static boolean isSameTestResult(Set<Integer> failedTest, String outputFile) {
		Set<Integer> realFailedTests = Collector.findFailedTestFromFile(outputFile);
		if (realFailedTests.size() != failedTest.size()) {
			return false;
		}
		for (Integer fail : realFailedTests) {
			if (!failedTest.contains(fail)) {
				return false;
			}
		}
		return true;
	}

}
