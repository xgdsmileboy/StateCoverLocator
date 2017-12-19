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
public class DNN extends Model {

	public DNN() {
//		super(Constant.STR_ML_HOME + "/model/dnn", Constant.STR_ML_EXP_OUT_FILE_PATH + "/dnn",
//				Constant.STR_ML_PREDICT_EXP_PATH + "/dnn");
		super(Constant.STR_ML_HOME + "/model", Constant.STR_ML_EXP_OUT_FILE_PATH,
				Constant.STR_ML_PREDICT_EXP_PATH);
		__name__ = "@DNN ";
	}

	@Override
	public void trainModel(Subject subject) {
		File varModel = new File(_modelPath + "/" + subject.getName() + "_" + subject.getId() + "/var");
		File exprModel = new File(_modelPath + "/" + subject.getName() + "_" + subject.getId() + "/expr");
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
