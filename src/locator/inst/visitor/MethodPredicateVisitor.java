package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import edu.pku.sei.conditon.simple.FeatureGenerator;
import locator.common.config.Constant;
import locator.common.java.Pair;

public class MethodPredicateVisitor extends TraversalVisitor {
	private Map<Integer, List<String>> _returnPredicates = new HashMap<Integer, List<String>>();
	private Map<Integer, List<List<String>>> _returnInScopePredicates = new HashMap<Integer, List<List<String>>>();
	private List<Pair<String, String>> _variables = new ArrayList<Pair<String, String>>();
	private String _methodString;
	private int startLine = -1;
	private int endLine = -1;
	private String _srcPath = "";
	private String _relJavaPath = "";

	public MethodPredicateVisitor(String methodString, List<Pair<String, String>> fields, String srcPath, String relJavaPath) {
		_methodString = methodString;
		_variables.addAll(fields);
		_srcPath = srcPath;
		_relJavaPath = relJavaPath;
	}

	public Map<Integer, List<String>> getReturnPredicates() {
		return _returnPredicates;
	}
	
	public Map<Integer, List<List<String>>> getReturnInScopePredicates() {
		return _returnInScopePredicates;
	}
	
	public int getStartLine() {
		return startLine;
	}
	
	public List<List<String>> getVariablePredicates() {
		List<List<String>> predicates = new ArrayList<List<String>>();
		int varSize = _variables.size();
		for(int i = 0; i < varSize; i++) {
			for(int j = i; j < varSize; j++) {
				if (_variables.get(i).getSecond().equals(_variables.get(j).getSecond())
						&& !_variables.get(i).getFirst().equals(_variables.get(j).getFirst())) {
					predicates.add(getPredicateForVariables(_variables.get(i).getFirst(), _variables.get(j).getFirst()));
				}
			}
		}
		return predicates;
	}
	
	public boolean visit(MethodDeclaration node) {
		String message = buildMethodInfoString(node);
		if (message == null) {
			return true;
		}
		if (message.equals(_methodString)) {
			startLine = _cu.getLineNumber(node.getStartPosition());
			endLine = _cu.getLineNumber(node.getStartPosition() + node.getLength());
			List<SingleVariableDeclaration> params = node.parameters();
			for(SingleVariableDeclaration param : params) {
				_variables.add(new Pair<String, String>(param.getName().toString(), param.getType().toString()));
			}
		}
		return true;
	}

	public boolean visit(ReturnStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (start >= startLine && start < endLine) {
			Expression expr = node.getExpression();
			if (expr != null) {
				String condition = expr.toString();
				_returnPredicates.put(start, getPredicateForReturns(condition));
			}
			List<String> varFeature = FeatureGenerator.generateVarFeature(_srcPath, _relJavaPath, start);
			List<List<String>> preds = _returnInScopePredicates.get(start);
			if (preds == null) {
				preds = new ArrayList<List<String>>();
			}
			for (String feature : varFeature) {
				String[] elements = feature.split("\t");
				String varName = elements[Constant.FEATURE_VAR_NAME_INDEX];
				String varType = elements[Constant.FEATURE_VAR_TYPE_INDEX];
				for(Pair<String, String> otherVar : _variables) {
					if (varType.equals(otherVar.getSecond()) && !varName.equals(otherVar.getFirst())) {
						preds.add(getPredicateForVariables(varName, otherVar.getFirst()));
					}
				}
			}
			if (!preds.isEmpty()) {
				_returnInScopePredicates.put(start, preds);
			}
		}
		return true;
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
	
	private List<String> getPredicateForVariables(final String var1, final String var2) {
		final String operators[] = {" < ", " <= ", " > ", " >= ", " == ", " != "};
		List<String> similarPredicates = new ArrayList<String>();
		for (final String op : operators) {
			similarPredicates.add(var1 + op + var2 + "#VAR");
		}
		return similarPredicates;
	}
}

