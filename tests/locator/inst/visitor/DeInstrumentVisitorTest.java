/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import locator.common.config.Identifier;
import locator.common.java.JavaFile;
import locator.common.java.Method;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class DeInstrumentVisitorTest {

	@Test
	public void test_deInstrumentSingleMethod(){
		String path = "res/junitRes/ForDeInstrument.java";
		String methodString = "org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegratorTest#void#testSinFunction#?";
		CompilationUnit unit = JavaFile.genASTFromSource(JavaFile.readFileToString(path), ASTParser.K_COMPILATION_UNIT);
		DeInstrumentVisitor deInstrumentVisitor = new DeInstrumentVisitor();
		deInstrumentVisitor.setMethod(new Method(Identifier.getIdentifier(methodString)));
		unit.accept(deInstrumentVisitor);
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_deInstrumentTestClass(){
		String path = "res/junitRes/ForDeInstrument.java";
		CompilationUnit unit = JavaFile.genASTFromSource(JavaFile.readFileToString(path), ASTParser.K_COMPILATION_UNIT);
		unit.accept(new DeInstrumentVisitor());
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_deInstrumentSourceClass(){
		String path = "res/junitRes/ForDeInstrument.java";
		CompilationUnit unit = JavaFile.genASTFromSource(JavaFile.readFileToString(path), ASTParser.K_COMPILATION_UNIT);
		unit.accept(new DeInstrumentVisitor());
		System.out.println(unit.toString());
	}
	
}
