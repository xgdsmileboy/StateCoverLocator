package org.jfree.data.general;
import org.jfree.data.event.DatasetChangeListener;
import org.jfree.data.event.DatasetChangeEvent;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.jfree.chart.event.DatasetChangeInfo;
/** 
 * An abstract implementation of the                                                                                               {@link Dataset} interface, containing amechanism for registering change listeners.
 */
public abstract class AbstractDataset implements Dataset, Cloneable, Serializable, ObjectInputValidation {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=1918768939869230744L;
  /** 
 * The group that the dataset belongs to. 
 */
  private DatasetGroup group;
  /** 
 * Storage for registered change listeners. 
 */
  private transient EventListenerList listenerList;
  /** 
 * Constructs a dataset. By default, the dataset is assigned to its own group.
 */
  protected AbstractDataset(){
    this.group=new DatasetGroup();
    this.listenerList=new EventListenerList();
  }
  /** 
 * Returns the dataset group for the dataset.
 * @return The group (never <code>null</code>).
 * @see #setGroup(DatasetGroup)
 */
  public DatasetGroup getGroup(){
    return this.group;
  }
  /** 
 * Sets the dataset group for the dataset.
 * @param group  the group (<code>null</code> not permitted).
 * @see #getGroup()
 */
  public void setGroup(  DatasetGroup group){
    if (group == null) {
      throw new IllegalArgumentException("Null 'group' argument.");
    }
    this.group=group;
  }
  /** 
 * Registers an object to receive notification of changes to the dataset.
 * @param listener  the object to register.
 * @see #removeChangeListener(DatasetChangeListener)
 */
  public void addChangeListener(  DatasetChangeListener listener){
    this.listenerList.add(DatasetChangeListener.class,listener);
  }
  /** 
 * Deregisters an object so that it no longer receives notification of changes to the dataset.
 * @param listener  the object to deregister.
 * @see #addChangeListener(DatasetChangeListener)
 */
  public void removeChangeListener(  DatasetChangeListener listener){
    this.listenerList.remove(DatasetChangeListener.class,listener);
  }
  /** 
 * Returns <code>true</code> if the specified object is registered with the dataset as a listener.  Most applications won't need to call this method, it exists mainly for use by unit testing code.
 * @param listener  the listener.
 * @return A boolean.
 * @see #addChangeListener(DatasetChangeListener)
 * @see #removeChangeListener(DatasetChangeListener)
 */
  public boolean hasListener(  EventListener listener){
    List list=Arrays.asList(this.listenerList.getListenerList());
    return list.contains(listener);
  }
  /** 
 * Notifies all registered listeners that the dataset has changed.
 * @param info  information about the change (<code>null</code> notpermitted).
 * @see #addChangeListener(DatasetChangeListener)
 * @since 1.2.0
 */
  protected void fireDatasetChanged(  DatasetChangeInfo info){
    notifyListeners(new DatasetChangeEvent(this,this,info));
  }
  /** 
 * Notifies all registered listeners that the dataset has changed.
 * @param event  contains information about the event that triggered thenotification.
 * @see #addChangeListener(DatasetChangeListener)
 * @see #removeChangeListener(DatasetChangeListener)
 */
  protected void notifyListeners(  DatasetChangeEvent event){
    Object[] listeners=this.listenerList.getListenerList();
    for (int i=listeners.length - 2; i >= 0; i-=2) {
      if (listeners[i] == DatasetChangeListener.class) {
        ((DatasetChangeListener)listeners[i + 1]).datasetChanged(event);
      }
    }
  }
  /** 
 * Returns a clone of the dataset. The cloned dataset will NOT include the                                                                                              {@link DatasetChangeListener} references that have been registered withthis dataset.
 * @return A clone.
 * @throws CloneNotSupportedException  if the dataset does not supportcloning.
 */
  public Object clone() throws CloneNotSupportedException {
    AbstractDataset clone=(AbstractDataset)super.clone();
    clone.listenerList=new EventListenerList();
    return clone;
  }
  /** 
 * Handles serialization.
 * @param stream  the output stream.
 * @throws IOException if there is an I/O problem.
 */
  private void writeObject(  ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
  }
  /** 
 * Restores a serialized object.
 * @param stream  the input stream.
 * @throws IOException if there is an I/O problem.
 * @throws ClassNotFoundException if there is a problem loading a class.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.listenerList=new EventListenerList();
    stream.registerValidation(this,10);
  }
  /** 
 * Validates the object. We use this opportunity to call listeners who have registered during the deserialization process, as listeners are not serialized. This method is called by the serialization system after the entire graph is read. This object has registered itself to the system with a priority of 10. Other callbacks may register with a higher priority number to be called before this object, or with a lower priority number to be called after the listeners were notified. All listeners are supposed to have register by now, either in their readObject or validateObject methods. Notify them that this dataset has changed.
 * @exception InvalidObjectException If the object cannot validate itself.
 */
  public void validateObject() throws InvalidObjectException {
    fireDatasetChanged(new DatasetChangeInfo());
  }
}
