package org.jfree.chart.event;
import org.jfree.chart.axis.Axis;
/** 
 * A change event that encapsulates information about a change to an axis.
 */
public class AxisChangeEvent extends ChartChangeEvent {
  /** 
 * The axis that generated the change event. 
 */
  private Axis axis;
  /** 
 * Creates a new AxisChangeEvent.
 * @param axis  the axis that generated the event.
 */
  public AxisChangeEvent(  Axis axis){
    super(axis);
    this.axis=axis;
  }
  /** 
 * Returns the axis that generated the event.
 * @return The axis that generated the event.
 */
  public Axis getAxis(){
    return this.axis;
  }
}
