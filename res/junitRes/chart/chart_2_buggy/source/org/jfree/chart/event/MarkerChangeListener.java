package org.jfree.chart.event;
import java.util.EventListener;
import org.jfree.chart.plot.Marker;
/** 
 * The interface that must be supported by classes that wish to receive notification of changes to a                                                                                               {@link Marker}.
 * @since 1.0.3
 */
public interface MarkerChangeListener extends EventListener {
  /** 
 * Receives notification of a marker change event.
 * @param event  the event.
 * @since 1.0.3
 */
  public void markerChanged(  MarkerChangeEvent event);
}
