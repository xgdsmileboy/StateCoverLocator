/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.run.path;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Condition;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.sun.corba.se.impl.orbutil.DenseIntMapImpl;
import com.sun.org.apache.bcel.internal.generic.LNEG;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath.Step;

import java_cup.terminal;
import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.CoverInfo;
import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.CmdFactory;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.core.run.Runner;
import locator.inst.Instrument;
import locator.inst.predict.Predictor;
import locator.inst.visitor.DeInstrumentVisitor;
import locator.inst.visitor.MultiLinePredicateInstrumentVisitor;
import locator.inst.visitor.NewPredicateInstrumentVisitor;
import locator.inst.visitor.NewTestMethodInstrumentVisitor;
import locator.inst.visitor.StatementInstrumentVisitor;
import locator.inst.visitor.feature.ExprFilter;
import locator.inst.visitor.feature.FeatureExtraction;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class Coverage {

	private final static String __name__ = "@Coverage ";

	
	public static Map<String, CoverInfo> computeOriginalCoverage(Subject subject, Pair<Set<Integer>, Set<Integer>> failedTestAndCoveredMethods){
		// initialize coverage information
		Map<String, CoverInfo> coverage = new HashMap<>();

		String src = subject.getHome() + subject.getSsrc();
		String test = subject.getHome() + subject.getTsrc();
		
		// instrument those methods ran by failed tests
		StatementInstrumentVisitor statementInstrumentVisitor = new StatementInstrumentVisitor(failedTestAndCoveredMethods.getSecond());
		Instrument.execute(src, statementInstrumentVisitor);
		
		NewTestMethodInstrumentVisitor newTestMethodInstrumentVisitor = new NewTestMethodInstrumentVisitor(failedTestAndCoveredMethods.getFirst());
		Instrument.execute(test, newTestMethodInstrumentVisitor);
		// delete all bin file to make it re-compiled
		ExecuteCommand.deleteGivenFolder(subject.getHome() + subject.getSbin());
		ExecuteCommand.deleteGivenFolder(subject.getHome() + subject.getTbin());
		ExecuteCommand.deleteGivenFile(Constant.STR_TMP_INSTR_OUTPUT_FILE);
		if(!Runner.testSuite(subject)){
			System.err.println(__name__ + "Failed to compute original coverage information for build failed.");
			System.exit(0);
		}
	
		ExecuteCommand.copyFolder(src + "_ori", src);
		ExecuteCommand.copyFolder(test + "_ori", test);
//		Instrument.execute(src, new DeInstrumentVisitor());
//		Instrument.execute(test, new DeInstrumentVisitor());
		
		return ExecutionPathBuilder.buildCoverage(Constant.STR_TMP_INSTR_OUTPUT_FILE, new HashSet<>());
	}
	
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
//	public static Map<String, CoverInfo> computeCoverage(Subject subject, Pair<Set<Integer>, Set<Integer>> allTests) {
//		// compute path for failed test cases
//		Set<Method> failedPath = Collector.collectCoveredMethod(subject, allTests.getFirst());
//
//		// //TODO : for debugging
//		// for(Method method : failedPath){
//		// System.out.println(method);
//		// }
//
//		// initialize coverage information
//		Map<String, CoverInfo> coverage = new HashMap<>();
//
//		// instrument those methods ran by failed tests
//		StatementInstrumentVisitor statementInstrumentVisitor = new StatementInstrumentVisitor(failedPath);
//		Instrument.execute(subject.getHome() + subject.getSsrc(), statementInstrumentVisitor);
//
//		// run all failed test
//		int allTestCount = allTests.getFirst().size();
//		int currentCount = 1;
//		for (Integer failedTestID : allTests.getFirst()) {
//			String testString = Identifier.getMessage(failedTestID);
//
//			System.out.println("Failed test [" + currentCount + " / " + allTestCount + "] : " + testString);
//			currentCount ++;
//
//			String[] testInfo = testString.split("#");
//			if (testInfo.length < 4) {
//				LevelLogger.error(__name__ + "#computeCoverage test format error : " + testString);
//				System.exit(0);
//			}
//			String testcase = testInfo[0] + "::" + testInfo[2];
//			// run each test case and collect all test statements covered
//			try {
//				ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSingleCmd(subject, testcase));
//			} catch (Exception e) {
//				LevelLogger.fatal(__name__ + "#computeCoverage run test suite failed !", e);
//			}
//
//			Map<String, Integer> tmpCover = ExecutionPathBuilder
//					.collectAllExecutedStatements(Constant.STR_TMP_INSTR_OUTPUT_FILE);
//			for (Entry<String, Integer> entry : tmpCover.entrySet()) {
//				String statement = entry.getKey();
//				Integer coverCount = entry.getValue();
//				CoverInfo coverInfo = coverage.get(statement);
//				if (coverInfo == null) {
//					coverInfo = new CoverInfo();
//					coverInfo.failedAdd(coverCount);
//					coverage.put(statement, coverInfo);
//				} else {
//					coverInfo.failedAdd(coverCount);
//				}
//			}
//		}
//
//		allTestCount = allTests.getSecond().size();
//		currentCount = 1;
//		// run all passed test
//		for (Integer passTestID : allTests.getSecond()) {
//			String testString = Identifier.getMessage(passTestID);
//
//			System.out.println("Passed test [" + currentCount + " / " + allTestCount + "] : " + testString);
//			currentCount ++;
//
//			String[] testInfo = testString.split("#");
//			if (testInfo.length < 4) {
//				LevelLogger.error(__name__ + "#computeCoverage test format error : " + testString);
//				System.exit(0);
//			}
//			String testcase = testInfo[0] + "::" + testInfo[2];
//			// run each test case and collect all test statements covered
//			try {
//				ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSingleCmd(subject, testcase));
//			} catch (Exception e) {
//				LevelLogger.fatal(__name__ + "#computeCoverage run test suite failed !", e);
//			}
//
//			Map<String, Integer> tmpCover = ExecutionPathBuilder
//					.collectAllExecutedStatements(Constant.STR_TMP_INSTR_OUTPUT_FILE);
//			for (Entry<String, Integer> entry : tmpCover.entrySet()) {
//				String statement = entry.getKey();
//				Integer coverCount = entry.getValue();
//				CoverInfo coverInfo = coverage.get(statement);
//				if (coverInfo == null) {
//					coverInfo = new CoverInfo();
//					coverInfo.passedAdd(coverCount);
//					coverage.put(statement, coverInfo);
//				} else {
//					coverInfo.passedAdd(coverCount);
//				}
//			}
//		}
//
//		Instrument.execute(subject.getHome() + subject.getSsrc(), new DeInstrumentVisitor());
//
//		return coverage;
//	}

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

		
		int allTestCount = testcases.size();
		int currentCount = 1;
		for (Integer testID : testcases) {
			String testString = Identifier.getMessage(testID);

			LevelLogger.info("Test [" + currentCount + " / " + allTestCount + "] : " + testString);
			currentCount ++;

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
	public static Map<String, CoverInfo> computePredicateCoverage(Subject subject, Set<String> allStatements,
			Set<Integer> failedTests) {
		String srcPath = subject.getHome() + subject.getSsrc();
		String testPath = subject.getHome() + subject.getTsrc();
		NewTestMethodInstrumentVisitor newTestMethodInstrumentVisitor = new NewTestMethodInstrumentVisitor(failedTests);
		Instrument.execute(testPath, newTestMethodInstrumentVisitor);
		
		Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates = getAllPredicates(subject, allStatements);
		
		System.out.println("-----------------------------------FOR DEBUG--------------------------------------------");
		printInfo(file2Line2Predicates);
		
		MultiLinePredicateInstrumentVisitor instrumentVisitor = new MultiLinePredicateInstrumentVisitor();
		for(Entry<String, Map<Integer, List<Pair<String, String>>>> entry : file2Line2Predicates.entrySet()){
			String fileName = entry.getKey();
			Map<Integer, List<Pair<String, String>>> allPreds = entry.getValue();
			CompilationUnit unit = JavaFile.genAST(fileName);
			instrumentVisitor.setCondition(allPreds);
			unit.accept(instrumentVisitor);
			
			JavaFile.writeStringToFile(fileName, unit.toString());
		}
		// delete all bin file to make it re-compiled
		ExecuteCommand.deleteGivenFolder(subject.getHome() + subject.getSbin());
		ExecuteCommand.deleteGivenFolder(subject.getHome() + subject.getTbin());
		ExecuteCommand.deleteGivenFile(Constant.STR_TMP_INSTR_OUTPUT_FILE);
		// if the instrumented project builds success, and the test
		// result is the same with original project
		if(!Runner.testSuite(subject)){
			System.out.println("Build failed by predicates : ");
//			printInfo(file2Line2Predicates);
			//should be failed 
//			System.exit(0);
			String file = Constant.HOME + "/rlst.log";
			JavaFile.writeStringToFile(file, "Project : " + subject.getName() + "_" + subject.getId() + " Build failed by predicates!\n", true);
			return null;
		}
		if(!isSameTestResult(failedTests, Constant.STR_TMP_D4J_OUTPUT_FILE)){
			LevelLogger.info("Cause different test state by predicates :");
//			printInfo(file2Line2Predicates);
//			System.exit(0);
			String file = Constant.HOME + "/rlst.log";
			JavaFile.writeStringToFile(file, "Project : " + subject.getName() + "_" + subject.getId() + " Different test result!\n", true);
		} else {
			String file = Constant.HOME + "/rlst.log";
			JavaFile.writeStringToFile(file, "Project : " + subject.getName() + "_" + subject.getId() + " Success!\n", true);
		}
		
		Map<String, CoverInfo> coverage = ExecutionPathBuilder.buildCoverage(Constant.STR_TMP_INSTR_OUTPUT_FILE);
		
		ExecuteCommand.copyFolder(srcPath + "_ori", srcPath);
		ExecuteCommand.copyFolder(testPath + "_ori", testPath);
//		Instrument.execute(testPath, new DeInstrumentVisitor());
//		Instrument.execute(subject.getHome() + subject.getSsrc(), new DeInstrumentVisitor());
		return coverage;
	}
	
	
	private static Map<String, Map<Integer, List<Pair<String, String>>>> getAllPredicates(Subject subject, Set<String> allStatements){

		//parse all object type
		ExprFilter.init(subject);
		
		String srcPath = subject.getHome() + subject.getSsrc();
		
		Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates = new HashMap<>();
		
		int allStmtCount = allStatements.size();
		int currentStmtCount = 1;
		
		for (String stmt : allStatements) {
			LevelLogger.info("======================== [" + currentStmtCount + "/" + allStmtCount + "] statements =================.");
			currentStmtCount ++;
			
			String[] stmtInfo = stmt.split("#");
			if (stmtInfo.length != 2) {
				LevelLogger.error(__name__ + "#getAllPredicates statement parse error : " + stmt);
				System.exit(0);
			}
			Integer methodID = Integer.valueOf(stmtInfo[0]);
			int line = Integer.parseInt(stmtInfo[1]);
			String methodString = Identifier.getMessage(methodID);
			LevelLogger.info("Current statement  : **" + methodString + "#" + line + "**");
			String[] methodInfo = methodString.split("#");
			if (methodInfo.length < 4) {
				LevelLogger.error(__name__ + "#getAllPredicates method info parse error : " + methodString);
				System.exit(0);
			}
			String clazz = methodInfo[0].replace(".", Constant.PATH_SEPARATOR);
			int index = clazz.indexOf("$");
			if (index > 0) {
				clazz = clazz.substring(0, index);
			}
			String relJavaPath = clazz + ".java";
			
			String fileName = srcPath + "/" + relJavaPath;
			File file = new File(fileName);
			if(!file.exists()){
				LevelLogger.error("Cannot find file : " + fileName);
				continue;
			}
			
			// <varName, type>
			Map<String, String> allLegalVariablesMap = new HashMap<>();
			Pair<List<String>, List<String>> features = FeatureExtraction.extractAllFeatures(srcPath, relJavaPath,
					line, allLegalVariablesMap);
			List<String> varFeatures = features.getFirst();
			List<String> expFeatures = features.getSecond();
			Pair<Map<String, List<Pair<String, String>>>, Map<String, List<Pair<String, String>>>> allConditions = Predictor.predict(subject, varFeatures, expFeatures, allLegalVariablesMap);
			// TODO : currently, only instrument predicates for left variables
			Map<String, List<Pair<String, String>>> conditionsForRightVars = allConditions.getSecond();
			// if predicted conditions are not empty for right variables,
			// instrument each condition one by one and compute coverage
			// information for each predicate
			if (conditionsForRightVars != null && conditionsForRightVars.size() > 0) {
				String javaFile = srcPath + Constant.PATH_SEPARATOR + relJavaPath;
				// the source file will instrumented iteratively, before which
				// the original source file should be saved
				ExecuteCommand.copyFile(javaFile, javaFile + ".bak");
				NewPredicateInstrumentVisitor newPredicateInstrumentVisitor = new NewPredicateInstrumentVisitor(null, line);
				List<Pair<String, String>> legalConditions = new ArrayList<>();
				// read original file once
				String source = JavaFile.readFileToString(javaFile);
				String binFile = subject.getHome() + subject.getSbin() + "/" + clazz + ".class";
				
				for(Entry<String, List<Pair<String, String>>> entry : conditionsForRightVars.entrySet()){
					int count = 0;
					int allConditionCount = entry.getValue().size();
					int currentConditionCount = 1;
					for(Pair<String, String> condition : entry.getValue()){
						LevelLogger.info("Validate conditions by compiling : [" + currentConditionCount + "/" + allConditionCount + "].");
						currentConditionCount ++;
						// instrument one condition statement into source file
						CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(source,
								ASTParser.K_COMPILATION_UNIT);
						
						List<Pair<String, String>> onePredicate = new ArrayList<>();
						onePredicate.add(condition);
						newPredicateInstrumentVisitor.setCondition(onePredicate);
						
						compilationUnit.accept(newPredicateInstrumentVisitor);
						
						JavaFile.writeStringToFile(javaFile, compilationUnit.toString());
						ExecuteCommand.deleteGivenFile(binFile);
						
						if(Runner.compileSubject(subject)){
							legalConditions.add(condition);
							// add opposite conditions as well
							Pair<String, String> otherSide = new Pair<>();
							otherSide.setFirst("!(" + condition.getFirst() + ")");
							otherSide.setSecond(condition.getSecond());
							legalConditions.add(otherSide);
							LevelLogger.info("Passed build : " + condition.toString() + "\t ADD \t" + otherSide.toString());
							count ++;
							// only keep partial predicates "top K"
							if(count > Constant.TOP_K_PREDICATES_FOR_EACH_VAR){
								break;
							}
						} else {
							LevelLogger.info("Build failed : " + condition.toString());
						}
					}
				}
				
				if(legalConditions.size() > 0){
					Map<Integer, List<Pair<String, String>>> line2Predicate = file2Line2Predicates.get(javaFile);
					if(line2Predicate == null){
						line2Predicate = new HashMap<>();
					}
					List<Pair<String, String>> predicates = line2Predicate.get(line);
					if(predicates == null){
						predicates = new ArrayList<>();
					}
					predicates.addAll(legalConditions);
					line2Predicate.put(line, predicates);
					file2Line2Predicates.put(javaFile, line2Predicate);
				}
				// delete corresponding class file to enable re-compile for next loop.
				ExecuteCommand.deleteGivenFile(binFile);
				// restore original source file
				ExecuteCommand.moveFile(javaFile + ".bak", javaFile);
			} // end of "conditionsForRightVars != null"
		} // end of "for(String stmt : allStatements)"
		
		return file2Line2Predicates;
	}
	
	private static void printInfo(Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates){
		System.out.println("\n------------------------begin predicate info------------------------\n");
		
		for(Entry<String, Map<Integer, List<Pair<String, String>>>> entry : file2Line2Predicates.entrySet()){
			System.out.println("FILE NAME : " + entry.getKey());
			for(Entry<Integer, List<Pair<String, String>>> line2Preds : entry.getValue().entrySet()){
				System.out.println("LINE : " + line2Preds.getKey());
				System.out.print("PREDICATES : ");
				for(Pair<String, String> pair : line2Preds.getValue()){
					System.out.print(pair.getFirst() + ",");
				}
				System.out.println("\n");
			}
		}
		System.out.println("\n------------------------end predicate info------------------------\n");
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
