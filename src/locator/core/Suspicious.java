/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import locator.common.java.Subject;
import locator.common.util.CList;
import locator.common.util.Pair;
import locator.core.alg.Algorithm;

/**
 * @author Jiajun
 * @date Jun 13, 2017
 */
public class Suspicious {
	
	private static String seperator = "\t";
	
	public static void compute(Subject subject, List<Algorithm> algorithms, int totalFailed, int totalPassed, boolean useStatisticalDebugging,
			boolean useSober){
		Map<String, CList> suspicious = new HashMap<>();
		int listLen = algorithms.size();
		StringBuffer header = new StringBuffer();
		header.append("line");
		// compute suspicious for each algorithm
		for(int i = 0; i < listLen; i++){
			header.append(seperator);
			header.append(algorithms.get(i).getName());
			List<Pair<String, Double>> result = algorithms.get(i).compute(subject, totalFailed, totalPassed, useStatisticalDebugging);
			for(Pair<String, Double> pair : result){
				String line = pair.getFirst();
				Double susp = pair.getSecond();
				if(suspicious.containsKey(line)){
					suspicious.get(line).set(i, susp);
				} else {
					CList list = new CList(listLen);
					list.set(i, susp);
					suspicious.put(line, list);
				}
			}
		}
		if (useSober) {
			return;
		}
//		 write into file
		String outputFile = useStatisticalDebugging ? "result_sd.csv" : "result.csv";
		File file = new File(subject.getCoverageInfoPath() + "/" + outputFile);
		if(!file.exists()){
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		header.append("\n");
		try {
			br.write(header.toString());
			for(Entry<String, CList> entry : suspicious.entrySet()){
				StringBuffer sb = new StringBuffer();
				sb.append(entry.getKey());
				CList list = entry.getValue();
				for(int i = 0; i < list.size(); i++){
					sb.append(seperator);
					sb.append(list.get(i));
				}
				sb.append("\n");
				br.write(sb.toString());
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
