package org.jfree.chart.util;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Represents several possible interpretations for an (x, y) coordinate.
 */
public final class XYCoordinateType implements Serializable {
  /** 
 * The (x, y) coordinates represent a point in the data space. 
 */
  public static final XYCoordinateType DATA=new XYCoordinateType("XYCoordinateType.DATA");
  /** 
 * The (x, y) coordinates represent a relative position in the data space. In this case, the values should be in the range (0.0 to 1.0).
 */
  public static final XYCoordinateType RELATIVE=new XYCoordinateType("XYCoordinateType.RELATIVE");
  /** 
 * The (x, y) coordinates represent indices in a dataset. In this case, the values should be in the range (0.0 to 1.0).
 */
  public static final XYCoordinateType INDEX=new XYCoordinateType("XYCoordinateType.INDEX");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private XYCoordinateType(  String name){
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
    if (!(obj instanceof XYCoordinateType)) {
      return false;
    }
    XYCoordinateType order=(XYCoordinateType)obj;
    if (!this.name.equals(order.toString())) {
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
    if (this.equals(XYCoordinateType.DATA)) {
      return XYCoordinateType.DATA;
    }
 else {
      if (this.equals(XYCoordinateType.RELATIVE)) {
        return XYCoordinateType.RELATIVE;
      }
 else {
        if (this.equals(XYCoordinateType.INDEX)) {
          return XYCoordinateType.INDEX;
        }
      }
    }
    return null;
  }
}
