/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package locator.aux.extractor.core.feature.item;

import org.eclipse.jdt.core.dom.Type;

import locator.aux.extractor.core.parser.Use;
import locator.aux.extractor.core.parser.Variable;

/**
 * @author Jiajun
 *
 * Jul 24, 2018
 */
public class VarType extends Feature {

	private static VarType instance = null;
	private Use _use;
	private Type _type;
	
	public static VarType getInstance() {
		if(instance == null) {
			instance = new VarType();
		}
		return instance;
	}
	
	protected VarType() {
		super("VarType");
	}
	
	private VarType(Use use) {
		this();
		_use = use;
	}

	@Override
	public Feature extractFeature(Use use) {
		VarType varType = new VarType(use);
		varType._type = use.getVariableDefine().getType();
		return varType;
	}

	@Override
	public String getStringFormat() {
		return String.valueOf(_type);
	}
	

	@Override
	public String extractFeature(Variable variable, int line) {
		return String.valueOf(variable.getType());
	}
}
