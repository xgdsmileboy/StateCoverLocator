package org.jfree.chart.event;
import java.util.EventListener;
/** 
 * The interface that must be supported by classes that wish to receive notification of changes to an axis. <P> The Plot class implements this interface, and automatically registers with its axes (if any). Any axis changes are passed on by the plot as a plot change event.  This is part of the notification mechanism that ensures that charts are redrawn whenever changes are made to any chart component.
 */
public interface AxisChangeListener extends EventListener {
  /** 
 * Receives notification of an axis change event.
 * @param event  the event.
 */
  public void axisChanged(  AxisChangeEvent event);
}
