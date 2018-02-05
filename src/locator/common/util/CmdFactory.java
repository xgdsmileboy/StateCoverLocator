/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.util;

import locator.common.config.Constant;
import locator.common.java.Subject;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class CmdFactory {

	public static String[] createCopyTrainDataForL2S(Subject subject) {
		// python set_evn.py train math 12 0 0
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(Constant.COMMAND_CD);
		stringBuffer.append(Constant.STR_ML_HOME);
		stringBuffer.append(" && ");
		stringBuffer.append(Constant.COMMAND_PYTHON);
		stringBuffer.append(Constant.STR_ML_HOME + "/set_evn.py ");
		stringBuffer.append(subject.getName());
		stringBuffer.append(" ");
		stringBuffer.append(subject.getId());
		stringBuffer.append(" 0 0");
		String[] cmd = new String[] { "/bin/bash", "-c", stringBuffer.toString() };
		return cmd;
	}
	
	/**
	 * build execution command for running the whole test suite
	 * 
	 * @param subject
	 *            : subject to be tested
	 * @return commands need to be executed
	 */
	public static String[] createTestSuiteCmd(Subject subject) {
		return createD4JCmd(subject, "test");
	}

	/**
	 * build execution command for running a single test case
	 * 
	 * @param subject
	 *            : subject to be tested
	 * @param testcase
	 *            : test case to be executed
	 * @return commands need to be executed
	 */
	public static String[] createTestSingleCmd(Subject subject, String testcase) {
		return createD4JCmd(subject, "test -t " + testcase);
	}

	/**
	 * build execution command for compiling a subject
	 * 
	 * @param subject
	 *            : subject to be compiled
	 * @return commands need to be executed
	 */
	public static String[] createBuildSubjectCmd(Subject subject) {
		return createD4JCmd(subject, "compile");
	}

	/**
	 * create d4j command based on the given argument {@code args}
	 * 
	 * @param subject
	 *            : subject to be focused
	 * @param args
	 *            : command to be executed, e.g., "test", "compile", etc.
	 * @return command need to be executed
	 */
	private static String[] createD4JCmd(Subject subject, String args) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(Constant.COMMAND_CD + subject.getHome() + " && ");
		stringBuffer.append(Constant.COMMAND_D4J + args);
		String[] cmd = new String[] { "/bin/bash", "-c", stringBuffer.toString() };
		return cmd;
	}

	/**
	 * create model training command
	 * 
	 * @param subject
	 * @return
	 */
	public static String[] createTrainCmd(Subject subject) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("source ");
		stringBuffer.append(Constant.TENSORFLOW_ACTIVATE_PATH);
		stringBuffer.append(" && ");
		stringBuffer.append(Constant.COMMAND_CD);
		stringBuffer.append(Constant.STR_ML_HOME);
		stringBuffer.append(" && ");
		stringBuffer.append(Constant.COMMAND_PYTHON);
		stringBuffer.append(Constant.STR_ML_HOME + "/train_model.py ");
		stringBuffer.append(subject.getName());
		stringBuffer.append(" ");
		stringBuffer.append(subject.getId());
		stringBuffer.append(" ");
		stringBuffer.append(Constant.TRAINING_MODEL);
		stringBuffer.append(" && deactivate");
		String[] cmd = new String[] { "/bin/bash", "-c", stringBuffer.toString() };
		return cmd;
	}

	/**
	 * create predicate predicting command
	 * 
	 * @param subject
	 * @return
	 */
	public static String[] createPredictCmd(Subject subject) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("source ");
		stringBuffer.append(Constant.TENSORFLOW_ACTIVATE_PATH);
		stringBuffer.append(" && ");
		stringBuffer.append(Constant.COMMAND_CD);
		stringBuffer.append(Constant.STR_ML_HOME);
		stringBuffer.append(" && ");
		stringBuffer.append(Constant.COMMAND_PYTHON);
		stringBuffer.append(Constant.STR_ML_HOME + "/run_predict.py ");
		stringBuffer.append(subject.getName());
		stringBuffer.append(" ");
		stringBuffer.append(subject.getId());
		stringBuffer.append(" ");
		stringBuffer.append(Constant.TRAINING_MODEL);
		stringBuffer.append(" && deactivate");
		String[] cmd = new String[] { "/bin/bash", "-c", stringBuffer.toString() };
		return cmd;
	}
	
	public static String[] createL2SCommand(Subject subject, String... predictFlags) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("source ");
		stringBuffer.append(Constant.TENSORFLOW_ACTIVATE_PATH);
		stringBuffer.append(" && ");
		stringBuffer.append(Constant.COMMAND_CD);
		stringBuffer.append(Constant.STR_ML_HOME);
		stringBuffer.append(" && ");
		stringBuffer.append(Constant.COMMAND_PYTHON);
		stringBuffer.append(Constant.STR_ML_HOME + "/run_predict.py ");
		stringBuffer.append(subject.getName());
		stringBuffer.append(" ");
		stringBuffer.append(subject.getId());
		stringBuffer.append(" ");
		stringBuffer.append("l2s");
		for(String string : predictFlags) {
			stringBuffer.append(" ");
			stringBuffer.append(string);
		}
		stringBuffer.append(" && deactivate");
		String[] cmd = new String[] { "/bin/bash", "-c", stringBuffer.toString() };
		return cmd;
	}
}
