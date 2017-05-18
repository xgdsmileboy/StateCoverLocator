package org.jfree.chart.axis;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Represents the width types for a category label.
 */
public final class CategoryLabelWidthType implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-6976024792582949656L;
  /** 
 * Percentage of category. 
 */
  public static final CategoryLabelWidthType CATEGORY=new CategoryLabelWidthType("CategoryLabelWidthType.CATEGORY");
  /** 
 * Percentage of range. 
 */
  public static final CategoryLabelWidthType RANGE=new CategoryLabelWidthType("CategoryLabelWidthType.RANGE");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name (<code>null</code> not permitted).
 */
  private CategoryLabelWidthType(  String name){
    if (name == null) {
      throw new IllegalArgumentException("Null 'name' argument.");
    }
    this.name=name;
  }
  /** 
 * Returns a string representing the object.
 * @return The string (never </code>null</code>).
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
    if (!(obj instanceof CategoryLabelWidthType)) {
      return false;
    }
    CategoryLabelWidthType t=(CategoryLabelWidthType)obj;
    if (!this.name.equals(t.toString())) {
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
    if (this.equals(CategoryLabelWidthType.CATEGORY)) {
      return CategoryLabelWidthType.CATEGORY;
    }
 else {
      if (this.equals(CategoryLabelWidthType.RANGE)) {
        return CategoryLabelWidthType.RANGE;
      }
    }
    return null;
  }
}
