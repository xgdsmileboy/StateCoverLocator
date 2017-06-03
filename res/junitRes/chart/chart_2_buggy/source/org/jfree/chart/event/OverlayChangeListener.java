package org.jfree.chart.event;
import java.util.EventListener;
import org.jfree.chart.panel.Overlay;
/** 
 * A listener for changes to an                                                                                                                                                                     {@link Overlay}.
 * @since 1.0.13
 */
public interface OverlayChangeListener extends EventListener {
  /** 
 * This method is called to notify a listener of a change event.
 * @param event  the event.
 */
  public void overlayChanged(  OverlayChangeEvent event);
}
