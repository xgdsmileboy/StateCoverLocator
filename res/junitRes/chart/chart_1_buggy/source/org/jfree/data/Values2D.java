package org.jfree.data;
/** 
 * A general purpose interface that can be used to access a table of values.
 */
public interface Values2D {
  /** 
 * Returns the number of rows in the table.
 * @return The row count.
 */
  public int getRowCount();
  /** 
 * Returns the number of columns in the table.
 * @return The column count.
 */
  public int getColumnCount();
  /** 
 * Returns a value from the table.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @return The value (possibly <code>null</code>).
 * @throws IndexOutOfBoundsException if the <code>row</code>or <code>column</code> is out of bounds.
 */
  public Number getValue(  int row,  int column);
}
