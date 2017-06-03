package org.jfree.data.pie;
import org.jfree.data.SelectableValue;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.SortOrder;
import org.jfree.data.KeyedObjects;
import org.jfree.data.KeyedValues;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.event.DatasetChangeEvent;
/** 
 * A default implementation of the                                                                                                                                                                    {@link PieDataset} interface.
 */
public class DefaultPieDataset extends AbstractPieDataset implements PieDataset, PieDatasetSelectionState, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=2904745139106540618L;
  /** 
 * Storage for the data. 
 */
  private KeyedObjects data;
  /** 
 * Constructs a new dataset, initially empty.
 */
  public DefaultPieDataset(){
    this.data=new KeyedObjects();
    setSelectionState(this);
  }
  /** 
 * Creates a new dataset by copying data from a                                                                                                                                                                    {@link KeyedValues}instance.
 * @param data  the data (<code>null</code> not permitted).
 */
  public DefaultPieDataset(  KeyedValues data){
    if (data == null) {
      throw new IllegalArgumentException("Null 'data' argument.");
    }
    this.data=new KeyedObjects();
    for (int i=0; i < data.getItemCount(); i++) {
      SelectableValue dataItem=new SelectableValue(data.getValue(i));
      this.data.addObject(data.getKey(i),dataItem);
    }
  }
  /** 
 * Returns the number of items in the dataset.
 * @return The item count.
 */
  public int getItemCount(){
    return this.data.getItemCount();
  }
  /** 
 * Returns the categories in the dataset.  The returned list is unmodifiable.
 * @return The categories in the dataset.
 */
  public List getKeys(){
    return Collections.unmodifiableList(this.data.getKeys());
  }
  /** 
 * Returns the key for the specified item, or <code>null</code>.
 * @param item  the item index (in the range <code>0</code> to<code>getItemCount() - 1</code>).
 * @return The key, or <code>null</code>.
 * @throws IndexOutOfBoundsException if <code>item</code> is not in thespecified range.
 */
  public Comparable getKey(  int item){
    return this.data.getKey(item);
  }
  /** 
 * Returns the index for a key, or -1 if the key is not recognised.
 * @param key  the key (<code>null</code> not permitted).
 * @return The index, or <code>-1</code> if the key is unrecognised.
 * @throws IllegalArgumentException if <code>key</code> is<code>null</code>.
 */
  public int getIndex(  Comparable key){
    return this.data.getIndex(key);
  }
  /** 
 * Returns a value.
 * @param item  the value index.
 * @return The value (possibly <code>null</code>).
 */
  public Number getValue(  int item){
    Number result=null;
    if (getItemCount() > item) {
      SelectableValue dataItem=(SelectableValue)this.data.getObject(item);
      result=dataItem.getValue();
    }
    return result;
  }
  /** 
 * Returns the data value associated with a key.
 * @param key  the key (<code>null</code> not permitted).
 * @return The value (possibly <code>null</code>).
 * @throws UnknownKeyException if the key is not recognised.
 */
  public Number getValue(  Comparable key){
    if (key == null) {
      throw new IllegalArgumentException("Null 'key' argument.");
    }
    SelectableValue dataItem=(SelectableValue)this.data.getObject(key);
    return dataItem.getValue();
  }
  /** 
 * Sets the data value for a key and sends a                                                                                                                                                                    {@link DatasetChangeEvent} toall registered listeners.
 * @param key  the key (<code>null</code> not permitted).
 * @param value  the value.
 * @throws IllegalArgumentException if <code>key</code> is<code>null</code>.
 */
  public void setValue(  Comparable key,  Number value){
    int index=this.data.getIndex(key);
    PieDatasetChangeType ct=PieDatasetChangeType.ADD;
    if (index >= 0) {
      ct=PieDatasetChangeType.UPDATE;
    }
    this.data.setObject(key,new SelectableValue(value));
    PieDatasetChangeInfo info=new PieDatasetChangeInfo(ct,index,index);
    fireDatasetChanged(info);
  }
  /** 
 * Sets the data value for a key and sends a                                                                                                                                                                    {@link DatasetChangeEvent} toall registered listeners.
 * @param key  the key (<code>null</code> not permitted).
 * @param value  the value.
 * @throws IllegalArgumentException if <code>key</code> is<code>null</code>.
 */
  public void setValue(  Comparable key,  double value){
    setValue(key,new Double(value));
  }
  /** 
 * Inserts a new value at the specified position in the dataset or, if there is an existing item with the specified key, updates the value for that item and moves it to the specified position.  After the change is made, this methods sends a                                                                                                                                                                    {@link DatasetChangeEvent} to allregistered listeners.
 * @param position  the position (in the range 0 to getItemCount()).
 * @param key  the key (<code>null</code> not permitted).
 * @param value  the value (<code>null</code> permitted).
 * @since 1.0.6
 */
  public void insertValue(  int position,  Comparable key,  double value){
    insertValue(position,key,new Double(value));
  }
  /** 
 * Inserts a new value at the specified position in the dataset or, if there is an existing item with the specified key, updates the value for that item and moves it to the specified position.  After the change is made, this method sends a                                                                                                                                                                    {@link DatasetChangeEvent} to allregistered listeners.
 * @param position  the position (in the range 0 to getItemCount()).
 * @param key  the key (<code>null</code> not permitted).
 * @param value  the value (<code>null</code> permitted).
 * @since 1.0.6
 */
  public void insertValue(  int position,  Comparable key,  Number value){
    this.data.insertValue(position,key,value);
    PieDatasetChangeType ct=PieDatasetChangeType.ADD;
    fireDatasetChanged(new PieDatasetChangeInfo(ct,position,position));
  }
  /** 
 * Removes an item from the dataset and sends a                                                                                                                                                                    {@link DatasetChangeEvent}to all registered listeners.
 * @param key  the key (<code>null</code> not permitted).
 * @throws IllegalArgumentException if <code>key</code> is<code>null</code>.
 */
  public void remove(  Comparable key){
    int i=getIndex(key);
    this.data.removeValue(key);
    PieDatasetChangeType ct=PieDatasetChangeType.REMOVE;
    fireDatasetChanged(new PieDatasetChangeInfo(ct,i,i));
  }
  /** 
 * Clears all data from this dataset and sends a                                                                                                                                                                    {@link DatasetChangeEvent}to all registered listeners (unless the dataset was already empty).
 * @since 1.0.2
 */
  public void clear(){
    if (getItemCount() > 0) {
      this.data.clear();
      PieDatasetChangeType ct=PieDatasetChangeType.CLEAR;
      fireDatasetChanged(new PieDatasetChangeInfo(ct,-1,-1));
    }
  }
  /** 
 * Sorts the dataset's items by key and sends a                                                                                                                                                                    {@link DatasetChangeEvent}to all registered listeners.
 * @param order  the sort order (<code>null</code> not permitted).
 * @since 1.0.3
 */
  public void sortByKeys(  SortOrder order){
    this.data.sortByKeys(order);
    PieDatasetChangeType ct=PieDatasetChangeType.UPDATE;
    fireDatasetChanged(new PieDatasetChangeInfo(ct,0,getItemCount() - 1));
  }
  /** 
 * Sorts the dataset's items by value and sends a                                                                                                                                                                    {@link DatasetChangeEvent}to all registered listeners.
 * @param order  the sort order (<code>null</code> not permitted).
 * @since 1.0.3
 */
  public void sortByValues(  SortOrder order){
    this.data.sortByObjects(order);
    PieDatasetChangeType ct=PieDatasetChangeType.UPDATE;
    fireDatasetChanged(new PieDatasetChangeInfo(ct,0,getItemCount() - 1));
  }
  /** 
 * Tests if this object is equal to another.
 * @param obj  the other object.
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof PieDataset)) {
      return false;
    }
    PieDataset that=(PieDataset)obj;
    int count=getItemCount();
    if (that.getItemCount() != count) {
      return false;
    }
    for (int i=0; i < count; i++) {
      Comparable k1=getKey(i);
      Comparable k2=that.getKey(i);
      if (!k1.equals(k2)) {
        return false;
      }
      Number v1=getValue(i);
      Number v2=that.getValue(i);
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
    return true;
  }
  /** 
 * Returns a hash code.
 * @return A hash code.
 */
  public int hashCode(){
    return this.data.hashCode();
  }
  /** 
 * Returns a clone of the dataset.
 * @return A clone.
 * @throws CloneNotSupportedException This class will not throw thisexception, but subclasses (if any) might.
 */
  public Object clone() throws CloneNotSupportedException {
    DefaultPieDataset clone=(DefaultPieDataset)super.clone();
    clone.data=(KeyedObjects)this.data.clone();
    return clone;
  }
  public boolean isSelected(  Comparable key){
    SelectableValue item=(SelectableValue)this.data.getObject(key);
    return item.isSelected();
  }
  public void setSelected(  Comparable key,  boolean selected){
    setSelected(key,selected,true);
  }
  public void setSelected(  Comparable key,  boolean selected,  boolean notify){
    SelectableValue item=(SelectableValue)this.data.getObject(key);
    item.setSelected(selected);
    if (notify) {
      fireSelectionEvent();
    }
  }
  public void clearSelection(){
    int itemCount=getItemCount();
    for (int i=0; i < itemCount; i++) {
      SelectableValue item=(SelectableValue)this.data.getObject(i);
      item.setSelected(false);
    }
    fireSelectionEvent();
  }
  public void fireSelectionEvent(){
    this.fireDatasetChanged(new DatasetChangeInfo());
  }
}
