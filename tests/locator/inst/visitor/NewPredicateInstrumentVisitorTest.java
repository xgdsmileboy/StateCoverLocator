/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import locator.common.java.JavaFile;
import locator.common.util.Pair;

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
		List<Pair<String, String>> conditions = new ArrayList<>();
		conditions.add(new Pair<String, String>("a > b", "0.5"));
		PredicateInstrumentVisitor predicateInstrumentVisitor = new PredicateInstrumentVisitor(conditions, 27);
		compilationUnit.accept(predicateInstrumentVisitor);
		System.out.println(compilationUnit);
//		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 1);
	}
	
	@Test
	public void test_predicateInstrumentForClazz_assignment() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		List<Pair<String, String>> conditions = new ArrayList<>();
		conditions.add(new Pair<String, String>("a > b", "0.5"));
		PredicateInstrumentVisitor predicateInstrumentVisitor = new PredicateInstrumentVisitor(conditions, 36);
		compilationUnit.accept(predicateInstrumentVisitor);
		System.out.println(compilationUnit);
	}
	
	@Test
	public void test_predicateInstrumentForClazz_vds() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		List<Pair<String, String>> conditions = new ArrayList<>();
		conditions.add(new Pair<String, String>("a > b", "0.5"));
		PredicateInstrumentVisitor predicateInstrumentVisitor = new PredicateInstrumentVisitor(conditions, 64);
		compilationUnit.accept(predicateInstrumentVisitor);
		System.out.println(compilationUnit);
	}
	
	@Test
	public void test_predicateInstrumentForClazz_while() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		List<Pair<String, String>> conditions = new ArrayList<>();
		conditions.add(new Pair<String, String>("a > b", "0.5"));
		PredicateInstrumentVisitor predicateInstrumentVisitor = new PredicateInstrumentVisitor(conditions, 75);
		compilationUnit.accept(predicateInstrumentVisitor);
		System.out.println(compilationUnit);
	}
	
	@Test
	public void test_predicateInstrumentForClazz_for() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		List<Pair<String, String>> conditions = new ArrayList<>();
		conditions.add(new Pair<String, String>("a > b", "0.5"));
		PredicateInstrumentVisitor predicateInstrumentVisitor = new PredicateInstrumentVisitor(conditions, 121);
		compilationUnit.accept(predicateInstrumentVisitor);
		System.out.println(compilationUnit);
	}
	
	@Test
	public void test_predicateInstrumentForClazz_do() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		List<Pair<String, String>> conditions = new ArrayList<>();
		conditions.add(new Pair<String, String>("a > b", "0.5"));
		PredicateInstrumentVisitor predicateInstrumentVisitor = new PredicateInstrumentVisitor(conditions, 128);
		compilationUnit.accept(predicateInstrumentVisitor);
		System.out.println(compilationUnit);
	}
}
