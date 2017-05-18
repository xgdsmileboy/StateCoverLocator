package org.jfree.data.statistics;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * A class for creating constants to represent the histogram type.  See Bloch's enum tip in 'Effective Java'.
 */
public class HistogramType implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=2618927186251997727L;
  /** 
 * Frequency histogram. 
 */
  public static final HistogramType FREQUENCY=new HistogramType("FREQUENCY");
  /** 
 * Relative frequency. 
 */
  public static final HistogramType RELATIVE_FREQUENCY=new HistogramType("RELATIVE_FREQUENCY");
  /** 
 * Scale area to one. 
 */
  public static final HistogramType SCALE_AREA_TO_1=new HistogramType("SCALE_AREA_TO_1");
  /** 
 * The type name. 
 */
  private String name;
  /** 
 * Creates a new type.
 * @param name  the name.
 */
  private HistogramType(  String name){
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
 * Tests this type for equality with an arbitrary object.
 * @param obj  the object to test against.
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof HistogramType)) {
      return false;
    }
    HistogramType t=(HistogramType)obj;
    if (!this.name.equals(t.name)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code value for the object.
 * @return The hashcode
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
    if (this.equals(HistogramType.FREQUENCY)) {
      return HistogramType.FREQUENCY;
    }
 else {
      if (this.equals(HistogramType.RELATIVE_FREQUENCY)) {
        return HistogramType.RELATIVE_FREQUENCY;
      }
 else {
        if (this.equals(HistogramType.SCALE_AREA_TO_1)) {
          return HistogramType.SCALE_AREA_TO_1;
        }
      }
    }
    return null;
  }
}
