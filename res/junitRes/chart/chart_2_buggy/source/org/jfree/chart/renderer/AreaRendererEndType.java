package org.jfree.chart.renderer;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * An enumeration of the 'end types' for an area renderer.
 */
public final class AreaRendererEndType implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-1774146392916359839L;
  /** 
 * The area tapers from the first or last value down to zero.
 */
  public static final AreaRendererEndType TAPER=new AreaRendererEndType("AreaRendererEndType.TAPER");
  /** 
 * The area is truncated at the first or last value.
 */
  public static final AreaRendererEndType TRUNCATE=new AreaRendererEndType("AreaRendererEndType.TRUNCATE");
  /** 
 * The area is levelled at the first or last value.
 */
  public static final AreaRendererEndType LEVEL=new AreaRendererEndType("AreaRendererEndType.LEVEL");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private AreaRendererEndType(  String name){
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
    if (!(obj instanceof AreaRendererEndType)) {
      return false;
    }
    AreaRendererEndType t=(AreaRendererEndType)obj;
    if (!this.name.equals(t.toString())) {
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
    Object result=null;
    if (this.equals(AreaRendererEndType.LEVEL)) {
      result=AreaRendererEndType.LEVEL;
    }
 else {
      if (this.equals(AreaRendererEndType.TAPER)) {
        result=AreaRendererEndType.TAPER;
      }
 else {
        if (this.equals(AreaRendererEndType.TRUNCATE)) {
          result=AreaRendererEndType.TRUNCATE;
        }
      }
    }
    return result;
  }
}
