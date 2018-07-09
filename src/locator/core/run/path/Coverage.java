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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import jdk7.wrapper.JCompiler;
import locator.common.config.Configure;
import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.CoverInfo;
import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.CmdFactory;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.common.util.Utils;
import locator.core.run.Runner;
import locator.inst.Instrument;
import locator.inst.predict.Predictor;
import locator.inst.visitor.BranchInstrumentVisitor;
import locator.inst.visitor.MultiLinePredicateInstrumentVisitor;
import locator.inst.visitor.NewNoSideEffectPredicateInstrumentVisitor;
import locator.inst.visitor.NewPredicateInstrumentVisitor;
import locator.inst.visitor.NewTestMethodInstrumentVisitor;
import locator.inst.visitor.StatementInstrumentVisitor;
import locator.inst.visitor.TraversalVisitor;
import locator.inst.visitor.feature.ExprFilter;
import locator.inst.visitor.feature.FeatureExtraction;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class Coverage {

    private final static String __name__ = "@Coverage ";

	/**
	 * compute the original coverage information of statements, it can compute the
	 * traditional statement coverage and branch coverage based on the argument
	 * {@code visitor}
	 * 
	 * @param subject:
	 *            subject to be tested
	 * @param failedTestAndCoveredMethods
	 *            : a pair that contains the ids of failed test cases and covered
	 *            methods by failed test cases.
	 * @param visitor
	 *            : class of {@code BranchInstrumentVisitor} or
	 *            {@code StatementInstrumentVisitor}
	 * @return
	 */
	public static Map<String, CoverInfo> computeOriginalCoverage(Subject subject,
			Pair<Set<Integer>, Set<Integer>> failedTestAndCoveredMethods, Class visitor) {

        String src = subject.getHome() + subject.getSsrc();
        String test = subject.getHome() + subject.getTsrc();

        TraversalVisitor traversalVisitor = null;
        if(visitor == BranchInstrumentVisitor.class) {
        	traversalVisitor = new BranchInstrumentVisitor(failedTestAndCoveredMethods.getSecond());
        } else {
        	traversalVisitor = new StatementInstrumentVisitor(failedTestAndCoveredMethods.getSecond());
        }
        // instrument those methods ran by failed tests
        Instrument.execute(src, traversalVisitor);

        NewTestMethodInstrumentVisitor newTestMethodInstrumentVisitor = new NewTestMethodInstrumentVisitor(
                failedTestAndCoveredMethods.getFirst(), false);
        Instrument.execute(test, newTestMethodInstrumentVisitor);
        
        // delete all bin file to make it re-compiled
        ExecuteCommand.deleteGivenFolder(subject.getHome() + subject.getSbin());
        ExecuteCommand.deleteGivenFolder(subject.getHome() + subject.getTbin());
        ExecuteCommand.deleteGivenFile(Constant.STR_TMP_INSTR_OUTPUT_FILE);
        
        if (!Runner.testSuite(subject)) {
            LevelLogger.error(__name__ + "Failed to compute original coverage information for build failed.");
            System.exit(0);
        }

        ExecuteCommand.copyFolder(src + "_ori", src);
        ExecuteCommand.copyFolder(test + "_ori", test);

        return ExecutionPathBuilder.buildCoverage(Constant.STR_TMP_INSTR_OUTPUT_FILE, new HashSet<Integer>());
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

        // instrument those methods ran by failed tests
        // StatementInstrumentVisitor statementInstrumentVisitor = new
        // StatementInstrumentVisitor(coveredMethod);
        StatementInstrumentVisitor statementInstrumentVisitor = new StatementInstrumentVisitor();
        Instrument.execute(subject.getHome() + subject.getSsrc(), statementInstrumentVisitor);

        int allTestCount = testcases.size();
        int currentCount = 1;
        for (Integer testID : testcases) {
            String testString = Identifier.getMessage(testID);

            LevelLogger.info("Test [" + (currentCount ++) + " / " + allTestCount + "] : " + testString);

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
            Set<Integer> failedTests, boolean useStatisticalDebugging, boolean useSober) {
        String srcPath = subject.getHome() + subject.getSsrc();
        String testPath = subject.getHome() + subject.getTsrc();
        NewTestMethodInstrumentVisitor newTestMethodInstrumentVisitor = new NewTestMethodInstrumentVisitor(failedTests, useSober);
        Instrument.execute(testPath, newTestMethodInstrumentVisitor);

		Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates = null;

		long start = System.currentTimeMillis();
		if (useStatisticalDebugging) {
			file2Line2Predicates = new HashMap<>();
			Map<String, List<Integer>> file2LocationList = mapLocations2File(allStatements, srcPath);
			NewNoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NewNoSideEffectPredicateInstrumentVisitor(
					useSober);
			for (Entry<String, List<Integer>> entry : file2LocationList.entrySet()) {
				String relJavaPath = entry.getKey();
				String fileName = srcPath + "/" + relJavaPath;
				Set<Integer> locations = new HashSet<>(entry.getValue());
				CompilationUnit unit = (CompilationUnit) JavaFile.genASTFromSourceWithType(
						JavaFile.readFileToString(fileName), ASTParser.K_COMPILATION_UNIT, fileName, subject);
				instrumentVisitor.initOneRun(locations, srcPath, relJavaPath);
				unit.accept(instrumentVisitor);
				JavaFile.writeStringToFile(fileName, unit.toString());
				file2Line2Predicates.put(fileName, instrumentVisitor.getPredicates());
			}
			LevelLogger.debug("-------------------FOR DEBUG----------------------");
			printPredicateInfo(file2Line2Predicates, subject, useStatisticalDebugging);
		} else {
			if (Constant.RECOVER_PREDICATE_FROM_FILE) {
				file2Line2Predicates = recoverPredicates(subject, useStatisticalDebugging);
			}
			if (file2Line2Predicates == null) {
				file2Line2Predicates = getAllPredictPredicates(subject, allStatements, useSober);
				// Delete empty file and line.
				// Please DO NOT comment out the following line! 2018/01/01
				file2Line2Predicates = recoverPredicates(subject, useStatisticalDebugging);
			}
			LevelLogger.debug("-------------------FOR DEBUG----------------------");
			printPredicateInfo(file2Line2Predicates, subject, useStatisticalDebugging);

			MultiLinePredicateInstrumentVisitor instrumentVisitor = new MultiLinePredicateInstrumentVisitor(useSober);
			for (Entry<String, Map<Integer, List<Pair<String, String>>>> entry : file2Line2Predicates.entrySet()) {
				String fileName = entry.getKey();
				Map<Integer, List<Pair<String, String>>> allPreds = entry.getValue();
				CompilationUnit unit = JavaFile.genAST(fileName);
				instrumentVisitor.setCondition(allPreds);
				unit.accept(instrumentVisitor);
				JavaFile.writeStringToFile(fileName, unit.toString());
			}
		}
		long duration = System.currentTimeMillis() - start;
		LevelLogger.info("Predicate validation time : " + Utils.transformMilli2Time(duration));

        // delete all bin file to make it re-compiled
        ExecuteCommand.deleteGivenFolder(subject.getHome() + subject.getSbin());
        ExecuteCommand.deleteGivenFolder(subject.getHome() + subject.getTbin());
        ExecuteCommand.deleteGivenFile(Constant.STR_TMP_INSTR_OUTPUT_FILE);
        Configure.compileAuxiliaryJava(subject);
        // if the instrumented project builds success, and the test
        // result is the same with original project
        if (!Runner.testSuite(subject)) {
           LevelLogger.error("Build failed by predicates : ");
            String file = Constant.HOME + "/rlst.log";
            JavaFile.writeStringToFile(file, "Project : " + subject.getName() + "_" + subject.getId() + " Build failed by predicates!\n", true);
            return null;
        }
        if (!isSameTestResult(failedTests, Constant.STR_TMP_D4J_OUTPUT_FILE)) {
            LevelLogger.info("Cause different test state by predicates :");
            String file = Constant.HOME + "/rlst.log";
            JavaFile.writeStringToFile(file, "Project : " + subject.getNameAndId() + " Different test result!\n", true);
            logErrorInfo(subject, failedTests);
        } else {
            String file = Constant.HOME + "/rlst.log";
            JavaFile.writeStringToFile(file, "Project : " + subject.getNameAndId() + " Success!\n", true);
        }

        Map<String, CoverInfo> coverage = null;
        if (!useSober) {
            coverage = ExecutionPathBuilder.buildCoverage(Constant.STR_TMP_INSTR_OUTPUT_FILE);
        }

        ExecuteCommand.copyFolder(srcPath + "_ori", srcPath);
        ExecuteCommand.copyFolder(testPath + "_ori", testPath);
        return coverage;
    }
    
	/**
	 * log error information when the predicates cause different test results
	 * 
	 * @param subject
	 *            : the subject to test
	 * @param failedTests
	 *            : the ids of failed test cases
	 */
	private static void logErrorInfo(Subject subject, Set<Integer> failedTests) {
		String subjectNameAndId = subject.getNameAndId();
         String diff_result_error = Constant.HOME + "/info/" + subjectNameAndId + "_diff.log";
         StringBuffer stringBuffer = new StringBuffer();
         stringBuffer.append("Project : " + subjectNameAndId + "\n");
         stringBuffer.append("---Original failed test cases : \n");
         for(Integer integer : failedTests) {
         	stringBuffer.append(Identifier.getMessage(integer) + "\n");
         }
         stringBuffer.append("---Failed test cases after instrument: \n");
         for(Integer integer : Collector.findFailedTestFromFile(Constant.STR_TMP_D4J_OUTPUT_FILE)) {
     		stringBuffer.append(Identifier.getMessage(integer) + "\n");
         }
         stringBuffer.append(JavaFile.readFileToString(Constant.STR_TMP_D4J_OUTPUT_FILE));
         JavaFile.writeStringToFile(diff_result_error, stringBuffer.toString());
    }
    

    private static Map<String, List<Integer>> mapLocations2File(Set<String> allStatements, String srcPath) {
        Map<String, List<Integer>> file2LocationList = new HashMap<>();
        int allStmtCount = allStatements.size();
        int currentStmtCount = 1;
        for (String stmt : allStatements) {
            LevelLogger.debug("======================== [" + currentStmtCount + "/" + allStmtCount
                    + "] statements (statistical debugging) =================.");
            currentStmtCount++;
            String[] stmtInfo = stmt.split("#");
            if (stmtInfo.length != 2) {
                LevelLogger.error(__name__ + "#mapLocations2File statement parse error : " + stmt);
                System.exit(0);
            }
            Integer methodID = Integer.valueOf(stmtInfo[0]);
            int line = Integer.parseInt(stmtInfo[1]);
            if (line == 2317) {
                LevelLogger.debug(__name__ + "#mapLocations2File : exist");
            }
            String methodString = Identifier.getMessage(methodID);
            LevelLogger.debug("Current statement  : **" + methodString + "#" + line + "**");
            String[] methodInfo = methodString.split("#");
            if (methodInfo.length < 4) {
                LevelLogger.error(__name__ + "#mapLocations2File method info parse error : " + methodString);
                System.exit(0);
            }
            String clazz = methodInfo[0].replace(".", Constant.PATH_SEPARATOR);
            int index = clazz.indexOf("$");
            if (index > 0) {
                clazz = clazz.substring(0, index);
            }
            String relJavaPath = clazz + ".java";

            String fileName = srcPath + Constant.PATH_SEPARATOR + relJavaPath;

            List<Integer> list = file2LocationList.get(relJavaPath);
            if (list == null) {
                File file = new File(fileName);
                if (!file.exists()) {
                    LevelLogger.error("Cannot find file : " + fileName);
                    continue;
                }
                list = new LinkedList<>();
            }
            list.add(line);
            file2LocationList.put(relJavaPath, list);
        }
        return file2LocationList;
    }

    private static Map<String, Map<Integer, List<Pair<String, String>>>> getAllPredictPredicates(Subject subject,
            Set<String> allStatements, boolean useSober) {

        // parse all object type
        ExprFilter.init(subject);

        String srcPath = subject.getHome() + subject.getSsrc();

        Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates = new HashMap<>();

        int allStmtCount = allStatements.size();
        int currentStmtCount = 1;

        Map<String, LineInfo> lineInfoMapping = new HashMap<String, LineInfo>();
        List<String> varFeatures = new ArrayList<String>();
        List<String> exprFeatures = new ArrayList<String>();
        for (String stmt : allStatements) {
            LevelLogger.info("======================== [" + currentStmtCount + "/" + allStmtCount
                    + "] statements =================.");
            currentStmtCount++;

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
            if (!file.exists()) {
                LevelLogger.error("Cannot find file : " + fileName);
                continue;
            }

            // <varName, type>
            LineInfo info = new LineInfo(line, relJavaPath, clazz);
            Set<String> newAddedKeys = FeatureExtraction.extractAllFeatures(srcPath, relJavaPath, line, info,
                    varFeatures, exprFeatures);
            for (String key : newAddedKeys) {
                lineInfoMapping.put(key, info);
            }
        }
        Map<String, Map<String, List<Pair<String, String>>>> conditionsForRightVars = Predictor.predict(subject,
                varFeatures, exprFeatures, lineInfoMapping);
        // TODO : currently, only instrument predicates for right variables
        // if predicted conditions are not empty for right variables,
        // instrument each condition one by one and compute coverage
        // information for each predicate
        JCompiler compiler = JCompiler.getInstance();
        for (Map.Entry<String, Map<String, List<Pair<String, String>>>> entry : conditionsForRightVars.entrySet()) {
            final LineInfo info = lineInfoMapping.get(entry.getKey());
            int line = info.getLine();
            if (entry.getValue() != null && entry.getValue().size() > 0) {
                String relJavaPath = info.getRelJavaPath();
                String javaFile = srcPath + Constant.PATH_SEPARATOR + relJavaPath;
                // the source file will instrumented iteratively, before which
                // the original source file should be saved
                // ExecuteCommand.copyFile(javaFile, javaFile + ".bak");
                NewPredicateInstrumentVisitor newPredicateInstrumentVisitor = new NewPredicateInstrumentVisitor(null,
                        line);
                List<Pair<String, String>> legalConditions = new ArrayList<>();
                // read original file once
                String source = JavaFile.readFileToString(javaFile);

                for (Entry<String, List<Pair<String, String>>> innerEntry : entry.getValue().entrySet()) {
                    int count = 0;
                    int allConditionCount = innerEntry.getValue().size();
                    int currentConditionCount = 1;
                    for (Pair<String, String> condition : innerEntry.getValue()) {
                        LevelLogger.info("Validate conditions by compiling : [" + currentConditionCount + "/"
                                + allConditionCount + "].");
                        currentConditionCount++;

                        // instrument one condition statement into source file
                        CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(source,
                                ASTParser.K_COMPILATION_UNIT);

                        List<Pair<String, String>> onePredicate = new ArrayList<>();
                        onePredicate.add(condition);
                        newPredicateInstrumentVisitor.setCondition(onePredicate);

                        compilationUnit.accept(newPredicateInstrumentVisitor);

                        if (compiler.compile(subject, relJavaPath, compilationUnit.toString())) {
                            legalConditions.add(condition);
                            if (!useSober) {
                                // add opposite conditions as well
                                Pair<String, String> otherSide = new Pair<>();
                                otherSide.setFirst("!(" + condition.getFirst() + ")");
                                otherSide.setSecond(condition.getSecond());
                                legalConditions.add(otherSide);
                            }
                            LevelLogger.info("Passed build : " + condition.toString() + "\t ADD \t");
                            count++;
                            // only keep partial predicates "top K"
                            if (count >= Constant.TOP_K_PREDICATES_FOR_EACH_VAR) {
                                break;
                            }
                        } else {
                            LevelLogger.info("Build failed : " + condition.toString());
                        }
                    }
                }

                if (legalConditions.size() > 0) {
                    Map<Integer, List<Pair<String, String>>> line2Predicate = file2Line2Predicates.get(javaFile);
                    if (line2Predicate == null) {
                        line2Predicate = new HashMap<>();
                    }
                    List<Pair<String, String>> predicates = line2Predicate.get(line);
                    if (predicates == null) {
                        predicates = new ArrayList<>();
                    }
                    predicates.addAll(legalConditions);
                    line2Predicate.put(line, predicates);
                    file2Line2Predicates.put(javaFile, line2Predicate);
                }
                if (!compiler.compile(subject, relJavaPath, source)) {
                    if (!Runner.compileSubject(subject)) {
                        LevelLogger.error(
                                __name__ + "#getAllPredicates ERROR : compile original source failed : "
                                        + javaFile);
                    }
                }
                // delete corresponding class file to enable re-compile for next
                // loop.
                // ExecuteCommand.deleteGivenFile(binFile);
                // restore original source file
                // ExecuteCommand.moveFile(javaFile + ".bak", javaFile);
            } // end of "conditionsForRightVars != null"
        } // end of "for(String stmt : allStatements)"

        return file2Line2Predicates;
    }

    private static void printPredicateInfo(Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates,
            Subject subject, boolean useStatisticalDebugging) {
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
        String outputFile = subject.getCoverageInfoPath() + "/predicates_backup";
        if (useStatisticalDebugging) {
            outputFile += "_sd.txt";
        } else {
            outputFile += ".txt";
        }
        JavaFile.writeStringToFile(outputFile, contents.toString(), true);
    }

    public static Map<String, Map<Integer, List<Pair<String, String>>>> recoverPredicates(Subject subject,
            boolean useStatisticalDebugging) {
        Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates = new HashMap<>();
        String inputFile = subject.getCoverageInfoPath() + "/predicates_backup";
        if (useStatisticalDebugging) {
            inputFile += "_sd.txt";
        } else {
            inputFile += ".txt";
        }
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
            if(parts.length != 4) continue;
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
