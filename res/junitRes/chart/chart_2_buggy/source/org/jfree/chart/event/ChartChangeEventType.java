package org.jfree.chart.event;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Defines tokens used to indicate an event type.
 */
public final class ChartChangeEventType implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=5481917022435735602L;
  /** 
 * GENERAL. 
 */
  public static final ChartChangeEventType GENERAL=new ChartChangeEventType("ChartChangeEventType.GENERAL");
  /** 
 * NEW_DATASET. 
 */
  public static final ChartChangeEventType NEW_DATASET=new ChartChangeEventType("ChartChangeEventType.NEW_DATASET");
  /** 
 * DATASET_UPDATED. 
 */
  public static final ChartChangeEventType DATASET_UPDATED=new ChartChangeEventType("ChartChangeEventType.DATASET_UPDATED");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private ChartChangeEventType(  String name){
    this.name=name;
  }
  /** 
 * Returns a string representing the object.
 * @return The string.
 */
  public String toString(){
    return this.name;
  }
  /** 
 * Returns <code>true</code> if this object is equal to the specified object, and <code>false</code> otherwise.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ChartChangeEventType)) {
      return false;
    }
    ChartChangeEventType that=(ChartChangeEventType)obj;
    if (!this.name.equals(that.toString())) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code value for the object.
 * @return The hashcode
 */
  public int hashCode(){
    return this.name.hashCode();
  }
  /** 
 * Ensures that serialization returns the unique instances.
 * @return The object.
 * @throws ObjectStreamException if there is a problem.
 */
  private Object readResolve() throws ObjectStreamException {
    if (this.equals(ChartChangeEventType.GENERAL)) {
      return ChartChangeEventType.GENERAL;
    }
 else {
      if (this.equals(ChartChangeEventType.NEW_DATASET)) {
        return ChartChangeEventType.NEW_DATASET;
      }
 else {
        if (this.equals(ChartChangeEventType.DATASET_UPDATED)) {
          return ChartChangeEventType.DATASET_UPDATED;
        }
      }
    }
    return null;
  }
}
