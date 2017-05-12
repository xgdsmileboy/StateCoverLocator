/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor.feature;

import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

import locator.common.java.Pair;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class FeatureExtraction {
	
	public static Pair<FeatureEntry, Map<String, FeatureEntry>> extractFeature(ASTNode statement, int line){
		Pair<FeatureEntry, Map<String, FeatureEntry>> features = new Pair<>();
		
		return features;
	}
	
}
