package org.jfree.chart.labels;
import org.jfree.data.xy.XYDataset;
/** 
 * Interface for a tooltip generator for plots that use data from an                              {@link XYDataset}.
 */
public interface XYToolTipGenerator {
  /** 
 * Generates the tooltip text for the specified item.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The tooltip text (possibly <code>null</code>).
 */
  public String generateToolTip(  XYDataset dataset,  int series,  int item);
}
