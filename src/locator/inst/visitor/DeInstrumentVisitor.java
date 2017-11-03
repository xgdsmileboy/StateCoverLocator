/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SwitchStatement;

import locator.common.config.Constant;
import locator.common.config.Identifier;

/**
 * delete all print statement instrumented into the source code
 * 
 * @author Jiajun
 *
 */
public class DeInstrumentVisitor extends TraversalVisitor {

	private final String __name__ = "@DeInstrumentVisitor ";

	public DeInstrumentVisitor() {

	}

	public DeInstrumentVisitor(Set<Integer> methods) {
		_methods = methods;
	}

	public boolean visit(MethodDeclaration node) {
		if (_methods != null) {
			boolean flag = false;
			for (Integer method : _methods) {
				String mString = buildMethodInfoString(node);
				int id = Identifier.getIdentifier(mString);
				if (method == id) {
					flag = true;
				}
			}
			if (!flag) {
				return true;
			}
		}
		RemoveStatementVisitor removeStatementVisitor = new RemoveStatementVisitor();
		node.accept(removeStatementVisitor);
		return true;
	}

}

class RemoveStatementVisitor extends ASTVisitor {
	public boolean visit(Block node) {

		List<ASTNode> statements = new ArrayList<>();

		for (Object statement : node.statements()) {
			statements.add((ASTNode) ASTNode.copySubtree(AST.newAST(Constant.AST_LEVEL), (ASTNode) statement));
		}

		node.statements().clear();
		for (ASTNode astNode : statements) {
			if(astNode instanceof Block){
				Block block = (Block) astNode;
				int size = block.statements().size();
				if(size == 2){
					boolean isInstrument = true;
					for(int i = 0; i < size; i++){
						if(block.statements().get(i).toString().startsWith("auxiliary.Dumper")){
							continue;
						}
						isInstrument = false;
					}
					if(isInstrument){
						continue;
					}
				}
			} else if (astNode instanceof ExpressionStatement) {
				ExpressionStatement expressionStatement = (ExpressionStatement) astNode;
				if (expressionStatement.getExpression() instanceof MethodInvocation) {
					MethodInvocation methodInvocation = (MethodInvocation) expressionStatement.getExpression();
					if (IsInstrumentation(methodInvocation)) {
						continue;
					}
				}
			} else if (astNode instanceof SwitchStatement) {
				SwitchStatement switchStatement = (SwitchStatement) astNode;
				List<ASTNode> swStatements = new ArrayList<>();
				AST ast = AST.newAST(Constant.AST_LEVEL);
				for (Object object : switchStatement.statements()) {
					swStatements.add(ASTNode.copySubtree(ast, (ASTNode) object));
				}
				switchStatement.statements().clear();
				for (ASTNode swNode : swStatements) {
					if (swNode instanceof ExpressionStatement) {
						ExpressionStatement expressionStatement = (ExpressionStatement) swNode;
						if (expressionStatement.getExpression() instanceof MethodInvocation) {
							MethodInvocation methodInvocation = (MethodInvocation) expressionStatement.getExpression();
							if (IsInstrumentation(methodInvocation)) {
								continue;
							}
						}
					}
					switchStatement.statements().add(ASTNode.copySubtree(switchStatement.getAST(), swNode));
				}

			} else if (astNode instanceof IfStatement) {
				IfStatement ifStatement = (IfStatement) astNode;
				if (ifStatement.getThenStatement() instanceof Block && ifStatement.getElseStatement() == null) {
					Block block = (Block) ifStatement.getThenStatement();
					if(block.statements().size() == 1 && block.statements().get(0) instanceof ExpressionStatement){
						ExpressionStatement es = (ExpressionStatement) block.statements().get(0);
						if(es.getExpression() instanceof MethodInvocation && IsInstrumentation((MethodInvocation)es.getExpression())){
							continue;
						}
					}
				}
			}
			node.statements().add(ASTNode.copySubtree(node.getAST(), astNode));
		}

		return true;
	}

	private boolean IsInstrumentation(MethodInvocation node) {
		Expression expression = node.getExpression();
		if (expression != null && expression.toString().equals("auxiliary.Dumper")) {
			return true;
		}
		return false;
	}

	// private boolean IsInstrumentation(MethodInvocation node) {
	// if (node.getName().getFullyQualifiedName().equals("println") &&
	// node.arguments() != null) {
	// List<Object> args = node.arguments();
	// if (args != null && args.size() > 0 &&
	// args.get(0).toString().contains(Constant.INSTRUMENT_FLAG)) {
	// return true;
	// }
	//// if (args != null && args.size() > 0 && args.get(0) instanceof
	// StringLiteral) {
	//// StringLiteral stringLiteral = (StringLiteral) args.get(0);
	//// if
	// (stringLiteral.getLiteralValue().startsWith(Constant.INSTRUMENT_FLAG)) {
	//// return true;
	//// }
	//// }
	// }
	// return false;
	// }

}
