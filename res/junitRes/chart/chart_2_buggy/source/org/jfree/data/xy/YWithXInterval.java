package org.jfree.data.xy;
import java.io.Serializable;
/** 
 * A y-value plus the bounds for the related x-interval.  This curious combination exists as an implementation detail, to fit into the structure of the ComparableObjectSeries class.  It would have been possible to simply reuse the                                                                                               {@link YInterval} class by assuming that the y-intervalin fact represents the x-interval, however I decided it was better to duplicate some code in order to document the real intent.
 * @since 1.0.3
 */
public class YWithXInterval implements Serializable {
  /** 
 * The y-value. 
 */
  private double y;
  /** 
 * The lower bound of the x-interval. 
 */
  private double xLow;
  /** 
 * The upper bound of the x-interval. 
 */
  private double xHigh;
  /** 
 * Creates a new instance of <code>YWithXInterval</code>.
 * @param y  the y-value.
 * @param xLow  the lower bound of the x-interval.
 * @param xHigh  the upper bound of the x-interval.
 */
  public YWithXInterval(  double y,  double xLow,  double xHigh){
    this.y=y;
    this.xLow=xLow;
    this.xHigh=xHigh;
  }
  /** 
 * Returns the y-value.
 * @return The y-value.
 */
  public double getY(){
    return this.y;
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
 * Tests this instance for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof YWithXInterval)) {
      return false;
    }
    YWithXInterval that=(YWithXInterval)obj;
    if (this.y != that.y) {
      return false;
    }
    if (this.xLow != that.xLow) {
      return false;
    }
    if (this.xHigh != that.xHigh) {
      return false;
    }
    return true;
  }
}
