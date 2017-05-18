package org.jfree.chart.entity;
import java.util.Collection;
import java.util.Iterator;
/** 
 * This interface defines the methods used to access an ordered list of     {@link ChartEntity} objects.
 */
public interface EntityCollection {
  /** 
 * Clears all entities.
 */
  public void clear();
  /** 
 * Adds an entity to the collection.
 * @param entity  the entity (<code>null</code> not permitted).
 */
  public void add(  ChartEntity entity);
  /** 
 * Adds the entities from another collection to this collection.
 * @param collection  the other collection.
 */
  public void addAll(  EntityCollection collection);
  /** 
 * Returns an entity whose area contains the specified point.
 * @param x  the x coordinate.
 * @param y  the y coordinate.
 * @return The entity.
 */
  public ChartEntity getEntity(  double x,  double y);
  /** 
 * Returns an entity from the collection.
 * @param index  the index (zero-based).
 * @return An entity.
 */
  public ChartEntity getEntity(  int index);
  /** 
 * Returns the entity count.
 * @return The entity count.
 */
  public int getEntityCount();
  /** 
 * Returns the entities in an unmodifiable collection.
 * @return The entities.
 */
  public Collection getEntities();
  /** 
 * Returns an iterator for the entities in the collection.
 * @return An iterator.
 */
  public Iterator iterator();
}
