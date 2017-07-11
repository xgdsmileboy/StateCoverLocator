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

import locator.common.config.Constant;
import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.inst.visitor.feature.ExprFilter;
import locator.inst.visitor.feature.FeatureEntry;
import locator.inst.visitor.feature.NewExprFilter;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class Predictor {

	private final static String __name__ = "@Predictor ";
	
	private static Set<String> _uniqueFeatures = new HashSet<>();

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
	public static Pair<Map<String, List<Pair<String, String>>>, Map<String, List<Pair<String, String>>>> predict(Subject subject, List<String> varFeatures,
			List<String> expFeatures, Map<String, String> allLegalVariablesMap) {
		Pair<Map<String, List<Pair<String, String>>>, Map<String, List<Pair<String, String>>>> conditions = new Pair<>();
		String currentClassName = null;
		// TODO : return conditions for left variable and right variables
		File varFile = new File(subject.getVarFeatureOutputPath());
		JavaFile.writeStringToFile(varFile, Constant.FEATURE_VAR_HEADER);
		for (String string : varFeatures) {
			// filter duplicated features
			if(_uniqueFeatures.contains(string)){
				continue;
			}
			_uniqueFeatures.add(string);
			// TODO : for debug
			LevelLogger.info(string);
			if(currentClassName == null){
				String[] features = string.split("\t");
				int index = features[Constant.FEATURE_FILE_NAME_INDEX].length();
				// name.java -> name
				currentClassName = features[Constant.FEATURE_FILE_NAME_INDEX].substring(0, index - 5);
			}
			JavaFile.writeStringToFile(varFile, string + "\n", true);
		}

		File expFile = new File(subject.getExprFeatureOutputPath());
		JavaFile.writeStringToFile(expFile, Constant.FEATURE_EXPR_HEADER);
		for (String string : expFeatures) {
			// filter duplicated features
			if(_uniqueFeatures.contains(string)){
				continue;
			}
			_uniqueFeatures.add(string);
			// TODO : for debug
			LevelLogger.info(string);
			JavaFile.writeStringToFile(expFile, string + "\n", true);
		}

		try {
			ExecuteCommand.executePredict(subject);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		File rslFile = new File(subject.getPredicResultPath());
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(rslFile));
		} catch (FileNotFoundException e) {
			LevelLogger.warn(__name__ + "#predict file not found : " + rslFile.getAbsolutePath());
			return conditions;
		}
		Map<String, List<Pair<String, String>>> rightConditions = new HashMap<>();
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
					if(condition.length() < 1){
						LevelLogger.error(__name__ + "@predict illegal predicate : " + line);
						continue;
					}
					String varType = allLegalVariablesMap.get(varName);
					String prob = columns[3];
					String newCond = NewExprFilter.filter(varType, varName, condition, allLegalVariablesMap, currentClassName);
					if(newCond != null && newCond.length() > 1){
						List<Pair<String, String>> preds = rightConditions.get(varName);
						if(preds == null){
							preds = new ArrayList<>();
						}
						Pair<String, String> pair = new Pair<String, String>(newCond, prob);
						preds.add(pair);
						rightConditions.put(varName, preds);
						
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
