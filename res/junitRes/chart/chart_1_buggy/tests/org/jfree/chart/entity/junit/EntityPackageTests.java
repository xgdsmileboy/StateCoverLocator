package org.jfree.chart.entity.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the org.jfree.chart.entity package. <P> These tests can be run using JUnit (http://www.junit.org).
 */
public class EntityPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.entity");
    suite.addTestSuite(CategoryItemEntityTests.class);
    suite.addTestSuite(CategoryLabelEntityTests.class);
    suite.addTestSuite(LegendItemEntityTests.class);
    suite.addTestSuite(PieSectionEntityTests.class);
    suite.addTestSuite(StandardEntityCollectionTests.class);
    suite.addTestSuite(TickLabelEntityTests.class);
    suite.addTestSuite(XYItemEntityTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public EntityPackageTests(  String name){
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
