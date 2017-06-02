package org.jfree.chart.block;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;
import org.jfree.chart.util.Size2D;
/** 
 * Arranges a block in the center of its container.  This class is immutable.
 */
public class CenterArrangement implements Arrangement, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-353308149220382047L;
  /** 
 * Creates a new instance.
 */
  public CenterArrangement(){
  }
  /** 
 * Adds a block to be managed by this instance.  This method is usually called by the                                                                                                                                                                {@link BlockContainer}, you shouldn't need to call it directly.
 * @param block  the block.
 * @param key  a key that controls the position of the block.
 */
  public void add(  Block block,  Object key){
  }
  /** 
 * Calculates and sets the bounds of all the items in the specified container, subject to the given constraint.  The <code>Graphics2D</code> can be used by some items (particularly items containing text) to calculate sizing parameters.
 * @param container  the container whose items are being arranged.
 * @param g2  the graphics device.
 * @param constraint  the size constraint.
 * @return The size of the container after arrangement of the contents.
 */
  public Size2D arrange(  BlockContainer container,  Graphics2D g2,  RectangleConstraint constraint){
    LengthConstraintType w=constraint.getWidthConstraintType();
    LengthConstraintType h=constraint.getHeightConstraintType();
    if (w == LengthConstraintType.NONE) {
      if (h == LengthConstraintType.NONE) {
        return arrangeNN(container,g2);
      }
 else {
        if (h == LengthConstraintType.FIXED) {
          throw new RuntimeException("Not implemented.");
        }
 else {
          if (h == LengthConstraintType.RANGE) {
            throw new RuntimeException("Not implemented.");
          }
        }
      }
    }
 else {
      if (w == LengthConstraintType.FIXED) {
        if (h == LengthConstraintType.NONE) {
          return arrangeFN(container,g2,constraint);
        }
 else {
          if (h == LengthConstraintType.FIXED) {
            throw new RuntimeException("Not implemented.");
          }
 else {
            if (h == LengthConstraintType.RANGE) {
              throw new RuntimeException("Not implemented.");
            }
          }
        }
      }
 else {
        if (w == LengthConstraintType.RANGE) {
          if (h == LengthConstraintType.NONE) {
            return arrangeRN(container,g2,constraint);
          }
 else {
            if (h == LengthConstraintType.FIXED) {
              return arrangeRF(container,g2,constraint);
            }
 else {
              if (h == LengthConstraintType.RANGE) {
                return arrangeRR(container,g2,constraint);
              }
            }
          }
        }
      }
    }
    throw new IllegalArgumentException("Unknown LengthConstraintType.");
  }
  /** 
 * Arranges the blocks in the container with a fixed width and no height constraint.
 * @param container  the container.
 * @param g2  the graphics device.
 * @param constraint  the constraint.
 * @return The size.
 */
  protected Size2D arrangeFN(  BlockContainer container,  Graphics2D g2,  RectangleConstraint constraint){
    List blocks=container.getBlocks();
    Block b=(Block)blocks.get(0);
    Size2D s=b.arrange(g2,RectangleConstraint.NONE);
    double width=constraint.getWidth();
    Rectangle2D bounds=new Rectangle2D.Double((width - s.width) / 2.0,0.0,s.width,s.height);
    b.setBounds(bounds);
    return new Size2D((width - s.width) / 2.0,s.height);
  }
  /** 
 * Arranges the blocks in the container with a fixed with and a range constraint on the height.
 * @param container  the container.
 * @param g2  the graphics device.
 * @param constraint  the constraint.
 * @return The size following the arrangement.
 */
  protected Size2D arrangeFR(  BlockContainer container,  Graphics2D g2,  RectangleConstraint constraint){
    Size2D s=arrangeFN(container,g2,constraint);
    if (constraint.getHeightRange().contains(s.height)) {
      return s;
    }
 else {
      RectangleConstraint c=constraint.toFixedHeight(constraint.getHeightRange().constrain(s.getHeight()));
      return arrangeFF(container,g2,c);
    }
  }
  /** 
 * Arranges the blocks in the container with the overall height and width specified as fixed constraints.
 * @param container  the container.
 * @param g2  the graphics device.
 * @param constraint  the constraint.
 * @return The size following the arrangement.
 */
  protected Size2D arrangeFF(  BlockContainer container,  Graphics2D g2,  RectangleConstraint constraint){
    return arrangeFN(container,g2,constraint);
  }
  /** 
 * Arranges the blocks with the overall width and height to fit within specified ranges.
 * @param container  the container.
 * @param g2  the graphics device.
 * @param constraint  the constraint.
 * @return The size after the arrangement.
 */
  protected Size2D arrangeRR(  BlockContainer container,  Graphics2D g2,  RectangleConstraint constraint){
    Size2D s1=arrangeNN(container,g2);
    if (constraint.getWidthRange().contains(s1.width)) {
      return s1;
    }
 else {
      RectangleConstraint c=constraint.toFixedWidth(constraint.getWidthRange().getUpperBound());
      return arrangeFR(container,g2,c);
    }
  }
  /** 
 * Arranges the blocks in the container with a range constraint on the width and a fixed height.
 * @param container  the container.
 * @param g2  the graphics device.
 * @param constraint  the constraint.
 * @return The size following the arrangement.
 */
  protected Size2D arrangeRF(  BlockContainer container,  Graphics2D g2,  RectangleConstraint constraint){
    Size2D s=arrangeNF(container,g2,constraint);
    if (constraint.getWidthRange().contains(s.width)) {
      return s;
    }
 else {
      RectangleConstraint c=constraint.toFixedWidth(constraint.getWidthRange().constrain(s.getWidth()));
      return arrangeFF(container,g2,c);
    }
  }
  /** 
 * Arranges the block with a range constraint on the width, and no constraint on the height.
 * @param container  the container.
 * @param g2  the graphics device.
 * @param constraint  the constraint.
 * @return The size following the arrangement.
 */
  protected Size2D arrangeRN(  BlockContainer container,  Graphics2D g2,  RectangleConstraint constraint){
    Size2D s1=arrangeNN(container,g2);
    if (constraint.getWidthRange().contains(s1.width)) {
      return s1;
    }
 else {
      RectangleConstraint c=constraint.toFixedWidth(constraint.getWidthRange().getUpperBound());
      return arrangeFN(container,g2,c);
    }
  }
  /** 
 * Arranges the blocks without any constraints.  This puts all blocks into a single row.
 * @param container  the container.
 * @param g2  the graphics device.
 * @return The size after the arrangement.
 */
  protected Size2D arrangeNN(  BlockContainer container,  Graphics2D g2){
    List blocks=container.getBlocks();
    Block b=(Block)blocks.get(0);
    Size2D s=b.arrange(g2,RectangleConstraint.NONE);
    b.setBounds(new Rectangle2D.Double(0.0,0.0,s.width,s.height));
    return new Size2D(s.width,s.height);
  }
  /** 
 * Arranges the blocks with no width constraint and a fixed height constraint.  This puts all blocks into a single row.
 * @param container  the container.
 * @param g2  the graphics device.
 * @param constraint  the constraint.
 * @return The size after the arrangement.
 */
  protected Size2D arrangeNF(  BlockContainer container,  Graphics2D g2,  RectangleConstraint constraint){
    return arrangeNN(container,g2);
  }
  /** 
 * Clears any cached information.
 */
  public void clear(){
  }
  /** 
 * Tests this instance for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CenterArrangement)) {
      return false;
    }
    return true;
  }
}
