/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package locator.aux.extractor.core.process.feature.item;

import locator.aux.extractor.core.ast.node.StmtType;
import locator.aux.extractor.core.ast.node.Use;
import locator.aux.extractor.core.ast.node.Variable;
import locator.aux.extractor.core.ast.node.Use.USETYPE;

/**
 * @author Jiajun
 *
 * Jul 24, 2018
 */
public class ParamDef extends Feature {

	private static ParamDef instance = null;
	private Use _use;
	private boolean _isParam;
	
	public static ParamDef getInstance() {
		if(instance == null) {
			instance = new ParamDef();
		}
		return instance;
	}
	
	protected ParamDef() {
		super("isParam");
	}
	
	private ParamDef(Use use) {
		this();
		_use = use;
	}

	@Override
	public Feature extractFeature(Use use) {
		ParamDef paramDef = new ParamDef(use);
		for(Use u : use.getVariableDefine().getUseSet()) {
			if(u.getUseType() == USETYPE.DEFINE) {
				paramDef._isParam = u.getStmtType() == StmtType.METHODECL;
			}
		}
		return paramDef;
	}

	@Override
	public String getStringFormat() {
		return _isParam ? "1" : "0";
	}
	

	@Override
	public String extractFeature(Variable variable, int line) {
		boolean isParam = false;
		for(Use use : variable.getUseSet()) {
			if(use.getUseType() == USETYPE.DEFINE) {
				isParam = use.getStmtType() == StmtType.METHODECL;
				break;
			}
		}
		return isParam ? "1" : "0";
	}
	
}
