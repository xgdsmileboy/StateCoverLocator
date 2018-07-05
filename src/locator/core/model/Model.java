/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.model;

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
import locator.core.run.path.LineInfo;
import locator.inst.visitor.feature.NewExprFilter;

/**
 * 
 * @author Jiajun
 *
 * Jul 5, 2018
 */
public abstract class Model {

	protected String _modelPath = Constant.STR_ML_HOME + "/model/";
	protected String _outPath = Constant.STR_ML_OUT_FILE_PATH;
	protected String _inPath = Constant.STR_ML_PREDICT_EXP_PATH;
	protected static Set<String> _uniqueFeatures = new HashSet<>();
	protected String __name__ = "@Model ";
	protected String _modelName = "model";

	protected Model(String modelName) {
		_modelName = modelName;
		_modelPath = Constant.STR_ML_HOME + "/" + modelName;
		_outPath = Constant.STR_ML_OUT_FILE_PATH + "/" + modelName;
		_inPath = Constant.STR_ML_PREDICT_EXP_PATH + "/" + modelName;
	}
	
	public String getDataOutPath() {
		return _outPath;
	}
	
	public String getPredictRsltPath() {
		return _inPath;
	}
	
	
	public abstract void trainModel(Subject subject);
	public abstract void evaluate(Subject subject);
	
	/**
	 * 
	 * @param subject
	 *            : current predict subject
	 * @param varFeatures:
	 *            features for variable predict
	 * @param exprFeatures
	 *            : features for expression predict
	 * @param lineInfoMapping
	 *            : record the info for each line of source code, formatted as
	 *            <xx.java#145, {line, relJavaPath, clazz}>
	 * @return <fileName, <variable, [<predicate, probability>]>>
	 */
	public Map<String, Map<String, List<Pair<String, String>>>> predict(Subject subject, List<String> varFeatures,
			List<String> exprFeatures, Map<String, LineInfo> lineInfoMapping) {
		String currentClassName = null;
		// TODO : return conditions for left variable and right variables
		File varFile = new File(subject.getVarFeatureOutputPath());
		JavaFile.writeStringToFile(varFile, Constant.FEATURE_VAR_HEADER);
		for (String string : varFeatures) {
			// filter duplicated features
			if (_uniqueFeatures.contains(string)) {
				continue;
			}
			_uniqueFeatures.add(string);
			// TODO : for debug
			LevelLogger.info(string);
			if (currentClassName == null) {
				String[] features = string.split("\t");
				int index = features[Constant.FEATURE_FILE_NAME_INDEX].length();
				// name.java -> name
				currentClassName = features[Constant.FEATURE_FILE_NAME_INDEX].substring(0, index - 5);
			}
			JavaFile.writeStringToFile(varFile, string + "\n", true);
		}

		File expFile = new File(subject.getExprFeatureOutputPath());
		JavaFile.writeStringToFile(expFile, Constant.FEATURE_EXPR_HEADER);
		for (String string : exprFeatures) {
			// filter duplicated features
			if (_uniqueFeatures.contains(string)) {
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

		Map<String, Map<String, List<Pair<String, String>>>> rightConditions = new HashMap<>();
		File rslFile = new File(subject.getPredicResultPath());
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(rslFile));
		} catch (FileNotFoundException e) {
			LevelLogger.warn(__name__ + "#predict file not found : " + rslFile.getAbsolutePath());
			return rightConditions;
		}

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
					String key = columns[0];
					String varName = columns[1];
					String condition = columns[2].replace("$", varName);
					String varType = lineInfoMapping.get(key).getLegalVariableType(varName);
					String prob = columns[3];
					String newCond = NewExprFilter.filter(varType, varName, condition, lineInfoMapping.get(key),
							currentClassName);
					if (newCond != null) {
						Map<String, List<Pair<String, String>>> linePreds = rightConditions.get(key);
						if (linePreds == null) {
							linePreds = new HashMap<>();
						}
						List<Pair<String, String>> preds = linePreds.get(varName);
						if (preds == null) {
							preds = new ArrayList<>();
						}
						Pair<String, String> pair = new Pair<String, String>(newCond, prob);
						preds.add(pair);
						linePreds.put(varName, preds);
						rightConditions.put(key, linePreds);

					} else {
						LevelLogger.info(
								"Filter illegal predicates : " + varName + "(" + varType + ")" + " -> " + condition);
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

		return rightConditions;
	}

}
