package org.jfree.chart.annotations;
import org.jfree.data.Range;
/** 
 * An interface that supplies information about the bounds of the annotation.
 * @since 1.0.13
 */
public interface XYAnnotationBoundsInfo {
  /** 
 * Returns a flag that determines whether or not the annotation's bounds should be taken into account for auto-range calculations on the axes that the annotation is plotted against.
 * @return A boolean.
 */
  public boolean getIncludeInDataBounds();
  /** 
 * Returns the range of x-values (in data space) that the annotation uses.
 * @return The x-range.
 */
  public Range getXRange();
  /** 
 * Returns the range of y-values (in data space) that the annotation uses.
 * @return The y-range.
 */
  public Range getYRange();
}
