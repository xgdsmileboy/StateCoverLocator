package org.jfree.chart.title.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the org.jfree.chart.title package. <P> These tests can be run using JUnit (http://www.junit.org).
 */
public class TitlePackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.title");
    suite.addTestSuite(CompositeTitleTests.class);
    suite.addTestSuite(DateTitleTests.class);
    suite.addTestSuite(ImageTitleTests.class);
    suite.addTestSuite(LegendGraphicTests.class);
    suite.addTestSuite(LegendTitleTests.class);
    suite.addTestSuite(PaintScaleLegendTests.class);
    suite.addTestSuite(ShortTextTitleTests.class);
    suite.addTestSuite(TextTitleTests.class);
    suite.addTestSuite(TitleTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public TitlePackageTests(  String name){
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
