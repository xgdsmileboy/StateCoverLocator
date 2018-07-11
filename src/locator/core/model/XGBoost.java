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
import locator.common.util.Utils;
import locator.inst.visitor.feature.FeatureExtraction;

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

	@Override
	public boolean prepare(Subject subject) {
		// get train features
		String outPath = _outPath + "/" + subject.getName() + "/" + subject.getName() + "_" + subject.getId();

		// create necessary directories
		Utils.pathGuarantee(outPath + "/var", outPath + "/expr", outPath + "/cluster", outPath + "/pred",
				subject.getPredictResultDir());

		// generate features for trainning
		String targetVarPath = outPath + "/var/" + subject.getName() + "_" + subject.getId() + ".var.csv";
		String targetExprPath = outPath + "/expr/" + subject.getName() + "_" + subject.getId() + ".expr.csv";
		FeatureExtraction.generateTrainFeatures(subject, targetVarPath, targetExprPath);
		return true;
	}
	
}
