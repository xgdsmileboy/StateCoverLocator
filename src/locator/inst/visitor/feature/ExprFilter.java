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

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.validator.PublicClassValidator;

import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.LevelLogger;

/**
 * @author Jiajun
 * @date May 27, 2017
 */
public class ExprFilter {

	private static Map<String, Pair<Set<String>, String>> _typeInfo = new HashMap<>();
	
	private static Set<String> _legalMethods = new HashSet<>();

	static{
		_legalMethods.add("size");
		_legalMethods.add("length");
		_legalMethods.add("toString");
		// TODO : need to complete
	}
	
	public static void init(Subject subject) {
		String path = subject.getHome() + subject.getSsrc();
		List<String> fileList = JavaFile.ergodic(path, new ArrayList<>());
		MethodAndFieldCollectorVisitor visitor = new MethodAndFieldCollectorVisitor();
		for (String fileName : fileList) {
			CompilationUnit unit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(fileName),
					ASTParser.K_COMPILATION_UNIT);
			unit.accept(visitor);
		}
	}

	public static boolean isLegalExpr(String type, String varName, String condition, Set<String> locaLegalVarNames,
			String currentClassName) {
		if (!isPrimitiveType(type)) {

			if (varName.equals("THIS")) {
				varName = "this";
			}

			if (condition.equals(varName) || condition.startsWith(varName + " >")
					|| condition.startsWith(varName + " <")) {
				return false;
			}
			if (condition.endsWith(">= " + varName) || condition.endsWith("<= " + varName)
					|| condition.endsWith("> " + varName) || condition.endsWith("< " + varName)) {
				return false;
			}
			if (condition.contains("[" + varName + "]")) {
				return false;
			}
			if (condition.startsWith(varName + " -") || condition.startsWith(varName + " +")) {
				return false;
			}

		}

		ASTNode node = JavaFile.genASTFromSource(condition, ASTParser.K_EXPRESSION);
		
		if(node instanceof InfixExpression){
			InfixExpression infixExpression = (InfixExpression) node;
			if(infixExpression.getLeftOperand().toString().equals(infixExpression.getRightOperand().toString())){
				return false;
			}
		}
		
		ExprAnalysisVisitor visitor = new ExprAnalysisVisitor(varName, type);
		node.accept(visitor);
		if(visitor.hasSideEffect()){
			return false;
		}
		// user-defined class
		if (_typeInfo.containsKey(type) && !visitor.isLegal()) {
			return false;
		}
		Set<String> singleVars = visitor.getSingleVariables();
		for (String var : singleVars) {
			if (!locaLegalVarNames.contains(var) && !isField(var, currentClassName)) {
				return false;
			}
		}
		
		for(String methodName : visitor.getMethods()){
			if(!_legalMethods.contains(methodName)){
				return false;
			}
		}
		
		// condition expressions contains array access
		boolean isArrayAccess = condition.contains(varName + "[");
		// is primitive type, illegal field/method/array access
		if (isPrimitiveType(type) && (condition.contains(varName + ".") || isArrayAccess)) {
			return false;
		}
		// array access non-array
		if (isArrayAccess && !type.contains("[")) {
			return false;
		}
		return true;
	}
	
	private static boolean isField(String varName, String type){
		Pair<Set<String>, String> pair = _typeInfo.get(type);
		while(pair != null){
			if(pair.getFirst().contains(varName)){
				return true;
			} else {
				String parent = pair.getSecond();
				if(parent != null){
					pair = _typeInfo.get(parent);
				} else {
					pair = null;
				}
			}
		}
		return false;
	}

	private static boolean isPrimitiveType(String type) {
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
		private Set<String> _singleVariableNames = new HashSet<>();
		private Set<String> _methodInvacations = new HashSet<>();

		public ExprAnalysisVisitor(String varName, String type) {
			_varName = varName;
			_type = type;
		}

		public boolean isLegal() {
			return _legal;
		}
		
		public boolean hasSideEffect(){
			return _hasSideEffect;
		}
		
		public Set<String> getMethods(){
			return _methodInvacations;
		}
		
		public Set<String> getSingleVariables(){
			return _singleVariableNames;
		}
		
		public boolean visit(ArrayAccess node){
			String name = node.getArray().toString();
			String index = node.getIndex().toString();
			if((name.equals(_varName) && !_type.contains("[")) || index.equals(_varName)){
				_legal = false;
				return false;
			}
			return true;
		}
		
		public boolean visit(QualifiedName node){
			String qualifier = node.getQualifier().toString();
			String name = node.getName().toString();
			if(_varName.equals(qualifier)){
				_legal = isField(name, _type);
				if(!_legal){
					return false;
				}
			}
			return true;
		}
		
		public boolean visit(SimpleName node){
			String name = node.getFullyQualifiedName();
			ASTNode parent = node.getParent();
			if(parent != null){
				String parentStr = parent.toString().replace("this.", "");
				if(!parentStr.contains("." + name)){
					_singleVariableNames.add(name);
				}
			}
			
			return true;
		}
		
		public boolean visit(MethodInvocation node){
			_methodInvacations.add(node.getName().getFullyQualifiedName());
			if(node.getExpression() != null){
				String var = node.getExpression().toString();
				String name = node.getName().toString();
				if(_varName.equals(var)){
					_legal = isField(name, _type);
					if(!_legal){
						return false;
					}
				}
			}
			return true;
		}
		
		public boolean visit(FieldAccess node){
			String qualifier = node.getExpression().toString();
			String name = node.getName().toString();
			if(_varName.equals(qualifier)){
				_legal = isField(name, _type);
				if(!_legal){
					return false;
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
		
		public boolean visit(PostfixExpression node){
			_hasSideEffect = true;
			return false;
		}
		
		public boolean visit(Assignment node){
			_hasSideEffect = true;
			return false;
		}
		
		public boolean visit(InfixExpression node){
			String left = node.getLeftOperand().toString();
			if(left.equals("this")){
				if(node.getRightOperand() instanceof NumberLiteral){
					_legal = false;
					return false;
				}
			}
			String right = node.getRightOperand().toString();
			if(right.equals("this")){
				if(node.getLeftOperand() instanceof NumberLiteral || left.equals("this")){
					_legal = false;
					return false;
				}
			}
			return true;
		}

	}
}
