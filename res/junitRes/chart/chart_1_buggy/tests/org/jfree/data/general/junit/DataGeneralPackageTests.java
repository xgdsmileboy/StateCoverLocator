package org.jfree.data.general.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * Some tests for the <code>org.jfree.data</code> package that can be run using JUnit.  You can find more information about JUnit at <a href="http://www.junit.org">http://www.junit.org</a>.
 */
public class DataGeneralPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.data.general");
    suite.addTestSuite(DatasetGroupTests.class);
    suite.addTestSuite(DatasetUtilitiesTests.class);
    suite.addTestSuite(DefaultHeatMapDatasetTests.class);
    suite.addTestSuite(DefaultKeyedValueDatasetTests.class);
    suite.addTestSuite(DefaultKeyedValuesDatasetTests.class);
    suite.addTestSuite(DefaultKeyedValues2DDatasetTests.class);
    suite.addTestSuite(DefaultPieDatasetTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the test suite name.
 */
  public DataGeneralPackageTests(  String name){
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
