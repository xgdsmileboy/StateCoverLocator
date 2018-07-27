/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package locator.aux.extractor.core.parser;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import locator.aux.extractor.core.feature.VarFeature;

/**
 * @author Jiajun
 *
 * Jul 24, 2018
 */
public class BasicBlockTest {

	private String base = System.getProperty("user.dir");
	
	@Test
	public void test_getUsedVarFeatures_empty() {
		String srcBase = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String file = "org/apache/commons/math3/util/MathArrays.java";
		BasicBlock basicBlock = Analyzer.analyze(file, srcBase);
		List<VarFeature> varFeatures = basicBlock.getUsedVarFeatures(821);
		Assert.assertTrue(varFeatures.size() == 0);
		for(VarFeature varFeature : varFeatures) {
			System.out.println(varFeature.toStringFormat());
		}
	}
	
	@Test
	public void test_getUsedVarFeatures_notEmpty() {
		String srcBase = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String file = "org/apache/commons/math3/util/MathArrays.java";
		BasicBlock basicBlock = Analyzer.analyze(file, srcBase);
		List<VarFeature> varFeatures = basicBlock.getUsedVarFeatures(823);
		Assert.assertTrue(varFeatures.size() == 1);
		for(VarFeature varFeature : varFeatures) {
			System.out.println(varFeature.getFeatureHeader());
			System.out.println(varFeature.toStringFormat());
		}
	}	
	
	@Test
	public void test_getUsedVarFeatures_if() {
		String srcBase = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String file = "org/apache/commons/math3/util/MathArrays.java";
		BasicBlock basicBlock = Analyzer.analyze(file, srcBase);
		List<VarFeature> varFeatures = basicBlock.getUsedVarFeatures(817);
		Assert.assertTrue(varFeatures.size() == 2);
		System.out.println(varFeatures.get(0).getFeatureHeader());
		for(VarFeature varFeature : varFeatures) {
			System.out.println(varFeature.toStringFormat());
		}
	}	
	
	@Test
	public void test_getUsedVarFeatures_if2() {
		String srcBase = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String file = "org/apache/commons/math3/util/MathArrays.java";
		BasicBlock basicBlock = Analyzer.analyze(file, srcBase);
		List<VarFeature> varFeatures = basicBlock.getUsedVarFeatures(862);
		Assert.assertTrue(varFeatures.size() == 1);
		System.out.println(varFeatures.get(0).getFeatureHeader());
		for(VarFeature varFeature : varFeatures) {
			System.out.println(varFeature.toStringFormat());
		}
	}
	
	@Test
	public void test_getUsedVarFeatures_for() {
		String srcBase = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String file = "org/apache/commons/math3/util/MathArrays.java";
		BasicBlock basicBlock = Analyzer.analyze(file, srcBase);
		List<VarFeature> varFeatures = basicBlock.getUsedVarFeatures(826);
		Assert.assertTrue(varFeatures.size() == 3);
		System.out.println(varFeatures.get(0).getFeatureHeader());
		for(VarFeature varFeature : varFeatures) {
			System.out.println(varFeature.toStringFormat());
		}
	}	
	
}
