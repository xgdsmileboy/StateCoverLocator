/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.predict;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import locator.common.java.Pair;
import locator.inst.visitor.feature.FeatureEntry;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class Predictor {
	
	public static List<String> predict(FeatureEntry featureEntry){
		List<String> insertedStatement = new ArrayList<>();
		
		return insertedStatement;
	}
	
	public static Pair<Set<String>, Set<String>> predict(List<String> varFeatures, List<String> expFeatures){
		Pair<Set<String>, Set<String>> conditions = new Pair<>();
		//TODO : return conditions for left variable and right variables
		return conditions;
	}
	
	private static List<String> predict(String feature){
		List<String> insertedStatement = new ArrayList<>();
		
		return insertedStatement;
	}
	
}
