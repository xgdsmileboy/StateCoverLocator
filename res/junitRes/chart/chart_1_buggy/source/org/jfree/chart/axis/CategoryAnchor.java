package org.jfree.chart.axis;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Used to indicate one of three positions within a category: <code>START</code>, <code>MIDDLE</code> and <code>END</code>.
 */
public final class CategoryAnchor implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-2604142742210173810L;
  /** 
 * Start of period. 
 */
  public static final CategoryAnchor START=new CategoryAnchor("CategoryAnchor.START");
  /** 
 * Middle of period. 
 */
  public static final CategoryAnchor MIDDLE=new CategoryAnchor("CategoryAnchor.MIDDLE");
  /** 
 * End of period. 
 */
  public static final CategoryAnchor END=new CategoryAnchor("CategoryAnchor.END");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private CategoryAnchor(  String name){
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
    if (!(obj instanceof CategoryAnchor)) {
      return false;
    }
    CategoryAnchor position=(CategoryAnchor)obj;
    if (!this.name.equals(position.toString())) {
      return false;
    }
    return true;
  }
  /** 
 * Ensures that serialization returns the unique instances.
 * @return The object.
 * @throws ObjectStreamException if there is a problem.
 */
  private Object readResolve() throws ObjectStreamException {
    if (this.equals(CategoryAnchor.START)) {
      return CategoryAnchor.START;
    }
 else {
      if (this.equals(CategoryAnchor.MIDDLE)) {
        return CategoryAnchor.MIDDLE;
      }
 else {
        if (this.equals(CategoryAnchor.END)) {
          return CategoryAnchor.END;
        }
      }
    }
    return null;
  }
}
