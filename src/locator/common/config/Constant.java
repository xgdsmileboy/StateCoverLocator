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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;

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
//	public final static String INSTRUMENT_STR_SEP = "";//"#"
	public final static String INSTRUMENT_FLAG = "";//"[INST]"
	public final static String INSTRUMENT_K_TEST = "T";
	public final static String INSTRUMENT_K_SOURCE = "M";
	public final static String HOME = System.getProperty("user.dir");

	// common info
	public final static String SOURCE_FILE_SUFFIX = ".java";
	public final static String PATH_SEPARATOR = System.getProperty("file.separator");

	public static int AST_LEVEL = AST.JLS8;
	public static String JAVA_VERSION = JavaCore.VERSION_1_7;

	// instrument top K predicates for each variable
	public static int TOP_K_PREDICATES_FOR_EACH_VAR = 5;

	// time limit for running test suite with instrument predicates in minutes
	public static int TIME_OUT_RUN_TEST_SUITE = 60;
	
	// build flags
	public final static String ANT_BUILD_FAILED = "BUILD FAILED";
	public final static String ANT_BUILD_SUCCESS = "BUILD SUCCESSFUL";
	
	// variables collecting flag
	public static boolean BOOL_PREDICT_LEFT_VARIABLE = false;
	
	public static boolean BOOL_RECOMPUTE_ORI = false;
	
	// update predicate
	public static boolean BOOL_RECOVER_PREDICATE_FROM_FILE = true;
	public static boolean BOOL_USE_STATISTICAL_DEBUGGING = true;
	public static boolean BOOL_USE_SOBER = false;
	
	public static boolean BOOL_OUT_BRANCH_COVERAGE = false; 
	
	public static boolean BOOL_ADD_NULL_PREDICATE_FOR_ASSGIN = true;
	
	// training model
	// xgboost, dnn or randomforest
	public static String TRAINING_MODEL = "dnn";
	public static boolean RE_TRAIN_MODEL = false;
	public static boolean TRAINING_EVALUATION = false;
	
	// system command
	public static String COMMAND_CD = null;
	public static String COMMAND_RM = null;
	public static String COMMAND_MV = null;
	public static String COMMAND_CP = null;
	public static String COMMAND_TIMEOUT = null;
	public static String COMMAND_JAVA = null;
	public static String COMMAND_D4J = null;
	public static String COMMAND_PYTHON = null;
	public static String COMMAND_JAVA_HOME = null;

	public static String DUMPER_HOME = null;
	public static String PROJECT_HOME = null;
	
	// project properties
	public static final String [] PROJECT_NAME = {"chart", "closure", "lang", "math", "time"};
	public static Map<String, Integer> BUG_NUMBER = new HashMap<String, Integer>();

	// system properties
	/*
	 * res/d4jlibs
	 */
	public final static String STR_PROJ_DEPENDCE_PATH = HOME + PATH_SEPARATOR + "res" + PATH_SEPARATOR + "d4jlibs";
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
	 * out/error
	 */
	public final static String STR_ERROR_BACK_UP = STR_OUT_PATH + "/error";
	/**
	 * out/evaluation
	 */
	public final static String STR_ML_EVALUATION = STR_OUT_PATH + "/evaluation";
	
	/**
	 * out/time.log
	 */
	public final static String STR_TIME_LOG = STR_OUT_PATH + "/time.log";

	/**
	 * rlst.log
	 */
	public final static String STR_RESULT_RECORD_LOG = HOME + "/rlst.log";
	
	/*
	 * Machine learning home path TODO : need to refactor
	 */
	/**
	 * python
	 */
	public final static String STR_ML_HOME = HOME + "/python";
	/**
	 * python/input
	 */
	public final static String STR_ML_OUT_FILE_PATH = STR_ML_HOME + "/input";
	/**
	 * python/output
	 */
	public final static String STR_ML_PREDICT_EXP_PATH = STR_ML_HOME + "/output";
	public static String STR_TENSORFLOW_ACTIVATE_PATH;
	
	public enum PredicateStatement {
		IF, WHILE, DO, RETURN, FOR, ASSIGN, SWITCH
	};
	
	static {
		Properties prop = new Properties();
		try {
			Constant.DUMPER_HOME = Constant.HOME;
			String filePath = Constant.HOME + "/res/conf/system.properties";
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			prop.load(in);

			// System commands
			Constant.COMMAND_JAVA_HOME = prop.getProperty("COMMAND.JAVA_HOME").replace("/", Constant.PATH_SEPARATOR);
			Constant.COMMAND_JAVA = COMMAND_JAVA_HOME + Constant.PATH_SEPARATOR + "bin" + Constant.PATH_SEPARATOR + "java ";
			
			Constant.COMMAND_CD = prop.getProperty("COMMAND.CD").replace("/", Constant.PATH_SEPARATOR) + " ";
			Constant.COMMAND_CP = prop.getProperty("COMMAND.CP").replace("/", Constant.PATH_SEPARATOR) + " ";
			// for deleting files
			Constant.COMMAND_RM = prop.getProperty("COMMAND.RM").replace("/", Constant.PATH_SEPARATOR) + " -rf ";
			Constant.COMMAND_TIMEOUT = prop.getProperty("COMMAND.TIMEOUT").replaceAll("/", Constant.PATH_SEPARATOR) + " ";
			// for backup file
			Constant.COMMAND_MV = prop.getProperty("COMMAND.MV").replace("/", Constant.PATH_SEPARATOR) + " ";
			Constant.COMMAND_D4J = prop.getProperty("COMMAND.D4J").replace("/", Constant.PATH_SEPARATOR) + " ";
			Constant.COMMAND_PYTHON = prop.getProperty("COMMAND.PYTHON").replace("/", Constant.PATH_SEPARATOR) + " ";
			Constant.STR_TENSORFLOW_ACTIVATE_PATH = prop.getProperty("TENSORFLOW.ACTIVATE").replace("/", Constant.PATH_SEPARATOR);
			in.close();
		} catch (IOException e) {
			LevelLogger.error(__name__ + "#config_system get properties failed!" + e.getMessage());
		}
		
		try {
			String filePath = Constant.HOME + "/res/conf/runtime.properties";
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			prop.load(in);
			
			Constant.PROJECT_HOME = prop.getProperty("PROJECT.HOME").replace("/", Constant.PATH_SEPARATOR);
			Constant.BOOL_RECOVER_PREDICATE_FROM_FILE = Boolean.parseBoolean(prop.getProperty("PREDICATE.RECOVER"));
			Constant.TOP_K_PREDICATES_FOR_EACH_VAR = Integer.parseInt(prop.getProperty("PREDICATE.TOPK"));
			Constant.TRAINING_MODEL = prop.getProperty("TRAINING.MODEL").trim();
			Constant.RE_TRAIN_MODEL = Boolean.parseBoolean(prop.getProperty("MODEL.RETRAIN"));
			Constant.TRAINING_EVALUATION =  Boolean.parseBoolean(prop.getProperty("TRAINING.EVALUATION"));
			Constant.BOOL_USE_SOBER =  Boolean.parseBoolean(prop.getProperty("USE.SOBER"));
			Constant.BOOL_USE_STATISTICAL_DEBUGGING =  Boolean.parseBoolean(prop.getProperty("USE.STATISTICAL.DEBUGGING"));
			Constant.BOOL_PREDICT_LEFT_VARIABLE = Boolean.parseBoolean(prop.getProperty("PREDICT.LEFT.VAR"));
			Constant.BOOL_OUT_BRANCH_COVERAGE = Boolean.parseBoolean(prop.getProperty("BRANCH.COVERAGE"));
			Constant.TIME_OUT_RUN_TEST_SUITE = Integer.parseInt(prop.getProperty("TEST.TIMEOUT"));
			Constant.BOOL_RECOMPUTE_ORI = Boolean.parseBoolean(prop.getProperty("RECOMPUTE.ORI"));
			in.close();
		} catch (IOException e) {
			LevelLogger.error(__name__ + "#config_runtime get properties failed!" + e.getMessage());
		}
		
		BUG_NUMBER.put("chart", 26);
		BUG_NUMBER.put("closure", 133);
		BUG_NUMBER.put("lang", 65);
		BUG_NUMBER.put("math", 106);
		BUG_NUMBER.put("time", 27);
		BUG_NUMBER.put("mockito", 38);
	}
}