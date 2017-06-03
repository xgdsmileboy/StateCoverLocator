package org.jfree.chart.labels;
import org.jfree.data.xy.XYZDataset;
/** 
 * Interface for a tooltip generator for plots that use data from an                                                                                                                                                                    {@link XYZDataset}.
 */
public interface XYZToolTipGenerator extends XYToolTipGenerator {
  /** 
 * Generates a tool tip text item for a particular item within a series.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The tooltip text (possibly <code>null</code>).
 */
  public String generateToolTip(  XYZDataset dataset,  int series,  int item);
}
