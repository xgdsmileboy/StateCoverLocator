package org.jfree.chart.axis;
import java.io.Serializable;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.util.ObjectUtilities;
/** 
 * The base class used to represent labelled ticks along an axis.
 */
public abstract class Tick implements Serializable, Cloneable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=6668230383875149773L;
  /** 
 * A text version of the tick value. 
 */
  private String text;
  /** 
 * The text anchor for the tick label. 
 */
  private TextAnchor textAnchor;
  /** 
 * The rotation anchor for the tick label. 
 */
  private TextAnchor rotationAnchor;
  /** 
 * The rotation angle. 
 */
  private double angle;
  /** 
 * Creates a new tick.
 * @param text  the formatted version of the tick value.
 * @param textAnchor  the text anchor (<code>null</code> not permitted).
 * @param rotationAnchor  the rotation anchor (<code>null</code> notpermitted).
 * @param angle  the angle.
 */
  public Tick(  String text,  TextAnchor textAnchor,  TextAnchor rotationAnchor,  double angle){
    if (textAnchor == null) {
      throw new IllegalArgumentException("Null 'textAnchor' argument.");
    }
    if (rotationAnchor == null) {
      throw new IllegalArgumentException("Null 'rotationAnchor' argument.");
    }
    this.text=text;
    this.textAnchor=textAnchor;
    this.rotationAnchor=rotationAnchor;
    this.angle=angle;
  }
  /** 
 * Returns the text version of the tick value.
 * @return A string (possibly <code>null</code>;
 */
  public String getText(){
    return this.text;
  }
  /** 
 * Returns the text anchor.
 * @return The text anchor (never <code>null</code>).
 */
  public TextAnchor getTextAnchor(){
    return this.textAnchor;
  }
  /** 
 * Returns the text anchor that defines the point around which the label is rotated.
 * @return A text anchor (never <code>null</code>).
 */
  public TextAnchor getRotationAnchor(){
    return this.rotationAnchor;
  }
  /** 
 * Returns the angle.
 * @return The angle.
 */
  public double getAngle(){
    return this.angle;
  }
  /** 
 * Tests this tick for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (obj instanceof Tick) {
      Tick t=(Tick)obj;
      if (!ObjectUtilities.equal(this.text,t.text)) {
        return false;
      }
      if (!ObjectUtilities.equal(this.textAnchor,t.textAnchor)) {
        return false;
      }
      if (!ObjectUtilities.equal(this.rotationAnchor,t.rotationAnchor)) {
        return false;
      }
      if (!(this.angle == t.angle)) {
        return false;
      }
      return true;
    }
    return false;
  }
  /** 
 * Returns a clone of the tick.
 * @return A clone.
 * @throws CloneNotSupportedException if there is a problem cloning.
 */
  public Object clone() throws CloneNotSupportedException {
    Tick clone=(Tick)super.clone();
    return clone;
  }
  /** 
 * Returns a string representation of the tick.
 * @return A string.
 */
  public String toString(){
    return this.text;
  }
}
