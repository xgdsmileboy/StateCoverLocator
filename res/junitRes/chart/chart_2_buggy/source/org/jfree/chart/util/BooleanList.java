package org.jfree.chart.util;
/** 
 * A list of <code>Boolean</code> objects.
 */
public class BooleanList extends AbstractObjectList {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-8543170333219422042L;
  /** 
 * Creates a new list.
 */
  public BooleanList(){
  }
  /** 
 * Returns a                                                                                                                                                                {@link Boolean} from the list.
 * @param index the index (zero-based).
 * @return a {@link Boolean} from the list.
 */
  public Boolean getBoolean(  int index){
    return (Boolean)get(index);
  }
  /** 
 * Sets the value for an item in the list.  The list is expanded if necessary.
 * @param index  the index (zero-based).
 * @param b  the boolean.
 */
  public void setBoolean(  int index,  Boolean b){
    set(index,b);
  }
  /** 
 * Tests the list for equality with another object (typically also a list).
 * @param obj  the other object.
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj instanceof BooleanList) {
      return super.equals(obj);
    }
    return false;
  }
  /** 
 * Returns a hash code value for the object.
 * @return The hashcode.
 */
  public int hashCode(){
    return super.hashCode();
  }
}
