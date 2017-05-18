package org.jfree.data.event;
import org.jfree.data.event.SeriesChangeEvent;
import java.util.EventListener;
/** 
 * Methods for receiving notification of changes to a data series.
 */
public interface SeriesChangeListener extends EventListener {
  /** 
 * Called when an observed series changes in some way.
 * @param event  information about the change.
 */
  public void seriesChanged(  SeriesChangeEvent event);
}
