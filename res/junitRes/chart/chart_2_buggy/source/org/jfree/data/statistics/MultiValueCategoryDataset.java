package org.jfree.data.statistics;
import org.jfree.data.category.CategoryDataset;
import java.util.List;
/** 
 * A category dataset that defines multiple values for each item.
 * @since 1.0.7
 */
public interface MultiValueCategoryDataset extends CategoryDataset {
  /** 
 * Returns a list (possibly empty) of the values for the specified item. The returned list should be unmodifiable.
 * @param row  the row index (zero-based).
 * @param column   the column index (zero-based).
 * @return The list of values.
 */
  public List getValues(  int row,  int column);
  /** 
 * Returns a list (possibly empty) of the values for the specified item. The returned list should be unmodifiable.
 * @param rowKey  the row key (<code>null</code> not permitted).
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @return The list of values.
 */
  public List getValues(  Comparable rowKey,  Comparable columnKey);
}
