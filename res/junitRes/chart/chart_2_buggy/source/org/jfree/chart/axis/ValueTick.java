package org.jfree.chart.axis;
import org.jfree.chart.text.TextAnchor;
/** 
 * A value tick.
 */
public abstract class ValueTick extends Tick {
  /** 
 * The value. 
 */
  private double value;
  /** 
 * The tick type (major or minor).
 * @since 1.0.7
 */
  private TickType tickType;
  /** 
 * Creates a new value tick.
 * @param value  the value.
 * @param label  the label.
 * @param textAnchor  the part of the label that is aligned to the anchorpoint.
 * @param rotationAnchor  defines the rotation point relative to the label.
 * @param angle  the rotation angle (in radians).
 */
  public ValueTick(  double value,  String label,  TextAnchor textAnchor,  TextAnchor rotationAnchor,  double angle){
    this(TickType.MAJOR,value,label,textAnchor,rotationAnchor,angle);
    this.value=value;
  }
  /** 
 * Creates a new value tick.
 * @param tickType  the tick type (major or minor).
 * @param value  the value.
 * @param label  the label.
 * @param textAnchor  the part of the label that is aligned to the anchorpoint.
 * @param rotationAnchor  defines the rotation point relative to the label.
 * @param angle  the rotation angle (in radians).
 * @since 1.0.7
 */
  public ValueTick(  TickType tickType,  double value,  String label,  TextAnchor textAnchor,  TextAnchor rotationAnchor,  double angle){
    super(label,textAnchor,rotationAnchor,angle);
    this.value=value;
    this.tickType=tickType;
  }
  /** 
 * Returns the value.
 * @return The value.
 */
  public double getValue(){
    return this.value;
  }
  /** 
 * Returns the tick type (major or minor).
 * @return The tick type.
 * @since 1.0.7
 */
  public TickType getTickType(){
    return this.tickType;
  }
  /** 
 * Tests this tick for equality with an arbitrary object.
 * @param obj  the object to test (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ValueTick)) {
      return false;
    }
    ValueTick that=(ValueTick)obj;
    if (this.value != that.value) {
      return false;
    }
    if (!this.tickType.equals(that.tickType)) {
      return false;
    }
    return super.equals(obj);
  }
}
