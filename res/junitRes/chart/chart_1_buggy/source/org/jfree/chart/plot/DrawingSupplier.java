package org.jfree.chart.plot;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
/** 
 * A supplier of <code>Paint</code>, <code>Stroke</code> and <code>Shape</code> objects for use by plots and renderers.  By providing a central place for obtaining these items, we can ensure that duplication is avoided. <p> To support the cloning of charts, classes that implement this interface should also implement <code>PublicCloneable</code>.
 */
public interface DrawingSupplier {
  /** 
 * Returns the next paint in a sequence maintained by the supplier.
 * @return The paint.
 */
  public Paint getNextPaint();
  /** 
 * Returns the next outline paint in a sequence maintained by the supplier.
 * @return The paint.
 */
  public Paint getNextOutlinePaint();
  /** 
 * Returns the next fill paint in a sequence maintained by the supplier.
 * @return The paint.
 * @since 1.0.6
 */
  public Paint getNextFillPaint();
  /** 
 * Returns the next <code>Stroke</code> object in a sequence maintained by the supplier.
 * @return The stroke.
 */
  public Stroke getNextStroke();
  /** 
 * Returns the next <code>Stroke</code> object in a sequence maintained by the supplier.
 * @return The stroke.
 */
  public Stroke getNextOutlineStroke();
  /** 
 * Returns the next <code>Shape</code> object in a sequence maintained by the supplier.
 * @return The shape.
 */
  public Shape getNextShape();
}
