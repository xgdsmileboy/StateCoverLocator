/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import locator.common.config.Constant;

/**
 * This class is an interface to run script command background
 * 
 * @author Jiajun
 *
 */
public class ExecuteCommand {

	private final static String __name__ = "@ExecuteCommand ";

	public static void deletePathFile() {
		File file = new File(Constant.STR_TMP_INSTR_OUTPUT_FILE);
		if (file.exists()) {
			file.delete();
		}
	}

	public static String moveFile(String source, String target) {
		String[] cmd = new String[] { "/bin/bash", "-c", Constant.COMMAND_MV + source + " " + target };
		return execute(cmd);
	}

	public static String moveFolder(String source, String target) {
		String[] cmd = new String[] { "/bin/bash", "-c", Constant.COMMAND_MV + "-f " + source + " " + target };
		return execute(cmd);
	}

	public static String copyFile(String source, String target) {
		String[] cmd = new String[] { "/bin/bash", "-c", Constant.COMMAND_CP + source + " " + target };
		return execute(cmd);
	}

	public static String deleteDataFiles() {
		String[] cmd = new String[] { "/bin/bash", "-c", Constant.COMMAND_RM + Constant.STR_ALL_DATA_COLLECT_PATH };
		return execute(cmd);
	}

	public static String deleteOutputFile() {
		String[] cmd = new String[] { "/bin/bash", "-c",
				Constant.COMMAND_RM + Constant.STR_OUT_PATH + Constant.PATH_SEPARATOR + "*" };
		return execute(cmd);
	}

	private static String execute(String... command) {
		Process process = null;
		String result = null;
		try {
			process = Runtime.getRuntime().exec(command);
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
			try {
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
		return result;
	}

	// public static void main(String[] args) {
	// DynamicRuntimeInfo _dynamicRuntimeInfo = new DynamicRuntimeInfo("chart",
	// 1);
	// String[] cmds =
	// InfoBuilder.buildDefects4JTestCommand(_dynamicRuntimeInfo,
	// "org.jfree.chart.renderer.category.junit.AbstractCategoryItemRendererTests",
	// "test2947660");
	//// newExecutedDefects4JTest(cmds);
	// executeDefects4JTest(cmds, Constant.STR_TMP_D4J_OUTPUT_FILE);
	// }

	public static void executeDefects4JTest(String[] command)
			throws IOException, InterruptedException {
		
		deletePathFile();

		File file = new File(Constant.STR_TMP_D4J_OUTPUT_FILE);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		final Process process = Runtime.getRuntime().exec(command);
		final FileOutputStream resultOutStream = new FileOutputStream(file);

		new Thread() {
			public void run() {
				InputStream errorInStream = new BufferedInputStream(process.getErrorStream());
				int num = 0;
				byte[] bs = new byte[1024];
				try {
					while ((num = errorInStream.read(bs)) != -1) {
						resultOutStream.write(bs, 0, num);
						resultOutStream.flush();
					}
				} catch (IOException e) {
					LevelLogger.fatal(__name__ + "#executeDefects4JTest Procss output redirect exception !", e);
				} finally {
					try {
						errorInStream.close();
					} catch (IOException e) {
					}
				}
			}
		}.start();

		new Thread() {
			public void run() {
				InputStream processInStream = new BufferedInputStream(process.getInputStream());
				int num = 0;
				byte[] bs = new byte[1024];
				try {
					while ((num = processInStream.read(bs)) != -1) {
						resultOutStream.write(bs, 0, num);
						resultOutStream.flush();
					}
				} catch (IOException e) {
					LevelLogger.fatal(__name__ + "#executeDefects4JTest Procss output redirect exception !", e);
				} finally {
					try {
						processInStream.close();
					} catch (IOException e) {
					}
				}
			}
		}.start();

		process.waitFor();
	}
}
