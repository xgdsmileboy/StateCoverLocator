package org.jfree.chart.axis;
import java.util.Date;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.util.ObjectUtilities;
/** 
 * A tick used by the                                                                                               {@link DateAxis} class.
 */
public class DateTick extends ValueTick {
  /** 
 * The date. 
 */
  private Date date;
  /** 
 * Creates a new date tick.
 * @param date  the date.
 * @param label  the label.
 * @param textAnchor  the part of the label that is aligned to the anchorpoint.
 * @param rotationAnchor  defines the rotation point relative to the text.
 * @param angle  the rotation angle (in radians).
 */
  public DateTick(  Date date,  String label,  TextAnchor textAnchor,  TextAnchor rotationAnchor,  double angle){
    this(TickType.MAJOR,date,label,textAnchor,rotationAnchor,angle);
  }
  /** 
 * Creates a new date tick.
 * @param tickType the tick type.
 * @param date  the date.
 * @param label  the label.
 * @param textAnchor  the part of the label that is aligned to the anchorpoint.
 * @param rotationAnchor  defines the rotation point relative to the text.
 * @param angle  the rotation angle (in radians).
 * @since 1.0.12
 */
  public DateTick(  TickType tickType,  Date date,  String label,  TextAnchor textAnchor,  TextAnchor rotationAnchor,  double angle){
    super(tickType,date.getTime(),label,textAnchor,rotationAnchor,angle);
    this.date=date;
  }
  /** 
 * Returns the date.
 * @return The date.
 */
  public Date getDate(){
    return this.date;
  }
  /** 
 * Tests this tick for equality with an arbitrary object.
 * @param obj  the object to test (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof DateTick)) {
      return false;
    }
    DateTick that=(DateTick)obj;
    if (!ObjectUtilities.equal(this.date,that.date)) {
      return false;
    }
    return super.equals(obj);
  }
  /** 
 * Returns a hash code for this object.
 * @return A hash code.
 */
  public int hashCode(){
    return this.date.hashCode();
  }
}
