/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.util;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.CoverInfo;
import locator.common.java.JavaFile;
import locator.common.java.Subject;

/**
 * @author Jiajun
 * @date Dec 19, 2017
 */
public class Utils {

	public static void pathGuarantee(String ... paths) {
		File file = null;
		for(String string : paths) {
			file = new File(string);
			if(!file.exists()) {
				file.mkdirs();
			}
		}
	}
	
	public static void backupSource(Subject subject){
		String src = subject.getHome() + subject.getSsrc();
		File file = new File(src + "_ori");
		if(file.exists()){
			ExecuteCommand.copyFolder(src + "_ori", src);
		} else {
			ExecuteCommand.copyFolder(src, src + "_ori");
		}
		String test = subject.getHome() + subject.getTsrc();
		File tfile = new File(test + "_ori");
		if(tfile.exists()){
			ExecuteCommand.copyFolder(test + "_ori", test);
		} else {
			ExecuteCommand.copyFolder(test, test + "_ori");
		}
	}
	
	public static void printCoverage(Map<String, CoverInfo> coverage, String filePath) {
		File file = new File(filePath);
		String header = "line\tfcover\tpcover\tf_observed_cover\tp_observed_cover\n";
		JavaFile.writeStringToFile(file, header, false);
		for (Entry<String, CoverInfo> entry : coverage.entrySet()) {
			StringBuffer stringBuffer = new StringBuffer();
			String key = entry.getKey();
			String[] info = key.split("#");
			String methodString = Identifier.getMessage(Integer.parseInt(info[0]));
			stringBuffer.append(methodString);
			String moreInfo = key.substring(info[0].length() + 1);
			stringBuffer.append("#");
			stringBuffer.append(moreInfo);
			
			stringBuffer.append("\t");
			stringBuffer.append(entry.getValue().getFailedCount());
			stringBuffer.append("\t");
			stringBuffer.append(entry.getValue().getPassedCount());
			stringBuffer.append("\t");
			stringBuffer.append(entry.getValue().getFailedObservedCount());
			stringBuffer.append("\t");
			stringBuffer.append(entry.getValue().getPassedObservedCount());
			stringBuffer.append("\n");
			// view coverage.csv file
			JavaFile.writeStringToFile(file, stringBuffer.toString(), true);
		}
	}
	
	public static void recordSubjects(List<Subject> subjects) {
		String content = "name\tid\n";
		for(Subject subject : subjects) {
			content += subject.getName() + "\t" + Integer.toString(subject.getId()) + "\n";
		}
		JavaFile.writeStringToFile(Constant.HOME + "/logs/project.log", content);
	}
	
}
