package org.jfree.data.statistics.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * Some tests for the <code>org.jfree.data.statistics</code> package that can be run using JUnit.  You can find more information about JUnit at <a href="http://www.junit.org">http://www.junit.org</a>.
 */
public class DataStatisticsPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.data.statistics");
    suite.addTestSuite(BoxAndWhiskerCalculatorTests.class);
    suite.addTestSuite(BoxAndWhiskerItemTests.class);
    suite.addTestSuite(DefaultBoxAndWhiskerCategoryDatasetTests.class);
    suite.addTestSuite(DefaultBoxAndWhiskerXYDatasetTests.class);
    suite.addTestSuite(DefaultMultiValueCategoryDatasetTests.class);
    suite.addTestSuite(DefaultStatisticalCategoryDatasetTests.class);
    suite.addTestSuite(HistogramBinTests.class);
    suite.addTestSuite(HistogramDatasetTests.class);
    suite.addTestSuite(MeanAndStandardDeviationTests.class);
    suite.addTestSuite(RegressionTests.class);
    suite.addTestSuite(SimpleHistogramBinTests.class);
    suite.addTestSuite(SimpleHistogramDatasetTests.class);
    suite.addTestSuite(StatisticsTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the test suite name.
 */
  public DataStatisticsPackageTests(  String name){
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
