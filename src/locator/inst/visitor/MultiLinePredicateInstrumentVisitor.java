/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import locator.common.java.Pair;
import locator.inst.gen.GenStatement;

/**
 * @author Jiajun
 * @date Jun 19, 2017
 */
public class MultiLinePredicateInstrumentVisitor extends TraversalVisitor{


	private final static String __name__ = "@MultiLinePredicateInstrumentVisitor ";
	
	private Map<Integer, List<Pair<String, String>>> _condition = null;
	
	private boolean _useSober = false;

	public MultiLinePredicateInstrumentVisitor(boolean useSober) {
		_condition = new HashMap<>();
		_useSober = useSober;
	}
	
	public MultiLinePredicateInstrumentVisitor(Map<Integer, List<Pair<String, String>>> condition, boolean useSober) {
		_condition = condition;
		_useSober = useSober;
	}

	public void setCondition(Map<Integer, List<Pair<String, String>>> condition) {
		_condition = condition;
	}

	@Override
	public boolean visit(MethodDeclaration node) {

		int startLine = _cu.getLineNumber(node.getStartPosition());
		int endLine = _cu.getLineNumber(node.getStartPosition() + node.getLength());

		// no line need to be instrument for current method declaration
		if(!containLine(startLine, endLine)){
			return true;
		}
		
		
		String message = buildMethodInfoString(node);
		if (message == null) {
			return true;
		}
		String keyValue = String.valueOf(Identifier.getIdentifier(message));
		// optimize instrument
		String methodID = keyValue;

		Block methodBody = node.getBody();

		if (methodBody == null) {
			return true;
		}

		List<ASTNode> blockStatement = new ArrayList<>();

		for (int i = 0; i < methodBody.statements().size(); i++) {
			ASTNode astNode = (ASTNode) methodBody.statements().get(i);
			blockStatement.addAll(process(astNode, methodID));
		}

		methodBody.statements().clear();
		for (ASTNode statement : blockStatement) {
			methodBody.statements().add(ASTNode.copySubtree(methodBody.getAST(), statement));
		}

		return true;
	}

	private List<ASTNode> process(ASTNode statement, String methodID) {

		List<ASTNode> result = new ArrayList<>();

		int startLine = _cu.getLineNumber(statement.getStartPosition());
		int endLine = _cu.getLineNumber(statement.getStartPosition() + statement.getLength());
		
		if(!containLine(startLine, endLine)){
			result.add(statement);
			return result;
		}

		if (statement instanceof IfStatement) {

			IfStatement ifStatement = (IfStatement) statement;
			startLine = _cu.getLineNumber(ifStatement.getExpression().getStartPosition());
			result.addAll(genInstrument(methodID, startLine));

			Statement thenBody = ifStatement.getThenStatement();

			if (thenBody != null) {
				startLine = _cu.getLineNumber(thenBody.getStartPosition());
				endLine = _cu.getLineNumber(thenBody.getStartPosition() + thenBody.getLength());
				if(containLine(startLine, endLine)){
					Block newThenBlock = processBlock(wrapBlock(thenBody), methodID);
					ifStatement.setThenStatement((Statement) ASTNode.copySubtree(ifStatement.getAST(), newThenBlock));
				}
			}

			Statement elseBody = ifStatement.getElseStatement();
			if (elseBody != null) {
				startLine = _cu.getLineNumber(elseBody.getStartPosition());
				endLine = _cu.getLineNumber(elseBody.getStartPosition() + elseBody.getLength());
				if(containLine(startLine, endLine)){
					Block newElseBlock = processBlock(wrapBlock(elseBody), methodID);
					ifStatement.setElseStatement((Statement) ASTNode.copySubtree(ifStatement.getAST(), newElseBlock));
				}
			}
			result.add(ifStatement);
		} else if (statement instanceof WhileStatement) {
			
			WhileStatement whileStatement = (WhileStatement) statement;
			int lineNumber = _cu.getLineNumber(whileStatement.getExpression().getStartPosition());
			result.addAll(genInstrument(methodID, lineNumber));

			Statement whilebody = whileStatement.getBody();

			if (whilebody != null) {
				startLine = _cu.getLineNumber(whilebody.getStartPosition());
				endLine = _cu.getLineNumber(whilebody.getStartPosition() + whilebody.getLength());
				if(containLine(startLine, endLine)){
					Block newWhileBlock = processBlock(wrapBlock(whilebody), methodID);
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
			if(lineNumber != -1){
				result.addAll(genInstrument(methodID, lineNumber));
			}

			Statement forBody = forStatement.getBody();

			if (forBody != null) {
				startLine = _cu.getLineNumber(forBody.getStartPosition());
				endLine = _cu.getLineNumber(forBody.getStartPosition() + forBody.getLength());
				if(containLine(startLine, endLine)) {
					Block newForBlock = processBlock(wrapBlock(forBody), methodID);
					forStatement.setBody((Statement) ASTNode.copySubtree(forStatement.getAST(), newForBlock));
				}
			}

			result.add(forStatement);
		} else if (statement instanceof DoStatement) {

			DoStatement doStatement = (DoStatement) statement;

			int lineNumber = _cu.getLineNumber(doStatement.getExpression().getStartPosition());
			result.addAll(genInstrument(methodID, lineNumber));

			Statement doBody = doStatement.getBody();
			if (doBody != null) {
				startLine = _cu.getLineNumber(doBody.getStartPosition());
				endLine = _cu.getLineNumber(doBody.getStartPosition() + doBody.getLength());
				if(containLine(startLine, endLine)) {
					Block newDoBlock = processBlock(wrapBlock(doBody), methodID);
					doStatement.setBody((Statement) ASTNode.copySubtree(doStatement.getAST(), newDoBlock));
				}
			}

			result.add(doStatement);
		} else if (statement instanceof Block) {
			Block block = (Block) statement;
			Block newBlock = processBlock(block, methodID);
			result.add(newBlock);
		} else if (statement instanceof EnhancedForStatement) {

			EnhancedForStatement enhancedForStatement = (EnhancedForStatement) statement;

			int lineNumber = _cu.getLineNumber(enhancedForStatement.getExpression().getStartPosition());
			result.addAll(genInstrument(methodID, lineNumber));

			Statement enhancedBody = enhancedForStatement.getBody();
			if (enhancedBody != null) {

				startLine = _cu.getLineNumber(enhancedBody.getStartPosition());
				endLine = _cu.getLineNumber(enhancedBody.getStartPosition() + enhancedBody.getLength());
				if(containLine(startLine, endLine)) {
					Block newEnhancedBlock = processBlock(wrapBlock(enhancedBody), methodID);
					enhancedForStatement
							.setBody((Statement) ASTNode.copySubtree(enhancedForStatement.getAST(), newEnhancedBlock));
				}
			}

			result.add(enhancedForStatement);
		} else if (statement instanceof SwitchStatement) {

			SwitchStatement switchStatement = (SwitchStatement) statement;

			int lineNumber = _cu.getLineNumber(switchStatement.getExpression().getStartPosition());
			result.addAll(genInstrument(methodID, lineNumber));

			List<ASTNode> statements = new ArrayList<>();
			AST ast = AST.newAST(Constant.AST_LEVEL);
			for (Object object : switchStatement.statements()) {
				ASTNode astNode = (ASTNode) object;
				statements.add(ASTNode.copySubtree(ast, astNode));
			}

			switchStatement.statements().clear();

			for (ASTNode astNode : statements) {
				for (ASTNode node : process(astNode, methodID)) {
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
				if(containLine(startLine, endLine)){
					Block newTryBlock = processBlock(tryBlock, methodID);
					tryStatement.setBody((Block) ASTNode.copySubtree(tryStatement.getAST(), newTryBlock));
				}
			}

			List catchList = tryStatement.catchClauses();
			if (catchList != null) {
				for (Object object : catchList) {
					if (object instanceof CatchClause) {
						CatchClause catchClause = (CatchClause) object;
						Block catchBlock = catchClause.getBody();
						Block newCatchBlock = processBlock(catchBlock, methodID);
						catchClause.setBody((Block) ASTNode.copySubtree(catchClause.getAST(), newCatchBlock));
					}
				}
			}

			Block finallyBlock = tryStatement.getFinally();
			if (finallyBlock != null) {
				startLine = _cu.getLineNumber(finallyBlock.getStartPosition());
				endLine = _cu.getLineNumber(finallyBlock.getStartPosition() + finallyBlock.getLength());
				if(containLine(startLine, endLine)) {
					Block newFinallyBlock = processBlock(finallyBlock, methodID);
					tryStatement.setFinally((Block) ASTNode.copySubtree(tryStatement.getAST(), newFinallyBlock));
				}
			}

			result.add(tryStatement);
		} else {
			Statement copy = (Statement) ASTNode.copySubtree(AST.newAST(Constant.AST_LEVEL), statement);
			List<ASTNode> tmpInserted = genInstrument(methodID, startLine);
			
			if (statement instanceof ConstructorInvocation || statement instanceof SuperConstructorInvocation) {
				result.add(copy);
				result.addAll(tmpInserted);
			} else if (statement instanceof ContinueStatement || statement instanceof BreakStatement
					|| statement instanceof ReturnStatement || statement instanceof ThrowStatement
					|| statement instanceof AssertStatement || statement instanceof ExpressionStatement
					|| statement instanceof VariableDeclarationStatement) {
				result.addAll(tmpInserted);
				result.add(copy);

			} else if (statement instanceof LabeledStatement) {
				result.addAll(tmpInserted);
				result.add(copy);
			} else if (statement instanceof SynchronizedStatement) {
				result.addAll(tmpInserted);
				result.add(copy);
			} else {
				result.addAll(tmpInserted);
				result.add(copy);
			}
		}

		return result;
	}
	
	private List<ASTNode> genInstrument(String methodID, int line){
		List<ASTNode> result = new ArrayList<>();
		List<Pair<String, String>> preds = _condition.get(line);
		if (preds != null) {
			for(int count = 0; count < preds.size(); count ++){
				String condition = preds.get(count).getFirst();
				String prob = preds.get(count).getSecond();
				ASTNode inserted = _useSober ? GenStatement.newGenPredicateStatementForEvaluationBias(condition, methodID + "#" + line + "#" + condition + "#" + prob) :
					GenStatement.newGenPredicateStatement(condition, methodID + "#" + line + "#" + condition + "#" + prob);
				if(inserted != null){
					result.add(inserted);
				}
			}
			_condition.remove(line);
		}
		return result;
	}

	private Block processBlock(Block block, String methodID) {
		Block newBlock = AST.newAST(Constant.AST_LEVEL).newBlock();
		if (block == null) {
			return newBlock;
		}
		for (Object object : block.statements()) {
			ASTNode astNode = (ASTNode) object;
			List<ASTNode> newStatements = process(astNode, methodID);
			for (ASTNode newStatement : newStatements) {
				newBlock.statements().add(ASTNode.copySubtree(newBlock.getAST(), newStatement));
			}
		}
		return newBlock;
	}
	
	
	private Block wrapBlock(Statement statement){
		Block block = null;
		if(statement instanceof Block){
			block = (Block) statement;
		} else {
			AST ast = AST.newAST(Constant.AST_LEVEL);
			block = ast.newBlock();
			block.statements().add(ASTNode.copySubtree(block.getAST(), statement));
		}
		return block;
	}
	
	private boolean containLine(int startLine, int endLine){
		for(Integer line : _condition.keySet()){
			if (startLine <= line && line <= endLine) {
				return true;
			}
		}
		return false;
	}

	
}
