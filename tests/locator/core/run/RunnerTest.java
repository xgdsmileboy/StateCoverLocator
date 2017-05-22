/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.run;

import org.junit.Assert;
import org.junit.Test;

import locator.common.config.Constant;
import locator.common.java.Subject;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class RunnerTest {

	@Test
	public void test_testSuite() {
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "/tests", "build", "build-tests");
		Assert.assertTrue("Should success !", Runner.testSuite(subject));
	}

	@Test
	public void test_testSingleCase() {
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "/tests", "build", "build-tests");
		String testCase = "org.jfree.chart.renderer.category.junit.AbstractCategoryItemRendererTests::test2947660";
		Assert.assertTrue("Should success !", Runner.testSingleCase(subject, testCase));
	}

	@Test
	public void test_compileSubject() {
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "/tests", "build", "build-tests");
		Assert.assertTrue("Should success !", Runner.compileSubject(subject));
	}

}
