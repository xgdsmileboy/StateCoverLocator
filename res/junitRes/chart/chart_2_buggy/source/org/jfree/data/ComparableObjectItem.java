package org.jfree.data;
import java.io.Serializable;
import org.jfree.chart.util.ObjectUtilities;
/** 
 * Represents one (Comparable, Object) data item for use in a                                                                                                                                                                    {@link ComparableObjectSeries}.
 * @since 1.0.3
 */
public class ComparableObjectItem implements Cloneable, Comparable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=2751513470325494890L;
  /** 
 * The x-value. 
 */
  private Comparable x;
  /** 
 * The y-value. 
 */
  private Object obj;
  /** 
 * Constructs a new data item.
 * @param x  the x-value (<code>null</code> NOT permitted).
 * @param y  the y-value (<code>null</code> permitted).
 */
  public ComparableObjectItem(  Comparable x,  Object y){
    if (x == null) {
      throw new IllegalArgumentException("Null 'x' argument.");
    }
    this.x=x;
    this.obj=y;
  }
  /** 
 * Returns the x-value.
 * @return The x-value (never <code>null</code>).
 */
  protected Comparable getComparable(){
    return this.x;
  }
  /** 
 * Returns the y-value.
 * @return The y-value (possibly <code>null</code>).
 */
  protected Object getObject(){
    return this.obj;
  }
  /** 
 * Sets the y-value for this data item.  Note that there is no corresponding method to change the x-value.
 * @param y  the new y-value (<code>null</code> permitted).
 */
  protected void setObject(  Object y){
    this.obj=y;
  }
  /** 
 * Returns an integer indicating the order of this object relative to another object. <P> For the order we consider only the x-value: negative == "less-than", zero == "equal", positive == "greater-than".
 * @param o1  the object being compared to.
 * @return An integer indicating the order of this data pair objectrelative to another object.
 */
  public int compareTo(  Object o1){
    int result;
    if (o1 instanceof ComparableObjectItem) {
      ComparableObjectItem that=(ComparableObjectItem)o1;
      return this.x.compareTo(that.x);
    }
 else {
      result=1;
    }
    return result;
  }
  /** 
 * Returns a clone of this object.
 * @return A clone.
 * @throws CloneNotSupportedException not thrown by this class, butsubclasses may differ.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  /** 
 * Tests if this object is equal to another.
 * @param obj  the object to test against for equality (<code>null</code>permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ComparableObjectItem)) {
      return false;
    }
    ComparableObjectItem that=(ComparableObjectItem)obj;
    if (!this.x.equals(that.x)) {
      return false;
    }
    if (!ObjectUtilities.equal(this.obj,that.obj)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code.
 * @return A hash code.
 */
  public int hashCode(){
    int result;
    result=this.x.hashCode();
    result=29 * result + (this.obj != null ? this.obj.hashCode() : 0);
    return result;
  }
}
