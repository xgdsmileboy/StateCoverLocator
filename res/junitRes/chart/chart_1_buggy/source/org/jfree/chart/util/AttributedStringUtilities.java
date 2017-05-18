package org.jfree.chart.util;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.CharacterIterator;
import java.util.Map;
/** 
 * Some utility methods for working with <code>AttributedString</code> objects.
 */
public class AttributedStringUtilities {
  /** 
 * Private constructor prevents object creation.
 */
  private AttributedStringUtilities(){
  }
  /** 
 * Tests two attributed strings for equality.
 * @param s1  string 1 (<code>null</code> permitted).
 * @param s2  string 2 (<code>null</code> permitted).
 * @return <code>true</code> if <code>s1</code> and <code>s2</code> areequal or both <code>null</code>, and <code>false</code> otherwise.
 */
  public static boolean equal(  AttributedString s1,  AttributedString s2){
    if (s1 == null) {
      return (s2 == null);
    }
    if (s2 == null) {
      return false;
    }
    AttributedCharacterIterator it1=s1.getIterator();
    AttributedCharacterIterator it2=s2.getIterator();
    char c1=it1.first();
    char c2=it2.first();
    int start=0;
    while (c1 != CharacterIterator.DONE) {
      int limit1=it1.getRunLimit();
      int limit2=it2.getRunLimit();
      if (limit1 != limit2) {
        return false;
      }
      Map m1=it1.getAttributes();
      Map m2=it2.getAttributes();
      if (!m1.equals(m2)) {
        return false;
      }
      for (int i=start; i < limit1; i++) {
        if (c1 != c2) {
          return false;
        }
        c1=it1.next();
        c2=it2.next();
      }
      start=limit1;
    }
    return c2 == CharacterIterator.DONE;
  }
}
