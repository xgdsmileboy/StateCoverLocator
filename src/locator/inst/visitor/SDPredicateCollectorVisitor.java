/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import locator.aux.extractor.CodeAnalyzer;
import locator.common.config.Constant;

/**
 * 
 * @author Jiajun
 * @date Aug 16, 2018
 */
public class SDPredicateCollectorVisitor extends TraversalVisitor {

	private Set<Integer> _lines = null;
	private String _srcPath = "";
	private String _relJavaPath = "";
	private Map<Integer, Set<String>> _predicates = new HashMap<>();
	
	public SDPredicateCollectorVisitor() {
	}
	
	public void initOneRun(Set<Integer> lines, String srcPath, String relJavaPath) {
		_lines = lines;
		_srcPath = srcPath;
		_relJavaPath = relJavaPath;
		clear();
	}
	
	public Map<Integer, Set<String>> getPredicates() {
		return _predicates;
	}
	
	private void clear() {
		_predicates = new HashMap<>();
	}
	
	public boolean visit(ReturnStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (_lines.contains(start)) {
			Expression expr = node.getExpression();
			if (expr != null && isComparableType(expr.resolveTypeBinding())) {
				String condition = expr.toString().replace("\n", " ").replaceAll("\\s+", " ");
				addPredicates(getPredicateForReturns(condition), start);
			}
		}
		return true;
	}
	
	public boolean visit(IfStatement node) {
		int start = _cu.getLineNumber(node.getExpression().getStartPosition());
		if (_lines.contains(start)) {
			Expression expr = node.getExpression();
			String condition = expr.toString().replaceAll("\\s+", " ");
			addPredicates(getPredicateForConditions(condition), start);
		}
		return true;
	}
	
	public boolean visit(ForStatement node) {
		int position = node.getStartPosition();
		if (node.getExpression() != null) {
			position = node.getExpression().getStartPosition();
		} else if (node.initializers() != null && node.initializers().size() > 0) {
			position = ((ASTNode) node.initializers().get(0)).getStartPosition();
		} else if (node.updaters() != null && node.updaters().size() > 0) {
			position = ((ASTNode) node.updaters().get(0)).getStartPosition();
		}
		int start = _cu.getLineNumber(position);
		if (_lines.contains(start)) {
			Expression expr = node.getExpression();
			if(expr != null) {
				String condition = expr.toString().replaceAll("\\s+", " ");
				addPredicates(getPredicateForConditions(condition), start);
			}
		}
		return true;
	}
	
	public boolean visit(WhileStatement node) {
		int start = _cu.getLineNumber(node.getExpression().getStartPosition());
		if (_lines.contains(start)) {
			Expression expr = node.getExpression();
			String condition = expr.toString().replaceAll("\\s+", " ");
			if (condition.equals("true")) {
				return true;
			}
			addPredicates(getPredicateForConditions(condition), start);
		}
		return true;
	}
	
	public boolean visit(DoStatement node) {
		int start = _cu.getLineNumber(node.getExpression().getStartPosition());
		if (_lines.contains(start)) {
			Expression expr = node.getExpression();
			String condition = expr.toString().replaceAll("\\s+", " ");
			if (condition.equals("true")) {
				return true;
			}
			addPredicates(getPredicateForConditions(condition), start);
		}
		return true;
	}
	
//	public boolean visit(SwitchStatement node) {
//		int start = _cu.getLineNumber(node.getExpression().getStartPosition());
//		if (start == _line) {
//			Expression expr = node.getExpression();
//			String condition = expr.toString();
//			_predicates.add(getPredicateForConditions(condition));
//		}
//		return true;
//	}
	
	public boolean visit(Assignment node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (_lines.contains(start)) {
			Expression expr = node.getRightHandSide();
			if(expr instanceof NumberLiteral || expr instanceof BooleanLiteral || expr instanceof CharacterLiteral) {
				return true;
			}
			if (node.getLeftHandSide() != null && expr != null) {
				String lhString = node.getLeftHandSide().toString();
//				ITypeBinding type = expr.resolveTypeBinding();
				ITypeBinding type = node.getLeftHandSide().resolveTypeBinding();
				if (isComparableType(type)) {
					String rightExprStr = expr.toString().replaceAll("\\s+", " ");
					Set<String> variables = new HashSet<>();
					Map<String, String> availableVars = CodeAnalyzer.getAllLocalVariablesAvailableWithType(_srcPath, _relJavaPath, start, true);
					for(Entry<String, String> entry : availableVars.entrySet()) {
						String varName = entry.getKey();
						if(!lhString.equals(varName) && !varName.equals(rightExprStr) && entry.getValue().equals(type.getName())) {
							variables.add(entry.getKey());
						}
					}
					if (!variables.isEmpty()) {						
						addPredicates(getPredicatesForAssignment(rightExprStr, variables), start);
					}
				} else if(Constant.BOOL_ADD_NULL_PREDICATE_FOR_ASSGIN) {
					List<String> predicates = new ArrayList<>(1);
					predicates.add(expr.toString() + " = null");
					addPredicates(predicates, start);
				}
			}
		}
		return true;
	}
	
	public boolean visit(VariableDeclarationStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (_lines.contains(start)) {
			List<VariableDeclarationFragment> fragments = node.fragments();
			for(VariableDeclarationFragment fragment : fragments) {
				Expression expr = fragment.getInitializer();
				if (expr != null) {
					if (expr instanceof NumberLiteral || expr instanceof BooleanLiteral
							|| expr instanceof CharacterLiteral) {
						continue;
					}
					String rightExprStr = expr.toString();
					ITypeBinding type = expr.resolveTypeBinding();
					String typeStr = null;
					if(type != null) {
						if (isComparableType(type)) {
							typeStr = type.getName();
						}
					} else if(fragment.resolveBinding() != null) {
						type = fragment.resolveBinding().getType();
						if(type != null && isComparableType(type)) {
							typeStr = type.getName();
						}
					}
					if (typeStr != null) {
						int line = _cu.getLineNumber(fragment.getStartPosition());
						Set<String> variables = new HashSet<>();
						Map<String, String> varAvailable = CodeAnalyzer.getAllLocalVariablesAvailableWithType(_srcPath, _relJavaPath, line, true);
						for(Entry<String, String> entry : varAvailable.entrySet()) {
							if(!entry.getKey().equals(rightExprStr) && entry.getValue().equals(type.getName())) {
								variables.add(entry.getKey());
							}
						}
						
						if (!variables.isEmpty()) {							
							addPredicates(getPredicatesForAssignment(rightExprStr, variables), start);
						}
					}
				}
			}
		}
		return true;
	}
	
	private List<String> getPredicateForReturns(final String expr) {
		List<String> predicates = new ArrayList<String>();
		final String operators[] = { "< 0", "<= 0", "> 0", ">= 0", "== 0",
				"!= 0" };
		for (final String op : operators) {
			predicates.add("(" + expr + ")" + op);
		}
		return predicates;
	}
	
	private List<String> getPredicateForConditions(final String expr) {
		List<String> predicates = new ArrayList<String>();
		predicates.add(expr);
		predicates.add("!(" + expr + ")");
		return predicates;
	}
	
	private List<String> getPredicatesForAssignment(final String var1, final Set<String> variables) {
		final String operators[] = {" < ", " <= ", " > ", " >= ", " == ", " != "};
		List<String> predicates = new ArrayList<String>();
		for(final String variable : variables) {
			for (final String op : operators) {
				predicates.add(var1 + op + variable);
			}
		}
		return predicates;
	}
	
	private void addPredicates(List<String> predicates, int line) {
		Set<String> predicateSet = _predicates.get(line);
		if (predicateSet == null) {
			predicateSet = new HashSet<>();
			_predicates.put(line, predicateSet);
		}
		predicateSet.addAll(predicates);
	}
	
	private boolean isComparableType(ITypeBinding type) {
		if (type == null) {
			return false;
		}
		switch(type.getName()) {
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
	
}
