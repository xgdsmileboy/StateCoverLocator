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
import java.util.ArrayList;
import java.util.List;

import locator.common.config.Constant;
import locator.common.util.LevelLogger;

/**
 * This class is responsible for constructing the execution path from the output
 * information and compute the intersection point for different test
 * 
 * @author Jiajun
 *
 */
public class ExecutionPathBuilder {

	private final static String __name__ = "@ExecutionPathBuilder ";
	private final static long MILLIS_PER_MINUTE = 1000 * 60;

	public static List<String> findFailedTestFromFile(String outputFilePath) {
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
		List<String> failedTest = new ArrayList<>();
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
							failedTest.add(testStr);
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

//	public static List<TestMethod> buildPathFromFile(DynamicRuntimeInfo dynamicRuntimeInfo, String outputFilePath) {
//		List<TestMethod> testMethodList = new ArrayList<>();
//		if (outputFilePath == null) {
//			LevelLogger.error(__name__ + "#build Illegal input argument : null.");
//			return testMethodList;
//		}
//		File file = new File(outputFilePath);
//		if (!file.exists() || !file.isFile()) {
//			LevelLogger.error(__name__ + "#build Illegal input argument, not a file : " + file.getAbsolutePath());
//			return testMethodList;
//		}
//
//		BufferedReader bufferedReader;
//		try {
//			bufferedReader = new BufferedReader(new FileReader(file));
//		} catch (FileNotFoundException e) {
//			LevelLogger.fatal(__name__ + "#build Read file failed : " + file.getAbsolutePath(), e);
//			return testMethodList;
//		}
//
//		String line = null;
//		TestMethod lastTestMethod = new TestMethod(-1);
//		int testCycleForOneTest = 1;
//		try {
//			//limit the time for reading file at most 10 minutes
//			long endGuard = System.currentTimeMillis() + (2 * MILLIS_PER_MINUTE);
//			while ((line = bufferedReader.readLine()) != null) {
//				// limit the time for reading file
//				if(System.currentTimeMillis() > endGuard){
//					break;
//				}
//				line = line.trim();
//					
//				String[] strings = line.split("#");
//				if (strings.length < 3) {
//					LevelLogger.error(__name__ + "#build Invalid output message format : " + line);
//					continue;
//				}
//
//				int methodID = Integer.parseInt(strings[1]);
//				int lineNumber = Integer.parseInt(strings[2]);
//				
//				String instrumentKey = strings[0];
//				if (instrumentKey.endsWith(Constant.INSTRUMENT_TEST)) {
//				
//					testCycleForOneTest++;
//
//					TestMethod testMethod = new TestMethod(methodID);
//
//					if (!testMethod.equals(lastTestMethod)) {
//						testCycleForOneTest = 1;
//					}
//					testMethod.setTestStatementNumber(testCycleForOneTest);
//					lastTestMethod = testMethod;
//					testMethodList.add(testMethod);
//
//					lastTestMethod.addExecutedLine(lineNumber);
//
//				} else {
//					boolean duplicated = false;
//					List<Method> path = lastTestMethod.getExecutionPath();
//					if(path != null && path.size() > 0){
//						Method lastExecutedMethod = path.get(path.size() - 1);
//						if(lastExecutedMethod.getMethodID() == methodID){
//							List<Integer> lIntegers = lastExecutedMethod.getExecutedLines();
//							if(!lIntegers.contains(lineNumber)){
//								lastExecutedMethod.addExecutedLine(lineNumber);
//							}
//							duplicated = true;
//						}
//					}
//					
//					if(!duplicated){
//						int sampleCycle = 0;
//						boolean canBeAsWatchPoint = false;
//						int min = lineNumber;
//						for(ExecutedMethod ExecutedMethod : lastTestMethod.getExecutionPath()){
//							if(ExecutedMethod.getMethodID() == methodID){
//								int lastCycle = ExecutedMethod.getSampleCycle();
//								sampleCycle = sampleCycle > lastCycle ? sampleCycle : lastCycle;
//								for(Integer executedLines : ExecutedMethod.getExecutedLines()){
//									min = min < executedLines ? min : executedLines;
//								}
//							}
//						}
//						if(lineNumber == min){
//							canBeAsWatchPoint = true;
//							sampleCycle ++;
//						}
//						
//						ExecutedMethod method = new ExecutedMethod(methodID);
//						method.setCanBeWatchPoint(canBeAsWatchPoint);
//						method.setSampleCycle(sampleCycle);
//						lastTestMethod.addExecutedMethod(method, lineNumber);
//					}
//				}
//
//			}
//			bufferedReader.close();
//		} catch (IOException e) {
//			LevelLogger.fatal(__name__ + "#build Read file failed : " + file.getAbsolutePath(), e);
//			return testMethodList;
//		} finally {
//			if (bufferedReader != null) {
//				try {
//					bufferedReader.close();
//				} catch (IOException e) {
//					LevelLogger.error(__name__ + "#build Close file failed : " + file.getAbsolutePath(), e);
//				}
//			}
//		}
//		return testMethodList;
//	}
	
}
