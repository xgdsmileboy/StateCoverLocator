package org.jfree.chart.event;
import java.util.EventListener;
/** 
 * The interface that must be supported by classes that wish to receive notification of changes to a renderer.
 */
public interface RendererChangeListener extends EventListener {
  /** 
 * Receives notification of a renderer change event.
 * @param event  the event.
 */
  public void rendererChanged(  RendererChangeEvent event);
}
