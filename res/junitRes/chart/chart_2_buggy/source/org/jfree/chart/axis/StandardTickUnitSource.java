package org.jfree.chart.axis;
import java.io.Serializable;
import java.text.DecimalFormat;
/** 
 * A source that can used by the                                                                                               {@link NumberAxis} class to obtain asuitable  {@link TickUnit}.  Instances of this class are                                                                                               {@link Serializable}from version 1.0.7 onwards.  Cloning is not supported, because instances are immutable.
 */
public class StandardTickUnitSource implements TickUnitSource, Serializable {
  /** 
 * Constant for log(10.0). 
 */
  private static final double LOG_10_VALUE=Math.log(10.0);
  /** 
 * Default constructor.
 */
  public StandardTickUnitSource(){
    super();
  }
  /** 
 * Returns a tick unit that is larger than the supplied unit.
 * @param unit  the unit (<code>null</code> not permitted).
 * @return A tick unit that is larger than the supplied unit.
 */
  public TickUnit getLargerTickUnit(  TickUnit unit){
    double x=unit.getSize();
    double log=Math.log(x) / LOG_10_VALUE;
    double higher=Math.ceil(log);
    return new NumberTickUnit(Math.pow(10,higher),new DecimalFormat("0.0E0"));
  }
  /** 
 * Returns the tick unit in the collection that is greater than or equal to (in size) the specified unit.
 * @param unit  the unit (<code>null</code> not permitted).
 * @return A unit from the collection.
 */
  public TickUnit getCeilingTickUnit(  TickUnit unit){
    return getLargerTickUnit(unit);
  }
  /** 
 * Returns the tick unit in the collection that is greater than or equal to the specified size.
 * @param size  the size.
 * @return A unit from the collection.
 */
  public TickUnit getCeilingTickUnit(  double size){
    double log=Math.log(size) / LOG_10_VALUE;
    double higher=Math.ceil(log);
    return new NumberTickUnit(Math.pow(10,higher),new DecimalFormat("0.0E0"));
  }
  /** 
 * Tests this instance for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    return (obj instanceof StandardTickUnitSource);
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    return 0;
  }
}
