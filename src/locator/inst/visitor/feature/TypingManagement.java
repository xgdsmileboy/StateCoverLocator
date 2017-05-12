/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor.feature;

import java.util.HashMap;
import java.util.Map;

import locator.common.util.LevelLogger;

/**
 * @author Jiajun
 * @date May 12, 2017
 */
public class TypingManagement {
	
	private final static String __name__ = "@TypingManagement ";
	
	private static Map<String, TypingInfo> _clazzInfo = new HashMap<>();
	
	public static void add(String clazzIdentifier, TypingInfo type){
		if(!contain(clazzIdentifier)){
			_clazzInfo.put(clazzIdentifier, type);
		} else {
			LevelLogger.error(__name__ + "#add Already contains typing information for clazz : " + clazzIdentifier);
		}
	}
	
	public static boolean contain(String clazzIdentifier){
		return _clazzInfo.containsKey(clazzIdentifier);
	}
	
}
