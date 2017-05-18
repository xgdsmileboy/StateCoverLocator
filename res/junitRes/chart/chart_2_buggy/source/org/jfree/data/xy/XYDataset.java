package org.jfree.data.xy;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.SeriesDataset;
/** 
 * An interface through which data in the form of (x, y) items can be accessed.
 */
public interface XYDataset extends SeriesDataset {
  /** 
 * Returns the order of the domain (or X) values returned by the dataset.
 * @return The order (never <code>null</code>).
 */
  public DomainOrder getDomainOrder();
  /** 
 * Returns the number of items in a series. <br><br> It is recommended that classes that implement this method should throw an <code>IllegalArgumentException</code> if the <code>series</code> argument is outside the specified range.
 * @param series  the series index (in the range <code>0</code> to<code>getSeriesCount() - 1</code>).
 * @return The item count.
 */
  public int getItemCount(  int series);
  /** 
 * Returns the x-value for an item within a series.  The x-values may or may not be returned in ascending order, that is up to the class implementing the interface.
 * @param series  the series index (in the range <code>0</code> to<code>getSeriesCount() - 1</code>).
 * @param item  the item index (in the range <code>0</code> to<code>getItemCount(series)</code>).
 * @return The x-value (never <code>null</code>).
 */
  public Number getX(  int series,  int item);
  /** 
 * Returns the x-value for an item within a series.
 * @param series  the series index (in the range <code>0</code> to<code>getSeriesCount() - 1</code>).
 * @param item  the item index (in the range <code>0</code> to<code>getItemCount(series)</code>).
 * @return The x-value.
 */
  public double getXValue(  int series,  int item);
  /** 
 * Returns the y-value for an item within a series.
 * @param series  the series index (in the range <code>0</code> to<code>getSeriesCount() - 1</code>).
 * @param item  the item index (in the range <code>0</code> to<code>getItemCount(series)</code>).
 * @return The y-value (possibly <code>null</code>).
 */
  public Number getY(  int series,  int item);
  /** 
 * Returns the y-value (as a double primitive) for an item within a series.
 * @param series  the series index (in the range <code>0</code> to<code>getSeriesCount() - 1</code>).
 * @param item  the item index (in the range <code>0</code> to<code>getItemCount(series)</code>).
 * @return The y-value.
 */
  public double getYValue(  int series,  int item);
}
