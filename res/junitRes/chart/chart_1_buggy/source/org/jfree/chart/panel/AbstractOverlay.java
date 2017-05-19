package org.jfree.chart.panel;
import javax.swing.event.EventListenerList;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.OverlayChangeEvent;
import org.jfree.chart.event.OverlayChangeListener;
/** 
 * A base class for implementing overlays for a                               {@link ChartPanel}.
 * @since 1.0.13
 */
public class AbstractOverlay {
  /** 
 * Storage for registered change listeners. 
 */
  private transient EventListenerList changeListeners;
  /** 
 * Default constructor.
 */
  public AbstractOverlay(){
    this.changeListeners=new EventListenerList();
  }
  /** 
 * Registers an object for notification of changes to the overlay.
 * @param listener  the listener (<code>null</code> not permitted).
 * @see #removeChangeListener(OverlayChangeListener)
 */
  public void addChangeListener(  OverlayChangeListener listener){
    if (listener == null) {
      throw new IllegalArgumentException("Null 'listener' argument.");
    }
    this.changeListeners.add(OverlayChangeListener.class,listener);
  }
  /** 
 * Deregisters an object for notification of changes to the overlay.
 * @param listener  the listener (<code>null</code> not permitted)
 * @see #addChangeListener(OverlayChangeListener)
 */
  public void removeChangeListener(  OverlayChangeListener listener){
    if (listener == null) {
      throw new IllegalArgumentException("Null 'listener' argument.");
    }
    this.changeListeners.remove(OverlayChangeListener.class,listener);
  }
  /** 
 * Sends a default                               {@link ChartChangeEvent} to all registered listeners.<P> This method is for convenience only.
 */
  public void fireOverlayChanged(){
    OverlayChangeEvent event=new OverlayChangeEvent(this);
    notifyListeners(event);
  }
  /** 
 * Sends a                               {@link ChartChangeEvent} to all registered listeners.
 * @param event  information about the event that triggered thenotification.
 */
  protected void notifyListeners(  OverlayChangeEvent event){
    Object[] listeners=this.changeListeners.getListenerList();
    for (int i=listeners.length - 2; i >= 0; i-=2) {
      if (listeners[i] == OverlayChangeListener.class) {
        ((OverlayChangeListener)listeners[i + 1]).overlayChanged(event);
      }
    }
  }
}
