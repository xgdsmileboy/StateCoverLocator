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
		List<Subject> list = Configure.getSubjectFromXML("res/junitRes/test.xml");
		Assert.assertTrue(list.size() == 3);
		
		String expected0 = "[_name=chart, _id=1, _ssrc=/source, _tsrc=/tests, _sbin=/build, _tbin=/build-tests]";
		String actural0 = list.get(0).toString();
		Assert.assertTrue("expected : " + expected0 + "\n" + "actural : " + actural0, actural0.equals(expected0));
		
		String expected1 = "[_name=lang, _id=1, _ssrc=null, _tsrc=/src/test/java, _sbin=/target/classes, _tbin=/target/tests]";
		String actural1 = list.get(1).toString();
		Assert.assertTrue("expected : " + expected1 + "\n" + "actural : " + actural1, actural1.equals(expected1));
		
		Assert.assertTrue(list.get(2).getName().equals("math"));
		Assert.assertTrue(list.get(2).getId() == 1);
		Assert.assertTrue(list.get(2).getSbin().equals("/target/classes"));
		Assert.assertTrue(list.get(2).getSsrc().equals(""));
		Assert.assertTrue(list.get(2).getTsrc().equals("/src/test/java"));
		Assert.assertTrue(list.get(2).getTbin().equals("/target/test-classes"));
		
	}
	
	@Test
	public void test_getSubjectFromXMLFailed(){
		try{
			List<Subject> list = Configure.getSubjectFromXML("res/junitRes/failed.xml");
			Assert.fail();
		} catch(NumberFormatException e) {
			Assert.assertTrue(e.getMessage().equals("Parse id failed!"));
		}
	}
	
	@Test
	public void test_config_dumper(){
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "/tests", "build", "build-tests");
		Configure.config_dumper(subject);
		File file = new File("res/junitRes/chart/chart_1_buggy/source/auxiliary/Dumper.java");
		Assert.assertTrue("Auxiliary file is not copied right.", file.exists());
	}
	
}
