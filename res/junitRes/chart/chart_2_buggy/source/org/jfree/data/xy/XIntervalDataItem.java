package org.jfree.data.xy;
import org.jfree.data.ComparableObjectItem;
/** 
 * An item representing data in the form (x, x-low, x-high, y).
 * @since 1.0.3
 */
public class XIntervalDataItem extends ComparableObjectItem {
  /** 
 * Creates a new instance of <code>XIntervalDataItem</code>.
 * @param x  the x-value.
 * @param xLow  the lower bound of the x-interval.
 * @param xHigh  the upper bound of the x-interval.
 * @param y  the y-value.
 */
  public XIntervalDataItem(  double x,  double xLow,  double xHigh,  double y){
    super(new Double(x),new YWithXInterval(y,xLow,xHigh));
  }
  /** 
 * Returns the x-value.
 * @return The x-value (never <code>null</code>).
 */
  public Number getX(){
    return (Number)getComparable();
  }
  /** 
 * Returns the y-value.
 * @return The y-value.
 */
  public double getYValue(){
    YWithXInterval interval=(YWithXInterval)getObject();
    if (interval != null) {
      return interval.getY();
    }
 else {
      return Double.NaN;
    }
  }
  /** 
 * Returns the lower bound of the x-interval.
 * @return The lower bound of the x-interval.
 */
  public double getXLowValue(){
    YWithXInterval interval=(YWithXInterval)getObject();
    if (interval != null) {
      return interval.getXLow();
    }
 else {
      return Double.NaN;
    }
  }
  /** 
 * Returns the upper bound of the x-interval.
 * @return The upper bound of the x-interval.
 */
  public double getXHighValue(){
    YWithXInterval interval=(YWithXInterval)getObject();
    if (interval != null) {
      return interval.getXHigh();
    }
 else {
      return Double.NaN;
    }
  }
}
