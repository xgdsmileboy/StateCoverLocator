/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.run.path;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import locator.common.config.Constant;
import locator.common.java.CoverInfo;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class ExecutionPathBuilderTest {
	
	@Test
	public void test_buildCoverage(){
		Map<String, CoverInfo> coverage = ExecutionPathBuilder.buildCoverage(Constant.STR_TMP_INSTR_OUTPUT_FILE, new HashSet<Integer>());
		for(Entry<String, CoverInfo> entry : coverage.entrySet()){
			System.out.println(entry.getKey() + " " + entry.getValue().getFailedCount() + " " + entry.getValue().getPassedCount());
		}
	}
	

	// @Test
	// public void test_findFailedTestFromFile(){
	// Constant.PROJECT_HOME = "res/junitRes";
	// Subject subject = new Subject("chart", 1, "/source", "tests", "build",
	// "build-tests");
	// boolean success = Runner.testSuite(subject);
	// Assert.assertTrue(success);
	//
	// List<String> failedTests =
	// ExecutionPathBuilder.findFailedTestFromFile(Constant.STR_TMP_D4J_OUTPUT_FILE);
	//
	// Assert.assertEquals(1, failedTests.size());
	// String expected =
	// "org.jfree.chart.renderer.category.junit.AbstractCategoryItemRendererTests::test2947660";
	// String actural = failedTests.get(0);
	// Assert.assertTrue(expected.equals(actural));
	//
	// }
	//
}
