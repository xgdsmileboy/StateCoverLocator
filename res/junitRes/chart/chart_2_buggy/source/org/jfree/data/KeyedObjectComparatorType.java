package org.jfree.data;
/** 
 * Used to indicate the type of a                                                                                                                                                                    {@link KeyedObjectComparator} : 'by key' or'by object'.
 */
public final class KeyedObjectComparatorType {
  /** 
 * An object representing 'by key' sorting. 
 */
  public static final KeyedObjectComparatorType BY_KEY=new KeyedObjectComparatorType("KeyedObjectComparatorType.BY_KEY");
  /** 
 * An object representing 'by value' sorting. 
 */
  public static final KeyedObjectComparatorType BY_VALUE=new KeyedObjectComparatorType("KeyedObjectComparatorType.BY_VALUE");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private KeyedObjectComparatorType(  String name){
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
    if (!(obj instanceof KeyedObjectComparatorType)) {
      return false;
    }
    KeyedObjectComparatorType type=(KeyedObjectComparatorType)obj;
    if (!this.name.equals(type.name)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code.
 * @return A hash code.
 */
  public int hashCode(){
    return this.name.hashCode();
  }
}
