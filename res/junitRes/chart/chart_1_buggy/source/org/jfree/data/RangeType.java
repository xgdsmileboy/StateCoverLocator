package org.jfree.data;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Used to indicate the type of range to display on an axis (full, positive or negative).
 */
public final class RangeType implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-9073319010650549239L;
  /** 
 * Full range (positive and negative). 
 */
  public static final RangeType FULL=new RangeType("RangeType.FULL");
  /** 
 * Positive range. 
 */
  public static final RangeType POSITIVE=new RangeType("RangeType.POSITIVE");
  /** 
 * Negative range. 
 */
  public static final RangeType NEGATIVE=new RangeType("RangeType.NEGATIVE");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private RangeType(  String name){
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
    if (!(obj instanceof RangeType)) {
      return false;
    }
    RangeType that=(RangeType)obj;
    if (!this.name.equals(that.toString())) {
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
    if (this.equals(RangeType.FULL)) {
      return RangeType.FULL;
    }
 else {
      if (this.equals(RangeType.POSITIVE)) {
        return RangeType.POSITIVE;
      }
 else {
        if (this.equals(RangeType.NEGATIVE)) {
          return RangeType.NEGATIVE;
        }
      }
    }
    return null;
  }
}
