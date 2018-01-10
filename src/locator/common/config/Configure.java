/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import jdk7.wrapper.JCompiler;
import locator.common.java.JavaFile;
import locator.common.java.Subject;
import locator.common.util.LevelLogger;

public class Configure {

	private final static String __name__ = "@Configure ";
	private static String auxiliaryString;

	/**
	 * configure d4j buggy project information from file.
	 * fix bug for inconsistent directories for same project
	 * @param name : project name, lower case
	 * @param id : bug id
	 * @return a subject instance contains basic information
	 */
	public static Subject getSubject(String name, int id){
		String fileName = Constant.HOME + "/res/d4j-info/src_path/" + name + "/" + id + ".txt";
		File file = new File(fileName);
		if(!file.exists()){
			System.out.println("File : " + fileName + " does not exist!");
			return null;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
		List<String> source = new ArrayList<>();
		try {
			while((line = br.readLine()) != null){
				source.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(source.size() < 4){
			System.err.println("PROJEC INFO CONFIGURE ERROR !");
			System.exit(0);
		}
		
		String ssrc = source.get(0);
		String sbin = source.get(1);
		String tsrc = source.get(2);
		String tbin = source.get(3);
		
		Subject subject = new Subject(name, id, ssrc, tsrc, sbin, tbin);
		return subject;
	}
	
	public static boolean compileAuxiliaryJava(Subject subject) {
		JCompiler compiler = JCompiler.getInstance();
		File file = new File(subject.getHome() + subject.getSbin());
		if(!file.exists()) {
			file.mkdirs();
		}
		return compiler.compile(subject, "auxiliary/Dumper.java", auxiliaryString);
	}
	
	/**
	 * read subject configure information from configure file
	 * 
	 * @param fileName
	 *            : configure file path containing the subject configuration
	 *            information
	 * @return a list of subject
	 * @throws NumberFormatException
	 *             : when the subject id is not configured correctly
	 */
	public static List<Subject> getSubjectFromXML(String fileName) throws NumberFormatException {
		List<Subject> list = new ArrayList<>();

		File inputXml = new File(fileName);
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(inputXml);
			Element root = document.getRootElement();

			for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				String name = element.attributeValue("name");
				int id = 0;
				try {
					id = Integer.parseInt(element.attributeValue("id"));
				} catch (NumberFormatException e) {
					throw new NumberFormatException("Parse id failed!");
				}
				String ssrc = element.elementText("ssrc");
				String tsrc = element.elementText("tsrc");
				String sbin = element.elementText("sbin");
				String tbin = element.elementText("tbin");
				List<String> classpath = new LinkedList<>();
				String clpath = element.elementText("classpath");
				if(classpath != null && !classpath.isEmpty()) {
					for (String elm : clpath.split(",")) {
						classpath.add(Constant.HOME + Constant.PATH_SEPARATOR + "res" + Constant.PATH_SEPARATOR
								+ "d4jlibs" + Constant.PATH_SEPARATOR + elm);
					}
				}
				Subject subject = new Subject(name, id, ssrc, tsrc, sbin, tbin, classpath);
				list.add(subject);
			}
		} catch (DocumentException e) {
			LevelLogger.fatal(__name__ + "#getSubjectFromXML parse xml file failed !", e);
		}
		return list;
	}
	
	public static void config_astlevel(Subject subject) {
		if(subject.getName().equals("lang") && subject.getId() >= 42) {
			Constant.AST_LEVEL = AST.JLS3;
			Constant.JAVA_VERSION = JavaCore.VERSION_1_4;
		} else {
			Constant.AST_LEVEL = AST.JLS8;
			Constant.JAVA_VERSION = JavaCore.VERSION_1_7;
		}
	}

	/**
	 * copy the auxiliary file into the subject source path to make the
	 * instrument running correctly
	 * 
	 * @param subject
	 *            : current subject
	 */
	public static void config_dumper(Subject subject) {
		File file = new File("res/auxiliary/Dumper.java");
		if (!file.exists()) {
			LevelLogger.error("File : " + file.getAbsolutePath() + " not exist.");
			System.exit(0);
		}
		CompilationUnit cu = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(file),
				ASTParser.K_COMPILATION_UNIT);
		cu.accept(new ConfigDumperVisitor());
		String formatSource = null;
		Formatter formatter = new Formatter();
		try {
			formatSource = formatter.formatSource(cu.toString());
		} catch (FormatterException e) {
			System.err.println(__name__ + "#execute Format Code Error for : " + file.getAbsolutePath());
			formatSource = cu.toString();
		}

		String path = subject.getHome() + subject.getSsrc();
		String target = path + Constant.PATH_SEPARATOR + "auxiliary/Dumper.java";
		File targetFile = new File(target);
		if (!targetFile.exists()) {
			targetFile.getParentFile().mkdirs();
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		auxiliaryString = formatSource;
		JavaFile.writeStringToFile(targetFile, formatSource);
		// fix bug for not compiling
		compileAuxiliaryJava(subject);
	}
}

/**
 * configure some properties in the dumper file
 * 
 * @author Jiajun
 *
 */
class ConfigDumperVisitor extends ASTVisitor {
	@Override
	public boolean visit(FieldDeclaration node) {
		for (Object object : node.fragments()) {
			if (object instanceof VariableDeclarationFragment) {
				VariableDeclarationFragment vdf = (VariableDeclarationFragment) object;
				String name = vdf.getName().getFullyQualifiedName();
				// configure the dependence (library) path and output path
				if (name.equals("OUT_AND_LIB_PATH")) {
					StringLiteral stringLiteral = node.getAST().newStringLiteral();
					stringLiteral.setLiteralValue(Constant.DUMPER_HOME);
					vdf.setInitializer(stringLiteral);
				}
			}
		}
		return true;
	}
}
