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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.MapMaker;

import locator.common.config.Constant;
import locator.common.java.Method;
import locator.common.java.Pair;
import locator.common.util.LevelLogger;
import soot.coffi.constant_element_value;
import soot.dava.internal.AST.ASTTryNode.container;
import soot.dava.internal.SET.SETStatementSequenceNode;
import soot.dava.toolkits.base.renamer.infoGatheringAnalysis;

/**
 * This class is responsible for constructing the execution path from the output
 * information
 * 
 * @author Jiajun
 *
 */
public class ExecutionPathBuilder {

	private final static String __name__ = "@ExecutionPathBuilder ";

	/**
	 * collect all methods executed from the given {@code outputFile}
	 * 
	 * @param outputFile
	 *            : file contains the output instrument information
	 * @return a map that maps each method to the executed numbers
	 */
	public static Map<Method, Integer> collectAllExecutedMethods(String outputFile) {
		Map<Method, Integer> allMethods = new HashMap<>();
		File file = new File(outputFile);
		if (!file.exists()) {
			LevelLogger.warn(__name__ + "#collectAllExecutedMethods file : " + outputFile + " does not exist.");
			return allMethods;
		}

		BufferedReader bReader = null;

		try {
			bReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			LevelLogger.fatal(__name__ + "#collectAllExecutedMethods open file failed !", e);
			return allMethods;
		}

		String line = null;
		try {
			while ((line = bReader.readLine()) != null) {
				String[] methodInfo = line.split("#");
				if (methodInfo.length < 3) {
					LevelLogger.error(__name__ + "#collectAllExecutedMethods instrument output format error : " + line);
					System.exit(0);
				}
				int mID = Integer.parseInt(methodInfo[1]);
				Method method = new Method(mID);
				Integer count = allMethods.get(method);
				if (count == null) {
					count = 0;
				}
				allMethods.put(method, count + 1);
			}
			bReader.close();
		} catch (IOException e) {
			LevelLogger.fatal(__name__ + "#collectAllExecutedMethods read file failed !", e);
			return null;
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (IOException e) {
				}
			}
		}

		return allMethods;
	}

	/**
	 * collect all executed statement based on the output file
	 * 
	 * @param outputFile
	 *            : file including the path information
	 * @return : a map contains all executed statements with corresponding
	 *         executed times, statement formatted as : "MethodID#lineNumebr" 
	 */
	public static Map<String, Integer> collectAllExecutedStatements(String outputFile) {
		Map<String, Integer> allMethods = new HashMap<>();
		File file = new File(outputFile);
		if (!file.exists()) {
			LevelLogger.warn(__name__ + "#collectAllExecutedMethods file : " + outputFile + " does not exist.");
			return allMethods;
		}

		BufferedReader bReader = null;

		try {
			bReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			LevelLogger.fatal(__name__ + "#collectAllExecutedMethods open file failed !", e);
			return allMethods;
		}

		String line = null;
		try {
			while ((line = bReader.readLine()) != null) {
				String[] methodInfo = line.split("#");
				if (methodInfo.length < 3) {
					LevelLogger.error(__name__ + "#collectAllExecutedMethods instrument output format error : " + line);
					System.exit(0);
				}
				String statement = methodInfo[1] + "#" + methodInfo[2];
				Integer count = allMethods.get(statement);
				if (count == null) {
					count = 0;
				}
				allMethods.put(statement, count + 1);
			}
			bReader.close();
		} catch (IOException e) {
			LevelLogger.fatal(__name__ + "#collectAllExecutedMethods read file failed !", e);
			return null;
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (IOException e) {
				}
			}
		}

		return allMethods;
	}

	/**
	 * 
	 * @param outputFile
	 * @return <TestMethodID, <StatementString, coveredTimes>>
	 */
	public static Map<Integer, Map<String, Integer>> collectPredicateCoverageInfo(String outputFile) {
		// each test holds a map : mapping each line and its covered times by
		// this test case
		Map<Integer, Map<String, Integer>> coverageInfo = new HashMap<>();
		File file = new File(outputFile);
		if (!file.exists()) {
			LevelLogger.warn(__name__ + "#collectPredicateCoverageInfo file : " + outputFile + " does not exist.");
			return coverageInfo;
		}

		BufferedReader bReader = null;

		try {
			bReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			LevelLogger.fatal(__name__ + "#collectPredicateCoverageInfo open file failed !", e);
			return coverageInfo;
		}

		String line = null;
		int lastTestID = -1;
		try {
			while ((line = bReader.readLine()) != null) {
				String[] methodInfo = line.split("#");
				if (methodInfo.length < 3) {
					LevelLogger
							.error(__name__ + "#collectPredicateCoverageInfo instrument output format error : " + line);
					System.exit(0);
				}
				String flag = methodInfo[0];
				if (flag.equals(Constant.INSTRUMENT_FLAG + Constant.INSTRUMENT_K_TEST)) {
					lastTestID = Integer.parseInt(methodInfo[1]);
					continue;
				}

				String statement = methodInfo[1] + "#" + methodInfo[2];
				Map<String, Integer> lineMap = coverageInfo.get(lastTestID);
				if (lineMap != null) {
					Integer count = lineMap.get(statement);
					if (count == null) {
						count = 0;
					}
					lineMap.put(statement, Integer.valueOf(count + 1));
				} else {
					lineMap = new HashMap<>();
					lineMap.put(statement, Integer.valueOf(1));
					coverageInfo.put(lastTestID, lineMap);
				}
			}
			bReader.close();
		} catch (IOException e) {
			LevelLogger.fatal(__name__ + "#collectPredicateCoverageInfo read file failed !", e);
			return null;
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (IOException e) {
				}
			}
		}

		return coverageInfo;
	}

}
