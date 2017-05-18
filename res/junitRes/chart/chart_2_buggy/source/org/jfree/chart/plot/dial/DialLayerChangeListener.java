package org.jfree.chart.plot.dial;
import java.util.EventListener;
/** 
 * The interface via which an object is notified of changes to a                                                                                              {@link DialLayer}.  The                                                                                               {@link DialPlot} class listens for changes to itslayers in this way.
 * @since 1.0.7
 */
public interface DialLayerChangeListener extends EventListener {
  /** 
 * A call-back method for receiving notification of a change to a                                                                                              {@link DialLayer}.
 * @param event  the event.
 */
  public void dialLayerChanged(  DialLayerChangeEvent event);
}
