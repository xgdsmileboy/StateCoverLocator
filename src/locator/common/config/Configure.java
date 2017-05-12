/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import locator.common.java.JavaFile;
import locator.common.java.Subject;
import locator.common.util.LevelLogger;

public class Configure {

	private final static String __name__ = "@Configure ";

	public static List<Subject> getSubjectFromXML(String fileName) throws NumberFormatException{
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
				try{
					id = Integer.parseInt(element.attributeValue("id"));
				} catch (NumberFormatException e){
					throw new NumberFormatException("Parse id failed!");
				}
				String ssrc = element.elementText("ssrc");
				String tsrc = element.elementText("tsrc");
				String sbin = element.elementText("sbin");
				String tbin = element.elementText("tbin");
				Subject subject = new Subject(name, id, ssrc, tsrc, sbin, tbin);
				list.add(subject);
			}
		} catch (DocumentException e) {
			LevelLogger.fatal(__name__ + "#getSubjectFromXML parse xml file failed !", e);
		}
		return list;
	}

	public static void config_dumper(Subject subject) {
		File file = new File("res/auxiliary/Dumper.java");
		if (!file.exists()) {
			LevelLogger.error("File : " + file.getAbsolutePath() + " not exist.");
			System.exit(0);
		}
		CompilationUnit cu = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(file), ASTParser.K_COMPILATION_UNIT);
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

		JavaFile.writeStringToFile(targetFile, formatSource);
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
