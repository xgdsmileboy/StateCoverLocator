package org.jfree.chart.event;
import java.util.EventListener;
/** 
 * The interface that must be supported by classes that wish to receive notification of chart progress events.
 */
public interface ChartProgressListener extends EventListener {
  /** 
 * Receives notification of a chart progress event.
 * @param event  the event.
 */
  public void chartProgress(  ChartProgressEvent event);
}
