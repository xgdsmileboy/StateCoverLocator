/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.java;

/**
 * @author Jiajun
 * @date May 11, 2017
 */
public class CoverInfo {

	private int _failedCount = 0;
	private int _passedCount = 0;

	public CoverInfo() {
	}

	public void failedAdd(int count) {
		_failedCount += count;
	}

	public void passedAdd(int count) {
		_passedCount += count;
	}

	public int getFailedCount() {
		return _failedCount;
	}

	public int getPassedCount() {
		return _passedCount;
	}
}
