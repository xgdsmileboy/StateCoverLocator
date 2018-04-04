package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
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
import locator.common.config.Constant.PredicateStatement;
import locator.common.config.Identifier;
import locator.common.java.Pair;
import locator.inst.gen.GenName;
import locator.inst.gen.GenStatement;

public class MethodEnterExitPredicateVisitor extends TraversalVisitor{
	
	private final static String __name__ = "@NoSideEffectPredicateInstrumentVisitor ";
	
	private Map<Integer, List<Pair<String, String>>> _condition = null;
	
	private Type _retType;
	
	private static AST ast = AST.newAST(Constant.AST_LEVEL);
	
	public MethodEnterExitPredicateVisitor() {
		_condition = new HashMap<>();
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
		
		List<ASTNode> variablePredicatesEnter = genVariablePredicateInstrument(methodID, startLine, endLine, true);
		
		List<ASTNode> blockStatement = new ArrayList<>();

		for (int i = 0; i < methodBody.statements().size(); i++) {
			ASTNode astNode = (ASTNode) methodBody.statements().get(i);
			blockStatement.addAll(process(astNode, methodID));
		}

		methodBody.statements().clear();
		for(ASTNode statement : variablePredicatesEnter) {
			methodBody.statements().add(ASTNode.copySubtree(methodBody.getAST(), statement));
		}
		
		TryStatement tryStatement = ast.newTryStatement();
		Block tryBody = ast.newBlock();
		for (ASTNode statement : blockStatement) {
			tryBody.statements().add(ASTNode.copySubtree(tryBody.getAST(), statement));
		}
		tryStatement.setBody((Block) ASTNode.copySubtree(tryStatement.getAST(), tryBody));
		Block finallyBlock = ast.newBlock();
		List<ASTNode> variablePredicatesExit = genVariablePredicateInstrument(methodID, startLine, endLine, false);
		for(ASTNode statement : variablePredicatesExit) {
			finallyBlock.statements().add(ASTNode.copySubtree(finallyBlock.getAST(), statement));
		}
		tryStatement.setFinally((Block) ASTNode.copySubtree(tryStatement.getAST(), finallyBlock));
		
		methodBody.statements().add(ASTNode.copySubtree(methodBody.getAST(), tryStatement));

		return true;
	}
	
	private List<ASTNode> process(ASTNode statement, String methodID) {

		List<ASTNode> result = new ArrayList<>();

		int startLine = _cu.getLineNumber(statement.getStartPosition());
		int endLine = _cu.getLineNumber(statement.getStartPosition() + statement.getLength());
		
		if(!containLine(startLine, endLine)){
			// fix: add initializer for variable declaration
			result.add(addInitializer(statement));
//			result.add(statement);
			return result;
		}

		if (statement instanceof IfStatement) {

			IfStatement ifStatement = (IfStatement) statement;
			startLine = _cu.getLineNumber(ifStatement.getExpression().getStartPosition());

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
			whileStatement.setBody((Statement) ASTNode.copySubtree(whileStatement.getAST(), bodyBlock));
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
			
			forStatement.setBody((Statement) ASTNode.copySubtree(forStatement.getAST(), bodyBlock));
			result.add(forStatement);
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
			doStatement.setBody((Statement) ASTNode.copySubtree(doStatement.getAST(), bodyBlock));
			result.add(doStatement);
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
		} else if (statement instanceof ReturnStatement) {
			ReturnStatement returnStatement = (ReturnStatement) statement;
			if (returnStatement.getExpression() == null || _condition.get(startLine) == null) {
				result.add(returnStatement);
			} else {
				result.add(genReturnStatementInstrument(returnStatement, methodID, startLine));
			}
		} else {
			// fix 2018-3-6: add initializer for variable declaration
			result.add(addInitializer(statement));
//			result.add(statement);
		}

		return result;
	}
	
	private ASTNode addInitializer(ASTNode stmt) {
		if(stmt instanceof VariableDeclarationStatement) {
			VariableDeclarationStatement vds = (VariableDeclarationStatement) stmt;
			// fix bug : skip variables with "final" modifier
			if(!Modifier.isFinal(vds.getModifiers())) {
				Expression expression = genDefaultValue(vds.getType());
				if (expression != null) {
					for(Object object : vds.fragments()) {
						if (object instanceof VariableDeclarationFragment) {
							VariableDeclarationFragment vdf = (VariableDeclarationFragment) object;
							if(vdf.getInitializer() == null) {
								vdf.setInitializer((Expression) ASTNode.copySubtree(vdf.getAST(), expression));
							}
						}
					}
				}
			}
		}
		return stmt;
	}
	
	private Expression genDefaultValue(Type type) {
		Expression expression = null;
		if(type.isPrimitiveType()) {
			switch(type.toString()) {
			case "byte":
			case "short":
			case "int":
			case "long": expression = ast.newNumberLiteral("0"); break;
			case "char": expression = ast.newCharacterLiteral(); break;
			case "float": expression = ast.newNumberLiteral("0.0f"); break;
			case "double": expression = ast.newNumberLiteral("0.0"); break;
			case "boolean": expression = ast.newBooleanLiteral(false); break;
			case "void":
			}
		} else {
			expression = ast.newNullLiteral();
		}
		return expression;
	}
	
	private List<ASTNode> genVariablePredicateInstrument(String methodID, int startLine, int endLine, boolean isEnter) {
		List<Pair<String, String>> preds = _condition.get(startLine);
		List<ASTNode> result = new ArrayList<ASTNode>();
		if (preds == null) {
			return result;
		}
		int logLine = isEnter ? startLine : endLine;
		for(Pair<String, String> predicate : preds) {
			int pos = predicate.getFirst().indexOf("#");
			String p = predicate.getFirst().substring(0, pos);
			ASTNode inserted = GenStatement.newGenPredicateStatementWithoutTry(p, methodID + "#" + logLine + "#" + p + "#1");
			if(inserted != null){
				result.add(inserted);
			}
		}
		return result;
	}
	
	private void addPredicates(Block block, String tempVarName, String methodID, int line, String originalExpr) {
		// fix a bug: should not instrument predicates for constant boolean values
		// it may cause compilation error
		// public type func(){ for(;true;) { if(...) return;  } }
		// => public type func(){ for(;log(true);) { if(...) return;  } }
		// cause compilation error for missing "return" statement.
		if(originalExpr.isEmpty() || "true".equals(originalExpr) || "false".equals(originalExpr)) {
			return;
		}
		List<ASTNode> predicates = genPredicateInstrument(methodID, tempVarName, line, originalExpr);
		for(ASTNode predicate : predicates) {
			block.statements().add(ASTNode.copySubtree(block.getAST(), predicate));
		}
	}
	
	private Block genReturnStatementInstrument(ReturnStatement node, String methodID, int line) {
		Block  block = ast.newBlock();
		String tempVarName = GenName.genVariableName(line);
		String originalExpr = node.getExpression().toString();
		ASTNode assign = genDeclarationAssignment(node.getExpression(), tempVarName);
		if (assign == null) {
			block.statements().add(ASTNode.copySubtree(block.getAST(), node));
			return block;
		}
		node.setExpression((Expression) node.copySubtree(node.getAST(), ast.newSimpleName(tempVarName)));
		block.statements().add(ASTNode.copySubtree(block.getAST(), assign));
		addPredicates(block, tempVarName, methodID, line, originalExpr);
		block.statements().add(ASTNode.copySubtree(block.getAST(), node));
		return block;
	}
	
	private ExpressionStatement genDeclarationAssignment(Expression expr, String tempVarName) {
		ITypeBinding type = expr.resolveTypeBinding();
		Assignment assign = ast.newAssignment();
		assign.setOperator(Assignment.Operator.ASSIGN);
		VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();
		vdf.setName(ast.newSimpleName(tempVarName));
		VariableDeclarationExpression vde = ast.newVariableDeclarationExpression(vdf);
		Type varType = getDeclarationType(type);
		if (varType == null) {
			return null;
		}
		vde.setType((Type) ASTNode.copySubtree(vde.getAST(), varType));
		assign.setLeftHandSide(vde);
		assign.setRightHandSide((Expression) assign.copySubtree(assign.getAST(), expr));
		ExpressionStatement exprStatement = ast.newExpressionStatement(assign);
		return exprStatement;
	}
	
	private Type getDeclarationType(ITypeBinding type) { // return only
		if (type == null) {
			if(_retType.isPrimitiveType() || isPrimitiveWrapper(_retType)) {
				return _retType;
			} else {
				return null;
			}
		} else {
			if (type.isPrimitive()) {
				return ast.newPrimitiveType(ITypeBinding2PrimitiveTypeCode(type));
			}
		}
		return null;
	}
	
	private boolean isPrimitiveWrapper(Type type) {
		if(type == null) {
			return false;
		}
		switch(type.toString()) {
		case "Integer":
		case "Short":
		case "Character":
		case "Boolean":
		case "Long":
		case "Float":
		case "Double":
		case "Byte":
			return true;
		default:
		}
		return false;
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
	
	private List<ASTNode> genPredicateInstrument(String methodID, String tempVarName, int line, String originalExpr){
		List<ASTNode> result = new ArrayList<>();
		if (_condition.get(line) != null) {
			List<Pair<String, String>> preds = _condition.get(line);
			for(Pair<String, String> pred: preds) {
				if (pred.getFirst().endsWith("#VAR")) {
					String p = pred.getFirst().substring(0, pred.getFirst().length() - 4);
					ASTNode inserted = GenStatement.newGenPredicateStatementWithoutTry(p, methodID + "#" + line + "#" + p + "#1");
					if(inserted != null){
						result.add(inserted);
					}
				}
			}
			
			List<Pair<String, String>> predicates = getPredicateForReturns(tempVarName, originalExpr);
			for(Pair<String, String> predicate : predicates) {
				ASTNode inserted = GenStatement.newGenPredicateStatementWithoutTry(predicate.getFirst(), methodID + "#" + line + "#" + predicate.getSecond() + "#1");
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
		final String operators[] = { "< 0", "<= 0", "> 0", ">= 0", "== 0",
				"!= 0" };
		for (final String op : operators) {
			predicates.add(new Pair<String, String>(expr + op, "(" + originalExpr + ")" + op));
		}
		return predicates;
	}
}
 