package org.jfree.chart.util;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * An enumeration of the vertical alignment types (<code>TOP</code>, <code>BOTTOM</code> and <code>CENTER</code>).
 */
public final class VerticalAlignment implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=7272397034325429853L;
  /** 
 * Top alignment. 
 */
  public static final VerticalAlignment TOP=new VerticalAlignment("VerticalAlignment.TOP");
  /** 
 * Bottom alignment. 
 */
  public static final VerticalAlignment BOTTOM=new VerticalAlignment("VerticalAlignment.BOTTOM");
  /** 
 * Center alignment. 
 */
  public static final VerticalAlignment CENTER=new VerticalAlignment("VerticalAlignment.CENTER");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private VerticalAlignment(  String name){
    this.name=name;
  }
  /** 
 * Returns a string representing the object.
 * @return the string.
 */
  public String toString(){
    return this.name;
  }
  /** 
 * Returns <code>true</code> if this object is equal to the specified object, and <code>false</code> otherwise.
 * @param obj  the other object.
 * @return a boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof VerticalAlignment)) {
      return false;
    }
    VerticalAlignment alignment=(VerticalAlignment)obj;
    if (!this.name.equals(alignment.name)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code value for the object.
 * @return the hashcode
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
    if (this.equals(VerticalAlignment.TOP)) {
      return VerticalAlignment.TOP;
    }
 else {
      if (this.equals(VerticalAlignment.BOTTOM)) {
        return VerticalAlignment.BOTTOM;
      }
 else {
        if (this.equals(VerticalAlignment.CENTER)) {
          return VerticalAlignment.CENTER;
        }
 else {
          return null;
        }
      }
    }
  }
}
