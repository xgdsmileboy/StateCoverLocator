package locator.inst.visitor.feature;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		String regex = "\\$([a-zA-Z_][a-zA-Z_1-9]*)\\$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(condWithType);
		String originalType = varType;
		if (matcher.find()) {
			originalType = matcher.group(1);
		}
		String condition = condWithType.replaceAll(regex, varName);

		return condition;
		
	}
	
	
	private static boolean isPrimitiveType(String type) {
		if(type == null) return false;
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
		if(type == null) return false;
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
}
