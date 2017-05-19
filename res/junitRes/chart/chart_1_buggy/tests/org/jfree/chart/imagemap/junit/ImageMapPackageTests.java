package org.jfree.chart.imagemap.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the org.jfree.chart.imagemap package. <P> These tests can be run using JUnit (http://www.junit.org).
 */
public class ImageMapPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.imagemap");
    suite.addTestSuite(DynamicDriveToolTipTagFragmentGeneratorTests.class);
    suite.addTestSuite(ImageMapUtilitiesTests.class);
    suite.addTestSuite(OverLIBToolTipTagFragmentGeneratorTests.class);
    suite.addTestSuite(StandardToolTipTagFragmentGeneratorTests.class);
    suite.addTestSuite(StandardURLTagFragmentGeneratorTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public ImageMapPackageTests(  String name){
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
