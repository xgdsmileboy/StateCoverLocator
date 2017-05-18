package org.jfree.chart.event;
import java.util.EventListener;
/** 
 * The interface that must be supported by classes that wish to receive notification of changes to a plot.
 */
public interface PlotChangeListener extends EventListener {
  /** 
 * Receives notification of a plot change event.
 * @param event  the event.
 */
  public void plotChanged(  PlotChangeEvent event);
}
