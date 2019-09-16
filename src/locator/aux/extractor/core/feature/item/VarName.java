/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package locator.aux.extractor.core.feature.item;

import locator.aux.extractor.core.parser.Use;
import locator.aux.extractor.core.parser.Variable;

/**
 * @author Jiajun
 *
 * Jul 24, 2018
 */
public class VarName extends Feature {

	private static VarName instance = null;
	private Use _use;
	private String _name;
	
	public static VarName getInstance() {
		if(instance == null) {
			instance = new VarName();
		}
		return instance;
	}
	
	protected VarName() {
		super("VarName");
	}
	
	private VarName(Use use) {
		this();
		_use = use;
	}

	@Override
	public Feature extractFeature(Use use) {
		VarName varName = new VarName(use);
		varName._name = use.getVariableDefine().getName();
		return varName;
	}

	@Override
	public String getStringFormat() {
		return _name;
	}
	

	@Override
	public String extractFeature(Variable variable, int line) {
		return variable.getName();
	}
	
}
