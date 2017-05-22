/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.util;

import org.junit.Assert;
import org.junit.Test;

import locator.common.config.Constant;
import locator.common.java.Subject;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class CmdFactoryTest {

	@Test
	public void test_createTestSuiteCmd() {
		Constant.COMMAND_D4J = "defects4j ";
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "/tests", "build", "build-tests");
		String[] actuals = CmdFactory.createTestSuiteCmd(subject);
		String[] expecteds = new String[] { "/bin/bash", "-c", "cd " + subject.getHome() + " && " + "defects4j test" };
		Assert.assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void test_createTestSingleCmd() {
		Constant.COMMAND_D4J = "defects4j ";
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "/tests", "build", "build-tests");
		String[] actuals = CmdFactory.createTestSingleCmd(subject, "classA::testMethod");
		String[] expecteds = new String[] { "/bin/bash", "-c",
				"cd " + subject.getHome() + " && " + "defects4j test -t classA::testMethod" };
		Assert.assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void test_createBuildSubjectCmd() {
		Constant.COMMAND_D4J = "defects4j ";
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "/tests", "build", "build-tests");
		String[] actuals = CmdFactory.createBuildSubjectCmd(subject);
		String[] expecteds = new String[] { "/bin/bash", "-c",
				"cd " + subject.getHome() + " && " + "defects4j compile" };
		Assert.assertArrayEquals(expecteds, actuals);
	}

}
