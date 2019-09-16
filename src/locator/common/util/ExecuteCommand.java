/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import locator.common.config.Constant;
import locator.common.java.Subject;
import locator.core.model.Model;
//import sun.awt.geom.Crossings.EvenOdd;

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
		final List<String> results = new ArrayList<String>();
		try {
			ProcessBuilder builder = getProcessBuilder(command);
			builder.redirectErrorStream(true);
			process = builder.start();
			final InputStream inputStream = process.getInputStream();
			
			Thread processReader = new Thread(){
				public void run() {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					String line;
					try {
						while((line = reader.readLine()) != null) {
							results.add(line + "\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			
			processReader.start();
			try {
				processReader.join();
				process.waitFor();
			} catch (InterruptedException e) {
				LevelLogger.error(__name__ + "#execute Process interrupted !");
				return "";
			}
		} catch (IOException e) {
			LevelLogger.error(__name__ + "#execute Process output redirect exception !");
		} finally {
			if (process != null) {
				process.destroy();
			}
			process = null;
		}
		
		String result = "";
		for(String s: results) {
			result += s;
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
	public static void executeTrain(Subject subject, Model model) throws IOException, InterruptedException {
		String[] cmd = CmdFactory.createTrainCmd(subject, model);
		executeAndOutputConsole(cmd);
	}
	
	/**
	 * execute evaluate command
	 * 
	 * @param subject
	 *            : subject for evaluating
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void executeEvaluate(Subject subject, Model model) throws IOException, InterruptedException {
		String[] cmd = CmdFactory.createEvaluateCmd(subject, model);
		executeAndOutputFile(cmd, Constant.STR_TMP_ML_LOG_FILE);
	}

	/**
	 * execute predicate predicting
	 * 
	 * @param subject
	 *            : subject for predicting
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void executePredict(Subject subject, Model model) throws IOException, InterruptedException {
		String[] cmd = CmdFactory.createPredictCmd(subject, model);
//		executeAndOutputConsole(cmd);
		executeAndOutputFile(cmd, Constant.STR_TMP_ML_LOG_FILE);
	}

	public static List<String> executeCompile(String[] command) throws IOException, InterruptedException {
		Process process = null;
		final List<String> results = new ArrayList<String>();
		try {
			ProcessBuilder builder = getProcessBuilder(command);
			builder.redirectErrorStream(true);
			process = builder.start();
			final InputStream inputStream = process.getInputStream();
			
			Thread processReader = new Thread(){
				public void run() {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					String line;
					try {
						while((line = reader.readLine()) != null) {
							results.add(line + "\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			
			processReader.start();
			try {
				processReader.join();
				process.waitFor();
			} catch (InterruptedException e) {
				LevelLogger.error(__name__ + "#execute Process interrupted !");
				return results;
			}
		} catch (IOException e) {
			LevelLogger.error(__name__ + "#execute Process output redirect exception !");
		} finally {
			if (process != null) {
				process.destroy();
			}
			process = null;
		}
		
		return results;
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
		Process process = null;
		try {
			ProcessBuilder builder = getProcessBuilder(command);
			builder.redirectErrorStream(true);
			process = builder.start();
			final InputStream inputStream = process.getInputStream();
			
			Thread processReader = new Thread(){
				public void run() {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					String line;
					try {
						while((line = reader.readLine()) != null) {
							LevelLogger.info(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			
			processReader.start();
			try {
				processReader.join();
				process.waitFor();
			} catch (InterruptedException e) {
				LevelLogger.error(__name__ + "#execute Process interrupted !");
			}
		} catch (IOException e) {
			LevelLogger.error(__name__ + "#execute Process output redirect exception !");
		} finally {
			if (process != null) {
				process.destroy();
			}
			process = null;
		}
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
	private static void executeAndOutputFile(String[] command, String outputFile)
			throws IOException, InterruptedException {
		final BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		Process process = null;
		final List<String> results = new ArrayList<String>();
		try {
			ProcessBuilder builder = getProcessBuilder(command);
			builder.redirectErrorStream(true);
			process = builder.start();
			final InputStream inputStream = process.getInputStream();
			
			Thread processReader = new Thread(){
				public void run() {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					String line;
					try {
						while((line = reader.readLine()) != null) {
							writer.write(line + "\n");
							writer.flush();
							results.add(line + "\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			
			processReader.start();
			try {
				processReader.join();
				process.waitFor();
			} catch (InterruptedException e) {
				LevelLogger.error(__name__ + "#execute Process interrupted !");
			}
			writer.close();
		} catch (IOException e) {
			LevelLogger.error(__name__ + "#execute Process output redirect exception !");
		} finally {
			if (process != null) {
				process.destroy();
			}
			process = null;
		}
	}
	
	private static ProcessBuilder getProcessBuilder(String[] command) { 
		ProcessBuilder builder = new ProcessBuilder(command);
		Map<String, String> evn = builder.environment();
		evn.put("JAVA_HOME", Constant.COMMAND_JAVA_HOME);
		evn.put("PATH", Constant.COMMAND_JAVA_HOME + "/bin:" + evn.get("PATH"));
		return builder;
	}
}
