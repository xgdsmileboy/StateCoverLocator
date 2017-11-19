package locator.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;

public abstract class TrueFalseCoverageNumberAlgorithm extends Algorithm{
	public abstract double getScore(PredicateCoverage predCov, int totalFailed, int totalPassed);
	
	public List<Pair<String, Double>> compute(Subject subject, int totalFailed, int totalPassed, boolean useStatisticalDebugging) { 
		List<Pair<String, Double>> result = new ArrayList<Pair<String, Double>>();
		
		List<String> predContent = JavaFile.readFileToStringList(Constant.STR_TMP_INSTR_OUTPUT_FILE);
		Map<String, PredicateCoverage> statementScore = new HashMap<String, PredicateCoverage>();
		for(int i = 0; i < predContent.size();) {
			String line = predContent.get(i);
			int probPos = line.lastIndexOf('#');
			PredicateCoverage predCov = new PredicateCoverage();
			predCov.setProb(Double.valueOf(line.substring(probPos + 1)));
			int predPos = line.lastIndexOf('#', probPos - 1);
			predCov.setPredicate(line.substring(predPos + 1, probPos));
			predCov.setStatementInfo(line.substring(0, predPos));
			i++;
			while (i < predContent.size()) {
				String parts[] = predContent.get(i).split("\t");
				if (parts.length == 3) {
					int trueCnt = Integer.valueOf(parts[0]);
					int falseCnt = Integer.valueOf(parts[1]);
					double prob = trueCnt * 1.0 / (trueCnt + falseCnt);
					if (parts[2].equals("0")) {
						predCov.addFailEvaluationBias(prob);
					} else {
						predCov.addPassEvaluationBias(prob);
					}
				} else {
					break;
				}
				i++;
			}
			predCov.setScore(getScore(predCov, totalFailed, totalPassed));

			PredicateCoverage previousPredCov = statementScore.get(predCov.getStatementInfo());
			if (previousPredCov == null || predCov.getScore() > previousPredCov.getScore()) { // only use the maximum pred score
				statementScore.put(predCov.getStatementInfo(), predCov);
				predicate = predCov;
			}
		}
		
		List<Pair<Double, String>> contents = new ArrayList<Pair<Double, String>>();
		for(Map.Entry<String, PredicateCoverage> p : statementScore.entrySet()) {
			PredicateCoverage predCov = p.getValue();
			String[] info = predCov.getStatementInfo().split("#");
			String methodString = Identifier.getMessage(Integer.parseInt(info[0]));
			String moreInfo = predCov.getStatementInfo().substring(info[0].length() + 1);
			contents.add(new Pair<>(predCov.getScore(),
					methodString + "#" + moreInfo +
					"\t" + Double.toString(predCov.getScore()) +
					"\t" + predCov.getPredicate()));
		}
		writeToFile(contents, subject, useStatisticalDebugging);
		
		return result;
	}
}
