/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package locator.aux.extractor.core.process.feature.item;

import java.util.Set;

import locator.aux.extractor.core.ast.node.StmtType;
import locator.aux.extractor.core.ast.node.Use;
import locator.aux.extractor.core.ast.node.Variable;

/**
 * @author Jiajun
 *
 * Jul 24, 2018
 */
public class LoopUse extends Feature {

	private static LoopUse instance = null;
	private Use _use;
	private boolean _loopUse = false;
	
	public static LoopUse getInstance() {
		if(instance == null) {
			instance = new LoopUse();
		}
		return instance;
	}
	
	protected LoopUse() {
		super("LoopUse");
	}
	
	private LoopUse(Use use) {
		this();
		_use = use;
	}

	@Override
	public Feature extractFeature(Use use) {
		LoopUse loopUse = new LoopUse(use);
		// TODO: note that here is not accurate since variable used in FOR stmt may not used as condition 
		loopUse._loopUse = use.getStmtType() == StmtType.DO || use.getStmtType() == StmtType.WHILE
				|| use.getStmtType() == StmtType.FOR_COND || use.getStmtType() == StmtType.ENHANCEDFOR;
		return loopUse;
	}

	@Override
	public String getStringFormat() {
		return _loopUse ? "1" : "0";
	}
	

	@Override
	public String extractFeature(Variable variable, int line) {
		Set<Use> uses = variable.getUseSet();
		for(Use use : uses) {
			if(use.getLineNumber() == line) {
				StmtType type = use.getStmtType();
				boolean used = type == StmtType.DO || type == StmtType.WHILE || type == StmtType.FOR_COND;
				return used ? "1" : "0";
			}
		}
		return "0";
	}
	
}
