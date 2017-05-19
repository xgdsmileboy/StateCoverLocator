package org.jfree.data.time.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.MonthConstants;
import org.jfree.data.time.Second;
/** 
 * Tests for the             {@link Second} class.
 */
public class SecondTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(SecondTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public SecondTests(  String name){
    super(name);
  }
  /** 
 * Common test setup.
 */
  protected void setUp(){
  }
  /** 
 * Test that a Second instance is equal to itself. SourceForge Bug ID: 558850.
 */
  public void testEqualsSelf(){
    Second second=new Second();
    assertTrue(second.equals(second));
  }
  /** 
 * Tests the equals method.
 */
  public void testEquals(){
    Day day1=new Day(29,MonthConstants.MARCH,2002);
    Hour hour1=new Hour(15,day1);
    Minute minute1=new Minute(15,hour1);
    Second second1=new Second(34,minute1);
    Day day2=new Day(29,MonthConstants.MARCH,2002);
    Hour hour2=new Hour(15,day2);
    Minute minute2=new Minute(15,hour2);
    Second second2=new Second(34,minute2);
    assertTrue(second1.equals(second2));
  }
  /** 
 * In GMT, the 4.55:59pm on 21 Mar 2002 is java.util.Date(1016729759000L). Use this to check the Second constructor.
 */
  public void testDateConstructor1(){
    TimeZone zone=TimeZone.getTimeZone("GMT");
    Calendar c=new GregorianCalendar(zone);
    Second s1=new Second(new Date(1016729758999L),zone);
    Second s2=new Second(new Date(1016729759000L),zone);
    assertEquals(58,s1.getSecond());
    assertEquals(1016729758999L,s1.getLastMillisecond(c));
    assertEquals(59,s2.getSecond());
    assertEquals(1016729759000L,s2.getFirstMillisecond(c));
  }
  /** 
 * In Chicago, the 4.55:59pm on 21 Mar 2002 is java.util.Date(1016751359000L). Use this to check the Second constructor.
 */
  public void testDateConstructor2(){
    TimeZone zone=TimeZone.getTimeZone("America/Chicago");
    Calendar c=new GregorianCalendar(zone);
    Second s1=new Second(new Date(1016751358999L),zone);
    Second s2=new Second(new Date(1016751359000L),zone);
    assertEquals(58,s1.getSecond());
    assertEquals(1016751358999L,s1.getLastMillisecond(c));
    assertEquals(59,s2.getSecond());
    assertEquals(1016751359000L,s2.getFirstMillisecond(c));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    Second s1=new Second();
    Second s2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(s1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      s2=(Second)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(s1,s2);
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    Second s1=new Second(13,45,5,1,2,2003);
    Second s2=new Second(13,45,5,1,2,2003);
    assertTrue(s1.equals(s2));
    int h1=s1.hashCode();
    int h2=s2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * The             {@link Second} class is immutable, so should not be{@link Cloneable}.
 */
  public void testNotCloneable(){
    Second s=new Second(13,45,5,1,2,2003);
    assertFalse(s instanceof Cloneable);
  }
  /** 
 * Some checks for the getFirstMillisecond() method.
 */
  public void testGetFirstMillisecond(){
    Locale saved=Locale.getDefault();
    Locale.setDefault(Locale.UK);
    TimeZone savedZone=TimeZone.getDefault();
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    Second s=new Second(15,43,15,1,4,2006);
    assertEquals(1143902595000L,s.getFirstMillisecond());
    Locale.setDefault(saved);
    TimeZone.setDefault(savedZone);
  }
  /** 
 * Some checks for the getFirstMillisecond(TimeZone) method.
 */
  public void testGetFirstMillisecondWithTimeZone(){
    Second s=new Second(50,59,15,1,4,1950);
    TimeZone zone=TimeZone.getTimeZone("America/Los_Angeles");
    Calendar c=new GregorianCalendar(zone);
    assertEquals(-623289610000L,s.getFirstMillisecond(c));
    boolean pass=false;
    try {
      s.getFirstMillisecond((Calendar)null);
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
    Second s=new Second(55,40,2,15,4,2000);
    GregorianCalendar calendar=new GregorianCalendar(Locale.GERMANY);
    calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
    assertEquals(955766455000L,s.getFirstMillisecond(calendar));
    boolean pass=false;
    try {
      s.getFirstMillisecond((Calendar)null);
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
    Second s=new Second(1,1,1,1,1,1970);
    assertEquals(61999L,s.getLastMillisecond());
    Locale.setDefault(saved);
    TimeZone.setDefault(savedZone);
  }
  /** 
 * Some checks for the getLastMillisecond(TimeZone) method.
 */
  public void testGetLastMillisecondWithTimeZone(){
    Second s=new Second(55,1,2,7,7,1950);
    TimeZone zone=TimeZone.getTimeZone("America/Los_Angeles");
    Calendar c=new GregorianCalendar(zone);
    assertEquals(-614962684001L,s.getLastMillisecond(c));
    boolean pass=false;
    try {
      s.getLastMillisecond((Calendar)null);
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
    Second s=new Second(50,45,21,21,4,2001);
    GregorianCalendar calendar=new GregorianCalendar(Locale.GERMANY);
    calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
    assertEquals(987889550999L,s.getLastMillisecond(calendar));
    boolean pass=false;
    try {
      s.getLastMillisecond((Calendar)null);
    }
 catch (    NullPointerException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getSerialIndex() method.
 */
  public void testGetSerialIndex(){
    Second s=new Second(1,1,1,1,1,2000);
    assertEquals(3155850061L,s.getSerialIndex());
    s=new Second(1,1,1,1,1,1900);
    assertEquals(176461L,s.getSerialIndex());
  }
  /** 
 * Some checks for the testNext() method.
 */
  public void testNext(){
    Second s=new Second(55,30,1,12,12,2000);
    s=(Second)s.next();
    assertEquals(2000,s.getMinute().getHour().getYear());
    assertEquals(12,s.getMinute().getHour().getMonth());
    assertEquals(12,s.getMinute().getHour().getDayOfMonth());
    assertEquals(1,s.getMinute().getHour().getHour());
    assertEquals(30,s.getMinute().getMinute());
    assertEquals(56,s.getSecond());
    s=new Second(59,59,23,31,12,9999);
    assertNull(s.next());
  }
  /** 
 * Some checks for the getStart() method.
 */
  public void testGetStart(){
    Locale saved=Locale.getDefault();
    Locale.setDefault(Locale.ITALY);
    Calendar cal=Calendar.getInstance(Locale.ITALY);
    cal.set(2006,Calendar.JANUARY,16,3,47,55);
    cal.set(Calendar.MILLISECOND,0);
    Second s=new Second(55,47,3,16,1,2006);
    assertEquals(cal.getTime(),s.getStart());
    Locale.setDefault(saved);
  }
  /** 
 * Some checks for the getEnd() method.
 */
  public void testGetEnd(){
    Locale saved=Locale.getDefault();
    Locale.setDefault(Locale.ITALY);
    Calendar cal=Calendar.getInstance(Locale.ITALY);
    cal.set(2006,Calendar.JANUARY,16,3,47,55);
    cal.set(Calendar.MILLISECOND,999);
    Second s=new Second(55,47,3,16,1,2006);
    assertEquals(cal.getTime(),s.getEnd());
    Locale.setDefault(saved);
  }
}
