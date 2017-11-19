package locator.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Information of a predicate, including coverage and predicted probability.
 * @author lillian
 *
 */
public class PredicateCoverage {
	
	private String statementInfo;
	
	private int fcover;
	
	private int pcover;
	
	private String predicate;
	
	private double prob;
	
	private double score;
	
	private List<Double> passEvaluationBias = new ArrayList<Double>();
	
	private List<Double> failEvaluationBias = new ArrayList<Double>();

	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getStatementInfo() {
		return statementInfo;
	}
	public void setStatementInfo(String statementInfo) {
		this.statementInfo = statementInfo;
	}
	public int getFcover() {
		return fcover;
	}
	public void setFcover(int fcover) {
		this.fcover = fcover;
	}
	public int getPcover() {
		return pcover;
	}
	public void setPcover(int pcover) {
		this.pcover = pcover;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public double getProb() {
		return prob;
	}
	public void setProb(double prob) {
		this.prob = prob;
	}
	public void addPassEvaluationBias(double prob) {
		this.passEvaluationBias.add(prob);
	}
	public void addFailEvaluationBias(double prob) {
		this.failEvaluationBias.add(prob);
	}
	public List<Double> getPassEvaluationBias() {
		return this.passEvaluationBias;
	}
	public List<Double> getFailEvaluationBias() {
		return this.failEvaluationBias;
	}
}
