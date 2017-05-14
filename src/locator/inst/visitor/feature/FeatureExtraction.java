/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor.feature;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.pku.sei.conditon.simple.FeatureGenerator;
import locator.common.java.Pair;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class FeatureExtraction {

	public static Pair<FeatureEntry, Map<String, FeatureEntry>> extractFeature(ASTNode statement, int line) {
		Pair<FeatureEntry, Map<String, FeatureEntry>> features = new Pair<>();

		return features;
	}

	public static List<String> extractVarFeature(String srcPath, String relJavaPath, int line) {
		return FeatureGenerator.generateVarFeature(srcPath, relJavaPath, line);
	}

	public static List<String> extractExpFeature(String srcPath, String relJavaPath, int line) {
		return FeatureGenerator.generateExprFeature(srcPath, relJavaPath, line);
	}

}
