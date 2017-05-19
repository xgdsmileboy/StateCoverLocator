package org.jfree.chart.util.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the org.jfree.chart.util package. <P> These tests can be run using JUnit (http://www.junit.org).
 */
public class UtilPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.util");
    suite.addTestSuite(BooleanListTests.class);
    suite.addTestSuite(HashUtilitiesTests.class);
    suite.addTestSuite(LineUtilitiesTests.class);
    suite.addTestSuite(LogFormatTests.class);
    suite.addTestSuite(PaintListTests.class);
    suite.addTestSuite(PaintMapTests.class);
    suite.addTestSuite(RelativeDateFormatTests.class);
    suite.addTestSuite(SerialUtilitiesTests.class);
    suite.addTestSuite(ShapeListTests.class);
    suite.addTestSuite(ShapeUtilitiesTests.class);
    suite.addTestSuite(StrokeListTests.class);
    suite.addTestSuite(StrokeMapTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public UtilPackageTests(  String name){
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
