package org.jfree.chart.block;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.Drawable;
import org.jfree.chart.util.Size2D;
/** 
 * A block is an arbitrary item that can be drawn (in Java2D space) within a rectangular area, has a preferred size, and can be arranged by an     {@link Arrangement} manager.
 */
public interface Block extends Drawable {
  /** 
 * Returns an ID for the block.
 * @return An ID.
 */
  public String getID();
  /** 
 * Sets the ID for the block.
 * @param id  the ID.
 */
  public void setID(  String id);
  /** 
 * Arranges the contents of the block, with no constraints, and returns the block size.
 * @param g2  the graphics device.
 * @return The size of the block.
 */
  public Size2D arrange(  Graphics2D g2);
  /** 
 * Arranges the contents of the block, within the given constraints, and returns the block size.
 * @param g2  the graphics device.
 * @param constraint  the constraint (<code>null</code> not permitted).
 * @return The block size (in Java2D units, never <code>null</code>).
 */
  public Size2D arrange(  Graphics2D g2,  RectangleConstraint constraint);
  /** 
 * Returns the current bounds of the block.
 * @return The bounds.
 */
  public Rectangle2D getBounds();
  /** 
 * Sets the bounds of the block.
 * @param bounds  the bounds.
 */
  public void setBounds(  Rectangle2D bounds);
  /** 
 * Draws the block within the specified area.  Refer to the documentation for the implementing class for information about the <code>params</code> and return value supported.
 * @param g2  the graphics device.
 * @param area  the area.
 * @param params  optional parameters (<code>null</code> permitted).
 * @return An optional return value (possibly <code>null</code>).
 */
  public Object draw(  Graphics2D g2,  Rectangle2D area,  Object params);
}
