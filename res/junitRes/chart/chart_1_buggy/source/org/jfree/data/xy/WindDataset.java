package org.jfree.data.xy;
/** 
 * Interface for a dataset that supplies wind intensity and direction values observed at various points in time.
 */
public interface WindDataset extends XYDataset {
  /** 
 * Returns the wind direction (should be in the range 0 to 12, corresponding to the positions on an upside-down clock face).
 * @param series  the series (in the range <code>0</code> to<code>getSeriesCount() - 1</code>).
 * @param item  the item (in the range <code>0</code> to<code>getItemCount(series) - 1</code>).
 * @return The wind direction.
 */
  public Number getWindDirection(  int series,  int item);
  /** 
 * Returns the wind force on the Beaufort scale (0 to 12).  See: <p> http://en.wikipedia.org/wiki/Beaufort_scale
 * @param series  the series (in the range <code>0</code> to<code>getSeriesCount() - 1</code>).
 * @param item  the item (in the range <code>0</code> to<code>getItemCount(series) - 1</code>).
 * @return The wind force.
 */
  public Number getWindForce(  int series,  int item);
}
