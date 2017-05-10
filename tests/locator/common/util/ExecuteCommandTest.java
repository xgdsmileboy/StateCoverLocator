/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.util;

import org.junit.Test;

import locator.common.config.Constant;
import locator.common.java.Subject;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class ExecuteCommandTest {
	
	@Test
	public void test_testSuite(){
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "/tests", "build", "build-tests");
		String[] cmd = CmdFactory.createTestSuiteCmd(subject);
		try {
			ExecuteCommand.executeDefects4JTest(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
