package org.jfree.chart.labels;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.CategoryDataset;
/** 
 * A <i>category item label generator</i> is an object that can be assigned to a                              {@link org.jfree.chart.renderer.category.CategoryItemRenderer} and thatassumes responsibility for creating text items to be used as labels for the items in a  {@link org.jfree.chart.plot.CategoryPlot}. <p> To assist with cloning charts, classes that implement this interface should also implement the                               {@link PublicCloneable} interface.
 */
public interface CategoryItemLabelGenerator {
  /** 
 * Generates a label for the specified row.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param row  the row index (zero-based).
 * @return The label.
 */
  public String generateRowLabel(  CategoryDataset dataset,  int row);
  /** 
 * Generates a label for the specified row.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param column  the column index (zero-based).
 * @return The label.
 */
  public String generateColumnLabel(  CategoryDataset dataset,  int column);
  /** 
 * Generates a label for the specified item. The label is typically a formatted version of the data value, but any text can be used.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @return The label (possibly <code>null</code>).
 */
  public String generateLabel(  CategoryDataset dataset,  int row,  int column);
}
