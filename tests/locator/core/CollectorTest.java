/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.Subject;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class CollectorTest {
	
	@Test
	public void test_collectAllPassedTestCases(){
		Constant.PROJECT_HOME = "res/junitRes";
		String failedTestString = "org.jfree.chart.renderer.category.junit.AbstractCategoryItemRendererTests#void#test2947660#?";
		Set<Integer> failedTests = new HashSet<>();
		int failedID = Identifier.getIdentifier(failedTestString);
		failedTests.add(failedID);
		Subject subject = new Subject("chart", 1, "/source", "tests", "build", "build-tests");
		Set<Integer> allPassedTestMethod = Collector.collectAllPassedTestCases(subject, failedTests);
		
		Assert.assertTrue("Passed tests should not be zero.", allPassedTestMethod.size() > 0);
		Assert.assertFalse("Failed test should be excluded.", allPassedTestMethod.contains(failedID));
	}
	
}
