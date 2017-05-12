/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor.feature;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.Type;

/**
 * @author Jiajun
 * @date May 12, 2017
 */
public class TypingInfo {

	private static Map<String, Type> fieldTypeMap = new HashMap<>();
	private static Map<String, Map<String, Type>> localTypeMap = new HashMap<>();

	
	public static void resetAll(){
		fieldTypeMap = new HashMap<>();
		localTypeMap = new HashMap<>();
	}
	
	public static boolean addFieldType(String fieldName, Type type) {
		if (fieldTypeMap.containsKey(fieldName) && !fieldTypeMap.get(fieldName).equals(type) && !fieldTypeMap.get(fieldName).toString().equals(type.toString())) {
			System.out.println("Field type inconsistancy '" + fieldName + "' with types : "
					+ fieldTypeMap.get(fieldName) + " and " + type);
			return false;
		}
		fieldTypeMap.put(fieldName, type);
		return true;
	}

	public static boolean addMethodVariableType(String methodName, String varName, Type type) {
		if (!localTypeMap.containsKey(methodName)) {
			Map<String, Type> map = new HashMap<>();
			map.put(varName, type);
			localTypeMap.put(methodName, map);
			return true;
		} else {
			Map<String, Type> map = localTypeMap.get(methodName);
			if (map.containsKey(varName) && !map.get(varName).equals(type) && !map.get(varName).toString().equals(type.toString())) {
				System.out.println("Variable type inconsistancy of '" + varName + "' in method '" + methodName
						+ "' with types : " + map.get(varName) + " and " + type);
				return false;
			}
			map.put(varName, type);
			return true;
		}
	}

	public static Type getVariableType(String methodName, String varName) {
		
		if (localTypeMap.containsKey(methodName) && localTypeMap.get(methodName).get(varName) != null) {
			return localTypeMap.get(methodName).get(varName);
		} else {
			return fieldTypeMap.get(varName);
		}
	}

	public static Class<?> convert2Class(Type type) {
		
		System.out.println("type : " + type);
		
		switch (type.toString()) {
		case "void":
			return void.class;
		case "int":
			return int.class;
		case "char":
			return char.class;
		case "short":
			return short.class;
		case "long":
			return long.class;
		case "float":
			return float.class;
		case "double":
			return double.class;
		case "byte":
			return byte.class;
		default:
		}
		
		if(type.toString().contains("[")){
			return Arrays.class;
		}
		return null;
	}

}
