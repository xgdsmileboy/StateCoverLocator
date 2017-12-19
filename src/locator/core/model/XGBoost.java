/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.model;

import java.io.File;

import edu.pku.sei.conditon.simple.FeatureGenerator;
import locator.common.config.Constant;
import locator.common.java.Subject;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.common.util.Utils;

/**
 * @author Jiajun
 * @date Dec 19, 2017
 */
public class XGBoost extends Model {

	public XGBoost() {
		super(Constant.STR_ML_HOME + "/model", Constant.STR_ML_EXP_OUT_FILE_PATH,
				Constant.STR_ML_PREDICT_EXP_PATH);
//		super(Constant.STR_ML_HOME + "/model/xgboost", Constant.STR_ML_EXP_OUT_FILE_PATH + "/xgboost",
//				Constant.STR_ML_PREDICT_EXP_PATH + "/xgboost");
		__name__ = "@XGBoost ";
	}

	@Override
	public void trainModel(Subject subject) {
		File varModel = new File(_modelPath + subject.getName() + "_" + subject.getId() + "_" + Constant.TRAINING_MODEL
				+ ".var_model.pkl");
		File exprModel = new File(_modelPath + subject.getName() + "_" + subject.getId() + "_" + Constant.TRAINING_MODEL
				+ ".expr_model.pkl");
		// check existing models
		if (varModel.exists() && exprModel.exists()) {
			LevelLogger.info("Models are already exist and will be used directly !");
			return;
		}
		// get train features
		String srcPath = subject.getHome() + subject.getSsrc();
		String outPath = _outPath + "/" + subject.getName() + "/" + subject.getName() + "_" + subject.getId();

		// create necessary directories
		Utils.pathGuarantee(outPath + "/var", outPath + "/expr", outPath + "/cluster", outPath + "/pred",
				subject.getPredictResultDir());

		// generate features for trainning
		String targetVarPath = outPath + "/var/" + subject.getName() + "_" + subject.getId() + ".var.csv";
		String targetExprPath = outPath + "/expr/" + subject.getName() + "_" + subject.getId() + ".expr.csv";
		FeatureGenerator.generateTrainFeature(srcPath, targetVarPath, targetExprPath);

		// train model
		try {
			LevelLogger.info(">>>>>> Begin Trainning ...");
			ExecuteCommand.executeTrain(subject);
			LevelLogger.info(">>>>>> End Trainning !");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
