package locator.inst.gen;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TryStatement;

import locator.common.config.Constant;
import locator.common.java.JavaFile;

public class GenStatement {

	private static AST ast = AST.newAST(Constant.AST_LEVEL);

	/**
	 * generate "System.out.println()" statement
	 * 
	 * @param expression
	 * @return
	 */
	private static Statement genPrinter(Expression expression) {
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("System.out"));
		methodInvocation.setName(ast.newSimpleName("println"));
		methodInvocation.arguments().add(expression);
		ExpressionStatement expressionStatement = ast.newExpressionStatement(methodInvocation);
		return expressionStatement;
	}

	/**
	 * generate write file statement "auxiliary.Dumper.write()"
	 * 
	 * @param expression
	 * @return
	 */
	private static Statement genWriter(Expression expression) {
		// auxiliary.Dumper.write(expression);
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		// change slowWriter to writer Jun@2018-1-13 
//		methodInvocation.setName(ast.newSimpleName("slowWrite"));
		methodInvocation.setName(ast.newSimpleName("write"));
		methodInvocation.arguments().add(expression);
		ExpressionStatement expressionStatement = ast.newExpressionStatement(methodInvocation);
		return expressionStatement;
	}
	
	/**
	 * generate write file statement "auxiliary.Dumper.write(message)"
	 * 
	 * @param message
	 * @return
	 */
	public static Statement genWriter(String message) {
		StringLiteral literal = ast.newStringLiteral();
		literal.setLiteralValue(message);
		return genWriter(literal);
	}

	/**
	 * generate printer {@code Statement} with the arguments as given
	 * {@code locMessage} and {@code line}
	 * 
	 * @param locMessage
	 * @param line
	 * @return
	 */
	public static Statement genASTNode(String locMessage, int line) {
		StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(locMessage + "#" + line);
		return genWriter(stringLiteral);
	}

	/**
	 * generate predicate {@code IfStatement} and insert print line info
	 * 
	 * @param condition
	 *            : condition to be inserted
	 * @param message
	 *            : location information
	 * @param line
	 *            : line number
	 * @return a if statement with a line number printer
	 */
	@Deprecated
	public static IfStatement genPredicateStatement(String condition, String message, int line) {
		IfStatement ifStatement = ast.newIfStatement();
		Expression conditionExp = (Expression) JavaFile.genASTFromSource(condition, ASTParser.K_EXPRESSION);
		ifStatement.setExpression((Expression) ASTNode.copySubtree(ast, conditionExp));
		Statement statement = genASTNode(message, line);
		Block thenBlock = ast.newBlock();
		thenBlock.statements().add(statement);
		ifStatement.setThenStatement(thenBlock);
		return ifStatement;
	}
	
	public static Block genInstrumentStatementForTestWithReset(boolean succTest){
		Block block = ast.newBlock();
		
		// auxiliary.Dumper.SUCC_TEST = succTest;
		QualifiedName qName = ast.newQualifiedName(ast.newName("auxiliary.Dumper"), ast.newSimpleName("SUCC_TEST"));
		Assignment assignment = ast.newAssignment();
		assignment.setLeftHandSide(qName);
		assignment.setRightHandSide(ast.newBooleanLiteral(succTest));
		ExpressionStatement assignStatement = ast.newExpressionStatement(assignment);
		
		// auxiliary.Dumper.reset();
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("reset"));
		ExpressionStatement resetStatement = ast.newExpressionStatement(methodInvocation);
		
		
		// {
		//    auxiliary.Dumper.SUCC_TEST = succTest;
		//    auxiliary.Dumper.reset();
		// }
		block.statements().add(assignStatement);
		block.statements().add(resetStatement);
		
		return block;
	}
	
	public static Block genInstrumentStatementForTestWithResetTrueOrFalse(boolean succTest) {
		Block block = genInstrumentStatementForTestWithReset(succTest);
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("resetTrueOrFalse"));
		ExpressionStatement resetStatement = ast.newExpressionStatement(methodInvocation);
		block.statements().add(resetStatement);
		return block;
	}
	
	public static Block genInstrumentStatementForResultDump() {
		Block block = ast.newBlock();
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("dump"));
		ExpressionStatement dumpStatement = ast.newExpressionStatement(methodInvocation);
		block.statements().add(dumpStatement);
		return block;
	}
	
	public static Block genInstrumentStatementForResultDumpTrueOrFalse() {
		Block block = ast.newBlock();
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("dumpTrueOrFalse"));
		ExpressionStatement dumpStatement = ast.newExpressionStatement(methodInvocation);
		block.statements().add(dumpStatement);
		return block;
	}
	
	public static TryStatement newGenPredicateStatement(String condition, String message) {
		Expression conditionExp = null;
		// condition
		try{
			conditionExp = (Expression) JavaFile.genASTFromSource(condition, ASTParser.K_EXPRESSION);
		} catch (Exception e){
			return null;
		}
		
		// if(condition)
		IfStatement ifStatement = ast.newIfStatement();
		ifStatement.setExpression((Expression) ASTNode.copySubtree(ast, conditionExp));
		
		// auxiliary.Dumper.write(message);
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("write"));
		
		StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(message);
		methodInvocation.arguments().add(stringLiteral);
		ExpressionStatement invokeStatement = ast.newExpressionStatement(methodInvocation);
		
		// if(condition){
		//     auxiliary.Dumper.write(message);
		// }
		Block thenBlock = ast.newBlock();
		thenBlock.statements().add(invokeStatement);
		ifStatement.setThenStatement(thenBlock);
		
		// auxiliary.Dumper.observe(message);
		MethodInvocation methodInvocation2 = ast.newMethodInvocation();
		methodInvocation2.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation2.setName(ast.newSimpleName("observe"));
				
		StringLiteral stringLiteral2 = ast.newStringLiteral();
		stringLiteral2.setLiteralValue(message);
		methodInvocation2.arguments().add(stringLiteral2);
		ExpressionStatement invokeStatement2 = ast.newExpressionStatement(methodInvocation2);
		
		// try{
		//    auxiliary.Dumper.observe(message);
		//    if(condition){
		//        auxiliary.Dumper.write(message);
		//    }
		// }
		Block tryBody = ast.newBlock();
		tryBody.statements().add(invokeStatement2);
		tryBody.statements().add(ifStatement);
		TryStatement tryStatement = ast.newTryStatement();
		tryStatement.setBody(tryBody);
		
		// catch (Exception e){}
		CatchClause catchClause = ast.newCatchClause();
		SingleVariableDeclaration singleVariableDeclaration = ast.newSingleVariableDeclaration();
		singleVariableDeclaration.setName(ast.newSimpleName("fakeException"));
		singleVariableDeclaration.setType(ast.newSimpleType(ast.newSimpleName("Exception")));
		catchClause.setException(singleVariableDeclaration);
		catchClause.setBody(ast.newBlock());
		
		// try{
		//    if(condition){
		//        auxiliary.Dumper.write(message);
		//    }
		// }catch (Exception e){}
		tryStatement.catchClauses().add(catchClause);
		
		return tryStatement;
	}
	
	public static Block newGenPredicateStatementWithoutTry(String condition, String message) {
		Expression conditionExp = null;
		// condition
		try{
			conditionExp = (Expression) JavaFile.genASTFromSource(condition, ASTParser.K_EXPRESSION);
		} catch (Exception e){
			return null;
		}
		
		// if(condition)
		IfStatement ifStatement = ast.newIfStatement();
		ifStatement.setExpression((Expression) ASTNode.copySubtree(ast, conditionExp));
		
		// auxiliary.Dumper.write(message);
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("write"));
		
		StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(message);
		methodInvocation.arguments().add(stringLiteral);
		ExpressionStatement invokeStatement = ast.newExpressionStatement(methodInvocation);
		
		// if(condition){
		//     auxiliary.Dumper.write(message);
		// }
		Block thenBlock = ast.newBlock();
		thenBlock.statements().add(invokeStatement);
		ifStatement.setThenStatement(thenBlock);
		
		// auxiliary.Dumper.observe(message);
		MethodInvocation methodInvocation2 = ast.newMethodInvocation();
		methodInvocation2.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation2.setName(ast.newSimpleName("observe"));
				
		StringLiteral stringLiteral2 = ast.newStringLiteral();
		stringLiteral2.setLiteralValue(message);
		methodInvocation2.arguments().add(stringLiteral2);
		ExpressionStatement invokeStatement2 = ast.newExpressionStatement(methodInvocation2);
		
		// try{
		//    auxiliary.Dumper.observe(message);
		//    if(condition){
		//        auxiliary.Dumper.write(message);
		//    }
		// }
		Block body = ast.newBlock();
		body.statements().add(invokeStatement2);
		body.statements().add(ifStatement);
		return body;
	}

	public static TryStatement newGenPredicateStatementForEvaluationBias(String condition, String message) {
		Expression conditionExp = null;
		// condition
		try{
			conditionExp = (Expression) JavaFile.genASTFromSource(condition, ASTParser.K_EXPRESSION);
		} catch (Exception e){
			return null;
		}
		
		// if(condition)
		IfStatement ifStatement = ast.newIfStatement();
		ifStatement.setExpression((Expression) ASTNode.copySubtree(ast, conditionExp));
		
		// auxiliary.Dumper.writeTrue(message);
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("writeTrue"));
		
		StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(message);
		methodInvocation.arguments().add(stringLiteral);
		ExpressionStatement invokeStatement = ast.newExpressionStatement(methodInvocation);
		
		// if(condition){
		//     auxiliary.Dumper.writeTrue(message);
		// }
		Block thenBlock = ast.newBlock();
		thenBlock.statements().add(invokeStatement);
		ifStatement.setThenStatement(thenBlock);
		
		// auxiliary.Dumper.writeFalse(message);
		MethodInvocation methodInvocation2 = ast.newMethodInvocation();
		methodInvocation2.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation2.setName(ast.newSimpleName("writeFalse"));
		StringLiteral stringLiteral2 = ast.newStringLiteral();
		stringLiteral2.setLiteralValue(message);
		methodInvocation2.arguments().add(stringLiteral2);
		ExpressionStatement invokeStatement2 = ast.newExpressionStatement(methodInvocation2);
		
		// if(condition){
		//     auxiliary.Dumper.writeTrue(message);
		// } else {
		//     auxiliary.Dumper.writeFalse(message);
		// }
		Block elseBlock = ast.newBlock();
		elseBlock.statements().add(invokeStatement2);
		ifStatement.setElseStatement(elseBlock);
		
		// try{
		//     if(condition){
		//         auxiliary.Dumper.writeTrue(message);
		//     } else {
		//         auxiliary.Dumper.writeFalse(message);
		//     }
		// }
		Block tryBody = ast.newBlock();
		tryBody.statements().add(ifStatement);
		TryStatement tryStatement = ast.newTryStatement();
		tryStatement.setBody(tryBody);
		
		// catch (Exception e){}
		CatchClause catchClause = ast.newCatchClause();
		SingleVariableDeclaration singleVariableDeclaration = ast.newSingleVariableDeclaration();
		singleVariableDeclaration.setName(ast.newSimpleName("fakeException"));
		singleVariableDeclaration.setType(ast.newSimpleType(ast.newSimpleName("Exception")));
		catchClause.setException(singleVariableDeclaration);
		catchClause.setBody(ast.newBlock());
		
		// try{
		//     if(condition){
		//         auxiliary.Dumper.writeTrue(message);
		//     } else {
		//         auxiliary.Dumper.writeFalse(message);
		//     }
		// }catch (Exception e){}
		tryStatement.catchClauses().add(catchClause);
		
		return tryStatement;
	}
}
