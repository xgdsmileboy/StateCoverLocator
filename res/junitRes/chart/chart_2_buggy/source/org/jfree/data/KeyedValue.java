package org.jfree.data;
/** 
 * A (key, value) pair.
 * @see DefaultKeyedValue
 */
public interface KeyedValue extends Value {
  /** 
 * Returns the key associated with the value.  The key returned by this method should be immutable.
 * @return The key (never <code>null</code>).
 */
  public Comparable getKey();
}
