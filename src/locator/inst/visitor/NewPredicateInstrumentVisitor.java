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
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
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
import locator.common.util.Pair;
import locator.inst.gen.GenStatement;

/**
 * @author Jiajun
 * @date Jun 2, 2017
 */
public class NewPredicateInstrumentVisitor extends TraversalVisitor {

	private final static String __name__ = "@NewPredicateInstrumentVisitor ";

	private int _line = -1;

	private List<Pair<String, String>> _condition = null;

	/**
	 * 
	 */
	public NewPredicateInstrumentVisitor(List<Pair<String, String>> condition, int line) {
		_condition = condition;
		_line = line;
	}

	public void setCondition(List<Pair<String, String>> condition) {
		_condition = condition;
	}

	@Override
	public boolean visit(MethodDeclaration node) {

		String message = buildMethodInfoString(node);
		if (message == null) {
			return true;
		}
		String keyValue = String.valueOf(Identifier.getIdentifier(message));
		// optimize instrument
//		message = Constant.INSTRUMENT_FLAG + _methodFlag + Constant.INSTRUMENT_STR_SEP + keyValue + "#" + _line;
		message = keyValue + "#" + _line;

		Block methodBody = node.getBody();

		if (methodBody == null) {
			return true;
		}

		List<ASTNode> blockStatement = new ArrayList<>();

		int startLine = _cu.getLineNumber(node.getStartPosition());
		int endLine = _cu.getLineNumber(node.getStartPosition() + node.getLength());

		if (_line < startLine || endLine < _line) {
			return true;
		}

		for (int i = 0; i < methodBody.statements().size(); i++) {
			ASTNode astNode = (ASTNode) methodBody.statements().get(i);
			blockStatement.addAll(process(astNode, message));
		}

		methodBody.statements().clear();
		for (ASTNode statement : blockStatement) {
			methodBody.statements().add(ASTNode.copySubtree(methodBody.getAST(), statement));
		}

		return true;
	}

	private List<ASTNode> process(ASTNode statement, String message) {

		List<ASTNode> result = new ArrayList<>();

		int startLine = _cu.getLineNumber(statement.getStartPosition());
		int endLine = _cu.getLineNumber(statement.getStartPosition() + statement.getLength());
		if (startLine > _line || endLine < _line) {
			result.add(ASTNode.copySubtree(AST.newAST(Constant.AST_LEVEL), statement));
			return result;
		}

		if (statement instanceof IfStatement) {

			IfStatement ifStatement = (IfStatement) statement;
			startLine = _cu.getLineNumber(ifStatement.getExpression().getStartPosition());
			if (startLine == _line) {
				// ASTNode inserted =
				// GenStatement.genPredicateStatement(_condition, message,
				// _line);
				for(int count = 0; count < _condition.size(); count ++){
					String condition = _condition.get(count).getFirst();
					String prob = _condition.get(count).getSecond();
					ASTNode inserted = GenStatement.newGenPredicateStatement(condition, message + "#" + condition + "#" + prob + "#" + count);
					if(inserted != null){
						result.add(inserted);
					}
				}
				result.add(ASTNode.copySubtree(AST.newAST(Constant.AST_LEVEL), statement));
				return result;
			}

			Statement thenBody = ifStatement.getThenStatement();

			if (thenBody != null) {
				startLine = _cu.getLineNumber(thenBody.getStartPosition());
				endLine = _cu.getLineNumber(thenBody.getStartPosition() + thenBody.getLength());
				if (startLine <= _line && _line <= endLine) {
					Block thenBlock = null;
					if (thenBody instanceof Block) {
						thenBlock = (Block) thenBody;
					} else {
						AST ast = AST.newAST(Constant.AST_LEVEL);
						thenBlock = ast.newBlock();
						thenBlock.statements().add(ASTNode.copySubtree(thenBlock.getAST(), thenBody));
					}

					Block newThenBlock = processBlock(thenBlock, message);
					ifStatement.setThenStatement((Statement) ASTNode.copySubtree(ifStatement.getAST(), newThenBlock));
				}
			}

			Statement elseBody = ifStatement.getElseStatement();
			if (elseBody != null) {
				startLine = _cu.getLineNumber(elseBody.getStartPosition());
				endLine = _cu.getLineNumber(elseBody.getStartPosition() + elseBody.getLength());
				if (startLine <= _line && _line <= endLine) {
					Block elseBlock = null;
					if (elseBody instanceof Block) {
						elseBlock = (Block) elseBody;
					} else {
						AST ast = AST.newAST(Constant.AST_LEVEL);
						elseBlock = ast.newBlock();
						elseBlock.statements().add(ASTNode.copySubtree(elseBlock.getAST(), elseBody));
					}
					Block newElseBlock = processBlock(elseBlock, message);
					ifStatement.setElseStatement((Statement) ASTNode.copySubtree(ifStatement.getAST(), newElseBlock));
				}
			}
			result.add(ifStatement);
		} else if (statement instanceof WhileStatement) {

			WhileStatement whileStatement = (WhileStatement) statement;

			int lineNumber = _cu.getLineNumber(whileStatement.getExpression().getStartPosition());
			if (lineNumber == _line) {
				// ASTNode inserted =
				// GenStatement.genPredicateStatement(_condition, message,
				// _line);
				Block block = whileStatement.getAST().newBlock();
				for(int count = 0; count < _condition.size(); count ++){
					String condition = _condition.get(count).getFirst();
					String prob = _condition.get(count).getSecond();
					ASTNode inserted = GenStatement.newGenPredicateStatement(condition, message + "#" + condition + "#" + prob + "#" + count);
					if(inserted != null){
						result.add(inserted);
						block.statements().add(ASTNode.copySubtree(block.getAST(), inserted));
					}
				}
				block = extractNodeIntoBlock(block, whileStatement.getBody());
				whileStatement.setBody(block);
				result.add(ASTNode.copySubtree(AST.newAST(Constant.AST_LEVEL), whileStatement));
				return result;
			}

			Statement whilebody = whileStatement.getBody();

			if (whilebody != null) {
				startLine = _cu.getLineNumber(whilebody.getStartPosition());
				endLine = _cu.getLineNumber(whilebody.getStartPosition() + whilebody.getLength());
				if (startLine <= _line && _line <= endLine) {
					Block whileBlock = null;
					if (whilebody instanceof Block) {
						whileBlock = (Block) whilebody;
					} else {
						AST ast = AST.newAST(Constant.AST_LEVEL);
						whileBlock = ast.newBlock();
						whileBlock.statements().add(ASTNode.copySubtree(whileBlock.getAST(), whilebody));
					}

					Block newWhileBlock = processBlock(whileBlock, message);
					whileStatement.setBody((Statement) ASTNode.copySubtree(whileStatement.getAST(), newWhileBlock));
				}
			}
			result.add(whileStatement);
		} else if (statement instanceof ForStatement) {

			ForStatement forStatement = (ForStatement) statement;

			int lineNumber = -1;
			if(forStatement.getExpression() != null){
				lineNumber = _cu.getLineNumber(forStatement.getExpression().getStartPosition());
			} else if(forStatement.initializers() != null && forStatement.initializers().size() > 0){
				lineNumber = _cu.getLineNumber(((ASTNode)forStatement.initializers().get(0)).getStartPosition());
			} else if(forStatement.updaters() != null && forStatement.updaters().size() > 0){
				lineNumber = _cu.getLineNumber(((ASTNode)forStatement.updaters().get(0)).getStartPosition());
			}
			
			if (lineNumber == _line) {
				// ASTNode inserted =
				// GenStatement.genPredicateStatement(_condition, message,
				// _line);
				Block block = forStatement.getAST().newBlock();
				for(int count = 0; count < _condition.size(); count ++){
					String condition = _condition.get(count).getFirst();
					String prob = _condition.get(count).getSecond();
					ASTNode inserted = GenStatement.newGenPredicateStatement(condition, message + "#" + condition + "#" + prob + "#" + count);
					if(inserted != null){
						block.statements().add(ASTNode.copySubtree(block.getAST(), inserted));
						result.add(inserted);
					}
				}
				
				block = extractNodeIntoBlock(block, forStatement.getBody());
				forStatement.setBody(block);
				result.add(ASTNode.copySubtree(AST.newAST(Constant.AST_LEVEL), forStatement));
				return result;
			}

			Statement forBody = forStatement.getBody();

			if (forBody != null) {
				startLine = _cu.getLineNumber(forBody.getStartPosition());
				endLine = _cu.getLineNumber(forBody.getStartPosition() + forBody.getLength());
				if (startLine <= _line && _line <= endLine) {
					Block forBlock = null;
					if (forBody instanceof Block) {
						forBlock = (Block) forBody;
					} else {
						AST ast = AST.newAST(Constant.AST_LEVEL);
						forBlock = ast.newBlock();
						forBlock.statements().add(ASTNode.copySubtree(forBlock.getAST(), forBody));
					}

					Block newForBlock = processBlock(forBlock, message);
					forStatement.setBody((Statement) ASTNode.copySubtree(forStatement.getAST(), newForBlock));
				}
			}

			result.add(forStatement);
		} else if (statement instanceof DoStatement) {

			DoStatement doStatement = (DoStatement) statement;

			int lineNumber = _cu.getLineNumber(doStatement.getExpression().getStartPosition());
			if (lineNumber == _line) {
				// ASTNode inserted =
				// GenStatement.genPredicateStatement(_condition, message,
				// _line);
				Block block = doStatement.getAST().newBlock();
				block = extractNodeIntoBlock(block, doStatement.getBody());
				
				for(int count = 0; count < _condition.size(); count ++){
					String condition = _condition.get(count).getFirst();
					String prob = _condition.get(count).getSecond();
					ASTNode inserted = GenStatement.newGenPredicateStatement(condition, message + "#" + condition + "#" + prob + "#" + count);
					if(inserted != null){
						block.statements().add(ASTNode.copySubtree(block.getAST(), inserted));
					}
				}
				
				doStatement.setBody(block);
				result.add(ASTNode.copySubtree(AST.newAST(Constant.AST_LEVEL), doStatement));
				return result;
			}

			Statement doBody = doStatement.getBody();
			if (doBody != null) {
				startLine = _cu.getLineNumber(doBody.getStartPosition());
				endLine = _cu.getLineNumber(doBody.getStartPosition() + doBody.getLength());
				if (startLine <= _line && _line <= endLine) {
					Block doBlock = null;
					if (doBody instanceof Block) {
						doBlock = (Block) doBody;
					} else {
						AST ast = AST.newAST(Constant.AST_LEVEL);
						doBlock = ast.newBlock();
						doBlock.statements().add(ASTNode.copySubtree(doBlock.getAST(), doBody));
					}

					Block newDoBlock = processBlock(doBlock, message);
					doStatement.setBody((Statement) ASTNode.copySubtree(doStatement.getAST(), newDoBlock));
				}
			}

			result.add(doStatement);
		} else if (statement instanceof Block) {
			Block block = (Block) statement;
			Block newBlock = processBlock(block, message);
			result.add(newBlock);
		} else if (statement instanceof EnhancedForStatement) {

			EnhancedForStatement enhancedForStatement = (EnhancedForStatement) statement;

			int lineNumber = _cu.getLineNumber(enhancedForStatement.getExpression().getStartPosition());
			if (lineNumber == _line) {
				// ASTNode inserted =
				// GenStatement.genPredicateStatement(_condition, message,
				// _line);
				Block block = enhancedForStatement.getAST().newBlock();
				for(int count = 0; count < _condition.size(); count ++){
					String condition = _condition.get(count).getFirst();
					String prob = _condition.get(count).getSecond();
					ASTNode inserted = GenStatement.newGenPredicateStatement(condition, message + "#" + condition + "#" + prob + "#" + count);
					if(inserted != null){
						result.add(inserted);
						block.statements().add(ASTNode.copySubtree(block.getAST(), inserted));
					}
				}
				block = extractNodeIntoBlock(block, enhancedForStatement.getBody());
				enhancedForStatement.setBody(block);
				result.add(ASTNode.copySubtree(AST.newAST(Constant.AST_LEVEL), enhancedForStatement));
				return result;
			}

			Statement enhancedBody = enhancedForStatement.getBody();
			if (enhancedBody != null) {

				startLine = _cu.getLineNumber(enhancedBody.getStartPosition());
				endLine = _cu.getLineNumber(enhancedBody.getStartPosition() + enhancedBody.getLength());
				if (startLine <= _line && _line <= endLine) {

					Block enhancedBlock = null;
					if (enhancedBody instanceof Block) {
						enhancedBlock = (Block) enhancedBody;
					} else {
						AST ast = AST.newAST(Constant.AST_LEVEL);
						enhancedBlock = ast.newBlock();
						enhancedBlock.statements().add(ASTNode.copySubtree(enhancedBlock.getAST(), enhancedBody));
					}
					Block newEnhancedBlock = processBlock(enhancedBlock, message);
					enhancedForStatement
							.setBody((Statement) ASTNode.copySubtree(enhancedForStatement.getAST(), newEnhancedBlock));
				}
			}

			result.add(enhancedForStatement);
		} else if (statement instanceof SwitchStatement) {

			SwitchStatement switchStatement = (SwitchStatement) statement;

			int lineNumber = _cu.getLineNumber(switchStatement.getExpression().getStartPosition());
			if (lineNumber == _line) {
				// ASTNode inserted =
				// GenStatement.genPredicateStatement(_condition, message,
				// _line);
				for(int count = 0; count < _condition.size(); count ++){
					String condition = _condition.get(count).getFirst();
					String prob = _condition.get(count).getSecond();
					ASTNode inserted = GenStatement.newGenPredicateStatement(condition, message + "#" + condition + "#" + prob + "#" + count);
					if(inserted != null){
						result.add(inserted);
					}
				}
				result.add(ASTNode.copySubtree(AST.newAST(Constant.AST_LEVEL), statement));
				return result;
			}

			List<ASTNode> statements = new ArrayList<>();
			AST ast = AST.newAST(Constant.AST_LEVEL);
			for (Object object : switchStatement.statements()) {
				ASTNode astNode = (ASTNode) object;
				statements.add(ASTNode.copySubtree(ast, astNode));
			}

			switchStatement.statements().clear();

			for (ASTNode astNode : statements) {
				for (ASTNode node : process(astNode, message)) {
					switchStatement.statements().add(ASTNode.copySubtree(switchStatement.getAST(), node));
				}
			}

			result.add(switchStatement);
		} else if (statement instanceof TryStatement) {

			TryStatement tryStatement = (TryStatement) statement;

			Block tryBlock = tryStatement.getBody();

			if (tryBlock != null) {
				startLine = _cu.getLineNumber(tryBlock.getStartPosition());
				endLine = _cu.getLineNumber(tryBlock.getStartPosition() + tryBlock.getLength());
				if (startLine <= _line && _line <= endLine) {
					Block newTryBlock = processBlock(tryBlock, message);
					tryStatement.setBody((Block) ASTNode.copySubtree(tryStatement.getAST(), newTryBlock));
				}
			}

			List catchList = tryStatement.catchClauses();
			if (catchList != null) {
				for (Object object : catchList) {
					if (object instanceof CatchClause) {
						CatchClause catchClause = (CatchClause) object;
						Block catchBlock = catchClause.getBody();
						Block newCatchBlock = processBlock(catchBlock, message);
						catchClause.setBody((Block) ASTNode.copySubtree(catchClause.getAST(), newCatchBlock));
					}
				}
			}

			Block finallyBlock = tryStatement.getFinally();
			if (finallyBlock != null) {
				startLine = _cu.getLineNumber(finallyBlock.getStartPosition());
				endLine = _cu.getLineNumber(finallyBlock.getStartPosition() + finallyBlock.getLength());
				if (startLine <= _line && _line <= endLine) {
					Block newFinallyBlock = processBlock(finallyBlock, message);
					tryStatement.setFinally((Block) ASTNode.copySubtree(tryStatement.getAST(), newFinallyBlock));
				}
			}

			result.add(tryStatement);
		} else {
			Statement copy = (Statement) ASTNode.copySubtree(AST.newAST(Constant.AST_LEVEL), statement);
			// Statement insert = GenStatement.genPredicateStatement(_condition,
			// message, _line);
			List<ASTNode> tmpInserted = new ArrayList<>();
			for(int count = 0; count < _condition.size(); count ++){
				String condition = _condition.get(count).getFirst();
				String prob = _condition.get(count).getSecond();
				ASTNode inserted = GenStatement.newGenPredicateStatement(condition, message + "#" + condition + "#" + prob + "#" + count);
				if(inserted != null){
					tmpInserted.add(inserted);
				}
			}
//			Statement insert = GenStatement.newGenPredicateStatement(_condition, message);
			
			// fix, 2018-1-5, insert statement for left hand side variable in
			// assignment
			if ((statement instanceof ExpressionStatement
					&& ((ExpressionStatement) statement).getExpression() instanceof Assignment)
					|| statement instanceof VariableDeclarationStatement || statement instanceof ConstructorInvocation
					|| statement instanceof SuperConstructorInvocation) {
				result.add(copy);
				// result.add(insert);
				result.addAll(tmpInserted);
			} else if (statement instanceof ContinueStatement || statement instanceof BreakStatement
					|| statement instanceof ReturnStatement || statement instanceof ThrowStatement
					|| statement instanceof AssertStatement || statement instanceof ExpressionStatement) {
//				result.add(insert);
				result.addAll(tmpInserted);
				result.add(copy);

			} else if (statement instanceof LabeledStatement) {
//				result.add(insert);
				result.addAll(tmpInserted);
				result.add(copy);
			} else if (statement instanceof SynchronizedStatement) {
//				result.add(insert);
				result.addAll(tmpInserted);
				result.add(copy);
			} else {
//				result.add(insert);
				result.addAll(tmpInserted);
				result.add(copy);
			}
		}

		return result;
	}
	
	private Block extractNodeIntoBlock(Block block, ASTNode node) {
		if(node != null) {
			if(node instanceof Block) {
				block.statements().addAll(ASTNode.copySubtrees(block.getAST(), ((Block) node).statements()));
			} else {
				block.statements().add(ASTNode.copySubtree(block.getAST(), node));
			}
		}
		return block;
	}

	private Block processBlock(Block block, String message) {
		Block newBlock = AST.newAST(Constant.AST_LEVEL).newBlock();
		if (block == null) {
			return newBlock;
		}
		for (Object object : block.statements()) {
			ASTNode astNode = (ASTNode) object;
			List<ASTNode> newStatements = process(astNode, message);
			for (ASTNode newStatement : newStatements) {
				newBlock.statements().add(ASTNode.copySubtree(newBlock.getAST(), newStatement));
			}
		}
		return newBlock;
	}

}
