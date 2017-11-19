/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;

/**
 * @author Jiajun
 * @date Jun 13, 2017
 */
public abstract class Algorithm {
	
	protected PredicateCoverage predicate;
	
	public abstract String getName();
	
	public abstract List<Pair<String, Double>> compute(Subject subject, int totalFailed, int totalPassed, boolean useStatisticalDebugging);
	
	protected void writeToFile(List<Pair<Double, String>> contents, Subject subject, boolean useStatisticalDebugging) {
		// change to java 1.7
		Collections.sort(contents, new Comparator<Pair<Double, String>>() {
			@Override
			public int compare(Pair<Double, String> o1, Pair<Double, String> o2) {
				return -Double.compare(o1.getFirst(), o2.getFirst());
			}
			
		});
//		contents.sort(new Comparator<Pair<Double, String>>() {
//			@Override
//			public int compare(Pair<Double, String> o1, Pair<Double, String> o2) {
//				return -Double.compare(o1.getFirst(), o2.getFirst());
//			}
//			
//		});
		String sortedResult = "line\toriginal_score\tmax_predicate_score\ttotal\tpredicate\n";
		for(Pair<Double, String> line : contents) {
			sortedResult += line.getSecond() + "\n";
		}
		String algOutputFile = subject.getCoverageInfoPath() + "/" + getName() + "_coverage";
		if (useStatisticalDebugging) {
			algOutputFile += "_sd.csv";
		} else {
			algOutputFile += ".csv";
		}
		JavaFile.writeStringToFile(algOutputFile, sortedResult);
	}
}
