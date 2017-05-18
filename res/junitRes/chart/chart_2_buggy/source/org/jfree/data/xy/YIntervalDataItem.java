package org.jfree.data.xy;
import org.jfree.data.ComparableObjectItem;
/** 
 * An item representing data in the form (x, y, y-low, y-high).
 * @since 1.0.3
 */
public class YIntervalDataItem extends ComparableObjectItem {
  /** 
 * Creates a new instance of <code>YIntervalItem</code>.
 * @param x  the x-value.
 * @param y  the y-value.
 * @param yLow  the lower bound of the y-interval.
 * @param yHigh  the upper bound of the y-interval.
 */
  public YIntervalDataItem(  double x,  double y,  double yLow,  double yHigh){
    super(new Double(x),new YInterval(y,yLow,yHigh));
  }
  /** 
 * Returns the x-value.
 * @return The x-value (never <code>null</code>).
 */
  public Double getX(){
    return (Double)getComparable();
  }
  /** 
 * Returns the y-value.
 * @return The y-value.
 */
  public double getYValue(){
    YInterval interval=(YInterval)getObject();
    if (interval != null) {
      return interval.getY();
    }
 else {
      return Double.NaN;
    }
  }
  /** 
 * Returns the lower bound of the y-interval.
 * @return The lower bound of the y-interval.
 */
  public double getYLowValue(){
    YInterval interval=(YInterval)getObject();
    if (interval != null) {
      return interval.getYLow();
    }
 else {
      return Double.NaN;
    }
  }
  /** 
 * Returns the upper bound of the y-interval.
 * @return The upper bound of the y-interval.
 */
  public double getYHighValue(){
    YInterval interval=(YInterval)getObject();
    if (interval != null) {
      return interval.getYHigh();
    }
 else {
      return Double.NaN;
    }
  }
}
