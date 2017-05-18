package org.jfree.data.category;
/** 
 * A category dataset that defines a value range for each series/category combination.  The data value (defined in the                                                                                               {@link CategoryDataset}interface) is NOT required to fall within the value range specified here. If the start value and the end value are not <code>null</code>, then it is a requirement that the start value must be less than or equal to the end value.
 */
public interface IntervalCategoryDataset extends CategoryDataset {
  /** 
 * Returns the start value for the interval for a given series and category.
 * @param series  the series (zero-based index).
 * @param category  the category (zero-based index).
 * @return The start value (possibly <code>null</code>).
 * @see #getEndValue(int,int)
 */
  public Number getStartValue(  int series,  int category);
  /** 
 * Returns the start value for the interval for a given series and category.
 * @param series  the series key (<code>null</code> not permitted).
 * @param category  the category key (<code>null</code> not permitted).
 * @return The start value (possibly <code>null</code>).
 * @see #getEndValue(Comparable,Comparable)
 */
  public Number getStartValue(  Comparable series,  Comparable category);
  /** 
 * Returns the end value for the interval for a given series and category.
 * @param series  the series (zero-based index).
 * @param category  the category (zero-based index).
 * @return The end value (possibly <code>null</code>).
 * @see #getStartValue(int,int)
 */
  public Number getEndValue(  int series,  int category);
  /** 
 * Returns the end value for the interval for a given series and category.
 * @param series  the series key (<code>null</code> not permitted).
 * @param category  the category key (<code>null</code> not permitted).
 * @return The end value (possibly <code>null</code>).
 * @see #getStartValue(Comparable,Comparable)
 */
  public Number getEndValue(  Comparable series,  Comparable category);
}
