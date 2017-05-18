package org.jfree.chart.plot;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Used to indicate the style for the lines linking pie sections to their corresponding labels.
 * @since 1.0.10
 */
public final class PieLabelLinkStyle implements Serializable {
  /** 
 * STANDARD. 
 */
  public static final PieLabelLinkStyle STANDARD=new PieLabelLinkStyle("PieLabelLinkStyle.STANDARD");
  /** 
 * QUAD_CURVE. 
 */
  public static final PieLabelLinkStyle QUAD_CURVE=new PieLabelLinkStyle("PieLabelLinkStyle.QUAD_CURVE");
  /** 
 * CUBIC_CURVE. 
 */
  public static final PieLabelLinkStyle CUBIC_CURVE=new PieLabelLinkStyle("PieLabelLinkStyle.CUBIC_CURVE");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private PieLabelLinkStyle(  String name){
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
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PieLabelLinkStyle)) {
      return false;
    }
    PieLabelLinkStyle style=(PieLabelLinkStyle)obj;
    if (!this.name.equals(style.toString())) {
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
    Object result=null;
    if (this.equals(PieLabelLinkStyle.STANDARD)) {
      result=PieLabelLinkStyle.STANDARD;
    }
 else {
      if (this.equals(PieLabelLinkStyle.QUAD_CURVE)) {
        result=PieLabelLinkStyle.QUAD_CURVE;
      }
 else {
        if (this.equals(PieLabelLinkStyle.CUBIC_CURVE)) {
          result=PieLabelLinkStyle.CUBIC_CURVE;
        }
      }
    }
    return result;
  }
}
