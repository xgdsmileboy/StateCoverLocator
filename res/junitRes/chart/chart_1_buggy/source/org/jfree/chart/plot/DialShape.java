package org.jfree.chart.plot;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Used to indicate the background shape for a     {@link org.jfree.chart.plot.MeterPlot}.
 */
public final class DialShape implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-3471933055190251131L;
  /** 
 * Circle. 
 */
  public static final DialShape CIRCLE=new DialShape("DialShape.CIRCLE");
  /** 
 * Chord. 
 */
  public static final DialShape CHORD=new DialShape("DialShape.CHORD");
  /** 
 * Pie. 
 */
  public static final DialShape PIE=new DialShape("DialShape.PIE");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private DialShape(  String name){
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
    if (!(obj instanceof DialShape)) {
      return false;
    }
    DialShape shape=(DialShape)obj;
    if (!this.name.equals(shape.toString())) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
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
    if (this.equals(DialShape.CIRCLE)) {
      return DialShape.CIRCLE;
    }
 else {
      if (this.equals(DialShape.CHORD)) {
        return DialShape.CHORD;
      }
 else {
        if (this.equals(DialShape.PIE)) {
          return DialShape.PIE;
        }
      }
    }
    return null;
  }
}
