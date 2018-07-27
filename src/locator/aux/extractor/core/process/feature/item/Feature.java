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
 * @date Jul 7, 2018
 */
public abstract class Feature {

	protected String _fName = "Feature";
	
	protected Feature(String featureName) {
		_fName = featureName;
	}
	
	public String getName() {
		return _fName;
	}
	
	public abstract Feature extractFeature(Use use);
	public abstract String extractFeature(Variable variable, int line);
	public abstract String getStringFormat();
	
}
