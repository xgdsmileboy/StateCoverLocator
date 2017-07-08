/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.LevelLogger;

/**
 * @author Jiajun
 * @date Jun 26, 2017
 */
public class NewExprFilter {

	private static Map<String, Pair<Set<String>, String>> _typeInfo = new HashMap<>();

	private static Set<String> _legalMethods = new HashSet<>();

	static {
		_legalMethods.add("size");
		_legalMethods.add("length");
		_legalMethods.add("toString");
		_legalMethods.add("contains");
		_legalMethods.add("containsKey");
		_legalMethods.add("Math.abs");
		_legalMethods.add("Double.isInfinite");
		_legalMethods.add("Double.isNaN");
		// TODO : need to complete
	}

	public static void init(Subject subject) {
		String path = subject.getHome() + subject.getSsrc();
		List<String> fileList = JavaFile.ergodic(path, new ArrayList<>());
		MethodAndFieldCollectorVisitor visitor = new MethodAndFieldCollectorVisitor();
		for (String fileName : fileList) {
			CompilationUnit unit = JavaFile.genAST(fileName);
			unit.accept(visitor);
		}
	}

	public static String filter(String type, String varName, String condition, Map<String, String> locaLegalVar,
			String currentClassName) {
		if (varName.equals("THIS")) {
			return null;
		}

		ASTNode node = JavaFile.genASTFromSource(condition, ASTParser.K_EXPRESSION);

		ExprAnalysisVisitor visitor = new ExprAnalysisVisitor(varName, type, locaLegalVar, currentClassName);
		node.accept(visitor);

		if (visitor.hasSideEffect() || !visitor.isLegal()) {
			return null;
		}

		return node.toString();
	}

	static class MethodAndFieldCollectorVisitor extends ASTVisitor {

		private final String __name__ = "@ExprFilter$MethodAndFieldCollectorVisitor ";

		@Override
		public boolean visit(TypeDeclaration node) {
			String clazzName = node.getName().getFullyQualifiedName();
			String parent = null;
			Type superType = node.getSuperclassType();
			if (superType != null) {
				parent = superType.toString();
			}
			if (_typeInfo.containsKey(clazzName)) {
				LevelLogger.warn(__name__ + "#visitor class name conflict : " + clazzName);
			}
			Set<String> methodAndVarSet = new HashSet<>();

			for (FieldDeclaration fieldDeclaration : node.getFields()) {
				for (Object frag : fieldDeclaration.fragments()) {
					if (frag instanceof VariableDeclarationFragment) {
						VariableDeclarationFragment vdf = (VariableDeclarationFragment) frag;
						String varName = vdf.getName().getFullyQualifiedName();
						methodAndVarSet.add(varName);
					}
				}
			}

			for (MethodDeclaration methodDeclaration : node.getMethods()) {
				String methodName = methodDeclaration.getName().getFullyQualifiedName();
				methodAndVarSet.add(methodName);
			}

			Pair<Set<String>, String> pair = new Pair<Set<String>, String>(methodAndVarSet, parent);
			_typeInfo.put(clazzName, pair);

			return false;
		}
	}

	static class ExprAnalysisVisitor extends ASTVisitor {
		private boolean _legal = true;
		private boolean _hasSideEffect = false;
		private String _varName = null;
		private String _type = null;
		private Map<String, String> _localVarMap = null;
		private String _currentClass = null;

		public ExprAnalysisVisitor(String varName, String type, Map<String, String> localVarMap, String currentClass) {
			_varName = varName;
			_type = type;
			_localVarMap = localVarMap;
			_currentClass = currentClass;
		}

		public boolean isLegal() {
			return _legal;
		}

		public boolean hasSideEffect() {
			return _hasSideEffect;
		}

		public boolean visit(ArrayAccess node) {
			String name = node.getArray().toString();
			String type = _localVarMap.get(name);
			if (type == null) {
				type = "";
			}

			String index = node.getIndex().toString();
			if (!type.contains("[") || index.equals(_varName)) {
				_legal = false;
				return false;
			}
			return true;
		}

		public boolean visit(QualifiedName node) {
			String qualifier = node.getQualifier().toString();
			String name = node.getName().toString();
			if (_localVarMap.containsKey(qualifier)) {
				String type = _localVarMap.get(qualifier);
				if (isPrimitiveType(type)) {
					_legal = false;
					return false;
				}
				_legal = isField(name, _type);
				return _legal;
			}
			return true;
		}

		public boolean visit(SimpleName node) {
			String name = node.getFullyQualifiedName();
			if(Character.isUpperCase(name.charAt(0))){
				return true;
			}
			ASTNode parent = node.getParent();
			if (parent != null) {
				String parentStr = parent.toString().replace("this.", "");
				if (!parentStr.contains("." + name)) {
					if (!_localVarMap.containsKey(name) && !isField(name, _currentClass)) {
						_legal = false;
						return false;
					}
				}
			}

			return true;
		}

		public boolean visit(MethodInvocation node) {
			String name = node.getName().getFullyQualifiedName();
			if (node.getExpression() != null) {
				String expr = node.getExpression().toString();
				String type = _localVarMap.get(expr);
				if (type != null) {
					if (isPrimitiveType(type) || !isField(name, type)) {
						_legal = false;
						return false;
					}
				}
				if (Character.isUpperCase(expr.charAt(0))) {
					name = expr + "." + name;
				}
			}
			if (!_legalMethods.contains(name)) {
				_legal = false;
				return false;
			}
			return true;
		}

		public boolean visit(FieldAccess node) {
			String qualifier = node.getExpression().toString();
			String name = node.getName().toString();
			if (_localVarMap.containsKey(qualifier)) {
				String type = _localVarMap.get(qualifier);
				if (isPrimitiveType(type)) {
					_legal = false;
					return false;
				} else {
					_legal = isField(name, type);
					return _legal;
				}
			}
			return true;
		}

		public boolean visit(PrefixExpression node) {
			if (node.getOperator().equals(PrefixExpression.Operator.DECREMENT)
					|| node.getOperator().equals(PrefixExpression.Operator.INCREMENT)) {
				_hasSideEffect = true;
				return false;
			}
			return true;
		}

		public boolean visit(PostfixExpression node) {
			_hasSideEffect = true;
			return false;
		}

		public boolean visit(Assignment node) {
			_hasSideEffect = true;
			return false;
		}

		public boolean visit(InfixExpression node) {

			Expression leftExp = node.getLeftOperand();
			Expression rightExp = node.getRightOperand();

			String left = leftExp.toString();
			String right = rightExp.toString();
			if (left.equals(right) || ("this".equals(left) || "this".equals(right))) {
				_legal = false;
				return false;
			}
			;

			String leftType = _localVarMap.get(left);
			String rightType = _localVarMap.get(right);

			// parse literal
			if (leftExp instanceof BooleanLiteral) {
				leftType = "boolean";
			} else if (leftExp instanceof NumberLiteral) {
				leftType = parseNumber((NumberLiteral) leftExp);
			} else if (leftExp instanceof StringLiteral) {
				leftType = "String";
			} else if (leftExp instanceof NullLiteral) {
				if(isPrimitiveType(rightType)){
					_legal = false;
					return false;
				} else {
					return true;
				}
			}

			if (rightExp instanceof BooleanLiteral) {
				rightType = "boolean";
			} else if (rightExp instanceof NumberLiteral) {
				rightType = parseNumber((NumberLiteral) rightExp);
			} else if (rightExp instanceof StringLiteral) {
				rightType = "String";
			} else if (rightExp instanceof NullLiteral) {
				if(isPrimitiveType(leftType)){
					_legal = false;
					return false;
				} else {
					return true;
				}
			}
			
			// normalize operator and match typing
			boolean normalizeOp = true;
			String operator = node.getOperator().toString();
			switch (operator) {
			case ">=":
			case "<":
				node.setOperator(InfixExpression.Operator.GREATER_EQUALS);
				normalizeOp = false;
			case ">":
			case "<=":
				if(normalizeOp){
					node.setOperator(InfixExpression.Operator.GREATER);
					normalizeOp = false;
				}
			case "==":
			case "!=":
				if(normalizeOp){
					node.setOperator(InfixExpression.Operator.EQUALS);
					normalizeOp = false;
				}
			case "*":
			case "/":
			case "+":
			case "-":
				if ((leftType != null && !isNumber(leftType))
						|| (rightType != null && !isNumber(rightType))) {
					_legal = false;
					return false;
				}
				break;
			case "%":
			case "<<":
			case ">>":
			case ">>>":
			case "^":
			case "&":
			case "|":
				if ((leftType != null && !leftType.equals("int")) || (rightType != null && !rightType.equals("int"))) {
					_legal = false;
					return false;
				}
				break;
			case "&&":
			case "||":
				if ((leftType != null && !isBoolean(leftType)) || (rightType != null && !isBoolean(rightType))) {
					_legal = false;
					return false;
				}
				break;
			default:
				break;
			}
			
			// normalize value
			if(leftType != null && leftType.equals("int") && rightExp instanceof NumberLiteral){
				NumberLiteral literal = (NumberLiteral) rightExp;
				String value = literal.getToken();
				switch(operator){
				case ">": value = floor(value); break;
				case ">=": value = ceiling(value); break;
				case "==":
				case "!=":
					// int type compare with non-int type
					if(!isInt(value)){
						_legal = false;
						return false;
					}
				default :
					break;
				}
				AST ast = AST.newAST(AST.JLS8);
				NumberLiteral numberLiteral = ast.newNumberLiteral(value);
				node.setRightOperand((Expression) ASTNode.copySubtree(node.getAST(), numberLiteral));
			} else if(rightType != null && rightType.equals("int") && leftExp instanceof NumberLiteral){
				NumberLiteral literal = (NumberLiteral) leftExp;
				String value = literal.getToken();
				switch(operator){
				case ">": value = ceiling(value); break;
				case ">=": value = floor(value); break;
				case "==":
				case "!=":
					// int type compare with non-int type
					if(!isInt(value)){
						_legal = false;
						return false;
					}
				default :
					break;
				}
				AST ast = AST.newAST(AST.JLS8);
				NumberLiteral numberLiteral = ast.newNumberLiteral(value);
				node.setLeftOperand((Expression) ASTNode.copySubtree(node.getAST(), numberLiteral));
			}
			
			return true;
		}
		
		private boolean isInt(String number){
			try{
				Integer.parseInt(number);
				return true;
			} catch (Exception e){}
			return false;
		}
		
		private String floor(String number){
			try{
				double value = Double.parseDouble(number);
				int norm = (int)Math.floor(value);
				return String.valueOf(norm);
			} catch (Exception e){}
			return number;
		}
		
		private String ceiling(String number){
			try{
				double value = Double.parseDouble(number);
				int norm = (int)Math.ceil(value);
				return String.valueOf(norm);
			} catch (Exception e){}
			return number;
		}
		
		private String parseNumber(NumberLiteral node) {
			String token = node.getToken();
			try {
				Integer.parseInt(token);
				return "int";
			} catch (Exception e) {
			}
			try {
				Long.parseLong(token);
				return "long";
			} catch (Exception e) {
			}
			try {
				Float.parseFloat(token);
				return "float";
			} catch (Exception e) {
			}
			try {
				Double.parseDouble(token);
				return "double";
			} catch (Exception e) {
			}

			return null;
		}

		private static boolean isField(String varName, String type) {
			Pair<Set<String>, String> pair = _typeInfo.get(type);
			while (pair != null) {
				if (pair.getFirst().contains(varName)) {
					return true;
				} else {
					String parent = pair.getSecond();
					if (parent != null) {
						pair = _typeInfo.get(parent);
					} else {
						pair = null;
					}
				}
			}
			return false;
		}

		private static boolean isPrimitiveType(String type) {
			if (type == null) {
				return false;
			}
			switch (type) {
			case "int":
			case "char":
			case "short":
			case "long":
			case "float":
			case "double":
			case "boolean":
			case "byte":
			case "Integer":
			case "Long":
			case "Double":
			case "Float":
			case "Character":
			case "Short":
			case "Boolean":
			case "Byte":
				return true;
			default:
				return false;
			}
		}
		
		private static boolean isNumber(String type){
			if (type == null) {
				return false;
			}
			switch (type) {
			case "int":
			case "short":
			case "long":
			case "float":
			case "double":
				return true;
			default:
				return false;
			}
		}

		private static boolean isBoolean(String type) {
			return type != null && (type.equals("boolean") || type.equals("Boolean"));
		}

	}
}
