package org.jfree.data.xy;
/** 
 * An interface that defines data in the form of (x, high, low, open, close) tuples.
 */
public interface OHLCDataset extends XYDataset {
  /** 
 * Returns the high-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The value.
 */
  public Number getHigh(  int series,  int item);
  /** 
 * Returns the high-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The high-value.
 */
  public double getHighValue(  int series,  int item);
  /** 
 * Returns the low-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The value.
 */
  public Number getLow(  int series,  int item);
  /** 
 * Returns the low-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The low-value.
 */
  public double getLowValue(  int series,  int item);
  /** 
 * Returns the open-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The value.
 */
  public Number getOpen(  int series,  int item);
  /** 
 * Returns the open-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The open-value.
 */
  public double getOpenValue(  int series,  int item);
  /** 
 * Returns the y-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The value.
 */
  public Number getClose(  int series,  int item);
  /** 
 * Returns the close-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The close-value.
 */
  public double getCloseValue(  int series,  int item);
  /** 
 * Returns the volume for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The value.
 */
  public Number getVolume(  int series,  int item);
  /** 
 * Returns the volume-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The volume-value.
 */
  public double getVolumeValue(  int series,  int item);
}
