package locator.inst.gen;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;

import locator.common.java.JavaFile;

public class GenStatement {

	private static AST ast = AST.newAST(AST.JLS8);

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
		methodInvocation.setName(ast.newSimpleName("write"));
		methodInvocation.arguments().add(expression);
		ExpressionStatement expressionStatement = ast.newExpressionStatement(methodInvocation);
		return expressionStatement;
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

}
