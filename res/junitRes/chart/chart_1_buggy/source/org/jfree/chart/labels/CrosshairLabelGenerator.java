package org.jfree.chart.labels;
import org.jfree.chart.plot.Crosshair;
/** 
 * A label generator for crosshairs.
 * @since 1.0.13
 */
public interface CrosshairLabelGenerator {
  /** 
 * Returns a string that can be used as the label for a crosshair.
 * @param crosshair  the crosshair (<code>null</code> not permitted).
 * @return The label (possibly <code>null</code>).
 */
  public String generateLabel(  Crosshair crosshair);
}
