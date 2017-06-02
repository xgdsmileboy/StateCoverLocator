package org.jfree.data;
import java.io.Serializable;
import org.jfree.chart.util.PublicCloneable;
/** 
 * A (key, value) pair.  This class provides a default implementation of the                                                                                                                                                                {@link KeyedValue} interface.
 */
public class DefaultKeyedValue implements KeyedValue, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-7388924517460437712L;
  /** 
 * The key. 
 */
  private Comparable key;
  /** 
 * The value. 
 */
  private Number value;
  /** 
 * Creates a new (key, value) item.
 * @param key  the key (should be immutable, <code>null</code> notpermitted).
 * @param value  the value (<code>null</code> permitted).
 */
  public DefaultKeyedValue(  Comparable key,  Number value){
    if (key == null) {
      throw new IllegalArgumentException("Null 'key' argument.");
    }
    this.key=key;
    this.value=value;
  }
  /** 
 * Returns the key.
 * @return The key (never <code>null</code>).
 */
  public Comparable getKey(){
    return this.key;
  }
  /** 
 * Returns the value.
 * @return The value (possibly <code>null</code>).
 */
  public Number getValue(){
    return this.value;
  }
  /** 
 * Sets the value.
 * @param value  the value (<code>null</code> permitted).
 */
  public synchronized void setValue(  Number value){
    this.value=value;
  }
  /** 
 * Tests this key-value pair for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof DefaultKeyedValue)) {
      return false;
    }
    DefaultKeyedValue that=(DefaultKeyedValue)obj;
    if (!this.key.equals(that.key)) {
      return false;
    }
    if (this.value != null ? !this.value.equals(that.value) : that.value != null) {
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
    result=(this.key != null ? this.key.hashCode() : 0);
    result=29 * result + (this.value != null ? this.value.hashCode() : 0);
    return result;
  }
  /** 
 * Returns a clone.  It is assumed that both the key and value are immutable objects, so only the references are cloned, not the objects themselves.
 * @return A clone.
 * @throws CloneNotSupportedException Not thrown by this class, butsubclasses (if any) might.
 */
  public Object clone() throws CloneNotSupportedException {
    DefaultKeyedValue clone=(DefaultKeyedValue)super.clone();
    return clone;
  }
  /** 
 * Returns a string representing this instance, primarily useful for debugging.
 * @return A string.
 */
  public String toString(){
    return "(" + this.key.toString() + ", "+ this.value.toString()+ ")";
  }
}
