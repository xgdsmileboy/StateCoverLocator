/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import locator.common.config.Identifier;
import locator.common.java.JavaFile;

/**
 * @author Jiajun
 * @date Jun 2, 2017
 */
public class TestMethodInstrumentVisitorTest {

	@Test
	public void test_InstrumentForTestClass(){
		String filePath = "res/junitRes/Test.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		String methodString = "org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegratorTest#void#testNormalDistributionWithLargeSigma#?";
		Set<Integer> methods = new HashSet<>();
		methods.add(Identifier.getIdentifier(methodString));
		compilationUnit.accept(new TestMethodInstrumentVisitor(methods, false));
		System.out.println(compilationUnit);
		
	}
	
}
