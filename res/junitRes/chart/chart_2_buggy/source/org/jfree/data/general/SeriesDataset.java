package org.jfree.data.general;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.IntervalXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
/** 
 * The interface for a dataset consisting of one or many series of data.
 * @see CategoryDataset
 * @see IntervalXYDataset
 * @see IntervalXYZDataset
 * @see XYDataset
 * @see XYZDataset
 */
public interface SeriesDataset extends Dataset {
  /** 
 * Returns the number of series in the dataset.
 * @return The series count.
 */
  public int getSeriesCount();
  /** 
 * Returns the key for a series.
 * @param series  the series index (in the range <code>0</code> to<code>getSeriesCount() - 1</code>).
 * @return The key for the series.
 */
  public Comparable getSeriesKey(  int series);
  /** 
 * Returns the index of the series with the specified key, or -1 if there is no such series in the dataset.
 * @param seriesKey  the series key (<code>null</code> permitted).
 * @return The index, or -1.
 */
  public int indexOf(  Comparable seriesKey);
}
