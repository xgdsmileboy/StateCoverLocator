/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import edu.pku.sei.conditon.util.JavaFile;
import locator.common.java.Pair;


/**
 * @author Jiajun
 * @date Jan 4, 2018
 */
public class NoSideEffectPredicateInstrumentVisitorTest {

	
	private CompilationUnit getTestCompilationUnit() {
		return (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString("./res/junitRes/SideEffect.java"), ASTParser.K_COMPILATION_UNIT);
	}
	
	@Test
	public void test_if() {
		NoSideEffectPredicateInstrumentVisitor instrumentVisitor = new NoSideEffectPredicateInstrumentVisitor(false);
		Map<Integer, List<Pair<String, String>>> condition = new HashMap<>();
		List<Pair<String, String>> pairs = new ArrayList<>();
		pairs.add(new Pair<String, String>("a[i] > 5", "1.0"));
		condition.put(33, pairs);
		instrumentVisitor.setCondition(condition);
		CompilationUnit unit = getTestCompilationUnit();
		unit.accept(instrumentVisitor);
		System.out.println(unit.toString());
	}
	
}
