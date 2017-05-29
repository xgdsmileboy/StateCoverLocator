package locator.inst.predict;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.inst.visitor.feature.FeatureExtraction;

public class PredictorTest {

	@Test
	public void test_predict() {
		// test assignment statement "num = num.divide(gcd);"
		String path = System.getProperty("user.dir") + "/res/junitRes/math/math_3_buggy/src/main/java";
		String relJavaPath = "org/apache/commons/math3/util/MathArrays.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 826);
//		System.out.println("Variables : ");
//		for (String string : vars.getFirst()) {
//			System.out.println(string);
//		}
//		System.out.println("Expressions : ");
//		for (String string : vars.getSecond()) {
//			System.out.println(string);
//		}

		Subject subject = new Subject("math", 3, "/src/main/java", "/src/test/java", "/target/classes",
				"/target/test-classes");
		Pair<Set<String>, Set<String>> conditions = Predictor.predict(subject, vars.getFirst(), vars.getSecond());

		for (String cond : conditions.getSecond()) {
			System.out.println(cond);
		}
	}
	
	@Test
	public void test_predict_2() {
		// test assignment statement "num = num.divide(gcd);"
		String path = "/home/jiajun/d4j/projects/math/math_4_buggy/src/main/java";
		String relJavaPath = "org/apache/commons/math3/geometry/euclidean/threed/SubLine.java";
		Pair<List<String>, List<String>> vars = FeatureExtraction.extractAllFeatures(path, relJavaPath, 71);
//		System.out.println("Variables : ");
//		for (String string : vars.getFirst()) {
//			System.out.println(string);
//		}
//		System.out.println("Expressions : ");
//		for (String string : vars.getSecond()) {
//			System.out.println(string);
//		}

		Subject subject = new Subject("math", 4, "/src/main/java", "/src/test/java", "/target/classes",
				"/target/test-classes");
		Pair<Set<String>, Set<String>> conditions = Predictor.predict(subject, vars.getFirst(), vars.getSecond());

		for (String cond : conditions.getSecond()) {
			System.out.println(cond);
		}
	}

}
