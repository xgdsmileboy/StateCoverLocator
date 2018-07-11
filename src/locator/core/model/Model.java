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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.JavaFile;
import locator.common.java.Subject;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;
import locator.common.util.Pair;
import locator.core.LineInfo;
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
	protected String _predicates_backup_file;
	protected static Set<String> _uniqueFeatures = new HashSet<>();
	protected String __name__ = "@Model ";
	protected String _modelName = "model";

	protected Model(String modelName, String predicateBackupFile) {
		_modelName = modelName;
		_predicates_backup_file = predicateBackupFile;
		_modelPath = Constant.STR_ML_HOME + "/" + modelName;
		_outPath = Constant.STR_ML_OUT_FILE_PATH + "/" + modelName;
		_inPath = Constant.STR_ML_PREDICT_EXP_PATH + "/" + modelName;
	}
	
	public String getModelName() {
		return _modelName;
	}
	
	public String getDataOutPath() {
		return _outPath;
	}
	
	public String getPredictRsltPath() {
		return _inPath;
	}
	
	
	public abstract boolean modelExist(Subject subject);

	public abstract boolean prepare(Subject subject);

	public abstract boolean trainModel(Subject subject);

	public abstract boolean evaluate(Subject subject);

	public abstract boolean instrumentPredicates(
			Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates, boolean useSober);

	public abstract Map<String, Map<Integer, List<Pair<String, String>>>> getAllPredicates(Subject subject,
			Set<String> allStatements, boolean useSober);

	protected Map<String, List<Integer>> mapLocations2File(Subject subject, Set<String> allStatements) {
		String srcPath = subject.getHome() + subject.getSsrc();
		Map<String, List<Integer>> file2LocationList = new HashMap<>();
		int allStmtCount = allStatements.size();
		int currentStmtCount = 1;
		for (String stmt : allStatements) {
			LevelLogger.debug("======================== [" + currentStmtCount + "/" + allStmtCount
					+ "] statements (statistical debugging) =================.");
			currentStmtCount++;
			String[] stmtInfo = stmt.split("#");
			if (stmtInfo.length != 2) {
				LevelLogger.error(__name__ + "#mapLocations2File statement parse error : " + stmt);
				System.exit(0);
			}
			Integer methodID = Integer.valueOf(stmtInfo[0]);
			int line = Integer.parseInt(stmtInfo[1]);
			if (line == 2317) {
				LevelLogger.debug(__name__ + "#mapLocations2File : exist");
			}
			String methodString = Identifier.getMessage(methodID);
			LevelLogger.debug("Current statement  : **" + methodString + "#" + line + "**");
			String[] methodInfo = methodString.split("#");
			if (methodInfo.length < 4) {
				LevelLogger.error(__name__ + "#mapLocations2File method info parse error : " + methodString);
				System.exit(0);
			}
			String clazz = methodInfo[0].replace(".", Constant.PATH_SEPARATOR);
			int index = clazz.indexOf("$");
			if (index > 0) {
				clazz = clazz.substring(0, index);
			}
			String relJavaPath = clazz + ".java";

			String fileName = srcPath + Constant.PATH_SEPARATOR + relJavaPath;

			List<Integer> list = file2LocationList.get(relJavaPath);
			if (list == null) {
				File file = new File(fileName);
				if (!file.exists()) {
					LevelLogger.error("Cannot find file : " + fileName);
					continue;
				}
				list = new LinkedList<>();
			}
			list.add(line);
			file2LocationList.put(relJavaPath, list);
		}
		return file2LocationList;
	}
	
	
	protected void printPredicateInfo(Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates,
			Subject subject) {
		LevelLogger.debug("\n------------------------begin predicate info------------------------\n");

		StringBuffer contents = new StringBuffer();
		for (Entry<String, Map<Integer, List<Pair<String, String>>>> entry : file2Line2Predicates.entrySet()) {
			LevelLogger.debug("FILE NAME : " + entry.getKey());
			for (Entry<Integer, List<Pair<String, String>>> line2Preds : entry.getValue().entrySet()) {
				LevelLogger.debug("LINE : " + line2Preds.getKey());
				LevelLogger.debug("PREDICATES : ");
				for (Pair<String, String> pair : line2Preds.getValue()) {
					LevelLogger.debug(pair.getFirst() + ",");
					contents.append(entry.getKey() + "\t" + line2Preds.getKey() + "\t" + pair.getFirst() + "\t"
							+ pair.getSecond());
					contents.append("\n");
				}
				LevelLogger.debug("\n");
			}
		}
		LevelLogger.debug("\n------------------------end predicate info------------------------\n");
		String outputFile = subject.getCoverageInfoPath() + "/" + _predicates_backup_file;
		JavaFile.writeStringToFile(outputFile, contents.toString(), true);
	}

	protected Map<String, Map<Integer, List<Pair<String, String>>>> recoverPredicates(Subject subject) {
		Map<String, Map<Integer, List<Pair<String, String>>>> file2Line2Predicates = new HashMap<>();
		String inputFile = subject.getCoverageInfoPath() + "/" + _predicates_backup_file;
		File file = new File(inputFile);
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		LevelLogger.info("Recover predicates from file.");
		List<String> contents = JavaFile.readFileToStringList(file);
		for (String content : contents) {
			if (contents.isEmpty()) {
				continue;
			}
			String parts[] = content.split("\t");
			// fix bug: predicates may contain anonymous class
			if (parts.length != 4) {
				continue;
			}
			Map<Integer, List<Pair<String, String>>> line2Predicates = file2Line2Predicates.get(parts[0]);
			if (line2Predicates == null) {
				line2Predicates = new HashMap<>();
				file2Line2Predicates.put(parts[0], line2Predicates);
			}
			List<Pair<String, String>> predicates = line2Predicates.get(Integer.valueOf(parts[1]));
			if (predicates == null) {
				predicates = new ArrayList<>();
				line2Predicates.put(Integer.valueOf(parts[1]), predicates);
			}
			predicates.add(new Pair<String, String>(parts[2], parts[3]));
		}
		return file2Line2Predicates;
	}
	
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
			ExecuteCommand.executePredict(subject, this);
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
