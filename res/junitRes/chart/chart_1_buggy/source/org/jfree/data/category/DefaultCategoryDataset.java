package org.jfree.data.category;
import java.io.Serializable;
import java.util.List;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.KeyedObjects2D;
import org.jfree.data.SelectableValue;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.event.DatasetChangeEvent;
/** 
 * A default implementation of the      {@link CategoryDataset} interface.
 */
public class DefaultCategoryDataset extends AbstractCategoryDataset implements CategoryDataset, SelectableCategoryDataset, CategoryDatasetSelectionState, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-8168173757291644622L;
  /** 
 * A storage structure for the data.
 */
  private KeyedObjects2D data;
  /** 
 * Creates a new (empty) dataset.
 */
  public DefaultCategoryDataset(){
    this.data=new KeyedObjects2D();
    setSelectionState(this);
  }
  /** 
 * Returns the number of rows in the table.
 * @return The row count.
 * @see #getColumnCount()
 */
  public int getRowCount(){
    return this.data.getRowCount();
  }
  /** 
 * Returns the number of columns in the table.
 * @return The column count.
 * @see #getRowCount()
 */
  public int getColumnCount(){
    return this.data.getColumnCount();
  }
  /** 
 * Returns a value from the table.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @return The value (possibly <code>null</code>).
 * @see #addValue(Number,Comparable,Comparable)
 * @see #removeValue(Comparable,Comparable)
 */
  public Number getValue(  int row,  int column){
    SelectableValue sv=(SelectableValue)this.data.getObject(row,column);
    if (sv == null) {
      return null;
    }
 else {
      return sv.getValue();
    }
  }
  /** 
 * Returns the key for the specified row.
 * @param row  the row index (zero-based).
 * @return The row key.
 * @see #getRowIndex(Comparable)
 * @see #getRowKeys()
 * @see #getColumnKey(int)
 */
  public Comparable getRowKey(  int row){
    return this.data.getRowKey(row);
  }
  /** 
 * Returns the row index for a given key.
 * @param key  the row key (<code>null</code> not permitted).
 * @return The row index.
 * @see #getRowKey(int)
 */
  public int getRowIndex(  Comparable key){
    return this.data.getRowIndex(key);
  }
  /** 
 * Returns the row keys.
 * @return The keys.
 * @see #getRowKey(int)
 */
  public List getRowKeys(){
    return this.data.getRowKeys();
  }
  /** 
 * Returns a column key.
 * @param column  the column index (zero-based).
 * @return The column key.
 * @see #getColumnIndex(Comparable)
 */
  public Comparable getColumnKey(  int column){
    return this.data.getColumnKey(column);
  }
  /** 
 * Returns the column index for a given key.
 * @param key  the column key (<code>null</code> not permitted).
 * @return The column index.
 * @see #getColumnKey(int)
 */
  public int getColumnIndex(  Comparable key){
    return this.data.getColumnIndex(key);
  }
  /** 
 * Returns the column keys.
 * @return The keys.
 * @see #getColumnKey(int)
 */
  public List getColumnKeys(){
    return this.data.getColumnKeys();
  }
  /** 
 * Returns the value for a pair of keys.
 * @param rowKey  the row key (<code>null</code> not permitted).
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @return The value (possibly <code>null</code>).
 * @throws UnknownKeyException if either key is not defined in the dataset.
 * @see #addValue(Number,Comparable,Comparable)
 */
  public Number getValue(  Comparable rowKey,  Comparable columnKey){
    SelectableValue sv=(SelectableValue)this.data.getObject(rowKey,columnKey);
    if (sv != null) {
      return sv.getValue();
    }
 else {
      return null;
    }
  }
  /** 
 * Adds a value to the table.  Performs the same function as setValue().
 * @param value  the value.
 * @param rowKey  the row key.
 * @param columnKey  the column key.
 * @see #getValue(Comparable,Comparable)
 * @see #removeValue(Comparable,Comparable)
 */
  public void addValue(  Number value,  Comparable rowKey,  Comparable columnKey){
    this.data.addObject(new SelectableValue(value),rowKey,columnKey);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Adds a value to the table.
 * @param value  the value.
 * @param rowKey  the row key.
 * @param columnKey  the column key.
 * @see #getValue(Comparable,Comparable)
 */
  public void addValue(  double value,  Comparable rowKey,  Comparable columnKey){
    addValue(new Double(value),rowKey,columnKey);
  }
  /** 
 * Adds or updates a value in the table and sends a     {@link DatasetChangeEvent} to all registered listeners.
 * @param value  the value (<code>null</code> permitted).
 * @param rowKey  the row key (<code>null</code> not permitted).
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @see #getValue(Comparable,Comparable)
 */
  public void setValue(  Number value,  Comparable rowKey,  Comparable columnKey){
    this.data.setObject(new SelectableValue(value),rowKey,columnKey);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Adds or updates a value in the table and sends a     {@link DatasetChangeEvent} to all registered listeners.
 * @param value  the value.
 * @param rowKey  the row key (<code>null</code> not permitted).
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @see #getValue(Comparable,Comparable)
 */
  public void setValue(  double value,  Comparable rowKey,  Comparable columnKey){
    setValue(new Double(value),rowKey,columnKey);
  }
  /** 
 * Adds the specified value to an existing value in the dataset (if the existing value is <code>null</code>, it is treated as if it were 0.0).
 * @param value  the value.
 * @param rowKey  the row key (<code>null</code> not permitted).
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @throws UnknownKeyException if either key is not defined in the dataset.
 */
  public void incrementValue(  double value,  Comparable rowKey,  Comparable columnKey){
    double existing=0.0;
    Number n=getValue(rowKey,columnKey);
    if (n != null) {
      existing=n.doubleValue();
    }
    setValue(existing + value,rowKey,columnKey);
  }
  /** 
 * Removes a value from the dataset and sends a      {@link DatasetChangeEvent}to all registered listeners.
 * @param rowKey  the row key (<code>null</code> not permitted).
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @see #addValue(Number,Comparable,Comparable)
 * @throws UnknownKeyException if either key is not in the dataset.
 */
  public void removeValue(  Comparable rowKey,  Comparable columnKey){
    this.data.removeObject(rowKey,columnKey);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Removes a row from the dataset and sends a      {@link DatasetChangeEvent}to all registered listeners.
 * @param rowIndex  the row index.
 * @see #removeColumn(int)
 */
  public void removeRow(  int rowIndex){
    this.data.removeRow(rowIndex);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Removes a row from the dataset and sends a      {@link DatasetChangeEvent}to all registered listeners.
 * @param rowKey  the row key.
 * @see #removeColumn(Comparable)
 */
  public void removeRow(  Comparable rowKey){
    this.data.removeRow(rowKey);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Removes a column from the dataset and sends a      {@link DatasetChangeEvent}to all registered listeners.
 * @param columnIndex  the column index.
 * @see #removeRow(int)
 */
  public void removeColumn(  int columnIndex){
    this.data.removeColumn(columnIndex);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Removes a column from the dataset and sends a      {@link DatasetChangeEvent}to all registered listeners.
 * @param columnKey  the column key (<code>null</code> not permitted).
 * @see #removeRow(Comparable)
 * @throws UnknownKeyException if <code>columnKey</code> is not definedin the dataset.
 */
  public void removeColumn(  Comparable columnKey){
    this.data.removeColumn(columnKey);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Clears all data from the dataset and sends a      {@link DatasetChangeEvent}to all registered listeners.
 */
  public void clear(){
    this.data.clear();
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Tests this dataset for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CategoryDataset)) {
      return false;
    }
    CategoryDataset that=(CategoryDataset)obj;
    if (!getRowKeys().equals(that.getRowKeys())) {
      return false;
    }
    if (!getColumnKeys().equals(that.getColumnKeys())) {
      return false;
    }
    int rowCount=getRowCount();
    int colCount=getColumnCount();
    for (int r=0; r < rowCount; r++) {
      for (int c=0; c < colCount; c++) {
        Number v1=getValue(r,c);
        Number v2=that.getValue(r,c);
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
 * Returns a hash code for the dataset.
 * @return A hash code.
 */
  public int hashCode(){
    return this.data.hashCode();
  }
  /** 
 * Returns a clone of the dataset.
 * @return A clone.
 * @throws CloneNotSupportedException if there is a problem cloning thedataset.
 */
  public Object clone() throws CloneNotSupportedException {
    DefaultCategoryDataset clone=(DefaultCategoryDataset)super.clone();
    clone.data=(KeyedObjects2D)this.data.clone();
    return clone;
  }
  public boolean isSelected(  int row,  int column){
    SelectableValue sv=(SelectableValue)this.data.getObject(row,column);
    return sv.isSelected();
  }
  public void setSelected(  int row,  int column,  boolean selected){
    setSelected(row,column,selected,true);
  }
  public void setSelected(  int row,  int column,  boolean selected,  boolean notify){
    SelectableValue sv=(SelectableValue)this.data.getObject(row,column);
    sv.setSelected(selected);
    if (notify) {
      fireSelectionEvent();
    }
  }
  public void clearSelection(){
    int rowCount=getRowCount();
    int colCount=getColumnCount();
    for (int r=0; r < rowCount; r++) {
      for (int c=0; c < colCount; c++) {
        setSelected(r,c,false,false);
      }
    }
    fireSelectionEvent();
  }
  public void fireSelectionEvent(){
    fireDatasetChanged(new DatasetChangeInfo());
  }
}
