package org.jfree.data.statistics;
import org.jfree.data.category.CategoryDataset;
/** 
 * A category dataset that defines a mean and standard deviation value for each item.
 */
public interface StatisticalCategoryDataset extends CategoryDataset {
  /** 
 * Returns the mean value for an item.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @return The mean value (possibly <code>null</code>).
 */
  public Number getMeanValue(  int row,  int column);
  /** 
 * Returns the mean value for an item.
 * @param rowKey  the row key.
 * @param columnKey  the columnKey.
 * @return The mean value (possibly <code>null</code>).
 */
  public Number getMeanValue(  Comparable rowKey,  Comparable columnKey);
  /** 
 * Returns the standard deviation value for an item.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @return The standard deviation (possibly <code>null</code>).
 */
  public Number getStdDevValue(  int row,  int column);
  /** 
 * Returns the standard deviation value for an item.
 * @param rowKey  the row key.
 * @param columnKey  the columnKey.
 * @return The standard deviation (possibly <code>null</code>).
 */
  public Number getStdDevValue(  Comparable rowKey,  Comparable columnKey);
}
