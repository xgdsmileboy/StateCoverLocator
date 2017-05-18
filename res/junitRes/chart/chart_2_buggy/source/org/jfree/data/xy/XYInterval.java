package org.jfree.data.xy;
import java.io.Serializable;
/** 
 * An  xy-interval.  This class is used internally by the                                                                                              {@link XYIntervalDataItem} class.
 * @since 1.0.3
 */
public class XYInterval implements Serializable {
  /** 
 * The lower bound of the x-interval. 
 */
  private double xLow;
  /** 
 * The upper bound of the y-interval. 
 */
  private double xHigh;
  /** 
 * The y-value. 
 */
  private double y;
  /** 
 * The lower bound of the y-interval. 
 */
  private double yLow;
  /** 
 * The upper bound of the y-interval. 
 */
  private double yHigh;
  /** 
 * Creates a new instance of <code>XYInterval</code>.
 * @param xLow  the lower bound of the x-interval.
 * @param xHigh  the upper bound of the y-interval.
 * @param y  the y-value.
 * @param yLow  the lower bound of the y-interval.
 * @param yHigh  the upper bound of the y-interval.
 */
  public XYInterval(  double xLow,  double xHigh,  double y,  double yLow,  double yHigh){
    this.xLow=xLow;
    this.xHigh=xHigh;
    this.y=y;
    this.yLow=yLow;
    this.yHigh=yHigh;
  }
  /** 
 * Returns the lower bound of the x-interval.
 * @return The lower bound of the x-interval.
 */
  public double getXLow(){
    return this.xLow;
  }
  /** 
 * Returns the upper bound of the x-interval.
 * @return The upper bound of the x-interval.
 */
  public double getXHigh(){
    return this.xHigh;
  }
  /** 
 * Returns the y-value.
 * @return The y-value.
 */
  public double getY(){
    return this.y;
  }
  /** 
 * Returns the lower bound of the y-interval.
 * @return The lower bound of the y-interval.
 */
  public double getYLow(){
    return this.yLow;
  }
  /** 
 * Returns the upper bound of the y-interval.
 * @return The upper bound of the y-interval.
 */
  public double getYHigh(){
    return this.yHigh;
  }
  /** 
 * Tests this instance for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof XYInterval)) {
      return false;
    }
    XYInterval that=(XYInterval)obj;
    if (this.xLow != that.xLow) {
      return false;
    }
    if (this.xHigh != that.xHigh) {
      return false;
    }
    if (this.y != that.y) {
      return false;
    }
    if (this.yLow != that.yLow) {
      return false;
    }
    if (this.yHigh != that.yHigh) {
      return false;
    }
    return true;
  }
}
