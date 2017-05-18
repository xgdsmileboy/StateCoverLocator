package org.jfree.chart.util;
/** 
 * A list of objects that can grow as required. <p> When cloning, the objects in the list are NOT cloned, only the references.
 */
public class ObjectList extends AbstractObjectList {
  /** 
 * Default constructor.
 */
  public ObjectList(){
  }
  /** 
 * Creates a new list.
 * @param initialCapacity  the initial capacity.
 */
  public ObjectList(  int initialCapacity){
    super(initialCapacity);
  }
  /** 
 * Returns the object at the specified index, if there is one, or <code>null</code>.
 * @param index  the object index.
 * @return The object or <code>null</code>.
 */
  public Object get(  int index){
    return super.get(index);
  }
  /** 
 * Sets an object reference (overwriting any existing object).
 * @param index  the object index.
 * @param object  the object (<code>null</code> permitted).
 */
  public void set(  int index,  Object object){
    super.set(index,object);
  }
  /** 
 * Returns the index of the specified object, or -1 if the object is not in the list.
 * @param object  the object.
 * @return The index or -1.
 */
  public int indexOf(  Object object){
    return super.indexOf(object);
  }
}
