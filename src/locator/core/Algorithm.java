/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core;

import java.util.List;

import locator.common.java.Pair;
import locator.common.java.Subject;

/**
 * @author Jiajun
 * @date Jun 13, 2017
 */
public interface Algorithm {
	
	public String getName();
	
	public List<Pair<String, Float>> compute(Subject subject);
	
}
