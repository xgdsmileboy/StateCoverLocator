package org.jfree.chart.labels;
import org.jfree.data.pie.PieDataset;
/** 
 * A tool tip generator that is used by the     {@link org.jfree.chart.plot.PiePlot} class.
 */
public interface PieToolTipGenerator {
  /** 
 * Generates a tool tip text item for the specified item in the dataset. This method can return <code>null</code> to indicate that no tool tip should be displayed for an item.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param key  the section key (<code>null</code> not permitted).
 * @return The tool tip text (possibly <code>null</code>).
 */
  public String generateToolTip(  PieDataset dataset,  Comparable key);
}
