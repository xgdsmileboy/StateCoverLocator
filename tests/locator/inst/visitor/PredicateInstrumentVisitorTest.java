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
 * @date May 12, 2017
 */
public class PredicateInstrumentVisitorTest {
	
	@Test
	public void test_predicateInstrumentForClazz(){
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		PredicateInstrumentVisitor predicateInstrumentVisitor = new PredicateInstrumentVisitor("a > b", 23);
		compilationUnit.accept(predicateInstrumentVisitor);
		String expected = JavaFile.readFileToString("res/junitRes/testOracle/BigFraction-PIns1.java");
		Assert.assertTrue(compilationUnit.toString().equals(expected));
	}
	
}
