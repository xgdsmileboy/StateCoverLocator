package org.jfree.data.general;
import java.io.Serializable;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.PublicCloneable;
/** 
 * A dataset that stores a single value (that is possibly <code>null</code>). This class provides a default implementation of the                                                                                                                                                                    {@link ValueDataset}interface.
 */
public class DefaultValueDataset extends AbstractDataset implements ValueDataset, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=8137521217249294891L;
  /** 
 * The value. 
 */
  private Number value;
  /** 
 * Constructs a new dataset, initially empty.
 */
  public DefaultValueDataset(){
    this(null);
  }
  /** 
 * Creates a new dataset with the specified value.
 * @param value  the value.
 */
  public DefaultValueDataset(  double value){
    this(new Double(value));
  }
  /** 
 * Creates a new dataset with the specified value.
 * @param value  the initial value (<code>null</code> permitted).
 */
  public DefaultValueDataset(  Number value){
    super();
    this.value=value;
  }
  /** 
 * Returns the value.
 * @return The value (possibly <code>null</code>).
 */
  public Number getValue(){
    return this.value;
  }
  /** 
 * Sets the value and sends a                                                                                                                                                                    {@link DatasetChangeEvent} to all registeredlisteners.
 * @param value  the new value (<code>null</code> permitted).
 */
  public void setValue(  Number value){
    this.value=value;
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
    if (obj instanceof ValueDataset) {
      ValueDataset vd=(ValueDataset)obj;
      return ObjectUtilities.equal(this.value,vd.getValue());
    }
    return false;
  }
  /** 
 * Returns a hash code.
 * @return A hash code.
 */
  public int hashCode(){
    return (this.value != null ? this.value.hashCode() : 0);
  }
}
