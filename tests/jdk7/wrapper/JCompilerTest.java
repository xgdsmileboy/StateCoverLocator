/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package jdk7.wrapper;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import locator.common.config.Constant;
import locator.common.java.JavaFile;
import locator.common.java.Subject;

/**
 * @author Jiajun
 * @date Dec 14, 2017
 */
public class JCompilerTest {

	String base = System.getProperty("user.dir") + "/res/d4jlibs";
	public void setup() {
		Constant.PROJECT_HOME = System.getProperty("user.dir") + "/res/junitRes";
	}

	@Test
	public void test_compile_lang() {
		setup();
		List<String> libs = new LinkedList<>();
		libs.add(base + "/cglib-nodep-2.2.2.jar");
		libs.add(base + "/commons-io-2.4.jar");
		libs.add(base + "/easymock-3.1.jar");
		libs.add(base + "/hamcrest-core-1.3.jar");
		libs.add(base + "/junit-4.11.jar");
		libs.add(base + "/objenesis-1.2.jar");
		Subject subject = new Subject("lang", 1, "/src/main/java", "/src/test/java", "/target/classes", "/target/tests",
				libs);

		JCompiler compiler = JCompiler.getInstance();
		System.out.println("compile pass : " + compiler.compile(subject));
	}

	@Test
	public void test_compile_math() {
		setup();
		List<String> libs = new LinkedList<>();
		libs.add(base + "/hamcrest-core-1.3.jar");
		libs.add(base + "/junit-4.11.jar");
		Subject subject = new Subject("math", 3, "/src/main/java", "/src/test/java", "/target/classes",
				"/target/test-classes", libs);

		JCompiler compiler = JCompiler.getInstance();
		System.out.println("compile pass : " + compiler.compile(subject));
	}

	@Test
	public void test_compile_chart() {
		setup();
		List<String> libs = new LinkedList<>();
		libs.add(base + "/junit-4.11.jar");
		libs.add(base + "/iText-2.1.4.jar");
		libs.add(base + "/servlet.jar");
		Subject subject = new Subject("chart", 1, "/source", "/tests", "/build", "/build-tests", libs);

		JCompiler compiler = JCompiler.getInstance();
		System.out.println("compile pass : " + compiler.compile(subject));
	}

	@Test
	public void test_compile_time() {
		setup();
		List<String> libs = new LinkedList<>();
		libs.add(base + "/junit-4.11.jar");
		libs.add(base + "/joda-convert-1.2.jar");
		Subject subject = new Subject("time", 1, "/src/main/java", "/src/test/java", "/target/classes", "/target/tests",
				libs);

		JCompiler compiler = JCompiler.getInstance();
		System.out.println("compile pass : " + compiler.compile(subject));
	}

	@Test
	public void test_compile_closure() {
		setup();
		// TODO : need to compile the whole project first from command line, 
		// since it needs to compile the .proto files first.
		List<String> libs = new LinkedList<>();
		libs.add(base + "/caja-r4314.jar");
		libs.add(base + "/jarjar.jar");
		libs.add(base + "/ant.jar");
		libs.add(base + "/ant-launcher.jar");
		libs.add(base + "/args4j.jar");
		libs.add(base + "/jsr305.jar");
		libs.add(base + "/guava.jar");
		libs.add(base + "/json.jar");
		libs.add(base + "/protobuf-java.jar");
		libs.add(base + "/junit-4.11.jar");
		libs.add(base + "/rhino.jar");
		libs.add("/Users/Jiajun/Code/Java/fault-localization/StateCoverLocator/res/junitRes/closure/closure_1_buggy/build/classes");
		Subject subject = new Subject("closure", 1, "/src", "/test", "/build/classes", "/build/test", libs);

		JCompiler compiler = JCompiler.getInstance();
		System.out.println("compile pass : " + compiler.compile(subject));
	}

	@Test
	public void test_compile_file() {
		setup();
		String content = JavaFile
				.readFileToString(Constant.PROJECT_HOME + "/lang/lang_1_buggy/src/main/java/org/apache/commons/lang3/ArrayUtils.java");
		List<String> libs = new LinkedList<>();
		libs.add(base + "/cglib-nodep-2.2.2.jar");
		libs.add(base + "/commons-io-2.4.jar");
		libs.add(base + "/easymock-3.1.jar");
		libs.add(base + "/hamcrest-core-1.3.jar");
		libs.add(base + "/junit-4.11.jar");
		libs.add(base + "/objenesis-1.2.jar");
		Subject subject = new Subject("lang", 1, "/src/main/java", "/src/test/java", "/target/classes", "/target/tests",
				libs);

		JCompiler compiler = JCompiler.getInstance();
		System.out.println(
				"compile pass : " + compiler.compile(subject, "org/apache/commons/lang3/ArrayUtils.java", content));
	}

}
