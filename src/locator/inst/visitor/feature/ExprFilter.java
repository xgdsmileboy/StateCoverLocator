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
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

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

	public static boolean isLegalExpr(String type, String varName, String condition) {
		// user-defined class
		if (_typeInfo.containsKey(type)) {
			ASTNode node = JavaFile.genASTFromSource(condition, ASTParser.K_EXPRESSION);
			ExprAnalysisVisitor visitor = new ExprAnalysisVisitor(varName, type);
			node.accept(visitor);
			return visitor.isLegal();
		}
		//condition expressions contains array access
		boolean isArrayAccess = condition.contains(varName + "[");
		//is primitive type, illegal field/method/array access
		if (isPrimitiveType(type) && (condition.contains(varName + ".") || isArrayAccess)) {
			return false;
		}
		//array access non-array
		if(isArrayAccess && !type.contains("[")){
			return false;
		}
		return true;
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
			return true;
		default:
			return false;
		}
	}

	static class MethodAndFieldCollectorVisitor extends ASTVisitor {

		private final String __name__ = "@ExprFilter$MethodAndFieldCollectorVisitor ";

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * TypeDeclaration)
		 */
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
		private String _varName = null;
		private String _type = null;

		public ExprAnalysisVisitor(String varName, String type) {
			_varName = varName;
			_type = type;
		}

		public boolean isLegal() {
			return _legal;
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
				_legal = false;
				Pair<Set<String>, String> pair = _typeInfo.get(_type);
				while(pair != null){
					if(pair.getFirst().contains(name)){
						_legal = true;
						break;
					} else {
						String parent = pair.getSecond();
						if(parent != null){
							pair = _typeInfo.get(parent);
						} else {
							pair = null;
						}
					}
				}
				if(!_legal){
					return false;
				}
			}
			return true;
		}
		
		
		public boolean visit(MethodInvocation node){
			if(node.getExpression() != null){
				String var = node.getExpression().toString();
				String name = node.getName().toString();
				if(_varName.equals(var)){
					_legal = false;
					Pair<Set<String>, String> pair = _typeInfo.get(_type);
					while(pair != null){
						if(pair.getFirst().contains(name)){
							_legal = true;
							break;
						} else {
							String parent = pair.getSecond();
							if(parent != null){
								pair = _typeInfo.get(parent);
							} else {
								pair = null;
							}
						}
					}
					if(!_legal){
						return false;
					}
				}
			}
			return true;
		}

	}

}
