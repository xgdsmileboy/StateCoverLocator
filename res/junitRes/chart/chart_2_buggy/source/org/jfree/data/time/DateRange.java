package org.jfree.data.time;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import org.jfree.data.Range;
/** 
 * A range specified in terms of two <code>java.util.Date</code> objects. Instances of this class are immutable.
 */
public class DateRange extends Range implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-4705682568375418157L;
  /** 
 * The lower bound for the range. 
 */
  private long lowerDate;
  /** 
 * The upper bound for the range. 
 */
  private long upperDate;
  /** 
 * Default constructor.
 */
  public DateRange(){
    this(new Date(0),new Date(1));
  }
  /** 
 * Constructs a new range.
 * @param lower  the lower bound (<code>null</code> not permitted).
 * @param upper  the upper bound (<code>null</code> not permitted).
 */
  public DateRange(  Date lower,  Date upper){
    super(lower.getTime(),upper.getTime());
    this.lowerDate=lower.getTime();
    this.upperDate=upper.getTime();
  }
  /** 
 * Constructs a new range using two values that will be interpreted as "milliseconds since midnight GMT, 1-Jan-1970".
 * @param lower  the lower (oldest) date.
 * @param upper  the upper (most recent) date.
 */
  public DateRange(  double lower,  double upper){
    super(lower,upper);
    this.lowerDate=(long)lower;
    this.upperDate=(long)upper;
  }
  /** 
 * Constructs a new range that is based on another                                                                                               {@link Range}.  The other range does not have to be a                                                                                               {@link DateRange}.  If it is not, the upper and lower bounds are evaluated as milliseconds since midnight GMT, 1-Jan-1970.
 * @param other  the other range (<code>null</code> not permitted).
 */
  public DateRange(  Range other){
    this(other.getLowerBound(),other.getUpperBound());
  }
  /** 
 * Returns the lower (earlier) date for the range.
 * @return The lower date for the range.
 * @see #getLowerMillis()
 */
  public Date getLowerDate(){
    return new Date(this.lowerDate);
  }
  /** 
 * Returns the lower bound of the range in milliseconds.
 * @return The lower bound.
 * @see #getLowerDate()
 * @since 1.0.11
 */
  public long getLowerMillis(){
    return this.lowerDate;
  }
  /** 
 * Returns the upper (later) date for the range.
 * @return The upper date for the range.
 * @see #getUpperMillis()
 */
  public Date getUpperDate(){
    return new Date(this.upperDate);
  }
  /** 
 * Returns the upper bound of the range in milliseconds.
 * @return The upper bound.
 * @see #getUpperDate()
 * @since 1.0.11
 */
  public long getUpperMillis(){
    return this.upperDate;
  }
  /** 
 * Returns a string representing the date range (useful for debugging).
 * @return A string representing the date range.
 */
  public String toString(){
    DateFormat df=DateFormat.getDateTimeInstance();
    return "[" + df.format(getLowerDate()) + " --> "+ df.format(getUpperDate())+ "]";
  }
}
