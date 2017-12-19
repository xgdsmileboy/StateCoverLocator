/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.model;

import java.util.List;
import java.util.Map;

import locator.common.config.Constant;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.core.run.path.LineInfo;

/**
 * @author Jiajun
 * @date Dec 19, 2017
 */
public class L2S extends Model {

	public L2S() {
		super(Constant.STR_ML_HOME + "/model/l2s", Constant.STR_ML_EXP_OUT_FILE_PATH + "/l2s",
				Constant.STR_ML_PREDICT_EXP_PATH + "/l2s");
		__name__ = "@L2S ";
	}

	@Override
	public void trainModel(Subject subject) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Map<String, List<Pair<String, String>>>> predict(Subject subject, List<String> varFeaturs,
			List<String> exprFeatures, Map<String, LineInfo> lineInforMapping) {
		// TODO Auto-generated method stub
		return null;
	}

}
