package locator.core;

public class StatisticalDebugging extends PredicateCoverageAlgorithm {

	private static double normalize = 1;
	
	@Override
	public double getScore(int fcover, int pcover, int totalFailed,
			int totalPassed, int fcoverObserved, int pcoverObserved) {
		double failureP = fcover == 0 ? 0 : fcover * 1.0 / (fcover + pcover);
		double contextP = fcoverObserved * 1.0 / (fcoverObserved + pcoverObserved);
		double increaseP = failureP - contextP;
		double result = 2 / (1 / increaseP + Math.log(totalFailed) / Math.log(fcover + normalize));
		return increaseP < 0 ? Double.NEGATIVE_INFINITY : result;
	}

	@Override
	public String getName() {
		return "StatisticalDebugging";
	}

}
