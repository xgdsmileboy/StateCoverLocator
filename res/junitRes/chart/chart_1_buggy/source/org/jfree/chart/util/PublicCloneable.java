package org.jfree.chart.util;
/** 
 * An interface that exposes the clone() method.
 */
public interface PublicCloneable extends Cloneable {
  /** 
 * Returns a clone of the object.
 * @return A clone.
 * @throws CloneNotSupportedException if cloning is not supported for somereason.
 */
  public Object clone() throws CloneNotSupportedException ;
}
