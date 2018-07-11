package locator.core.alg;

public class Simple extends PredicateCoverageAlgorithm {

	/**
	 * 1 - passed
	 */
	@Override
	public double getScore(int fcover, int pcover, int totalFailed,
			int totalPassed, int fcoverObserved, int pcoverObserved) {
		if (pcover + fcover == 0) return 0;
		if (fcover > 0) {
			return 1 - pcover;
		} else {
			return - totalPassed - 1;
		}
	}

	@Override
	public String getName() {
		return "Simple";
	}

}
