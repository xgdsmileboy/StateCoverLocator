package locator.core.model.predict;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import locator.common.java.JavaFile;

public class PredicateFilter {

	/**
	 * pre-filter illegal predicate
	 * 
	 * @param condWithType
	 *            : predicted predicate that formatted like "$TYPE$ < 1", where TYPE
	 *            denotes the type of the variable in the original expression
	 * @param varName
	 *            : current variable to be used
	 * @param varType
	 *            : variable type of variable {@code varName}
	 * @return condition with replacing the $TYPE$ with {@code varName} if illegal,
	 *         or null
	 */
	public static String filter(String condWithType, String varName, String varType) {
		// match the $TYPE$
		String regex = "\\$([a-zA-Z_]+.*)\\$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(condWithType);
		String originalType = varType;
		if (matcher.find()) {
			originalType = matcher.group(1);
		}
		if (isBoolean(originalType) && !isBoolean(varType)) {
			return null;
		} else if (!isPrimitiveType(originalType) && isPrimitiveType(varType)) {
			return null;
		}
		String condition = condWithType.replaceAll(regex, varName);
		condition = condition.replace("this.", "");

		ASTNode expression = JavaFile.genASTFromSource(condition, ASTParser.K_EXPRESSION);
		String normalized = null;
		if (expression != null && !expression.toString().isEmpty()) {
			normalized = normalize(expression);
		}
		return normalized == null ? condition : normalized;

	}

	private static boolean isBoolean(String type) {
		if (type == null)
			return false;
		return "boolean".equals(type) || "Boolean".equals(type);
	}

	private static boolean isPrimitiveType(String type) {
		if (type == null)
			return false;
		switch (type) {
		case "int":
		case "char":
		case "short":
		case "long":
		case "float":
		case "double":
		case "boolean":
		case "byte":
			return true;
		default:
			return false;
		}
	}

	private static boolean isPrimitiveWrapper(String type) {
		if (type == null)
			return false;
		switch (type) {
		case "Integer":
		case "Long":
		case "Double":
		case "Float":
		case "Character":
		case "Short":
		case "Boolean":
		case "Byte":
			return true;
		default:
			return false;
		}
	}

	private static String normalize(ASTNode expression) {
		NumberVisitor visitor = new NumberVisitor();
		expression.accept(visitor);
		return simplify(expression);
	}
	
	private static String simplify(ASTNode expression) {
		if (expression instanceof PrefixExpression) {
			PrefixExpression prefixExpression = (PrefixExpression) expression;
			if ("!".equals(prefixExpression.getOperator().toString())) {
				return simplify(prefixExpression.getOperand());
			}
		} else if (expression instanceof ParenthesizedExpression) {
			ParenthesizedExpression pe = (ParenthesizedExpression) expression;
			return simplify(pe.getExpression());
		} else if (expression instanceof InfixExpression) {
			InfixExpression ie = (InfixExpression) expression;
			String operator = ie.getOperator().toString();
			switch (operator) {
			// case ">=":
			// case ">":
			// case "==":
			// case "&&": break;
			case "<":
				operator = ">=";
				break;
			case "<=":
				operator = ">";
				break;
			case "!=":
				operator = "==";
				break;
			case "||":
				operator = "&&";
				break;
			default:
				break;
			}
			return ie.getLeftOperand().toString() + operator + ie.getRightOperand().toString();
		}
		return expression.toString();
	}

	private static class NumberVisitor extends ASTVisitor {
		public boolean visit(NumberLiteral literal) {
			Pattern pattern = Pattern.compile("[0-9]+(\\.0+)?");
			String value = literal.getToken();
			if (pattern.matcher(value).matches()) {
				int index = value.indexOf('.');
				if (index > 0) {
					value = value.substring(0, index);
					literal.setToken(value);
				}
			}
			return true;
		}
	}
}
