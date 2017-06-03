package org.jfree.chart.block;
import org.jfree.chart.entity.EntityCollection;
/** 
 * Used to return results from the draw() method in the                                                                                                                                                                     {@link Block}class.
 */
public class BlockResult implements EntityBlockResult {
  /** 
 * The entities from the block. 
 */
  private EntityCollection entities;
  /** 
 * Creates a new result instance.
 */
  public BlockResult(){
    this.entities=null;
  }
  /** 
 * Returns the collection of entities from the block.
 * @return The entities.
 */
  public EntityCollection getEntityCollection(){
    return this.entities;
  }
  /** 
 * Sets the entities for the block.
 * @param entities  the entities.
 */
  public void setEntityCollection(  EntityCollection entities){
    this.entities=entities;
  }
}
