package org.jfree.chart.event;
import org.jfree.chart.plot.Plot;
/** 
 * An event that can be forwarded to any     {@link org.jfree.chart.event.PlotChangeListener} to signal a change to aplot.
 */
public class PlotChangeEvent extends ChartChangeEvent {
  /** 
 * The plot that generated the event. 
 */
  private Plot plot;
  /** 
 * Creates a new PlotChangeEvent.
 * @param plot  the plot that generated the event.
 */
  public PlotChangeEvent(  Plot plot){
    super(plot);
    this.plot=plot;
  }
  /** 
 * Returns the plot that generated the event.
 * @return The plot that generated the event.
 */
  public Plot getPlot(){
    return this.plot;
  }
}
