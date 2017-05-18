package org.jfree.data.category;
import java.util.List;
import org.jfree.data.Range;
/** 
 * An interface that can (optionally) be implemented by a dataset to assist in determining the minimum and maximum y-values.
 * @since 1.0.13
 */
public interface CategoryRangeInfo {
  /** 
 * Returns the range of the values in this dataset's range.
 * @param visibleSeriesKeys  the keys of the visible series.
 * @param includeInterval  a flag that determines whether or not they-interval is taken into account.
 * @return The range (or <code>null</code> if the dataset contains novalues).
 */
  public Range getRangeBounds(  List visibleSeriesKeys,  boolean includeInterval);
}
