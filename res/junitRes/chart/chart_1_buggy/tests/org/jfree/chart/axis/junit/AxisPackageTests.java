package org.jfree.chart.axis.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the org.jfree.chart.axis package. <P> These tests can be run using JUnit (http://www.junit.org).
 */
public class AxisPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.axis");
    suite.addTestSuite(AxisLocationTests.class);
    suite.addTestSuite(AxisSpaceTests.class);
    suite.addTestSuite(AxisTests.class);
    suite.addTestSuite(CategoryAnchorTests.class);
    suite.addTestSuite(CategoryAxisTests.class);
    suite.addTestSuite(CategoryAxis3DTests.class);
    suite.addTestSuite(CategoryLabelPositionTests.class);
    suite.addTestSuite(CategoryLabelPositionsTests.class);
    suite.addTestSuite(CategoryLabelWidthTypeTests.class);
    suite.addTestSuite(CategoryTickTests.class);
    suite.addTestSuite(CyclicNumberAxisTests.class);
    suite.addTestSuite(DateAxisTests.class);
    suite.addTestSuite(DateTickTests.class);
    suite.addTestSuite(DateTickMarkPositionTests.class);
    suite.addTestSuite(DateTickUnitTests.class);
    suite.addTestSuite(ExtendedCategoryAxisTests.class);
    suite.addTestSuite(LogAxisTests.class);
    suite.addTestSuite(LogarithmicAxisTests.class);
    suite.addTestSuite(MarkerAxisBandTests.class);
    suite.addTestSuite(ModuloAxisTests.class);
    suite.addTestSuite(MonthDateFormatTests.class);
    suite.addTestSuite(NumberAxisTests.class);
    suite.addTestSuite(NumberAxis3DTests.class);
    suite.addTestSuite(NumberTickUnitTests.class);
    suite.addTestSuite(PeriodAxisTests.class);
    suite.addTestSuite(PeriodAxisLabelInfoTests.class);
    suite.addTestSuite(QuarterDateFormatTests.class);
    suite.addTestSuite(SegmentedTimelineTests.class);
    suite.addTestSuite(SegmentedTimelineTests2.class);
    suite.addTestSuite(StandardTickUnitSourceTests.class);
    suite.addTestSuite(SubCategoryAxisTests.class);
    suite.addTestSuite(SymbolAxisTests.class);
    suite.addTestSuite(TickUnitsTests.class);
    suite.addTestSuite(ValueAxisTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public AxisPackageTests(  String name){
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
