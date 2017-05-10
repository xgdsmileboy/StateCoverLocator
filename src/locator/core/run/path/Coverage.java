/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.run.path;

import locator.common.java.Subject;
import locator.common.util.CmdFactory;
import locator.common.util.ExecuteCommand;
import locator.common.util.LevelLogger;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class Coverage {
	
	private final static String __name__ = "@Coverage ";
	
	public static void computeCoverage(Subject subject){
		//run each test case and collect all test statements covered
		try {
			ExecuteCommand.executeDefects4JTest(CmdFactory.createTestSuiteCmd(subject));
		} catch (Exception e) {
			LevelLogger.fatal(__name__ + "#computeCoverage run test suite failed !", e);
		}
		
		
	}
	
}
