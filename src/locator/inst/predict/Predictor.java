/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.predict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import locator.common.config.Constant;
import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.inst.visitor.feature.FeatureEntry;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class Predictor {

	private final static String __name__ = "@Predictor ";

	public static List<String> predict(FeatureEntry featureEntry) {
		List<String> insertedStatement = new ArrayList<>();

		return insertedStatement;
	}

	/**
	 * predict expressions based on the given features,
	 * 
	 * @param subject
	 *            : current predict subject
	 * @param varFeatures
	 *            : features for variable predict
	 * @param expFeatures
	 *            : features for expression predict
	 * @return a pair of conditions, the first set contains all conditions for
	 *         left variable [currently, not use] the second set contains all
	 *         conditions for right variables
	 */
	public static Pair<Set<String>, Set<String>> predict(Subject subject, List<String> varFeatures,
			List<String> expFeatures) {
		Pair<Set<String>, Set<String>> conditions = new Pair<>();
		// TODO : return conditions for left variable and right variables
		File file = new File(Constant.STR_ML_VAR_OUT_FILE_PATH + Constant.PATH_SEPARATOR + subject.getName() + "_"
				+ subject.getId() + ".var.csv");
		String varTitle = "id	line	column	filename	methodname	varname	vartype	lastassign	isparam	incondnum	bodyuse	if\n";
		JavaFile.writeStringToFile(file, varTitle);
		for (String string : varFeatures) {
			JavaFile.writeStringToFile(file, string + "\n", true);
		}

		file = new File(Constant.STR_ML_EXP_OUT_FILE_PATH + Constant.PATH_SEPARATOR + subject.getName() + "_"
				+ subject.getId() + ".expr.csv");
		String expTitle = "id	line	column	filename	methodname	varname	vartype	else	return	right\n";
		JavaFile.writeStringToFile(file, expTitle);
		for (String string : expFeatures) {
			JavaFile.writeStringToFile(file, string + "\n", true);
		}

		try {
			ExecuteCommand.executePredict();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		file = new File(Constant.STR_ML_PREDICT_EXP_PATH + Constant.PATH_SEPARATOR + subject.getName() + "_"
				+ subject.getId() + ".joint.csv");
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			LevelLogger.warn(__name__ + "#predict file not found : " + file.getAbsolutePath());
			return conditions;
		}
		Set<String> rightConditions = new HashSet<>();
		String line = null;
		try {
			//filter title
			if(bReader.readLine() != null){
				//parse predict result "8	u	$ == null	0.8319194802"
				while ((line = bReader.readLine()) != null) {
					String[] columns = line.split("\t");
					if(columns.length < 4){
						LevelLogger.error(__name__ + "@predict Parse predict result failed : " + line);
						continue;
					}
					String varName = columns[1];
					String condition = columns[2].replace("$", varName);
					rightConditions.add(condition);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		conditions.setSecond(rightConditions);
		return conditions;
	}

}
