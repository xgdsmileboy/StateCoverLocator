package org.jfree.chart.plot.dial;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.jfree.chart.util.HashUtilities;
/** 
 * A base class that can be used to implement a                                                                                                                                                                {@link DialLayer}.  It includes an event notification mechanism.
 * @since 1.0.7
 */
public abstract class AbstractDialLayer implements DialLayer {
  /** 
 * A flag that controls whether or not the layer is visible. 
 */
  private boolean visible;
  /** 
 * Storage for registered listeners. 
 */
  private transient EventListenerList listenerList;
  /** 
 * Creates a new instance.
 */
  protected AbstractDialLayer(){
    this.visible=true;
    this.listenerList=new EventListenerList();
  }
  /** 
 * Returns <code>true</code> if this layer is visible (should be displayed), and <code>false</code> otherwise.
 * @return A boolean.
 * @see #setVisible(boolean)
 */
  public boolean isVisible(){
    return this.visible;
  }
  /** 
 * Sets the flag that determines whether or not this layer is drawn by the plot, and sends a                                                                                                                                                                {@link DialLayerChangeEvent} to all registeredlisteners.
 * @param visible  the flag.
 * @see #isVisible()
 */
  public void setVisible(  boolean visible){
    this.visible=visible;
    notifyListeners(new DialLayerChangeEvent(this));
  }
  /** 
 * Tests this instance for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof AbstractDialLayer)) {
      return false;
    }
    AbstractDialLayer that=(AbstractDialLayer)obj;
    return this.visible == that.visible;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=23;
    result=HashUtilities.hashCode(result,this.visible);
    return result;
  }
  /** 
 * Returns a clone of this instance.
 * @return A clone.
 * @throws CloneNotSupportedException if there is a problem cloning thisinstance.
 */
  public Object clone() throws CloneNotSupportedException {
    AbstractDialLayer clone=(AbstractDialLayer)super.clone();
    clone.listenerList=new EventListenerList();
    return clone;
  }
  /** 
 * Registers an object for notification of changes to the dial layer.
 * @param listener  the object that is being registered.
 * @see #removeChangeListener(DialLayerChangeListener)
 */
  public void addChangeListener(  DialLayerChangeListener listener){
    this.listenerList.add(DialLayerChangeListener.class,listener);
  }
  /** 
 * Deregisters an object for notification of changes to the dial layer.
 * @param listener  the object to deregister.
 * @see #addChangeListener(DialLayerChangeListener)
 */
  public void removeChangeListener(  DialLayerChangeListener listener){
    this.listenerList.remove(DialLayerChangeListener.class,listener);
  }
  /** 
 * Returns <code>true</code> if the specified object is registered with the dataset as a listener.  Most applications won't need to call this method, it exists mainly for use by unit testing code.
 * @param listener  the listener.
 * @return A boolean.
 */
  public boolean hasListener(  EventListener listener){
    List list=Arrays.asList(this.listenerList.getListenerList());
    return list.contains(listener);
  }
  /** 
 * Notifies all registered listeners that the dial layer has changed. The                                                                                                                                                                {@link DialLayerChangeEvent} provides information about the change.
 * @param event  information about the change to the axis.
 */
  protected void notifyListeners(  DialLayerChangeEvent event){
    Object[] listeners=this.listenerList.getListenerList();
    for (int i=listeners.length - 2; i >= 0; i-=2) {
      if (listeners[i] == DialLayerChangeListener.class) {
        ((DialLayerChangeListener)listeners[i + 1]).dialLayerChanged(event);
      }
    }
  }
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.listenerList=new EventListenerList();
  }
}
