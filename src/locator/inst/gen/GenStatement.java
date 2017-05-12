package locator.inst.gen;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.Message;

import locator.common.java.JavaFile;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;

public class GenStatement {

	private static AST ast = AST.newAST(AST.JLS8);
	
//	private static Statement genPrinter(Expression expression) {
//		MethodInvocation methodInvocation = ast.newMethodInvocation();
//		methodInvocation.setExpression(ast.newName("System.out"));
//		methodInvocation.setName(ast.newSimpleName("println"));
//		methodInvocation.arguments().add(expression);
//		ExpressionStatement expressionStatement = ast.newExpressionStatement(methodInvocation);
//		return expressionStatement;
//	}
	//replace the printer with a file writer
	private static Statement genPrinter(Expression expression) {
		//auxiliary.Dumper.write(expression);
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("write"));
		methodInvocation.arguments().add(expression);
		ExpressionStatement expressionStatement = ast.newExpressionStatement(methodInvocation);
		return expressionStatement;
	}

	/**
	 * generate "System.out.println()" {@code Statement} with the arguments as
	 * given {@code locMessage} and {@code line}
	 * 
	 * @param locMessage
	 * @param line
	 * @return
	 */
	public static Statement genASTNode(String locMessage, int line) {
		// MethodInvocation methodInvocation = ast.newMethodInvocation();
		// methodInvocation.setExpression(ast.newName("System.out"));
		// methodInvocation.setName(ast.newSimpleName("println"));
		StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(locMessage + "#" + line);
		return genPrinter(stringLiteral);
		// methodInvocation.arguments().add(stringLiteral);
		// ExpressionStatement expressionStatement =
		// ast.newExpressionStatement(methodInvocation);
		// return expressionStatement;
	}

	/**
	 * generate simple variable println {@code Statement}
	 * 
	 * @param message
	 * @param variable
	 * @return
	 */
	public static Statement genPrimitiveStatement(String prefix, String message, String variable) {
		// // System.out.println()
		// MethodInvocation methodInvocation = ast.newMethodInvocation();
		// methodInvocation.setExpression(ast.newName("System.out"));
		// methodInvocation.setName(ast.newSimpleName("println"));

		// "message variable value
		InfixExpression halfExpression = ast.newInfixExpression();
		StringLiteral leftString = ast.newStringLiteral();
		if(prefix == null){
			leftString.setLiteralValue(message + "#" + variable + "#");
		}else{
			leftString.setLiteralValue(message + "#" +prefix + "." + variable + "#");
		}
		halfExpression.setLeftOperand(leftString);
		halfExpression.setOperator(Operator.PLUS);
		Expression variableName = null;
		if (prefix != null) {
			if(prefix.equals("this")){
				ThisExpression expression = ast.newThisExpression();
				FieldAccess fieldAccess = ast.newFieldAccess();
				fieldAccess.setExpression(expression);
				fieldAccess.setName(ast.newSimpleName(variable));
				variableName = fieldAccess;
			}else {
				Name prefixName = ast.newName(prefix);
				SimpleName simpleName = ast.newSimpleName(variable);
				variableName = ast.newQualifiedName(prefixName, simpleName);
			}
		} else {
			variableName = ast.newSimpleName(variable);
		}
		halfExpression.setRightOperand(variableName);

		return genPrinter(halfExpression);

		// // "message|<variable:"+variable+">"
		// InfixExpression fullExpression = ast.newInfixExpression();
		// fullExpression.setLeftOperand(halfExpression);
		// fullExpression.setOperator(Operator.AND);
		// StringLiteral rightString = ast.newStringLiteral();
		// rightString.setLiteralValue(">");
		// fullExpression.setRightOperand(rightString);

		// methodInvocation.arguments().add(halfExpression);
		// ExpressionStatement expressionStatement =
		// ast.newExpressionStatement(methodInvocation);
		// return expressionStatement;
	}

	private static Statement genSingleMethodInvocationStatement(String prefix, String variable, String method, String message) {
		// // System.out.println()
		// MethodInvocation methodInvocation = ast.newMethodInvocation();
		// methodInvocation.setExpression(ast.newName("System.out"));
		// methodInvocation.setName(ast.newSimpleName("println"));

		// "message variable.method"
		InfixExpression infixExpression = ast.newInfixExpression();
		StringLiteral stringLiteral = ast.newStringLiteral();
		if(prefix != null){
			stringLiteral.setLiteralValue(message + "#" + prefix + "." + variable + "." + method + "#");
		} else{
			stringLiteral.setLiteralValue(message + "#" + variable + "." + method + "#");
		}

		// "message variable.method" +
		infixExpression.setLeftOperand(stringLiteral);
		infixExpression.setOperator(Operator.PLUS);

		// variable.method()
		MethodInvocation invocation = ast.newMethodInvocation();
		if(prefix == null){
			invocation.setExpression(ast.newSimpleName(variable));
		} else {
			Expression invocationExp = null;
			if(prefix.equals("this")){
				ThisExpression expression = ast.newThisExpression();
				FieldAccess fieldAccess = ast.newFieldAccess();
				fieldAccess.setExpression(expression);
				fieldAccess.setName(ast.newSimpleName(variable));
				invocationExp = fieldAccess;
			}else{
				Name prefixName = ast.newSimpleName(prefix);
				invocationExp = ast.newQualifiedName(prefixName, ast.newSimpleName(variable));
			}
			invocation.setExpression(invocationExp);
		}
		invocation.setName(ast.newSimpleName(method));

		// "message variable.method " + variable.method()
		infixExpression.setRightOperand(invocation);

		return genPrinter(infixExpression);

		// methodInvocation.arguments().add(infixExpression);
		// ExpressionStatement expressionStatement =
		// ast.newExpressionStatement(methodInvocation);
		// return expressionStatement;
	}

	public static Statement genMethodInvocationStatement(String prefix, String variable, String method, String message) {
		// if()
		IfStatement ifStatement = ast.newIfStatement();

		// variable == null
		InfixExpression nullCheckerExpression = ast.newInfixExpression();
		NullLiteral nullLiteral = ast.newNullLiteral();
		Expression lExpression = null;
		if(prefix == null){
			lExpression = ast.newSimpleName(variable);
		} else {
			if(prefix.equals("this")){
				ThisExpression thisExpression = ast.newThisExpression();
				FieldAccess fieldAccess = ast.newFieldAccess();
				fieldAccess.setExpression(thisExpression);
				fieldAccess.setName(ast.newSimpleName(variable));
				lExpression = fieldAccess;
			}else{
				lExpression = ast.newQualifiedName(ast.newName(prefix), ast.newSimpleName(variable));
			}
		}
		nullCheckerExpression.setLeftOperand(lExpression);
		nullCheckerExpression.setOperator(Operator.EQUALS);
		nullCheckerExpression.setRightOperand(nullLiteral);

		// if(variable == null)
		ifStatement.setExpression(nullCheckerExpression);

		// if(variable == null){ System.out.println("message
		// variable_null_method null"); }
		StringLiteral stringLiteral = ast.newStringLiteral();
		if(prefix == null){
//			stringLiteral.setLiteralValue(message + "#" + variable + "_null_" + method + "#null");
			stringLiteral.setLiteralValue(message + "#" + variable + "." + method + "#null");
		} else {
//			stringLiteral.setLiteralValue(message + "#" + prefix + "." + variable + "_null_" + method + "#null");
			stringLiteral.setLiteralValue(message + "#" + prefix + "." + variable + "." + method + "#null");
		}
		Statement printNullStatement = genPrinter(stringLiteral);
		Block thenBlock = ast.newBlock();
		thenBlock.statements().add(printNullStatement);
		ifStatement.setThenStatement(thenBlock);

		// if(variable == null)
		// { System.out.println("message variable_null_method null"); }
		// else 
		// { System.out.println("message variable.method " + variable.method); }
		Statement printMethodStatement = genSingleMethodInvocationStatement(prefix, variable, method, message);
		Block elseBlock = ast.newBlock();
		elseBlock.statements().add(printMethodStatement);
		ifStatement.setElseStatement(elseBlock);

		return ifStatement;
	}

	public static Statement genNullCheckerStatement(String prefix, String variable, String message) {
		// variable == null
		InfixExpression fullInfixExpression = ast.newInfixExpression();
		StringLiteral messageLiteral = ast.newStringLiteral();
		if(prefix == null){
			messageLiteral.setLiteralValue(message + "#" + variable + "==null#");
		} else {
			messageLiteral.setLiteralValue(message + "#" + prefix + "." + variable + "==null#");
		}
		fullInfixExpression.setLeftOperand(messageLiteral);
		fullInfixExpression.setOperator(Operator.PLUS);

		Expression lExpression = null;
		if(prefix == null){
			lExpression = ast.newSimpleName(variable);
		} else {
			if(prefix.equals("this")){
				ThisExpression thisExpression = ast.newThisExpression();
				FieldAccess fieldAccess = ast.newFieldAccess();
				fieldAccess.setExpression(thisExpression);
				fieldAccess.setName(ast.newSimpleName(variable));
				lExpression = fieldAccess;
			}else{
				lExpression = ast.newQualifiedName(ast.newName(prefix), ast.newSimpleName(variable));
			}
		}
		
		NullLiteral nullLiteral = ast.newNullLiteral();
		InfixExpression partialInfixExpression = ast.newInfixExpression();
		partialInfixExpression.setLeftOperand(lExpression);
		partialInfixExpression.setOperator(Operator.EQUALS);
		partialInfixExpression.setRightOperand(nullLiteral);

		MethodInvocation wrap = ast.newMethodInvocation();
		wrap.setExpression(ast.newSimpleName("String"));
		wrap.setName(ast.newSimpleName("valueOf"));
		wrap.arguments().add(partialInfixExpression);

		fullInfixExpression.setRightOperand(wrap);

		return genPrinter(fullInfixExpression);
	}
	
	public static Statement genThisFieldDumpMethodInvocation(String message){
		ThisExpression thisExpression = ast.newThisExpression();
		return genDumpMethodInvation(message, thisExpression);
		
	}
	
	public static Statement genVariableDumpMethodInvation(String message, String variableName){
		SimpleName simpleName = ast.newSimpleName(variableName);
		CastExpression castExpression = ast.newCastExpression();
		return genDumpMethodInvation(message, simpleName);
	}
	
	private static Statement genDumpMethodInvation(String message, Expression expression){
		//auxiliary.Dumper.dump(expression)
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("dump"));
		methodInvocation.arguments().add(expression);
		
		//message + "#" + auxliliary.Dumper.dump(expression)
		InfixExpression infixExpression = ast.newInfixExpression();
		StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(message + "#");
		
		infixExpression.setLeftOperand(stringLiteral);
		infixExpression.setOperator(InfixExpression.Operator.PLUS);
		infixExpression.setRightOperand(methodInvocation);
		
		return genPrinter(infixExpression);
	}
	
	public static Statement genReturnStatement(String varName){
		ReturnStatement returnStatement = ast.newReturnStatement();
		SimpleName simpleName = ast.newSimpleName(varName);
		returnStatement.setExpression(simpleName);
		return returnStatement;
	}
	
	public static Statement genThrowStatement(String varName){
		ThrowStatement throwStatement = ast.newThrowStatement();
		throwStatement.setExpression(ast.newSimpleName(varName));
		return throwStatement;
	}
	
	public static IfStatement genPredicateStatement(String condition, String message, int line){
		IfStatement ifStatement = ast.newIfStatement();
		Expression conditionExp = (Expression) JavaFile.genASTFromSource(condition, ASTParser.K_EXPRESSION);
		ifStatement.setExpression((Expression) ASTNode.copySubtree(ast, conditionExp));
		Statement statement = genASTNode(message, line);
		Block thenBlock = ast.newBlock();
		thenBlock.statements().add(statement);
		ifStatement.setThenStatement(thenBlock);
		return ifStatement;
	}

}
