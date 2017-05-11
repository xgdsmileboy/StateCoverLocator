/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.run.path;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.google.common.base.Utf8;

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
import locator.inst.visitor.StatementInstrumentVisitor;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class Coverage {
	
	private final static String __name__ = "@Coverage ";
	
	public static Map<String, CoverInfo> computeCoverage(Subject subject, Pair<Set<Integer>, Set<Integer>> allTests){
		// compute path for failed test cases
		Set<Method> failedPath = Collector.collectRunningMethod(subject, allTests.getFirst());
		
//		//TODO : for debugging
//		for(Method method : failedPath){
//			System.out.println(method);
//		}
		
		// initialize coverage information
		Map<String, CoverInfo> coverage = new HashMap<>();
		
		//instrument those methods ran by failed tests
		StatementInstrumentVisitor statementInstrumentVisitor = new StatementInstrumentVisitor(failedPath);
		Instrument.execute(subject.getHome() + subject.getSsrc(), statementInstrumentVisitor);
		
		//run all failed test
		for(Integer failedTestID : allTests.getFirst()){
			String testString = Identifier.getMessage(failedTestID);
			
			System.out.println("failed test : " + testString);
			
			String[] testInfo = testString.split("#");
			if(testInfo.length < 4){
				LevelLogger.error(__name__ + "#computeCoverage test format error : " + testString);
				System.exit(0);
			}
			String testcase = testInfo[0] + "::" + testInfo[2];
			//run each test case and collect all test statements covered
			try {
				ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSingleCmd(subject, testcase));
			} catch (Exception e) {
				LevelLogger.fatal(__name__ + "#computeCoverage run test suite failed !", e);
			}
			
			Map<String, Integer> tmpCover = ExecutionPathBuilder.collectAllExecutedStatements(Constant.STR_TMP_INSTR_OUTPUT_FILE);
			for(Entry<String, Integer> entry : tmpCover.entrySet()){
				String statement = entry.getKey();
				Integer coverCount = entry.getValue();
				CoverInfo coverInfo = coverage.get(statement);
				if(coverInfo == null){
					coverInfo= new CoverInfo();
					coverInfo.failedAdd(coverCount);
					coverage.put(statement, coverInfo);
				} else{
					coverInfo.failedAdd(coverCount);
				}
			}
		}
		
		//run all passed test
		for(Integer passTestID : allTests.getSecond()){
			String testString = Identifier.getMessage(passTestID);
			
			System.out.println("passed test : " + testString);
			
			String[] testInfo = testString.split("#");
			if(testInfo.length < 4){
				LevelLogger.error(__name__ + "#computeCoverage test format error : " + testString);
				System.exit(0);
			}
			String testcase = testInfo[0] + "::" + testInfo[2];
			//run each test case and collect all test statements covered
			try {
				ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSingleCmd(subject, testcase));
			} catch (Exception e) {
				LevelLogger.fatal(__name__ + "#computeCoverage run test suite failed !", e);
			}
			
			Map<String, Integer> tmpCover = ExecutionPathBuilder.collectAllExecutedStatements(Constant.STR_TMP_INSTR_OUTPUT_FILE);
			for(Entry<String, Integer> entry : tmpCover.entrySet()){
				String statement = entry.getKey();
				Integer coverCount = entry.getValue();
				CoverInfo coverInfo = coverage.get(statement);
				if(coverInfo == null){
					coverInfo = new CoverInfo();
					coverInfo.passedAdd(coverCount);
					coverage.put(statement, coverInfo);
				}else{
					coverInfo.passedAdd(coverCount);
				}
			}
		}
		
		return coverage;
	}
	
}
