package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import edu.pku.sei.conditon.simple.FeatureGenerator;
import locator.common.config.Constant;
import locator.inst.visitor.feature.SideEffectAnalysis;

public class StatisticalDebuggingPredicatesVisitor extends ASTVisitor {
	private int _line = -1;
	private CompilationUnit _cu = null;
	private List<String> _predicates = new ArrayList<String>();
	private String _leftVar = "";
	private String _leftVarType = "";
	private String _srcPath = "";
	private String _relJavaPath = "";

	public StatisticalDebuggingPredicatesVisitor(int line, String srcPath, String relJavaPath) {
		_line = line;
		_srcPath = srcPath;
		_relJavaPath = relJavaPath;
	}

	public List<String> getPredicates() {
		return _predicates;
	}
	
	public List<String> getAssignmentPredicates() {
		List<String> predicates = new ArrayList<String>();
		if (!_leftVarType.isEmpty()) {
			Set<String> variables = new HashSet<String>();
			List<String> varFeature = FeatureGenerator.generateVarFeature(_srcPath, _relJavaPath, _line);
			for (String feature : varFeature) {
				String[] elements = feature.split("\t");
				String varName = elements[Constant.FEATURE_VAR_NAME_INDEX];
				String varType = elements[Constant.FEATURE_VAR_TYPE_INDEX];
				if (varType.equals(_leftVarType)) {
					variables.add(varName);
				}
			}
			predicates.addAll(getPredicatesForAssignment(variables));
		}
		return predicates;
	}

	public boolean visit(CompilationUnit unit) {
		_cu = unit;
		return true;
	}

	public boolean visit(ReturnStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (start == _line) {
			Expression expr = node.getExpression();
			if (expr != null) {
				String condition = expr.toString();
				if (!SideEffectAnalysis.hasSideEffect(condition)) {
					_predicates.addAll(getPredicateForReturns(condition));
				}
			}
		}
		return true;
	}
	
	public boolean visit(IfStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (start == _line) {
			Expression expr = node.getExpression();
			String condition = expr.toString();
			if (!SideEffectAnalysis.hasSideEffect(condition)) {
				_predicates.addAll(getPredicateForConditions(condition));
			}
		}
		return true;
	}
	
	public boolean visit(ForStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (start == _line) {
			Expression expr = node.getExpression();
			String condition = expr.toString();
			if (!SideEffectAnalysis.hasSideEffect(condition)) {
				_predicates.addAll(getPredicateForConditions(condition));
			}
		}
		return true;
	}
	
	public boolean visit(WhileStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (start == _line) {
			Expression expr = node.getExpression();
			String condition = expr.toString();
			if (!SideEffectAnalysis.hasSideEffect(condition)) {
				_predicates.addAll(getPredicateForConditions(condition));
			}
		}
		return true;
	}
	
	public boolean visit(DoStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (start == _line) {
			Expression expr = node.getExpression();
			String condition = expr.toString();
			if (!SideEffectAnalysis.hasSideEffect(condition)) {
				_predicates.addAll(getPredicateForConditions(condition));
			}
		}
		return true;
	}
	
	public boolean visit(SwitchStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (start == _line) {
			Expression expr = node.getExpression();
			String condition = expr.toString();
			if (!SideEffectAnalysis.hasSideEffect(condition)) {
				_predicates.addAll(getPredicateForConditions(condition));
			}
		}
		return true;
	}
	
	public boolean visit(Assignment node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (start == _line) {
			Expression expr = node.getLeftHandSide();
			if (expr != null) {
				_leftVar = expr.toString();
				ITypeBinding type = expr.resolveTypeBinding();
				if (type.isPrimitive()) {
					_leftVarType = type.getName();
				}
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
	
	private List<String> getPredicateForConditions(final String expr) {
		List<String> predicates = new ArrayList<String>();
		predicates.add(expr);
		predicates.add("!(" + expr + ")");
		return predicates;
	}
	
	private List<String> getPredicatesForAssignment(final Set<String> variables) {
		final String operators[] = {"<", "<=", ">", ">=", "==", "!="};
		List<String> predicates = new ArrayList<String>();
		for(final String variable : variables) {
			for (final String op : operators) {
				predicates.add(_leftVar + op + variable);
			}
		}
		return predicates;
	}
}
