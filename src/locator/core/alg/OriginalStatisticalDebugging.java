package locator.core.alg;

public class OriginalStatisticalDebugging extends PredicateCoverageAlgorithm {

	
	@Override
	public double getScore(int fcover, int pcover, int totalFailed,
			int totalPassed, int fcoverObserved, int pcoverObserved) {
		double failureP = fcover == 0 ? 0 : fcover * 1.0 / (fcover + pcover);
		double contextP = fcoverObserved * 1.0 / (fcoverObserved + pcoverObserved);
		double increaseP = failureP - contextP;
		double result = 2 / (1 / increaseP + 1 / (Math.log(fcover) / Math.log(totalFailed)));
		if (Double.isNaN(result)) {
			result = 0;
		}
		return increaseP < 0 ? Double.NEGATIVE_INFINITY : result;
	}

	@Override
	public String getName() {
		return "OriginalStatisticalDebugging";
	}

}
