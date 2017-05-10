/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;

import locator.common.config.Identifier;
import locator.common.java.Method;
import locator.common.util.LevelLogger;

/**
 * delete all print statement instrumented into the source code
 * 
 * @author Jiajun
 *
 */
public class DeInstrumentVisitor extends TraversalVisitor {

	private final String __name__ = "@DeInstrumentVisitor "; 
	private Method _method = null;
	
	public DeInstrumentVisitor(){
		
	}
	
	public DeInstrumentVisitor(Method method){
		_method = method;
	}
	
	public boolean visit(CompilationUnit unit){
		if (_method != null) {
			// filter unrelative files
			String methodInfo = Identifier.getMessage(_method.getMethodID());
			if (!methodInfo.contains(unit.getPackage().getName().getFullyQualifiedName())) {
				LevelLogger.debug(__name__ + "@visit Not the right java file.");
				return false;
			}
		}
		return true;
	}
	
	public boolean visit(MethodDeclaration node){
		if (_method != null && !_method.match(node)) {
			return true;
		}
		RemoveStatementVisitor removeStatementVisitor = new RemoveStatementVisitor();
		node.accept(removeStatementVisitor);
		return true;
	}



	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFlag(String methodFlag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMethod(Method method) {
		_method = method;
	}
	
	public static void main(String[] args) {
		
	}
}

class RemoveStatementVisitor extends ASTVisitor{
	public boolean visit(Block node) {

		List<ASTNode> statements = new ArrayList<>();

		for (Object statement : node.statements()) {
			statements.add((ASTNode) ASTNode.copySubtree(AST.newAST(AST.JLS8), (ASTNode) statement));
		}

		node.statements().clear();
		for (ASTNode astNode : statements) {
			if (astNode instanceof ExpressionStatement) {
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
				AST ast = AST.newAST(AST.JLS8);
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

			} else if(astNode instanceof IfStatement){
				IfStatement ifStatement = (IfStatement) astNode;
				if(ifStatement.getThenStatement() instanceof Block && ifStatement.getElseStatement() instanceof Block){
					Block thenBlock = (Block) ifStatement.getThenStatement();
					Block elseBlock = (Block) ifStatement.getElseStatement();
					if(thenBlock.statements().size()==1 && elseBlock.statements().size() == 1){
						Statement thenStatement = (Statement) thenBlock.statements().get(0);
						Statement elseStatement = (Statement) elseBlock.statements().get(0);
						if(thenStatement instanceof ExpressionStatement && elseStatement instanceof ExpressionStatement){
							ExpressionStatement thenExpression = (ExpressionStatement) thenStatement;
							ExpressionStatement elseExpression = (ExpressionStatement) elseStatement;
							if(thenExpression.getExpression() instanceof MethodInvocation && elseExpression.getExpression() instanceof MethodInvocation){
								if(IsInstrumentation((MethodInvocation) thenExpression.getExpression()) && IsInstrumentation((MethodInvocation) elseExpression.getExpression())){
									continue;
								}
							}
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
		if (expression != null && expression.toString().equals("auxiliary.Dumper") && node.getName().getFullyQualifiedName().equals("write")) {
			return true;
		}
		return false;
	}
	
//	private boolean IsInstrumentation(MethodInvocation node) {
//		if (node.getName().getFullyQualifiedName().equals("println") && node.arguments() != null) {
//			List<Object> args = node.arguments();
//			if (args != null && args.size() > 0 && args.get(0).toString().contains(Constant.INSTRUMENT_FLAG)) {
//				return true;
//			}
////			if (args != null && args.size() > 0 && args.get(0) instanceof StringLiteral) {
////				StringLiteral stringLiteral = (StringLiteral) args.get(0);
////				if (stringLiteral.getLiteralValue().startsWith(Constant.INSTRUMENT_FLAG)) {
////					return true;
////				}
////			}
//		}
//		return false;
//	}
	
}
