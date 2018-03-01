package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
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
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import edu.pku.sei.conditon.simple.FeatureGenerator;
import locator.common.config.Constant;
import locator.common.java.Pair;

public class StatisticalDebuggingPredicatesVisitor extends ASTVisitor {
	private int _line = -1;
	private CompilationUnit _cu = null;
	private List<String> _predicates = new ArrayList<String>();
	private List<Pair<String, String>> _leftVars = new ArrayList<Pair<String, String>>();
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
	
	public List<List<String>> getAssignmentPredicates() {
		List<List<String>> predicates = new ArrayList<List<String>>();
		if (!_leftVars.isEmpty()) {
			Map<String, Set<String>> variables = new HashMap<String, Set<String>>();
			List<String> varFeature = FeatureGenerator.generateVarFeature(_srcPath, _relJavaPath, _line);
			for (String feature : varFeature) {
				String[] elements = feature.split("\t");
				String varName = elements[Constant.FEATURE_VAR_NAME_INDEX];
				String varType = elements[Constant.FEATURE_VAR_TYPE_INDEX];
				for(Pair<String, String> leftVar : _leftVars) {
					if (varType.equals(leftVar.getSecond())) {
						Set<String> rightVars = variables.get(leftVar.getFirst());
						if (rightVars == null) {
							rightVars = new HashSet<String>();
							variables.put(leftVar.getFirst(), rightVars);
						}
						rightVars.add(varName);
					}
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
				_predicates.addAll(getPredicateForReturns(condition));
			}
		}
		return true;
	}
	
	public boolean visit(IfStatement node) {
		int start = _cu.getLineNumber(node.getExpression().getStartPosition());
		if (start == _line) {
			Expression expr = node.getExpression();
			String condition = expr.toString();
			_predicates.addAll(getPredicateForConditions(condition));
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
		if (start == _line) {
			Expression expr = node.getExpression();
			if(expr != null) {
				String condition = expr.toString();
				_predicates.addAll(getPredicateForConditions(condition));
			}
		}
		return true;
	}
	
	public boolean visit(WhileStatement node) {
		int start = _cu.getLineNumber(node.getExpression().getStartPosition());
		if (start == _line) {
			Expression expr = node.getExpression();
			String condition = expr.toString();
			_predicates.addAll(getPredicateForConditions(condition));
		}
		return true;
	}
	
	public boolean visit(DoStatement node) {
		int start = _cu.getLineNumber(node.getExpression().getStartPosition());
		if (start == _line) {
			Expression expr = node.getExpression();
			String condition = expr.toString();
			_predicates.addAll(getPredicateForConditions(condition));
		}
		return true;
	}
	
	public boolean visit(SwitchStatement node) {
		int start = _cu.getLineNumber(node.getExpression().getStartPosition());
		if (start == _line) {
			Expression expr = node.getExpression();
			String condition = expr.toString();
			_predicates.addAll(getPredicateForConditions(condition));
		}
		return true;
	}
	
	public boolean visit(Assignment node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (start == _line) {
			Expression expr = node.getLeftHandSide();
			if (expr != null) {
				ITypeBinding type = expr.resolveTypeBinding();
				if (type.isPrimitive()) {
					_leftVars.add(new Pair<String, String>(expr.toString(), type.getName()));
				}
			}
		}
		return true;
	}
	
	public boolean visit(VariableDeclarationStatement node) {
		int start = _cu.getLineNumber(node.getStartPosition());
		if (start == _line) {
			List<VariableDeclarationFragment> fragments = node.fragments();
			for(VariableDeclarationFragment fragment : fragments) {
				Expression expr = fragment.getInitializer();
				if (expr != null) {
					ITypeBinding type = expr.resolveTypeBinding();
					if(type != null) {
						if (type.isPrimitive()) {
							_leftVars.add(new Pair<String, String>(fragment.getName().getFullyQualifiedName(), type.getName()));
						}
					} else if(fragment.resolveBinding() != null) {
						type = fragment.resolveBinding().getType();
						if(type != null && type.isPrimitive()) {
							_leftVars.add(new Pair<String, String>(fragment.getName().getFullyQualifiedName(), type.getName()));
						}
					}
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
	
	private List<List<String>> getPredicatesForAssignment(final Map<String, Set<String>> variables) {
		final String operators[] = {"<", "<=", ">", ">=", "==", "!="};
		List<List<String>> predicates = new ArrayList<List<String>>();
		for(final Map.Entry<String, Set<String>> variable : variables.entrySet()) {
			String v0 = variable.getKey();
			for(final String v : variable.getValue()) {
				if(v0.equals(v)) continue;
				List<String> similarPredicates = new ArrayList<String>();
				for (final String op : operators) {
					similarPredicates.add(variable.getKey() + op + v);
				}
				predicates.add(similarPredicates);
			}
		}
		return predicates;
	}
}
