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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

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
			long begin = System.currentTimeMillis();
			ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSuiteCmd(subject));
			long end = System.currentTimeMillis();
			LevelLogger.info("Run all test cases cost : " + ((end - begin)/1000) + " sec"); 
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#testSuite run test suite failed !", e);
		}
		return checkBuild();
	}

	public static boolean testSingleCase(Subject subject, String testcase) {
		try {
			LevelLogger.info("TESTING : " + testcase);
			ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSingleCmd(subject, testcase));
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#testSingle run test suite failed !", e);
		}
		return checkBuild();
	}
	public static boolean compileSubject(Subject subject, Set<String> illegalConditions){
		List<String> message = null;
		try {
			message = ExecuteCommand.executeCompile(CmdFactory.createBuildSubjectCmd(subject));
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#buildSubject run build subject failed !", e);
		}
		
		boolean success = false;
		Pattern pattern = Pattern.compile("(?<=\\()[\\s\\S]*(?=\\))");
		for(String string : message){
			Matcher matcher = pattern.matcher(string);
			if(matcher.find()){
				illegalConditions.add(matcher.group(0));
			}
			if(string.contains(Constant.ANT_BUILD_SUCCESS)){
				success = true;
			}
		}
		
		return success;
	}
	

	public static boolean compileSubject(Subject subject) {
		List<String> message = null;
		try {
			message = ExecuteCommand.executeCompile(CmdFactory.createBuildSubjectCmd(subject));
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#buildSubject run build subject failed !", e);
		}
		
		boolean success = false;
		for(int i = message.size() - 1; i >= 0; i--){
			if (message.get(i).contains(Constant.ANT_BUILD_SUCCESS)) {
				success = true;
				break;
			}
		}
		
		return success;
	}

	private static boolean checkBuild() {
		// check whether the project is built successfully
		// based on the output info @Constant.STR_TMP_D4J_OUTPUT_FILE
		File file = new File(Constant.STR_TMP_D4J_OUTPUT_FILE);
		if (!file.exists()) {
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
			while ((line = bReader.readLine()) != null) {
				if (line.startsWith(Constant.ANT_BUILD_SUCCESS)) {
					buildSuccess = true;
					break;
				} else if (line.startsWith(Constant.ANT_BUILD_FAILED)) {
					buildSuccess = false;
					break;
				}
			}
		} catch (IOException e) {
			LevelLogger.fatal(__name__ + "#checkBuild read file failed !", e);
		}

		return buildSuccess;
	}
	
	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("(?<=\\()[\\s\\S]*(?=\\))");
		
		List<String> message = new ArrayList<>();
		message.add("[javac]  if (hK[i] != 0.0) {");
		message.add("    [javac]     ^");
		message.add("    [javac]   symbol:   variable hK");
		message.add("    [javac]   location: class MathArrays");
		message.add("    [javac] /home/jiajun/d4j/projects/math/math_3_buggy/src/main/java/org/apache/commons/math3/util/MathArrays.java:627: error: non-static variable this cannot be referenced from a static context");
		message.add("    [javac] if (this.lindep[i], compile, this.lindep[i]) {");
		message.add("    [javac]         ^");
		message.add("    [javac]   symbol: variable lindep");
		message.add("    [javac] /home/jiajun/d4j/projects/math/math_3_buggy/src/main/java/org/apache/commons/math3/util/MathArrays.java:630: error: cannot find symbol");
		message.add("    [javac] if (taken[i]) {");
		message.add("		    [javac]     ^");
		message.add("    [javac]   symbol:   variable taken");
		message.add("    [javac]   location: class MathArrays");
		message.add("    [javac] /home/jiajun/d4j/projects/math/math_3_buggy/src/main/java/org/apache/commons/math3/util/MathArrays.java:642: error: incomparable types: double[] and double");
		message.add("    [javac] if (a != 0.0]");
		
		for(String string : message){
			Matcher matcher = pattern.matcher(string);
			if(matcher.find()){
				System.out.println(matcher.group(0));
			}
		}
		
		
	}
	
}
