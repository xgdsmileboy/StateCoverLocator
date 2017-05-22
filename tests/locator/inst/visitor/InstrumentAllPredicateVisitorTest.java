/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Assert;
import org.junit.Test;

import locator.common.config.Identifier;
import locator.common.java.JavaFile;

/**
 * @author Jiajun
 * @date May 12, 2017
 */
public class InstrumentAllPredicateVisitorTest {

	@Test
	public void test_instrumentSinglePredicate() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		String methodString = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		Map<Integer, Set<String>> conditionMap = new HashMap<>();
		Set<String> conditionSet1 = new HashSet<>();
		conditionSet1.add("den > 0");
		conditionMap.put(23, conditionSet1);
		int methodID = Identifier.getIdentifier(methodString);
		Map<Integer, Map<Integer, Set<String>>> predicateMap = new HashMap<>();
		predicateMap.put(methodID, conditionMap);
		InstrumentAllPredicatesVisitor instrumentAllPredicatesVisitor = new InstrumentAllPredicatesVisitor(
				predicateMap);
		compilationUnit.accept(instrumentAllPredicatesVisitor);
		System.out.println(compilationUnit.toString());
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 1);
	}

	@Test
	public void test_instrumentMultiPredicatesForSingleMethod() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		String methodString = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		Map<Integer, Set<String>> conditionMap = new HashMap<>();
		Set<String> conditionSet1 = new HashSet<>();
		conditionSet1.add("den > 0");
		conditionMap.put(23, conditionSet1);
		Set<String> conditionSet2 = new HashSet<>();
		conditionSet2.add("num <= 0");
		conditionSet2.add("num > 10");
		conditionMap.put(26, conditionSet2);
		int methodID = Identifier.getIdentifier(methodString);
		Map<Integer, Map<Integer, Set<String>>> predicateMap = new HashMap<>();
		predicateMap.put(methodID, conditionMap);
		InstrumentAllPredicatesVisitor instrumentAllPredicatesVisitor = new InstrumentAllPredicatesVisitor(
				predicateMap);
		compilationUnit.accept(instrumentAllPredicatesVisitor);
		System.out.println(compilationUnit.toString());
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 3);
	}

	@Test
	public void test_instrumentMultiPredicatesForMultiMethod() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		// for the first method
		String methodString = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		Map<Integer, Set<String>> conditionMap = new HashMap<>();
		Set<String> conditionSet1 = new HashSet<>();
		conditionSet1.add("den > 0");
		conditionMap.put(23, conditionSet1);
		Set<String> conditionSet2 = new HashSet<>();
		conditionSet2.add("num <= 0");
		conditionSet2.add("num > 10");
		conditionMap.put(26, conditionSet2);
		int methodID = Identifier.getIdentifier(methodString);

		// for the second method
		String anotherMethodString = "org.apache.commons.math3.fraction.BigFraction$TroubleClazz#boolean#method2#?";
		Map<Integer, Set<String>> anotherConditionMap = new HashMap<>();
		Set<String> anotherConditionSet = new HashSet<>();
		anotherConditionSet.add("true");
		anotherConditionMap.put(49, anotherConditionSet);
		int anotherMethodID = Identifier.getIdentifier(anotherMethodString);

		Map<Integer, Map<Integer, Set<String>>> predicateMap = new HashMap<>();
		predicateMap.put(methodID, conditionMap);
		predicateMap.put(anotherMethodID, anotherConditionMap);

		InstrumentAllPredicatesVisitor instrumentAllPredicatesVisitor = new InstrumentAllPredicatesVisitor(
				predicateMap);
		compilationUnit.accept(instrumentAllPredicatesVisitor);
		System.out.println(compilationUnit.toString());
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 4);
	}

	@Test
	public void test_instrumentPredicateFomWrongClass() {
		String filePath = "res/junitRes/Test.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		String methodString = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		Map<Integer, Set<String>> conditionMap = new HashMap<>();
		Set<String> conditionSet = new HashSet<>();
		conditionSet.add("den > 0");
		conditionMap.put(23, conditionSet);
		int methodID = Identifier.getIdentifier(methodString);
		Map<Integer, Map<Integer, Set<String>>> predicateMap = new HashMap<>();
		predicateMap.put(methodID, conditionMap);
		InstrumentAllPredicatesVisitor instrumentAllPredicatesVisitor = new InstrumentAllPredicatesVisitor(
				predicateMap);
		compilationUnit.accept(instrumentAllPredicatesVisitor);
		System.out.println(compilationUnit.toString());
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 0);
	}

	@Test
	public void test_instrumentPredicateInEmptyLine() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		String methodString = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		Map<Integer, Set<String>> conditionMap = new HashMap<>();
		Set<String> conditionSet = new HashSet<>();
		conditionSet.add("den > 0");
		conditionMap.put(47, conditionSet);
		int methodID = Identifier.getIdentifier(methodString);
		Map<Integer, Map<Integer, Set<String>>> predicateMap = new HashMap<>();
		predicateMap.put(methodID, conditionMap);
		InstrumentAllPredicatesVisitor instrumentAllPredicatesVisitor = new InstrumentAllPredicatesVisitor(
				predicateMap);
		compilationUnit.accept(instrumentAllPredicatesVisitor);
		System.out.println(compilationUnit.toString());
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 0);
	}

}
