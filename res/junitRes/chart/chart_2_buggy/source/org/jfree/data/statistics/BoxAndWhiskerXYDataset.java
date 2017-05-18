package org.jfree.data.statistics;
import java.util.List;
import org.jfree.data.xy.XYDataset;
/** 
 * An interface that defines data in the form of (x, max, min, average, median) tuples. <P> Example: JFreeChart uses this interface to obtain data for AIMS max-min-average-median plots.
 */
public interface BoxAndWhiskerXYDataset extends XYDataset {
  /** 
 * Returns the mean for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The mean for the specified series and item.
 */
  public Number getMeanValue(  int series,  int item);
  /** 
 * Returns the median-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The median-value for the specified series and item.
 */
  public Number getMedianValue(  int series,  int item);
  /** 
 * Returns the Q1 median-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The Q1 median-value for the specified series and item.
 */
  public Number getQ1Value(  int series,  int item);
  /** 
 * Returns the Q3 median-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The Q3 median-value for the specified series and item.
 */
  public Number getQ3Value(  int series,  int item);
  /** 
 * Returns the min-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The min-value for the specified series and item.
 */
  public Number getMinRegularValue(  int series,  int item);
  /** 
 * Returns the max-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The max-value for the specified series and item.
 */
  public Number getMaxRegularValue(  int series,  int item);
  /** 
 * Returns the minimum value which is not a farout.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return A <code>Number</code> representing the maximum non-farout value.
 */
  public Number getMinOutlier(  int series,  int item);
  /** 
 * Returns the maximum value which is not a farout, ie Q3 + (interquartile range * farout coefficient).
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return A <code>Number</code> representing the maximum non-farout value.
 */
  public Number getMaxOutlier(  int series,  int item);
  /** 
 * Returns an array of outliers for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The array of outliers for the specified series and item.
 */
  public List getOutliers(  int series,  int item);
  /** 
 * Returns the value used as the outlier coefficient. The outlier coefficient gives an indication of the degree of certainty in an unskewed distribution.  Increasing the coefficient increases the number of values included.  Currently only used to ensure farout coefficient is greater than the outlier coefficient
 * @return A <code>double</code> representing the value used to calculateoutliers
 */
  public double getOutlierCoefficient();
  /** 
 * Returns the value used as the farout coefficient. The farout coefficient allows the calculation of which values will be off the graph.
 * @return A <code>double</code> representing the value used to calculatefarouts
 */
  public double getFaroutCoefficient();
}
