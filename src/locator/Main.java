/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator;

import java.util.List;
import java.util.Set;

import locator.common.config.Configure;
import locator.common.config.Constant;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.LevelLogger;
import locator.core.Collector;
import locator.inst.Instrument;
import locator.inst.visitor.DeInstrumentVisitor;

public class Main {
	
	private final static String __name__ = "@Main ";
	
	private static void proceed(){
		
		List<Subject> allSubject = Configure.getSubjectFromXML(Constant.HOME + "/res/conf/project.xml");
		if(allSubject.size() < 1){
			LevelLogger.error(__name__ + "#proceed no subjects !");
			return;
		}
		Subject subject = allSubject.get(0);
		//preprocess : remove all instrument
		DeInstrumentVisitor deInstrumentVisitor = new DeInstrumentVisitor();
		Instrument.execute(subject.getHome()+subject.getSsrc(), deInstrumentVisitor);
		Instrument.execute(subject.getHome() + subject.getTsrc(), deInstrumentVisitor);
		//copy auxiliary file to subject path
		Configure.config_dumper(subject);
		
		//step 1: collect all tests
		Pair<Set<Integer>, Set<Integer>> allTests = Collector.collectAllTestCases(subject);
		
		
		//step 2: for each failed test collect running path
		
		//step 3: for each statement ran by failed tests, instrument predicates
		
		//step 4: run each test and collect coverage info
		
		//step 5: calculate suspiciousness for each statement based on predefined formula
				
	}
	
	public static void main(String[] args) {
		
		Constant.PROJECT_HOME = "/Users/Jiajun/Code/Java/manualD4J";
		
		proceed();
	}

}
