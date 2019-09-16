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
public class ColumnNumber extends Feature {

	private static ColumnNumber instance = null;
	private Use _use;
	
	public static ColumnNumber getInstance() {
		if(instance == null) {
			instance = new ColumnNumber();
		}
		return instance;
	}
	
	protected ColumnNumber() {
		super("Column");
	}
	
	private ColumnNumber(Use use) {
		this();
		_use = use;
	}

	@Override
	public Feature extractFeature(Use use) {
		ColumnNumber columnNumber = new ColumnNumber(use);
		return columnNumber;
	}

	@Override
	public String getStringFormat() {
		return String.valueOf(_use.getColumnNumber());
	}

	@Override
	public String extractFeature(Variable variable, int line) {
		return "?";
	}

}
