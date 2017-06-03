package org.jfree.chart.renderer;
import java.awt.Paint;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
/** 
 * A source for <code>Paint</code> instances, used by the                                                                                                                                                                    {@link XYBlockRenderer}. <br><br> NOTE: Classes that implement this interface should also implement <code>PublicCloneable</code> and <code>Serializable</code>, so that any renderer (or other object instance) that references an instance of this interface can still be cloned or serialized.
 * @since 1.0.4
 */
public interface PaintScale {
  /** 
 * Returns the lower bound for the scale.
 * @return The lower bound.
 * @see #getUpperBound()
 */
  public double getLowerBound();
  /** 
 * Returns the upper bound for the scale.
 * @return The upper bound.
 * @see #getLowerBound()
 */
  public double getUpperBound();
  /** 
 * Returns a <code>Paint</code> instance for the specified value.
 * @param value  the value.
 * @return A <code>Paint</code> instance (never <code>null</code>).
 */
  public Paint getPaint(  double value);
}
