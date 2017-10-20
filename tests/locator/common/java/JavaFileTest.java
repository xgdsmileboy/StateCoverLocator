/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.java;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * @author Jiajun
 * @date Oct 20, 2017
 */
public class JavaFileTest {

	public void test_jdt_parse_keywords_bug() {
		String content = " public class Test {"
				+ "public static Test suite() {"
				+ "TestSuite suite = new TestSuite();"
				+ "suite.setName(\"Commons-Lang (all) Tests\");"
				+ "suite.addTest(LangTestSuite.suite());"
				+ "suite.addTest(BuilderTestSuite.suite());"
				+ "suite.addTest(EnumTestSuite.suite());"
//				+ "suite.addTest(org.apache.commons.lang.enum.EnumTestSuite.suite());"  //enum is the keyword
				+ "suite.addTest(ExceptionTestSuite.suite());"
				+ "suite.addTest(MathTestSuite.suite());"
				+ "suite.addTest(MutableTestSuite.suite());"
				+ "suite.addTest(ReflectTestSuite.suite());"
				+ "suite.addTest(TextTestSuite.suite());"
				+ "suite.addTest(TimeTestSuite.suite());"
				+ "return suite;}}";
		CompilationUnit unit = (CompilationUnit) JavaFile.genASTFromSource(content, ASTParser.K_COMPILATION_UNIT);
		System.out.println(unit.toString());
	}
}
