package org.jfree.data;
/** 
 * An interface through which (single-dimension) data values can be accessed.
 */
public interface Values {
  /** 
 * Returns the number of items (values) in the collection.
 * @return The item count (possibly zero).
 */
  public int getItemCount();
  /** 
 * Returns the value with the specified index.
 * @param index  the item index (in the range <code>0</code> to<code>getItemCount() - 1</code>).
 * @return The value (possibly <code>null</code>).
 * @throws IndexOutOfBoundsException if <code>index</code> is not in thespecified range.
 */
  public Number getValue(  int index);
}
