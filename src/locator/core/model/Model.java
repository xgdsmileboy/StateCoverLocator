/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import locator.common.config.Constant;
import locator.common.java.Subject;
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

	
	public String getVarFeatureOutputPath(Subject subject) {
		String file = _outPath + "/" + subject.getName() + "/" + subject.getNameAndId() + "/pred/" + subject.getNameAndId() + ".var.csv";
		return file;
	}

	public String getExprFeatureOutputPath(Subject subject) {
		String file = _outPath + "/" + subject.getName() + "/" + subject.getNameAndId() + "/pred/" + subject.getNameAndId() + ".expr.csv";
		return file;
	}

	public String getPredicResultPath(Subject subject) {
		String file = _inPath + "/" + subject.getName() + "/" + subject.getNameAndId() + "/" + subject.getNameAndId() + ".joint.csv";
		return file;
	}
	
	public String getPredictResultDir(Subject subject) {
		String file = _inPath + "/" + subject.getName() + "/" + subject.getNameAndId();
		return file;
	}
	
	
}
