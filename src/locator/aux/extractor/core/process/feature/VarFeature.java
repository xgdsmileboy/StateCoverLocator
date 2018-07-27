/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package locator.aux.extractor.core.process.feature;

import java.util.LinkedList;
import java.util.List;

import locator.aux.extractor.core.ast.node.Use;
import locator.aux.extractor.core.ast.node.Variable;
import locator.aux.extractor.core.process.feature.item.ColumnNumber;
import locator.aux.extractor.core.process.feature.item.Feature;
import locator.aux.extractor.core.process.feature.item.FileName;
import locator.aux.extractor.core.process.feature.item.IfUse;
import locator.aux.extractor.core.process.feature.item.LastAssign;
import locator.aux.extractor.core.process.feature.item.LineNumber;
import locator.aux.extractor.core.process.feature.item.LoopUse;
import locator.aux.extractor.core.process.feature.item.MethodName;
import locator.aux.extractor.core.process.feature.item.ParamDef;
import locator.aux.extractor.core.process.feature.item.VarName;
import locator.aux.extractor.core.process.feature.item.VarType;

/**
 * @author Jiajun
 *
 * Jul 19, 2018
 */
public class VarFeature {
	
	private Use _use;
	private List<Feature> _features;
	private final static char SEP = '\t';
	
	private static List<Feature> featureConfig = new LinkedList<>();
	
	static {
		// can configure flexibly
		featureConfig.add(LineNumber.getInstance());
		featureConfig.add(ColumnNumber.getInstance());
		featureConfig.add(FileName.getInstance());
		featureConfig.add(MethodName.getInstance());
		featureConfig.add(VarName.getInstance());
		featureConfig.add(VarType.getInstance());
		featureConfig.add(ParamDef.getInstance());
		featureConfig.add(LastAssign.getInstance());
		featureConfig.add(LoopUse.getInstance());
		featureConfig.add(IfUse.getInstance());
	}
	
	public static int getFeatureIndex(Class<? extends Feature> clazz) {
		for(int index = 0; index < featureConfig.size(); index ++) {
			if(featureConfig.get(index).getClass() == clazz) {
				return index;
			}
		}
		return -1;
	}
	
	public VarFeature(Use use) {
		_use = use;
		_features = new LinkedList<>();
		for(Feature feature : featureConfig) {
			_features.add(feature.extractFeature(use));
		}
	}
	
	public static String getFeatureHeader() {
		StringBuffer header = new StringBuffer();
		for(Feature feature : featureConfig) {
			header.append(feature.getName() + SEP);
		}
		return header.toString().trim();
	}
	
	public String toStringFormat() {
		StringBuffer features = new StringBuffer();
		for(Feature feature : _features) {
			String fString = feature.getStringFormat();
			if(fString == null) {
				return null;
			}
			features.append(fString + SEP);
		}
		return features.toString().trim();
	}
	
	public static String extractFeature(Variable variable, int line) {
		StringBuffer buffer = new StringBuffer();
		for(Feature feature : featureConfig) {
			String fString = feature.extractFeature(variable, line);
			if(fString == null) {
				return null;
			}
			buffer.append(fString + SEP);
		}
		return buffer.toString().trim();
	}
	
}
