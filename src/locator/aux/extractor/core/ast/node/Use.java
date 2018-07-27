/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.aux.extractor.core.ast.node;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;

/**
 * 
 * @author Jiajun
 *
 * Jul 11, 2018
 */
public class Use {

	public static enum USETYPE{
		DEFINE("DEFINE"),
		READ("READ"),
		WRITE("WRITE"),
		UNKNOWN("UNKNOWN");
		
		private String _value;
		private USETYPE(String value) {
			_value = value;
		}
		
		@Override
		public String toString() {
			return _value;
		}
	}
	
	private USETYPE _usetype;
	private int _line = 0;
	private int _column = 0;
	private ASTNode _expression;
	private Expression _booleanExpr;
	private String _exprWithHole;
	private StmtType _stmtType = StmtType.UNKNOWN;
	private BasicBlock _basicBlock;
	private boolean _isfieldUse = false;
	private String _objOfFieldUse = null;
	private Variable _variable; 
	
	public Use(int line, int column, ASTNode expression, Expression booleanExpr, USETYPE usetype, StmtType stmtType, BasicBlock basicBlock) {
		_line = line;
		_column = column;
		_expression = expression;
		_booleanExpr = booleanExpr;
		_usetype = usetype;
		_stmtType = stmtType;
		_basicBlock = basicBlock;
	}
	
	public void setIsField(boolean isfieldUse, String objOfField) {
		_isfieldUse = isfieldUse;
		_objOfFieldUse = objOfField;
	}
	
	public void setVariable(Variable variable) {
		_variable = variable;
	}
	
	public Variable getVariableDefine() {
		return _variable;
	}
	
	public boolean isFieldUse() {
		return _isfieldUse;
	}
	
	public String getExprWithHole() {
		if (_exprWithHole == null && _booleanExpr != null) {
			if (_variable != null) {
				_exprWithHole = Transformer.tranform(_basicBlock.getCompilationUnit(), _booleanExpr, _line, _column,
						_variable.getName(), _variable.getType());
			}
		}
		return _exprWithHole;
	}

	public String getObjectOfFieldUse() {
		return _objOfFieldUse;
	}
	
	public USETYPE getUseType() {
		return _usetype;
	}
	
	public StmtType getStmtType() {
		return _stmtType;
	}
	
	public int getLineNumber() {
		return _line;
	}
	
	public int getColumnNumber() {
		return _column;
	}
	
	public ASTNode getUsedExpression() {
		return _expression;
	}

	public BasicBlock getParentBlock() {
		return _basicBlock;
	}

	public void dump() {
		System.out.println("[USE ]Type : " + _usetype.toString() + "(" + _line + ", " + _column + ")");
	}
	
	@Override
	public String toString() {
		return "[USE ]Type : " + _usetype.toString() + "(" + _line + ", " + _column + ")";
	}
	
	private static class Transformer extends ASTVisitor{
		
		private static CompilationUnit _unit;
		private static int _line;
		private static int _column;
		private static String _varName;
		private static Type _varType;
		public static String tranform(final CompilationUnit unit, final Expression expression, final int line,
				final int column, final String varName, final Type varType) {
			_unit = unit;
			_line = line;
			_column = column;
			_varName = varName;
			_varType = varType;
			return process(expression);
		}
		
		public static String process(Expression expression) {
			if (expression instanceof ArrayAccess) {
				ArrayAccess arrayAccess = (ArrayAccess) expression;
				return process(arrayAccess.getArray()) + "[" + process(arrayAccess.getIndex()) + "]";
				
			} else if (expression instanceof Assignment) {
				Assignment assignment = (Assignment) expression;
				return process(assignment.getLeftHandSide()) + assignment.getOperator().toString()
						+ process(assignment.getRightHandSide());
				
			} else if (expression instanceof BooleanLiteral) {
				return expression.toString();
				
			} else if (expression instanceof CastExpression) {
				CastExpression castExpression = (CastExpression) expression;
				return "(" + castExpression.getType().toString() + ")" + process(castExpression.getExpression());
				
			} else if (expression instanceof CharacterLiteral) {
				return expression.toString();
				
			} else if (expression instanceof ConditionalExpression) {
				ConditionalExpression ce = (ConditionalExpression) expression;
				return process(ce.getExpression()) + "?" + process(ce.getThenExpression()) + ":"
						+ process(ce.getElseExpression());
				
			} else if (expression instanceof FieldAccess) {
				FieldAccess fa = (FieldAccess) expression;
				return process(fa.getExpression()) + "." + process(fa.getName());
				
			} else if (expression instanceof InfixExpression) {
				InfixExpression infixExpression = (InfixExpression) expression;
				return process(infixExpression.getLeftOperand()) + infixExpression.getOperator().toString()
						+ process(infixExpression.getRightOperand());
				
			} else if (expression instanceof InstanceofExpression) {
				InstanceofExpression ie = (InstanceofExpression) expression;
				return process(ie.getLeftOperand()) + " instanceof " + ie.getRightOperand();
				
			} else if (expression instanceof MethodInvocation) {
				MethodInvocation mi = (MethodInvocation) expression;
				StringBuffer string = new StringBuffer();
				if(mi.getExpression() != null) {
					string.append(process(mi.getExpression()) + ".");
				}
				string.append(mi.getName().toString());
				string.append("(");
				for(int i = 0; i < mi.arguments().size(); i++) {
					if(i > 0) {
						string.append(",");
					}
					string.append(process((Expression) mi.arguments().get(i)));
				}
				string.append(")");
				return string.toString();
			} else if (expression instanceof Name) {
				if(expression instanceof SimpleName) {
					int line = _unit.getLineNumber(expression.getStartPosition());
					int column = _unit.getColumnNumber(expression.getStartPosition());
					if(line == _line && column == _column && expression.toString().equals(_varName)) {
						return "$" + _varType + "$";
					} else {
						return expression.toString();
					}
				} else if(expression instanceof QualifiedName) {
					QualifiedName qName = (QualifiedName) expression;
					return process(qName.getQualifier()) + "." + process(qName.getName());
				}
			} else if (expression instanceof NullLiteral) {
				return expression.toString();
				
			} else if (expression instanceof NumberLiteral) {
				return expression.toString();
				
			} else if (expression instanceof ParenthesizedExpression) {
				ParenthesizedExpression pe = (ParenthesizedExpression) expression;
				return "(" + process(pe.getExpression()) + ")";
				
			} else if (expression instanceof PostfixExpression) {
				PostfixExpression pe = (PostfixExpression) expression;
				return process(pe.getOperand()) + pe.getOperator().toString();

			} else if (expression instanceof PrefixExpression) {
				PrefixExpression pe = (PrefixExpression) expression;
				return pe.getOperator().toString() + process(pe.getOperand());

			} else if (expression instanceof StringLiteral) {
				return expression.toString();

			} else if (expression instanceof SuperFieldAccess) {
				SuperFieldAccess sfa = (SuperFieldAccess) expression;
				StringBuffer string = new StringBuffer();
				if(sfa.getQualifier() != null) {
					string.append(process(sfa.getQualifier()) + ".");
				}
				string.append("super.");
				string.append(process(sfa.getName()));
				return string.toString();

			} else if (expression instanceof SuperMethodInvocation) {
				SuperMethodInvocation smi = (SuperMethodInvocation) expression;
				StringBuffer string = new StringBuffer();
				if(smi.getQualifier() != null) {
					string.append(process(smi.getQualifier()) + ".");
				}
				string.append("super." + process(smi.getName()) + "(");
				for(int i = 0; i < smi.arguments().size(); i++) {
					if(i > 0) {
						string.append(",");
					}
					string.append(process((Expression) smi.arguments().get(i)));
				}
				string.append(")");
				return string.toString();
				
			} else if (expression instanceof ThisExpression) {
				return expression.toString();
				
			} else if (expression instanceof TypeLiteral) {
				return expression.toString();
				
			} else {
				return expression.toString();
			}
			return null;
		}

	}
	
}
