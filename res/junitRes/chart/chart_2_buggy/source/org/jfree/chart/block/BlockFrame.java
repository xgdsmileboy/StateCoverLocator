package org.jfree.chart.block;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.RectangleInsets;
/** 
 * A block frame is a type of border that can be drawn around the outside of any                                                                                                                                                                {@link AbstractBlock}.  Classes that implement this interface should implement                                                                                                                                                                {@link PublicCloneable} OR be immutable.
 * @since 1.0.5
 */
public interface BlockFrame {
  /** 
 * Returns the space reserved for the border.
 * @return The space (never <code>null</code>).
 */
  public RectangleInsets getInsets();
  /** 
 * Draws the border by filling in the reserved space (in black).
 * @param g2  the graphics device.
 * @param area  the area.
 */
  public void draw(  Graphics2D g2,  Rectangle2D area);
}
