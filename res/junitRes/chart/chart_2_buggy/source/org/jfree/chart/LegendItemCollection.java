package org.jfree.chart;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.util.ObjectUtilities;
/** 
 * A collection of legend items.
 */
public class LegendItemCollection implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=1365215565589815953L;
  /** 
 * Storage for the legend items. 
 */
  private List items;
  /** 
 * Constructs a new legend item collection, initially empty.
 */
  public LegendItemCollection(){
    this.items=new java.util.ArrayList();
  }
  /** 
 * Adds a legend item to the collection.
 * @param item  the item to add.
 */
  public void add(  LegendItem item){
    this.items.add(item);
  }
  /** 
 * Adds the legend items from another collection to this collection.
 * @param collection  the other collection.
 */
  public void addAll(  LegendItemCollection collection){
    this.items.addAll(collection.items);
  }
  /** 
 * Returns a legend item from the collection.
 * @param index  the legend item index (zero-based).
 * @return The legend item.
 */
  public LegendItem get(  int index){
    return (LegendItem)this.items.get(index);
  }
  /** 
 * Returns the number of legend items in the collection.
 * @return The item count.
 */
  public int getItemCount(){
    return this.items.size();
  }
  /** 
 * Returns an iterator that provides access to all the legend items.
 * @return An iterator.
 */
  public Iterator iterator(){
    return this.items.iterator();
  }
  /** 
 * Tests this collection for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof LegendItemCollection)) {
      return false;
    }
    LegendItemCollection that=(LegendItemCollection)obj;
    if (!this.items.equals(that.items)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a clone of the collection.
 * @return A clone.
 * @throws CloneNotSupportedException if an item in the collection is notcloneable.
 */
  public Object clone() throws CloneNotSupportedException {
    LegendItemCollection clone=(LegendItemCollection)super.clone();
    clone.items=(List)ObjectUtilities.deepClone(this.items);
    return clone;
  }
}
