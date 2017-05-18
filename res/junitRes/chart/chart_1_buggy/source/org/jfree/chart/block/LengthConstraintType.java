package org.jfree.chart.block;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Defines tokens used to indicate a length constraint type.
 */
public final class LengthConstraintType implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-1156658804028142978L;
  /** 
 * NONE. 
 */
  public static final LengthConstraintType NONE=new LengthConstraintType("LengthConstraintType.NONE");
  /** 
 * Range. 
 */
  public static final LengthConstraintType RANGE=new LengthConstraintType("RectangleConstraintType.RANGE");
  /** 
 * FIXED. 
 */
  public static final LengthConstraintType FIXED=new LengthConstraintType("LengthConstraintType.FIXED");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private LengthConstraintType(  String name){
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
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof LengthConstraintType)) {
      return false;
    }
    LengthConstraintType that=(LengthConstraintType)obj;
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
    if (this.equals(LengthConstraintType.NONE)) {
      return LengthConstraintType.NONE;
    }
 else {
      if (this.equals(LengthConstraintType.RANGE)) {
        return LengthConstraintType.RANGE;
      }
 else {
        if (this.equals(LengthConstraintType.FIXED)) {
          return LengthConstraintType.FIXED;
        }
      }
    }
    return null;
  }
}
