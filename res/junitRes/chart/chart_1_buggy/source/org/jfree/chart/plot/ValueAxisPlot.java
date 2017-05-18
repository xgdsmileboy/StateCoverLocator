package org.jfree.chart.plot;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.Range;
/** 
 * An interface that is implemented by plots that use a      {@link ValueAxis}, providing a standard method to find the current data range.
 */
public interface ValueAxisPlot {
  /** 
 * Returns the data range that should apply for the specified axis.
 * @param axis  the axis.
 * @return The data range.
 */
  public Range getDataRange(  ValueAxis axis);
}
