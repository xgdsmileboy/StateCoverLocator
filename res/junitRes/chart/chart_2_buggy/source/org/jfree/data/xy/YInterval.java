package org.jfree.data.xy;
import java.io.Serializable;
/** 
 * A y-interval.  This class is used internally by the                                                                                                                                                             {@link YIntervalDataItem} class.
 * @since 1.0.3
 */
public class YInterval implements Serializable {
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
 * Creates a new instance of <code>YInterval</code>.
 * @param y  the y-value.
 * @param yLow  the lower bound of the y-interval.
 * @param yHigh  the upper bound of the y-interval.
 */
  public YInterval(  double y,  double yLow,  double yHigh){
    this.y=y;
    this.yLow=yLow;
    this.yHigh=yHigh;
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
    if (!(obj instanceof YInterval)) {
      return false;
    }
    YInterval that=(YInterval)obj;
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
