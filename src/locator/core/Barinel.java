/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core;

/**
 * @author Jiajun
 * @date Jun 13, 2017
 */
public class Barinel extends PredicateCoverageAlgorithm {

	@Override
	public String getName() {
		return "Barinel";
	}

	/**
	 * 1 - (passed(s) / (passed(s) + failed(s)))
	 */
	@Override
	public double getScore(int fcover, int pcover, int totalFailed,
			int totalPassed, int fcoverObserved, int pcoverObserved) {
		if (pcover + fcover == 0) return 0;
		return 1.0 - (pcover * 1.0 / (pcover + fcover));
	}

}
