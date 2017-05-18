package org.jfree.chart.event;
import java.util.EventListener;
/** 
 * The interface that must be supported by classes that wish to receive notification of chart events. <P> The      {@link org.jfree.chart.ChartPanel} class registers itself with thechart it displays, and whenever the chart changes, the panel redraws itself.
 */
public interface ChartChangeListener extends EventListener {
  /** 
 * Receives notification of a chart change event.
 * @param event  the event.
 */
  public void chartChanged(  ChartChangeEvent event);
}
