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
import java.util.Set;

import locator.common.config.Identifier;
import locator.common.java.Subject;
import locator.common.util.LevelLogger;

/**
 * @author Jiajun
 * @date May 9, 2017
 */
public class Collector {

	private final static String __name__ = "@Collector ";
	
	public static Set<Integer> collectAllPassedTestCases(Subject subject, Set<Integer> failedTests){
		Set<Integer> allPassedTestCases = new HashSet<>();
		
		File file = new File(subject.getHome() + "/all-tests.txt");
		if(!file.exists()){
			LevelLogger.error(__name__ + "#collectAllPassedTestCases file : " + file.getAbsolutePath() +" does not exist !");
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
			while((line = bReader.readLine()) != null){
				int indexOfLeftBracket = line.indexOf("(");
				int indexOfRightBracket = line.indexOf(")");
				if(indexOfLeftBracket < 0 || indexOfRightBracket < 0){
					LevelLogger.warn(__name__ + "#collectAllPassedTestCases find bracket failed !");
					continue;
				}
				String methodName = line.substring(0, indexOfLeftBracket);
				String clazzName = line.substring(indexOfLeftBracket + 1, indexOfRightBracket);
				String methodString = clazzName + "#void#" + methodName + "#?";
				int methodID = Identifier.getIdentifier(methodString);
				if(!failedTests.contains(methodID)){
					allPassedTestCases.add(methodID);
				}
			}
		} catch (IOException e) {
			LevelLogger.fatal(__name__ + "#collectAllPassedTestCases read file error !", e);
		}
		return allPassedTestCases;
	}
	
	
}
