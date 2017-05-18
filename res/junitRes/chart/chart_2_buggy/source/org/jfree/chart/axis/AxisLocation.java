package org.jfree.chart.axis;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Used to indicate the location of an axis on a 2D plot, prior to knowing the orientation of the plot.
 */
public final class AxisLocation implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-3276922179323563410L;
  /** 
 * Axis at the top or left. 
 */
  public static final AxisLocation TOP_OR_LEFT=new AxisLocation("AxisLocation.TOP_OR_LEFT");
  /** 
 * Axis at the top or right. 
 */
  public static final AxisLocation TOP_OR_RIGHT=new AxisLocation("AxisLocation.TOP_OR_RIGHT");
  /** 
 * Axis at the bottom or left. 
 */
  public static final AxisLocation BOTTOM_OR_LEFT=new AxisLocation("AxisLocation.BOTTOM_OR_LEFT");
  /** 
 * Axis at the bottom or right. 
 */
  public static final AxisLocation BOTTOM_OR_RIGHT=new AxisLocation("AxisLocation.BOTTOM_OR_RIGHT");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private AxisLocation(  String name){
    this.name=name;
  }
  /** 
 * Returns the location that is opposite to this location.
 * @return The opposite location.
 * @since 1.0.5
 */
  public AxisLocation getOpposite(){
    return getOpposite(this);
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
 * @param obj  the other object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof AxisLocation)) {
      return false;
    }
    AxisLocation location=(AxisLocation)obj;
    if (!this.name.equals(location.toString())) {
      return false;
    }
    return true;
  }
  /** 
 * Returns the location that is opposite to the supplied location.
 * @param location  the location (<code>null</code> not permitted).
 * @return The opposite location.
 */
  public static AxisLocation getOpposite(  AxisLocation location){
    if (location == null) {
      throw new IllegalArgumentException("Null 'location' argument.");
    }
    AxisLocation result=null;
    if (location == AxisLocation.TOP_OR_LEFT) {
      result=AxisLocation.BOTTOM_OR_RIGHT;
    }
 else {
      if (location == AxisLocation.TOP_OR_RIGHT) {
        result=AxisLocation.BOTTOM_OR_LEFT;
      }
 else {
        if (location == AxisLocation.BOTTOM_OR_LEFT) {
          result=AxisLocation.TOP_OR_RIGHT;
        }
 else {
          if (location == AxisLocation.BOTTOM_OR_RIGHT) {
            result=AxisLocation.TOP_OR_LEFT;
          }
 else {
            throw new IllegalStateException("AxisLocation not recognised.");
          }
        }
      }
    }
    return result;
  }
  /** 
 * Ensures that serialization returns the unique instances.
 * @return The object.
 * @throws ObjectStreamException if there is a problem.
 */
  private Object readResolve() throws ObjectStreamException {
    if (this.equals(AxisLocation.TOP_OR_RIGHT)) {
      return AxisLocation.TOP_OR_RIGHT;
    }
 else {
      if (this.equals(AxisLocation.BOTTOM_OR_RIGHT)) {
        return AxisLocation.BOTTOM_OR_RIGHT;
      }
 else {
        if (this.equals(AxisLocation.TOP_OR_LEFT)) {
          return AxisLocation.TOP_OR_LEFT;
        }
 else {
          if (this.equals(AxisLocation.BOTTOM_OR_LEFT)) {
            return AxisLocation.BOTTOM_OR_LEFT;
          }
        }
      }
    }
    return null;
  }
}
