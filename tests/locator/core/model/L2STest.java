package locator.core.model;

import org.junit.Test;

import locator.common.config.Constant;
import locator.common.java.Subject;

public class L2STest {

	
	@Test
	public void test() {
		Constant.PROJECT_HOME = "/home/jiajun/d4j/test_pojs";
		Subject subject = new Subject("math", 1, "/src/main/java", "/src/test/java", "/target/classes", "/target/test-classes", null);
		L2S l2s = new L2S();
		l2s.predict(subject, null, null, null);
		
	}
	
}
