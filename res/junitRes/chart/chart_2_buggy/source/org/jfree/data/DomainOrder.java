package org.jfree.data;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Used to indicate sorting order if any (ascending, descending or none).
 */
public final class DomainOrder implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=4902774943512072627L;
  /** 
 * No order. 
 */
  public static final DomainOrder NONE=new DomainOrder("DomainOrder.NONE");
  /** 
 * Ascending order. 
 */
  public static final DomainOrder ASCENDING=new DomainOrder("DomainOrder.ASCENDING");
  /** 
 * Descending order. 
 */
  public static final DomainOrder DESCENDING=new DomainOrder("DomainOrder.DESCENDING");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private DomainOrder(  String name){
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
 * @param obj  the other object.
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof DomainOrder)) {
      return false;
    }
    DomainOrder that=(DomainOrder)obj;
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
    if (this.equals(DomainOrder.ASCENDING)) {
      return DomainOrder.ASCENDING;
    }
 else {
      if (this.equals(DomainOrder.DESCENDING)) {
        return DomainOrder.DESCENDING;
      }
 else {
        if (this.equals(DomainOrder.NONE)) {
          return DomainOrder.NONE;
        }
      }
    }
    return null;
  }
}
