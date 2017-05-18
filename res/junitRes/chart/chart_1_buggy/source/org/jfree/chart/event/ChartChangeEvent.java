package org.jfree.chart.event;
import java.util.EventObject;
import org.jfree.chart.JFreeChart;
/** 
 * A change event that encapsulates information about a change to a chart.
 */
public class ChartChangeEvent extends EventObject {
  /** 
 * The type of event. 
 */
  private ChartChangeEventType type;
  /** 
 * The chart that generated the event. 
 */
  private JFreeChart chart;
  /** 
 * Creates a new chart change event.
 * @param source  the source of the event (could be the chart, a title,an axis etc.)
 */
  public ChartChangeEvent(  Object source){
    this(source,null,ChartChangeEventType.GENERAL);
  }
  /** 
 * Creates a new chart change event.
 * @param source  the source of the event (could be the chart, a title, anaxis etc.)
 * @param chart  the chart that generated the event.
 */
  public ChartChangeEvent(  Object source,  JFreeChart chart){
    this(source,chart,ChartChangeEventType.GENERAL);
  }
  /** 
 * Creates a new chart change event.
 * @param source  the source of the event (could be the chart, a title, anaxis etc.)
 * @param chart  the chart that generated the event.
 * @param type  the type of event.
 */
  public ChartChangeEvent(  Object source,  JFreeChart chart,  ChartChangeEventType type){
    super(source);
    this.chart=chart;
    this.type=type;
  }
  /** 
 * Returns the chart that generated the change event.
 * @return The chart that generated the change event.
 */
  public JFreeChart getChart(){
    return this.chart;
  }
  /** 
 * Sets the chart that generated the change event.
 * @param chart  the chart that generated the event.
 */
  public void setChart(  JFreeChart chart){
    this.chart=chart;
  }
  /** 
 * Returns the event type.
 * @return The event type.
 */
  public ChartChangeEventType getType(){
    return this.type;
  }
  /** 
 * Sets the event type.
 * @param type  the event type.
 */
  public void setType(  ChartChangeEventType type){
    this.type=type;
  }
}
