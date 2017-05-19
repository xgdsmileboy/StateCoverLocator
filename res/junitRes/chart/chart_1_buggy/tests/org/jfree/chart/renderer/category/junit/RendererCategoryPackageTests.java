package org.jfree.chart.renderer.category.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the org.jfree.chart.renderer.category package. <P> These tests can be run using JUnit (http://www.junit.org).
 */
public class RendererCategoryPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.renderer.category");
    suite.addTestSuite(AbstractCategoryItemRendererTests.class);
    suite.addTestSuite(AreaRendererTests.class);
    suite.addTestSuite(BarRendererTests.class);
    suite.addTestSuite(BarRenderer3DTests.class);
    suite.addTestSuite(BoxAndWhiskerRendererTests.class);
    suite.addTestSuite(CategoryStepRendererTests.class);
    suite.addTestSuite(DefaultCategoryItemRendererTests.class);
    suite.addTestSuite(GanttRendererTests.class);
    suite.addTestSuite(GradientBarPainterTests.class);
    suite.addTestSuite(GroupedStackedBarRendererTests.class);
    suite.addTestSuite(IntervalBarRendererTests.class);
    suite.addTestSuite(LayeredBarRendererTests.class);
    suite.addTestSuite(LevelRendererTests.class);
    suite.addTestSuite(LineAndShapeRendererTests.class);
    suite.addTestSuite(LineRenderer3DTests.class);
    suite.addTestSuite(MinMaxCategoryRendererTests.class);
    suite.addTestSuite(ScatterRendererTests.class);
    suite.addTestSuite(StackedAreaRendererTests.class);
    suite.addTestSuite(StackedBarRendererTests.class);
    suite.addTestSuite(StackedBarRenderer3DTests.class);
    suite.addTestSuite(StandardBarPainterTests.class);
    suite.addTestSuite(StatisticalBarRendererTests.class);
    suite.addTestSuite(StatisticalLineAndShapeRendererTests.class);
    suite.addTestSuite(WaterfallBarRendererTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public RendererCategoryPackageTests(  String name){
    super(name);
  }
  /** 
 * Runs the test suite using JUnit's text-based runner.
 * @param args  ignored.
 */
  public static void main(  String[] args){
    junit.textui.TestRunner.run(suite());
  }
}
