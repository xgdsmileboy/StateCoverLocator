package org.jfree.chart.axis;
import java.io.Serializable;
import java.text.NumberFormat;
/** 
 * A numerical tick unit.
 */
public class NumberTickUnit extends TickUnit implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=3849459506627654442L;
  /** 
 * A formatter for the tick unit. 
 */
  private NumberFormat formatter;
  /** 
 * Creates a new number tick unit.
 * @param size  the size of the tick unit.
 */
  public NumberTickUnit(  double size){
    this(size,NumberFormat.getNumberInstance());
  }
  /** 
 * Creates a new number tick unit.
 * @param size  the size of the tick unit.
 * @param formatter  a number formatter for the tick unit (<code>null</code>not permitted).
 */
  public NumberTickUnit(  double size,  NumberFormat formatter){
    super(size);
    if (formatter == null) {
      throw new IllegalArgumentException("Null 'formatter' argument.");
    }
    this.formatter=formatter;
  }
  /** 
 * Creates a new number tick unit.
 * @param size  the size of the tick unit.
 * @param formatter  a number formatter for the tick unit (<code>null</code>not permitted).
 * @param minorTickCount  the number of minor ticks.
 * @since 1.0.7
 */
  public NumberTickUnit(  double size,  NumberFormat formatter,  int minorTickCount){
    super(size,minorTickCount);
    if (formatter == null) {
      throw new IllegalArgumentException("Null 'formatter' argument.");
    }
    this.formatter=formatter;
  }
  /** 
 * Converts a value to a string.
 * @param value  the value.
 * @return The formatted string.
 */
  public String valueToString(  double value){
    return this.formatter.format(value);
  }
  /** 
 * Tests this formatter for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof NumberTickUnit)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    NumberTickUnit that=(NumberTickUnit)obj;
    if (!this.formatter.equals(that.formatter)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a string representing this unit.
 * @return A string.
 */
  public String toString(){
    return "[size=" + this.valueToString(this.getSize()) + "]";
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=super.hashCode();
    result=29 * result + (this.formatter != null ? this.formatter.hashCode() : 0);
    return result;
  }
}
