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

import locator.common.config.Constant;
import locator.common.java.JavaFile;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class StatementInstrumentVisitorTest {
	
	@Test
	public void test_InstrumentForTestClass(){
		String filePath = "res/junitRes/IterativeLegendreGaussIntegratorTest.java";
		CompilationUnit compilationUnit = JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		StatementInstrumentVisitor statementInstrumentVisitor = new StatementInstrumentVisitor();
		statementInstrumentVisitor.setFlag(Constant.INSTRUMENT_K_TEST);
		compilationUnit.accept(statementInstrumentVisitor);
		System.out.println(compilationUnit.toString());
	}
	
	@Test
	public void test_InstrumentForSourceClass(){
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		compilationUnit.accept(new StatementInstrumentVisitor());
		System.out.println(compilationUnit.toString());
	}
	
}
