package org.jfree.data;
import java.util.Comparator;
import org.jfree.chart.util.SortOrder;
/** 
 * A utility class that can compare and order two                                                                                               {@link KeyedValue} instancesand sort them into ascending or descending order by key or by value.
 */
public class KeyedValueComparator implements Comparator {
  /** 
 * The comparator type. 
 */
  private KeyedValueComparatorType type;
  /** 
 * The sort order. 
 */
  private SortOrder order;
  /** 
 * Creates a new comparator.
 * @param type  the type (<code>BY_KEY</code> or <code>BY_VALUE</code>,<code>null</code> not permitted).
 * @param order  the order (<code>null</code> not permitted).
 */
  public KeyedValueComparator(  KeyedValueComparatorType type,  SortOrder order){
    if (order == null) {
      throw new IllegalArgumentException("Null 'order' argument.");
    }
    this.type=type;
    this.order=order;
  }
  /** 
 * Returns the type.
 * @return The type (never <code>null</code>).
 */
  public KeyedValueComparatorType getType(){
    return this.type;
  }
  /** 
 * Returns the sort order.
 * @return The sort order (never <code>null</code>).
 */
  public SortOrder getOrder(){
    return this.order;
  }
  /** 
 * Compares two                                                                                               {@link KeyedValue} instances and returns an<code>int</code> that indicates the relative order of the two objects.
 * @param o1  object 1.
 * @param o2  object 2.
 * @return An int indicating the relative order of the objects.
 */
  public int compare(  Object o1,  Object o2){
    if (o2 == null) {
      return -1;
    }
    if (o1 == null) {
      return 1;
    }
    int result;
    KeyedValue kv1=(KeyedValue)o1;
    KeyedValue kv2=(KeyedValue)o2;
    if (this.type == KeyedValueComparatorType.BY_KEY) {
      if (this.order.equals(SortOrder.ASCENDING)) {
        result=kv1.getKey().compareTo(kv2.getKey());
      }
 else {
        if (this.order.equals(SortOrder.DESCENDING)) {
          result=kv2.getKey().compareTo(kv1.getKey());
        }
 else {
          throw new IllegalArgumentException("Unrecognised sort order.");
        }
      }
    }
 else {
      if (this.type == KeyedValueComparatorType.BY_VALUE) {
        Number n1=kv1.getValue();
        Number n2=kv2.getValue();
        if (n2 == null) {
          return -1;
        }
        if (n1 == null) {
          return 1;
        }
        double d1=n1.doubleValue();
        double d2=n2.doubleValue();
        if (this.order.equals(SortOrder.ASCENDING)) {
          if (d1 > d2) {
            result=1;
          }
 else {
            if (d1 < d2) {
              result=-1;
            }
 else {
              result=0;
            }
          }
        }
 else {
          if (this.order.equals(SortOrder.DESCENDING)) {
            if (d1 > d2) {
              result=-1;
            }
 else {
              if (d1 < d2) {
                result=1;
              }
 else {
                result=0;
              }
            }
          }
 else {
            throw new IllegalArgumentException("Unrecognised sort order.");
          }
        }
      }
 else {
        throw new IllegalArgumentException("Unrecognised type.");
      }
    }
    return result;
  }
}
