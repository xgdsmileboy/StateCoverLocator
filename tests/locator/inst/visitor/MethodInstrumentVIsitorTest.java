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
 * @date May 11, 2017
 */
public class MethodInstrumentVIsitorTest {

	@Test
	public void test_methodInstrumentForClazz() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		compilationUnit.accept(new MethodInstrumentVisitor());
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 10);
	}
	
	@Test
	public void test_methodInstrumentForSingleMethod() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		String methodString = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		Set<Method> methods = new HashSet<>();
		methods.add(new Method(Identifier.getIdentifier(methodString)));
		compilationUnit.accept(new MethodInstrumentVisitor(methods));
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 1);
	}
	
	@Test
	public void test_methodInstrumentForMultiMethod() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		Set<Method> methods = new HashSet<>();
		String methodString1 = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		methods.add(new Method(Identifier.getIdentifier(methodString1)));
		String methodString2 = "org.apache.commons.math3.fraction.BigFraction#BigFraction#divide#?,BigInteger";
		methods.add(new Method(Identifier.getIdentifier(methodString2)));
		String methodString3 = "org.apache.commons.math3.fraction.BigFraction$TroubleClazz#boolean#method2#?";
		methods.add(new Method(Identifier.getIdentifier(methodString3)));
		compilationUnit.accept(new MethodInstrumentVisitor(methods));
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 3);
	}

}
