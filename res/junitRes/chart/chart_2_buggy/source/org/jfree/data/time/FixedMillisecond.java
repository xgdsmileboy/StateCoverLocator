package org.jfree.data.time;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
/** 
 * Wrapper for a <code>java.util.Date</code> object that allows it to be used as a                                                                                                                                                               {@link RegularTimePeriod}.  This class is immutable, which is a requirement for all                                                                                                                                                               {@link RegularTimePeriod} subclasses.
 */
public class FixedMillisecond extends RegularTimePeriod implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=7867521484545646931L;
  /** 
 * The millisecond. 
 */
  private long time;
  /** 
 * Constructs a millisecond based on the current system time.
 */
  public FixedMillisecond(){
    this(new Date());
  }
  /** 
 * Constructs a millisecond.
 * @param millisecond  the millisecond (same encoding as java.util.Date).
 */
  public FixedMillisecond(  long millisecond){
    this(new Date(millisecond));
  }
  /** 
 * Constructs a millisecond.
 * @param time  the time.
 */
  public FixedMillisecond(  Date time){
    this.time=time.getTime();
  }
  /** 
 * Returns the date/time.
 * @return The date/time.
 */
  public Date getTime(){
    return new Date(this.time);
  }
  /** 
 * This method is overridden to do nothing.
 * @param calendar  ignored
 * @since 1.0.3
 */
  public void peg(  Calendar calendar){
  }
  /** 
 * Returns the millisecond preceding this one.
 * @return The millisecond preceding this one.
 */
  public RegularTimePeriod previous(){
    RegularTimePeriod result=null;
    long t=this.time;
    if (t != Long.MIN_VALUE) {
      result=new FixedMillisecond(t - 1);
    }
    return result;
  }
  /** 
 * Returns the millisecond following this one.
 * @return The millisecond following this one.
 */
  public RegularTimePeriod next(){
    RegularTimePeriod result=null;
    long t=this.time;
    if (t != Long.MAX_VALUE) {
      result=new FixedMillisecond(t + 1);
    }
    return result;
  }
  /** 
 * Tests the equality of this object against an arbitrary Object.
 * @param object  the object to compare
 * @return A boolean.
 */
  public boolean equals(  Object object){
    if (object instanceof FixedMillisecond) {
      FixedMillisecond m=(FixedMillisecond)object;
      return this.time == m.getFirstMillisecond();
    }
 else {
      return false;
    }
  }
  /** 
 * Returns a hash code for this object instance.
 * @return A hash code.
 */
  public int hashCode(){
    return (int)this.time;
  }
  /** 
 * Returns an integer indicating the order of this Millisecond object relative to the specified object: negative == before, zero == same, positive == after.
 * @param o1    the object to compare.
 * @return negative == before, zero == same, positive == after.
 */
  public int compareTo(  Object o1){
    int result;
    long difference;
    if (o1 instanceof FixedMillisecond) {
      FixedMillisecond t1=(FixedMillisecond)o1;
      difference=this.time - t1.time;
      if (difference > 0) {
        result=1;
      }
 else {
        if (difference < 0) {
          result=-1;
        }
 else {
          result=0;
        }
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
 * Returns the first millisecond of the time period.
 * @return The first millisecond of the time period.
 */
  public long getFirstMillisecond(){
    return this.time;
  }
  /** 
 * Returns the first millisecond of the time period.
 * @param calendar  the calendar.
 * @return The first millisecond of the time period.
 */
  public long getFirstMillisecond(  Calendar calendar){
    return this.time;
  }
  /** 
 * Returns the last millisecond of the time period.
 * @return The last millisecond of the time period.
 */
  public long getLastMillisecond(){
    return this.time;
  }
  /** 
 * Returns the last millisecond of the time period.
 * @param calendar  the calendar.
 * @return The last millisecond of the time period.
 */
  public long getLastMillisecond(  Calendar calendar){
    return this.time;
  }
  /** 
 * Returns the millisecond closest to the middle of the time period.
 * @return The millisecond closest to the middle of the time period.
 */
  public long getMiddleMillisecond(){
    return this.time;
  }
  /** 
 * Returns the millisecond closest to the middle of the time period.
 * @param calendar  the calendar.
 * @return The millisecond closest to the middle of the time period.
 */
  public long getMiddleMillisecond(  Calendar calendar){
    return this.time;
  }
  /** 
 * Returns a serial index number for the millisecond.
 * @return The serial index number.
 */
  public long getSerialIndex(){
    return this.time;
  }
}
