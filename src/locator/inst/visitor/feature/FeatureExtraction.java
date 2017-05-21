/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor.feature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import edu.pku.sei.conditon.simple.FeatureGenerator;
import locator.common.config.Constant;
import locator.common.java.JavaFile;
import locator.common.java.Pair;
import soot.coffi.constant_element_value;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class FeatureExtraction {

	@Deprecated
	public static Pair<FeatureEntry, Map<String, FeatureEntry>> extractFeature(ASTNode statement, int line) {
		Pair<FeatureEntry, Map<String, FeatureEntry>> features = new Pair<>();

		return features;
	}

	public static Pair<List<String>, List<String>> extractAllFeatures(String srcPath, String relJavaPath, int line) {
		List<String> varFeature = FeatureGenerator.generateVarFeature(srcPath, relJavaPath, line);
		List<String> expFeature = FeatureGenerator.generateExprFeature(srcPath, relJavaPath, line);

		CompilationUnit cu = (CompilationUnit) JavaFile.genASTFromSource(
				JavaFile.readFileToString(srcPath + Constant.PATH_SEPARATOR + relJavaPath),
				ASTParser.K_COMPILATION_UNIT);
		VariableCollector variableCollector = new VariableCollector(line);
		cu.accept(variableCollector);
		Set<String> rightVars = variableCollector.getRightVariables();

		List<String> filteredVarFeatures = new ArrayList<>();
		for (String feature : varFeature) {
			String[] elements = feature.split("\t");
			if (rightVars.contains(elements[5])) {
				// TODO : should be removed after re-train the prediction model
				feature = feature.replace("CALLER_USE", "CLALLER_USE");
				filteredVarFeatures.add(feature);
			}
		}

		List<String> filteredExpFeatures = new ArrayList<>();
		for (String feature : expFeature) {
			String[] elements = feature.split("\t");
			if (rightVars.contains(elements[5])) {
				filteredExpFeatures.add(feature);
			}
		}
		return new Pair<List<String>, List<String>>(filteredVarFeatures, filteredExpFeatures);

	}

	private static class VariableCollector extends ASTVisitor {

		private int _line = -1;
		private CompilationUnit _cu = null;
		private String _leftVar = null;
		private Set<String> _rightVars = null;

		public VariableCollector(int line) {
			_line = line;
			_leftVar = null;
			_rightVars = new HashSet<>();
		}

		public boolean visit(CompilationUnit unit) {
			_cu = unit;
			return true;
		}

		public String getLeftVariable() {
			return _leftVar;
		}

		public Set<String> getRightVariables() {
			return _rightVars;
		}

		public boolean visit(Block block) {
			int start = _cu.getLineNumber(block.getStartPosition());
			int end = _cu.getLineNumber(block.getStartPosition() + block.getLength());
			if (_line < start || end < _line) {
				return true;
			}
			for (Object object : block.statements()) {
				Statement stmt = (Statement) object;
				if (!process(stmt)) {
					return false;
				}
			}
			return true;
		}

		public boolean process(ASTNode statement) {
			int start = _cu.getLineNumber(statement.getStartPosition());
			int end = _cu.getLineNumber(statement.getStartPosition() + statement.getLength());
			if (_line < start || end < _line) {
				return true;
			} else if (statement instanceof BreakStatement || statement instanceof ContinueStatement
					|| statement instanceof EmptyStatement || statement instanceof LabeledStatement
					|| statement instanceof ThrowStatement || statement instanceof SwitchCase
					|| statement instanceof TryStatement || statement instanceof TypeDeclarationStatement) {
				return false;
			} else if (statement instanceof ConstructorInvocation) {
				ConstructorInvocation constructorInvocation = (ConstructorInvocation) statement;
				for (Object object : constructorInvocation.arguments()) {
					Pair<String, Set<String>> vars = parseName((ASTNode) object);
					_rightVars.addAll(vars.getSecond());
				}
				return false;
			} else if (statement instanceof DoStatement) {
				DoStatement doStatement = (DoStatement) statement;
				int line = _cu.getLineNumber(doStatement.getExpression().getStartPosition());
				if (line == _line) {
					Pair<String, Set<String>> vars = parseName(doStatement.getExpression());
					_leftVar = vars.getFirst();
					_rightVars.addAll(vars.getSecond());
					return false;
				}
			} else if (statement instanceof EnhancedForStatement) {
				EnhancedForStatement enhancedForStatement = (EnhancedForStatement) statement;
				int line = _cu.getLineNumber(enhancedForStatement.getExpression().getStartPosition());
				if (line == _line) {
					Pair<String, Set<String>> vars = parseName(enhancedForStatement.getExpression());
					_leftVar = vars.getFirst();
					_rightVars.addAll(vars.getSecond());
					return false;
				}
			} else if (statement instanceof ExpressionStatement) {
				Pair<String, Set<String>> vars = parseName(statement);
				_leftVar = vars.getFirst();
				_rightVars.addAll(vars.getSecond());
				return false;
			} else if (statement instanceof ForStatement) {
				ForStatement forStatement = (ForStatement) statement;
				int line = _cu.getLineNumber(forStatement.getExpression().getStartPosition());
				if (line == _line) {
					Pair<String, Set<String>> vars = parseName(forStatement.getExpression());
					_leftVar = vars.getFirst();
					_rightVars.addAll(vars.getSecond());
					return false;
				}
			} else if (statement instanceof IfStatement) {
				IfStatement ifStatement = (IfStatement) statement;
				int line = _cu.getLineNumber(ifStatement.getExpression().getStartPosition());
				if (line == _line) {
					Pair<String, Set<String>> vars = parseName(ifStatement.getExpression());
					_leftVar = vars.getFirst();
					_rightVars.addAll(vars.getSecond());
					return false;
				}
			} else if (statement instanceof ReturnStatement) {
				ReturnStatement returnStatement = (ReturnStatement) statement;
				Pair<String, Set<String>> vars = parseName(returnStatement.getExpression());
				_leftVar = vars.getFirst();
				_rightVars.addAll(vars.getSecond());
				return false;
			} else if (statement instanceof SuperConstructorInvocation) {
				SuperConstructorInvocation sCI = (SuperConstructorInvocation) statement;
				for (Object object : sCI.arguments()) {
					Pair<String, Set<String>> vars = parseName((ASTNode) object);
					_rightVars.addAll(vars.getSecond());
				}
				return false;
			} else if (statement instanceof SwitchStatement) {
				SwitchStatement switchStatement = (SwitchStatement) statement;
				int line = _cu.getLineNumber(switchStatement.getExpression().getStartPosition());
				if (line == _line) {
					Pair<String, Set<String>> vars = parseName(switchStatement.getExpression());
					_rightVars.addAll(vars.getSecond());
					return false;
				}
				for (Object object : switchStatement.statements()) {
					if (!process((ASTNode) object)) {
						return false;
					}
				}
			} else if (statement instanceof SynchronizedStatement) {
				SynchronizedStatement scs = (SynchronizedStatement) statement;
				int line = _cu.getLineNumber(scs.getExpression().getStartPosition());
				if (line == _line) {
					Pair<String, Set<String>> vars = parseName(scs.getExpression());
					_rightVars.addAll(vars.getSecond());
					return false;
				}
			} else if (statement instanceof VariableDeclarationStatement) {
				VariableDeclarationStatement vds = (VariableDeclarationStatement) statement;
				for (Object object : vds.fragments()) {
					Pair<String, Set<String>> vars = parseName(vds);
					_rightVars.addAll(vars.getSecond());
				}
				return false;
			} else if (statement instanceof WhileStatement) {
				WhileStatement whileStatement = (WhileStatement) statement;
				int line = _cu.getLineNumber(whileStatement.getExpression().getStartPosition());
				if (line == _line) {
					Pair<String, Set<String>> vars = parseName(whileStatement.getExpression());
					_rightVars.addAll(vars.getSecond());
					return false;
				}
			}
			return true;
		}

		private static Pair<String, Set<String>> parseName(ASTNode node) {
			// System.out.println(node);
			CollectSimpleName collectSimpleName = new CollectSimpleName();
			node.accept(collectSimpleName);
			return collectSimpleName.getVariables();
		}

		private static class CollectSimpleName extends ASTVisitor {
			private String leftVariable;
			private Set<String> rightVariables;
			private Set<String> filteredVars;

			public CollectSimpleName() {
				leftVariable = null;
				rightVariables = new HashSet<>();
				filteredVars = new HashSet<>();
			}

			public Pair<String, Set<String>> getVariables() {
				// System.out.println("left : " + leftVariable);
				// System.out.print("right : ");
				// for (String string : rightVariables) {
				// System.out.print(string + ",");
				// }
				// System.out.println();
				return new Pair<String, Set<String>>(leftVariable, rightVariables);
			}

			public boolean visit(SimpleName node) {
				String name = node.getFullyQualifiedName();
				if (isAllCaptialCharacters(name)) {
					return true;
				}
				if (!filteredVars.contains(name)) {
					rightVariables.add(name);
				}
				return true;
			}

			public boolean visit(VariableDeclarationStatement node) {
				for (Object object : node.fragments()) {
					if (object instanceof VariableDeclarationFragment) {
						VariableDeclarationFragment vdf = (VariableDeclarationFragment) object;
						String name = vdf.getName().getFullyQualifiedName();
						filteredVars.add(name);
						rightVariables.remove(name);
					}
				}
				return true;
			}

			public boolean visit(Assignment node) {
				Expression expression = node.getLeftHandSide();
				if (expression instanceof Name) {
					String name = ((Name) expression).getFullyQualifiedName();
					int index = name.indexOf(".");
					if (index > 0) {
						name = name.substring(0, index);
					}
					if (!filteredVars.contains(name)) {
						leftVariable = name;
					}
				}
				return true;
			}

			private boolean isAllCaptialCharacters(String name) {
				name = name.replace("_", "");
				return name.toUpperCase().equals(name);
			}
		}
	}

}
