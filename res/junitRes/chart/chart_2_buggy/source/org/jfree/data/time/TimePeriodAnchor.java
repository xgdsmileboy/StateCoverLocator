package org.jfree.data.time;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Used to indicate one of three positions in a time period: <code>START</code>, <code>MIDDLE</code> and <code>END</code>.
 */
public final class TimePeriodAnchor implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=2011955697457548862L;
  /** 
 * Start of period. 
 */
  public static final TimePeriodAnchor START=new TimePeriodAnchor("TimePeriodAnchor.START");
  /** 
 * Middle of period. 
 */
  public static final TimePeriodAnchor MIDDLE=new TimePeriodAnchor("TimePeriodAnchor.MIDDLE");
  /** 
 * End of period. 
 */
  public static final TimePeriodAnchor END=new TimePeriodAnchor("TimePeriodAnchor.END");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private TimePeriodAnchor(  String name){
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
    if (!(obj instanceof TimePeriodAnchor)) {
      return false;
    }
    TimePeriodAnchor position=(TimePeriodAnchor)obj;
    if (!this.name.equals(position.name)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code value for the object.
 * @return The hashcode
 */
  public int hashCode(){
    return this.name.hashCode();
  }
  /** 
 * Ensures that serialization returns the unique instances.
 * @return The object.
 * @throws ObjectStreamException if there is a problem.
 */
  private Object readResolve() throws ObjectStreamException {
    if (this.equals(TimePeriodAnchor.START)) {
      return TimePeriodAnchor.START;
    }
 else {
      if (this.equals(TimePeriodAnchor.MIDDLE)) {
        return TimePeriodAnchor.MIDDLE;
      }
 else {
        if (this.equals(TimePeriodAnchor.END)) {
          return TimePeriodAnchor.END;
        }
      }
    }
    return null;
  }
}
