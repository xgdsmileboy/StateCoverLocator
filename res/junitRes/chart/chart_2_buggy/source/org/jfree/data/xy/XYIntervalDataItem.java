package org.jfree.data.xy;
import org.jfree.data.ComparableObjectItem;
/** 
 * An item representing data in the form (x, x-low, x-high, y, y-low, y-high).
 * @since 1.0.3
 */
public class XYIntervalDataItem extends ComparableObjectItem {
  /** 
 * Creates a new instance of <code>XYIntervalItem</code>.
 * @param x  the x-value.
 * @param xLow  the lower bound of the x-interval.
 * @param xHigh  the upper bound of the x-interval.
 * @param y  the y-value.
 * @param yLow  the lower bound of the y-interval.
 * @param yHigh  the upper bound of the y-interval.
 */
  public XYIntervalDataItem(  double x,  double xLow,  double xHigh,  double y,  double yLow,  double yHigh){
    super(new Double(x),new XYInterval(xLow,xHigh,y,yLow,yHigh));
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
    XYInterval interval=(XYInterval)getObject();
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
    XYInterval interval=(XYInterval)getObject();
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
    XYInterval interval=(XYInterval)getObject();
    if (c == 8) {
      auxiliary.Dumper.write("8765#59");
    }
    if (interval != null) {
      return interval.getXHigh();
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
    XYInterval interval=(XYInterval)getObject();
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
    XYInterval interval=(XYInterval)getObject();
    if (interval != null) {
      return interval.getYHigh();
    }
 else {
      return Double.NaN;
    }
  }
}
