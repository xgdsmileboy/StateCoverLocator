package org.jfree.chart.labels;
import org.jfree.data.xy.XYDataset;
/** 
 * A generator that creates labels for the series in an                                                                                               {@link XYDataset}. <P> Classes that implement this interface should be either (a) immutable, or (b) cloneable via the <code>PublicCloneable</code> interface (defined in the JCommon class library).  This provides a mechanism for the referring renderer to clone the generator if necessary.
 */
public interface XYSeriesLabelGenerator {
  /** 
 * Generates a label for the specified series.  This label will be used for the chart legend.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series.
 * @return A series label.
 */
  public String generateLabel(  XYDataset dataset,  int series);
}
