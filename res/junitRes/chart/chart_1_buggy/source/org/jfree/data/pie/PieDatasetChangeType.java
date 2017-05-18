package org.jfree.data.pie;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * An enumeration of the pie dataset change types.
 * @since 1.2.0
 */
public class PieDatasetChangeType implements Serializable {
  /** 
 * Represents a new dataset.
 */
  public static final PieDatasetChangeType NEW=new PieDatasetChangeType("PieDatasetChangeType.NEW");
  /** 
 * Represents the addition of one or more data items to the series in a contiguous range.
 */
  public static final PieDatasetChangeType ADD=new PieDatasetChangeType("PieDatasetChangeType.ADD");
  /** 
 * Represents the removal of one or more data items in a contiguous range.
 */
  public static final PieDatasetChangeType REMOVE=new PieDatasetChangeType("PieDatasetChangeType.REMOVE");
  /** 
 * Add one item and remove one other item. 
 */
  public static final PieDatasetChangeType ADD_AND_REMOVE=new PieDatasetChangeType("PieDatasetChangeType.ADD_AND_REMOVE");
  /** 
 * Represents a change of value for one or more data items in a contiguous range.
 */
  public static final PieDatasetChangeType UPDATE=new PieDatasetChangeType("PieDatasetChangeType.UPDATE");
  /** 
 * Represents a change to an item key. 
 */
  public static final PieDatasetChangeType CHANGE_KEY=new PieDatasetChangeType("PieDatasetChangeType.ITEM_KEY");
  /** 
 * Represents the clearing of all values in the dataset. 
 */
  public static final PieDatasetChangeType CLEAR=new PieDatasetChangeType("PieDatasetChangeType.CLEAR");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private PieDatasetChangeType(  String name){
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
    if (!(obj instanceof PieDatasetChangeType)) {
      return false;
    }
    PieDatasetChangeType pdct=(PieDatasetChangeType)obj;
    if (!this.name.equals(pdct.toString())) {
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
    if (this.equals(PieDatasetChangeType.ADD)) {
      result=PieDatasetChangeType.ADD;
    }
    return result;
  }
}
