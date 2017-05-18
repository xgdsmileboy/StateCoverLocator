package org.jfree.data.event;
import org.jfree.data.event.DatasetChangeEvent;
import java.util.EventListener;
/** 
 * The interface that must be supported by classes that wish to receive notification of changes to a dataset.
 */
public interface DatasetChangeListener extends EventListener {
  /** 
 * Receives notification of an dataset change event.
 * @param event  information about the event.
 */
  public void datasetChanged(  DatasetChangeEvent event);
}
