package org.jfree.data.time;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
/** 
 * Represents a minute.  This class is immutable, which is a requirement for all                                                                                               {@link RegularTimePeriod} subclasses.
 */
public class Minute extends RegularTimePeriod implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=2144572840034842871L;
  /** 
 * Useful constant for the first minute in a day. 
 */
  public static final int FIRST_MINUTE_IN_HOUR=0;
  /** 
 * Useful constant for the last minute in a day. 
 */
  public static final int LAST_MINUTE_IN_HOUR=59;
  /** 
 * The day. 
 */
  private Day day;
  /** 
 * The hour in which the minute falls. 
 */
  private byte hour;
  /** 
 * The minute. 
 */
  private byte minute;
  /** 
 * The first millisecond. 
 */
  private long firstMillisecond;
  /** 
 * The last millisecond. 
 */
  private long lastMillisecond;
  /** 
 * Constructs a new Minute, based on the system date/time.
 */
  public Minute(){
    this(new Date());
  }
  /** 
 * Constructs a new Minute.
 * @param minute  the minute (0 to 59).
 * @param hour  the hour (<code>null</code> not permitted).
 */
  public Minute(  int minute,  Hour hour){
    if (hour == null) {
      throw new IllegalArgumentException("Null 'hour' argument.");
    }
    this.minute=(byte)minute;
    this.hour=(byte)hour.getHour();
    this.day=hour.getDay();
    peg(Calendar.getInstance());
  }
  /** 
 * Constructs a new instance, based on the supplied date/time and the default time zone.
 * @param time  the time (<code>null</code> not permitted).
 * @see #Minute(Date,TimeZone)
 */
  public Minute(  Date time){
    this(time,TimeZone.getDefault());
  }
  /** 
 * Constructs a new Minute, based on the supplied date/time and timezone.
 * @param time  the time (<code>null</code> not permitted).
 * @param zone  the time zone (<code>null</code> not permitted).
 */
  public Minute(  Date time,  TimeZone zone){
    if (time == null) {
      throw new IllegalArgumentException("Null 'time' argument.");
    }
    if (zone == null) {
      throw new IllegalArgumentException("Null 'zone' argument.");
    }
    Calendar calendar=Calendar.getInstance(zone);
    calendar.setTime(time);
    int min=calendar.get(Calendar.MINUTE);
    this.minute=(byte)min;
    this.hour=(byte)calendar.get(Calendar.HOUR_OF_DAY);
    this.day=new Day(time,zone);
    peg(calendar);
  }
  /** 
 * Creates a new minute.
 * @param minute  the minute (0-59).
 * @param hour  the hour (0-23).
 * @param day  the day (1-31).
 * @param month  the month (1-12).
 * @param year  the year (1900-9999).
 */
  public Minute(  int minute,  int hour,  int day,  int month,  int year){
    this(minute,new Hour(hour,new Day(day,month,year)));
  }
  /** 
 * Returns the day.
 * @return The day.
 * @since 1.0.3
 */
  public Day getDay(){
    return this.day;
  }
  /** 
 * Returns the hour.
 * @return The hour (never <code>null</code>).
 */
  public Hour getHour(){
    return new Hour(this.hour,this.day);
  }
  /** 
 * Returns the hour.
 * @return The hour.
 * @since 1.0.3
 */
  public int getHourValue(){
    return this.hour;
  }
  /** 
 * Returns the minute.
 * @return The minute.
 */
  public int getMinute(){
    return this.minute;
  }
  /** 
 * Returns the first millisecond of the minute.  This will be determined relative to the time zone specified in the constructor, or in the calendar instance passed in the most recent call to the                                                                                              {@link #peg(Calendar)} method.
 * @return The first millisecond of the minute.
 * @see #getLastMillisecond()
 */
  public long getFirstMillisecond(){
    return this.firstMillisecond;
  }
  /** 
 * Returns the last millisecond of the minute.  This will be determined relative to the time zone specified in the constructor, or in the calendar instance passed in the most recent call to the                                                                                              {@link #peg(Calendar)} method.
 * @return The last millisecond of the minute.
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
 * Returns the minute preceding this one.
 * @return The minute preceding this one.
 */
  public RegularTimePeriod previous(){
    Minute result;
    if (this.minute != FIRST_MINUTE_IN_HOUR) {
      result=new Minute(this.minute - 1,getHour());
    }
 else {
      Hour h=(Hour)getHour().previous();
      if (h != null) {
        result=new Minute(LAST_MINUTE_IN_HOUR,h);
      }
 else {
        result=null;
      }
    }
    return result;
  }
  /** 
 * Returns the minute following this one.
 * @return The minute following this one.
 */
  public RegularTimePeriod next(){
    Minute result;
    if (this.minute != LAST_MINUTE_IN_HOUR) {
      result=new Minute(this.minute + 1,getHour());
    }
 else {
      Hour nextHour=(Hour)getHour().next();
      if (nextHour != null) {
        result=new Minute(FIRST_MINUTE_IN_HOUR,nextHour);
      }
 else {
        result=null;
      }
    }
    return result;
  }
  /** 
 * Returns a serial index number for the minute.
 * @return The serial index number.
 */
  public long getSerialIndex(){
    long hourIndex=this.day.getSerialIndex() * 24L + this.hour;
    return hourIndex * 60L + this.minute;
  }
  /** 
 * Returns the first millisecond of the minute.
 * @param calendar  the calendar which defines the timezone(<code>null</code> not permitted).
 * @return The first millisecond.
 * @throws NullPointerException if <code>calendar</code> is<code>null</code>.
 */
  public long getFirstMillisecond(  Calendar calendar){
    int year=this.day.getYear();
    int month=this.day.getMonth() - 1;
    int day=this.day.getDayOfMonth();
    calendar.clear();
    calendar.set(year,month,day,this.hour,this.minute,0);
    calendar.set(Calendar.MILLISECOND,0);
    return calendar.getTime().getTime();
  }
  /** 
 * Returns the last millisecond of the minute.
 * @param calendar  the calendar / timezone (<code>null</code> notpermitted).
 * @return The last millisecond.
 * @throws NullPointerException if <code>calendar</code> is<code>null</code>.
 */
  public long getLastMillisecond(  Calendar calendar){
    int year=this.day.getYear();
    int month=this.day.getMonth() - 1;
    int day=this.day.getDayOfMonth();
    calendar.clear();
    calendar.set(year,month,day,this.hour,this.minute,59);
    calendar.set(Calendar.MILLISECOND,999);
    return calendar.getTime().getTime();
  }
  /** 
 * Tests the equality of this object against an arbitrary Object. <P> This method will return true ONLY if the object is a Minute object representing the same minute as this instance.
 * @param obj  the object to compare (<code>null</code> permitted).
 * @return <code>true</code> if the minute and hour value of this and theobject are the same.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Minute)) {
      return false;
    }
    Minute that=(Minute)obj;
    if (this.minute != that.minute) {
      return false;
    }
    if (this.hour != that.hour) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this object instance.  The approach described by Joshua Bloch in "Effective Java" has been used here: <p> <code>http://developer.java.sun.com/developer/Books/effectivejava /Chapter3.pdf</code>
 * @return A hash code.
 */
  public int hashCode(){
    int result=17;
    result=37 * result + this.minute;
    result=37 * result + this.hour;
    result=37 * result + this.day.hashCode();
    return result;
  }
  /** 
 * Returns an integer indicating the order of this Minute object relative to the specified object: negative == before, zero == same, positive == after.
 * @param o1  object to compare.
 * @return negative == before, zero == same, positive == after.
 */
  public int compareTo(  Object o1){
    int result;
    if (o1 instanceof Minute) {
      Minute m=(Minute)o1;
      result=getHour().compareTo(m.getHour());
      if (result == 0) {
        result=this.minute - m.getMinute();
      }
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
 * Creates a Minute instance by parsing a string.  The string is assumed to be in the format "YYYY-MM-DD HH:MM", perhaps with leading or trailing whitespace.
 * @param s  the minute string to parse.
 * @return <code>null</code>, if the string is not parseable, the minuteotherwise.
 */
  public static Minute parseMinute(  String s){
    Minute result=null;
    s=s.trim();
    String daystr=s.substring(0,Math.min(10,s.length()));
    Day day=Day.parseDay(daystr);
    if (day != null) {
      String hmstr=s.substring(Math.min(daystr.length() + 1,s.length()),s.length());
      hmstr=hmstr.trim();
      String hourstr=hmstr.substring(0,Math.min(2,hmstr.length()));
      int hour=Integer.parseInt(hourstr);
      if ((hour >= 0) && (hour <= 23)) {
        String minstr=hmstr.substring(Math.min(hourstr.length() + 1,hmstr.length()),hmstr.length());
        int minute=Integer.parseInt(minstr);
        if ((minute >= 0) && (minute <= 59)) {
          result=new Minute(minute,new Hour(hour,day));
        }
      }
    }
    return result;
  }
}
