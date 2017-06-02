package org.jfree.chart.event;
import java.util.EventObject;
import org.jfree.chart.panel.Overlay;
/** 
 * A change event for an                                                                                                                                                                {@link Overlay}.
 * @since 1.0.13
 */
public class OverlayChangeEvent extends EventObject {
  /** 
 * Creates a new change event.
 * @param source  the event source.
 */
  public OverlayChangeEvent(  Object source){
    super(source);
  }
}
