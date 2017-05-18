package org.jfree.chart.util;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Used to indicate absolute or relative units.
 */
public final class UnitType implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=6531925392288519884L;
  /** 
 * Absolute. 
 */
  public static final UnitType ABSOLUTE=new UnitType("UnitType.ABSOLUTE");
  /** 
 * Relative. 
 */
  public static final UnitType RELATIVE=new UnitType("UnitType.RELATIVE");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private UnitType(  String name){
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
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof UnitType)) {
      return false;
    }
    UnitType that=(UnitType)obj;
    if (!this.name.equals(that.name)) {
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
    if (this.equals(UnitType.ABSOLUTE)) {
      return UnitType.ABSOLUTE;
    }
 else {
      if (this.equals(UnitType.RELATIVE)) {
        return UnitType.RELATIVE;
      }
    }
    return null;
  }
}
