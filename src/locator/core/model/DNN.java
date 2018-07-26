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
public class DNN extends MLModel {

	public final static String NAME = "dnn";
	
	public DNN() {
		super(DNN.NAME);
		__name__ = "@DNN ";
	}

	@Override
	public boolean modelExist(Subject subject) {
		File varModel = new File(_modelPath + "/" + subject.getName() + "/" + subject.getNameAndId() + "/var");
		File exprModel = new File(_modelPath + "/" + subject.getName() + "/" + subject.getNameAndId() + "/expr");
		if (varModel.exists() && exprModel.exists()) {
			LevelLogger.info("Models are already exist and will be used directly !");
			return true;
		}
		return false;
	}

}
