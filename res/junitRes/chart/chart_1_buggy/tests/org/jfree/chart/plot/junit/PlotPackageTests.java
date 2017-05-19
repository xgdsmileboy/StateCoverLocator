package org.jfree.chart.plot.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the org.jfree.chart.plot package. <P> These tests can be run using JUnit (http://www.junit.org).
 */
public class PlotPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.plot");
    suite.addTestSuite(CategoryMarkerTests.class);
    suite.addTestSuite(CategoryPlotTests.class);
    suite.addTestSuite(CombinedDomainCategoryPlotTests.class);
    suite.addTestSuite(CombinedDomainXYPlotTests.class);
    suite.addTestSuite(CombinedRangeCategoryPlotTests.class);
    suite.addTestSuite(CombinedRangeXYPlotTests.class);
    suite.addTestSuite(CompassPlotTests.class);
    suite.addTestSuite(DefaultDrawingSupplierTests.class);
    suite.addTestSuite(FastScatterPlotTests.class);
    suite.addTestSuite(IntervalMarkerTests.class);
    suite.addTestSuite(MarkerTests.class);
    suite.addTestSuite(MeterIntervalTests.class);
    suite.addTestSuite(MeterPlotTests.class);
    suite.addTestSuite(MultiplePiePlotTests.class);
    suite.addTestSuite(PieLabelRecordTests.class);
    suite.addTestSuite(PiePlotTests.class);
    suite.addTestSuite(PiePlot3DTests.class);
    suite.addTestSuite(PlotOrientationTests.class);
    suite.addTestSuite(PlotRenderingInfoTests.class);
    suite.addTestSuite(PlotTests.class);
    suite.addTestSuite(PolarPlotTests.class);
    suite.addTestSuite(RingPlotTests.class);
    suite.addTestSuite(SpiderWebPlotTests.class);
    suite.addTestSuite(ThermometerPlotTests.class);
    suite.addTestSuite(ValueMarkerTests.class);
    suite.addTestSuite(XYPlotTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public PlotPackageTests(  String name){
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
