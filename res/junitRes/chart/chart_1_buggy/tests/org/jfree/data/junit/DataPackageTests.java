package org.jfree.data.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * Some tests for the <code>org.jfree.data</code> package that can be run using JUnit. You can find more information about JUnit at <a href="http://www.junit.org">http://www.junit.org</a>.
 */
public class DataPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.data");
    suite.addTestSuite(ComparableObjectItemTests.class);
    suite.addTestSuite(ComparableObjectSeriesTests.class);
    suite.addTestSuite(DataUtilitiesTests.class);
    suite.addTestSuite(DefaultKeyedValueTests.class);
    suite.addTestSuite(DefaultKeyedValuesTests.class);
    suite.addTestSuite(DefaultKeyedValues2DTests.class);
    suite.addTestSuite(DomainOrderTests.class);
    suite.addTestSuite(KeyedObjectTests.class);
    suite.addTestSuite(KeyedObjectsTests.class);
    suite.addTestSuite(KeyedObjects2DTests.class);
    suite.addTestSuite(KeyToGroupMapTests.class);
    suite.addTestSuite(RangeTests.class);
    suite.addTestSuite(RangeTypeTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the test suite name.
 */
  public DataPackageTests(  String name){
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
