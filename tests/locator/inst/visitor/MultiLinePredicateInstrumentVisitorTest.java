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

import locator.common.java.JavaFile;
import locator.common.java.Pair;

/**
 * @author Jiajun
 * @date Jan 5, 2018
 */
public class MultiLinePredicateInstrumentVisitorTest {

	@Test
	public void test_predicateInstrumentForClazz() {
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = (CompilationUnit) JavaFile
				.genASTFromSource(JavaFile.readFileToString(filePath), ASTParser.K_COMPILATION_UNIT);
		Map<Integer, List<Pair<String, String>>> predicates = new HashMap<>();
		List<Pair<String, String>> conditions = new ArrayList<>();
		conditions.add(new Pair<String, String>("den > 1", "0.5"));
		predicates.put(27, conditions);
		List<Pair<String, String>> condsForAssign = new ArrayList<>();
		condsForAssign.add(new Pair<String, String>("num < 0", "0.8"));
		predicates.put(36, condsForAssign);
		List<Pair<String, String>> condsForVds = new ArrayList<>();
		condsForVds.add(new Pair<String, String>("bits > 0", "1.0"));
		predicates.put(64, condsForVds);
		MultiLinePredicateInstrumentVisitor predicateInstrumentVisitor = new MultiLinePredicateInstrumentVisitor(predicates, false);
		compilationUnit.accept(predicateInstrumentVisitor);
		System.out.println(compilationUnit);
	}
}
