package locator.core.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import locator.common.config.Configure;
import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.Subject;
import locator.common.util.Pair;


public class DNNTest {

	
	Subject subject = null;
	
	
	@Before
	public void setUp() throws Exception {
		Constant.PROJECT_HOME = "/home/ubuntu/code/d4j/projects_4_fl";
		subject = Configure.getSubject("chart", 14);
		Identifier.resetAll();
	}
	
	@Test
	public void test_predict_chart_14_1() {
		Constant.PROJECT_HOME = "/home/ubuntu/code/d4j/projects_4_fl";
		subject = Configure.getSubject("chart", 14);
		Identifier.resetAll();
		
		DNN dnn = new DNN();
		int methodID = Identifier.getIdentifier("org.jfree.chart.plot.CategoryPlot#boolean#removeDomainMarker#?,int,Marker,Layer,boolean");
		int line = 2166;
		Set<String> allStatements = new HashSet<>();
		allStatements.add(methodID + "#" + line);
		Map<String, Map<Integer, List<Pair<String, String>>>> predicates = dnn.getAllPredicates(subject, allStatements, false);
		for(Entry<String, Map<Integer, List<Pair<String, String>>>> entry : predicates.entrySet()) {
			System.out.println(entry.getKey());
			for(Entry<Integer, List<Pair<String, String>>> inner : entry.getValue().entrySet()) {
				System.out.println(inner.getKey());
				for(Pair<String, String> pair : inner.getValue()) {
					System.out.println(pair.getFirst() + " : " + pair.getSecond());
				}
			}
		}
	}

}
