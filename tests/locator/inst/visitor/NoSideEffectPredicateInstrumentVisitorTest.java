package locator.inst.visitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import locator.common.config.Configure;
import locator.common.config.Constant;
import locator.common.java.JavaFile;
import locator.common.java.Subject;

public class NoSideEffectPredicateInstrumentVisitorTest {

	private String file = "res/junitRes/SideEffect.java";
	private String srcPath = "/res/junitRes";
	private String relJavaPath = "SideEffect.java";
	
	private CompilationUnit getTestCompilationUnit() {
		return (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(file), ASTParser.K_COMPILATION_UNIT);
	}
	
	@Test
	public void test_if() {
		SDStmtPredicateInstrumentVisitor instrumentVisitor = new SDStmtPredicateInstrumentVisitor(false, true, true, true);
		Set<Integer> lines = new HashSet<>();
		lines.add(11);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_for() {
		SDStmtPredicateInstrumentVisitor instrumentVisitor = new SDStmtPredicateInstrumentVisitor(false, true, true, true);
		Set<Integer> lines = new HashSet<>();
		lines.add(10);
		lines.add(22);
		lines.add(34);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_while() {
		SDStmtPredicateInstrumentVisitor instrumentVisitor = new SDStmtPredicateInstrumentVisitor(false, true, true, true);
		Set<Integer> lines = new HashSet<>();
		lines.add(64);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_do() {
		SDStmtPredicateInstrumentVisitor instrumentVisitor = new SDStmtPredicateInstrumentVisitor(false, true, true, true);
		Set<Integer> lines = new HashSet<>();
		lines.add(48);
		lines.add(57);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_switch() {
		SDStmtPredicateInstrumentVisitor instrumentVisitor = new SDStmtPredicateInstrumentVisitor(false, true, true, true);
		Set<Integer> lines = new HashSet<>();
		lines.add(80);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		System.out.println(unit.toString());
	}
	
	// No instrument? May be the type of the variable does not resolved
	@Test
	public void test_return() {
		SDStmtPredicateInstrumentVisitor instrumentVisitor = new SDStmtPredicateInstrumentVisitor(false, true, true, true);
		Set<Integer> lines = new HashSet<>();
		lines.add(90);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_return_not_instrument() {
		SDStmtPredicateInstrumentVisitor instrumentVisitor = new SDStmtPredicateInstrumentVisitor(false, true, true, true);
		Set<Integer> lines = new HashSet<>();
		lines.add(119);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_return_in_died_loop() {
		SDStmtPredicateInstrumentVisitor instrumentVisitor = new SDStmtPredicateInstrumentVisitor(false, true, true, true);
		Set<Integer> lines = new HashSet<>();
		lines.add(124);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		// there should be no instrument
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_initialize_array() {
		SDStmtPredicateInstrumentVisitor instrumentVisitor = new SDStmtPredicateInstrumentVisitor(false, true, true, true);
		Set<Integer> lines = new HashSet<>();
		lines.add(133);
		lines.add(134);
		lines.add(135);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		// there should be no instrument
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_compare_assignObject() {
		Constant.PROJECT_HOME = Constant.HOME + "/res/junitRes";
		Subject subject = Configure.getSubject("time", 1);
		String relJavaFile = "org/joda/time/Partial.java";
		String src = subject.getHome() + subject.getSsrc();
		String fileName = src + "/" + relJavaFile; 
		
		SDStmtPredicateInstrumentVisitor instrumentVisitor = new SDStmtPredicateInstrumentVisitor(false, true, true, true);
		Set<Integer> lines = new HashSet<>();
		lines.add(245);
		instrumentVisitor.initOneRun(lines, src, relJavaPath);
		
		CompilationUnit unit = (CompilationUnit) JavaFile.genASTFromSourceWithType(
				JavaFile.readFileToString(fileName), ASTParser.K_COMPILATION_UNIT, fileName, subject);
		unit.accept(instrumentVisitor);
		// there should be no instrument
		System.out.println(unit.toString());
		
	}
	
	
}
