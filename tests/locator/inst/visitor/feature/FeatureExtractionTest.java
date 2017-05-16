/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor.feature;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import locator.common.java.Pair;

/**
 * @author Jiajun
 * @date May 16, 2017
 */
public class FeatureExtractionTest {
	
	@Test
	public void test_print(){
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 48);
		for (String string : vars.getFirst()) {
			System.out.println(string);
		}
		for (String string : vars.getSecond()) {
			System.out.println(string);
		}
	}
	
	@Test
	public void test_extractAllFeatures(){
		//test variable declaration statement " List keys=dataset.getKeys();"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 48);
		List<String> varFeature = vars.getFirst();
		List<String> expFeature = vars.getSecond();
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 2);
		Assert.assertTrue(varFeature.get(0).equals("x	48	0	DatasetUtilities.java	calculatePieDatasetTotal	keys	List	getKeys()	0	0	NO_USE	?"));
		Assert.assertTrue(varFeature.get(1).equals("x	48	0	DatasetUtilities.java	calculatePieDatasetTotal	dataset	PieDataset	PARAM_AS	1	1	CALLER_USE	?"));
		Assert.assertTrue(expFeature.get(0).equals("x	48	0	DatasetUtilities.java	calculatePieDatasetTotal	keys	List	0	DEF	?"));
		Assert.assertTrue(expFeature.get(1).equals("x	48	0	DatasetUtilities.java	calculatePieDatasetTotal	dataset	PieDataset	0	DEF	?"));
	}
	
	@Test
	public void test_extractAllFeatures_for(){
		//test for expression " for (int current=0; current < columnCount; current++)"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 85);
		List<String> varFeature = vars.getFirst();
		List<String> expFeature = vars.getSecond();
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 2);
	}
	
	@Test
	public void test_extractAllFeatures_vdf(){
		//test variable declaration statement "int column=dataset.getColumnIndex(columnKey);"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 98);
		List<String> varFeature = vars.getFirst();
		List<String> expFeature = vars.getSecond();
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 2);
	}
	
	@Test
	public void test_extractAllFeatures_ret(){
		//test return statement "return createPieDatasetForRow(dataset,row);"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 74);
		List<String> varFeature = vars.getFirst();
		List<String> expFeature = vars.getSecond();
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 2);
	}
	
	@Test
	public void test_extractAllFeatures_minvoke(){
		//test method invocation statement "result.setValue(columnKey,dataset.getValue(row,current));"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 87);
		List<String> varFeature = vars.getFirst();
		List<String> expFeature = vars.getSecond();
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 5);
	}
	
	@Test
	public void test_extractAllFeatures_if(){
		//test if statement "if (value / total < minimumPercent) {"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 145);
		List<String> varFeature = vars.getFirst();
		List<String> expFeature = vars.getSecond();
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 3);
	}
	
	@Test
	public void test_extractAllFeatures_throw(){
		//test thow statement "throw new IllegalArgumentException("Null 'f' argument.");"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 290);
		List<String> varFeature = vars.getFirst();
		List<String> expFeature = vars.getSecond();
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 0);
	}
	
	@Test
	public void test_extractAllFeatures_Const(){
		//test constant statement "double minimum=Double.POSITIVE_INFINITY;"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 824);
		List<String> varFeature = vars.getFirst();
		List<String> expFeature = vars.getSecond();
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 0);
	}
	
	@Test
	public void test_extractAllFeatures_switch(){
		//test switch statement "switch(a){"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 1574);
		List<String> varFeature = vars.getFirst();
		List<String> expFeature = vars.getSecond();
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 1);
	}
	
	@Test
	public void test_extractAllFeatures_case(){
		//test switch case statement "int b = a --;"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 1577);
		List<String> varFeature = vars.getFirst();
		List<String> expFeature = vars.getSecond();
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 1);
	}
	
	@Test
	public void test_extractAllFeatures_assignment(){
		//test assignment statement "runningTotal=runningTotal + value;"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 1558);
		List<String> varFeature = vars.getFirst();
		List<String> expFeature = vars.getSecond();
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 2);
	}
}
