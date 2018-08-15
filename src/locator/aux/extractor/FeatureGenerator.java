/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.aux.extractor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import locator.aux.extractor.core.feature.ClassifierFeature;
import locator.aux.extractor.core.feature.ExprFeature;
import locator.aux.extractor.core.feature.VarFeature;
import locator.aux.extractor.core.feature.item.FileName;
import locator.aux.extractor.core.feature.item.VarName;
import locator.aux.extractor.core.feature.item.VarType;
import locator.aux.extractor.core.parser.Analyzer;
import locator.aux.extractor.core.parser.BasicBlock;
import locator.aux.extractor.core.parser.Use;
import locator.aux.extractor.core.parser.Variable;
import locator.common.java.JavaFile;
import locator.core.LineInfo;
import locator.core.alg.Algorithm;

/**
 * This class is an interface to access the feature generating process, which
 * can be adapt to new APIs easily
 * 
 * @author Jiajun
 * @date Jul 7, 2018
 */
public class FeatureGenerator {

	/**
	 * Given the source code base path and target file name, this method generate
	 * all variable features for training, and write the result to the file
	 * {@code tarFile}
	 * 
	 * @param baseDir
	 *            : base directory of source code
	 * @param tarFile
	 *            : target file name (absolute file path)
	 */
	public static void generateTrainVarFeatures(String baseDir, String tarFile) {
		List<String> files = JavaFile.ergodic(baseDir, new LinkedList<String>());
		StringBuffer featuresBuff = new StringBuffer();
		featuresBuff.append(VarFeature.getFeatureHeader());
		for (String file : files) {
			String relJavaFile = file.substring(baseDir.length() + 1);
			BasicBlock basicBlock = Analyzer.analyze(relJavaFile, baseDir);
			Set<Use> uses = basicBlock.recursivelyGetUses();
			for (Use use : uses) {
				featuresBuff.append('\n' + new VarFeature(use).toStringFormat());
			}
		}
		JavaFile.writeStringToFile(tarFile, featuresBuff.toString());
	}

	/**
	 * Given the source base path and target file name, this method generate all
	 * expression features for training, and write the result to the file
	 * {@code tarFile}
	 * 
	 * @param baseDir
	 *            : base directory of source code
	 * @param tarFile
	 *            : target file name (absolute file path)
	 */
	public static void generateTrainExprFeatures(String baseDir, String tarFile) {
		List<String> files = JavaFile.ergodic(baseDir, new LinkedList<String>());
		StringBuffer featuresBuff = new StringBuffer();
		featuresBuff.append(ExprFeature.getFeatureHeader());
		for (String file : files) {
			String relJavaFile = file.substring(baseDir.length() + 1);
			BasicBlock basicBlock = Analyzer.analyze(relJavaFile, baseDir);
			Set<Use> uses = basicBlock.recursivelyGetUses();
			for (Use use : uses) {
				String feature = new ExprFeature(use).toStringFormat();
				if (feature == null) {
					continue;
				}
				featuresBuff.append('\n' + feature);
			}
		}
		JavaFile.writeStringToFile(tarFile, featuresBuff.toString());
	}

	/**
	 * Generate a list of variable features for a given line of code with filtering
	 * set {@code varWhiteList}
	 * 
	 * @param baseDir
	 *            : base directory of source code
	 * @param relJavaFile
	 *            : relative java file path
	 * @param line
	 *            : line number of source code
	 * @param varWhiteList
	 *            : white list if legal variable names
	 * @return a list of features
	 */
	public static List<String> generateVarFeatureForLineWithFilter(String baseDir, String relJavaFile, int line,
			Set<String> varWhiteList) {
		List<String> features = new LinkedList<>();
		BasicBlock basicBlock = Analyzer.analyze(relJavaFile, baseDir);
		Set<Variable> variables = basicBlock.getAllValidVariables(line);
		for (Variable variable : variables) {
			if (varWhiteList.contains(variable.getName())) {
				features.add(VarFeature.extractFeature(variable, line));
			}
		}
		return features;
	}

	/**
	 * Generate a list of variable features for a given line of code
	 * 
	 * @param baseDir
	 *            : base directory of source code
	 * @param relJavaFile
	 *            : relative java file path
	 * @param line
	 *            : line number of source code
	 * @return a list of features
	 */
	public static List<String> generateVarFeatureForLine(String baseDir, String relJavaFile, int line) {
		List<String> features = new LinkedList<>();
		BasicBlock basicBlock = Analyzer.analyze(relJavaFile, baseDir);
		Set<Variable> variables = basicBlock.getAllValidVariables(line);
		for (Variable variable : variables) {
			features.add(VarFeature.extractFeature(variable, line));
		}
		return features;
	}

	/**
	 * Generate a list of expression features for a given line of code with the
	 * filtering of {@code varWhiteList}
	 * 
	 * @param baseDir
	 *            : base directory of source code
	 * @param relJavaFile
	 *            : relative java file path
	 * @param line
	 *            : line number of source code
	 * @param varWhiteList
	 *            : white list of legal variables to generate features
	 * @return a list of features
	 */
	public static List<String> generateExprFeatureForLineWithFilter(String baseDir, String relJavaFile, int line,
			Set<String> varWhiteList) {
		List<String> features = new LinkedList<>();
		BasicBlock basicBlock = Analyzer.analyze(relJavaFile, baseDir);
		Set<Variable> variables = basicBlock.getAllValidVariables(line);
		for (Variable variable : variables) {
			if (varWhiteList.contains(variable.getName())) {
				String feature = ExprFeature.extractFeature(variable, line);
				if (feature == null) {
					continue;
				}
				features.add(feature);
			}
		}
		return features;
	}

	/**
	 * Generate expression features for a given line of code
	 * 
	 * @param baseDir
	 *            : base directory of source code
	 * @param relJavaFile
	 *            : relative java file path
	 * @param line
	 *            : line number of source code
	 * @return a list of features
	 */
	public static List<String> generateExprFeatureForLine(String baseDir, String relJavaFile, int line) {
		List<String> features = new LinkedList<>();
		BasicBlock basicBlock = Analyzer.analyze(relJavaFile, baseDir);
		Set<Variable> variables = basicBlock.getAllValidVariables(line);
		for (Variable variable : variables) {
			String feature = ExprFeature.extractFeature(variable, line);
			if (feature == null) {
				continue;
			}
			features.add(feature);
		}
		return features;
	}

	private static final int varNameIndex = VarFeature.getFeatureIndex(VarName.class);
	private static final int varTypeIndex = VarFeature.getFeatureIndex(VarType.class);
	private static final int varFileNameIndex = VarFeature.getFeatureIndex(FileName.class);
	private static final int exprVarNameIndex = ExprFeature.getFeatureIndex(VarName.class);
	private static final int exprFileNameIndex = ExprFeature.getFeatureIndex(FileName.class);

	/**
	 * Generate variable and expression feature for prediction and obtain a set of
	 * keys to those variables that formatted as fineName::line::varName
	 * 
	 * INFLUENCE:
	 * <p>
	 * the {@code varFeatures} will be updated with the corresponding variable
	 * features
	 * </p>
	 * <p>
	 * the {@code exprFeatures} will be updated with the corresponding expression
	 * features
	 * </p>
	 * <p>
	 * the {@code info} will be updated, legal variables will be added
	 * </p>
	 * 
	 * @param srcPath
	 *            : base directory of source code
	 * @param info
	 *            : line information to extract features
	 * @param includeVarWrite
	 *            : a flag denoting whether predict for variables with
	 *            {@code USETYPE.WRITE} use.
	 * @param varFeatures
	 *            : list of variable features
	 * @param exprFeatures
	 *            : list of expression features
	 * @return a set of keys of variables
	 */
	public static Set<String> obtainAllUsedVaraiblesForPredict(String srcPath, LineInfo info, boolean includeVarWrite,
			List<String> varFeatures, List<String> exprFeatures) {
		Set<String> variables = null;

		String relJavaFile = info.getRelJavaPath();
		int line = info.getLine();
		if (includeVarWrite) {
			variables = CodeAnalyzer.getAllVariablesUsed(srcPath, relJavaFile, line);
		} else {
			variables = CodeAnalyzer.getAllVariablesReadUse(srcPath, relJavaFile, line);
		}
		Set<String> keys = new HashSet<>();
		List<String> varF = FeatureGenerator.generateVarFeatureForLine(srcPath, relJavaFile, line);
		List<String> expF = FeatureGenerator.generateExprFeatureForLine(srcPath, relJavaFile, line);

		for (String feature : varF) {
			String[] elements = feature.split("\t");
			String varName = elements[varNameIndex];
			String varType = elements[varTypeIndex];
			info.addLegalVariable(varName, varType);
			if (variables.contains(varName)) {
				varFeatures.add(feature);
				String key = elements[varFileNameIndex] + "::" + line + "::" + varName;
				keys.add(key);
			}
		}

		for (String feature : expF) {
			String[] elements = feature.split("\t");
			String varName = elements[exprVarNameIndex];
			if (variables.contains(varName)) {
				exprFeatures.add(feature);
				String key = elements[exprFileNameIndex] + "::" + line + "::" + varName;
				keys.add(key);
			}
		}

		return keys;
	}

	public static String getVarFeatureHeader() {
		return VarFeature.getFeatureHeader();
	}
	
	public static String getExprFeatureHeader() {
		return ExprFeature.getFeatureHeader();
	}
	
	/**
	 * Generate predicate classifier training data
	 * 
	 * @param baseDir
	 *            : base directory of source files
	 * @param tarFile
	 *            : target file to output feature data
	 * @param projNameForTrain
	 *            : project name for feature extraction
	 * @param idForTrain
	 *            : bug id for feature extraction
	 * @param algorithm
	 *            : which algorithm is used to decide the label
	 */
	public static void generateTrainClassifierFeatures(String baseDir, String tarFile, String projNameForTrain,
			int idForTrain, Algorithm algorithm) {
		String predicatePath = System.getProperty("user.dir") + "/res/label/" + projNameForTrain + "/"
				+ projNameForTrain + "_" + idForTrain + "/" + algorithm.getName() + ".csv";
		List<String> contents = JavaFile.readFileToStringList(predicatePath);
		StringBuffer buffer = new StringBuffer(ClassifierFeature.getFeatureHeader());
		
		for(int i = 1; i < contents.size(); i ++) {
			String[] data = contents.get(i).split("\t");
			String predicate = data[1];
			String label = data[2];
			String[] locs = data[0].split("#");
			if(locs.length != 5) {
				continue;
			}
			String clazz = locs[0];
			int line = -1;
			try {
				line = Integer.parseInt(locs[4]);
			} catch (Exception e) {
				continue;
			}
			int index = clazz.indexOf("$");
			if(index > 0) {
				clazz = clazz.substring(0, index);
			}
			String relJavaFile = clazz.replace(".", "/") + ".java";
			List<String> features = ClassifierFeature.getFeature(baseDir, relJavaFile, line, predicate, label);
			for(String string : features) {
				buffer.append("\n" + string);
			}
		}
		JavaFile.writeStringToFile(tarFile, buffer.toString());
	}

	/**
	 * Generate Classifier feature for the given location
	 * 
	 * @param baseDir
	 *            : base directory of source code
	 * @param relJavaFile
	 *            : relative file path of the java file to extract feature
	 * @param line
	 *            : line number
	 * @param predicates
	 *            : predicates at this location
	 * @return a list of features for predict
	 */
	public static List<String> generateClassifierFeatureForLine(String baseDir, String relJavaFile, int line, Set<String> predicates) {
		List<String> features = new LinkedList<>();
		for(String pred : predicates) {
			features.addAll(ClassifierFeature.getFeature(baseDir, relJavaFile, line, pred, "X"));
		}
		return features;
	}
	
}
