package org.jfree.chart.block.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the org.jfree.chart.block package. <P> These tests can be run using JUnit (http://www.junit.org).
 */
public class BlockPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.block");
    suite.addTestSuite(AbstractBlockTests.class);
    suite.addTestSuite(BlockBorderTests.class);
    suite.addTestSuite(BlockContainerTests.class);
    suite.addTestSuite(BorderArrangementTests.class);
    suite.addTestSuite(ColorBlockTests.class);
    suite.addTestSuite(ColumnArrangementTests.class);
    suite.addTestSuite(EmptyBlockTests.class);
    suite.addTestSuite(FlowArrangementTests.class);
    suite.addTestSuite(GridArrangementTests.class);
    suite.addTestSuite(LabelBlockTests.class);
    suite.addTestSuite(LineBorderTests.class);
    suite.addTestSuite(RectangleConstraintTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public BlockPackageTests(  String name){
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
