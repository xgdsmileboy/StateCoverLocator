/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.model;

import java.io.File;

import locator.common.java.Subject;
import locator.common.util.LevelLogger;

/**
 * 
 * @author Jiajun
 *
 * Jul 5, 2018
 */
public class XGBoost extends MLModel {

	public final static String NAME = "xgboost";
	
	public XGBoost() {
		super(XGBoost.NAME);
		__name__ = "@XGBoost ";
	}
	
	@Override
	public boolean modelExist(Subject subject) {
		File varModel = new File(_modelPath + "/" + subject.getNameAndId() + "_" + _modelName + ".var_model.pkl");
		File exprModel = new File(_modelPath + "/" + subject.getNameAndId() + "_" + _modelName + ".expr_model.pkl");
		// check existing models
		if (varModel.exists() && exprModel.exists()) {
			LevelLogger.info("Models are already exist and will be used directly !");
			return false;
		}
		return true;
	}
	
}
