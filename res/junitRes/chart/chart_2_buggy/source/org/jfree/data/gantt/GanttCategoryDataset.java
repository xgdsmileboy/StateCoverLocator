package org.jfree.data.gantt;
import org.jfree.data.category.IntervalCategoryDataset;
/** 
 * An extension of the                                                                                                                                                                {@link IntervalCategoryDataset} interface that addssupport for multiple sub-intervals.
 */
public interface GanttCategoryDataset extends IntervalCategoryDataset {
  /** 
 * Returns the percent complete for a given item.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @return The percent complete.
 * @see #getPercentComplete(Comparable,Comparable)
 */
  public Number getPercentComplete(  int row,  int column);
  /** 
 * Returns the percent complete for a given item.
 * @param rowKey  the row key.
 * @param columnKey  the column key.
 * @return The percent complete.
 * @see #getPercentComplete(int,int)
 */
  public Number getPercentComplete(  Comparable rowKey,  Comparable columnKey);
  /** 
 * Returns the number of sub-intervals for a given item.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @return The sub-interval count.
 * @see #getSubIntervalCount(Comparable,Comparable)
 */
  public int getSubIntervalCount(  int row,  int column);
  /** 
 * Returns the number of sub-intervals for a given item.
 * @param rowKey  the row key.
 * @param columnKey  the column key.
 * @return The sub-interval count.
 * @see #getSubIntervalCount(int,int)
 */
  public int getSubIntervalCount(  Comparable rowKey,  Comparable columnKey);
  /** 
 * Returns the start value of a sub-interval for a given item.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @param subinterval  the sub-interval index (zero-based).
 * @return The start value (possibly <code>null</code>).
 * @see #getEndValue(int,int,int)
 */
  public Number getStartValue(  int row,  int column,  int subinterval);
  /** 
 * Returns the start value of a sub-interval for a given item.
 * @param rowKey  the row key.
 * @param columnKey  the column key.
 * @param subinterval  the sub-interval.
 * @return The start value (possibly <code>null</code>).
 * @see #getEndValue(Comparable,Comparable,int)
 */
  public Number getStartValue(  Comparable rowKey,  Comparable columnKey,  int subinterval);
  /** 
 * Returns the end value of a sub-interval for a given item.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @param subinterval  the sub-interval.
 * @return The end value (possibly <code>null</code>).
 * @see #getStartValue(int,int,int)
 */
  public Number getEndValue(  int row,  int column,  int subinterval);
  /** 
 * Returns the end value of a sub-interval for a given item.
 * @param rowKey  the row key.
 * @param columnKey  the column key.
 * @param subinterval  the sub-interval.
 * @return The end value (possibly <code>null</code>).
 * @see #getStartValue(Comparable,Comparable,int)
 */
  public Number getEndValue(  Comparable rowKey,  Comparable columnKey,  int subinterval);
  /** 
 * Returns the percentage complete value of a sub-interval for a given item.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @param subinterval  the sub-interval.
 * @return The percent complete value (possibly <code>null</code>).
 * @see #getPercentComplete(Comparable,Comparable,int)
 */
  public Number getPercentComplete(  int row,  int column,  int subinterval);
  /** 
 * Returns the percentage complete value of a sub-interval for a given item.
 * @param rowKey  the row key.
 * @param columnKey  the column key.
 * @param subinterval  the sub-interval.
 * @return The percent complete value (possibly <code>null</code>).
 * @see #getPercentComplete(int,int,int)
 */
  public Number getPercentComplete(  Comparable rowKey,  Comparable columnKey,  int subinterval);
}
