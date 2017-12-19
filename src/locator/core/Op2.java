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
public class Op2 extends PredicateCoverageAlgorithm {

	@Override
	public String getName() {
		return "Op2";
	}

	/**
	 * failed(s) − (passed(s) / (totalPassed + 1))
	 */
	@Override
	public double getScore(int fcover, int pcover, int totalFailed,
			int totalPassed, int fcoverObserved, int pcoverObserved) {
		if (pcover + fcover == 0) return 0;
		return fcover - (pcover * 1.0 / (totalPassed + 1));
	}

}
