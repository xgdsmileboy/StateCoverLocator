package org.jfree.data.time.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/** 
 * Some tests for the <code>org.jfree.data.time</code> package that can be run using JUnit. You can find more information about JUnit at http://www.junit.org.
 */
public class DataTimePackageTests extends TestCase {
  /** 
 * Returns a test suite to the JUnit test runner.
 * @return The test suite.
 */
  public static Test suite(){
    TestSuite suite=new TestSuite("org.jfree.data.time");
    suite.addTestSuite(DateRangeTests.class);
    suite.addTestSuite(DayTests.class);
    suite.addTestSuite(FixedMillisecondTests.class);
    suite.addTestSuite(HourTests.class);
    suite.addTestSuite(MinuteTests.class);
    suite.addTestSuite(MillisecondTests.class);
    suite.addTestSuite(MonthTests.class);
    suite.addTestSuite(MovingAverageTests.class);
    suite.addTestSuite(QuarterTests.class);
    suite.addTestSuite(SecondTests.class);
    suite.addTestSuite(SimpleTimePeriodTests.class);
    suite.addTestSuite(TimePeriodAnchorTests.class);
    suite.addTestSuite(TimePeriodValueTests.class);
    suite.addTestSuite(TimePeriodValuesTests.class);
    suite.addTestSuite(TimePeriodValuesCollectionTests.class);
    suite.addTestSuite(TimeSeriesCollectionTests.class);
    suite.addTestSuite(TimeSeriesTests.class);
    suite.addTestSuite(TimeSeriesDataItemTests.class);
    suite.addTestSuite(TimeTableXYDatasetTests.class);
    suite.addTestSuite(WeekTests.class);
    suite.addTestSuite(YearTests.class);
    return suite;
  }
  /** 
 * Constructs the test suite.
 * @param name  the test suite name.
 */
  public DataTimePackageTests(  String name){
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
