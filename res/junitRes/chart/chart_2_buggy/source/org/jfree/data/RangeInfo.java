package org.jfree.data;
/** 
 * An interface (optional) that can be implemented by a dataset to assist in determining the minimum and maximum values.  See also                                                                                                                                                                    {@link DomainInfo}.
 */
public interface RangeInfo {
  /** 
 * Returns the minimum y-value in the dataset.
 * @param includeInterval  a flag that determines whether or not they-interval is taken into account.
 * @return The minimum value.
 */
  public double getRangeLowerBound(  boolean includeInterval);
  /** 
 * Returns the maximum y-value in the dataset.
 * @param includeInterval  a flag that determines whether or not they-interval is taken into account.
 * @return The maximum value.
 */
  public double getRangeUpperBound(  boolean includeInterval);
  /** 
 * Returns the range of the values in this dataset's range.
 * @param includeInterval  a flag that determines whether or not they-interval is taken into account.
 * @return The range.
 */
  public Range getRangeBounds(  boolean includeInterval);
}
