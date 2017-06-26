/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.org.apache.bcel.internal.generic.LNEG;

import locator.common.java.Subject;
import locator.common.util.LevelLogger;
import soot.coffi.constant_element_value;

/**
 * Used for label each method with a unique id
 * 
 * @author Jiajun
 * @date Mar 24, 2017
 */
public class Identifier {
	private static Map<Integer, String> identifiers = new HashMap<>();
	private static Map<String, Integer> inverseIdentifier = new HashMap<>();
	private static Integer counter = 0;

	public static void resetAll() {
		identifiers = new HashMap<>();
		inverseIdentifier = new HashMap<>();
		counter = 0;
	}
	
	public static void backup(Subject subject){
		String fileName =  Constant.STR_INFO_OUT_PATH + "/" + subject.getName() + "/" + subject.getName() + "_" + subject.getId() + "/identifier.txt" ;
		File file = new File(fileName);
		if(!file.exists()){
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				LevelLogger.error("Backup identifier information failed!");
				return;
			}
		}
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			LevelLogger.error("Backup identifier information failed!");
			return;
		}
		
		for(Entry<Integer, String> pair : identifiers.entrySet()){
			String line = pair.getKey() + "\t" + pair.getValue() + "\n";
			try {
				bw.write(line);
			} catch (IOException e) {
				LevelLogger.error("Backup identifier information failed!");
				return;
			}
		}
		try {
			bw.close();
		} catch (IOException e) {
			LevelLogger.error("Backup identifier information failed!");
			return;
		}
		
	}
	
	public static void restore(Subject subject){
		String fileName =  Constant.STR_INFO_OUT_PATH + "/" + subject.getName() + "/" + subject.getName() + "_" + subject.getId() + "/identifier.txt" ;
		File file = new File(fileName);
		if(!file.exists()){
			LevelLogger.error("Restore identifier information failed for not find file : " + fileName);
			return;
		}
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
		try {
			while((line = br.readLine()) != null){
				String[] info = line.split("\t");
				if(info.length != 2){
					LevelLogger.error("Restore identifier information format error : " + line);
					continue;
				}
				Integer id = Integer.parseInt(info[0]);
				String methodStr = info[1];
				identifiers.put(id, methodStr);
				inverseIdentifier.put(methodStr, id);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get exclusive method id based on the given method string information,
	 * 
	 * @param message
	 *            : string representation for method, e.g.,
	 *            "fullClasspath#returnType#methodName#arguments"
	 * @return an exclusive id for the given method
	 */
	public static Integer getIdentifier(String message) {
		Integer value = inverseIdentifier.get(message);
		if (value != null) {
			return value;
		} else {
			identifiers.put(counter, message);
			inverseIdentifier.put(message, counter);
			counter++;
			return counter - 1;
		}
	}

	/**
	 * get method string representation for the given method id
	 * 
	 * @param id
	 *            : method id
	 * @return a string of a method, e.g.,
	 *         "fullClasspath#returnType#methodName#arguments"
	 */
	public static String getMessage(int id) {
		String message = identifiers.get(Integer.valueOf(id));
		if (message == null) {
			message = "ERROR";
		}
		return message;
	}

}
