/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core;

import java.util.ArrayList;
import java.util.List;

import locator.common.java.Pair;
import locator.common.java.Subject;

/**
 * @author Jiajun
 * @date Jun 13, 2017
 */
public class Op2 implements Algorithm {

	@Override
	public String getName() {
		return "Op2";
	}
	
	/**
	 * failed(s) − (passed(s) / (totalPassed + 1))
	 */
	@Override
	public List<Pair<String, Float>> compute(Subject subject) {
		List<Pair<String, Float>> result = new ArrayList<>();
		String ori_coverage = subject.getCoverageInfoPath() + "/ori_coverage.csv";
		String pred_coverage = subject.getCoverageInfoPath() + "/pred_coverage.csv";
		
		// TODO Auto-generated method stub
		
		return result;
	}

}
