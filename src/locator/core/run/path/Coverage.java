/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.run.path;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.CoverInfo;
import locator.common.java.Method;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.CmdFactory;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.core.Collector;
import locator.inst.Instrument;
import locator.inst.visitor.DeInstrumentVisitor;
import locator.inst.visitor.StatementInstrumentVisitor;

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

}
