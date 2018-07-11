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
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.util.LevelLogger;
import locator.inst.gen.GenStatement;

/**
 * This class is used for instrument for each {@code Statement}
 * 
 * @author Jiajun
 * @date May 11, 2017
 */
public class StatementInstrumentVisitor extends TraversalVisitor {

	private final static String __name__ = "@StatementInstrumentVisitor ";

	public StatementInstrumentVisitor() {
	}

	public StatementInstrumentVisitor(String methodFlag) {
		_methodFlag = methodFlag;
	}

	public StatementInstrumentVisitor(Set<Integer> methods) {
		_methods = methods;
	}

	@Override
	public boolean visit(MethodDeclaration node) {

		String message = buildMethodInfoString(node);
		if (message == null) {
			return true;
		}
		int keyValue = Identifier.getIdentifier(message);

		if (shouldSkip(node, keyValue)) {
			return true;
		}

		// optimize instrument
//		message = Constant.INSTRUMENT_FLAG + _methodFlag + Constant.INSTRUMENT_STR_SEP + String.valueOf(keyValue);
		message = String.valueOf(keyValue);

		Block methodBody = node.getBody();

		if (methodBody == null) {
			return true;
		}

		List<Statement> blockStatement = new ArrayList<>();

		for (Object object : methodBody.statements()) {
			if (object instanceof Statement) {
				Statement statement = (Statement) object;
				blockStatement.addAll(process(statement, message));
			} else {
				LevelLogger.error(__name__ + "#visit UNKNOWN statement type : " + object.toString());
			}
		}
		methodBody.statements().clear();
		for (Statement statement : blockStatement) {
			methodBody.statements().add(ASTNode.copySubtree(methodBody.getAST(), statement));
		}

		return true;
	}

	private List<Statement> process(Statement statement, String message) {

		List<Statement> result = new ArrayList<>();

		if (statement instanceof IfStatement) {
			IfStatement ifStatement = (IfStatement) statement;

			int lineNumber = _cu.getLineNumber(ifStatement.getExpression().getStartPosition());
			Statement insert = GenStatement.genASTNode(message, lineNumber);
			result.add(insert);

			Statement thenBody = ifStatement.getThenStatement();
			if (thenBody != null) {
				Block thenBlock = null;
				if (thenBody instanceof Block) {
					thenBlock = (Block) thenBody;
				} else {
					AST ast = AST.newAST(Constant.AST_LEVEL);
					thenBlock = ast.newBlock();
					thenBlock.statements().add(ASTNode.copySubtree(thenBlock.getAST(), thenBody));
				}

				Block newThenBlock = processBlock(thenBlock, null, message);
				ifStatement.setThenStatement((Statement) ASTNode.copySubtree(ifStatement.getAST(), newThenBlock));
			}

			Statement elseBody = ifStatement.getElseStatement();
			if (elseBody != null) {
				Block elseBlock = null;
				if (elseBody instanceof Block) {
					elseBlock = (Block) elseBody;
				} else {
					AST ast = AST.newAST(Constant.AST_LEVEL);
					elseBlock = ast.newBlock();
					elseBlock.statements().add(ASTNode.copySubtree(elseBlock.getAST(), elseBody));
				}
				Block newElseBlock = processBlock(elseBlock, null, message);
				ifStatement.setElseStatement((Statement) ASTNode.copySubtree(ifStatement.getAST(), newElseBlock));
			}
			result.add(ifStatement);
		} else if (statement instanceof WhileStatement) {

			WhileStatement whileStatement = (WhileStatement) statement;
			Statement whilebody = whileStatement.getBody();
			if (whilebody != null) {
				Block whileBlock = null;
				if (whilebody instanceof Block) {
					whileBlock = (Block) whilebody;
				} else {
					AST ast = AST.newAST(Constant.AST_LEVEL);
					whileBlock = ast.newBlock();
					whileBlock.statements().add(ASTNode.copySubtree(whileBlock.getAST(), whilebody));
				}

				int lineNumber = _cu.getLineNumber(whileStatement.getExpression().getStartPosition());
				Statement insert = GenStatement.genASTNode(message, lineNumber);
				Block newWhileBlock = processBlock(whileBlock, insert, message);
				whileStatement.setBody((Statement) ASTNode.copySubtree(whileStatement.getAST(), newWhileBlock));
			}

			result.add(whileStatement);
		} else if (statement instanceof ForStatement) {

			ForStatement forStatement = (ForStatement) statement;
			Statement forBody = forStatement.getBody();
			if (forBody != null) {
				Block forBlock = null;
				if (forBody instanceof Block) {
					forBlock = (Block) forBody;
				} else {
					AST ast = AST.newAST(Constant.AST_LEVEL);
					forBlock = ast.newBlock();
					forBlock.statements().add(ASTNode.copySubtree(forBlock.getAST(), forBody));
				}

				int position = forStatement.getStartPosition();
				if (forStatement.getExpression() != null) {
					position = forStatement.getExpression().getStartPosition();
				} else if (forStatement.initializers() != null && forStatement.initializers().size() > 0) {
					position = ((ASTNode) forStatement.initializers().get(0)).getStartPosition();
				} else if (forStatement.updaters() != null && forStatement.updaters().size() > 0) {
					position = ((ASTNode) forStatement.updaters().get(0)).getStartPosition();
				}
				int lineNumber = _cu.getLineNumber(position);
				Statement insert = GenStatement.genASTNode(message, lineNumber);
				Block newForBlock = processBlock(forBlock, insert, message);
				forStatement.setBody((Statement) ASTNode.copySubtree(forStatement.getAST(), newForBlock));
			}

			result.add(forStatement);
		} else if (statement instanceof DoStatement) {

			DoStatement doStatement = (DoStatement) statement;
			Statement doBody = doStatement.getBody();
			if (doBody != null) {
				Block doBlock = null;
				if (doBody instanceof Block) {
					doBlock = (Block) doBody;
				} else {
					AST ast = AST.newAST(Constant.AST_LEVEL);
					doBlock = ast.newBlock();
					doBlock.statements().add(ASTNode.copySubtree(doBlock.getAST(), doBody));
				}

				Block newDoBlock = processBlock(doBlock, null, message);
				ASTNode lastStatement = (ASTNode) newDoBlock.statements().get(newDoBlock.statements().size() - 1);
				if (!(lastStatement instanceof BreakStatement || lastStatement instanceof ReturnStatement)) {
					int lineNumber = _cu.getLineNumber(doStatement.getExpression().getStartPosition());
					Statement insert = GenStatement.genASTNode(message, lineNumber);
					newDoBlock.statements().add(ASTNode.copySubtree(newDoBlock.getAST(), insert));
				}

				doStatement.setBody((Statement) ASTNode.copySubtree(doStatement.getAST(), newDoBlock));
			}

			result.add(doStatement);
		} else if (statement instanceof Block) {
			Block block = (Block) statement;
			Block newBlock = processBlock(block, null, message);
			result.add(newBlock);
		} else if (statement instanceof EnhancedForStatement) {

			EnhancedForStatement enhancedForStatement = (EnhancedForStatement) statement;
			Statement enhancedBody = enhancedForStatement.getBody();
			if (enhancedBody != null) {
				Block enhancedBlock = null;
				if (enhancedBody instanceof Block) {
					enhancedBlock = (Block) enhancedBody;
				} else {
					AST ast = AST.newAST(Constant.AST_LEVEL);
					enhancedBlock = ast.newBlock();
					enhancedBlock.statements().add(ASTNode.copySubtree(enhancedBlock.getAST(), enhancedBody));
				}

				int lineNumber = _cu.getLineNumber(enhancedForStatement.getExpression().getStartPosition());
				Statement insert = GenStatement.genASTNode(message, lineNumber);
				Block newEnhancedBlock = processBlock(enhancedBlock, insert, message);
				enhancedForStatement
						.setBody((Statement) ASTNode.copySubtree(enhancedForStatement.getAST(), newEnhancedBlock));
			}

			result.add(enhancedForStatement);
		} else if (statement instanceof SwitchStatement) {

			SwitchStatement switchStatement = (SwitchStatement) statement;
			List<ASTNode> statements = new ArrayList<>();
			AST ast = AST.newAST(Constant.AST_LEVEL);
			for (Object object : switchStatement.statements()) {
				ASTNode astNode = (ASTNode) object;
				statements.add(ASTNode.copySubtree(ast, astNode));
			}

			switchStatement.statements().clear();

			for (ASTNode astNode : statements) {
				if (astNode instanceof Statement) {
					Statement s = (Statement) astNode;
					for (Statement statement2 : process(s, message)) {
						switchStatement.statements().add(ASTNode.copySubtree(switchStatement.getAST(), statement2));
					}
				} else {
					switchStatement.statements().add(ASTNode.copySubtree(switchStatement.getAST(), astNode));
				}
			}

			int lineNumber = _cu.getLineNumber(switchStatement.getExpression().getStartPosition());
			Statement insert = GenStatement.genASTNode(message, lineNumber);

			result.add(insert);
			result.add(switchStatement);
		} else if (statement instanceof TryStatement) {

			TryStatement tryStatement = (TryStatement) statement;

			Block tryBlock = tryStatement.getBody();
			if (tryBlock != null) {
				Block newTryBlock = processBlock(tryBlock, null, message);
				tryStatement.setBody((Block) ASTNode.copySubtree(tryStatement.getAST(), newTryBlock));
			}

			List catchList = tryStatement.catchClauses();
			if (catchList != null) {
				for (Object object : catchList) {
					if (object instanceof CatchClause) {
						CatchClause catchClause = (CatchClause) object;
						Block catchBlock = catchClause.getBody();
						Block newCatchBlock = processBlock(catchBlock, null, message);
						catchClause.setBody((Block) ASTNode.copySubtree(catchClause.getAST(), newCatchBlock));
					}
				}
			}

			Block finallyBlock = tryStatement.getFinally();
			if (finallyBlock != null) {
				Block newFinallyBlock = processBlock(finallyBlock, null, message);
				tryStatement.setFinally((Block) ASTNode.copySubtree(tryStatement.getAST(), newFinallyBlock));
			}

			result.add(tryStatement);
		} else {
			int lineNumber = _cu.getLineNumber(statement.getStartPosition());
			Statement copy = (Statement) ASTNode.copySubtree(AST.newAST(Constant.AST_LEVEL), statement);
			Statement insert = GenStatement.genASTNode(message, lineNumber);

			if (statement instanceof ConstructorInvocation || statement instanceof SuperConstructorInvocation) {
				result.add(copy);
				result.add(insert);
			} else if (statement instanceof ContinueStatement || statement instanceof BreakStatement
					|| statement instanceof ReturnStatement || statement instanceof ThrowStatement
					|| statement instanceof AssertStatement || statement instanceof ExpressionStatement
					|| statement instanceof VariableDeclarationStatement) {
				result.add(insert);
				result.add(copy);

			} else if (statement instanceof LabeledStatement) {
				result.add(copy);
			} else if (statement instanceof SynchronizedStatement) {
				result.add(copy);
			} else {
				result.add(copy);
			}
		}

		return result;
	}

	private Block processBlock(Block block, Statement insert, String message) {
		Block newBlock = AST.newAST(Constant.AST_LEVEL).newBlock();
		if (block == null) {
			return newBlock;
		}
		if (insert != null) {
			newBlock.statements().add(ASTNode.copySubtree(newBlock.getAST(), insert));
		}
		for (Object object : block.statements()) {
			if (object instanceof Statement) {
				Statement statement = (Statement) object;
				List<Statement> newStatements = process(statement, message);
				for (Statement newStatement : newStatements) {
					newBlock.statements().add(ASTNode.copySubtree(newBlock.getAST(), newStatement));
				}
			} else {
				LevelLogger.error(__name__ + "#processBlock UNKNOWN astNode : " + object.toString());
			}
		}
		return newBlock;
	}

}
