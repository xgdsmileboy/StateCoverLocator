package org.jfree.chart.block;
import org.jfree.chart.entity.EntityCollection;
/** 
 * Provides access to the                                                                                                                                                                {@link EntityCollection} generated when a block isdrawn.
 */
public interface EntityBlockResult {
  /** 
 * Returns the entity collection.
 * @return An entity collection (possibly <code>null</code>).
 */
  public EntityCollection getEntityCollection();
}
