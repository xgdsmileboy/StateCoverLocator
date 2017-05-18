package org.jfree.data.general;
import java.io.Serializable;
/** 
 * A class that is used to group datasets (currently not used for any specific purpose).
 */
public class DatasetGroup implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-3640642179674185688L;
  /** 
 * The group id. 
 */
  private String id;
  /** 
 * Constructs a new group.
 */
  public DatasetGroup(){
    super();
    this.id="NOID";
  }
  /** 
 * Creates a new group with the specified id.
 * @param id  the identification for the group.
 */
  public DatasetGroup(  String id){
    if (id == null) {
      throw new IllegalArgumentException("Null 'id' argument.");
    }
    this.id=id;
  }
  /** 
 * Returns the identification string for this group.
 * @return The identification string.
 */
  public String getID(){
    return this.id;
  }
  /** 
 * Clones the group.
 * @return A clone.
 * @throws CloneNotSupportedException not by this class.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  /** 
 * Tests this instance for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof DatasetGroup)) {
      return false;
    }
    DatasetGroup that=(DatasetGroup)obj;
    if (!this.id.equals(that.id)) {
      return false;
    }
    return true;
  }
}
