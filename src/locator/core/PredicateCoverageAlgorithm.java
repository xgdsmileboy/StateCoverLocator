package locator.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.LevelLogger;

public abstract class PredicateCoverageAlgorithm extends Algorithm {
	public abstract double getScore(int fcover, int pcover, int totalFailed, int totalPassed);
	public List<Pair<String, Double>> compute(Subject subject, int totalFailed, int totalPassed, boolean useStatisticalDebugging) {
//		Map<String, ArrayList<PredicateCoverage>> predCoverage = new HashMap<String , ArrayList<PredicateCoverage>>();
		List<Pair<String, Double>> result = new ArrayList<Pair<String, Double>>();
		String oriCoveragePath = subject.getCoverageInfoPath() + "/ori_coverage.csv";
		String predCoveragePath = subject.getCoverageInfoPath() + "/pred_coverage";
		if (useStatisticalDebugging) {
			predCoveragePath += "_sd.csv";
		} else {
			predCoveragePath += ".csv";
		}

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
		Map<String, PredicateCoverage> statementScore = new HashMap<String, PredicateCoverage>();
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

			PredicateCoverage previousPredCov = statementScore.get(predCov.getStatementInfo());
			if (previousPredCov == null || predCov.getScore() > previousPredCov.getScore()) { // only use the maximum pred score
				statementScore.put(predCov.getStatementInfo(), predCov);
				predicate = predCov;
			}
		}
		
		List<Pair<Double, String>> contents = new ArrayList<Pair<Double, String>>();
		for(Pair<String, Double> p : result) {
			PredicateCoverage predCov = statementScore.get(p.getFirst());
			double s = predCov == null ? 0 : predCov.getScore();
			String predStr = predCov == null ? "" : predCov.getPredicate();
			contents.add(new Pair<>(p.getSecond() + s,
					p.getFirst() + "\t" + Double.toString(p.getSecond()) + 
					"\t" + Double.toString(s) + "\t" + Double.toString(p.getSecond() + s) +
					"\t" + predStr));
			p.setSecond(p.getSecond() + s); // add original and pred together
		}
		writeToFile(contents, subject, useStatisticalDebugging);
		
		return result;
	}
}