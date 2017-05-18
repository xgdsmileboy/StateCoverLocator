package org.jfree.data.time;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
/** 
 * Represents a year in the range -9999 to 9999.  This class is immutable, which is a requirement for all                                                                                               {@link RegularTimePeriod} subclasses.
 */
public class Year extends RegularTimePeriod implements Serializable {
  /** 
 * The minimum year value.
 * @since 1.0.11
 */
  public static final int MINIMUM_YEAR=-9999;
  /** 
 * The maximum year value.
 * @since 1.0.11
 */
  public static final int MAXIMUM_YEAR=9999;
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-7659990929736074836L;
  /** 
 * The year. 
 */
  private short year;
  /** 
 * The first millisecond. 
 */
  private long firstMillisecond;
  /** 
 * The last millisecond. 
 */
  private long lastMillisecond;
  /** 
 * Creates a new <code>Year</code>, based on the current system date/time.
 */
  public Year(){
    this(new Date());
  }
  /** 
 * Creates a time period representing a single year.
 * @param year  the year.
 */
  public Year(  int year){
    if ((year < Year.MINIMUM_YEAR) || (year > Year.MAXIMUM_YEAR)) {
      throw new IllegalArgumentException("Year constructor: year (" + year + ") outside valid range.");
    }
    this.year=(short)year;
    peg(Calendar.getInstance());
  }
  /** 
 * Creates a new <code>Year</code>, based on a particular instant in time, using the default time zone.
 * @param time  the time (<code>null</code> not permitted).
 * @see #Year(Date,TimeZone)
 */
  public Year(  Date time){
    this(time,TimeZone.getDefault());
  }
  /** 
 * Constructs a year, based on a particular instant in time and a time zone.
 * @param time  the time (<code>null</code> not permitted).
 * @param zone  the time zone.
 * @deprecated Since 1.0.12, use {@link #Year(Date,TimeZone,Locale)}instead.
 */
  public Year(  Date time,  TimeZone zone){
    this(time,zone,Locale.getDefault());
  }
  /** 
 * Creates a new <code>Year</code> instance, for the specified time zone and locale.
 * @param time  the current time (<code>null</code> not permitted).
 * @param zone  the time zone.
 * @param locale  the locale.
 * @since 1.0.12
 */
  public Year(  Date time,  TimeZone zone,  Locale locale){
    Calendar calendar=Calendar.getInstance(zone,locale);
    calendar.setTime(time);
    this.year=(short)calendar.get(Calendar.YEAR);
    peg(calendar);
  }
  /** 
 * Returns the year.
 * @return The year.
 */
  public int getYear(){
    return this.year;
  }
  /** 
 * Returns the first millisecond of the year.  This will be determined relative to the time zone specified in the constructor, or in the calendar instance passed in the most recent call to the                                                                                              {@link #peg(Calendar)} method.
 * @return The first millisecond of the year.
 * @see #getLastMillisecond()
 */
  public long getFirstMillisecond(){
    return this.firstMillisecond;
  }
  /** 
 * Returns the last millisecond of the year.  This will be determined relative to the time zone specified in the constructor, or in the calendar instance passed in the most recent call to the                                                                                              {@link #peg(Calendar)} method.
 * @return The last millisecond of the year.
 * @see #getFirstMillisecond()
 */
  public long getLastMillisecond(){
    return this.lastMillisecond;
  }
  /** 
 * Recalculates the start date/time and end date/time for this time period relative to the supplied calendar (which incorporates a time zone).
 * @param calendar  the calendar (<code>null</code> not permitted).
 * @since 1.0.3
 */
  public void peg(  Calendar calendar){
    this.firstMillisecond=getFirstMillisecond(calendar);
    this.lastMillisecond=getLastMillisecond(calendar);
  }
  /** 
 * Returns the year preceding this one.
 * @return The year preceding this one (or <code>null</code> if thecurrent year is -9999).
 */
  public RegularTimePeriod previous(){
    if (this.year > Year.MINIMUM_YEAR) {
      return new Year(this.year - 1);
    }
 else {
      return null;
    }
  }
  /** 
 * Returns the year following this one.
 * @return The year following this one (or <code>null</code> if the currentyear is 9999).
 */
  public RegularTimePeriod next(){
    if (this.year < Year.MAXIMUM_YEAR) {
      return new Year(this.year + 1);
    }
 else {
      return null;
    }
  }
  /** 
 * Returns a serial index number for the year. <P> The implementation simply returns the year number (e.g. 2002).
 * @return The serial index number.
 */
  public long getSerialIndex(){
    return this.year;
  }
  /** 
 * Returns the first millisecond of the year, evaluated using the supplied calendar (which determines the time zone).
 * @param calendar  the calendar (<code>null</code> not permitted).
 * @return The first millisecond of the year.
 * @throws NullPointerException if <code>calendar</code> is<code>null</code>.
 */
  public long getFirstMillisecond(  Calendar calendar){
    calendar.set(this.year,Calendar.JANUARY,1,0,0,0);
    calendar.set(Calendar.MILLISECOND,0);
    return calendar.getTime().getTime();
  }
  /** 
 * Returns the last millisecond of the year, evaluated using the supplied calendar (which determines the time zone).
 * @param calendar  the calendar (<code>null</code> not permitted).
 * @return The last millisecond of the year.
 * @throws NullPointerException if <code>calendar</code> is<code>null</code>.
 */
  public long getLastMillisecond(  Calendar calendar){
    calendar.set(this.year,Calendar.DECEMBER,31,23,59,59);
    calendar.set(Calendar.MILLISECOND,999);
    return calendar.getTime().getTime();
  }
  /** 
 * Tests the equality of this <code>Year</code> object to an arbitrary object.  Returns <code>true</code> if the target is a <code>Year</code> instance representing the same year as this object.  In all other cases, returns <code>false</code>.
 * @param obj  the object (<code>null</code> permitted).
 * @return <code>true</code> if the year of this and the object are thesame.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Year)) {
      return false;
    }
    Year that=(Year)obj;
    return (this.year == that.year);
  }
  /** 
 * Returns a hash code for this object instance.  The approach described by Joshua Bloch in "Effective Java" has been used here: <p> <code>http://developer.java.sun.com/developer/Books/effectivejava /Chapter3.pdf</code>
 * @return A hash code.
 */
  public int hashCode(){
    int result=17;
    int c=this.year;
    result=37 * result + c;
    return result;
  }
  /** 
 * Returns an integer indicating the order of this <code>Year</code> object relative to the specified object: negative == before, zero == same, positive == after.
 * @param o1  the object to compare.
 * @return negative == before, zero == same, positive == after.
 */
  public int compareTo(  Object o1){
    int result;
    if (o1 instanceof Year) {
      Year y=(Year)o1;
      result=this.year - y.getYear();
    }
 else {
      if (o1 instanceof RegularTimePeriod) {
        result=0;
      }
 else {
        result=1;
      }
    }
    return result;
  }
  /** 
 * Returns a string representing the year..
 * @return A string representing the year.
 */
  public String toString(){
    return Integer.toString(this.year);
  }
  /** 
 * Parses the string argument as a year. <P> The string format is YYYY.
 * @param s  a string representing the year.
 * @return <code>null</code> if the string is not parseable, the yearotherwise.
 */
  public static Year parseYear(  String s){
    int y;
    try {
      y=Integer.parseInt(s.trim());
    }
 catch (    NumberFormatException e) {
      throw new TimePeriodFormatException("Cannot parse string.");
    }
    try {
      return new Year(y);
    }
 catch (    IllegalArgumentException e) {
      throw new TimePeriodFormatException("Year outside valid range.");
    }
  }
}
