package org.jfree.data;
/** 
 * Used to indicate the type of a                                                                                               {@link KeyedValueComparator} : 'by key' or'by value'.
 */
public final class KeyedValueComparatorType {
  /** 
 * An object representing 'by key' sorting. 
 */
  public static final KeyedValueComparatorType BY_KEY=new KeyedValueComparatorType("KeyedValueComparatorType.BY_KEY");
  /** 
 * An object representing 'by value' sorting. 
 */
  public static final KeyedValueComparatorType BY_VALUE=new KeyedValueComparatorType("KeyedValueComparatorType.BY_VALUE");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private KeyedValueComparatorType(  String name){
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
 * @param o  the other object.
 * @return A boolean.
 */
  public boolean equals(  Object o){
    if (this == o) {
      return true;
    }
    if (!(o instanceof KeyedValueComparatorType)) {
      return false;
    }
    KeyedValueComparatorType type=(KeyedValueComparatorType)o;
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
