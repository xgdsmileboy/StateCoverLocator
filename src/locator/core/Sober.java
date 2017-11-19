package locator.core;

import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;

public class Sober extends TrueFalseCoverageNumberAlgorithm {

	@Override
	public String getName() {
		return "Sober";
	}

	@Override
	public double getScore(PredicateCoverage predCov, int totalFailed,
			int totalPassed) {
		while(predCov.getPassEvaluationBias().size() < totalPassed) {
			predCov.addPassEvaluationBias(0.5);
		}
		while(predCov.getFailEvaluationBias().size() < totalFailed) {
			predCov.addFailEvaluationBias(0.5);
		}
		double meanP = getMean(predCov.getPassEvaluationBias());
		double varP = getVar(predCov.getPassEvaluationBias(), meanP);
		double meanF = getMean(predCov.getFailEvaluationBias());
		double Z = (meanF - meanP) * Math.sqrt(totalFailed) / varP;
		NormalDistribution normalDistribution = new NormalDistribution(0, 1);
		return Math.log(varP / (Math.sqrt(totalFailed) * normalDistribution.density(Z)));
	}
	
	private double getMean(final List<Double> evaluationBias) {
		double meanP = 0;
		for(double prob : evaluationBias) {
			meanP += prob;
		}
		return meanP / evaluationBias.size();
	}

	private double getVar(final List<Double> evaluationBias, double mean) {
		double var = 0;
		for(double prob : evaluationBias) {
			var += (prob - mean) * (prob - mean);
		}
		return Math.sqrt(var / (evaluationBias.size() - 1));
	}
}
