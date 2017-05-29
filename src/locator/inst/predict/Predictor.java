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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.xml.internal.ws.message.StringHeader;

import jas.VisibilityAnnotationAttr;
import locator.common.config.Constant;
import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.inst.visitor.feature.ExprFilter;
import locator.inst.visitor.feature.FeatureEntry;
import polyglot.ast.Expr;

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
		Map<String, String> varTypeMap = new HashMap<>();
		// TODO : return conditions for left variable and right variables
		File varFile = new File(Constant.STR_ML_VAR_OUT_FILE_PATH + "/" + subject.getName() + "/" + subject.getName()
				+ "_" + subject.getId() + "/pred/" + subject.getName() + "_" + subject.getId() + ".var.csv");
		JavaFile.writeStringToFile(varFile, Constant.FEATURE_VAR_HEADER);
		for (String string : varFeatures) {
			// TODO : for debug
			System.out.println(string);
			String[] features = string.split("\t");
			String varName = features[Constant.FEATURE_VAR_NAME_INDEX];
			String varType = features[Constant.FEATURE_VAR_TYPE_INDEX];
			varTypeMap.put(varName, varType);
			JavaFile.writeStringToFile(varFile, string + "\n", true);
		}

		File expFile = new File(Constant.STR_ML_EXP_OUT_FILE_PATH + "/" + subject.getName() + "/" + subject.getName()
				+ "_" + subject.getId() + "/pred/" + subject.getName() + "_" + subject.getId() + ".expr.csv");
		JavaFile.writeStringToFile(expFile, Constant.FEATURE_EXPR_HEADER);
		for (String string : expFeatures) {
			// TODO : for debug
			System.out.println(string);
			JavaFile.writeStringToFile(expFile, string + "\n", true);
		}

		try {
			ExecuteCommand.executePredict(subject);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		File rslFile = new File(Constant.STR_ML_PREDICT_EXP_PATH + "/" + subject.getName() + "/" + subject.getName()
				+ "_" + subject.getId() + "/" + subject.getName() + "_" + subject.getId() + ".joint.csv");
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(rslFile));
		} catch (FileNotFoundException e) {
			LevelLogger.warn(__name__ + "#predict file not found : " + rslFile.getAbsolutePath());
			return conditions;
		}
		Set<String> rightConditions = new HashSet<>();
		String line = null;
		try {
			// filter title
			if (bReader.readLine() != null) {
				// parse predict result "8 u $ == null 0.8319194802"
				while ((line = bReader.readLine()) != null) {
					String[] columns = line.split("\t");
					if (columns.length < 4) {
						LevelLogger.error(__name__ + "@predict Parse predict result failed : " + line);
						continue;
					}
					String varName = columns[1];
					String condition = columns[2].replace("$", varName);
					String varType = varTypeMap.get(varName);
					if(ExprFilter.isLegalExpr(varType, varName, condition)){
						rightConditions.add(condition);
					} else {
						LevelLogger.info("Filter illegal predicates : " + varName + "(" + varType + ")" + " -> " + condition);
					}
					
				}
			}
			bReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ExecuteCommand.deleteGivenFile(varFile.getAbsolutePath());
		ExecuteCommand.deleteGivenFile(expFile.getAbsolutePath());
		ExecuteCommand.deleteGivenFile(rslFile.getAbsolutePath());
		conditions.setSecond(rightConditions);
		return conditions;
	}

}
