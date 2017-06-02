/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Assert;
import org.junit.Test;

import locator.common.java.JavaFile;

/**
 * @author Jiajun
 * @date Jun 2, 2017
 */
public class NewPredicateInstrumentVisitorTest {

	@Test
	public void test_predicateInstrumentForClazz() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		NewPredicateInstrumentVisitor predicateInstrumentVisitor = new NewPredicateInstrumentVisitor("a > b", 23);
		compilationUnit.accept(predicateInstrumentVisitor);
		System.out.println(compilationUnit);
//		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 1);
		compilationUnit.accept(new DeInstrumentVisitor());
		System.out.println(compilationUnit);
	}
	
}
