/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import locator.common.config.Constant;
import locator.common.util.LevelLogger;

/**
 * This class contains some auxiliary methods that provide convenience
 * 
 * @author Jiajun
 *
 */

public class JavaFile {

	private final static String __name__ = "@JavaFile ";

	
	public static CompilationUnit genAST(String fileName){
		return (CompilationUnit)genASTFromSource(readFileToString(fileName), ASTParser.K_COMPILATION_UNIT);
	}
	/**
	 * generate {@code CompilationUnit} from {@code ICompilationUnit}
	 * 
	 * @param icu
	 * @return
	 */
	public static CompilationUnit genASTFromICU(ICompilationUnit icu) {
		ASTParser astParser = ASTParser.newParser(Constant.AST_LEVEL);
		Map<?, ?> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(Constant.JAVA_VERSION, options);
		astParser.setCompilerOptions(options);
		astParser.setSource(icu);
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);
		astParser.setResolveBindings(true);
		return (CompilationUnit) astParser.createAST(null);
	}

	/**
	 * generate {@code CompilationUnit} from source code based on the specific
	 * type (e.g., {@code ASTParser.K_COMPILATION_UNIT})
	 * 
	 * @param icu
	 * @param type
	 * @return
	 */
	public static ASTNode genASTFromSource(String icu, int type) {
		ASTParser astParser = ASTParser.newParser(Constant.AST_LEVEL);
		Map<?, ?> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(Constant.JAVA_VERSION, options);
		astParser.setCompilerOptions(options);
		astParser.setSource(icu.toCharArray());
		astParser.setKind(type);
		astParser.setResolveBindings(true);
		return astParser.createAST(null);
	}
	
	public static ASTNode genASTFromSourceWithType(String icu, int type, String filePath, Subject subject) {
		ASTParser astParser = ASTParser.newParser(Constant.AST_LEVEL);
		Map<?, ?> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(Constant.JAVA_VERSION, options);
		astParser.setCompilerOptions(options);
		astParser.setSource(icu.toCharArray());
		astParser.setKind(type);
		astParser.setResolveBindings(true);
		astParser.setEnvironment(getClassPath(), new String[] {subject.getHome() + Constant.PATH_SEPARATOR + subject.getSsrc()}, null, true);
		astParser.setUnitName(filePath);
		astParser.setBindingsRecovery(true);
		return astParser.createAST(null);
	}
	
	/**
	 * create {@code CompilationUnit} from given file {@code srcFile}
	 * @param srcFile : absolute path of java source code
	 * @param srcPath : base path of source code
	 * @return a compilation unit
	 */
	public static CompilationUnit genASTFromFileWithType(String srcFile, String srcPath) {
		return (CompilationUnit) genASTFromSourceWithType(readFileToString(srcFile), ASTParser.K_COMPILATION_UNIT, srcFile, srcPath);
	}
	
	/**
	 * @param icu
	 * @param type
	 * @param filePath
	 * @param srcPath
	 * @return
	 * @see #genASTFromSourceWithType(String icu, String jversion, int astLevel, int
	 *      type, String filePath, String srcPath)
	 */
	public static ASTNode genASTFromSourceWithType(String icu, int type, String filePath, String srcPath) {
		return genASTFromSourceWithType(icu, Constant.JAVA_VERSION, Constant.AST_LEVEL, type, filePath, srcPath);
	}
	
	/**
	 * @param icu
	 * @param jversion
	 * @param type
	 * @param filePath
	 * @param srcPath
	 * @return
	 * @see #genASTFromSourceWithType(String icu, String jversion, int astLevel, int
	 *      type, String filePath, String srcPath)
	 */
	public static ASTNode genASTFromSourceWithType(String icu, String jversion, int type, String filePath, String srcPath) {
		return genASTFromSourceWithType(icu, jversion, Constant.AST_LEVEL, type, filePath, srcPath);
	}
	
	/**
	 * @param icu
	 * @param astLevel
	 * @param type
	 * @param filePath
	 * @param srcPath
	 * @return
	 * @see #genASTFromSourceWithType(String icu, String jversion, int astLevel, int
	 *      type, String filePath, String srcPath)
	 */
	public static ASTNode genASTFromSourceWithType(String icu, int astLevel, int type, String filePath, String srcPath) {
		return genASTFromSourceWithType(icu, Constant.JAVA_VERSION, astLevel, type, filePath, srcPath);
	}
	
	/**
	 * @param icu
	 *            : source code with {@code String} format
	 * @param jversion
	 *            : the version of JAVA, can be one of the following:
	 *            <ul>
	 *            <li>{@code JavaCore.VERSION_1_1}</li>
	 *            <li>{@code JavaCore.VERSION_1_2}</li>
	 *            <li>{@code JavaCore.VERSION_1_3}</li>
	 *            <li>{@code JavaCore.VERSION_1_4}</li>
	 *            <li>{@code JavaCore.VERSION_1_5}</li>
	 *            <li>{@code JavaCore.VERSION_1_6}</li>
	 *            <li>{@code JavaCore.VERSION_1_7}</li>
	 *            <li>{@code JavaCore.VERSION_1_8}</li>
	 *            </ul>
	 * @param astLevel
	 *            : AST level of created AST, can be one of the following:
	 *            <ul>
	 *            <li>{@code AST.JLS2}</li>
	 *            <li>{@code AST.JLS3}</li>
	 *            <li>{@code AST.JLS4}</li>
	 *            <li>{@code AST.JLS8}</li>
	 *            </ul>
	 * @param type
	 *            : the type of AST node, can be one of the following:
	 *            <ul>
	 *            <li>{@code ASTParser.K_CLASS_BODY_DECLARATIONS}</li>
	 *            <li>{@code ASTParser.K_COMPILATION_UNIT}</li>
	 *            <li>{@code ASTParser.K_EXPRESSION}</li>
	 *            <li>{@code ASTParser.K_STATEMENTS}</li>
	 *            </ul>
	 * @param filePath
	 *            : source file absolute path
	 * @param srcPath
	 *            : the base of source file
	 * @return AST
	 */
	public static ASTNode genASTFromSourceWithType(String icu, String jversion, int astLevel, int type, String filePath, String srcPath) {
		ASTParser astParser = ASTParser.newParser(astLevel);
		Map<?, ?> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(jversion, options);
		astParser.setCompilerOptions(options);
		astParser.setSource(icu.toCharArray());
		astParser.setKind(type);
		astParser.setResolveBindings(true);
		astParser.setEnvironment(getClassPath(), new String[] {srcPath}, null, true);
		astParser.setUnitName(filePath);
		astParser.setBindingsRecovery(true);
		return astParser.createAST(null);
	}
	
	private static String[] getClassPath() {
		String property = System.getProperty("java.class.path", ".");
		return property.split(File.pathSeparator);
	}

	/**
	 * write {@code string} into file with mode as "not append"
	 * 
	 * @param filePath
	 *            : path of file
	 * @param string
	 *            : message
	 * @return
	 */
	public static boolean writeStringToFile(String filePath, String string) {
		return writeStringToFile(filePath, string, false);
	}

	/**
	 * write {@code string} to file with mode as "not append"
	 * 
	 * @param file
	 *            : file of type {@code File}
	 * @param string
	 *            : message
	 * @return
	 */
	public static boolean writeStringToFile(File file, String string) {
		return writeStringToFile(file, string, false);
	}

	/**
	 * write {@code string} into file with specific mode
	 * 
	 * @param filePath
	 *            : file path
	 * @param string
	 *            : message
	 * @param append
	 *            : writing mode
	 * @return
	 */
	public static boolean writeStringToFile(String filePath, String string, boolean append) {
		if (filePath == null) {
			LevelLogger.error(__name__ + "#writeStringToFile Illegal file path : null.");
			return false;
		}
		File file = new File(filePath);
		return writeStringToFile(file, string, append);
	}

	/**
	 * write {@code string} into file with specific mode
	 * 
	 * @param file
	 *            : file of type {@code File}
	 * @param string
	 *            : message
	 * @param append
	 *            : writing mode
	 * @return
	 */
	public static boolean writeStringToFile(File file, String string, boolean append) {
		if (file == null) {
			LevelLogger.error(__name__ + "#writeStringToFile Illegal arguments (File) : null.");
			return false;
		}
		if (string == null) {
			LevelLogger.error(__name__ + "#writeStringToFile Illegal arguments (string) : null.");
			return false;
		}
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				LevelLogger.error(__name__ + "#writeStringToFile Create new file failed : " + file.getAbsolutePath());
				return false;
			}
		}
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			bufferedWriter.write(string);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * read string from file
	 * 
	 * @param filePath
	 *            : file path
	 * @return : string in the file
	 */
	public static String readFileToString(String filePath) {
		if (filePath == null) {
			LevelLogger.error(__name__ + "#readFileToString Illegal input file path : null.");
			return new String();
		}
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			LevelLogger.error(__name__ + "#readFileToString Illegal input file path : " + filePath);
			return new String();
		}
		return readFileToString(file);
	}

	/**
	 * read string from file
	 * 
	 * @param file
	 *            : file of type {@code File}
	 * @return : string in the file
	 */
	public static String readFileToString(File file) {
		if (file == null) {
			LevelLogger.error(__name__ + "#readFileToString Illegal input file : null.");
			return new String();
		}
		StringBuffer stringBuffer = new StringBuffer();
		InputStream in = null;
		InputStreamReader inputStreamReader = null;
		try {
			in = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(in, "UTF-8");
			char[] ch = new char[1024];
			int readCount = 0;
			while ((readCount = inputStreamReader.read(ch)) != -1) {
				stringBuffer.append(ch, 0, readCount);
			}
			inputStreamReader.close();
			in.close();

		} catch (Exception e) {
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e1) {
					return new String();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					return new String();
				}
			}
		}
		return stringBuffer.toString();
	}
	
	/**
	 * read string from file
	 * 
	 * @param filePath
	 *            : file path
	 * @return : list of string in the file
	 */
	public static List<String> readFileToStringList(String filePath) {
		if (filePath == null) {
			LevelLogger.error(__name__ + "#readFileToStringList Illegal input file path : null.");
			return new ArrayList<String>();
		}
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			LevelLogger.error(__name__ + "#readFileToString Illegal input file path : " + filePath);
			return new ArrayList<String>();
		}
		return readFileToStringList(file);
	}
	
	/**
	 * read string from file
	 * 
	 * @param file
	 *            : file of type {@code File}
	 * @return : list of string in the file
	 */
	public static List<String> readFileToStringList(File file) {
		List<String> results = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				results.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			LevelLogger.error(__name__ + "#readFileToStringList file not found : " + file.getAbsolutePath());
		} catch (IOException e) {
			LevelLogger.error(__name__ + "#readFileToStringList IO exception : " + file.getAbsolutePath(), e);
		}
		return results;
	}

	/**
	 * iteratively search files with the root as {@code file}
	 * 
	 * @param file
	 *            : root file of type {@code File}
	 * @param fileList
	 *            : list to save all the files
	 * @return : a list of all files
	 */
	public static List<File> ergodic(File file, List<File> fileList) {
		if (file == null) {
			LevelLogger.error(__name__ + "#ergodic Illegal input file : null.");
			return fileList;
		}
		File[] files = file.listFiles();
		if (files == null)
			return fileList;
		for (File f : files) {
			if (f.isDirectory()) {
				ergodic(f, fileList);
			} else if (f.getName().endsWith(".java"))
				fileList.add(f);
		}
		return fileList;
	}

	/**
	 * iteratively search the file in the given {@code directory}
	 * 
	 * @param directory
	 *            : root directory
	 * @param fileList
	 *            : list of file
	 * @return : a list of file
	 */
	public static List<String> ergodic(String directory, List<String> fileList) {
		if (directory == null) {
			LevelLogger.error(__name__ + "#ergodic Illegal input file : null.");
			return fileList;
		}
		File file = new File(directory);
		File[] files = file.listFiles();
		if (files == null)
			return fileList;
		for (File f : files) {
			if (f.isDirectory()) {
				ergodic(f.getAbsolutePath(), fileList);
			} else if (f.getName().endsWith(Constant.SOURCE_FILE_SUFFIX))
				fileList.add(f.getAbsolutePath());
		}
		return fileList;
	}

}