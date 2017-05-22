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
import org.junit.Assert;
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
	public void test_deInstrumentSingleMethod() {
		String path = "res/junitRes/ForDeInstrument.java";
		String methodString = "org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegratorTest#void#testSinFunction#?";
		CompilationUnit unit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(path),
				ASTParser.K_COMPILATION_UNIT);
		DeInstrumentVisitor deInstrumentVisitor = new DeInstrumentVisitor();
		Set<Method> methods = new HashSet<>();
		methods.add(new Method(Identifier.getIdentifier(methodString)));

		deInstrumentVisitor.setMethod(methods);
		unit.accept(deInstrumentVisitor);
		Assert.assertTrue(InstrumentCount.getInstrumentCount(unit) == 18);

		String anotherMethod = "org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegratorTest#void#testQuinticFunction#?";
		methods.add(new Method(Identifier.getIdentifier(anotherMethod)));
		deInstrumentVisitor.setMethod(methods);
		unit.accept(deInstrumentVisitor);
		Assert.assertTrue(InstrumentCount.getInstrumentCount(unit) == 0);
	}

	@Test
	public void test_deInstrumentTestClass() {
		String path = "res/junitRes/ForDeInstrument.java";
		CompilationUnit unit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(path),
				ASTParser.K_COMPILATION_UNIT);
		unit.accept(new DeInstrumentVisitor());
		Assert.assertTrue(InstrumentCount.getInstrumentCount(unit) == 0);
	}

}
