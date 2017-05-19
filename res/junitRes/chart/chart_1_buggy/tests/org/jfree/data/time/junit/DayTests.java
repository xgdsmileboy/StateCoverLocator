package org.jfree.data.time.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.time.Day;
import org.jfree.data.time.MonthConstants;
/** 
 * Tests for the             {@link Day} class.
 */
public class DayTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(DayTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public DayTests(  String name){
    super(name);
  }
  /** 
 * Common test setup.
 */
  protected void setUp(){
  }
  /** 
 * Check that a Day instance is equal to itself. SourceForge Bug ID: 558850.
 */
  public void testEqualsSelf(){
    Day day=new Day();
    assertTrue(day.equals(day));
  }
  /** 
 * Tests the equals method.
 */
  public void testEquals(){
    Day day1=new Day(29,MonthConstants.MARCH,2002);
    Day day2=new Day(29,MonthConstants.MARCH,2002);
    assertTrue(day1.equals(day2));
  }
  /** 
 * In GMT, the end of 29 Feb 2004 is java.util.Date(1,078,099,199,999L). Use this to check the day constructor.
 */
  public void testDateConstructor1(){
    TimeZone zone=TimeZone.getTimeZone("GMT");
    Calendar c=new GregorianCalendar(zone);
    Day d1=new Day(new Date(1078099199999L),zone);
    Day d2=new Day(new Date(1078099200000L),zone);
    assertEquals(MonthConstants.FEBRUARY,d1.getMonth());
    assertEquals(1078099199999L,d1.getLastMillisecond(c));
    assertEquals(MonthConstants.MARCH,d2.getMonth());
    assertEquals(1078099200000L,d2.getFirstMillisecond(c));
  }
  /** 
 * In Helsinki, the end of 29 Feb 2004 is java.util.Date(1,078,091,999,999L).  Use this to check the Day constructor.
 */
  public void testDateConstructor2(){
    TimeZone zone=TimeZone.getTimeZone("Europe/Helsinki");
    Calendar c=new GregorianCalendar(zone);
    Day d1=new Day(new Date(1078091999999L),zone);
    Day d2=new Day(new Date(1078092000000L),zone);
    assertEquals(MonthConstants.FEBRUARY,d1.getMonth());
    assertEquals(1078091999999L,d1.getLastMillisecond(c));
    assertEquals(MonthConstants.MARCH,d2.getMonth());
    assertEquals(1078092000000L,d2.getFirstMillisecond(c));
  }
  /** 
 * Set up a day equal to 1 January 1900.  Request the previous day, it should be null.
 */
  public void test1Jan1900Previous(){
    Day jan1st1900=new Day(1,MonthConstants.JANUARY,1900);
    Day previous=(Day)jan1st1900.previous();
    assertNull(previous);
  }
  /** 
 * Set up a day equal to 1 January 1900.  Request the next day, it should be 2 January 1900.
 */
  public void test1Jan1900Next(){
    Day jan1st1900=new Day(1,MonthConstants.JANUARY,1900);
    Day next=(Day)jan1st1900.next();
    assertEquals(2,next.getDayOfMonth());
  }
  /** 
 * Set up a day equal to 31 December 9999.  Request the previous day, it should be 30 December 9999.
 */
  public void test31Dec9999Previous(){
    Day dec31st9999=new Day(31,MonthConstants.DECEMBER,9999);
    Day previous=(Day)dec31st9999.previous();
    assertEquals(30,previous.getDayOfMonth());
  }
  /** 
 * Set up a day equal to 31 December 9999.  Request the next day, it should be null.
 */
  public void test31Dec9999Next(){
    Day dec31st9999=new Day(31,MonthConstants.DECEMBER,9999);
    Day next=(Day)dec31st9999.next();
    assertNull(next);
  }
  /** 
 * Problem for date parsing. <p> This test works only correct if the short pattern of the date format is "dd/MM/yyyy". If not, this test will result in a false negative.
 * @throws ParseException on parsing errors.
 */
  public void testParseDay() throws ParseException {
    GregorianCalendar gc=new GregorianCalendar(2001,12,31);
    SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
    Date reference=format.parse("31/12/2001");
    if (reference.equals(gc.getTime())) {
      Day d=Day.parseDay("31/12/2001");
      assertEquals(37256,d.getSerialDate().toSerial());
    }
    Day d=Day.parseDay("2001-12-31");
    assertEquals(37256,d.getSerialDate().toSerial());
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    Day d1=new Day(15,4,2000);
    Day d2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(d1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      d2=(Day)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(d1,d2);
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    Day d1=new Day(1,2,2003);
    Day d2=new Day(1,2,2003);
    assertTrue(d1.equals(d2));
    int h1=d1.hashCode();
    int h2=d2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * The             {@link Day} class is immutable, so should not be {@link Cloneable}.
 */
  public void testNotCloneable(){
    Day d=new Day(1,2,2003);
    assertFalse(d instanceof Cloneable);
  }
  /** 
 * Some checks for the getSerialIndex() method.
 */
  public void testGetSerialIndex(){
    Day d=new Day(1,1,1900);
    assertEquals(2,d.getSerialIndex());
    d=new Day(15,4,2000);
    assertEquals(36631,d.getSerialIndex());
  }
  /** 
 * Some checks for the getFirstMillisecond() method.
 */
  public void testGetFirstMillisecond(){
    Locale saved=Locale.getDefault();
    Locale.setDefault(Locale.UK);
    TimeZone savedZone=TimeZone.getDefault();
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    Day d=new Day(1,3,1970);
    assertEquals(5094000000L,d.getFirstMillisecond());
    Locale.setDefault(saved);
    TimeZone.setDefault(savedZone);
  }
  /** 
 * Some checks for the getFirstMillisecond(TimeZone) method.
 */
  public void testGetFirstMillisecondWithTimeZone(){
    Day d=new Day(26,4,1950);
    TimeZone zone=TimeZone.getTimeZone("America/Los_Angeles");
    Calendar c=new GregorianCalendar(zone);
    assertEquals(-621187200000L,d.getFirstMillisecond(c));
    boolean pass=false;
    try {
      d.getFirstMillisecond((Calendar)null);
    }
 catch (    NullPointerException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getFirstMillisecond(TimeZone) method.
 */
  public void testGetFirstMillisecondWithCalendar(){
    Day d=new Day(1,12,2001);
    GregorianCalendar calendar=new GregorianCalendar(Locale.GERMANY);
    calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
    assertEquals(1007164800000L,d.getFirstMillisecond(calendar));
    boolean pass=false;
    try {
      d.getFirstMillisecond((Calendar)null);
    }
 catch (    NullPointerException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getLastMillisecond() method.
 */
  public void testGetLastMillisecond(){
    Locale saved=Locale.getDefault();
    Locale.setDefault(Locale.UK);
    TimeZone savedZone=TimeZone.getDefault();
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    Day d=new Day(1,1,1970);
    assertEquals(82799999L,d.getLastMillisecond());
    Locale.setDefault(saved);
    TimeZone.setDefault(savedZone);
  }
  /** 
 * Some checks for the getLastMillisecond(TimeZone) method.
 */
  public void testGetLastMillisecondWithTimeZone(){
    Day d=new Day(1,2,1950);
    TimeZone zone=TimeZone.getTimeZone("America/Los_Angeles");
    Calendar c=new GregorianCalendar(zone);
    assertEquals(-628358400001L,d.getLastMillisecond(c));
    boolean pass=false;
    try {
      d.getLastMillisecond((Calendar)null);
    }
 catch (    NullPointerException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getLastMillisecond(TimeZone) method.
 */
  public void testGetLastMillisecondWithCalendar(){
    Day d=new Day(4,5,2001);
    Calendar calendar=Calendar.getInstance(TimeZone.getTimeZone("Europe/London"),Locale.UK);
    assertEquals(989017199999L,d.getLastMillisecond(calendar));
    boolean pass=false;
    try {
      d.getLastMillisecond((Calendar)null);
    }
 catch (    NullPointerException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the testNext() method.
 */
  public void testNext(){
    Day d=new Day(25,12,2000);
    d=(Day)d.next();
    assertEquals(2000,d.getYear());
    assertEquals(12,d.getMonth());
    assertEquals(26,d.getDayOfMonth());
    d=new Day(31,12,9999);
    assertNull(d.next());
  }
  /** 
 * Some checks for the getStart() method.
 */
  public void testGetStart(){
    Locale saved=Locale.getDefault();
    Locale.setDefault(Locale.ITALY);
    Calendar cal=Calendar.getInstance(Locale.ITALY);
    cal.set(2006,Calendar.NOVEMBER,3,0,0,0);
    cal.set(Calendar.MILLISECOND,0);
    Day d=new Day(3,11,2006);
    assertEquals(cal.getTime(),d.getStart());
    Locale.setDefault(saved);
  }
  /** 
 * Some checks for the getEnd() method.
 */
  public void testGetEnd(){
    Locale saved=Locale.getDefault();
    Locale.setDefault(Locale.ITALY);
    Calendar cal=Calendar.getInstance(Locale.ITALY);
    cal.set(1900,Calendar.JANUARY,1,23,59,59);
    cal.set(Calendar.MILLISECOND,999);
    Day d=new Day(1,1,1900);
    assertEquals(cal.getTime(),d.getEnd());
    Locale.setDefault(saved);
  }
}