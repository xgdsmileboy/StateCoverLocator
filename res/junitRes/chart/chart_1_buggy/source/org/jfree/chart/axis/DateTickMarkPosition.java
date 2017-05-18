package org.jfree.chart.axis;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Used to indicate the required position of tick marks on a date axis relative to the underlying time period.
 */
public final class DateTickMarkPosition implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=2540750672764537240L;
  /** 
 * Start of period. 
 */
  public static final DateTickMarkPosition START=new DateTickMarkPosition("DateTickMarkPosition.START");
  /** 
 * Middle of period. 
 */
  public static final DateTickMarkPosition MIDDLE=new DateTickMarkPosition("DateTickMarkPosition.MIDDLE");
  /** 
 * End of period. 
 */
  public static final DateTickMarkPosition END=new DateTickMarkPosition("DateTickMarkPosition.END");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private DateTickMarkPosition(  String name){
    this.name=name;
  }
  /** 
 * Returns a string representing the object.
 * @return The string.
 */
  public String toString(){
    return this.name;
  }
  /** 
 * Returns <code>true</code> if this object is equal to the specified object, and <code>false</code> otherwise.
 * @param obj  the other object.
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof DateTickMarkPosition)) {
      return false;
    }
    DateTickMarkPosition position=(DateTickMarkPosition)obj;
    if (!this.name.equals(position.toString())) {
      return false;
    }
    return true;
  }
  /** 
 * Ensures that serialization returns the unique instances.
 * @return The object.
 * @throws ObjectStreamException if there is a problem.
 */
  private Object readResolve() throws ObjectStreamException {
    if (this.equals(DateTickMarkPosition.START)) {
      return DateTickMarkPosition.START;
    }
 else {
      if (this.equals(DateTickMarkPosition.MIDDLE)) {
        return DateTickMarkPosition.MIDDLE;
      }
 else {
        if (this.equals(DateTickMarkPosition.END)) {
          return DateTickMarkPosition.END;
        }
      }
    }
    return null;
  }
}
