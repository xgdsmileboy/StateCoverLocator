package org.jfree.chart.labels;
import org.jfree.data.xy.XYDataset;
/** 
 * Interface for a label generator for plots that use data from an                                                                                                                                                               {@link XYDataset}.
 */
public interface XYItemLabelGenerator {
  /** 
 * Generates a label for the specified item. The label is typically a formatted version of the data value, but any text can be used.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The label (possibly <code>null</code>).
 */
  public String generateLabel(  XYDataset dataset,  int series,  int item);
}
