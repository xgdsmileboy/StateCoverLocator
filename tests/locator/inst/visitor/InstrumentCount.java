/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;

/**
 * @author Jiajun
 * @date May 13, 2017
 */
public class InstrumentCount {

	private static int counter = 0;
	
	public static int getInstrumentCount(CompilationUnit unit){
		CounterVisitor counterVisitor = new CounterVisitor();
		counter = 0;
		unit.accept(counterVisitor);
		return counter;
	}
	
	private static class CounterVisitor extends ASTVisitor{
		
		public boolean visit(Block node) {
			
			for (Object element : node.statements()) {
				ASTNode astNode = (ASTNode) element;
				if (astNode instanceof ExpressionStatement) {
					ExpressionStatement expressionStatement = (ExpressionStatement) astNode;
					if (expressionStatement.getExpression() instanceof MethodInvocation) {
						MethodInvocation methodInvocation = (MethodInvocation) expressionStatement.getExpression();
						if (IsInstrumentation(methodInvocation)) {
							counter ++;
							continue;
						}
					}
				} else if (astNode instanceof SwitchStatement) {
					SwitchStatement switchStatement = (SwitchStatement) astNode;
					for (Object obj : switchStatement.statements()) {
						ASTNode swNode = (ASTNode) obj;
						if (swNode instanceof ExpressionStatement) {
							ExpressionStatement expressionStatement = (ExpressionStatement) swNode;
							if (expressionStatement.getExpression() instanceof MethodInvocation) {
								MethodInvocation methodInvocation = (MethodInvocation) expressionStatement.getExpression();
								if (IsInstrumentation(methodInvocation)) {
									counter ++;
									continue;
								}
							}
						}
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
										counter ++;
										continue;
									}
								}
							}
						}
					}
				}
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
	}
}



