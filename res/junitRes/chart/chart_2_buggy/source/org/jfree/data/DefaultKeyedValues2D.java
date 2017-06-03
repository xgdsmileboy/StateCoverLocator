package org.jfree.data;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.PublicCloneable;
/** 
 * A data structure that stores zero, one or many values, where each value is associated with two keys (a 'row' key and a 'column' key).  The keys should be (a) instances of                                                                                                                                                                     {@link Comparable} and (b) immutable.
 */
public class DefaultKeyedValues2D implements KeyedValues2D, PublicCloneable, Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-5514169970951994748L;
  /** 
 * The row keys. 
 */
  private List rowKeys;
  /** 
 * The column keys. 
 */
  private List columnKeys;
  /** 
 * The row data. 
 */
  private List rows;
  /** 
 * If the row keys should be sorted by their comparable order. 
 */
  private boolean sortRowKeys;
  /** 
 * Creates a new instance (initially empty).
 */
  public DefaultKeyedValues2D(){
    this(false);
  }
  /** 
 * Creates a new instance (initially empty).
 * @param sortRowKeys  if the row keys should be sorted.
 */
  public DefaultKeyedValues2D(  boolean sortRowKeys){
    this.rowKeys=new java.util.ArrayList();
    this.columnKeys=new java.util.ArrayList();
    this.rows=new java.util.ArrayList();
    this.sortRowKeys=sortRowKeys;
  }
  /** 
 * Returns the row count.
 * @return The row count.
 * @see #getColumnCount()
 */
  public int getRowCount(){
    return this.rowKeys.size();
  }
  /** 
 * Returns the column count.
 * @return The column count.
 * @see #getRowCount()
 */
  public int getColumnCount(){
    return this.columnKeys.size();
  }
  /** 
 * Returns the value for a given row and column.
 * @param row  the row index.
 * @param column  the column index.
 * @return The value.
 * @see #getValue(Comparable,Comparable)
 */
  public Number getValue(  int row,  int column){
    Number result=null;
    DefaultKeyedValues rowData=(DefaultKeyedValues)this.rows.get(row);
    if (rowData != null) {
      Comparable columnKey=(Comparable)this.columnKeys.get(column);
      int index=rowData.getIndex(columnKey);
      if (index >= 0) {
        result=rowData.getValue(index);
      }
    }
    return result;
  }
  /** 
 * Returns the key for a given row.
 * @param row  the row index (in the range 0 to {@link #getRowCount()} - 1).
 * @return The row key.
 * @see #getRowIndex(Comparable)
 * @see #getColumnKey(int)
 */
  public Comparable getRowKey(  int row){
    return (Comparable)this.rowKeys.get(row);
  }
  /** 
 * Returns the row index for a given key.
 * @param key  the key (<code>null</code> not permitted).
 * @return The row index.
 * @see #getRowKey(int)
 * @see #getColumnIndex(Comparable)
 */
  public int getRowIndex(  Comparable key){
    if (key == null) {
      throw new IllegalArgumentException("Null 'key' argument.");
    }
    if (this.sortRowKeys) {
      return Collections.binarySearch(this.rowKeys,key);
    }
 else {
      return this.rowKeys.indexOf(key);
    }
  }
  /** 
 * Returns the row keys in an unmodifiable list.
 * @return The row keys.
 * @see #getColumnKeys()
 */
  public List getRowKeys(){
    return Collections.unmodifiableList(this.rowKeys);
  }
  /** 
 * Returns the key for a given column.
 * @param column  the column (in the range 0 to {@link #getColumnCount()}- 1).
 * @return The key.
 * @see #getColumnIndex(Comparable)
 * @see #getRowKey(int)
 */
  public Comparable getColumnKey(  int column){
    return (Comparable)this.columnKeys.get(column);
  }
  /** 
 * Returns the column index for a given key.
 * @param key  the key (<code>null</code> not permitted).
 * @return The column index.
 * @see #getColumnKey(int)
 * @see #getRowIndex(Comparable)
 */
  public int getColumnIndex(  Comparable key){
    if (key == null) {
      throw new IllegalArgumentException("Null 'key' argument.");
    }
    return this.columnKeys.indexOf(key);
  }
  /** 
 * Returns the column keys in an unmodifiable list.
 * @return The column keys.
 * @see #getRowKeys()
 */
  public List getColumnKeys(){
    return Collections.unmodifiableList(this.columnKeys);
  }
  /** 
 * Returns the value for the given row and column keys.  This method will throw an                                                                                                                                                                     {@link UnknownKeyException} if either key is not defined in thedata structure.
 * @param rowKey  the row key (<code>null</code> not permitted).
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @return The value (possibly <code>null</code>).
 * @see #addValue(Number,Comparable,Comparable)
 * @see #removeValue(Comparable,Comparable)
 */
  public Number getValue(  Comparable rowKey,  Comparable columnKey){
    if (rowKey == null) {
      throw new IllegalArgumentException("Null 'rowKey' argument.");
    }
    if (columnKey == null) {
      throw new IllegalArgumentException("Null 'columnKey' argument.");
    }
    if (!(this.columnKeys.contains(columnKey))) {
      throw new UnknownKeyException("Unrecognised columnKey: " + columnKey);
    }
    int row=getRowIndex(rowKey);
    if (row >= 0) {
      DefaultKeyedValues rowData=(DefaultKeyedValues)this.rows.get(row);
      int col=rowData.getIndex(columnKey);
      return (col >= 0 ? rowData.getValue(col) : null);
    }
 else {
      throw new UnknownKeyException("Unrecognised rowKey: " + rowKey);
    }
  }
  /** 
 * Adds a value to the table.  Performs the same function as #setValue(Number, Comparable, Comparable).
 * @param value  the value (<code>null</code> permitted).
 * @param rowKey  the row key (<code>null</code> not permitted).
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @see #setValue(Number,Comparable,Comparable)
 * @see #removeValue(Comparable,Comparable)
 */
  public void addValue(  Number value,  Comparable rowKey,  Comparable columnKey){
    setValue(value,rowKey,columnKey);
  }
  /** 
 * Adds or updates a value.
 * @param value  the value (<code>null</code> permitted).
 * @param rowKey  the row key (<code>null</code> not permitted).
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @see #addValue(Number,Comparable,Comparable)
 * @see #removeValue(Comparable,Comparable)
 */
  public void setValue(  Number value,  Comparable rowKey,  Comparable columnKey){
    DefaultKeyedValues row;
    int rowIndex=getRowIndex(rowKey);
    if (rowIndex >= 0) {
      row=(DefaultKeyedValues)this.rows.get(rowIndex);
    }
 else {
      row=new DefaultKeyedValues();
      if (this.sortRowKeys) {
        rowIndex=-rowIndex - 1;
        this.rowKeys.add(rowIndex,rowKey);
        this.rows.add(rowIndex,row);
      }
 else {
        this.rowKeys.add(rowKey);
        this.rows.add(row);
      }
    }
    row.setValue(columnKey,value);
    int columnIndex=this.columnKeys.indexOf(columnKey);
    if (columnIndex < 0) {
      this.columnKeys.add(columnKey);
    }
  }
  /** 
 * Removes a value from the table by setting it to <code>null</code>.  If all the values in the specified row and/or column are now <code>null</code>, the row and/or column is removed from the table.
 * @param rowKey  the row key (<code>null</code> not permitted).
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @see #addValue(Number,Comparable,Comparable)
 */
  public void removeValue(  Comparable rowKey,  Comparable columnKey){
    setValue(null,rowKey,columnKey);
    boolean allNull=true;
    int rowIndex=getRowIndex(rowKey);
    DefaultKeyedValues row=(DefaultKeyedValues)this.rows.get(rowIndex);
    for (int item=0, itemCount=row.getItemCount(); item < itemCount; item++) {
      if (row.getValue(item) != null) {
        allNull=false;
        break;
      }
    }
    if (allNull) {
      this.rowKeys.remove(rowIndex);
      this.rows.remove(rowIndex);
    }
    allNull=true;
    for (int item=0, itemCount=this.rows.size(); item < itemCount; item++) {
      row=(DefaultKeyedValues)this.rows.get(item);
      int columnIndex=row.getIndex(columnKey);
      if (columnIndex >= 0 && row.getValue(columnIndex) != null) {
        allNull=false;
        break;
      }
    }
    if (allNull) {
      for (int item=0, itemCount=this.rows.size(); item < itemCount; item++) {
        row=(DefaultKeyedValues)this.rows.get(item);
        int columnIndex=row.getIndex(columnKey);
        if (columnIndex >= 0) {
          row.removeValue(columnIndex);
        }
      }
      this.columnKeys.remove(columnKey);
    }
  }
  /** 
 * Removes a row.
 * @param rowIndex  the row index.
 * @see #removeRow(Comparable)
 * @see #removeColumn(int)
 */
  public void removeRow(  int rowIndex){
    this.rowKeys.remove(rowIndex);
    this.rows.remove(rowIndex);
  }
  /** 
 * Removes a row from the table.
 * @param rowKey  the row key (<code>null</code> not permitted).
 * @see #removeRow(int)
 * @see #removeColumn(Comparable)
 * @throws UnknownKeyException if <code>rowKey</code> is not defined in thetable.
 */
  public void removeRow(  Comparable rowKey){
    if (rowKey == null) {
      throw new IllegalArgumentException("Null 'rowKey' argument.");
    }
    int index=getRowIndex(rowKey);
    if (index >= 0) {
      removeRow(index);
    }
 else {
      throw new UnknownKeyException("Unknown key: " + rowKey);
    }
  }
  /** 
 * Removes a column.
 * @param columnIndex  the column index.
 * @see #removeColumn(Comparable)
 * @see #removeRow(int)
 */
  public void removeColumn(  int columnIndex){
    Comparable columnKey=getColumnKey(columnIndex);
    removeColumn(columnKey);
  }
  /** 
 * Removes a column from the table.
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @throws UnknownKeyException if the table does not contain a column withthe specified key.
 * @throws IllegalArgumentException if <code>columnKey</code> is<code>null</code>.
 * @see #removeColumn(int)
 * @see #removeRow(Comparable)
 */
  public void removeColumn(  Comparable columnKey){
    if (columnKey == null) {
      throw new IllegalArgumentException("Null 'columnKey' argument.");
    }
    if (!this.columnKeys.contains(columnKey)) {
      throw new UnknownKeyException("Unknown key: " + columnKey);
    }
    Iterator iterator=this.rows.iterator();
    while (iterator.hasNext()) {
      DefaultKeyedValues rowData=(DefaultKeyedValues)iterator.next();
      int index=rowData.getIndex(columnKey);
      if (index >= 0) {
        rowData.removeValue(columnKey);
      }
    }
    this.columnKeys.remove(columnKey);
  }
  /** 
 * Clears all the data and associated keys.
 */
  public void clear(){
    this.rowKeys.clear();
    this.columnKeys.clear();
    this.rows.clear();
  }
  /** 
 * Tests if this object is equal to another.
 * @param o  the other object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object o){
    if (o == null) {
      return false;
    }
    if (o == this) {
      return true;
    }
    if (!(o instanceof KeyedValues2D)) {
      return false;
    }
    KeyedValues2D kv2D=(KeyedValues2D)o;
    if (!getRowKeys().equals(kv2D.getRowKeys())) {
      return false;
    }
    if (!getColumnKeys().equals(kv2D.getColumnKeys())) {
      return false;
    }
    int rowCount=getRowCount();
    if (rowCount != kv2D.getRowCount()) {
      return false;
    }
    int colCount=getColumnCount();
    if (colCount != kv2D.getColumnCount()) {
      return false;
    }
    for (int r=0; r < rowCount; r++) {
      for (int c=0; c < colCount; c++) {
        Number v1=getValue(r,c);
        Number v2=kv2D.getValue(r,c);
        if (v1 == null) {
          if (v2 != null) {
            return false;
          }
        }
 else {
          if (!v1.equals(v2)) {
            return false;
          }
        }
      }
    }
    return true;
  }
  /** 
 * Returns a hash code.
 * @return A hash code.
 */
  public int hashCode(){
    int result;
    result=this.rowKeys.hashCode();
    result=29 * result + this.columnKeys.hashCode();
    result=29 * result + this.rows.hashCode();
    return result;
  }
  /** 
 * Returns a clone.
 * @return A clone.
 * @throws CloneNotSupportedException  this class will not throw thisexception, but subclasses (if any) might.
 */
  public Object clone() throws CloneNotSupportedException {
    DefaultKeyedValues2D clone=(DefaultKeyedValues2D)super.clone();
    clone.columnKeys=new java.util.ArrayList(this.columnKeys);
    clone.rowKeys=new java.util.ArrayList(this.rowKeys);
    clone.rows=(List)ObjectUtilities.deepClone(this.rows);
    return clone;
  }
}
