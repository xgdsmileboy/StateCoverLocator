package org.jfree.chart.needle.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the <code>org.jfree.chart.needle</code> package. <P> These tests can be run using JUnit (http://www.junit.org).
 */
public class NeedlePackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.needle");
    suite.addTestSuite(ArrowNeedleTests.class);
    suite.addTestSuite(LineNeedleTests.class);
    suite.addTestSuite(LongNeedleTests.class);
    suite.addTestSuite(MeterNeedleTests.class);
    suite.addTestSuite(MiddlePinNeedleTests.class);
    suite.addTestSuite(PinNeedleTests.class);
    suite.addTestSuite(PlumNeedleTests.class);
    suite.addTestSuite(PointerNeedleTests.class);
    suite.addTestSuite(ShipNeedleTests.class);
    suite.addTestSuite(WindNeedleTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public NeedlePackageTests(  String name){
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
