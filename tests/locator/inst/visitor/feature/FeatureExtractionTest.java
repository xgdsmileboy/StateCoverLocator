/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor.feature;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import locator.core.run.path.LineInfo;

/**
 * @author Jiajun
 * @date May 16, 2017
 */
public class FeatureExtractionTest {

	//****************
	// 2018-1-15
	// the feature extraction process has been changed, some test cases may be not right
	// collect right variables -> collect left hand side variables 
	//****************
	
	@Test
	public void test2(){
		String path = "/Users/Jiajun/Code/Defects4J/projects/math/math_65_buggy/src/main/java";
		String relJavaPath = "org/apache/commons/math/linear/Array2DRowRealMatrix.java";
		int line = 132;
		List<String> vars = new ArrayList<String>();
		List<String> exprs = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, line, new LineInfo(), vars, exprs);
		System.out.println("Variables : ");
		for (String string : vars) {
			System.out.println(string);
		}
		System.out.println("Expressions : ");
		for (String string : exprs) {
			System.out.println(string);
		}
	}
	
	@Test
	public void test_print() {
		String path = System.getProperty("user.dir") + "/res/junitRes";
		String relJavaPath = "BigFraction.java";
		List<String> vars = new ArrayList<String>();
		List<String> exprs = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 76, new LineInfo(), vars, exprs);
		System.out.println("Variables : ");
		for (String string : vars) {
			System.out.println(string);
		}
		System.out.println("Expressions : ");
		for (String string : exprs) {
			System.out.println(string);
		}
	}
	
	@Test
	public void test_print_2() {
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		List<String> vars = new ArrayList<String>();
		List<String> exprs = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 48, new LineInfo(), vars, exprs);
		System.out.println("Variables : ");
		for (String string : vars) {
			System.out.println(string);
		}
		System.out.println("Expressions : ");
		for (String string : vars) {
			System.out.println(string);
		}
	}

	@Test
	public void test_extractAllFeatures() {
		// test variable declaration statement " List keys=dataset.getKeys();"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		List<String> varFeature = new ArrayList<String>();
		List<String> expFeature = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 48, new LineInfo(), varFeature, expFeature);
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 1);
	}

	@Test
	public void test_extractAllFeatures_for() {
		// test for expression " for (int current=0; current < columnCount;
		// current++)"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		List<String> varFeature = new ArrayList<String>();
		List<String> expFeature = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 85, new LineInfo(), varFeature, expFeature);
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 2);
	}

	@Test
	public void test_extractAllFeatures_vdf() {
		// test variable declaration statement "int
		// column=dataset.getColumnIndex(columnKey);"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		List<String> varFeature = new ArrayList<String>();
		List<String> expFeature = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 98, new LineInfo(), varFeature, expFeature);
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 2);
	}

	@Test
	public void test_extractAllFeatures_ret() {
		// test return statement "return createPieDatasetForRow(dataset,row);"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		List<String> varFeature = new ArrayList<String>();
		List<String> expFeature = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 74, new LineInfo(), varFeature, expFeature);
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 2);
	}

	@Test
	public void test_extractAllFeatures_minvoke() {
		// test method invocation statement
		// "result.setValue(columnKey,dataset.getValue(row,current));"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		List<String> varFeature = new ArrayList<String>();
		List<String> expFeature = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 87, new LineInfo(), varFeature, expFeature);
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 5);
	}

	@Test
	public void test_extractAllFeatures_if() {
		// test if statement "if (value / total < minimumPercent) {"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		List<String> varFeature = new ArrayList<String>();
		List<String> expFeature = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 145, new LineInfo(), varFeature, expFeature);
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 3);
	}

	@Test
	public void test_extractAllFeatures_throw() {
		// test thow statement "throw new IllegalArgumentException("Null 'f'
		// argument.");"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		List<String> varFeature = new ArrayList<String>();
		List<String> expFeature = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 290, new LineInfo(), varFeature, expFeature);
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 0);
	}

	@Test
	public void test_extractAllFeatures_Const() {
		// test constant statement "double minimum=Double.POSITIVE_INFINITY;"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		List<String> varFeature = new ArrayList<String>();
		List<String> expFeature = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 824, new LineInfo(), varFeature, expFeature);
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 0);
	}

	@Test
	public void test_extractAllFeatures_switch() {
		// test switch statement "switch(a){"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		List<String> varFeature = new ArrayList<String>();
		List<String> expFeature = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 1574, new LineInfo(), varFeature, expFeature);
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 1);
	}

	@Test
	public void test_extractAllFeatures_case() {
		// test switch case statement "int b = a --;"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		List<String> varFeature = new ArrayList<String>();
		List<String> expFeature = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 1577, new LineInfo(), varFeature, expFeature);
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 0);
	}

	@Test
	public void test_extractAllFeatures_assignment() {
		// test assignment statement "runningTotal=runningTotal + value;"
		String path = System.getProperty("user.dir") + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaPath = "/org/jfree/data/general/DatasetUtilities.java";
		List<String> varFeature = new ArrayList<String>();
		List<String> expFeature = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 1558, new LineInfo(), varFeature, expFeature);
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 2);
	}

	@Test
	public void test_extractAllFeatures_assignment2() {
		// test assignment statement "num = num.divide(gcd);"
		String path = System.getProperty("user.dir") + "/res/junitRes/math/math_3_buggy/src/main/java";
		String relJavaPath = "/org/apache/commons/math3/fraction/BigFraction.java";
		List<String> varFeature = new ArrayList<String>();
		List<String> expFeature = new ArrayList<String>();
		FeatureExtraction.extractAllFeatures(path, relJavaPath, 36, new LineInfo(), varFeature, expFeature);
		// System.out.println("Variables : ");
		// for (String string : vars.getFirst()) {
		// System.out.println(string);
		// }
		// System.out.println("Expressions : ");
		// for (String string : vars.getSecond()) {
		// System.out.println(string);
		// }
		Assert.assertTrue(varFeature.size() == expFeature.size());
		Assert.assertTrue(varFeature.size() == 0);
	}
}
