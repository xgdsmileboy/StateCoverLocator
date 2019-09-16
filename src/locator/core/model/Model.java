/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.model;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.Subject;
import locator.common.util.LevelLogger;
import locator.common.util.Pair;

/**
 * 
 * @author Jiajun
 *
 * Jul 5, 2018
 */
public abstract class Model {

	protected String _modelPath;
	protected String _outPath;
	protected String _inPath;
	protected String _predicates_backup_file;
	protected String __name__ = "@Model ";
	protected String _modelName = "model";

	protected Model(String modelName, String predicateBackupFile) {
		_modelName = modelName;
		_predicates_backup_file = predicateBackupFile;
		_modelPath = Constant.STR_ML_HOME + "/model/" + modelName;
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

	
	public String getVarFeatureOutputFile(Subject subject) {
		String file = _outPath + "/" + subject.getName() + "/" + subject.getNameAndId() + "/pred/" + subject.getNameAndId() + ".var.csv";
		return file;
	}

	public String getExprFeatureOutputFile(Subject subject) {
		String file = _outPath + "/" + subject.getName() + "/" + subject.getNameAndId() + "/pred/" + subject.getNameAndId() + ".expr.csv";
		return file;
	}
	
	public String getClassifyFeatureOutputFile(Subject subject) {
		String file = _outPath + "/" + subject.getName() + "/" + subject.getNameAndId() + "/pred/" + subject.getNameAndId() + ".csv";
		return file;
	}

	public String getPredictResultFile(Subject subject) {
		String file = _inPath + "/" + subject.getName() + "/" + subject.getNameAndId() + "/" + subject.getNameAndId() + ".joint.csv";
		return file;
	}
	
	public String getPredictResultPath(Subject subject) {
		String file = _inPath + "/" + subject.getName() + "/" + subject.getNameAndId();
		return file;
	}
	
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
	
}
