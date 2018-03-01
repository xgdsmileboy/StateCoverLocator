package jdk7.wrapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import jdk7.javax.tools.Diagnostic;
import jdk7.javax.tools.DiagnosticListener;
import jdk7.javax.tools.JavaCompiler;
import jdk7.javax.tools.JavaFileObject;
import jdk7.javax.tools.SimpleJavaFileObject;
import jdk7.javax.tools.StandardJavaFileManager;
import jdk7.javax.tools.ToolProvider;
import locator.common.java.JavaFile;
import locator.common.java.Subject;
import polyglot.ast.Case;


public class JCompiler {

	public static enum SOURCE_LEVEL {
		L_1_4("1.4"),
		L_1_5("1.5"),
		L_1_6("1.6");
		public static SOURCE_LEVEL toSourceLevel(String string) {
			switch(string) {
			case "1.4":
			case "1_4": return SOURCE_LEVEL.L_1_4;
			case "1.5":
			case "1_5": return SOURCE_LEVEL.L_1_5;
			case "1.6":
			case "1_6": return SOURCE_LEVEL.L_1_6;
			default : return SOURCE_LEVEL.L_1_4;
			}
		}
		private String value;
		private SOURCE_LEVEL(String val) {value = val; }
		@Override
		public String toString() {
			return value;
		}
	}
	
	private final char separator = File.pathSeparatorChar; 
	private static JCompiler instance = null;
	public static JCompiler getInstance() {
		if(instance == null) {
			instance = new JCompiler();
		}
		return instance;
	}
	
	private static SOURCE_LEVEL source_level = SOURCE_LEVEL.L_1_6;
	private static SOURCE_LEVEL target_level = source_level.L_1_6;
	
	public static void setSourceLevel(SOURCE_LEVEL level) {
		source_level = level;
	}
	
	public static void setTargetLevel(SOURCE_LEVEL level) {
		target_level = level;
	}

	private JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	private MDiagnosticListener mDiagnosticListener = new MDiagnosticListener();
	
	private JCompiler(){}
	
	public static class MDiagnosticListener implements DiagnosticListener<JavaFileObject> {
		public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
			if(diagnostic.getKind() == Diagnostic.Kind.ERROR) {
				System.out.println("Line Number->" + diagnostic.getLineNumber());
				System.out.println("code       ->" + diagnostic.getCode());
				System.out.println("Message    ->" + diagnostic.getMessage(Locale.ENGLISH));
				System.out.println("Source     ->" + diagnostic.getSource());
				System.out.println("");
			}
		}
	}

	/**
	 * java File Object represents an in-memory java source file <br>
	 * so there is no need to put the source file on hard disk
	 **/
	private static class InMemoryJavaFileObject extends SimpleJavaFileObject {
		private String contents = null;

		public InMemoryJavaFileObject(String className, String contents) throws Exception {
			super(URI.create("string:///" + className), Kind.SOURCE);
			this.contents = contents;
		}

		public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
			return contents;
		}
	}

	public static List<JavaFileObject> getAllJavaFileObjects(String path) {
		List<String> files = JavaFile.ergodic(path, new LinkedList<String>());
		List<JavaFileObject> javaFileObjects = new LinkedList<>();
		int length = path.length();
		try {
			for(String file : files) {
				String content = JavaFile.readFileToString(file);
				String className = file.substring(length);
				SimpleJavaFileObject simpleJavaFileObject = new InMemoryJavaFileObject(className, content);
				javaFileObjects.add(simpleJavaFileObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return javaFileObjects;
	}

	/** compile your files by JavaCompiler */
	public boolean compile(Iterable<? extends JavaFileObject> files, Iterable<String> options) {
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(mDiagnosticListener, Locale.ENGLISH, null);
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, mDiagnosticListener, options, null, files);
		return task.call();
	}

	public boolean compile(Subject subject) {
		subject.checkAndInitDir();
		List<String> classpath = subject.getClasspath();
		StringBuffer libs = new StringBuffer();
		if(classpath != null && !classpath.isEmpty()) {
			libs.append(classpath.get(0));
			for(int i = 1; i < classpath.size(); i++) {
				libs.append(separator + classpath.get(i));
			}
			libs.append(separator + subject.getHome() + subject.getSbin());
			libs.append(separator + System.getenv("classpath"));
		} else {
			libs.append(System.getenv("classpath"));
		}
		Iterable<? extends JavaFileObject> files = getAllJavaFileObjects(subject.getHome() + subject.getSsrc());
		Iterable<String> options = Arrays.asList("-d", subject.getHome() + subject.getSbin(), "-classpath", libs.toString(), "-source", source_level.toString(), "-target", target_level.toString());
		if(!compile(files, options)) {
			return false;
		}
		files = getAllJavaFileObjects(subject.getHome() + subject.getTsrc());
		options = Arrays.asList("-d", subject.getHome() + subject.getTbin(), "-classpath", libs.toString(), "-source", source_level.toString(), "-target", target_level.toString());
		if(!compile(files, options)){
			return false;
		}
		return true;
	}
	
	public boolean compile(Subject subject, String className, String code2compile) {
		subject.checkAndInitDir();
		List<String> classpath = subject.getClasspath();
		StringBuffer libs = new StringBuffer();
		if(classpath != null && !classpath.isEmpty()) {
			libs.append(classpath.get(0));
			for(int i = 1; i < classpath.size(); i++) {
				libs.append(separator + classpath.get(i));
			}
			libs.append(separator + subject.getHome() + subject.getSbin());
			libs.append(separator + System.getenv("classpath"));
		} else {
			libs.append(System.getenv("classpath"));
		}
		SimpleJavaFileObject simpleJavaFileObject = null;
		try {
			simpleJavaFileObject = new InMemoryJavaFileObject(className, code2compile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Iterable<? extends JavaFileObject> files = new ArrayList<>(Arrays.asList(simpleJavaFileObject));
		Iterable<String> options = Arrays.asList("-d", subject.getHome() + subject.getSbin(), "-classpath", libs.toString(), "-source", source_level.toString(), "-target", target_level.toString());
		if(!compile(files, options)) {
			return false;
		}
		return true;
	}
	
}
