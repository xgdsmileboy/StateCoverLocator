package org.jfree.chart.event;
import org.jfree.chart.JFreeChart;
/** 
 * An event that contains information about the drawing progress of a chart.
 */
public class ChartProgressEvent extends java.util.EventObject {
  /** 
 * Indicates drawing has started. 
 */
  public static final int DRAWING_STARTED=1;
  /** 
 * Indicates drawing has finished. 
 */
  public static final int DRAWING_FINISHED=2;
  /** 
 * The type of event. 
 */
  private int type;
  /** 
 * The percentage of completion. 
 */
  private int percent;
  /** 
 * The chart that generated the event. 
 */
  private JFreeChart chart;
  /** 
 * Creates a new chart change event.
 * @param source  the source of the event (could be the chart, a title, anaxis etc.)
 * @param chart  the chart that generated the event.
 * @param type  the type of event.
 * @param percent  the percentage of completion.
 */
  public ChartProgressEvent(  Object source,  JFreeChart chart,  int type,  int percent){
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
  public int getType(){
    return this.type;
  }
  /** 
 * Sets the event type.
 * @param type  the event type.
 */
  public void setType(  int type){
    this.type=type;
  }
  /** 
 * Returns the percentage complete.
 * @return The percentage complete.
 */
  public int getPercent(){
    return this.percent;
  }
  /** 
 * Sets the percentage complete.
 * @param percent  the percentage.
 */
  public void setPercent(  int percent){
    this.percent=percent;
  }
}
