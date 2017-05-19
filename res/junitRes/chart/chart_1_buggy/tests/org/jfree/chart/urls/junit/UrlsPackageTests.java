package org.jfree.chart.urls.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the org.jfree.chart.urls package. <P> These tests can be run using JUnit (http://www.junit.org).
 */
public class UrlsPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.urls");
    suite.addTestSuite(CustomCategoryURLGeneratorTests.class);
    suite.addTestSuite(CustomPieURLGeneratorTests.class);
    suite.addTestSuite(CustomXYURLGeneratorTests.class);
    suite.addTestSuite(StandardCategoryURLGeneratorTests.class);
    suite.addTestSuite(StandardPieURLGeneratorTests.class);
    suite.addTestSuite(StandardXYURLGeneratorTests.class);
    suite.addTestSuite(TimeSeriesURLGeneratorTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public UrlsPackageTests(  String name){
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
