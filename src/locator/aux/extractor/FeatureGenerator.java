/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.aux.extractor;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import locator.aux.extractor.core.feature.ExprFeature;
import locator.aux.extractor.core.feature.VarFeature;
import locator.aux.extractor.core.parser.Analyzer;
import locator.aux.extractor.core.parser.BasicBlock;
import locator.aux.extractor.core.parser.Use;
import locator.aux.extractor.core.parser.Variable;
import locator.common.java.JavaFile;

/**
 * @author Jiajun
 * @date Jul 7, 2018
 */
public class FeatureGenerator {

	public static void generateTrainVarFeatures(String baseDir, String tarFile) {
		List<String> files = JavaFile.ergodic(baseDir, new LinkedList<String>());
//		List<VarFeature> features = new LinkedList<>();
		StringBuffer featuresBuff = new StringBuffer();
		featuresBuff.append(VarFeature.getFeatureHeader());
		for(String file : files) {
			String relJavaFile = file.substring(baseDir.length() + 1);
			BasicBlock basicBlock = Analyzer.analyze(relJavaFile, baseDir);
			Set<Use> uses = basicBlock.recursivelyGetUses();
			for(Use use : uses) {
				featuresBuff.append('\n' + new VarFeature(use).toStringFormat());
//				features.add(new VarFeature(use));
			}
		}
		JavaFile.writeStringToFile(tarFile, featuresBuff.toString());
	}
	
	public static void generateTrainExprFeatures(String baseDir, String tarFile) {
		List<String> files = JavaFile.ergodic(baseDir, new LinkedList<String>());
//		List<VarFeature> features = new LinkedList<>();
		StringBuffer featuresBuff = new StringBuffer();
		featuresBuff.append(ExprFeature.getFeatureHeader());
		for(String file : files) {
			String relJavaFile = file.substring(baseDir.length() + 1);
			BasicBlock basicBlock = Analyzer.analyze(relJavaFile, baseDir);
			Set<Use> uses = basicBlock.recursivelyGetUses();
			for(Use use : uses) {
				String feature = new ExprFeature(use).toStringFormat();
				if(feature == null) {
					continue;
				}
				featuresBuff.append('\n' + feature);
//				features.add(new VarFeature(use));
			}
		}
		JavaFile.writeStringToFile(tarFile, featuresBuff.toString());
	}
	
	public static List<String> generateVarFeatureForLineWithFilter(String baseDir, String relJavaFile, int line, Set<String> varWhiteList) {
		List<String> features = new LinkedList<>();
		BasicBlock basicBlock = Analyzer.analyze(relJavaFile, baseDir);
		Set<Variable> variables = basicBlock.getAllValidVariables(line);
		for(Variable variable : variables) {
			if(varWhiteList.contains(variable.getName())) {
				features.add(VarFeature.extractFeature(variable, line));
			}
		}
		return features;
	}
	
	public static List<String> generateVarFeatureForLine(String baseDir, String relJavaFile, int line) {
		List<String> features = new LinkedList<>();
		BasicBlock basicBlock = Analyzer.analyze(relJavaFile, baseDir);
		Set<Variable> variables = basicBlock.getAllValidVariables(line);
		for(Variable variable : variables) {
			features.add(VarFeature.extractFeature(variable, line));
		}
		return features;
	}
	
	public static List<String> generateExprFeatureForLineWithFilter(String baseDir, String relJavaFile, int line, Set<String> varWhiteList) {
		List<String> features = new LinkedList<>();
		BasicBlock basicBlock = Analyzer.analyze(relJavaFile, baseDir);
		Set<Variable> variables = basicBlock.getAllValidVariables(line);
		for(Variable variable : variables) {
			if(varWhiteList.contains(variable.getName())) {
				String feature = ExprFeature.extractFeature(variable, line);
				if(feature == null) {
					continue;
				}
				features.add(feature);
			}
		}
		return features;
	}
	
	public static List<String> generateExprFeatureForLine(String baseDir, String relJavaFile, int line) {
		List<String> features = new LinkedList<>();
		BasicBlock basicBlock = Analyzer.analyze(relJavaFile, baseDir);
		Set<Variable> variables = basicBlock.getAllValidVariables(line);
		for(Variable variable : variables) {
			String feature = ExprFeature.extractFeature(variable, line);
			if(feature == null) {
				continue;
			}
			features.add(feature);
		}
		return features;
	}
	
}
