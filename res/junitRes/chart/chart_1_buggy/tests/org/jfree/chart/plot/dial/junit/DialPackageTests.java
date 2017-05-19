package org.jfree.chart.plot.dial.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the <code>org.jfree.experimental.chart.plot.dial</code> package.  These tests can be run using JUnit (http://www.junit.org).
 */
public class DialPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.experimental.chart.plot.dial");
    suite.addTestSuite(AbstractDialLayerTests.class);
    suite.addTestSuite(DialBackgroundTests.class);
    suite.addTestSuite(DialCapTests.class);
    suite.addTestSuite(DialPlotTests.class);
    suite.addTestSuite(DialPointerTests.class);
    suite.addTestSuite(DialTextAnnotationTests.class);
    suite.addTestSuite(DialValueIndicatorTests.class);
    suite.addTestSuite(StandardDialFrameTests.class);
    suite.addTestSuite(ArcDialFrameTests.class);
    suite.addTestSuite(StandardDialRangeTests.class);
    suite.addTestSuite(StandardDialScaleTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public DialPackageTests(  String name){
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
