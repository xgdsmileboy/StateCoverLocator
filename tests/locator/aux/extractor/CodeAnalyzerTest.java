package locator.aux.extractor;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class CodeAnalyzerTest {

	private String base = System.getProperty("user.dir");
	
	@Test
	public void test_getAllVariablesUsed() {
		String baseDir = base + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaFile = "org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java";
		int line = 1222;
		Set<String> variables = CodeAnalyzer.getAllVariablesUsed(baseDir, relJavaFile, line);
		Assert.assertTrue(variables.size() == 1);
		for(String var : variables) {
			System.out.println(var);
		}
	}
	
	@Test
	public void test_getAllVariablesReadUse() {
		String baseDir = base + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaFile = "org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java";
		int line = 1222;
		Set<String> variables = CodeAnalyzer.getAllVariablesReadUse(baseDir, relJavaFile, line);
		Assert.assertTrue(variables.size() == 1);
		for(String var : variables) {
			System.out.println(var);
		}
	}
	
	@Test
	public void test_getAllVariablesWriteUse() {
		String baseDir = base + "/res/junitRes/chart/chart_1_buggy/source";
		String relJavaFile = "org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java";
		int line = 1222;
		Set<String> variables = CodeAnalyzer.getAllVariablesWriteUse(baseDir, relJavaFile, line);
		Assert.assertTrue(variables.size() == 0);
		for(String var : variables) {
			System.out.println(var);
		}
	}
	
}
