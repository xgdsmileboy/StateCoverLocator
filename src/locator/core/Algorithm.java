/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.LevelLogger;

/**
 * @author Jiajun
 * @date Jun 13, 2017
 */
public abstract class Algorithm {
	
	protected PredicateCoverage predicate;
	
	public abstract String getName();
	
	public abstract double getScore(int fcover, int pcover, int totalFailed, int totalPassed);
	
	public List<Pair<String, Double>> compute(Subject subject, int totalFailed, int totalPassed) {
//		Map<String, ArrayList<PredicateCoverage>> predCoverage = new HashMap<String , ArrayList<PredicateCoverage>>();
		List<Pair<String, Double>> result = new ArrayList<Pair<String, Double>>();
		String oriCoveragePath = subject.getCoverageInfoPath() + "/ori_coverage.csv";
		String predCoveragePath = subject.getCoverageInfoPath() + "/pred_coverage.csv";

		List<String> oriContent = JavaFile.readFileToStringList(oriCoveragePath);
		for(String line : oriContent) {
			String parts[] = line.split("\t");
			if (parts.length != 3) {
				LevelLogger.error("Format error in " + oriCoveragePath + 
						", each line should have three parts : " + line);
				return result;
			}
			String statementInfo = parts[0];
			if (statementInfo.equals("line")) continue;
			int fcover = Integer.valueOf(parts[1]);
			int pcover = Integer.valueOf(parts[2]);
			result.add(new Pair<String, Double>(statementInfo, getScore(fcover, pcover, totalFailed, totalPassed)));
		}
		
		List<String> predContent = JavaFile.readFileToStringList(predCoveragePath);
		Map<String, Double> statementScore = new HashMap<String, Double>();
		for(String line : predContent) {
			String parts[] = line.split("\t");
			if (parts.length != 3) {
				LevelLogger.error("Format error in " + predCoveragePath + 
						", each line should have three parts : " + line);
				return result;
			}
			String statementInfo = parts[0];
			if (statementInfo.equals("line")) continue;
			int fcover = Integer.valueOf(parts[1]);
			int pcover = Integer.valueOf(parts[2]);

			int probPos = statementInfo.lastIndexOf('#');
			PredicateCoverage predCov = new PredicateCoverage();
			predCov.setProb(Double.valueOf(statementInfo.substring(probPos + 1)));
			int predPos = statementInfo.lastIndexOf('#', probPos - 1);
			predCov.setPredicate(statementInfo.substring(predPos + 1, probPos));
			predCov.setFcover(fcover);
			predCov.setPcover(pcover);
			predCov.setStatementInfo(statementInfo.substring(0, predPos));
			predCov.setScore(getScore(fcover, pcover, totalFailed, totalPassed));

			Double previousScore = statementScore.get(predCov.getStatementInfo());
			if (previousScore == null || predCov.getScore() > previousScore) { // only use the maximum pred score
				statementScore.put(predCov.getStatementInfo(), predCov.getScore());
				predicate = predCov;
			}
		}
		
		List<Pair<Double, String>> contents = new ArrayList<Pair<Double, String>>();
		for(Pair<String, Double> p : result) {
			Double predScore = statementScore.get(p.getFirst());
			double s = predScore == null ? 0 : predScore;
			contents.add(new Pair<>(p.getSecond() + s,
					p.getFirst() + "\t" + Double.toString(p.getSecond()) + 
					"\t" + Double.toString(s) + "\t" + Double.toString(p.getSecond() + s)));
			p.setSecond(p.getSecond() + s); // add original and pred together
		}
		
		contents.sort(new Comparator<Pair<Double, String>>() {
			@Override
			public int compare(Pair<Double, String> o1, Pair<Double, String> o2) {
				return -Double.compare(o1.getFirst(), o2.getFirst());
			}
			
		});
		String sortedResult = "line\toriginal\tpredicate_max\ttotal\n";
		for(Pair<Double, String> line : contents) {
			sortedResult += line.getSecond() + "\n";
		}
		JavaFile.writeStringToFile(subject.getCoverageInfoPath() + "/" + getName() + "_coverage.csv", sortedResult);
		
		return result;
	}
}
