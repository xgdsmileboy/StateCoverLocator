package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.Pair;
import locator.common.util.LevelLogger;
import locator.inst.gen.GenName;
import locator.inst.gen.GenStatement;
import polyglot.ast.For;

public class NoSideEffectPredicateInstrumentVisitor extends TraversalVisitor{
	
	private final static String __name__ = "@NoSideEffectPredicateInstrumentVisitor ";
	
	private Map<Integer, List<Pair<String, String>>> _condition = null;
	
	private boolean _useSober = false;
	
	private Type _retType;
	
	private static AST ast = AST.newAST(Constant.AST_LEVEL);
	
	private enum PredicateStatement {
		IF, WHILE, DO, RETURN, FOR, ASSIGN, SWITCH
	};
	
	public NoSideEffectPredicateInstrumentVisitor(boolean useSober) {
		_condition = new HashMap<>();
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

		_retType = null;
		if (node.getReturnType2() != null) {
			_retType = node.getReturnType2();
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
			
			Block block = genIfStatementInstrument(ifStatement, methodID, startLine);

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
			
			block.statements().add(ASTNode.copySubtree(block.getAST(), ifStatement));
			result.add(block);
		} else if (statement instanceof WhileStatement) {
			
			WhileStatement whileStatement = (WhileStatement) statement;
			int lineNumber = _cu.getLineNumber(whileStatement.getExpression().getStartPosition());
			
			Statement whilebody = whileStatement.getBody();
			Block bodyBlock = null;
			
			if (whilebody != null) {
				startLine = _cu.getLineNumber(whilebody.getStartPosition());
				endLine = _cu.getLineNumber(whilebody.getStartPosition() + whilebody.getLength());
				if(containLine(startLine, endLine)){
					bodyBlock = processBlock(wrapBlock(whilebody), methodID);
				} else {
					bodyBlock = wrapBlock(whilebody);
				}
			} else {
				bodyBlock = ast.newBlock();
			}
			if (whileStatement.getExpression().toString().equals("true")) {
				whileStatement.setBody((Statement) ASTNode.copySubtree(whileStatement.getAST(), bodyBlock));
				result.add(whileStatement);
			} else {
				result.add(genWhileStatementInstrument(whileStatement, bodyBlock, methodID, lineNumber));
			}
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

			Statement forBody = forStatement.getBody();

			Block bodyBlock = null;
			if (forBody != null) {
				startLine = _cu.getLineNumber(forBody.getStartPosition());
				endLine = _cu.getLineNumber(forBody.getStartPosition() + forBody.getLength());
				if(containLine(startLine, endLine)) {
					bodyBlock = processBlock(wrapBlock(forBody), methodID);
				} else {
					bodyBlock = wrapBlock(forBody);
				}
			} else {
				bodyBlock = ast.newBlock();
			}
			
			if(lineNumber != -1){
				result.add(genForStatementInstrument(forStatement, bodyBlock, methodID, lineNumber));
			} else {
				forStatement.setBody((Statement) ASTNode.copySubtree(forStatement.getAST(), bodyBlock));
				result.add(forStatement);
			}
		} else if (statement instanceof DoStatement) {

			DoStatement doStatement = (DoStatement) statement;

			int lineNumber = _cu.getLineNumber(doStatement.getExpression().getStartPosition());
			
			Statement doBody = doStatement.getBody();
			Block bodyBlock = null;
			if (doBody != null) {
				startLine = _cu.getLineNumber(doBody.getStartPosition());
				endLine = _cu.getLineNumber(doBody.getStartPosition() + doBody.getLength());
				if(containLine(startLine, endLine)) {
					bodyBlock = processBlock(wrapBlock(doBody), methodID);
				} else {
					bodyBlock = wrapBlock(doBody);
				}
			} else {
				bodyBlock = ast.newBlock();
			}
			result.add(genDoStatementInstrument(doStatement, bodyBlock, methodID, lineNumber));
		} else if (statement instanceof Block) {
			Block block = (Block) statement;
			Block newBlock = processBlock(block, methodID);
			result.add(newBlock);
		} else if (statement instanceof EnhancedForStatement) {

			EnhancedForStatement enhancedForStatement = (EnhancedForStatement) statement;

//			int lineNumber = _cu.getLineNumber(enhancedForStatement.getExpression().getStartPosition());
//			result.addAll(genInstrument(methodID, lineNumber));

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
			Block block = genSwitchStatementInstrument(switchStatement, methodID, lineNumber);

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
			
			block.statements().add(ASTNode.copySubtree(block.getAST(), switchStatement));
			result.add(block);
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
		} else if (statement instanceof ReturnStatement) {
			ReturnStatement returnStatement = (ReturnStatement) statement;
			if (returnStatement.getExpression() == null || _condition.get(startLine) == null) {
				result.add(returnStatement);
			} else {
				result.add(genReturnStatementInstrument(returnStatement, methodID, startLine));
			}
		} else if (statement instanceof ExpressionStatement) {
			ExpressionStatement exprStatement = (ExpressionStatement) statement;
			Expression expr = exprStatement.getExpression();
			if (expr instanceof Assignment) {
				Block block = ast.newBlock();
				block.statements().add(ASTNode.copySubtree(block.getAST(), exprStatement));
				addPredicates(block, "", methodID, startLine, PredicateStatement.ASSIGN, "");
				result.add(block);
			} else {
				result.add(exprStatement);
			}
		} else {
			result.add(statement);
		}

		return result;
	}
	
	private void addPredicates(Block block, String tempVarName, String methodID, int line, PredicateStatement psType, String originalExpr) {
		List<ASTNode> predicates = genPredicateInstrument(methodID, tempVarName, line, psType, originalExpr);
		for(ASTNode predicate : predicates) {
			block.statements().add(ASTNode.copySubtree(block.getAST(), predicate));
		}
	}
	
	private Block genIfStatementInstrument(IfStatement node, String methodID, int line) {
		Block  block = ast.newBlock();
		String tempVarName = GenName.genVariableName(line);
		String originalExpr = node.getExpression().toString();
		ASTNode assign = genDeclarationAssignment(node.getExpression(), tempVarName, PredicateStatement.IF);
		if (assign == null) {
			block.statements().add(ASTNode.copySubtree(block.getAST(), node));
			return block;
		}
		node.setExpression((Expression) node.copySubtree(node.getAST(), ast.newSimpleName(tempVarName)));
		block.statements().add(assign);
		addPredicates(block, tempVarName, methodID, line, PredicateStatement.IF, originalExpr);
		return block;
	}
	
	private Block genForStatementInstrument(ForStatement node, Block body, String methodID, int line) {
		Block block = ast.newBlock();
		String tempVarName = GenName.genVariableName(line);
		String originalExpr = node.getExpression().toString();
		ASTNode dclAssign = genDeclarationAssignment(node.getExpression(), tempVarName, PredicateStatement.FOR);
		if (dclAssign == null) {
			node.setBody((Statement) ASTNode.copySubtree(node.getAST(), body));
			block.statements().add(ASTNode.copySubtree(block.getAST(), node));
			return block;
		}
		
		Block bodyBlock = ast.newBlock();
		bodyBlock.statements().add(ASTNode.copySubtree(bodyBlock.getAST(), dclAssign));
		addPredicates(bodyBlock, tempVarName, methodID, line, PredicateStatement.FOR, originalExpr);
		IfStatement ifStatement = ast.newIfStatement();
		ifStatement.setExpression((Expression) ASTNode.copySubtree(ifStatement.getAST(), ast.newSimpleName(tempVarName)));
		ifStatement.setThenStatement((Statement) ASTNode.copySubtree(ifStatement.getAST(), body));
		ifStatement.setElseStatement((Statement) ASTNode.copySubtree(ifStatement.getAST(), ast.newBreakStatement()));
		bodyBlock.statements().add(ASTNode.copySubtree(bodyBlock.getAST(), ifStatement));
//		ASTNode assign = genAssignment(node.getExpression(), tempVarName);
//		block.statements().add(ASTNode.copySubtree(block.getAST(), dclAssign));
//		addPredicates(block, tempVarName, methodID, line, PredicateStatement.FOR, originalExpr);
//		node.setExpression((Expression) ASTNode.copySubtree(node.getAST(), ast.newSimpleName(tempVarName)));
						
//		body.statements().add(ASTNode.copySubtree(body.getAST(), assign));
//		addPredicates(body, tempVarName, methodID, line, PredicateStatement.FOR, originalExpr);
		node.setBody((Statement) ASTNode.copySubtree(node.getAST(), bodyBlock));
		block.statements().add(ASTNode.copySubtree(block.getAST(), node));
		return block;
	}
	
	private Block genWhileStatementInstrument(WhileStatement node, Block body, String methodID, int line) {
		Block block = ast.newBlock();
		String tempVarName = GenName.genVariableName(line);
		String originalExpr = node.getExpression().toString();
		ASTNode dclAssign = genDeclarationAssignment(node.getExpression(), tempVarName, PredicateStatement.WHILE);
		if (dclAssign == null) {
			node.setBody((Statement) ASTNode.copySubtree(node.getAST(), body));
			block.statements().add(ASTNode.copySubtree(block.getAST(), node));
			return block;
		}
		ASTNode assign = genAssignment(node.getExpression(), tempVarName);
		block.statements().add(ASTNode.copySubtree(block.getAST(), dclAssign));
		addPredicates(block, tempVarName, methodID, line, PredicateStatement.WHILE, originalExpr);
		node.setExpression((Expression) ASTNode.copySubtree(node.getAST(), ast.newSimpleName(tempVarName)));
						
		body.statements().add(ASTNode.copySubtree(body.getAST(), assign));
		addPredicates(body, tempVarName, methodID, line, PredicateStatement.WHILE, originalExpr);
		node.setBody((Statement) ASTNode.copySubtree(node.getAST(), body));
		block.statements().add(ASTNode.copySubtree(block.getAST(), node));
		return block;
	}
	
	private Block genDoStatementInstrument(DoStatement node, Block body, String methodID, int line) {
		Block block = ast.newBlock();
		String tempVarName = GenName.genVariableName(line);
		String originalExpr = node.getExpression().toString();
		ASTNode dclAssign = genDeclarationAssignmentForDoStatement(tempVarName);
		if (dclAssign == null) {
			node.setBody((Statement) ASTNode.copySubtree(node.getAST(), body));
			block.statements().add(ASTNode.copySubtree(block.getAST(), node));
			return block;
		}
		ASTNode assign = genAssignment(node.getExpression(), tempVarName);
		block.statements().add(ASTNode.copySubtree(block.getAST(), dclAssign));
		node.setExpression((Expression) ASTNode.copySubtree(node.getAST(), ast.newSimpleName(tempVarName)));
						
		body.statements().add(ASTNode.copySubtree(body.getAST(), assign));
		addPredicates(body, tempVarName, methodID, line, PredicateStatement.DO, originalExpr);
		node.setBody((Statement) ASTNode.copySubtree(node.getAST(), body));
		block.statements().add(ASTNode.copySubtree(block.getAST(), node));
		return block;
	}
	
	private Block genSwitchStatementInstrument(SwitchStatement node, String methodID, int line) {
		Block  block = ast.newBlock();
		String tempVarName = GenName.genVariableName(line);
		String originalExpr = node.getExpression().toString();
		ASTNode assign = genDeclarationAssignment(node.getExpression(), tempVarName, PredicateStatement.SWITCH);
		if (assign == null) {
//			block.statements().add(ASTNode.copySubtree(block.getAST(), node));
			return block;
		}
		node.setExpression((Expression) node.copySubtree(node.getAST(), ast.newSimpleName(tempVarName)));
		block.statements().add(assign);
		addPredicates(block, tempVarName, methodID, line, PredicateStatement.SWITCH, originalExpr);
		return block;
	}
	
	private Block genReturnStatementInstrument(ReturnStatement node, String methodID, int line) {
		Block  block = ast.newBlock();
		String tempVarName = GenName.genVariableName(line);
		String originalExpr = node.getExpression().toString();
		ASTNode assign = genDeclarationAssignment(node.getExpression(), tempVarName, PredicateStatement.RETURN);
		if (assign == null) {
			block.statements().add(ASTNode.copySubtree(block.getAST(), node));
			return block;
		}
		node.setExpression((Expression) node.copySubtree(node.getAST(), ast.newSimpleName(tempVarName)));
		block.statements().add(ASTNode.copySubtree(block.getAST(), assign));
		addPredicates(block, tempVarName, methodID, line, PredicateStatement.RETURN, originalExpr);
		block.statements().add(ASTNode.copySubtree(block.getAST(), node));
		return block;
	}
	
	private ExpressionStatement genAssignment(Expression expr, String tempVarName) {
		Assignment assign = ast.newAssignment();
		assign.setOperator(Assignment.Operator.ASSIGN);
		Name leftVar = ast.newSimpleName(tempVarName);
		assign.setLeftHandSide((Expression) ASTNode.copySubtree(assign.getAST(), leftVar));
		assign.setRightHandSide((Expression) ASTNode.copySubtree(assign.getAST(), expr));
		ExpressionStatement exprStatement = ast.newExpressionStatement(assign);
		return exprStatement;
	}
	
//	private VariableDeclarationStatement genDeclaration(Expression expr, String tempVarName, PredicateStatement psType) {
//		ITypeBinding type = expr.resolveTypeBinding();
//		VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();
//		vdf.setName(ast.newSimpleName(tempVarName));
//		VariableDeclarationStatement vds = ast.newVariableDeclarationStatement(vdf);
//		Type varType = getDeclarationType(type, psType);
//		if (varType == null) {
//			return null;
//		}r
//		vds.setType((Type) ASTNode.copySubtree(vds.getAST(), varType));
//		return vds;
//	}
	
	private ExpressionStatement genDeclarationAssignmentForDoStatement(String tempVarName) {
		Assignment assign = ast.newAssignment();
		assign.setOperator(Assignment.Operator.ASSIGN);
		VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();
		vdf.setName(ast.newSimpleName(tempVarName));
		VariableDeclarationExpression vde = ast.newVariableDeclarationExpression(vdf);
		vde.setType((Type) ASTNode.copySubtree(vde.getAST(), ast.newPrimitiveType(PrimitiveType.BOOLEAN)));
		assign.setLeftHandSide(vde);
		assign.setRightHandSide((Expression) assign.copySubtree(assign.getAST(), ast.newBooleanLiteral(true)));
		ExpressionStatement exprStatement = ast.newExpressionStatement(assign);
		return exprStatement;
	}
	
	private ExpressionStatement genDeclarationAssignment(Expression expr, String tempVarName, PredicateStatement psType) {
		ITypeBinding type = expr.resolveTypeBinding();
		Assignment assign = ast.newAssignment();
		assign.setOperator(Assignment.Operator.ASSIGN);
		VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();
		vdf.setName(ast.newSimpleName(tempVarName));
		VariableDeclarationExpression vde = ast.newVariableDeclarationExpression(vdf);
		Type varType = getDeclarationType(type, psType);
		if (varType == null) {
			return null;
		}
		vde.setType((Type) ASTNode.copySubtree(vde.getAST(), varType));
		assign.setLeftHandSide(vde);
		assign.setRightHandSide((Expression) assign.copySubtree(assign.getAST(), expr));
		ExpressionStatement exprStatement = ast.newExpressionStatement(assign);
		return exprStatement;
	}
	
	private Type getDeclarationType(ITypeBinding type, PredicateStatement psType) {
		PrimitiveType.Code code = null;
		switch (psType) {
		case IF:
		case WHILE:
		case DO:
		case FOR:
			code = PrimitiveType.BOOLEAN;
			break;
		case RETURN:
			if (type == null) {
				break;
			}
		case SWITCH:
		case ASSIGN:
			code = ITypeBinding2PrimitiveTypeCode(type);
		}
		if (code == null && type != null) {
			LevelLogger.error(__name__ + "Unhandled type: " + type.getName());
			return null;
		}
		if (type == null && psType == PredicateStatement.RETURN) {
			return _retType;
		} else {
			return ast.newPrimitiveType(code);
		}
	}
	
	private PrimitiveType.Code ITypeBinding2PrimitiveTypeCode(ITypeBinding type) {
		if (!type.isPrimitive()) {
			return null;
		}
		switch(type.toString()) {
		case "int":
			return PrimitiveType.INT;
		case "short":
			return PrimitiveType.SHORT;
		case "char":
			return PrimitiveType.CHAR;
		case "boolean":
			return PrimitiveType.BOOLEAN;
		case "long":
			return PrimitiveType.LONG;
		case "float":
			return PrimitiveType.FLOAT;
		case "double":
			return PrimitiveType.DOUBLE;
		case "byte":
			return PrimitiveType.BYTE;
		case "void":
			return PrimitiveType.VOID;
		}
		return null;
	}
	
	private List<ASTNode> genPredicateInstrument(String methodID, String tempVarName, int line, PredicateStatement psType, String originalExpr){
		List<ASTNode> result = new ArrayList<>();
		if (_condition.get(line) != null) {
			List<Pair<String, String>> predicates = null;
			switch (psType) {
				case IF:
				case WHILE:
				case DO:
				case FOR:
				case SWITCH:
					predicates = getPredicateForConditions(tempVarName, originalExpr);
					break;
				case RETURN:
					predicates = getPredicateForReturns(tempVarName, originalExpr);
					break;
				case ASSIGN:
					List<Pair<String, String>> preds = _condition.get(line);
					predicates = new ArrayList<Pair<String, String>>();
					for(Pair<String, String> p : preds) {
						predicates.add(new Pair<String, String>(p.getFirst(), p.getFirst()));
					}
					break;
			}
			for(Pair<String, String> predicate : predicates) {
				ASTNode inserted = _useSober ? GenStatement.newGenPredicateStatementForEvaluationBias(predicate.getFirst(), methodID + "#" + line + "#" + predicate.getSecond() + "#1") :
					GenStatement.newGenPredicateStatementWithoutTry(predicate.getFirst(), methodID + "#" + line + "#" + predicate.getSecond() + "#1");
				if(inserted != null){
					result.add(inserted);
				}
			}
//			_condition.remove(line);
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

	private List<Pair<String, String>> getPredicateForReturns(final String expr, final String originalExpr) {
		List<Pair<String, String>> predicates = new ArrayList<Pair<String, String>>();
		final String operators[] = { " < 0", " <= 0", " > 0", " >= 0", " == 0",
				" != 0" };
		for (final String op : operators) {
			predicates.add(new Pair<String, String>(expr + op, "(" + originalExpr + ")" + op));
		}
		return predicates;
	}
	
	private List<Pair<String, String>> getPredicateForConditions(final String expr, final String originalExpr) {
		List<Pair<String, String>> predicates = new ArrayList<Pair<String, String>>();
		predicates.add(new Pair<String, String>(expr, originalExpr));
		predicates.add(new Pair<String, String>("!" + expr, "!(" + originalExpr + ")"));
		return predicates;
	}
}
 