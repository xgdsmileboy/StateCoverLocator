package org.jfree.data.time.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.time.Day;
import org.jfree.data.time.MonthConstants;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
/** 
 * Tests for the             {@link MovingAverage} class.
 */
public class MovingAverageTests extends TestCase {
  private static final double EPSILON=0.0000000001;
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(MovingAverageTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public MovingAverageTests(  String name){
    super(name);
  }
  /** 
 * A test for the values calculated from a time series.
 */
  public void test1(){
    TimeSeries source=createDailyTimeSeries1();
    TimeSeries maverage=MovingAverage.createMovingAverage(source,"Moving Average",3,3);
    assertEquals(7,maverage.getItemCount());
    double value=maverage.getValue(0).doubleValue();
    assertEquals(14.1,value,EPSILON);
    value=maverage.getValue(1).doubleValue();
    assertEquals(13.4,value,EPSILON);
    value=maverage.getValue(2).doubleValue();
    assertEquals(14.433333333333,value,EPSILON);
    value=maverage.getValue(3).doubleValue();
    assertEquals(14.933333333333,value,EPSILON);
    value=maverage.getValue(4).doubleValue();
    assertEquals(19.8,value,EPSILON);
    value=maverage.getValue(5).doubleValue();
    assertEquals(15.25,value,EPSILON);
    value=maverage.getValue(6).doubleValue();
    assertEquals(12.5,value,EPSILON);
  }
  /** 
 * Creates a sample series.
 * @return A sample series.
 */
  private TimeSeries createDailyTimeSeries1(){
    TimeSeries series=new TimeSeries("Series 1");
    series.add(new Day(11,MonthConstants.AUGUST,2003),11.2);
    series.add(new Day(13,MonthConstants.AUGUST,2003),13.8);
    series.add(new Day(17,MonthConstants.AUGUST,2003),14.1);
    series.add(new Day(18,MonthConstants.AUGUST,2003),12.7);
    series.add(new Day(19,MonthConstants.AUGUST,2003),16.5);
    series.add(new Day(20,MonthConstants.AUGUST,2003),15.6);
    series.add(new Day(25,MonthConstants.AUGUST,2003),19.8);
    series.add(new Day(27,MonthConstants.AUGUST,2003),10.7);
    series.add(new Day(28,MonthConstants.AUGUST,2003),14.3);
    return series;
  }
}
