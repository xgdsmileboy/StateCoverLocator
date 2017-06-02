package org.jfree.chart.labels;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.CategoryDataset;
/** 
 * A <i>category tool tip generator</i> is an object that can be assigned to a                                                                                                                                                               {@link org.jfree.chart.renderer.category.CategoryItemRenderer} and thatassumes responsibility for creating text items to be used as tooltips for the items in a  {@link org.jfree.chart.plot.CategoryPlot}. <p> To assist with cloning charts, classes that implement this interface should also implement the                                                                                                                                                                {@link PublicCloneable} interface.
 */
public interface CategoryToolTipGenerator {
  /** 
 * Generates the tool tip text for an item in a dataset.  Note: in the current dataset implementation, each row is a series, and each column contains values for a particular category.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @return The tooltip text (possibly <code>null</code>).
 */
  public String generateToolTip(  CategoryDataset dataset,  int row,  int column);
}
