package org.jfree.chart.plot.junit;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.PlotChangeListener;
/** 
 * A utility class for testing plot events.
 */
public class MyPlotChangeListener implements PlotChangeListener {
  private PlotChangeEvent event;
  /** 
 * Creates a new instance.
 */
  public MyPlotChangeListener(){
    this.event=null;
  }
  /** 
 * Returns the last event received by the listener.
 * @return The event.
 */
  public PlotChangeEvent getEvent(){
    return this.event;
  }
  /** 
 * Sets the event for the listener.
 * @param e  the event.
 */
  public void setEvent(  PlotChangeEvent e){
    this.event=e;
  }
  /** 
 * Receives notification of a plot change event.
 * @param e  the event.
 */
  public void plotChanged(  PlotChangeEvent e){
    this.event=e;
  }
}
