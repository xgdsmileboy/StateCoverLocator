package org.jfree.chart.renderer.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the org.jfree.chart.renderer package. <P> These tests can be run using JUnit (http://www.junit.org).
 */
public class RendererPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.renderer");
    suite.addTestSuite(AbstractRendererTests.class);
    suite.addTestSuite(AreaRendererEndTypeTests.class);
    suite.addTestSuite(DefaultPolarItemRendererTests.class);
    suite.addTestSuite(GrayPaintScaleTests.class);
    suite.addTestSuite(LookupPaintScaleTests.class);
    suite.addTestSuite(OutlierTests.class);
    suite.addTestSuite(RendererUtilitiesTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public RendererPackageTests(  String name){
    super(name);
  }
}
