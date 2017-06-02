package org.jfree.chart.plot;
import java.awt.Paint;
import java.awt.Stroke;
import org.jfree.chart.event.MarkerChangeEvent;
/** 
 * A marker that represents a single value.  Markers can be added to plots to highlight specific values.
 */
public class ValueMarker extends Marker {
  /** 
 * The value. 
 */
  private double value;
  /** 
 * Creates a new marker.
 * @param value  the value.
 */
  public ValueMarker(  double value){
    super();
    this.value=value;
  }
  /** 
 * Creates a new marker.
 * @param value  the value.
 * @param paint  the paint (<code>null</code> not permitted).
 * @param stroke  the stroke (<code>null</code> not permitted).
 */
  public ValueMarker(  double value,  Paint paint,  Stroke stroke){
    this(value,paint,stroke,paint,stroke,1.0f);
  }
  /** 
 * Creates a new value marker.
 * @param value  the value.
 * @param paint  the paint (<code>null</code> not permitted).
 * @param stroke  the stroke (<code>null</code> not permitted).
 * @param outlinePaint  the outline paint (<code>null</code> permitted).
 * @param outlineStroke  the outline stroke (<code>null</code> permitted).
 * @param alpha  the alpha transparency (in the range 0.0f to 1.0f).
 */
  public ValueMarker(  double value,  Paint paint,  Stroke stroke,  Paint outlinePaint,  Stroke outlineStroke,  float alpha){
    super(paint,stroke,outlinePaint,outlineStroke,alpha);
    this.value=value;
  }
  /** 
 * Returns the value.
 * @return The value.
 * @see #setValue(double)
 */
  public double getValue(){
    return this.value;
  }
  /** 
 * Sets the value for the marker and sends a                                                                                                                                                                {@link MarkerChangeEvent} toall registered listeners.
 * @param value  the value.
 * @see #getValue()
 * @since 1.0.3
 */
  public void setValue(  double value){
    this.value=value;
    notifyListeners(new MarkerChangeEvent(this));
  }
  /** 
 * Tests this marker for equality with an arbitrary object.  This method returns <code>true</code> if: <ul> <li><code>obj</code> is not <code>null</code>;</li> <li><code>obj</code> is an instance of <code>ValueMarker</code>;</li> <li><code>obj</code> has the same value as this marker;</li> <li><code>super.equals(obj)</code> returns <code>true</code>.</li> </ul>
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof ValueMarker)) {
      return false;
    }
    ValueMarker that=(ValueMarker)obj;
    if (this.value != that.value) {
      return false;
    }
    return true;
  }
}
