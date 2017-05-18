package org.jfree.chart.plot.dial;
/** 
 * A dial scale is a specialised layer that has the ability to convert data values into angles.
 * @since 1.0.7
 */
public interface DialScale extends DialLayer {
  /** 
 * Converts a data value to an angle (in degrees, using the same specification as Java's Arc2D class).
 * @param value  the data value.
 * @return The angle in degrees.
 * @see #angleToValue(double)
 */
  public double valueToAngle(  double value);
  /** 
 * Converts an angle (in degrees) to a data value.
 * @param angle  the angle (in degrees).
 * @return The data value.
 * @see #valueToAngle(double)
 */
  public double angleToValue(  double angle);
}
