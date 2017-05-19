package org.jfree.data.time.ohlc.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * Some tests for the <code>org.jfree.data.time.ohlc</code> package that can be run using JUnit.  You can find more information about JUnit at <a href="http://www.junit.org">http://www.junit.org</a>.
 */
public class OHLCPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.data.time.ohlc");
    suite.addTestSuite(OHLCItemTests.class);
    suite.addTestSuite(OHLCSeriesCollectionTests.class);
    suite.addTestSuite(OHLCSeriesTests.class);
    suite.addTestSuite(OHLCTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the test suite name.
 */
  public OHLCPackageTests(  String name){
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
