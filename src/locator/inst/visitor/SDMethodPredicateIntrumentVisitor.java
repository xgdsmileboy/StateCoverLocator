package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.Type;

import locator.aux.extractor.CodeAnalyzer;
import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.util.Pair;

public class SDMethodPredicateIntrumentVisitor extends NoSideEffectPreidcateInstrumentVisitor {
	private Set<Integer> _lines = null;
	private List<Pair<String, String>> _leftVars = new ArrayList<Pair<String, String>>();
	private String _srcPath = "";
	private String _relJavaPath = "";
	private boolean _useSober;
	private String _methodID = "";
	private Map<Integer, List<Pair<String, String>>> _predicates = new HashMap<>();
	private static AST ast = AST.newAST(Constant.AST_LEVEL);

	public SDMethodPredicateIntrumentVisitor(boolean useSober) {
		_useSober = useSober;
	}

	public void initOneRun(Set<Integer> lines, String srcPath, String relJavaPath) {
		_lines = lines;
		_srcPath = srcPath;
		_relJavaPath = relJavaPath;
		clear();
	}

	public Map<Integer, List<Pair<String, String>>> getPredicates() {
		return _predicates;
	}

	private void clear() {
		_leftVars.clear();
		_methodID = "";
		_predicates = new HashMap<>();
	}

	@Override
	public boolean visit(MethodDeclaration node) {

		int startLine = _cu.getLineNumber(node.getStartPosition());
		int endLine = _cu.getLineNumber(node.getStartPosition() + node.getLength());

		// no line need to be instrument for current method declaration
		if (!containLine(startLine, endLine)) {
			return true;
		}

		String message = buildMethodInfoString(node);
		if (message == null) {
			return false;
		}
		String keyValue = String.valueOf(Identifier.getIdentifier(message));
		// optimize instrument
		_methodID = keyValue;

		if(node.getBody() != null && node.getBody().statements().size() > 0) {
			ASTNode firstNode = (ASTNode) node.getBody().statements().get(0);
			int line = _cu.getLineNumber(node.getBody().getStartPosition());
			boolean staticMethod = Modifier.isStatic(node.getModifiers());
			List<ASTNode> insertNodes = new LinkedList<>();
			Map<String, Set<String>> alreadyConsidered = new HashMap<>();
			for(Object object : node.parameters()) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) object;
				ASTNode insert = process(svd, line, staticMethod, alreadyConsidered);
				if(insert != null) {
					insertNodes.add(insert);
				}
			}
			List<ASTNode> nodelist = node.getBody().statements();
			int index = ((firstNode instanceof ConstructorInvocation && firstNode.toString().startsWith("this("))
					|| firstNode instanceof SuperConstructorInvocation) ? 1 : 0;
			for(ASTNode astNode : insertNodes) {
				nodelist.add(index, ASTNode.copySubtree(node.getAST(), astNode));
			}
			
			int end =_cu.getLineNumber(node.getBody().getStartPosition() + node.getBody().getLength());
			for(; line <= end; line ++) {
				_lines.add(line);
			}
		}

		return true;
	}

	public boolean visit(ReturnStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (_lines.contains(start)) {
			Expression expr = node.getExpression();
			if (expr != null && !(expr instanceof ClassInstanceCreation)
					&& isComparableType(expr.resolveTypeBinding())) {
				String condition = expr.toString().replace("\n", " ").replaceAll("\\s+", " ");
				node.setExpression((Expression) ASTNode.copySubtree(node.getAST(),
						genReturnWithLog(expr, expr.resolveTypeBinding(), start)));
				addPredicates(getPredicateForReturns(condition), start);
			}
		}
		return true;
	}
	

	public ExpressionStatement process(SingleVariableDeclaration node, int line, boolean staticMethod, Map<String, Set<String>> alreadyConsidered) {
		Type type = node.getType();
		int extra = node.toString().contains("...") ? 1 : 0;
		ExpressionStatement exprStmt = null;
		if(extra > 0 || node.getExtraDimensions() > 0) {
			if(type.isArrayType()) {
				ArrayType arrayType = (ArrayType) type;
				type = ast.newArrayType((Type) ASTNode.copySubtree(ast, arrayType.getElementType()), arrayType.getDimensions() + node.getExtraDimensions() + extra);
			} else {
				type = ast.newArrayType((Type) ASTNode.copySubtree(ast, type), node.getExtraDimensions() + extra);
			}
		}
		
		if (isComparableType(type)) {
			Set<String> variables = new HashSet<>();
			String argName = node.getName().getFullyQualifiedName();
			Map<String, String> availableVars = null;
			if(staticMethod) {
				availableVars = CodeAnalyzer.getAllLocalVariablesAvailableWithType(_srcPath, _relJavaPath, line, true);
			} else {
				availableVars = CodeAnalyzer.getAllVariablesAvailableWithType(_srcPath, _relJavaPath, line);
			}
			for (Entry<String, String> entry : availableVars.entrySet()) {
				String varName = entry.getKey();
				if (!argName.equals(varName) && entry.getValue().equals(type.toString())) {
					Set<String> set = alreadyConsidered.get(argName);
					if(set == null || !set.contains(varName)) {
						variables.add(entry.getKey());
						if(set == null) {
							set = new HashSet<>();
							alreadyConsidered.put(argName, set);
						}
						set.add(varName);
						set = alreadyConsidered.get(varName);
						if(set == null) {
							set = new HashSet<>();
							alreadyConsidered.put(varName, set);
						}
						set.add(argName);
					}
				}
			}
			if (!variables.isEmpty()) {
				Expression expr = genLogCompare((Expression)node.getName(), variables, type.toString(), line);
				exprStmt = ast.newExpressionStatement(expr);
				addPredicates(getPredicatesForArguments(argName, variables), line);
			}
		}
			
		return exprStmt;
	}


	private List<String> getPredicateForReturns(final String expr) {
		List<String> predicates = new ArrayList<String>();
		final String operators[] = { "< 0", "<= 0", "> 0", ">= 0", "== 0", "!= 0" };
		for (final String op : operators) {
			predicates.add("(" + expr + ")" + op + "#RETURN");
		}
		return predicates;
	}


	private List<String> getPredicatesForArguments(final String var1, final Set<String> variables) {
		final String operators[] = { " < ", " <= ", " > ", " >= ", " == ", " != " };
		List<String> predicates = new ArrayList<String>();
		for (final String variable : variables) {
			for (final String op : operators) {
				predicates.add(var1 + op + variable + "#ARG");
			}
		}
		return predicates;
	}

	private void addPredicates(List<String> predicates, int line) {
		List<Pair<String, String>> predWithProb = _predicates.get(line);
		if (predWithProb == null) {
			predWithProb = new ArrayList<Pair<String, String>>();
			_predicates.put(line, predWithProb);
		}
		for (String p : predicates) {
			predWithProb.add(new Pair<String, String>(p, "1"));
		}
	}


	private Expression genReturnWithLog(Expression var1, ITypeBinding type, int line) {
		// auxiliary.Dumper.lpc;
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("lpc"));

		String typeLit = type.getName();
		if (type.isPrimitive()) {
			typeLit = primitive2Wrapper(typeLit);
			methodInvocation.arguments().add(ASTNode.copySubtree(methodInvocation.getAST(), var1));
		} else {
			methodInvocation.arguments().add(extractValue(var1, typeLit));
		}
		// auxiliary.Dumper.<XXXX>lpc;
		// methodInvocation.typeArguments().add(ast.newSimpleType(ast.newSimpleName(typeLit)));

		methodInvocation.arguments().add(zeroByType(typeLit));

		StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(_methodID + "#" + Integer.toString(line));

		StringLiteral var1Literal = ast.newStringLiteral();
		var1Literal.setLiteralValue(var1.toString());

		StringLiteral var2Literal = ast.newStringLiteral();
		var2Literal.setLiteralValue("0");

		BooleanLiteral useSoberLiteral = ast.newBooleanLiteral(_useSober);

		methodInvocation.arguments().add(stringLiteral);
		methodInvocation.arguments().add(var1Literal);
		methodInvocation.arguments().add(var2Literal);
		methodInvocation.arguments().add(useSoberLiteral);

		if (type.isPrimitive()) {
			return extractValue(methodInvocation, typeLit);
		} else {
			return methodInvocation;
		}
	}

	private Expression extractValue(Expression expr, String type) {
		String methodName = null;
		switch (type) {
		case "Character":
			methodName = "charValue";
			break;
		case "Integer":
			methodName = "intValue";
			break;
		case "Byte":
			methodName = "byteValue";
			break;
		case "Short":
			methodName = "shortValue";
			break;
		case "Long":
			methodName = "longValue";
			break;
		case "Float":
			methodName = "floatValue";
			break;
		case "Double":
			methodName = "doubleValue";
			break;
		default:
			return null;
		}
		MethodInvocation mi = ast.newMethodInvocation();
		CastExpression ce = ast.newCastExpression();
		ce.setExpression((Expression) ASTNode.copySubtree(ce.getAST(), expr));
		ce.setType(ast.newSimpleType(ast.newSimpleName(type)));
		ParenthesizedExpression parenthesizedExpression = ast.newParenthesizedExpression();
		parenthesizedExpression.setExpression(ce);
		mi.setExpression(parenthesizedExpression);
		mi.setName(ast.newSimpleName(methodName));
		return mi;
	}

	private Expression zeroByType(String type) {
		String pType = wrapper2Primitive(type);
		CastExpression ce = ast.newCastExpression();
		ce.setType(ast.newPrimitiveType(typeName2PrimitiveTypeCode(pType)));
		ce.setExpression(ast.newNumberLiteral("0"));
		return ce;
	}


	private Expression genLogCompare(Expression var1, Set<String> var2, String type, int line) {
		// auxiliary.Dumper.lpcs;
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("lpcs"));
		boolean isPrimitiveType = isPrimitiveType(type);
		if (isPrimitiveType) {
			type = primitive2Wrapper(type);
			methodInvocation.arguments().add(ASTNode.copySubtree(methodInvocation.getAST(), var1));
		} else {
			methodInvocation.arguments().add(extractValue(var1, type));
		}
		// auxiliary.Dumper.<XXXX>lpcs;
		// methodInvocation.typeArguments().add(ast.newSimpleType(ast.newSimpleName(typeLit)));

		methodInvocation.arguments().add(arrayToListVars(var2, type, isPrimitiveType));

		StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(_methodID + "#" + Integer.toString(line));

		StringLiteral var1Literal = ast.newStringLiteral();
		var1Literal.setLiteralValue(var1.toString());

		BooleanLiteral useSoberLiteral = ast.newBooleanLiteral(_useSober);

		methodInvocation.arguments().add(stringLiteral);
		methodInvocation.arguments().add(var1Literal);
		methodInvocation.arguments().add(arrayToListNames(var2));
		methodInvocation.arguments().add(useSoberLiteral);

		
		return methodInvocation;
	}

	private Expression arrayToListVars(Set<String> variables, String type, boolean isPrimitive) {
		String primType = wrapper2Primitive(type);
		ArrayCreation arrayCreation = ast.newArrayCreation();
		arrayCreation.setType(ast.newArrayType(ast.newPrimitiveType(typeName2PrimitiveTypeCode(primType))));
		ArrayInitializer initializer = ast.newArrayInitializer();
		if (isPrimitive) {
			for (String v : variables) {
				initializer.expressions().add(ast.newSimpleName(v));
			}
		} else {
			for (String v : variables) {
				initializer.expressions().add(extractValue(ast.newSimpleName(v), type));
			}
		}
		arrayCreation.setInitializer(initializer);
		return arrayCreation;
	}

	private Expression arrayToListNames(Set<String> variables) {
		ArrayCreation arrayCreation = ast.newArrayCreation();
		ArrayType type = ast.newArrayType(ast.newSimpleType(ast.newName("String")));
		arrayCreation.setType(type);
		ArrayInitializer initializer = ast.newArrayInitializer();
		for (String v : variables) {
			StringLiteral literal = ast.newStringLiteral();
			literal.setLiteralValue(v);
			initializer.expressions().add(literal);
		}
		arrayCreation.setInitializer(initializer);
		return arrayCreation;
	}

	private String primitive2Wrapper(String type) {
		switch (type) {
		case "byte":
			return "Byte";
		case "char":
			return "Character";
		case "int":
			return "Integer";
		case "short":
			return "Short";
		case "long":
			return "Long";
		case "float":
			return "Float";
		case "double":
			return "Double";
		default:
			return null;
		}
	}

	private String wrapper2Primitive(String type) {
		switch (type) {
		case "Byte":
			return "byte";
		case "Character":
			return "char";
		case "Integer":
			return "int";
		case "Short":
			return "short";
		case "Long":
			return "long";
		case "Float":
			return "float";
		case "Double":
			return "double";
		default:
			return null;
		}
	}
	
	private boolean isComparableType(ITypeBinding type) {
		if (type == null) {
			return false;
		}
		return isComparableType(type.getName());
	}

	private boolean isComparableType(Type type) {
		if(type == null) {
			return false;
		}
		return isComparableType(type.toString());
	}
	
	private boolean isComparableType(String type) {
		if (type == null) {
			return false;
		}
		switch (type) {
		case "byte":
		case "char":
		case "int":
		case "short":
		case "long":
		case "float":
		case "double":
		case "Byte":
		case "Character":
		case "Short":
		case "Integer":
		case "Long":
		case "Float":
		case "Double":
			return true;
		}
		return false;
	}

	private boolean containLine(int startLine, int endLine) {
		for (Integer line : _lines) {
			if (startLine <= line && line <= endLine) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isPrimitiveType(String type) {
		switch(type) {
		case "int":
		case "short":
		case "char":
		case "boolean":
		case "long":
		case "float":
		case "double":
		case "byte":
			return true;
		default:
			return false;
		}
	}

	private PrimitiveType.Code typeName2PrimitiveTypeCode(String type) {
		switch (type) {
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
}
