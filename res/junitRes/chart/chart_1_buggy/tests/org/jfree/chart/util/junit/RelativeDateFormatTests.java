package org.jfree.chart.util.junit;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.util.RelativeDateFormat;
/** 
 * Tests for the             {@link RelativeDateFormat} class.
 */
public class RelativeDateFormatTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(RelativeDateFormatTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public RelativeDateFormatTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the formatting.
 */
  public void testFormat(){
    RelativeDateFormat rdf=new RelativeDateFormat();
    String s=rdf.format(new Date(2 * 60L * 60L* 1000L + 122500L));
    assertEquals("2h2m2.500s",s);
  }
  /** 
 * Test that we can configure the RelativeDateFormat to show hh:mm:ss.
 */
  public void test2033092(){
    RelativeDateFormat rdf=new RelativeDateFormat();
    rdf.setShowZeroDays(false);
    rdf.setShowZeroHours(false);
    rdf.setMinuteSuffix(":");
    rdf.setHourSuffix(":");
    rdf.setSecondSuffix("");
    DecimalFormat hoursFormatter=new DecimalFormat();
    hoursFormatter.setMaximumFractionDigits(0);
    hoursFormatter.setMaximumIntegerDigits(2);
    hoursFormatter.setMinimumIntegerDigits(2);
    rdf.setHourFormatter(hoursFormatter);
    DecimalFormat minsFormatter=new DecimalFormat();
    minsFormatter.setMaximumFractionDigits(0);
    minsFormatter.setMaximumIntegerDigits(2);
    minsFormatter.setMinimumIntegerDigits(2);
    rdf.setMinuteFormatter(minsFormatter);
    DecimalFormat secondsFormatter=new DecimalFormat();
    secondsFormatter.setMaximumFractionDigits(0);
    secondsFormatter.setMaximumIntegerDigits(2);
    secondsFormatter.setMinimumIntegerDigits(2);
    rdf.setSecondFormatter(secondsFormatter);
    String s=rdf.format(new Date(2 * 60L * 60L* 1000L + 122500L));
    assertEquals("02:02:02",s);
  }
  /** 
 * Check that the equals() method can distinguish all fields.
 */
  public void testEquals(){
    RelativeDateFormat df1=new RelativeDateFormat();
    RelativeDateFormat df2=new RelativeDateFormat();
    assertEquals(df1,df2);
    df1.setBaseMillis(123L);
    assertFalse(df1.equals(df2));
    df2.setBaseMillis(123L);
    assertTrue(df1.equals(df2));
    df1.setDayFormatter(new DecimalFormat("0%"));
    assertFalse(df1.equals(df2));
    df2.setDayFormatter(new DecimalFormat("0%"));
    assertTrue(df1.equals(df2));
    df1.setDaySuffix("D");
    assertFalse(df1.equals(df2));
    df2.setDaySuffix("D");
    assertTrue(df1.equals(df2));
    df1.setHourFormatter(new DecimalFormat("0%"));
    assertFalse(df1.equals(df2));
    df2.setHourFormatter(new DecimalFormat("0%"));
    assertTrue(df1.equals(df2));
    df1.setHourSuffix("H");
    assertFalse(df1.equals(df2));
    df2.setHourSuffix("H");
    assertTrue(df1.equals(df2));
    df1.setMinuteFormatter(new DecimalFormat("0%"));
    assertFalse(df1.equals(df2));
    df2.setMinuteFormatter(new DecimalFormat("0%"));
    assertTrue(df1.equals(df2));
    df1.setMinuteSuffix("M");
    assertFalse(df1.equals(df2));
    df2.setMinuteSuffix("M");
    assertTrue(df1.equals(df2));
    df1.setSecondSuffix("S");
    assertFalse(df1.equals(df2));
    df2.setSecondSuffix("S");
    assertTrue(df1.equals(df2));
    df1.setShowZeroDays(!df1.getShowZeroDays());
    assertFalse(df1.equals(df2));
    df2.setShowZeroDays(!df2.getShowZeroDays());
    assertTrue(df1.equals(df2));
    df1.setSecondFormatter(new DecimalFormat("0.0"));
    assertFalse(df1.equals(df2));
    df2.setSecondFormatter(new DecimalFormat("0.0"));
    assertTrue(df1.equals(df2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    RelativeDateFormat df1=new RelativeDateFormat(123L);
    RelativeDateFormat df2=new RelativeDateFormat(123L);
    assertTrue(df1.equals(df2));
    int h1=df1.hashCode();
    int h2=df2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    NumberFormat nf=new DecimalFormat("0");
    RelativeDateFormat df1=new RelativeDateFormat();
    df1.setSecondFormatter(nf);
    RelativeDateFormat df2=null;
    df2=(RelativeDateFormat)df1.clone();
    assertTrue(df1 != df2);
    assertTrue(df1.getClass() == df2.getClass());
    assertTrue(df1.equals(df2));
    nf.setMinimumFractionDigits(2);
    assertFalse(df1.equals(df2));
  }
  /** 
 * Some tests for negative dates.
 */
  public void testNegative(){
    NumberFormat nf=new DecimalFormat("0");
    RelativeDateFormat df1=new RelativeDateFormat();
    df1.setSecondFormatter(nf);
    assertEquals("-0h0m1s",df1.format(new Date(-1000L)));
  }
}
