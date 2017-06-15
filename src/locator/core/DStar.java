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
public class DStar extends Algorithm {

	@Override
	public String getName() {
		return "DStar";
	}
	/**
	 * failed(s)^n / (passed(s) + (totalFailed - failed(s)))
	 * n is integer, here we set it as 2
	 */
	@Override
	public double getScore(int fcover, int pcover, int totalFailed,
			int totalPassed) {
		return fcover * fcover * 1.0 / (pcover + (totalFailed - fcover));
	}

}
