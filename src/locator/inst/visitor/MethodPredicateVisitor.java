package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import locator.common.java.Pair;

public class MethodPredicateVisitor extends TraversalVisitor {
	private Map<Integer, List<String>> _returnPredicates = new HashMap<Integer, List<String>>();
	private List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
	private List<Pair<String, String>> _fields = new ArrayList<Pair<String, String>>();
	private String _methodString;
	private int startLine = -1;
	private int endLine = -1;

	public MethodPredicateVisitor(String methodString, List<Pair<String, String>> fields) {
		_methodString = methodString;
		_fields = fields;
	}

	public Map<Integer, List<String>> getReturnPredicates() {
		return _returnPredicates;
	}
	
	public int getStartLine() {
		return startLine;
	}
	
	public List<List<String>> getVariablePredicates() {
		final String operators[] = {" < ", " <= ", " > ", " >= ", " == ", " != "};
		List<List<String>> predicates = new ArrayList<List<String>>();
		List<Pair<String, String>> variables = new ArrayList<Pair<String, String>>();
		variables.addAll(parameters);
		variables.addAll(_fields);
		int varSize = variables.size();
		for(int i = 0; i < varSize; i++) {
			for(int j = i; j < varSize; j++) {
				if (variables.get(i).getSecond().equals(variables.get(j).getSecond())
						&& !variables.get(i).getFirst().equals(variables.get(j).getFirst())) {
					List<String> similarPredicates = new ArrayList<String>();
					for (final String op : operators) {
						similarPredicates.add(variables.get(i).getFirst() + op + variables.get(j).getFirst());
					}
					predicates.add(similarPredicates);
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
				parameters.add(new Pair<String, String>(param.getName().toString(), param.getType().toString()));
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
		}
		return true;
	}
	
	private List<String> getPredicateForReturns(final String expr) {
		List<String> predicates = new ArrayList<String>();
		final String operators[] = { "< 0", "<= 0", "> 0", ">= 0", "== 0",
				"!= 0" };
		for (final String op : operators) {
			predicates.add("(" + expr + ")" + op);
		}
		return predicates;
	}
}

