/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst;

import org.junit.Assert;
import org.junit.Test;

import locator.common.config.Constant;
import locator.common.java.JavaFile;
import locator.common.java.Subject;
import locator.inst.visitor.DeInstrumentVisitor;
import locator.inst.visitor.StatementInstrumentVisitor;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class InstrumentTest {

	@Test
	public void test_instrumentSingleFile() {
		String filePath = "res/junitRes/Test.java";
		String content = JavaFile.readFileToString(filePath);
		StatementInstrumentVisitor statementInstrumentVisitor = new StatementInstrumentVisitor();
		statementInstrumentVisitor.setFlag(Constant.INSTRUMENT_K_TEST);
		Instrument.execute(filePath, statementInstrumentVisitor);
		String newContent = JavaFile.readFileToString(filePath);
		String instrumentContent = JavaFile.readFileToString("res/junitRes/testOracle/Test-InsFull.java");
		JavaFile.writeStringToFile(filePath, content);

		Assert.assertFalse(content.equals(newContent));
		Assert.assertTrue(newContent.equals(instrumentContent));
	}

	@Test
	public void test_instrumentMultiFiles() {
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "tests", "build", "build-tests");
		String filePath = subject.getHome() + subject.getSsrc();
		StatementInstrumentVisitor statementInstrumentVisitor = new StatementInstrumentVisitor();
		statementInstrumentVisitor.setFlag(Constant.INSTRUMENT_K_SOURCE);
		// after executed, all java files in
		// "res/junit/chart/chart_1_buggy/source" should be instrumented
		Instrument.execute(filePath, statementInstrumentVisitor);
	}

	@Test
	public void test_deInstrumentMultiFiles() {
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "tests", "build", "build-tests");
		String filePath = subject.getHome() + subject.getSsrc();
		DeInstrumentVisitor deInstrumentVisitor = new DeInstrumentVisitor();
		// after executed, all instrumented statements in java files in
		// "res/junit/chart/chart_1_buggy/source" should be removed
		Instrument.execute(filePath, deInstrumentVisitor);
	}

}
