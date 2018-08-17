package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

import locator.aux.extractor.CodeAnalyzer;
import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.util.Pair;

public class NoSideEffectPredicateInstrumentVisitor extends TraversalVisitor{
	private Set<Integer> _lines = null;
	private List<Pair<String, String>> _leftVars = new ArrayList<Pair<String, String>>();
	private String _srcPath = "";
	private String _relJavaPath = "";
	private boolean _useSober;
	private String _methodID = "";
	private Map<Integer, List<Pair<String, String>>> _predicates = new HashMap<>();
	private static AST ast = AST.newAST(Constant.AST_LEVEL);
	
	public NoSideEffectPredicateInstrumentVisitor(boolean useSober) {
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
		if(!containLine(startLine, endLine)){
			return true;
		}
		
		String message = buildMethodInfoString(node);
		if (message == null) {
			return false;
		}
		String keyValue = String.valueOf(Identifier.getIdentifier(message));
		// optimize instrument
		_methodID = keyValue;

		return true;
	}

//	public List<List<String>> getAssignmentPredicates() {
//		List<List<String>> predicates = new ArrayList<List<String>>();
//		if (!_leftVars.isEmpty()) {
//			Map<String, Set<String>> variables = new HashMap<String, Set<String>>();
//			List<String> varFeature = FeatureGenerator.generateVarFeature(_srcPath, _relJavaPath, _line);
//			for (String feature : varFeature) {
//				String[] elements = feature.split("\t");
//				String varName = elements[Constant.FEATURE_VAR_NAME_INDEX];
//				String varType = elements[Constant.FEATURE_VAR_TYPE_INDEX];
//				for(Pair<String, String> leftVar : _leftVars) {
//					if (varType.equals(leftVar.getSecond())) {
//						Set<String> rightVars = variables.get(leftVar.getFirst());
//						if (rightVars == null) {
//							rightVars = new HashSet<String>();
//							variables.put(leftVar.getFirst(), rightVars);
//						}
//						rightVars.add(varName);
//					}
//				}
//			}
//			predicates.addAll(getPredicatesForAssignment(variables));
//		}
//		return predicates;
//	}

	public boolean visit(ReturnStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (_lines.contains(start)) {
			Expression expr = node.getExpression();
			if (expr != null && isComparableType(expr.resolveTypeBinding())) {
				String condition = expr.toString().replace("\n", " ").replaceAll("\\s+", " ");
				node.setExpression((Expression) ASTNode.copySubtree(node.getAST(),
						genReturnWithLog(expr, expr.resolveTypeBinding(), start)));
				addPredicates(getPredicateForReturns(condition), start);
			}
		}
		return true;
	}
	
	public boolean visit(IfStatement node) {
		int start = _cu.getLineNumber(node.getExpression().getStartPosition());
		if (_lines.contains(start)) {
			Expression expr = node.getExpression();
			String condition = expr.toString().replaceAll("\\s+", " ");
			node.setExpression((Expression) ASTNode.copySubtree(node.getAST(), genConditionWithLog(expr, start)));
			addPredicates(getPredicateForConditions(condition), start);
		}
		return true;
	}
	
	public boolean visit(ForStatement node) {
		int position = node.getStartPosition();
		if (node.getExpression() != null) {
			position = node.getExpression().getStartPosition();
		} else if (node.initializers() != null && node.initializers().size() > 0) {
			position = ((ASTNode) node.initializers().get(0)).getStartPosition();
		} else if (node.updaters() != null && node.updaters().size() > 0) {
			position = ((ASTNode) node.updaters().get(0)).getStartPosition();
		}
		int start = _cu.getLineNumber(position);
		if (_lines.contains(start)) {
			Expression expr = node.getExpression();
			if(expr != null) {
				String condition = expr.toString().replaceAll("\\s+", " ");
				node.setExpression((Expression) ASTNode.copySubtree(node.getAST(), genConditionWithLog(expr, start)));
				addPredicates(getPredicateForConditions(condition), start);
			}
		}
		return true;
	}
	
	public boolean visit(WhileStatement node) {
		int start = _cu.getLineNumber(node.getExpression().getStartPosition());
		if (_lines.contains(start)) {
			Expression expr = node.getExpression();
			String condition = expr.toString().replaceAll("\\s+", " ");
			if (condition.equals("true")) {
				return true;
			}
			node.setExpression((Expression) ASTNode.copySubtree(node.getAST(), genConditionWithLog(expr, start)));
			addPredicates(getPredicateForConditions(condition), start);
		}
		return true;
	}
	
	public boolean visit(DoStatement node) {
		int start = _cu.getLineNumber(node.getExpression().getStartPosition());
		if (_lines.contains(start)) {
			Expression expr = node.getExpression();
			String condition = expr.toString().replaceAll("\\s+", " ");
			if (condition.equals("true")) {
				return true;
			}
			node.setExpression((Expression) ASTNode.copySubtree(node.getAST(), genConditionWithLog(expr, start)));
			addPredicates(getPredicateForConditions(condition), start);
		}
		return true;
	}
	
//	public boolean visit(SwitchStatement node) {
//		int start = _cu.getLineNumber(node.getExpression().getStartPosition());
//		if (start == _line) {
//			Expression expr = node.getExpression();
//			String condition = expr.toString();
//			_predicates.add(getPredicateForConditions(condition));
//		}
//		return true;
//	}
	
	public boolean visit(Assignment node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (_lines.contains(start)) {
			Expression expr = node.getRightHandSide();
			if(expr instanceof NumberLiteral || expr instanceof BooleanLiteral || expr instanceof CharacterLiteral) {
				return true;
			}
			if (node.getLeftHandSide() != null && expr != null) {
				String lhString = node.getLeftHandSide().toString();
//				ITypeBinding type = expr.resolveTypeBinding();
				ITypeBinding type = node.getLeftHandSide().resolveTypeBinding();
				if (isComparableType(type)) {
					String rightExprStr = expr.toString().replaceAll("\\s+", " ");
					Set<String> variables = new HashSet<>();
					Map<String, String> availableVars = CodeAnalyzer.getAllLocalVariablesAvailableWithType(_srcPath, _relJavaPath, start, true);
					for(Entry<String, String> entry : availableVars.entrySet()) {
						String varName = entry.getKey();
						if(!lhString.equals(varName) && !varName.equals(rightExprStr) && entry.getValue().equals(type.getName())) {
							variables.add(entry.getKey());
						}
					}
					if (!variables.isEmpty()) {						
						node.setRightHandSide((Expression) ASTNode.copySubtree(node.getAST(),
								genAssignWithLog(expr, variables, type, start)));
						addPredicates(getPredicatesForAssignment(lhString, variables), start);
					}
				} else if(Constant.BOOL_ADD_NULL_PREDICATE_FOR_ASSGIN) {
					node.setRightHandSide((Expression) ASTNode.copySubtree(node.getAST(), genCompNullWithLog(expr, type, start)));
					List<String> predicates = new ArrayList<>(1);
					predicates.add(lhString + " = null#ASSIGN");
					addPredicates(predicates, start);
				}
			}
		}
		return true;
	}
	
	public boolean visit(VariableDeclarationStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (_lines.contains(start)) {
			List<VariableDeclarationFragment> fragments = node.fragments();
			for(VariableDeclarationFragment fragment : fragments) {
				Expression expr = fragment.getInitializer();
				if (expr != null) {
					if(expr instanceof NumberLiteral || expr instanceof BooleanLiteral || expr instanceof CharacterLiteral) {
						continue;
					}
					String rightExprStr = expr.toString();
					ITypeBinding type = expr.resolveTypeBinding();
					String typeStr = null;
					if(type != null) {
						if (isComparableType(type)) {
							typeStr = type.getName();
						}
					} else if(fragment.resolveBinding() != null) {
						type = fragment.resolveBinding().getType();
						if(type != null && isComparableType(type)) {
							typeStr = type.getName();
						}
					}
					if (typeStr != null) {
						int line = _cu.getLineNumber(fragment.getStartPosition());
						Set<String> variables = new HashSet<>();
						Map<String, String> varAvailable = CodeAnalyzer.getAllLocalVariablesAvailableWithType(_srcPath, _relJavaPath, line, true);
						for(Entry<String, String> entry : varAvailable.entrySet()) {
							if(!entry.getKey().equals(rightExprStr) && entry.getValue().equals(type.getName())) {
								variables.add(entry.getKey());
							}
						}
						
						if (!variables.isEmpty()) {							
							fragment.setInitializer((Expression) ASTNode.copySubtree(node.getAST(),
									genAssignWithLog(expr, variables, type, start)));
							addPredicates(getPredicatesForAssignment(rightExprStr, variables), start);
						}
					}
				} else if(!Modifier.isFinal(node.getModifiers())) {
					Type type = node.getType();
					if(fragment.getExtraDimensions() > 0) {
						if(type.isArrayType()) {
							ArrayType arrayType = (ArrayType) type;
							type = ast.newArrayType((Type) ASTNode.copySubtree(ast, arrayType.getElementType()), arrayType.getDimensions() + fragment.getExtraDimensions());
						} else {
							type = ast.newArrayType((Type) ASTNode.copySubtree(ast, type), fragment.getExtraDimensions());
						}
					}
					Expression expression = genDefaultValue(type);
					fragment.setInitializer((Expression) ASTNode.copySubtree(fragment.getAST(), expression));
				}
			}
		}
		return true;
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
	
	private List<String> getPredicateForReturns(final String expr) {
		List<String> predicates = new ArrayList<String>();
		final String operators[] = { "< 0", "<= 0", "> 0", ">= 0", "== 0",
				"!= 0" };
		for (final String op : operators) {
			predicates.add("(" + expr + ")" + op + "#RETURN");
		}
		return predicates;
	}
	
	private List<String> getPredicateForConditions(final String expr) {
		List<String> predicates = new ArrayList<String>();
		predicates.add(expr + "#CONDITION");
		predicates.add("!(" + expr + ")#CONDITION");
		return predicates;
	}
	
	private List<String> getPredicatesForAssignment(final String var1, final Set<String> variables) {
		final String operators[] = {" < ", " <= ", " > ", " >= ", " == ", " != "};
		List<String> predicates = new ArrayList<String>();
		for(final String variable : variables) {
			List<String> similarPredicates = new ArrayList<String>();
			for (final String op : operators) {
				predicates.add(var1 + op + variable + "#ASSIGN");
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
		for(String p : predicates) {
			predWithProb.add(new Pair<String, String>(p, "1"));
		}
	}
	
	private Expression genConditionWithLog(Expression condition, int line) {
		if(condition instanceof BooleanLiteral) {
			return condition;
		}
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		if (_useSober) {			
			methodInvocation.setName(ast.newSimpleName("logConditionCoverageWithEvaluationBias"));
		} else {
			methodInvocation.setName(ast.newSimpleName("lcc"));
		}
		
		methodInvocation.arguments().add(ASTNode.copySubtree(methodInvocation.getAST(), condition));

		String trueLogInfo = _methodID + "#" + line + "#" + condition.toString() + "#1";
		String falseLogInfo = _methodID + "#" + line + "#!(" + condition.toString() + ")#1";

		StringLiteral trueStr = ast.newStringLiteral();
		trueStr.setLiteralValue(trueLogInfo);
		methodInvocation.arguments().add(trueStr);
		if (!_useSober) {			
			StringLiteral falseStr = ast.newStringLiteral();
			falseStr.setLiteralValue(falseLogInfo);
			methodInvocation.arguments().add(falseStr);
		}
		return methodInvocation;
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
//		methodInvocation.typeArguments().add(ast.newSimpleType(ast.newSimpleName(typeLit)));
				
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
		
		if(type.isPrimitive()) {
			return extractValue(methodInvocation, typeLit);
		} else {
			return methodInvocation;
		}
	}
	
	private Expression extractValue(Expression expr, String type) {
		String methodName = null;
		switch(type) {
		case "Character":
			methodName = "charValue"; break;
		case "Integer":
			methodName = "intValue"; break;
		case "Byte":
			methodName = "byteValue"; break;
		case "Short":
			methodName = "shortValue"; break;
		case "Long":
			methodName = "longValue"; break;
		case "Float":
			methodName = "floatValue"; break;
		case "Double":
			methodName = "doubleValue"; break;
		default:
			return null;
		}
		MethodInvocation mi = ast.newMethodInvocation();
		CastExpression ce = ast.newCastExpression();
		ce.setExpression((Expression)ASTNode.copySubtree(ce.getAST(), expr));
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
	
	private Expression genCompNullWithLog(Expression expr, ITypeBinding type, int line) {
		if(type == null || type.isPrimitive()) {
			return expr;
		}
		
		Type castType = parseType(type);
		if(castType == null) {
			return expr;
		}
		
		//  auxiliary.Dumper.lpcNull(final Object a, String message, String var, boolean useSober) 
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("lpcNull"));
		// Object a
		methodInvocation.arguments().add(ASTNode.copySubtree(ast, expr));
		
		// String message
		StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(_methodID + "#" + Integer.toString(line));
		methodInvocation.arguments().add(stringLiteral);
		
		// Stirng var
		StringLiteral exprString = ast.newStringLiteral();
		exprString.setLiteralValue(expr.toString());
		methodInvocation.arguments().add(exprString);
		
		// boolean useSober
		BooleanLiteral useSoberLiteral = ast.newBooleanLiteral(_useSober);
		methodInvocation.arguments().add(useSoberLiteral);
		
		// (Type)auxiliary.Dumper.lpcNull(final Object a, String message, String var, boolean useSober);
		CastExpression ce = ast.newCastExpression();
		ce.setType((Type) ASTNode.copySubtree(ast, castType));
		ce.setExpression(methodInvocation);
		
		return ce;
	}

	private Type parseType(ITypeBinding typeBinding) {
		if (typeBinding == null) {
			return null;
		}

		if (typeBinding.isPrimitive()) {
			return ast.newPrimitiveType(PrimitiveType.toCode(typeBinding.getName()));
		}

		if (typeBinding.isCapture()) {
			ITypeBinding wildCard = typeBinding.getWildcard();
			WildcardType capType = ast.newWildcardType();
			ITypeBinding bound = wildCard.getBound();
			if (bound != null) {
				capType.setBound(parseType(bound), wildCard.isUpperbound());
			}
			return capType;
		}

		if (typeBinding.isArray()) {
			Type elType = parseType(typeBinding.getElementType());
			return ast.newArrayType(elType, typeBinding.getDimensions());
		}

		if (typeBinding.isParameterizedType()) {
			ParameterizedType type = ast.newParameterizedType(parseType(typeBinding.getErasure()));

			@SuppressWarnings("unchecked")
			List<Type> newTypeArgs = type.typeArguments();
			for (ITypeBinding typeArg : typeBinding.getTypeArguments()) {
				newTypeArgs.add(parseType(typeArg));
			}

			return type;
		}

		// simple or raw type
		String qualName = typeBinding.getQualifiedName();
		if (qualName == null || "".equals(qualName)) {
			return null;
		}
		return ast.newSimpleType(ast.newName(qualName));
	}
	
	private Expression genAssignWithLog(Expression var1, Set<String> var2, ITypeBinding type, int line) {
		// auxiliary.Dumper.lpcs;
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("auxiliary.Dumper"));
		methodInvocation.setName(ast.newSimpleName("lpcs"));
		String typeLit = type.getName();
		if (type.isPrimitive()) {			
			typeLit = primitive2Wrapper(typeLit);
			methodInvocation.arguments().add(ASTNode.copySubtree(methodInvocation.getAST(), var1));
		} else {
			methodInvocation.arguments().add(extractValue(var1, typeLit));
		}
		// auxiliary.Dumper.<XXXX>lpcs;
//		methodInvocation.typeArguments().add(ast.newSimpleType(ast.newSimpleName(typeLit)));
		
		methodInvocation.arguments().add(arrayToListVars(var2, typeLit, type.isPrimitive()));
	
		StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(_methodID + "#" + Integer.toString(line));
		
		StringLiteral var1Literal = ast.newStringLiteral();
		var1Literal.setLiteralValue(var1.toString());
		
		BooleanLiteral useSoberLiteral = ast.newBooleanLiteral(_useSober);
		
		methodInvocation.arguments().add(stringLiteral);
		methodInvocation.arguments().add(var1Literal);
		methodInvocation.arguments().add(arrayToListNames(var2));
		methodInvocation.arguments().add(useSoberLiteral);
		
		if(type.isPrimitive()) {
			return extractValue(methodInvocation, typeLit);
		} else {
			return methodInvocation;
		}
	}
	
	private Expression arrayToListVars(Set<String> variables, String type, boolean isPrimitive) {
		String primType = wrapper2Primitive(type);
		ArrayCreation arrayCreation = ast.newArrayCreation();
		arrayCreation.setType(ast.newArrayType(ast.newPrimitiveType(typeName2PrimitiveTypeCode(primType))));
		ArrayInitializer initializer = ast.newArrayInitializer();
		if(isPrimitive) {
			for(String v : variables) {
				initializer.expressions().add(ast.newSimpleName(v));
			}
		} else {
			for(String v : variables) {
				initializer.expressions().add(extractValue(ast.newSimpleName(v), type));
			}
		}
		arrayCreation.setInitializer(initializer);
		return arrayCreation;
//		
//		MethodInvocation methodInvocation = ast.newMethodInvocation();
//		methodInvocation.setExpression(ast.newName("java.util.Arrays"));
//		methodInvocation.setName(ast.newSimpleName("asList"));
//		for(String v : variables) {
//			methodInvocation.arguments().add(ast.newSimpleName(v));
//		}
//		return methodInvocation;
	}
	
	private Expression arrayToListNames(Set<String> variables) {
		ArrayCreation arrayCreation = ast.newArrayCreation();
		ArrayType type = ast.newArrayType(ast.newSimpleType(ast.newName("String")));
		arrayCreation.setType(type);
		ArrayInitializer initializer = ast.newArrayInitializer();
		for(String v : variables) {
			StringLiteral literal = ast.newStringLiteral();
			literal.setLiteralValue(v);
			initializer.expressions().add(literal);
		}
		arrayCreation.setInitializer(initializer);
		return arrayCreation;
//		MethodInvocation methodInvocation = ast.newMethodInvocation();
//		methodInvocation.setExpression(ast.newName("java.util.Arrays"));
//		methodInvocation.setName(ast.newSimpleName("asList"));
//		for(String v : variables) {
//			StringLiteral str = ast.newStringLiteral();
//			str.setLiteralValue(v);
//			methodInvocation.arguments().add(str);
//		}
//		return methodInvocation;
	}
	
	private String primitive2Wrapper(String type) {
		switch(type) {
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
		switch(type) {
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
		switch(type.getName()) {
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
	
	private boolean containLine(int startLine, int endLine){
		for(Integer line : _lines){
			if (startLine <= line && line <= endLine) {
				return true;
			}
		}
		return false;
	}
	
	private PrimitiveType.Code typeName2PrimitiveTypeCode(String type) {
		switch(type) {
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
