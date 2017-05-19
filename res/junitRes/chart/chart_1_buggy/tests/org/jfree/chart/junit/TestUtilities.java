package org.jfree.chart.junit;
import java.util.Collection;
import java.util.Iterator;
/** 
 * Some utility methods for use by the testing code.
 */
public class TestUtilities {
  /** 
 * Returns <code>true</code> if the collections contains any object that is an instance of the specified class, and <code>false</code> otherwise.
 * @param collection  the collection.
 * @param c  the class.
 * @return A boolean.
 */
  public static boolean containsInstanceOf(  Collection collection,  Class c){
    Iterator iterator=collection.iterator();
    while (iterator.hasNext()) {
      Object obj=iterator.next();
      if (obj != null && obj.getClass().equals(c)) {
        return true;
      }
    }
    return false;
  }
}
