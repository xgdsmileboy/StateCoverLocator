/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public void test_instrumentSinglePredicate(){
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		String methodString = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		Map<Integer, List<String>> conditionMap = new HashMap<>();
		List<String> conditionList1 = new ArrayList<>();
		conditionList1.add("den > 0");
		conditionMap.put(23, conditionList1);
		int methodID = Identifier.getIdentifier(methodString);
		Map<Integer, Map<Integer, List<String>>> predicateMap = new HashMap<>();
		predicateMap.put(methodID, conditionMap);
		InstrumentAllPredicatesVisitor instrumentAllPredicatesVisitor = new InstrumentAllPredicatesVisitor(predicateMap);
		compilationUnit.accept(instrumentAllPredicatesVisitor);
		System.out.println(compilationUnit.toString());
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 1);
	}
	
	@Test
	public void test_instrumentMultiPredicatesForSingleMethod(){
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		String methodString = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		Map<Integer, List<String>> conditionMap = new HashMap<>();
		List<String> conditionList1 = new ArrayList<>();
		conditionList1.add("den > 0");
		conditionMap.put(23, conditionList1);
		List<String> conditionList2 = new ArrayList<>();
		conditionList2.add("num <= 0");
		conditionList2.add("num > 10");
		conditionMap.put(26, conditionList2);
		int methodID = Identifier.getIdentifier(methodString);
		Map<Integer, Map<Integer, List<String>>> predicateMap = new HashMap<>();
		predicateMap.put(methodID, conditionMap);
		InstrumentAllPredicatesVisitor instrumentAllPredicatesVisitor = new InstrumentAllPredicatesVisitor(predicateMap);
		compilationUnit.accept(instrumentAllPredicatesVisitor);
		System.out.println(compilationUnit.toString());
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 3);
	}
	
	@Test
	public void test_instrumentMultiPredicatesForMultiMethod(){
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		//for the first method
		String methodString = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		Map<Integer, List<String>> conditionMap = new HashMap<>();
		List<String> conditionList1 = new ArrayList<>();
		conditionList1.add("den > 0");
		conditionMap.put(23, conditionList1);
		List<String> conditionList2 = new ArrayList<>();
		conditionList2.add("num <= 0");
		conditionList2.add("num > 10");
		conditionMap.put(26, conditionList2);
		int methodID = Identifier.getIdentifier(methodString);
		Map<Integer, Map<Integer, List<String>>> predicateMap = new HashMap<>();
		predicateMap.put(methodID, conditionMap);
		
		//for the second method
		String anotherMethodString = "org.apache.commons.math3.fraction.BigFraction$TroubleClazz#boolean#method2#?";
		Map<Integer, List<String>> anotherConditionMap = new HashMap<>();
		List<String> anotherConditionList = new ArrayList<>();
		anotherConditionList.add("true");
		anotherConditionMap.put(49, anotherConditionList);
		int anotherMethodID = Identifier.getIdentifier(anotherMethodString);
		Map<Integer, Map<Integer, List<String>>> anotherPredicateMap = new HashMap<>();
		predicateMap.put(anotherMethodID, anotherConditionMap);
		
		InstrumentAllPredicatesVisitor instrumentAllPredicatesVisitor = new InstrumentAllPredicatesVisitor(predicateMap);
		compilationUnit.accept(instrumentAllPredicatesVisitor);
		System.out.println(compilationUnit.toString());
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 4);
	}
	
	@Test
	public void test_instrumentPredicateFomWrongClass(){
		String filePath = "res/junitRes/Test.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		String methodString = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		Map<Integer, List<String>> conditionMap = new HashMap<>();
		List<String> conditionList1 = new ArrayList<>();
		conditionList1.add("den > 0");
		conditionMap.put(23, conditionList1);
		int methodID = Identifier.getIdentifier(methodString);
		Map<Integer, Map<Integer, List<String>>> predicateMap = new HashMap<>();
		predicateMap.put(methodID, conditionMap);
		InstrumentAllPredicatesVisitor instrumentAllPredicatesVisitor = new InstrumentAllPredicatesVisitor(predicateMap);
		compilationUnit.accept(instrumentAllPredicatesVisitor);
		System.out.println(compilationUnit.toString());
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 0);
	}
	
	@Test
	public void test_instrumentPredicateInEmptyLine(){
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		String methodString = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		Map<Integer, List<String>> conditionMap = new HashMap<>();
		List<String> conditionList1 = new ArrayList<>();
		conditionList1.add("den > 0");
		conditionMap.put(47, conditionList1);
		int methodID = Identifier.getIdentifier(methodString);
		Map<Integer, Map<Integer, List<String>>> predicateMap = new HashMap<>();
		predicateMap.put(methodID, conditionMap);
		InstrumentAllPredicatesVisitor instrumentAllPredicatesVisitor = new InstrumentAllPredicatesVisitor(predicateMap);
		compilationUnit.accept(instrumentAllPredicatesVisitor);
		System.out.println(compilationUnit.toString());
		Assert.assertTrue(InstrumentCount.getInstrumentCount(compilationUnit) == 0);
	}
	
}
