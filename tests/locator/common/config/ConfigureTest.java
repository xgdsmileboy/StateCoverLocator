/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.config;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import locator.common.java.Subject;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class ConfigureTest {
	
	@Test
	public void test_getSubjectFromXML(){
		List<Subject> list = Configure.getSubjectFromXML();
		for (Subject subject : list) {
			System.out.println(subject);
		}
	}
	
	@Test
	public void test_config_dumper(){
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "/tests", "build", "build-tests");
		Configure.config_dumper(subject);
		File file = new File("res/junitRes/chart/chart_1_buggy/source/auxiliary/Dumper.java");
		Assert.assertTrue("Auxiliary file is not copy right.", file.exists());
	}
	
}
