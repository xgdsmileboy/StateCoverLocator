package locator.core;

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
}
