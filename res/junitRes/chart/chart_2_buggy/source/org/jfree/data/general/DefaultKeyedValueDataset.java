package org.jfree.data.general;
import java.io.Serializable;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.data.DefaultKeyedValue;
import org.jfree.data.KeyedValue;
/** 
 * A default implementation of the                                                                                               {@link KeyedValueDataset} interface.
 */
public class DefaultKeyedValueDataset extends AbstractDataset implements KeyedValueDataset, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-8149484339560406750L;
  /** 
 * Storage for the data. 
 */
  private KeyedValue data;
  /** 
 * Constructs a new dataset, initially empty.
 */
  public DefaultKeyedValueDataset(){
    this(null);
  }
  /** 
 * Creates a new dataset with the specified initial value.
 * @param key  the key.
 * @param value  the value (<code>null</code> permitted).
 */
  public DefaultKeyedValueDataset(  Comparable key,  Number value){
    this(new DefaultKeyedValue(key,value));
  }
  /** 
 * Creates a new dataset that uses the data from a                                                                                               {@link KeyedValue}instance.
 * @param data  the data (<code>null</code> permitted).
 */
  public DefaultKeyedValueDataset(  KeyedValue data){
    this.data=data;
  }
  /** 
 * Returns the key associated with the value, or <code>null</code> if the dataset has no data item.
 * @return The key.
 */
  public Comparable getKey(){
    Comparable result=null;
    if (this.data != null) {
      result=this.data.getKey();
    }
    return result;
  }
  /** 
 * Returns the value.
 * @return The value (possibly <code>null</code>).
 */
  public Number getValue(){
    Number result=null;
    if (this.data != null) {
      result=this.data.getValue();
    }
    return result;
  }
  /** 
 * Updates the value.
 * @param value  the new value (<code>null</code> permitted).
 */
  public void updateValue(  Number value){
    if (this.data == null) {
      throw new RuntimeException("updateValue: can't update null.");
    }
    setValue(this.data.getKey(),value);
  }
  /** 
 * Sets the value for the dataset and sends a                                                                                               {@link DatasetChangeEvent} toall registered listeners.
 * @param key  the key.
 * @param value  the value (<code>null</code> permitted).
 */
  public void setValue(  Comparable key,  Number value){
    this.data=new DefaultKeyedValue(key,value);
    this.fireDatasetChanged(new DatasetChangeInfo());
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
    if (!(obj instanceof KeyedValueDataset)) {
      return false;
    }
    KeyedValueDataset that=(KeyedValueDataset)obj;
    if (this.data == null) {
      if (that.getKey() != null || that.getValue() != null) {
        return false;
      }
      return true;
    }
    if (!ObjectUtilities.equal(this.data.getKey(),that.getKey())) {
      return false;
    }
    if (!ObjectUtilities.equal(this.data.getValue(),that.getValue())) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code.
 * @return A hash code.
 */
  public int hashCode(){
    return (this.data != null ? this.data.hashCode() : 0);
  }
  /** 
 * Creates a clone of the dataset.
 * @return A clone.
 * @throws CloneNotSupportedException This class will not throw thisexception, but subclasses (if any) might.
 */
  public Object clone() throws CloneNotSupportedException {
    DefaultKeyedValueDataset clone=(DefaultKeyedValueDataset)super.clone();
    return clone;
  }
}
