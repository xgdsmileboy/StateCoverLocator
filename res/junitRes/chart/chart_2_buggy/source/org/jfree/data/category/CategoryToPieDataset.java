package org.jfree.data.category;
import java.util.Collections;
import java.util.List;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.chart.util.TableOrder;
import org.jfree.data.pie.AbstractPieDataset;
import org.jfree.data.event.DatasetChangeEvent;
import org.jfree.data.event.DatasetChangeListener;
import org.jfree.data.pie.PieDataset;
/** 
 * A                                                                                                                                                                     {@link PieDataset} implementation that obtains its data from one row orcolumn of a  {@link CategoryDataset}.
 */
public class CategoryToPieDataset extends AbstractPieDataset implements PieDataset, DatasetChangeListener {
  /** 
 * For serialization. 
 */
  static final long serialVersionUID=5516396319762189617L;
  /** 
 * The source. 
 */
  private CategoryDataset source;
  /** 
 * The extract type. 
 */
  private TableOrder extract;
  /** 
 * The row or column index. 
 */
  private int index;
  /** 
 * An adaptor class that converts any                                                                                                                                                                     {@link CategoryDataset} into a{@link PieDataset}, by taking the values from a single row or column. <p> If <code>source</code> is <code>null</code>, the created dataset will be empty.
 * @param source  the source dataset (<code>null</code> permitted).
 * @param extract  extract data from rows or columns? (<code>null</code>not permitted).
 * @param index  the row or column index.
 */
  public CategoryToPieDataset(  CategoryDataset source,  TableOrder extract,  int index){
    if (extract == null) {
      throw new IllegalArgumentException("Null 'extract' argument.");
    }
    this.source=source;
    if (this.source != null) {
      this.source.addChangeListener(this);
    }
    this.extract=extract;
    this.index=index;
  }
  /** 
 * Returns the underlying dataset.
 * @return The underlying dataset (possibly <code>null</code>).
 * @since 1.0.2
 */
  public CategoryDataset getUnderlyingDataset(){
    return this.source;
  }
  /** 
 * Returns the extract type, which determines whether data is read from one row or one column of the underlying dataset.
 * @return The extract type.
 * @since 1.0.2
 */
  public TableOrder getExtractType(){
    return this.extract;
  }
  /** 
 * Returns the index of the row or column from which to extract the data.
 * @return The extract index.
 * @since 1.0.2
 */
  public int getExtractIndex(){
    return this.index;
  }
  /** 
 * Returns the number of items (values) in the collection.  If the underlying dataset is <code>null</code>, this method returns zero.
 * @return The item count.
 */
  public int getItemCount(){
    int result=0;
    if (this.source != null) {
      if (this.extract == TableOrder.BY_ROW) {
        result=this.source.getColumnCount();
      }
 else {
        if (this.extract == TableOrder.BY_COLUMN) {
          result=this.source.getRowCount();
        }
      }
    }
    return result;
  }
  /** 
 * Returns a value from the dataset.
 * @param item  the item index (zero-based).
 * @return The value (possibly <code>null</code>).
 * @throws IndexOutOfBoundsException if <code>item</code> is not in therange <code>0</code> to <code>getItemCount() - 1</code>.
 */
  public Number getValue(  int item){
    Number result=null;
    if (item < 0 || item >= getItemCount()) {
      throw new IndexOutOfBoundsException("The 'item' index is out of bounds.");
    }
    if (this.extract == TableOrder.BY_ROW) {
      result=this.source.getValue(this.index,item);
    }
 else {
      if (this.extract == TableOrder.BY_COLUMN) {
        result=this.source.getValue(item,this.index);
      }
    }
    return result;
  }
  /** 
 * Returns the key at the specified index.
 * @param index  the item index (in the range <code>0</code> to<code>getItemCount() - 1</code>).
 * @return The key.
 * @throws IndexOutOfBoundsException if <code>index</code> is not in thespecified range.
 */
  public Comparable getKey(  int index){
    Comparable result=null;
    if (index < 0 || index >= getItemCount()) {
      throw new IndexOutOfBoundsException("Invalid 'index': " + index);
    }
    if (this.extract == TableOrder.BY_ROW) {
      result=this.source.getColumnKey(index);
    }
 else {
      if (this.extract == TableOrder.BY_COLUMN) {
        result=this.source.getRowKey(index);
      }
    }
    return result;
  }
  /** 
 * Returns the index for a given key, or <code>-1</code> if there is no such key.
 * @param key  the key.
 * @return The index for the key, or <code>-1</code>.
 */
  public int getIndex(  Comparable key){
    int result=-1;
    if (this.source != null) {
      if (this.extract == TableOrder.BY_ROW) {
        result=this.source.getColumnIndex(key);
      }
 else {
        if (this.extract == TableOrder.BY_COLUMN) {
          result=this.source.getRowIndex(key);
        }
      }
    }
    return result;
  }
  /** 
 * Returns the keys for the dataset. <p> If the underlying dataset is <code>null</code>, this method returns an empty list.
 * @return The keys.
 */
  public List getKeys(){
    List result=Collections.EMPTY_LIST;
    if (this.source != null) {
      if (this.extract == TableOrder.BY_ROW) {
        result=this.source.getColumnKeys();
      }
 else {
        if (this.extract == TableOrder.BY_COLUMN) {
          result=this.source.getRowKeys();
        }
      }
    }
    return result;
  }
  /** 
 * Returns the value for a given key.  If the key is not recognised, the method should return <code>null</code> (but note that <code>null</code> can be associated with a valid key also).
 * @param key  the key.
 * @return The value (possibly <code>null</code>).
 */
  public Number getValue(  Comparable key){
    Number result=null;
    int keyIndex=getIndex(key);
    if (keyIndex != -1) {
      if (this.extract == TableOrder.BY_ROW) {
        result=this.source.getValue(this.index,keyIndex);
      }
 else {
        if (this.extract == TableOrder.BY_COLUMN) {
          result=this.source.getValue(keyIndex,this.index);
        }
      }
    }
    return result;
  }
  /** 
 * Sends a                                                                                                                                                                     {@link DatasetChangeEvent} to all registered listeners, withthis (not the underlying) dataset as the source.
 * @param event  the event (ignored, a new event with this dataset as thesource is sent to the listeners).
 */
  public void datasetChanged(  DatasetChangeEvent event){
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Tests this dataset for equality with an arbitrary object, returning <code>true</code> if <code>obj</code> is a dataset containing the same keys and values in the same order as this dataset.
 * @param obj  the object to test (<code>null</code> permitted).
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
}
