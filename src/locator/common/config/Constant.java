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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;

import jdk7.wrapper.JCompiler.SOURCE_LEVEL;
import locator.aux.extractor.core.feature.ExprFeature;
import locator.aux.extractor.core.feature.VarFeature;
import locator.aux.extractor.core.feature.item.FileName;
import locator.aux.extractor.core.feature.item.LineNumber;
import locator.aux.extractor.core.feature.item.VarName;
import locator.aux.extractor.core.feature.item.VarType;
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

	// least length for failed test trace
	public final static int TRACE_LENGTH_FOR_FAILED_TEST = 20;
	
	public static int AST_LEVEL = AST.JLS8;
	public static String JAVA_VERSION = JavaCore.VERSION_1_7;

	// instrument top K predicates for each variable
	public static int TOP_K_PREDICATES_FOR_EACH_VAR = 5;

	// build flags
	public final static String ANT_BUILD_FAILED = "BUILD FAILED";
	public final static String ANT_BUILD_SUCCESS = "BUILD SUCCESSFUL";
	
	// header for feature
	public final static String FEATURE_VAR_HEADER = VarFeature.getFeatureHeader();
	public final static String FEATURE_EXPR_HEADER = ExprFeature.getFeatureHeader();
	public final static int FEATURE_VAR_NAME_INDEX = VarFeature.getFeatureIndex(VarName.class);
	public final static int FEATURE_VAR_TYPE_INDEX = VarFeature.getFeatureIndex(VarType.class);
	public final static int FEATURE_FILE_NAME_INDEX = ExprFeature.getFeatureIndex(FileName.class);
	public final static int FEATURE_LINE_INDEX = ExprFeature.getFeatureIndex(LineNumber.class);
	
	// variables collecting flag
	public static boolean PREDICT_LEFT_HAND_SIDE_VARIABLE = false;
	
	// update predicate
	public static boolean RECOVER_PREDICATE_FROM_FILE = true;
	public static boolean USE_STATISTICAL_DEBUGGING = true;
	public static boolean USE_SOBER = false;
	
	public static boolean OUT_BRANCH_COVERAGE = false; 
	
	public static boolean ADD_NULL_PREDICATE_FOR_ASSGIN = true;
	
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
	public static Map<String, ProjectProperties> PROJECT_PROP = new HashMap<String, ProjectProperties>();
	public static Map<String, Integer> BUG_NUMBER = new HashMap<String, Integer>();

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
	 * out/error
	 */
	public final static String STR_ERROR_BACK_UP = STR_OUT_PATH + "/error";
	/**
	 * out/evaluation
	 */
	public final static String STR_ML_EVALUATION = STR_OUT_PATH + "/evaluation";
	
	public final static String TIME_LOG = STR_OUT_PATH + "/time.log";

	/**
	 * Machine learning home path TODO : need to refactor
	 */
	public final static String STR_ML_HOME = HOME + "/python";
	public final static String STR_ML_OUT_FILE_PATH = STR_ML_HOME + "/input";
	public final static String STR_ML_PREDICT_EXP_PATH = STR_ML_HOME + "/output";
	public static String TENSORFLOW_ACTIVATE_PATH = "";
	
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
			Constant.TENSORFLOW_ACTIVATE_PATH = prop.getProperty("TENSORFLOW.ACTIVATE").replace("/", Constant.PATH_SEPARATOR);
			in.close();
		} catch (IOException e) {
			LevelLogger.error(__name__ + "#config_system get properties failed!" + e.getMessage());
		}
		
		try {
			String filePath = Constant.HOME + "/res/conf/project.properties";
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			prop.load(in);
			String base = HOME + PATH_SEPARATOR + "res" + PATH_SEPARATOR + "d4jlibs";
			for(String name : Constant.PROJECT_NAME) {
				String ssrc = prop.getProperty(name.toUpperCase() + ".SSRC").replace("/", Constant.PATH_SEPARATOR);
				String tsrc = prop.getProperty(name.toUpperCase() + ".TSRC").replace("/", Constant.PATH_SEPARATOR);
				String sbin = prop.getProperty(name.toUpperCase() + ".SBIN").replace("/", Constant.PATH_SEPARATOR);
				String tbin = prop.getProperty(name.toUpperCase() + ".TBIN").replace("/", Constant.PATH_SEPARATOR);
				String sourceLevel = prop.getProperty(name.toUpperCase() + ".SRCLEVEL");
				if (sourceLevel == null || sourceLevel.length() == 0) {
					sourceLevel = "1.6";
				}
				String targetLevel = prop.getProperty(name.toUpperCase() + ".TARLEVEL");
				if(targetLevel == null || targetLevel.length() == 0) {
					targetLevel = "1.6";
				}
				List<String> classpath = new LinkedList<>();
				switch(name) {
				case "math":
					classpath.add(base + "/hamcrest-core-1.3.jar");
					classpath.add(base + "/junit-4.11.jar");
					break;
				case "chart":
					classpath.add(base + "/junit-4.11.jar");
					classpath.add(base + "/iText-2.1.4.jar");
					classpath.add(base + "/servlet.jar");
					break;
				case "lang":
					classpath.add(base + "/cglib-nodep-2.2.2.jar");
					classpath.add(base + "/commons-io-2.4.jar");
					classpath.add(base + "/easymock-3.1.jar");
					classpath.add(base + "/hamcrest-core-1.3.jar");
					classpath.add(base + "/junit-4.11.jar");
					classpath.add(base + "/objenesis-1.2.jar");
					break;
				case "closure":
					classpath.add(base + "/caja-r4314.jar");
					classpath.add(base + "/jarjar.jar");
					classpath.add(base + "/ant.jar");
					classpath.add(base + "/ant-launcher.jar");
					classpath.add(base + "/args4j.jar");
					classpath.add(base + "/jsr305.jar");
					classpath.add(base + "/guava.jar");
					classpath.add(base + "/json.jar");
					classpath.add(base + "/protobuf-java.jar");
					classpath.add(base + "/junit-4.11.jar");
					classpath.add(base + "/rhino.jar");
					break;
				case "time":
					classpath.add(base + "/junit-4.11.jar");
					classpath.add(base + "/joda-convert-1.2.jar");
					break;
				case "mockito":
					break;
				default :
					System.err.println("UNKNOWN project name : " + name);
				}
				Constant.PROJECT_PROP.put(name, new ProjectProperties(ssrc, tsrc, sbin, tbin, classpath, SOURCE_LEVEL.toSourceLevel(sourceLevel), SOURCE_LEVEL.toSourceLevel(targetLevel)));
			}
			in.close();
		} catch (IOException e) {
			LevelLogger.error(__name__ + "#config_project get properties failed!" + e.getMessage());
		}
		
		try {
			String filePath = Constant.HOME + "/res/conf/runtime.properties";
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			prop.load(in);
			
			Constant.PROJECT_HOME = prop.getProperty("PROJECT.HOME").replace("/", Constant.PATH_SEPARATOR);
			Constant.RECOVER_PREDICATE_FROM_FILE = Boolean.parseBoolean(prop.getProperty("PREDICATE.RECOVER"));
			Constant.TOP_K_PREDICATES_FOR_EACH_VAR = Integer.parseInt(prop.getProperty("PREDICATE.TOPK"));
			Constant.TRAINING_MODEL = prop.getProperty("TRAINING.MODEL").trim();
			Constant.RE_TRAIN_MODEL = Boolean.parseBoolean(prop.getProperty("MODEL.RETRAIN"));
			Constant.TRAINING_EVALUATION =  Boolean.parseBoolean(prop.getProperty("TRAINING.EVALUATION"));
			Constant.USE_SOBER =  Boolean.parseBoolean(prop.getProperty("USE.SOBER"));
			Constant.USE_STATISTICAL_DEBUGGING =  Boolean.parseBoolean(prop.getProperty("USE.STATISTICAL.DEBUGGING"));
			Constant.PREDICT_LEFT_HAND_SIDE_VARIABLE = Boolean.parseBoolean(prop.getProperty("PREDICT.LEFT.VAR"));
			Constant.OUT_BRANCH_COVERAGE = Boolean.parseBoolean(prop.getProperty("BRANCH.COVERAGE"));
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