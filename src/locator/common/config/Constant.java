/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.pku.sei.conditon.simple.SplIfAndVariablePairVisitor;
import edu.pku.sei.conditon.simple.SplIfStmtVisitor;
import locator.common.util.LevelLogger;

/**
 * This class contains all constant variables
 * 
 * @author Jiajun
 * @date May 9, 2017
 */
public class Constant {

	private final static String __name__ = "@Constant ";

	// used for instrument
	public final static String INSTRUMENT_DOT_SEPARATOR = ".";
	public final static String INSTRUMENT_STR_SEP = "";//"#"
	public final static String INSTRUMENT_FLAG = "";//"[INST]"
	public final static String INSTRUMENT_K_TEST = "T";
	public final static String INSTRUMENT_K_SOURCE = "M";
	public final static String HOME = System.getProperty("user.dir");

	// common info
	public final static String SOURCE_FILE_SUFFIX = ".java";
	public final static String PATH_SEPARATOR = System.getProperty("file.separator");

	// least length for failed test trace
	public final static int TRACE_LENGTH_FOR_FAILED_TEST = 20;

	// instrument top K predicates for each variable
	public final static int TOP_K_PREDICTED_PREDICATE = 2;

	// build flags
	public final static String ANT_BUILD_FAILED = "BUILD FAILED";
	public final static String ANT_BUILD_SUCCESS = "BUILD SUCCESSFUL";
	
	// header for feature
	public final static String FEATURE_VAR_HEADER = SplIfAndVariablePairVisitor.getVarHeader();
	public final static String FEATURE_EXPR_HEADER = SplIfStmtVisitor.getExprHeader();
	public final static int FEATURE_VAR_NAME_INDEX = 5;
	public final static int FEATURE_VAR_TYPE_INDEX = 6;
	public final static int FEATURE_FILE_NAME_INDEX = 3;
	
	// system command
	public static String COMMAND_CD = null;
	public static String COMMAND_RM = null;
	public static String COMMAND_MV = null;
	public static String COMMAND_CP = null;
	public static String COMMAND_JAVA = null;
	public static String COMMAND_D4J = null;
	public static String COMMAND_PYTHON = null;

	public static String DUMPER_HOME = null;
	public static String PROJECT_HOME = null;

	// system properties
	/**
	 * out
	 */
	public final static String STR_OUT_PATH = HOME + "/out";
	/**
	 * info
	 */
	public final static String STR_INFO_OUT_PATH = HOME + "/info";
	/**
	 * out/debug.log
	 */
	public final static String STR_LOG_FILE = STR_OUT_PATH + "/debug.log";
	/**
	 * out/d4j.out
	 */
	public final static String STR_TMP_D4J_OUTPUT_FILE = STR_OUT_PATH + "/d4j.out";
	/**
	 * out/ml.out
	 */
	public final static String STR_TMP_ML_LOG_FILE = STR_OUT_PATH + "/ml.out";
	/**
	 * out/path.out
	 */
	public final static String STR_TMP_INSTR_OUTPUT_FILE = STR_OUT_PATH + "/path.out";
	/**
	 * out/failed.test
	 */
	public final static String STR_FAILED_TEST_FILE = STR_OUT_PATH + "/failed.test";
	/**
	 * out/passed.test
	 */
	public final static String STR_PASSED_TEST_FILE = STR_OUT_PATH + "/passed.test";
	/**
	 * out/data
	 */
	public final static String STR_ALL_DATA_COLLECT_PATH = STR_OUT_PATH + "/data";

	/**
	 * Machine learning home path TODO : need to refactor
	 */
	public final static String STR_ML_HOME = HOME + "/python";
	public final static String STR_ML_VAR_OUT_FILE_PATH = STR_ML_HOME + "/input";
	public final static String STR_ML_EXP_OUT_FILE_PATH = STR_ML_HOME + "/input";
	public final static String STR_ML_PREDICT_EXP_PATH = STR_ML_HOME + "/output";

	static {
		Properties prop = new Properties();
		try {
			Constant.DUMPER_HOME = Constant.HOME;
			String filePath = Constant.HOME + "/res/conf/system.properties";
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			prop.load(in);

			// System commands
			Constant.COMMAND_JAVA = prop.getProperty("COMMAND.JAVA").replace("/", Constant.PATH_SEPARATOR) + " ";
			Constant.COMMAND_CD = prop.getProperty("COMMAND.CD").replace("/", Constant.PATH_SEPARATOR) + " ";
			Constant.COMMAND_CP = prop.getProperty("COMMAND.CP").replace("/", Constant.PATH_SEPARATOR) + " ";
			// for deleting files
			Constant.COMMAND_RM = prop.getProperty("COMMAND.RM").replace("/", Constant.PATH_SEPARATOR) + " -rf ";
			// for backup file
			Constant.COMMAND_MV = prop.getProperty("COMMAND.MV").replace("/", Constant.PATH_SEPARATOR) + " ";

			Constant.COMMAND_D4J = prop.getProperty("COMMAND.D4J").replace("/", Constant.PATH_SEPARATOR) + " ";

			Constant.COMMAND_PYTHON = prop.getProperty("COMMAND.PYTHON").replace("/", Constant.PATH_SEPARATOR) + " ";

			in.close();
		} catch (IOException e) {
			LevelLogger.error(__name__ + "#config_system get properties failed!" + e.getMessage());
		}
	}

}