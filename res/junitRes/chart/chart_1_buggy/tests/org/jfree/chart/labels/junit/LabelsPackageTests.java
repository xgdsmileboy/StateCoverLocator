package org.jfree.chart.labels.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * A collection of tests for the <code>org.jfree.chart.labels</code> package. <P> These tests can be run using JUnit (http://www.junit.org).
 */
public class LabelsPackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.chart.labels");
    suite.addTestSuite(BoxAndWhiskerToolTipGeneratorTests.class);
    suite.addTestSuite(BoxAndWhiskerXYToolTipGeneratorTests.class);
    suite.addTestSuite(BubbleXYItemLabelGeneratorTests.class);
    suite.addTestSuite(CustomXYItemLabelGeneratorTests.class);
    suite.addTestSuite(HighLowItemLabelGeneratorTests.class);
    suite.addTestSuite(IntervalCategoryItemLabelGeneratorTests.class);
    suite.addTestSuite(IntervalCategoryToolTipGeneratorTests.class);
    suite.addTestSuite(ItemLabelAnchorTests.class);
    suite.addTestSuite(ItemLabelPositionTests.class);
    suite.addTestSuite(MultipleXYSeriesLabelGeneratorTests.class);
    suite.addTestSuite(StandardCategoryItemLabelGeneratorTests.class);
    suite.addTestSuite(StandardCategorySeriesLabelGeneratorTests.class);
    suite.addTestSuite(StandardCategoryToolTipGeneratorTests.class);
    suite.addTestSuite(StandardPieSectionLabelGeneratorTests.class);
    suite.addTestSuite(StandardPieToolTipGeneratorTests.class);
    suite.addTestSuite(StandardXYItemLabelGeneratorTests.class);
    suite.addTestSuite(StandardXYSeriesLabelGeneratorTests.class);
    suite.addTestSuite(StandardXYToolTipGeneratorTests.class);
    suite.addTestSuite(StandardXYZToolTipGeneratorTests.class);
    suite.addTestSuite(SymbolicXYItemLabelGeneratorTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the suite name.
 */
  public LabelsPackageTests(  String name){
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
