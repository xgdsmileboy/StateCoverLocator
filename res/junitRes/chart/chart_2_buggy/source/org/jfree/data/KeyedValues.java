package org.jfree.data;
import java.util.List;
/** 
 * An ordered list of (key, value) items where the keys are unique and non-<code>null</code>.
 * @see Values
 * @see DefaultKeyedValues
 */
public interface KeyedValues extends Values {
  /** 
 * Returns the key associated with the item at a given position.  Note that some implementations allow re-ordering of the data items, so the result may be transient.
 * @param index  the item index (in the range <code>0</code> to<code>getItemCount() - 1</code>).
 * @return The key (never <code>null</code>).
 * @throws IndexOutOfBoundsException if <code>index</code> is not in thespecified range.
 */
  public Comparable getKey(  int index);
  /** 
 * Returns the index for a given key.
 * @param key  the key (<code>null</code> not permitted).
 * @return The index, or <code>-1</code> if the key is unrecognised.
 * @throws IllegalArgumentException if <code>key</code> is<code>null</code>.
 */
  public int getIndex(  Comparable key);
  /** 
 * Returns the keys for the values in the collection.  Note that you can access the values in this collection by key or by index.  For this reason, the key order is important - this method should return the keys in order.  The returned list may be unmodifiable.
 * @return The keys (never <code>null</code>).
 */
  public List getKeys();
  /** 
 * Returns the value for a given key.
 * @param key  the key.
 * @return The value (possibly <code>null</code>).
 * @throws UnknownKeyException if the key is not recognised.
 */
  public Number getValue(  Comparable key);
}
