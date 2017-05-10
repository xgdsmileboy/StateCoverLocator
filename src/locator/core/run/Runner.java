/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import locator.common.config.Constant;
import locator.common.java.Subject;
import locator.common.util.CmdFactory;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class Runner {
	
	private final static String __name__ = "@Run ";
	
	/**
	 * execute all test and collect all failed test
	 * 
	 * @return a list of failed test
	 */
	public static boolean testSuite(Subject subject) {
		try {
			ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSuiteCmd(subject));
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#testSuite run test suite failed !", e);
		}
		return checkBuild();
	}
	
	public static boolean testSingleCase(Subject subject, String testcase){
		try {
			ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSingleCmd(subject, testcase));
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#testSingle run test suite failed !", e);
		}
		return checkBuild();
	}
	
	public static boolean buildSubject(Subject subject){
		try {
			ExecuteCommand.executeDefects4JTest(CmdFactory.createBuildSubjectCmd(subject));
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#buildSubject run build subject failed !", e);
		}
		return checkBuild();
	}
	
	private static boolean checkBuild(){
		// check whether the project is built successfully 
		// based on the output info @Constant.STR_TMP_D4J_OUTPUT_FILE
		File file = new File(Constant.STR_TMP_D4J_OUTPUT_FILE);
		if(!file.exists()){
			LevelLogger.debug(__name__ + "#checkBuild file :" + Constant.STR_TMP_D4J_OUTPUT_FILE + " not exists.");
			return false;
		}
		
		BufferedReader bReader = null;
		
		try {
			bReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			LevelLogger.fatal(__name__ + "#checkBuild open file failed !", e);
			return false;
		}
		
		String line = null;
		boolean buildSuccess = false;
		try {
			while((line = bReader.readLine()) != null){
				if(line.startsWith(Constant.ANT_BUILD_SUCCESS)){
					buildSuccess = true;
					break;
				} else if(line.startsWith(Constant.ANT_BUILD_FAILED)){
					buildSuccess = false;
					break;
				}
			}
		} catch (IOException e) {
			LevelLogger.fatal(__name__ + "#checkBuild read file failed !", e);
		}
		
		return buildSuccess;
	}
}
