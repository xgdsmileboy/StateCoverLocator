/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.Method;
import locator.common.util.LevelLogger;

/**
 * Traverse a {@code CompilationUnit}
 * 
 * @author Jiajun
 *
 */
public abstract class TraversalVisitor extends ASTVisitor {

	private final String __name__ = "@TraversalVisitor ";

	protected CompilationUnit _cu;
	protected Set<Method> _methods;
	protected String _clazzName;
	protected String _methodFlag = Constant.INSTRUMENT_K_SOURCE;

	public void setFlag(String methodFlag) {
		_methodFlag = methodFlag;
	}

	public void setMethod(Set<Method> methods) {
		_methods = methods;
	}

	public final boolean traverse(CompilationUnit compilationUnit) {
		compilationUnit.accept(this);
		return true;
	}

	public void reset() {
		_methodFlag = Constant.INSTRUMENT_K_SOURCE;
		_methods = null;
		_clazzName = "";
		_cu = null;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		_cu = node;
		if (node.getPackage().getName() != null
				&& node.getPackage().getName().getFullyQualifiedName().equals("auxiliary")) {
			return false;
		}
		// filter unrelative files
		if (_methods != null) {
			boolean continueVisit = false;
			for (Method method : _methods) {
				String methodInfo = Identifier.getMessage(method.getMethodID());
				if (methodInfo.contains(_cu.getPackage().getName().getFullyQualifiedName())) {
					continueVisit = true;
					break;
				}
			}
			if (!continueVisit) {
				return false;
			}
		}
		_clazzName = null;
		String packageName = node.getPackage().getName().getFullyQualifiedName();
		List<Object> types = node.types();
		if (types.size() == 1) {
			if (types.get(0) instanceof TypeDeclaration) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) types.get(0);
				_clazzName = packageName + Constant.INSTRUMENT_DOT_SEPARATOR
						+ typeDeclaration.getName().getFullyQualifiedName();
			}
		} else {
			for (Object object : node.types()) {
				if (object instanceof TypeDeclaration) {
					TypeDeclaration type = (TypeDeclaration) object;
					if (Modifier.isPublic(type.getModifiers())) {
						_clazzName = packageName + Constant.INSTRUMENT_DOT_SEPARATOR
								+ type.getName().getFullyQualifiedName();
					}
				}
			}
		}
		if (_clazzName == null) {
			// LevelLogger.warn(__name__ + "#visit(CompilationUnit) no public
			// type declaration exists.");
			return false;
		}
		return true;
	}

	/**
	 * judging the current {@code MethodDeclaration} whether should be skipped
	 * or not. return {@code true} when : 1. if the current method is not
	 * contained by the given methods (if have). 2. it is not a concrete test
	 * method in test classes, e.g., {@code setUp}. otherwise, return false;
	 * 
	 * @param node
	 *            : {@code MethodDeclaration}
	 * @param methodID
	 *            : method id of the {@code MethodDeclaration}
	 * @return
	 */
	protected boolean shouldSkip(MethodDeclaration node, int methodID) {

		if (_methods != null) {
			boolean skip = true;
			for (Method method : _methods) {
				if (method.getMethodID() == methodID) {
					skip = false;
					break;
				}
			}
			if (skip) {
				return true;
			}
		}

		String name = node.getName().getFullyQualifiedName();
		// if(Modifier.isStatic(node.getModifiers())){
		// return true;
		// }

		if (_methodFlag.equals(Constant.INSTRUMENT_K_TEST) && (name.equals("setUp") || name.equals("countTestCases")
				|| name.equals("createResult") || name.equals("run") || name.equals("runBare") || name.equals("runTest")
				|| name.equals("tearDown") || name.equals("toString") || name.equals("getName")
				|| name.equals("setName"))) {
			return true;
		}

		// filter those functional methods in test class path, test method name
		// starting with "test" in Junit 3 while with annotation as "@Test" in
		// Junit 4,
		// TODO should be optimized since the "contain" method is time consuming
		if (_methodFlag.equals(Constant.INSTRUMENT_K_TEST) && !name.startsWith("test")
				&& !node.toString().contains("@Test")) {
			return true;
		}

		return false;
	}

	/**
	 * build full class information based on the given method declaration
	 * 
	 * @param node
	 *            : {@code MethodDeclaration}
	 * @return full class name for the method declaration, e.g.,
	 *         "package.Clazz$InnerClazz"
	 */
	private String getFullClazzName(MethodDeclaration node) {
		// filter those methods that defined in anonymous classes
		ASTNode parent = node.getParent();
		while (parent != null && !(parent instanceof TypeDeclaration)) {
			if (parent instanceof ClassInstanceCreation) {
				return null;
			}
			parent = parent.getParent();
		}
		String currentClassName = _clazzName;
		if (parent != null && parent instanceof TypeDeclaration) {
			TypeDeclaration typeDeclaration = (TypeDeclaration) parent;
			String parentName = typeDeclaration.getName().getFullyQualifiedName();
			if (!_clazzName.endsWith(parentName)) {
				currentClassName = _clazzName + "$" + parentName;
			}
		}
		return currentClassName;
	}

	/**
	 * build string representation for the given method declaration
	 * 
	 * @param node
	 *            : {@code MethodDeclaration}
	 * @return a string represent the method
	 */
	protected String buildMethodInfoString(MethodDeclaration node) {
		String currentClassName = getFullClazzName(node);
		if (currentClassName == null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer(currentClassName + "#");

		String retType = "?";
		if (node.getReturnType2() != null) {
			retType = node.getReturnType2().toString();
		}
		StringBuffer param = new StringBuffer("?");
		for (Object object : node.parameters()) {
			if (!(object instanceof SingleVariableDeclaration)) {
				LevelLogger
						.error(__name__ + "#visit Parameter is not a SingleVariableDeclaration : " + object.toString());
				param.append(",?");
			} else {
				SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration) object;
				param.append("," + singleVariableDeclaration.getType().toString());
			}
		}
		// add method return type
		buffer.append(retType + "#");
		// add method name
		buffer.append(node.getName().getFullyQualifiedName() + "#");
		// add method params, NOTE: the first parameter starts at index 1.
		buffer.append(param);
		return buffer.toString();
	}
}
