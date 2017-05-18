package org.jfree.chart;
import java.util.EventListener;
/** 
 * The interface that must be implemented by classes that wish to receive     {@link ChartMouseEvent} notifications from a {@link ChartPanel}.
 * @see ChartPanel#addChartMouseListener(ChartMouseListener)
 */
public interface ChartMouseListener extends EventListener {
  /** 
 * Callback method for receiving notification of a mouse click on a chart.
 * @param event  information about the event.
 */
  void chartMouseClicked(  ChartMouseEvent event);
  /** 
 * Callback method for receiving notification of a mouse movement on a chart.
 * @param event  information about the event.
 */
  void chartMouseMoved(  ChartMouseEvent event);
}
