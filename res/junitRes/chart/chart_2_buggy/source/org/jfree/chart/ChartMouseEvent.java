package org.jfree.chart;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;
import org.jfree.chart.entity.ChartEntity;
/** 
 * A mouse event for a chart that is displayed in a                                                                                                                                                                {@link ChartPanel}.
 * @see ChartMouseListener
 */
public class ChartMouseEvent extends EventObject implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-682393837314562149L;
  /** 
 * The chart that the mouse event relates to. 
 */
  private JFreeChart chart;
  /** 
 * The Java mouse event that triggered this event. 
 */
  private MouseEvent trigger;
  /** 
 * The chart entity (if any). 
 */
  private ChartEntity entity;
  /** 
 * Constructs a new event.
 * @param chart  the source chart (<code>null</code> not permitted).
 * @param trigger  the mouse event that triggered this event(<code>null</code> not permitted).
 * @param entity  the chart entity (if any) under the mouse point(<code>null</code> permitted).
 */
  public ChartMouseEvent(  JFreeChart chart,  MouseEvent trigger,  ChartEntity entity){
    super(chart);
    this.chart=chart;
    this.trigger=trigger;
    this.entity=entity;
  }
  /** 
 * Returns the chart that the mouse event relates to.
 * @return The chart (never <code>null</code>).
 */
  public JFreeChart getChart(){
    return this.chart;
  }
  /** 
 * Returns the mouse event that triggered this event.
 * @return The event (never <code>null</code>).
 */
  public MouseEvent getTrigger(){
    return this.trigger;
  }
  /** 
 * Returns the chart entity (if any) under the mouse point.
 * @return The chart entity (possibly <code>null</code>).
 */
  public ChartEntity getEntity(){
    return this.entity;
  }
}
