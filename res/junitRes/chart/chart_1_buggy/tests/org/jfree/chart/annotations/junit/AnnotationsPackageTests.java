package org.jfree.chart.annotations.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the <code>org.jfree.chart.annotations</code> package.  These tests can be run using JUnit (http://www.junit.org).
 */
public class AnnotationsPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.annotations");
    suite.addTestSuite(CategoryLineAnnotationTests.class);
    suite.addTestSuite(CategoryPointerAnnotationTests.class);
    suite.addTestSuite(CategoryTextAnnotationTests.class);
    suite.addTestSuite(TextAnnotationTests.class);
    suite.addTestSuite(XYBoxAnnotationTests.class);
    suite.addTestSuite(XYDrawableAnnotationTests.class);
    suite.addTestSuite(XYImageAnnotationTests.class);
    suite.addTestSuite(XYLineAnnotationTests.class);
    suite.addTestSuite(XYPointerAnnotationTests.class);
    suite.addTestSuite(XYPolygonAnnotationTests.class);
    suite.addTestSuite(XYShapeAnnotationTests.class);
    suite.addTestSuite(XYTextAnnotationTests.class);
    suite.addTestSuite(XYTitleAnnotationTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public AnnotationsPackageTests(  String name){
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
