package org.jfree.chart.block;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.Size2D;
/** 
 * A container for a collection of      {@link Block} objects.  The container usesan  {@link Arrangement} object to handle the position of each block.
 */
public class BlockContainer extends AbstractBlock implements Block, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=8199508075695195293L;
  /** 
 * The blocks within the container. 
 */
  private List blocks;
  /** 
 * The object responsible for laying out the blocks. 
 */
  private Arrangement arrangement;
  /** 
 * Creates a new instance with default settings.
 */
  public BlockContainer(){
    this(new BorderArrangement());
  }
  /** 
 * Creates a new instance with the specified arrangement.
 * @param arrangement  the arrangement manager (<code>null</code> notpermitted).
 */
  public BlockContainer(  Arrangement arrangement){
    if (arrangement == null) {
      throw new IllegalArgumentException("Null 'arrangement' argument.");
    }
    this.arrangement=arrangement;
    this.blocks=new ArrayList();
  }
  /** 
 * Returns the arrangement (layout) manager for the container.
 * @return The arrangement manager (never <code>null</code>).
 */
  public Arrangement getArrangement(){
    return this.arrangement;
  }
  /** 
 * Sets the arrangement (layout) manager.
 * @param arrangement  the arrangement (<code>null</code> not permitted).
 */
  public void setArrangement(  Arrangement arrangement){
    if (arrangement == null) {
      throw new IllegalArgumentException("Null 'arrangement' argument.");
    }
    this.arrangement=arrangement;
  }
  /** 
 * Returns <code>true</code> if there are no blocks in the container, and <code>false</code> otherwise.
 * @return A boolean.
 */
  public boolean isEmpty(){
    return this.blocks.isEmpty();
  }
  /** 
 * Returns an unmodifiable list of the      {@link Block} objects managed bythis arrangement.
 * @return A list of blocks.
 */
  public List getBlocks(){
    return Collections.unmodifiableList(this.blocks);
  }
  /** 
 * Adds a block to the container.
 * @param block  the block (<code>null</code> permitted).
 */
  public void add(  Block block){
    add(block,null);
  }
  /** 
 * Adds a block to the container.
 * @param block  the block (<code>null</code> permitted).
 * @param key  the key (<code>null</code> permitted).
 */
  public void add(  Block block,  Object key){
    this.blocks.add(block);
    this.arrangement.add(block,key);
  }
  /** 
 * Clears all the blocks from the container.
 */
  public void clear(){
    this.blocks.clear();
    this.arrangement.clear();
  }
  /** 
 * Arranges the contents of the block, within the given constraints, and returns the block size.
 * @param g2  the graphics device.
 * @param constraint  the constraint (<code>null</code> not permitted).
 * @return The block size (in Java2D units, never <code>null</code>).
 */
  public Size2D arrange(  Graphics2D g2,  RectangleConstraint constraint){
    return this.arrangement.arrange(this,g2,constraint);
  }
  /** 
 * Draws the container and all the blocks within it.
 * @param g2  the graphics device.
 * @param area  the area.
 */
  public void draw(  Graphics2D g2,  Rectangle2D area){
    draw(g2,area,null);
  }
  /** 
 * Draws the block within the specified area.
 * @param g2  the graphics device.
 * @param area  the area.
 * @param params  passed on to blocks within the container(<code>null</code> permitted).
 * @return An instance of {@link EntityBlockResult}, or <code>null</code>.
 */
  public Object draw(  Graphics2D g2,  Rectangle2D area,  Object params){
    EntityBlockParams ebp=null;
    StandardEntityCollection sec=null;
    if (params instanceof EntityBlockParams) {
      ebp=(EntityBlockParams)params;
      if (ebp.getGenerateEntities()) {
        sec=new StandardEntityCollection();
      }
    }
    Rectangle2D contentArea=(Rectangle2D)area.clone();
    contentArea=trimMargin(contentArea);
    drawBorder(g2,contentArea);
    contentArea=trimBorder(contentArea);
    contentArea=trimPadding(contentArea);
    Iterator iterator=this.blocks.iterator();
    while (iterator.hasNext()) {
      Block block=(Block)iterator.next();
      Rectangle2D bounds=block.getBounds();
      Rectangle2D drawArea=new Rectangle2D.Double(bounds.getX() + area.getX(),bounds.getY() + area.getY(),bounds.getWidth(),bounds.getHeight());
      Object r=block.draw(g2,drawArea,params);
      if (sec != null) {
        if (r instanceof EntityBlockResult) {
          EntityBlockResult ebr=(EntityBlockResult)r;
          EntityCollection ec=ebr.getEntityCollection();
          sec.addAll(ec);
        }
      }
    }
    BlockResult result=null;
    if (sec != null) {
      result=new BlockResult();
      result.setEntityCollection(sec);
    }
    return result;
  }
  /** 
 * Tests this container for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof BlockContainer)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    BlockContainer that=(BlockContainer)obj;
    if (!this.arrangement.equals(that.arrangement)) {
      return false;
    }
    if (!this.blocks.equals(that.blocks)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a clone of the container.
 * @return A clone.
 * @throws CloneNotSupportedException if there is a problem cloning.
 */
  public Object clone() throws CloneNotSupportedException {
    BlockContainer clone=(BlockContainer)super.clone();
    return clone;
  }
}
