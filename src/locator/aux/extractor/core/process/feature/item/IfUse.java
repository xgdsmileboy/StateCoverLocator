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

/**
 * @author Jiajun
 *
 * Jul 24, 2018
 */
public class IfUse extends Feature {

	
	private static IfUse instance = null;
	private Use _use;
	private boolean _inIf = false;
	
	public static IfUse getInstance() {
		if(instance == null) {
			instance = new IfUse();
		}
		return instance;
	}
	
	protected IfUse() {
		super("IfUse");
	}

	private IfUse(Use use) {
		super("IfUse");
		_use = use;
	}
	
	@Override
	public Feature extractFeature(Use use) {
		IfUse ifUse = new IfUse(use);
		ifUse._inIf = use.getStmtType() == StmtType.IF;
		return ifUse;
	}

	@Override
	public String getStringFormat() {
		return _inIf ? "1" : "0";
	}


	@Override
	public String extractFeature(Variable variable, int line) {
		return "?";
	}

}
