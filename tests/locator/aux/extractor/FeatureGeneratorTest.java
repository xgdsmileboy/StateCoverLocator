/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package locator.aux.extractor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import locator.aux.extractor.core.feature.ExprFeature;
import locator.aux.extractor.core.feature.VarFeature;
import locator.core.alg.Algorithm;
import locator.core.alg.Ochiai;

/**
 * @author Jiajun
 *
 * Jul 25, 2018
 */
public class FeatureGeneratorTest {

	private String base = System.getProperty("user.dir");
	
	@Test
	public void test_generateTrainVarFeatures() {
		String baseDir = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String tarFile = base + "/out/var.csv";
		FeatureGenerator.generateTrainVarFeatures(baseDir, tarFile);
	}
	
	@Test
	public void test_generateTrainVarFeatures_chart() {
		String baseDir = base + "/res/junitRes/chart/chart_1_buggy/source";
		String tarFile = base + "/out/var.csv";
		FeatureGenerator.generateTrainVarFeatures(baseDir, tarFile);
	}
	
	@Test
	public void test_generateTrainExprFeatures() {
		String baseDir = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String tarFile = base + "/out/expr.csv";
		FeatureGenerator.generateTrainExprFeatures(baseDir, tarFile);
	}
	
	@Test
	public void test_generateVarFeatureForLine() {
		String baseDir = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String relJavaFile = "org/apache/commons/math3/util/MathArrays.java";
		int line = 823;
		List<String> features = FeatureGenerator.generateVarFeatureForLine(baseDir, relJavaFile, line);
		Assert.assertTrue(features.size() == 3);
		System.out.println(VarFeature.getFeatureHeader());
		for(String feature : features) {
			System.out.println(feature);
		}
	}
	
	@Test
	public void test_generateVarFeatureForLine_if() {
		String baseDir = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String relJavaFile = "org/apache/commons/math3/util/MathArrays.java";
		int line = 817;
		List<String> features = FeatureGenerator.generateVarFeatureForLine(baseDir, relJavaFile, line);
		Assert.assertTrue(features.size() == 3);
		System.out.println(VarFeature.getFeatureHeader());
		for(String feature : features) {
			System.out.println(feature);
		}
	}
	
	@Test
	public void test_generateVarFeatureForLine_inner_block() {
		String baseDir = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String relJavaFile = "org/apache/commons/math3/util/MathArrays.java";
		int line = 832;
		List<String> features = FeatureGenerator.generateVarFeatureForLine(baseDir, relJavaFile, line);
		Assert.assertTrue(features.size() == 10);
		System.out.println(VarFeature.getFeatureHeader());
		for(String feature : features) {
			System.out.println(feature);
		}
	}
	
	@Test
	public void test_generateExprFeatureForLine() {
		String baseDir = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String relJavaFile = "org/apache/commons/math3/util/MathArrays.java";
		int line = 823;
		List<String> features = FeatureGenerator.generateExprFeatureForLine(baseDir, relJavaFile, line);
		Assert.assertTrue(features.size() == 3);
		System.out.println(ExprFeature.getFeatureHeader());
		for(String feature : features) {
			System.out.println(feature);
		}
	}
	
	@Test
	public void test_generateVarFeatureForLine_chart() {
		String baseDir = base + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaFile = "org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java";
		int line = 1222;
		List<String> features = FeatureGenerator.generateVarFeatureForLine(baseDir, relJavaFile, line);
		Assert.assertTrue(features.size() == 17);
		System.out.println(VarFeature.getFeatureHeader());
		for(String feature : features) {
			System.out.println(feature);
		}
	}
	
	@Test
	public void test_generateExprFeatureForLine_chart() {
		String baseDir = base + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaFile = "org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java";
		int line = 1222;
		List<String> features = FeatureGenerator.generateExprFeatureForLine(baseDir, relJavaFile, line);
		Assert.assertTrue(features.size() == 17);
		System.out.println(ExprFeature.getFeatureHeader());
		for(String feature : features) {
			System.out.println(feature);
		}
	}
	
	@Test
	public void test_generateClassifierTrainData() {
		String baseDir = base + "/res/junitRes/time/time_1_buggy/src/main/java";
		String tarFile = "/home/ubuntu/Desktop/test.csv";
		String projNameForTrain = "time";
		int idForTrain = 1;
		Algorithm algorithm = new Ochiai();
		FeatureGenerator.generateTrainClassifierFeatures(baseDir, tarFile, projNameForTrain, idForTrain, algorithm);
	}
	
	@Test
	public void test_generateClassifierFeatureForLine() {
		String baseDir = base + "/res/junitRes/time/time_1_buggy/src/main/java";
		String relJavaFile = "org/joda/time/Partial.java";
		int line = 217;
		Algorithm algorithm = new Ochiai();
		Set<String> predicates = new HashSet<>();
		predicates.add("loopUnitField.isSupported() == false");
		List<String> features = FeatureGenerator.generateClassifierFeatureForLine(baseDir, relJavaFile, line, predicates, algorithm);
		for(String string : features) {
			System.out.println(string);
		}
	}
	
}
