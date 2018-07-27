/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package locator.aux.extractor.core.parser;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jiajun
 *
 * Jul 15, 2018
 */
public class AnalyzerTest {

	private String base = System.getProperty("user.dir");
	
	@Test
	public void test_parse_file() {
		String srcBase = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String file = "org/apache/commons/math3/util/MathArrays.java";
		BasicBlock basicBlock = Analyzer.analyze(file, srcBase);
		basicBlock.dump();
	}
	
	@Test
	public void test_getValidVariables() {
		String srcBase = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String file = "org/apache/commons/math3/util/MathArrays.java";
		BasicBlock basicBlock = Analyzer.analyze(file, srcBase);
		Set<Variable> variables = basicBlock.getAllValidVariables(821);
		for(Variable variable : variables) {
			System.out.println(variable.getName() + "(" + variable.getType() + ")");
		}
	}
	
	@Test
	public void test_getValidVariables_withType() {
		String srcBase = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String file = "org/apache/commons/math3/util/MathArrays.java";
		BasicBlock basicBlock = Analyzer.analyze(file, srcBase);
		Set<Variable> variables = basicBlock.getAllValidVariables(821, "double[]");
		for(Variable variable : variables) {
			System.out.println(variable.getName() + "(" + variable.getType() + ")");
		}
	}
	
	@Test
	public void test_getUsedVariables() {
		String srcBase = base + "/res/junitRes/math/math_3_buggy/src/main/java";
		String file = "org/apache/commons/math3/util/MathArrays.java";
		BasicBlock basicBlock = Analyzer.analyze(file, srcBase);
		Set<Use> uses = basicBlock.getVariableUses(821);
		Assert.assertTrue(uses.size() == 0);
		
		uses = basicBlock.getVariableUses(826);
		Assert.assertTrue(uses.size() == 3);
		System.out.println("Should be three ----------------------------");
		for(Use use : uses) {
			System.out.println(use.getVariableDefine().getName() + "(" + use.getVariableDefine().getType() + ")");
		}
		
		uses = basicBlock.getVariableUses(849);
		Assert.assertTrue(uses.size() == 5);
		System.out.println("Should be five ----------------------------");
		for(Use use : uses) {
			System.out.println(use.getVariableDefine().getName() + "(" + use.getVariableDefine().getType() + ")");
		}
	}
	
}
