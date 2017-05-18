package org.jfree.chart.labels;
import org.jfree.data.category.CategoryDataset;
/** 
 * A generator that creates labels for the series in a      {@link CategoryDataset}. <P> Classes that implement this interface should be either (a) immutable, or (b) cloneable via the <code>PublicCloneable</code> interface (defined in the JCommon class library).  This provides a mechanism for the referring renderer to clone the generator if necessary.
 */
public interface CategorySeriesLabelGenerator {
  /** 
 * Generates a label for the specified series.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series index.
 * @return A series label.
 */
  public String generateLabel(  CategoryDataset dataset,  int series);
}
