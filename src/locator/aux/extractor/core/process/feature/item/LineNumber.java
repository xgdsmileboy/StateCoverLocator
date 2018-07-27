/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package locator.aux.extractor.core.process.feature.item;

import locator.aux.extractor.core.ast.node.Use;
import locator.aux.extractor.core.ast.node.Variable;

/**
 * @author Jiajun
 *
 * Jul 24, 2018
 */
public class LineNumber extends Feature {

	private static LineNumber instance = null;
	private Use _use;
	
	protected LineNumber() {
		super("Line");
	}
	
	protected LineNumber(Use use) {
		super("Line");
		_use = use;
	}
	
	public static LineNumber getInstance() {
		if(instance == null) {
			instance = new LineNumber();
		}
		return instance;
	}

	@Override
	public Feature extractFeature(Use use) {
		LineNumber lineNumber = new LineNumber(use);
		return lineNumber;
	}
	
	@Override
	public String getStringFormat() {
		return String.valueOf(_use.getLineNumber());
	}
	

	@Override
	public String extractFeature(Variable variable, int line) {
		return String.valueOf(line);
	}

}
