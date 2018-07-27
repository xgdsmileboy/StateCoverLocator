/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package locator.aux.extractor.core.feature.item;

import locator.aux.extractor.core.parser.StmtType;
import locator.aux.extractor.core.parser.Use;
import locator.aux.extractor.core.parser.Variable;

/**
 * @author Jiajun
 *
 * Jul 25, 2018
 */
public class PartialExpr extends Feature {

	private static PartialExpr instance = null;
	private Use _use;
	private String _partialExpr = null;
	
	
	public static PartialExpr getInstance() {
		if(instance == null) {
			instance = new PartialExpr();
		}
		return instance;
	}
	
	protected PartialExpr() {
		super("PartialExpr");
	}
	
	private PartialExpr(Use use) {
		this();
		_use = use;
	}

	@Override
	public Feature extractFeature(Use use) {
		PartialExpr partialExpr = new PartialExpr(use);
		StmtType stmtType = use.getStmtType();
		// used in a condition expression
		if (stmtType == StmtType.IF || stmtType == StmtType.FOR_COND || stmtType == StmtType.WHILE
				|| stmtType == StmtType.DO) {
			partialExpr._partialExpr = use.getExprWithHole();
		}

		return partialExpr;
	}

	@Override
	public String getStringFormat() {
		return _partialExpr;
	}
	
	@Override
	public String extractFeature(Variable variable, int line) {
		return "?";
	}
	
}
