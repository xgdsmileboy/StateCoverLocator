/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.model;

import java.io.File;

import edu.pku.sei.conditon.simple.FeatureGenerator;
import locator.common.java.Subject;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.common.util.Utils;

/**
 * 
 * @author Jiajun
 *
 * Jul 5, 2018
 */
public class DNN extends Model {

	public final static String NAME = "dnn";
	
	public DNN() {
		super(DNN.NAME);
		__name__ = "@DNN ";
	}

	@Override
	public void trainModel(Subject subject) {
		if(prepare(subject)) {
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
	
	@Override
	public void evaluate(Subject subject) {
		if(prepare(subject)) {
			// evaluate model
			try {
				LevelLogger.info(">>>>>> Begin Evaluating ...");
				ExecuteCommand.executeEvaluate(subject);
				LevelLogger.info(">>>>>> End Evaluating !");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private boolean prepare(Subject subject) {
		File varModel = new File(_modelPath + "/" + subject.getName() + "_" + subject.getId() + "/var");
		File exprModel = new File(_modelPath + "/" + subject.getName() + "_" + subject.getId() + "/expr");
		if (varModel.exists() && exprModel.exists()) {
			LevelLogger.info("Models are already exist and will be used directly !");
			return false;
		}
		// get train features
		String srcPath = subject.getHome() + subject.getSsrc();
		String outPath = _outPath + "/" + subject.getName() + "/" + subject.getNameAndId();

		// create necessary directories
		Utils.pathGuarantee(outPath + "/var", outPath + "/expr", outPath + "/cluster", outPath + "/pred",
				subject.getPredictResultDir());

		String targetVarPath = outPath + "/var/" + subject.getNameAndId() + ".var.csv";
		String targetExprPath = outPath + "/expr/" + subject.getNameAndId() + ".expr.csv";

		FeatureGenerator.generateTrainFeature(srcPath, targetVarPath, targetExprPath);
		return true;
	}

}
