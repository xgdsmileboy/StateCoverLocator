package locator.inst.visitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import locator.common.java.JavaFile;

public class NewNoSideEffectPredicateInstrumentVisitorTest {

	private String file = "res/junitRes/SideEffect.java";
	private String srcPath = "/res/junitRes";
	private String relJavaPath = "SideEffect.java";
	
	private CompilationUnit getTestCompilationUnit() {
		return (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(file), ASTParser.K_COMPILATION_UNIT);
	}
	
	@Test
	public void test_if() {
		NewNoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NewNoSideEffectPredicateInstrumentVisitor(false);
		Set<Integer> lines = new HashSet<>();
		lines.add(11);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_for() {
		NewNoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NewNoSideEffectPredicateInstrumentVisitor(false);
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
		NewNoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NewNoSideEffectPredicateInstrumentVisitor(false);
		Set<Integer> lines = new HashSet<>();
		lines.add(64);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_do() {
		NewNoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NewNoSideEffectPredicateInstrumentVisitor(false);
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
		NewNoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NewNoSideEffectPredicateInstrumentVisitor(false);
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
		NewNoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NewNoSideEffectPredicateInstrumentVisitor(false);
		Set<Integer> lines = new HashSet<>();
		lines.add(90);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_return_not_instrument() {
		NewNoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NewNoSideEffectPredicateInstrumentVisitor(false);
		Set<Integer> lines = new HashSet<>();
		lines.add(119);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		System.out.println(unit.toString());
	}
	
	@Test
	public void test_return_in_died_loop() {
		NewNoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NewNoSideEffectPredicateInstrumentVisitor(false);
		Set<Integer> lines = new HashSet<>();
		lines.add(124);
		instrumentVisitor.initOneRun(lines, srcPath, relJavaPath);
		
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		// there should be no instrument
		System.out.println(unit.toString());
	}
	
}
