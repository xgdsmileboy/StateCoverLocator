package org.jfree.chart.renderer.junit;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
/** 
 * A simple class for detecting whether or not a renderer has generated a             {@link RendererChangeEvent}.
 */
public class RendererChangeDetector implements RendererChangeListener {
  /** 
 * A flag that records whether or not a change event has been received. 
 */
  private boolean notified;
  /** 
 * Creates a new detector.
 */
  public RendererChangeDetector(){
    this.notified=false;
  }
  /** 
 * Returns the flag that indicates whether or not a change event has been received.
 * @return The flag.
 */
  public boolean getNotified(){
    return this.notified;
  }
  /** 
 * Sets the flag that indicates whether or not a change event has been received.
 * @param notified  the new value of the flag.
 */
  public void setNotified(  boolean notified){
    this.notified=notified;
  }
  /** 
 * Receives a             {@link RendererChangeEvent} from a renderer.
 * @param event  the event.
 */
  public void rendererChanged(  RendererChangeEvent event){
    this.notified=true;
  }
}
