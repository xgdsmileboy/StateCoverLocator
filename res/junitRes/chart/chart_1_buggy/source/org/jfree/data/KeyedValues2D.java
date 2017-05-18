package org.jfree.data;
import java.util.List;
/** 
 * An extension of the      {@link Values2D} interface where a unique key isassociated with the row and column indices.
 */
public interface KeyedValues2D extends Values2D {
  /** 
 * Returns the row key for a given index.
 * @param row  the row index (zero-based).
 * @return The row key.
 * @throws IndexOutOfBoundsException if <code>row</code> is out of bounds.
 */
  public Comparable getRowKey(  int row);
  /** 
 * Returns the row index for a given key.
 * @param key  the row key.
 * @return The row index, or <code>-1</code> if the key is unrecognised.
 */
  public int getRowIndex(  Comparable key);
  /** 
 * Returns the row keys.
 * @return The keys.
 */
  public List getRowKeys();
  /** 
 * Returns the column key for a given index.
 * @param column  the column index (zero-based).
 * @return The column key.
 * @throws IndexOutOfBoundsException if <code>row</code> is out of bounds.
 */
  public Comparable getColumnKey(  int column);
  /** 
 * Returns the column index for a given key.
 * @param key  the column key.
 * @return The column index, or <code>-1</code> if the key is unrecognised.
 */
  public int getColumnIndex(  Comparable key);
  /** 
 * Returns the column keys.
 * @return The keys.
 */
  public List getColumnKeys();
  /** 
 * Returns the value associated with the specified keys.
 * @param rowKey  the row key (<code>null</code> not permitted).
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @return The value.
 * @throws UnknownKeyException if either key is not recognised.
 */
  public Number getValue(  Comparable rowKey,  Comparable columnKey);
}
