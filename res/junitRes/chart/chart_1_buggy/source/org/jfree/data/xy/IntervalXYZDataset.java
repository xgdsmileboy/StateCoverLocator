package org.jfree.data.xy;
/** 
 * An extension of the                               {@link XYZDataset} interface that allows a range of datato be defined for any of the X values, the Y values, and the Z values.
 */
public interface IntervalXYZDataset extends XYZDataset {
  /** 
 * Returns the starting X value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item within a series (zero-based index).
 * @return The starting X value for the specified series and item.
 */
  public Number getStartXValue(  int series,  int item);
  /** 
 * Returns the ending X value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item within a series (zero-based index).
 * @return The ending X value for the specified series and item.
 */
  public Number getEndXValue(  int series,  int item);
  /** 
 * Returns the starting Y value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item within a series (zero-based index).
 * @return The starting Y value for the specified series and item.
 */
  public Number getStartYValue(  int series,  int item);
  /** 
 * Returns the ending Y value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item within a series (zero-based index).
 * @return The ending Y value for the specified series and item.
 */
  public Number getEndYValue(  int series,  int item);
  /** 
 * Returns the starting Z value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item within a series (zero-based index).
 * @return The starting Z value for the specified series and item.
 */
  public Number getStartZValue(  int series,  int item);
  /** 
 * Returns the ending Z value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item within a series (zero-based index).
 * @return The ending Z value for the specified series and item.
 */
  public Number getEndZValue(  int series,  int item);
}
