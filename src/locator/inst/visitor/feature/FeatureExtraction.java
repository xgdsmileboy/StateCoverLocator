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
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
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
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import edu.pku.sei.conditon.simple.FeatureGenerator;
import locator.common.config.Constant;
import locator.common.java.JavaFile;
import locator.common.java.Pair;
import soot.coffi.constant_element_value;
import soot.coffi.field_info;

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

	public static Pair<List<String>, List<String>> extractAllFeatures(String srcPath, String relJavaPath, int line, Map<String, String> allLegalLocalVariables) {
		List<String> varFeature = FeatureGenerator.generateVarFeature(srcPath, relJavaPath, line);
		List<String> expFeature = FeatureGenerator.generateExprFeature(srcPath, relJavaPath, line);

		CompilationUnit cu = (CompilationUnit) JavaFile.genASTFromSource(
				JavaFile.readFileToString(srcPath + Constant.PATH_SEPARATOR + relJavaPath),
				ASTParser.K_COMPILATION_UNIT);
		VariableCollector variableCollector = new VariableCollector(line);
		cu.accept(variableCollector);
		Set<String> rightVars = variableCollector.getObservedVariables();

		List<String> filteredVarFeatures = new ArrayList<>();
		for (String feature : varFeature) {
			String[] elements = feature.split("\t");
			String varName = elements[Constant.FEATURE_VAR_NAME_INDEX];
			String varType = elements[Constant.FEATURE_VAR_TYPE_INDEX];
			allLegalLocalVariables.put(varName, varType);
			System.out.println(feature);
			if (rightVars.contains(varName)) {
				filteredVarFeatures.add(feature);
			}
		}

		List<String> filteredExpFeatures = new ArrayList<>();
		for (String feature : expFeature) {
			String[] elements = feature.split("\t");
			if (rightVars.contains(elements[Constant.FEATURE_VAR_NAME_INDEX])) {
				filteredExpFeatures.add(feature);
			}
		}
		return new Pair<List<String>, List<String>>(filteredVarFeatures, filteredExpFeatures);

	}

	private static class VariableCollector extends ASTVisitor {

		private int _line = -1;
		private int _blockID = -1;
		private CompilationUnit _cu = null;
		private String _leftVar = null;
		private Set<String> _rightVars = null;
		// <varName, {<blockID, lineNum>}> 
		private Map<String, Set<Pair<Integer, Integer>>> _varAssign = null;
		// <varName, {<blockID, lineNum>}> same variable can be defined in different lines of different blocks
		private Map<String, Set<Pair<Integer, Integer>>> _varDef = null;
		// <blockID, parentBlockID>
		private Map<Integer, Integer> _blockParent = null;
		// <varName, {<blockID, lineNum>}> same variable can be first used in different lines of different blocks
		private Map<String, Set<Pair<Integer, Integer>>> _varUse = null;
		private Stack<Integer> _blockStack = new Stack<>();

		public VariableCollector(int line) {
			_line = line;
			_leftVar = null;
			_rightVars = new HashSet<>();
			_varAssign = new HashMap<>();
			_varDef = new HashMap<>();
			_blockParent = new HashMap<>();
			_varUse = new HashMap<>();
		}

		public boolean visit(CompilationUnit unit) {
			_cu = unit;
			return true;
		}

		public Set<String> getObservedVariables(){
//			for(Entry<String, Set<Pair<Integer, Integer>>> entry : _varDef.entrySet()){
//				System.out.println("name : " + entry.getKey());
//				for(Pair<Integer, Integer> entry_inner : entry.getValue()){
//					System.out.println("def : " + entry_inner.getFirst() + " " + entry_inner.getSecond());
//				}
//			}
//			
//			for(Entry<String, Set<Pair<Integer, Integer>>> entry : _varAssign.entrySet()){
//				System.out.println("name : " + entry.getKey());
//				for(Pair<Integer, Integer> entry_inner : entry.getValue()){
//					System.out.println("assign : " + entry_inner.getFirst() + " " + entry_inner.getSecond());
//				}
//			}
//			
//			for(Entry<String, Set<Pair<Integer, Integer>>> entry : _varUse.entrySet()){
//				System.out.println("name : " + entry.getKey());
//				for(Pair<Integer, Integer> entry_inner : entry.getValue()){
//					System.out.println("use : " + entry_inner.getFirst() + " " + entry_inner.getSecond());
//				}
//			}
			
			Set<String> retVars = new HashSet<>();
			for(String varName : _rightVars){
				// find define place
				Integer defBlock = 0;
				Integer defLine = 0;
				Set<Pair<Integer, Integer>> defs = _varDef.get(varName);
				if(defs != null){
					for(Pair<Integer, Integer> place : defs){
						// scope of variable define place should contain variable use place
						// to filter situations as follows:
						// (1) if(){ int a; }  (2) for(){ int a; use(a);}, 
						// define place should be second "a" but not first one;
						if(isChildBlock(_blockID, place.getFirst())){
							defBlock = place.getFirst();
							defLine = place.getSecond();
							break;
						}
					}
				}
				if(defLine == _line){
					continue;
				}
				// find last use place between define place and observe place in the same scope
				Integer useLine = 0;
				Set<Pair<Integer, Integer>> uses = _varUse.get(varName);
				if(uses != null){
					for(Pair<Integer, Integer> use : uses){
						// only consider usages in parent scope,
						// int a = 3; if(~) { c = a;}  use(a); 
						// even though "a" is used in if block, we skip it since we cannot make sure it can be executed
						if(isChildBlock(use.getFirst(), defBlock) && isChildBlock(_blockID, use.getFirst())){
							if(use.getSecond() > useLine && use.getSecond() < _line){
								useLine = use.getSecond();
							}
						}
					}
				}
				// no use before current location
				if(useLine == 0){
					retVars.add(varName);
					continue;
				}
				// find if assignment exists between the last use and current location
				Set<Pair<Integer, Integer>> assign = _varAssign.get(varName);
				if(assign != null){
					for(Pair<Integer, Integer> as : assign){
						// all assignment between define and observe places are considered, 
						// even it is in a inner block, since we do not know if it will be executed
						Integer assignLine = as.getSecond();
						if(useLine < assignLine && assignLine < _line){
							retVars.add(varName);
							break;
						}
					}
				} else {
					if(useLine == 0){
						retVars.add(varName);
					}
				}
			}
			
			return retVars;
		}
		
		public boolean visit(MethodDeclaration node){
			int start = _cu.getLineNumber(node.getStartPosition());
			int end = _cu.getLineNumber(node.getStartPosition() + node.getLength());
			if(_line < start || end <= _line){
				return true;
			}
			process(node.getBody());
			return false;
		}
		
		public boolean process(ASTNode statement) {
			if(statement == null){
				return true;
			}
			int start = _cu.getLineNumber(statement.getStartPosition());
			Integer currentBlockID = 0;
			if(!_blockStack.isEmpty()){
				currentBlockID = _blockStack.peek();
			}
			if(statement instanceof Block){
				Block block = (Block) statement;
				_blockParent.put(start, currentBlockID);
				_blockStack.push(start);
				for (Object object : block.statements()) {
					Statement stmt = (Statement) object;
					process(stmt);
				}
				_blockStack.pop();
			} else if (statement instanceof BreakStatement || statement instanceof ContinueStatement
					|| statement instanceof EmptyStatement || statement instanceof LabeledStatement
					|| statement instanceof ThrowStatement || statement instanceof SwitchCase
					|| statement instanceof TryStatement || statement instanceof TypeDeclarationStatement) {
			} else if (statement instanceof ConstructorInvocation) {
				ConstructorInvocation constructorInvocation = (ConstructorInvocation) statement;
				for (Object object : constructorInvocation.arguments()) {
					parseName((ASTNode) object, currentBlockID, start);
				}
			} else if (statement instanceof DoStatement) {
				DoStatement doStatement = (DoStatement) statement;
				int line = _cu.getLineNumber(doStatement.getExpression().getStartPosition());
				parseName(doStatement.getExpression(), currentBlockID, line);
				process(doStatement.getBody());
			} else if (statement instanceof EnhancedForStatement) {
				EnhancedForStatement enhancedForStatement = (EnhancedForStatement) statement;
				int line = _cu.getLineNumber(enhancedForStatement.getExpression().getStartPosition());
				parseName(enhancedForStatement.getExpression(), currentBlockID, line);
				process(enhancedForStatement.getBody());
			} else if (statement instanceof ExpressionStatement){
				parseName(statement, currentBlockID, start);
			} else if (statement instanceof ForStatement) {
				ForStatement forStatement = (ForStatement) statement;
				if(forStatement.initializers() != null){
					for(Object object : forStatement.initializers()){
						ASTNode node = (ASTNode) object;
						int line = _cu.getLineNumber(node.getStartPosition());
						parseName(node, currentBlockID, line);
					}
				}
				
				if(forStatement.getExpression() != null){
					int line = _cu.getLineNumber(forStatement.getExpression().getStartPosition());
					parseName(forStatement.getExpression(), currentBlockID, line);
				}
				
				if(forStatement.updaters() != null){
					for(Object object : forStatement.updaters()){
						ASTNode node = (ASTNode) object;
						int line = _cu.getLineNumber(node.getStartPosition());
						parseName(node, currentBlockID, line);
					}
				}
				
				process(forStatement.getBody());
			} else if (statement instanceof IfStatement) {
				IfStatement ifStatement = (IfStatement) statement;
				int line = _cu.getLineNumber(ifStatement.getExpression().getStartPosition());
				parseName(ifStatement.getExpression(), currentBlockID, line);
				process(ifStatement.getThenStatement());
				process(ifStatement.getElseStatement());
			} else if (statement instanceof ReturnStatement) {
				ReturnStatement returnStatement = (ReturnStatement) statement;
				parseName(returnStatement.getExpression(), currentBlockID, start);
			} else if (statement instanceof SuperConstructorInvocation) {
				SuperConstructorInvocation sCI = (SuperConstructorInvocation) statement;
				for (Object object : sCI.arguments()) {
					parseName((ASTNode) object, currentBlockID, start);
				}
			} else if (statement instanceof SwitchStatement) {
				SwitchStatement switchStatement = (SwitchStatement) statement;
				int line = _cu.getLineNumber(switchStatement.getExpression().getStartPosition());
				parseName(switchStatement.getExpression(), currentBlockID, line);
				for (Object object : switchStatement.statements()) {
					return process((ASTNode) object);
				}
			} else if (statement instanceof SynchronizedStatement) {
				SynchronizedStatement scs = (SynchronizedStatement) statement;
				int line = _cu.getLineNumber(scs.getExpression().getStartPosition());
				parseName(scs.getExpression(), currentBlockID, line);
				process(scs.getBody());
			} else if (statement instanceof VariableDeclarationStatement) {
				VariableDeclarationStatement vds = (VariableDeclarationStatement) statement;
				parseName(vds, currentBlockID, start);
			} else if (statement instanceof WhileStatement) {
				WhileStatement whileStatement = (WhileStatement) statement;
				int line = _cu.getLineNumber(whileStatement.getExpression().getStartPosition());
				parseName(whileStatement.getExpression(), currentBlockID, line);
				process(whileStatement.getBody());
			}
			return true;
		}
	
		private boolean isChildBlock(Integer child, Integer parent){
			if(child.equals(parent) || parent == 0){
				return true;
			}
			Integer realParent = _blockParent.get(child);
			while(realParent != 0){
				if(realParent == parent){
					return true;
				}
				realParent = _blockParent.get(realParent);
			}
			return false;
		}
		
		private void addUse(String varName, int blockID, int line){
			Set<Pair<Integer, Integer>> vars = _varUse.get(varName);
			if(vars != null){
				Pair<Integer, Integer> use = new Pair<Integer, Integer>(blockID, line);
				vars.add(use);
			} else {
				vars = new HashSet<>();
				Pair<Integer, Integer> use = new Pair<Integer, Integer>(blockID, line);
				vars.add(use);
				_varUse.put(varName, vars);
			}
		}
		
		private void addDef(String varName, int blockID, int line){
			Set<Pair<Integer, Integer>> vars = _varDef.get(varName);
			if(vars != null){
				Pair<Integer, Integer> def = new Pair<Integer, Integer>(blockID, line);
				vars.add(def);
			} else {
				vars = new HashSet<>();
				Pair<Integer, Integer> def = new Pair<Integer, Integer>(blockID, line);
				vars.add(def);
				_varDef.put(varName, vars);
			}
		}
		
		private void addAssign(String varName, int blockID, int line){
			Set<Pair<Integer, Integer>> vars = _varAssign.get(varName);
			if(vars != null){
				Pair<Integer, Integer> def = new Pair<Integer, Integer>(blockID, line);
				vars.add(def);
			} else {
				vars = new HashSet<>();
				Pair<Integer, Integer> def = new Pair<Integer, Integer>(blockID, line);
				vars.add(def);
				_varAssign.put(varName, vars);
			}
		}
		
		private void parseName(ASTNode node, int blockID, int line) {
			if(node == null){
				return;
			}
			// System.out.println(node);
			CollectSimpleName collectSimpleName = new CollectSimpleName();
			node.accept(collectSimpleName);
			Pair<Set<String>, Pair<String, Set<String>>> vars = collectSimpleName.getVariables();
			String leftName = vars.getSecond().getFirst(); 
			if(line == _line){
				_blockID = blockID;
				_leftVar = leftName;
				_rightVars.addAll(vars.getSecond().getSecond());
			}
			if(leftName != null){
				addAssign(leftName, blockID, line);
			}
			for(String varName : vars.getFirst()){
				addDef(varName, blockID, line);
			}
			for(String varName : vars.getSecond().getSecond()){
				addUse(varName, blockID, line);
			}
		}

		private class CollectSimpleName extends ASTVisitor {
			private String leftVariable;
			private Set<String> rightVariables;
			private Set<String> defVariables;

			public CollectSimpleName() {
				leftVariable = null;
				rightVariables = new HashSet<>();
				defVariables = new HashSet<>();
			}

			public Pair<Set<String>, Pair<String, Set<String>>> getVariables() {
				// System.out.println("left : " + leftVariable);
				// System.out.print("right : ");
				// for (String string : rightVariables) {
				// System.out.print(string + ",");
				// }
				// System.out.println();
				Pair<String, Set<String>> pair = new Pair<String, Set<String>>(leftVariable, rightVariables);
				return new Pair<Set<String>, Pair<String, Set<String>>>(defVariables, pair);
			}

			public boolean visit(SimpleName node) {
				ASTNode parent = node.getParent();
				if(parent instanceof SimpleType){
					return true;
				}
				if(parent instanceof MethodInvocation){
					MethodInvocation methodInvocation = (MethodInvocation) parent;
					if(methodInvocation.getName().equals(node)){
						return true;
					}
				}
				String name = node.getFullyQualifiedName();
				if (Character.isUpperCase(name.charAt(0)) || isAllCaptialCharacters(name)) {
					return true;
				}
				if(name.equals(leftVariable)){
					while(parent != null && !(parent instanceof MethodDeclaration)){
						if(parent instanceof Assignment){
							CollectSimpleName collectSimpleName = new CollectSimpleName();
							((Assignment)parent).getRightHandSide().accept(collectSimpleName);
							if(collectSimpleName.getVariables().getSecond().getSecond().contains(name)){
								rightVariables.add(name);
							}
						}
						parent = parent.getParent();
					}
					
				} else if (!defVariables.contains(name)) {
					rightVariables.add(name);
				}
				return true;
			}
			
			public boolean visit(VariableDeclarationFragment vdf){
				String name = vdf.getName().getFullyQualifiedName();
				defVariables.add(name);
				rightVariables.remove(name);
				return true;
			}
			
			public boolean visit(SingleVariableDeclaration svd){
				String name = svd.getName().getFullyQualifiedName();
				defVariables.add(name);
				return true;
			}
			
//			public boolean visit(VariableDeclarationExpression node){
//				for (Object object : node.fragments()) {
//					if (object instanceof VariableDeclarationFragment) {
//						VariableDeclarationFragment vdf = (VariableDeclarationFragment) object;
//						String name = vdf.getName().getFullyQualifiedName();
//						defVariables.add(name);
//						rightVariables.remove(name);
//					} else if(object instanceof SingleVariableDeclaration){
//						SingleVariableDeclaration svd = (SingleVariableDeclaration) object;
//						String name = svd.getName().getFullyQualifiedName();
//						defVariables.add(name);
//					}
//				}
//				return true;
//			}
//
//			public boolean visit(VariableDeclarationStatement node) {
//				for (Object object : node.fragments()) {
//					if (object instanceof VariableDeclarationFragment) {
//						VariableDeclarationFragment vdf = (VariableDeclarationFragment) object;
//						String name = vdf.getName().getFullyQualifiedName();
//						defVariables.add(name);
//						rightVariables.remove(name);
//					} else if(object instanceof SingleVariableDeclaration){
//						SingleVariableDeclaration svd = (SingleVariableDeclaration) object;
//						String name = svd.getName().getFullyQualifiedName();
//						defVariables.add(name);
//					}
//				}
//				return true;
//			}

			public boolean visit(Assignment node) {
				Expression expression = node.getLeftHandSide();
				if (expression instanceof Name) {
					String name = ((Name) expression).getFullyQualifiedName();
					int index = name.indexOf(".");
					if (index > 0) {
						name = name.substring(0, index);
					}
					if (!defVariables.contains(name)) {
						leftVariable = name;
					}
				} else if(expression instanceof ArrayAccess){
					ArrayAccess access = (ArrayAccess) expression;
					String name = access.getArray().toString();
					int index = name.lastIndexOf(".");
					if(index > 0){
						name = name.substring(index + 1, name.length());
					}
					if(!defVariables.contains(name)){
						leftVariable = name;
					}
				} else if(expression instanceof FieldAccess){
					FieldAccess fieldAccess = (FieldAccess) expression;
					String name = fieldAccess.getName().getFullyQualifiedName();
					if(!defVariables.contains(name)){
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
