/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst;

import org.junit.Test;

import locator.common.config.Constant;
import locator.common.java.Subject;
import locator.inst.visitor.DeInstrumentVisitor;
import locator.inst.visitor.StatementInstrumentVisitor;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class InstrumentTest {
	
	@Test
	public void test_instrumentSingleFile(){
		String filePath = "res/junitRes/IterativeLegendreGaussIntegratorTest.java";
		StatementInstrumentVisitor statementInstrumentVisitor = new StatementInstrumentVisitor();
		statementInstrumentVisitor.setFlag(Constant.INSTRUMENT_K_TEST);
		Instrument.execute(filePath, statementInstrumentVisitor);
	}
	
	@Test
	public void test_instrumentMultiFiles(){
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "tests", "build", "build-tests");
		String filePath = subject.getHome() + subject.getSsrc();
		StatementInstrumentVisitor statementInstrumentVisitor = new StatementInstrumentVisitor();
		statementInstrumentVisitor.setFlag(Constant.INSTRUMENT_K_SOURCE);
		Instrument.execute(filePath, statementInstrumentVisitor);
	}
	
	@Test
	public void test_deInstrumentSingleFile(){
		String filePath = "res/junitRes/IterativeLegendreGaussIntegratorTest.java";
		DeInstrumentVisitor deInstrumentVisitor = new DeInstrumentVisitor();
		Instrument.execute(filePath, deInstrumentVisitor);
	}
	
	@Test
	public void test_deInstrumentMultiFiles(){
		Constant.PROJECT_HOME = "res/junitRes";
		Subject subject = new Subject("chart", 1, "/source", "tests", "build", "build-tests");
		String filePath = subject.getHome() + subject.getSsrc();
		DeInstrumentVisitor deInstrumentVisitor = new DeInstrumentVisitor();
		Instrument.execute(filePath, deInstrumentVisitor);
	}
	
}
