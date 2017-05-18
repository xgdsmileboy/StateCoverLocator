package org.jfree.data.general;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * An enumeration of the series change types.
 * @since 1.2.0
 */
public class SeriesChangeType implements Serializable {
  /** 
 * Represents a change to the series key. 
 */
  public static final SeriesChangeType CHANGE_KEY=new SeriesChangeType("SeriesChangeType.CHANGE_KEY");
  /** 
 * Represents the addition of one or more data items to the series in a contiguous range.
 */
  public static final SeriesChangeType ADD=new SeriesChangeType("SeriesChangeType.ADD");
  /** 
 * Represents the removal of one or more data items in a contiguous range.
 */
  public static final SeriesChangeType REMOVE=new SeriesChangeType("SeriesChangeType.REMOVE");
  /** 
 * Add one item and remove one other item. 
 */
  public static final SeriesChangeType ADD_AND_REMOVE=new SeriesChangeType("SeriesChangeType.ADD_AND_REMOVE");
  /** 
 * Represents a change of value for one or more data items in a contiguous range.
 */
  public static final SeriesChangeType UPDATE=new SeriesChangeType("SeriesChangeType.UPDATE");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private SeriesChangeType(  String name){
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
    if (!(obj instanceof SeriesChangeType)) {
      return false;
    }
    SeriesChangeType style=(SeriesChangeType)obj;
    if (!this.name.equals(style.toString())) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
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
    Object result=null;
    if (this.equals(SeriesChangeType.ADD)) {
      result=SeriesChangeType.ADD;
    }
    return result;
  }
}
