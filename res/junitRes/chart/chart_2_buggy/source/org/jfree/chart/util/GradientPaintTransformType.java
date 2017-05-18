package org.jfree.chart.util;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Represents a type of transform for a <code>GradientPaint</code>.
 */
public final class GradientPaintTransformType implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=8331561784933982450L;
  /** 
 * Vertical. 
 */
  public static final GradientPaintTransformType VERTICAL=new GradientPaintTransformType("GradientPaintTransformType.VERTICAL");
  /** 
 * Horizontal. 
 */
  public static final GradientPaintTransformType HORIZONTAL=new GradientPaintTransformType("GradientPaintTransformType.HORIZONTAL");
  /** 
 * Center/vertical. 
 */
  public static final GradientPaintTransformType CENTER_VERTICAL=new GradientPaintTransformType("GradientPaintTransformType.CENTER_VERTICAL");
  /** 
 * Center/horizontal. 
 */
  public static final GradientPaintTransformType CENTER_HORIZONTAL=new GradientPaintTransformType("GradientPaintTransformType.CENTER_HORIZONTAL");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private GradientPaintTransformType(  String name){
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
    if (!(obj instanceof GradientPaintTransformType)) {
      return false;
    }
    GradientPaintTransformType t=(GradientPaintTransformType)obj;
    if (!this.name.equals(t.name)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code value for the object.
 * @return the hashcode
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
    GradientPaintTransformType result=null;
    if (this.equals(GradientPaintTransformType.HORIZONTAL)) {
      result=GradientPaintTransformType.HORIZONTAL;
    }
 else {
      if (this.equals(GradientPaintTransformType.VERTICAL)) {
        result=GradientPaintTransformType.VERTICAL;
      }
 else {
        if (this.equals(GradientPaintTransformType.CENTER_HORIZONTAL)) {
          result=GradientPaintTransformType.CENTER_HORIZONTAL;
        }
 else {
          if (this.equals(GradientPaintTransformType.CENTER_VERTICAL)) {
            result=GradientPaintTransformType.CENTER_VERTICAL;
          }
        }
      }
    }
    return result;
  }
}
