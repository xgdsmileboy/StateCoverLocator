/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.run.path;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import locator.common.config.Configure;
import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.CoverInfo;
import locator.common.java.JavaFile;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.inst.Instrument;
import locator.inst.visitor.DeInstrumentVisitor;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class CoverageTest {

	@Test
	public void test_computeCoverage() {
//		Constant.PROJECT_HOME = "res/junitRes";
//		Subject subject = new Subject("chart", 2, "/source", "/tests", "/build", "/build-tests");
//		// preprocess : remove all instrument
//		DeInstrumentVisitor deInstrumentVisitor = new DeInstrumentVisitor();
//		Instrument.execute(subject.getHome() + subject.getSsrc(), deInstrumentVisitor);
//		Instrument.execute(subject.getHome() + subject.getTsrc(), deInstrumentVisitor);
//		// copy auxiliary file to subject path
//		Configure.config_dumper(subject);
//
//		Pair<Set<Integer>, Set<Integer>> allTests = Collector.collectAllTestCases(subject);
//
//		Assert.assertTrue(allTests.getFirst().size() == 2);
//		Assert.assertTrue(allTests.getSecond().size() == 52);
//
//		Map<String, CoverInfo> coverage = Coverage.computeCoverage(subject, allTests);
//
//		File file = new File(System.getProperty("user.dir") + "/tests/locator/core/run/path/coverage.csv");
//		if (file.exists()) {
//			file.delete();
//		}
//		int line = 0;
//		for (Entry<String, CoverInfo> entry : coverage.entrySet()) {
//			line++;
//			StringBuffer stringBuffer = new StringBuffer();
//			String key = entry.getKey();
//			String[] info = key.split("#");
//			String methodString = Identifier.getMessage(Integer.parseInt(info[0]));
//			stringBuffer.append(methodString);
//			stringBuffer.append("#");
//			stringBuffer.append(info[1]);
//			stringBuffer.append("\t");
//			stringBuffer.append(entry.getValue().getFailedCount());
//			stringBuffer.append("\t");
//			stringBuffer.append(entry.getValue().getPassedCount());
//			stringBuffer.append("\n");
//			// view coverage.csv file
//			JavaFile.writeStringToFile(file, stringBuffer.toString(), true);
//		}
//		Assert.assertTrue(line == 93);
	}

	@Test
	public void test_getAllCoveredStatement() {
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 2, "/source", "/tests", "/build", "/build-tests", null);
		// preprocess : remove all instrument
		DeInstrumentVisitor deInstrumentVisitor = new DeInstrumentVisitor();
		Instrument.execute(subject.getHome() + subject.getSsrc(), deInstrumentVisitor);
		Instrument.execute(subject.getHome() + subject.getTsrc(), deInstrumentVisitor);
		// copy auxiliary file to subject path
		Configure.config_dumper(subject);

		String failedTest1 = "org.jfree.data.general.junit.DatasetUtilitiesTests#void#testBug2849731_2#?";
		int methodID = Identifier.getIdentifier(failedTest1);
		Set<Integer> testcases = new HashSet<>();
		testcases.add(methodID);
		Set<String> coveredStatement = Coverage.getAllCoveredStatement(subject, testcases);

		Assert.assertTrue(coveredStatement.size() == 89);

		String errorLocation = "org.jfree.data.general.DatasetUtilities#Range#iterateDomainBounds#?,XYDataset,boolean";
		int errorMethod = Identifier.getIdentifier(errorLocation);

		boolean containErrorLocation = false;
		for (String statement : coveredStatement) {
			String[] methodInfo = statement.split("#");
			int method = Integer.parseInt(methodInfo[0]);
			if (method == errorMethod) {
				containErrorLocation = true;
				break;
			}
		}

		Assert.assertTrue(containErrorLocation);
	}

}
