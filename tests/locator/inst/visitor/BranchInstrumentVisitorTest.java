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

import locator.common.java.JavaFile;

/**
 * @author Jiajun
 * @date Mar 20, 2018
 */
public class BranchInstrumentVisitorTest {

	@Test
	public void test_if() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		BranchInstrumentVisitor branchInstrumentVisitor = new BranchInstrumentVisitor();
		compilationUnit.accept(branchInstrumentVisitor);
		System.out.println(compilationUnit.toString());
	}
	
}
