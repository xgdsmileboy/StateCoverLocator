package org.jfree.chart.block;
/** 
 * An interface that is used by the draw() method of some                                                                                                                                                                {@link Block}implementations to determine whether or not to generate entities for the items within the block.
 */
public interface EntityBlockParams {
  /** 
 * Returns a flag that controls whether or not the block should return entities for the items it draws.
 * @return A boolean.
 */
  public boolean getGenerateEntities();
}
