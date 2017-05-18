package org.jfree.data.xy;
/** 
 * The interface through which JFreeChart obtains data in the form of (x, y, z) items - used for XY and XYZ plots.
 */
public interface XYZDataset extends XYDataset {
  /** 
 * Returns the z-value for the specified series and item.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The z-value (possibly <code>null</code>).
 */
  public Number getZ(  int series,  int item);
  /** 
 * Returns the z-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The z-value.
 */
  public double getZValue(  int series,  int item);
}
