package org.jfree.data.general;
import org.jfree.data.event.DatasetChangeListener;
/** 
 * The base interface for data sets. <P> All datasets are required to support the      {@link DatasetChangeEvent}mechanism by allowing listeners to register and receive notification of any changes to the dataset. <P> In addition, all datasets must belong to one (and only one)     {@link DatasetGroup}.  The group object maintains a reader-writer lock which provides synchronised access to the datasets in multi-threaded code.
 */
public interface Dataset {
  /** 
 * Registers an object for notification of changes to the dataset.
 * @param listener  the object to register.
 */
  public void addChangeListener(  DatasetChangeListener listener);
  /** 
 * Deregisters an object for notification of changes to the dataset.
 * @param listener  the object to deregister.
 */
  public void removeChangeListener(  DatasetChangeListener listener);
  /** 
 * Returns the dataset group.
 * @return The dataset group.
 */
  public DatasetGroup getGroup();
  /** 
 * Sets the dataset group.
 * @param group  the dataset group.
 */
  public void setGroup(  DatasetGroup group);
}
