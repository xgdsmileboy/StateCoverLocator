/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
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
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.inst.gen.GenStatement;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class InstrumentAllPredicatesVisitor extends TraversalVisitor {

	private final static String __name__ = "@InstrumentAllPredicatesVisitor ";

	private Map<Integer, Map<Integer, Set<String>>> _allInstruments = null;
	private Map<Integer, Set<String>> _currentConditions = null;

	public InstrumentAllPredicatesVisitor(Map<Integer, Map<Integer, Set<String>>> allInstruments) {
		_allInstruments = allInstruments;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		_cu = node;
		if (_allInstruments == null) {
			return false;
		}
		if (node.getPackage().getName() != null
				&& node.getPackage().getName().getFullyQualifiedName().equals("auxiliary")) {
			return false;
		}
		// filter unrelative files
		boolean continueVisit = false;
		for (Integer methodID : _allInstruments.keySet()) {
			String methodInfo = Identifier.getMessage(methodID);
			if (methodInfo.contains(_cu.getPackage().getName().getFullyQualifiedName())) {
				continueVisit = true;
				break;
			}
		}
		if (!continueVisit) {
			return false;
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodDeclaration node) {

		String message = buildMethodInfoString(node);
		if (message == null) {
			return true;
		}
		int methodID = Identifier.getIdentifier(message);

		if (!_allInstruments.containsKey(methodID)) {
			return true;
		}
		// record instrument information for current method and remove it from
		// the candidates to reduce future searching overhead
		_currentConditions = _allInstruments.get(methodID);
		_allInstruments.remove(methodID);

		String keyValue = String.valueOf(methodID);
		// optimize instrument
		message = Constant.INSTRUMENT_FLAG + _methodFlag + "#" + keyValue;

		Block methodBody = node.getBody();

		if (methodBody == null) {
			return true;
		}

		List<ASTNode> blockStatement = new ArrayList<>();

		for (int i = 0; i < methodBody.statements().size(); i++) {
			ASTNode astNode = (ASTNode) methodBody.statements().get(i);
			blockStatement.addAll(process(astNode, keyValue));
		}

		methodBody.statements().clear();
		for (ASTNode statement : blockStatement) {
			methodBody.statements().add(ASTNode.copySubtree(methodBody.getAST(), statement));
		}

		return true;
	}

	private List<ASTNode> process(ASTNode statement, String message) {

		List<ASTNode> result = new ArrayList<>();
		//
		// int startLine = _cu.getLineNumber(statement.getStartPosition());
		// int endLine = _cu.getLineNumber(statement.getStartPosition() +
		// statement.getLength());
		// if(startLine > _mutantLineNumber || endLine < _mutantLineNumber){
		// result.add(ASTNode.copySubtree(AST.newAST(AST.JLS8), statement));
		// return result;
		// }

		if (statement instanceof IfStatement) {

			IfStatement ifStatement = (IfStatement) statement;
			int expLine = _cu.getLineNumber(ifStatement.getExpression().getStartPosition());
			result.addAll(genListConditions(message, expLine));

			Statement thenBody = ifStatement.getThenStatement();

			if (thenBody != null) {
				Block thenBlock = null;
				if (thenBody instanceof Block) {
					thenBlock = (Block) thenBody;
				} else {
					AST ast = AST.newAST(AST.JLS8);
					thenBlock = ast.newBlock();
					thenBlock.statements().add(ASTNode.copySubtree(thenBlock.getAST(), thenBody));
				}

				Block newThenBlock = processBlock(thenBlock, message);
				ifStatement.setThenStatement((Statement) ASTNode.copySubtree(ifStatement.getAST(), newThenBlock));
			}

			Statement elseBody = ifStatement.getElseStatement();
			if (elseBody != null) {
				Block elseBlock = null;
				if (elseBody instanceof Block) {
					elseBlock = (Block) elseBody;
				} else {
					AST ast = AST.newAST(AST.JLS8);
					elseBlock = ast.newBlock();
					elseBlock.statements().add(ASTNode.copySubtree(elseBlock.getAST(), elseBody));
				}
				Block newElseBlock = processBlock(elseBlock, message);
				ifStatement.setElseStatement((Statement) ASTNode.copySubtree(ifStatement.getAST(), newElseBlock));
			}
			result.add(ifStatement);
		} else if (statement instanceof WhileStatement) {

			WhileStatement whileStatement = (WhileStatement) statement;

			int lineNumber = _cu.getLineNumber(whileStatement.getExpression().getStartPosition());
			result.addAll(genListConditions(message, lineNumber));

			Statement whilebody = whileStatement.getBody();

			if (whilebody != null) {
				Block whileBlock = null;
				if (whilebody instanceof Block) {
					whileBlock = (Block) whilebody;
				} else {
					AST ast = AST.newAST(AST.JLS8);
					whileBlock = ast.newBlock();
					whileBlock.statements().add(ASTNode.copySubtree(whileBlock.getAST(), whilebody));
				}

				Block newWhileBlock = processBlock(whileBlock, message);
				whileStatement.setBody((Statement) ASTNode.copySubtree(whileStatement.getAST(), newWhileBlock));
			}
			result.add(whileStatement);
		} else if (statement instanceof ForStatement) {

			ForStatement forStatement = (ForStatement) statement;

			int lineNumber = _cu.getLineNumber(forStatement.getExpression().getStartPosition());
			result.addAll(genListConditions(message, lineNumber));

			Statement forBody = forStatement.getBody();

			if (forBody != null) {
				Block forBlock = null;
				if (forBody instanceof Block) {
					forBlock = (Block) forBody;
				} else {
					AST ast = AST.newAST(AST.JLS8);
					forBlock = ast.newBlock();
					forBlock.statements().add(ASTNode.copySubtree(forBlock.getAST(), forBody));
				}

				Block newForBlock = processBlock(forBlock, message);
				forStatement.setBody((Statement) ASTNode.copySubtree(forStatement.getAST(), newForBlock));
			}

			result.add(forStatement);
		} else if (statement instanceof DoStatement) {

			DoStatement doStatement = (DoStatement) statement;

			int lineNumber = _cu.getLineNumber(doStatement.getExpression().getStartPosition());
			result.addAll(genListConditions(message, lineNumber));

			Statement doBody = doStatement.getBody();
			if (doBody != null) {
				Block doBlock = null;
				if (doBody instanceof Block) {
					doBlock = (Block) doBody;
				} else {
					AST ast = AST.newAST(AST.JLS8);
					doBlock = ast.newBlock();
					doBlock.statements().add(ASTNode.copySubtree(doBlock.getAST(), doBody));
				}

				Block newDoBlock = processBlock(doBlock, message);
				doStatement.setBody((Statement) ASTNode.copySubtree(doStatement.getAST(), newDoBlock));
			}

			result.add(doStatement);
		} else if (statement instanceof Block) {
			Block block = (Block) statement;
			Block newBlock = processBlock(block, message);
			result.add(newBlock);
		} else if (statement instanceof EnhancedForStatement) {

			EnhancedForStatement enhancedForStatement = (EnhancedForStatement) statement;

			int lineNumber = _cu.getLineNumber(enhancedForStatement.getExpression().getStartPosition());
			result.addAll(genListConditions(message, lineNumber));

			Statement enhancedBody = enhancedForStatement.getBody();
			if (enhancedBody != null) {
				Block enhancedBlock = null;
				if (enhancedBody instanceof Block) {
					enhancedBlock = (Block) enhancedBody;
				} else {
					AST ast = AST.newAST(AST.JLS8);
					enhancedBlock = ast.newBlock();
					enhancedBlock.statements().add(ASTNode.copySubtree(enhancedBlock.getAST(), enhancedBody));
				}
				Block newEnhancedBlock = processBlock(enhancedBlock, message);
				enhancedForStatement
						.setBody((Statement) ASTNode.copySubtree(enhancedForStatement.getAST(), newEnhancedBlock));
			}

			result.add(enhancedForStatement);
		} else if (statement instanceof SwitchStatement) {

			SwitchStatement switchStatement = (SwitchStatement) statement;

			int lineNumber = _cu.getLineNumber(switchStatement.getExpression().getStartPosition());
			result.addAll(genListConditions(message, lineNumber));

			List<ASTNode> statements = new ArrayList<>();
			AST ast = AST.newAST(AST.JLS8);
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
				Block newTryBlock = processBlock(tryBlock, message);
				tryStatement.setBody((Block) ASTNode.copySubtree(tryStatement.getAST(), newTryBlock));
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
				Block newFinallyBlock = processBlock(finallyBlock, message);
				tryStatement.setFinally((Block) ASTNode.copySubtree(tryStatement.getAST(), newFinallyBlock));
			}

			result.add(tryStatement);
		} else {
			int lineNumber = _cu.getLineNumber(statement.getStartPosition());
			List<ASTNode> insert = genListConditions(message, lineNumber);
			ASTNode copy = ASTNode.copySubtree(AST.newAST(AST.JLS8), statement);
			if (statement instanceof ConstructorInvocation) {
				result.add(copy);
				result.addAll(insert);
			} else if (statement instanceof ContinueStatement || statement instanceof BreakStatement
					|| statement instanceof ReturnStatement || statement instanceof ThrowStatement
					|| statement instanceof AssertStatement || statement instanceof ExpressionStatement
					|| statement instanceof ConstructorInvocation
					|| statement instanceof VariableDeclarationStatement) {
				result.addAll(insert);
				result.add(copy);

			} else if (statement instanceof LabeledStatement) {
				result.addAll(insert);
				result.add(copy);
			} else if (statement instanceof SynchronizedStatement) {
				result.addAll(insert);
				result.add(copy);
			} else {
				result.addAll(insert);
				result.add(copy);
			}
		}

		return result;
	}

	private Block processBlock(Block block, String message) {
		Block newBlock = AST.newAST(AST.JLS8).newBlock();
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

	private List<ASTNode> genListConditions(String message, Integer line) {
		List<ASTNode> result = new ArrayList<>();
		if (_currentConditions.containsKey(line)) {
			for (String condition : _currentConditions.get(line)) {
				ASTNode inserted = GenStatement.genPredicateStatement(condition, message, line);
				result.add(inserted);
			}
			_currentConditions.remove(line);
		}
		return result;
	}

}
