/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor.feature;

import org.eclipse.jdt.core.dom.Type;

/**
 * @author Jiajun
 * @date May 12, 2017
 */
public class FeatureEntry {

	public static enum AssignType {
		/*****
		 * here list some assignment types
		 *****************************************/
		// assign field value to current variable
		FIELD_AS("FIELD_AS"),
		// assign parameter to current variable
		PARAM_AS("PARAM_AS"),
		// other unknown assignment type
		UNKNOWN_AS("UNKNOWN_AS"),

		/*****
		 * here list some expression types
		 *****************************************/
		// TODO : used in method invocation, concrete method name should be set
		METHOD_DCL("METHOD_DEL_NEED_SET"),
		// TODO : used in infix expression, concrete operator should be set
		INFIX_EXPR("INFIX_EXPR_NEED_SET"),
		//
		PREFIX_EXPR("PREFIX_EXPR"),
		//
		POSTFIX_EXPR("POSTFIX_EXPR"),
		//
		ARRAY_ACCESS("ARRAY_ACCESS"),
		//
		ARRAY_CREATION("ARRAY_CREATION"),
		//
		CLASS_INSTANCE_CREATION("CLASS_INSTANCE_CREATION"),
		//
		FIELD_ACCESS("FIELD_ACCESS"),
		//
		CAST("CAST"),
		// simple name
		NAME("NAME"),
		// null literal
		NULL("NULL"),
		// boolean/string/type/character literal
		LITERAL("LITERAL"),
		// TODO : number literal, concrete value should be set
		NUMBER("NUMBER_NEED_SET"),
		// other unknown types
		OTHERS("OTHERS");

		private String _value;

		private AssignType(String value) {
			this._value = value;
		}

		public String getValue() {
			return this._value;
		}

		public void setValue(String value) {
			this._value = value;
		}
	}

	public static enum UseType {
		NO_USE("NO_USE");

		private String _value;

		private UseType(String value) {
			this._value = value;
		}

		public String getValue() {
			return this._value;
		}

		public void setValue(String value) {
			this._value = value;
		}
	}

	// TODO : label id for current if condition, no use in current circumstance,
	// default -1
	private int _ifID = -1;
	// line number of current statement line
	private int _line = -1;
	// column number of current statement
	private int _column = -1;
	// current java file name, NOTE : contain ".java"
	private String _fileName = "";
	// current method name
	private String _methodName = "";
	// variable name
	private String _varName = "";
	// variable type
	private Type _type = null;
	// current variable appears in which type statements
	private AssignType _lastAssignType = AssignType.UNKNOWN_AS;
	// this variable is a parameter
	private boolean _isParam = false;
	// number of appearing in if condition statement for this variable in
	// current method
	private int _countAppearInCondition = 0;
	// TODO : how this variable is used in if body, no use in current
	// circumstance, use default NO_USE
	private UseType _useTypeInIFBody = UseType.NO_USE;
	// current statement is a if condition containing this variable
	private boolean _inIf = false;

	/**
	 * 
	 */
	public FeatureEntry(int line, int column, String fileName, String methodName, String varName, Type type,
			boolean isParam, int countAppearInCondition, boolean inIf) {
		_line = line;
		_column = column;
		_fileName = fileName;
		_methodName = methodName;
		_varName = varName;
		_type = type;
		_isParam = isParam;
		_countAppearInCondition = countAppearInCondition;
		_inIf = inIf;
	}

	public void set_ifID(int ifID) {
		this._ifID = ifID;
	}

	public void set_lastAssignType(AssignType lastAssignType) {
		this._lastAssignType = lastAssignType;
	}

	public void set_countAppearInCondition(int countAppearInCondition) {
		this._countAppearInCondition = countAppearInCondition;
	}

	public void set_useTypeInIFBody(UseType useTypeInIFBody) {
		this._useTypeInIFBody = useTypeInIFBody;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(_ifID);
		stringBuffer.append("\t");
		stringBuffer.append(_line);
		stringBuffer.append("\t");
		stringBuffer.append(_column);
		stringBuffer.append("\t");
		stringBuffer.append(_fileName);
		stringBuffer.append("\t");
		stringBuffer.append(_methodName);
		stringBuffer.append("\t");
		stringBuffer.append(_varName);
		stringBuffer.append("\t");
		stringBuffer.append(_type);
		stringBuffer.append("\t");
		stringBuffer.append(_lastAssignType);
		stringBuffer.append("\t");
		stringBuffer.append(_isParam);
		stringBuffer.append("\t");
		stringBuffer.append(_countAppearInCondition);
		stringBuffer.append("\t");
		stringBuffer.append(_useTypeInIFBody);
		stringBuffer.append("\t");
		stringBuffer.append(_inIf);
		return super.toString();
	}

}
