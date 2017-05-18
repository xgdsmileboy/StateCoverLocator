package org.jfree.chart.block;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.Size2D;
/** 
 * An empty block with a fixed size.
 */
public class EmptyBlock extends AbstractBlock implements Block, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-4083197869412648579L;
  /** 
 * Creates a new block with the specified width and height.
 * @param width  the width.
 * @param height  the height.
 */
  public EmptyBlock(  double width,  double height){
    setWidth(width);
    setHeight(height);
  }
  /** 
 * Arranges the contents of the block, within the given constraints, and returns the block size.
 * @param g2  the graphics device.
 * @param constraint  the constraint (<code>null</code> not permitted).
 * @return The block size (in Java2D units, never <code>null</code>).
 */
  public Size2D arrange(  Graphics2D g2,  RectangleConstraint constraint){
    Size2D base=new Size2D(calculateTotalWidth(getWidth()),calculateTotalHeight(getHeight()));
    return constraint.calculateConstrainedSize(base);
  }
  /** 
 * Draws the block.  Since the block is empty, there is nothing to draw except the optional border.
 * @param g2  the graphics device.
 * @param area  the area.
 */
  public void draw(  Graphics2D g2,  Rectangle2D area){
    draw(g2,area,null);
  }
  /** 
 * Draws the block within the specified area.  Since the block is empty, there is nothing to draw except the optional border.
 * @param g2  the graphics device.
 * @param area  the area.
 * @param params  ignored (<code>null</code> permitted).
 * @return Always <code>null</code>.
 */
  public Object draw(  Graphics2D g2,  Rectangle2D area,  Object params){
    area=trimMargin(area);
    drawBorder(g2,area);
    return null;
  }
  /** 
 * Returns a clone of the block.
 * @return A clone.
 * @throws CloneNotSupportedException if there is a problem cloning.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
