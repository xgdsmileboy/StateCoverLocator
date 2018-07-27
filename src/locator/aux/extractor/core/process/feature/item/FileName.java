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
public class FileName extends Feature {

	private static FileName instance = null;
	private Use _use;
	private String _name;
	
	public static FileName getInstance() {
		if(instance == null) {
			instance = new FileName();
		}
		return instance;
	}
	
	protected FileName() {
		super("FileName");
	}
	
	private FileName(Use use) {
		this();
		_use = use;
	}
	
	
	@Override
	public Feature extractFeature(Use use) {
		FileName fileName = new FileName(use);
		String file = use.getParentBlock().getSourceFile();
		fileName._name = file.substring(file.lastIndexOf("/") + 1, file.length() - /*".java".length()*/ 5);
		return fileName;
	}

	@Override
	public String getStringFormat() {
		return _name;
	}

	@Override
	public String extractFeature(Variable variable, int line) {
		String file = variable.getFile();
		return file.substring(file.lastIndexOf("/") + 1, file.length() - /*".java".length()*/ 5);
	}

}
