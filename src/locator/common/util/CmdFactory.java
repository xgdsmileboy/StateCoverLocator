/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.util;

import locator.common.config.Constant;
import locator.common.java.Subject;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class CmdFactory {
	
	public static String[] createTestSuiteCmd(Subject subject){
		return createD4JCmd(subject, "test");
	}
	
	public static String[] createTestSingleCmd(Subject subject, String testcase){
		return createD4JCmd(subject, "test -t " + testcase);
	}
	
	public static String[] createBuildSubjectCmd(Subject subject){
		return createD4JCmd(subject, "compile");
	}
	
	private static String[] createD4JCmd(Subject subject, String args){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(Constant.COMMAND_CD + subject.getHome() + " && ");
		stringBuffer.append(Constant.COMMAND_D4J + args);
		String[] cmd = new String[] { "/bin/bash", "-c", stringBuffer.toString() };
		return cmd;
	}
	
}
