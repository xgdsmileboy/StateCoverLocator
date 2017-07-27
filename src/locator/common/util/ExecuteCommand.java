/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import locator.common.config.Constant;
import locator.common.java.Subject;

/**
 * This class is an interface to run script command background
 * 
 * @author Jiajun
 *
 */
public class ExecuteCommand {

	private final static String __name__ = "@ExecuteCommand ";

	/**
	 * delete file containing instrument outputs
	 */
	public static void deleteInstrumentOutputFile() {
		String[] cmd = new String[] { "/bin/bash", "-c", Constant.COMMAND_RM + Constant.STR_TMP_INSTR_OUTPUT_FILE };
		execute(cmd);
	}

	/**
	 * move a single file {@code source} to the file {@code target}
	 * 
	 * @param source
	 *            : source file to be moved
	 * @param target
	 *            : target file name
	 * @return execution information
	 */
	public static String moveFile(String source, String target) {
		String[] cmd = new String[] { "/bin/bash", "-c", Constant.COMMAND_MV + source + " " + target };
		return execute(cmd);
	}

	/**
	 * move the file folder {@code srcFolder} to the target file path
	 * {@code tarFolder}
	 * 
	 * @param srcFolder
	 *            : source folder to be moved
	 * @param tarFolder
	 *            : target folder
	 * @return execution information
	 */
	public static String moveFolder(String srcFolder, String tarFolder) {
		String[] cmd = new String[] { "/bin/bash", "-c", Constant.COMMAND_MV + "-f " + srcFolder + " " + tarFolder };
		return execute(cmd);
	}

	/**
	 * copy {@code source} file to {@code target} file
	 * 
	 * @param source
	 *            : source file to be copied
	 * @param target
	 *            : target file path
	 * @return execution information
	 */
	public static String copyFile(String source, String target) {
		String[] cmd = new String[] { "/bin/bash", "-c", Constant.COMMAND_CP + source + " " + target };
		return execute(cmd);
	}
	
	public static String copyFolder(String srcFolder, String tarFolder) {
		try {
			FileUtils.copyDirectory(new File(srcFolder), new File(tarFolder));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * delete all collected data
	 * 
	 * @return execution information
	 */
	public static String deleteGivenFile(String file) {
		String[] cmd = new String[] { "/bin/bash", "-c", Constant.COMMAND_RM + file };
		return execute(cmd);
	}
	
	public static String deleteGivenFolder(String folder) {
		String[] cmd = new String[] { "/bin/bash", "-c", Constant.COMMAND_RM + "-rf " + folder };
		return execute(cmd);
	}

	/**
	 * delete all collected data
	 * 
	 * @return execution information
	 */
	public static String deleteDataFiles() {
		String[] cmd = new String[] { "/bin/bash", "-c", Constant.COMMAND_RM + Constant.STR_ALL_DATA_COLLECT_PATH };
		return execute(cmd);
	}

	/**
	 * delete all output files
	 * 
	 * @return execution information
	 */
	public static String deleteOutputFile() {
		String[] cmd = new String[] { "/bin/bash", "-c",
				Constant.COMMAND_RM + Constant.STR_OUT_PATH + Constant.PATH_SEPARATOR + "*" };
		return execute(cmd);
	}

	/**
	 * execute given commands
	 * 
	 * @param command
	 *            : command to be executed
	 * @return output information when running given command
	 */
	private static String execute(String... command) {
		Process process = null;
		String result = null;
		try {
			process = Runtime.getRuntime().exec(command);
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				LevelLogger.error(__name__ + "#execute Process interrupted !");
				return "";
			}
			ByteArrayOutputStream resultOutStream = new ByteArrayOutputStream();
			InputStream errorInStream = new BufferedInputStream(process.getErrorStream());
			InputStream processInStream = new BufferedInputStream(process.getInputStream());
			int num = 0;
			byte[] bs = new byte[1024];
			while ((num = errorInStream.read(bs)) != -1) {
				resultOutStream.write(bs, 0, num);
			}
			while ((num = processInStream.read(bs)) != -1) {
				resultOutStream.write(bs, 0, num);
			}
			result = new String(resultOutStream.toByteArray());
			errorInStream.close();
			errorInStream = null;
			processInStream.close();
			processInStream = null;
			resultOutStream.close();
			resultOutStream = null;
		} catch (IOException e) {
			LevelLogger.error(__name__ + "#execute Process output redirect exception !");
		} finally {
			if (process != null) {
				process.destroy();
			}
			process = null;
		}
		return result;
	}

	/**
	 * execute d4j test command
	 * 
	 * @param command
	 *            : commands to be executed
	 * @throws IOException
	 *             : when the file does not exist for d4j output
	 * @throws InterruptedException
	 *             : when current process is interrupted
	 */
	public static void executeDefects4JTest(String[] command) throws IOException, InterruptedException {

		deleteInstrumentOutputFile();

		File file = new File(Constant.STR_TMP_D4J_OUTPUT_FILE);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		executeAndOutputFile(command, Constant.STR_TMP_D4J_OUTPUT_FILE);
	}

	/**
	 * execute train command
	 * 
	 * @param subject
	 *            : subject for training
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void executeTrain(Subject subject) throws IOException, InterruptedException {
		String[] cmd = CmdFactory.createTrainCmd(subject);
		executeAndOutputConsole(cmd);
	}

	/**
	 * execute predicate predicting
	 * 
	 * @param subject
	 *            : subject for predicting
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void executePredict(Subject subject) throws IOException, InterruptedException {
		String[] cmd = CmdFactory.createPredictCmd(subject);
//		executeAndOutputConsole(cmd);
		executeAndOutputFile(cmd, Constant.STR_TMP_ML_LOG_FILE);
	}

	public static List<String> executeCompile(String[] command) throws IOException, InterruptedException {
		List<String> message = new ArrayList<>();
		message.add(execute(command));
		return message;
	}
	
	/**
	 * execute outside command when given command and output execution
	 * information to console
	 * 
	 * @param command
	 *            : to be executed
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static void executeAndOutputConsole(String[] command) throws IOException, InterruptedException {
		LevelLogger.info(execute(command));
	}

	/**
	 * execute given command and output execution information into file
	 * 
	 * @param command
	 *            : command to be executed
	 * @param outputFile
	 *            : output file path
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static String executeAndOutputFile(String[] command, String outputFile)
			throws IOException, InterruptedException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		String output = execute(command);
		writer.write(output);
		writer.close();
		return output;
	}
}
