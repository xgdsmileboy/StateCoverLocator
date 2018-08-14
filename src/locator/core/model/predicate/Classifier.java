/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.model.predicate;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.WhileStatement;

import locator.aux.extractor.core.parser.Analyzer;
import locator.aux.extractor.core.parser.BasicBlock;
import locator.aux.extractor.core.parser.BasicBlock.BLOCKTYPE;
import locator.aux.extractor.core.parser.StmtType;
import locator.aux.extractor.core.parser.Use;
import locator.aux.extractor.core.parser.Use.USETYPE;
import locator.aux.extractor.core.parser.Variable;
import locator.common.config.Configure;
import locator.common.config.Constant;
import locator.common.java.JavaFile;
import locator.common.java.Subject;
import locator.common.util.Pair;
import locator.common.util.Utils;

/**
 * @author Jiajun
 * @date Aug 14, 2018
 */
public class Classifier {
	public Classifier() {
		
	}
	
	// fileName
	// methodName
	// varName
	// varType
	// failedTest
	// predicate
	// isUseful
	public static void buildTrainingData(Set<Subject> subjects) {
		String featureFile = "/Users/Jiajun/Desktop/temp/temp.csv";
		String header = "fileName\tmethodName\tvarName\tvatType\tpredicate\tuseful\n";
		JavaFile.writeStringToFile(featureFile, header);
		for(Subject subject : subjects) {
			Analyzer.clearCache();
			String srcPath = subject.getHome() + subject.getSsrc();
			List<String> files = JavaFile.ergodic(srcPath, new LinkedList<String>());
			Set<String> failedTests = getFailedTest(subject);
			Map<String, Set<Integer>> faultyLineMap = getAllFaultyLines(subject);
			
			for(String file : files) {
				StringBuffer stringBuffer = new StringBuffer();
				String relJavaFile = file.substring(srcPath.length() + 1);
				BasicBlock basicBlock = Analyzer.analyze(relJavaFile, srcPath);
				Set<Integer> faultyLines = faultyLineMap.get(relJavaFile);
				
				String fileName = new File(file).getName();
				System.out.println(fileName);
				Set<Variable> variables = basicBlock.recursivelyGetVariables();
				for(Variable variable : variables) {
					
					String varName = variable.getName();
					String varType = variable.getType().toString();
					
					for(Use use : variable.getUseSet()) {
						
						BasicBlock methodBlock = getMethodBlock(use);
						if(methodBlock == null) {
							continue;
						}
						String methodName = methodBlock.getBlockName();
						boolean isUseful = containsAnyLine(methodBlock.getCodeRange(), faultyLines);
						
						StmtType stmtType = use.getStmtType();
						Set<String> predicates = new HashSet<>();
						if (stmtType == StmtType.IF || stmtType == StmtType.FOR_COND || stmtType == StmtType.WHILE
								|| stmtType == StmtType.DO) {
							predicates.addAll(buildCondidtionPredicates(use));
						} else if(stmtType == StmtType.ASSIGNMENT || (stmtType == StmtType.VARDECL)) {
							
							predicates.addAll(buildAssignPredicates(varName, varType, use, basicBlock));
						} else if(stmtType == StmtType.RETURN) {
							predicates.addAll(buildReturnPredicates(use));
						}
						
						for (String predicate : predicates) {
							stringBuffer.append(Utils.strJoin("\t", fileName, methodName, varName, varType, predicate,
									String.valueOf(isUseful ? 1 : 0)) + "\n");
						}
					}
				}
				
				JavaFile.writeStringToFile(featureFile, stringBuffer.toString(), true);
			}
		}
	}
	
		
	private static Set<String> buildCondidtionPredicates(Use use) {
		Set<String> predicates = new HashSet<>();
		ASTNode expression = use.getUsedExpression();
		while(expression != null && !(expression instanceof Statement)) {
			StructuralPropertyDescriptor spd = expression.getLocationInParent();
			if(spd == IfStatement.EXPRESSION_PROPERTY || spd == ForStatement.EXPRESSION_PROPERTY ||
					spd == WhileStatement.EXPRESSION_PROPERTY || spd == DoStatement.EXPRESSION_PROPERTY) {
				predicates.add(expression.toString());
				return predicates;
			}
			expression = expression.getParent();
		}
		return predicates;
	}
	
	private static Set<String> buildReturnPredicates(Use use) {
		final String operators[] = { "< 0", "<= 0", "> 0", ">= 0", "== 0","!= 0" };
		Set<String> predicates = new HashSet<>();
		ASTNode expression = use.getUsedExpression();
		while(expression != null && !(expression instanceof Statement)) {
			StructuralPropertyDescriptor spd = expression.getLocationInParent();
			if(spd == ReturnStatement.EXPRESSION_PROPERTY) {
				Expression exp = (Expression) expression;
				if(exp.resolveTypeBinding() != null) {
					if(isComparableType(exp.resolveTypeBinding().toString())) {
						for(String operator : operators) {
							predicates.add(exp + operator);
						}
					}
				}
				return predicates;
			}
			expression = expression.getParent();
		}
		return predicates;
	}
	
	private static Set<String> buildAssignPredicates(String varName, String varType, Use use, BasicBlock basicBlock) {
		Set<String> predicates = new HashSet<>();
		if(use.getUseType() == USETYPE.DEFINE || use.getUseType() == USETYPE.WRITE) {
			if(isComparableType(varType)) {
				final String operators[] = {" < ", " <= ", " > ", " >= ", " == ", " != "};
				Set<Variable> valid = basicBlock.getAllValidVariables(use.getLineNumber());
				for(Variable v : valid) {
					if(!varName.equals(v.getName()) && varType.equals(v.getType().toString())) {
						for(String operator : operators) {
							predicates.add(varName + operator + v.getName());
						} 
					}
				}
			} else {
				predicates.add(varName + " == null");
				predicates.add(varName + " != null");
			}
		}
		return predicates;
	}
	
		
	private static boolean isComparableType(String type) {
		if (type == null) {
			return false;
		}
		switch(type) {
		case "byte":
		case "char":
		case "int":
		case "short":
		case "long":
		case "float":
		case "double":
		case "Byte":
		case "Character":
		case "Short":
		case "Integer":
		case "Long":
		case "Float":
		case "Double":
			return true;
		}
		return false;
	}
	
	private static boolean containsAnyLine(Pair<Integer, Integer> range, Set<Integer> lines) {
		if(lines == null) return false;
		for(Integer line : lines) {
			if(containsLine(range, line)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean containsLine(Pair<Integer, Integer> range, int line) {
		return range.getFirst() <= line && range.getSecond() >= line;
	}
	
	private static Set<String> getFailedTest(Subject subject) {
		Set<String> failedTest = new HashSet<>();
		//TODO : read from file
		return failedTest;
	}
	
	private static BasicBlock getMethodBlock(Use use) {
		BasicBlock basicBlock = use.getParentBlock();
		while(basicBlock != null && basicBlock.getBlockType() != BLOCKTYPE.METHOD) {
			basicBlock = basicBlock.getParent();
		}
		return basicBlock;
	}
	
	private static Map<String, Set<Integer>> getAllFaultyLines(Subject subject) {
		Map<String, Set<Integer>> map = new HashMap<>();
		Set<Integer> set = new HashSet<>();
		set.add(1797);
		map.put("org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java", set);
		return map;
	}
	
	public static void main(String[] args) {
		Constant.PROJECT_HOME = "/Users/Jiajun/Desktop/temp";
		Subject subject = Configure.getSubject("chart", 1);
		Set<Subject> subjects = new HashSet<>();
		subjects.add(subject);
		Classifier.buildTrainingData(subjects);
	}
	
}
