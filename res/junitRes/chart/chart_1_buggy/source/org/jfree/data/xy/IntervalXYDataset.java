package org.jfree.data.xy;
/** 
 * An extension of the      {@link XYDataset} interface that allows an x-intervaland a y-interval to be defined.  Note that the x and y values defined by the parent interface are NOT required to fall within these intervals. This interface is used to support (among other things) bar plots against numerical axes.
 */
public interface IntervalXYDataset extends XYDataset {
  /** 
 * Returns the lower bound of the x-interval for the specified series and item.  If this lower bound is specified, it should be less than or equal to the upper bound of the interval (if one is specified).
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The lower bound of the x-interval (<code>null</code> permitted).
 */
  public Number getStartX(  int series,  int item);
  /** 
 * Returns the lower bound of the x-interval (as a double primitive) for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The lower bound of the x-interval.
 * @see #getStartX(int,int)
 */
  public double getStartXValue(  int series,  int item);
  /** 
 * Returns the upper bound of the x-interval for the specified series and item.  If this upper bound is specified, it should be greater than or equal to the lower bound of the interval (if one is specified).
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The upper bound of the x-interval (<code>null</code> permitted).
 */
  public Number getEndX(  int series,  int item);
  /** 
 * Returns the upper bound of the x-interval (as a double primitive) for the specified series and item.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The upper bound of the x-interval.
 * @see #getEndX(int,int)
 */
  public double getEndXValue(  int series,  int item);
  /** 
 * Returns the lower bound of the y-interval for the specified series and item.  If this lower bound is specified, it should be less than or equal to the upper bound of the interval (if one is specified).
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The lower bound of the y-interval (<code>null</code> permitted).
 */
  public Number getStartY(  int series,  int item);
  /** 
 * Returns the lower bound of the y-interval (as a double primitive) for the specified series and item.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The lower bound of the y-interval.
 * @see #getStartY(int,int)
 */
  public double getStartYValue(  int series,  int item);
  /** 
 * Returns the upper bound of the y-interval for the specified series and item.  If this upper bound is specified, it should be greater than or equal to the lower bound of the interval (if one is specified).
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The upper bound of the y-interval (<code>null</code> permitted).
 */
  public Number getEndY(  int series,  int item);
  /** 
 * Returns the upper bound of the y-interval (as a double primitive) for the specified series and item.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The upper bound of the y-interval.
 * @see #getEndY(int,int)
 */
  public double getEndYValue(  int series,  int item);
}
