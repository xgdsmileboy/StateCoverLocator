package org.jfree.chart.event;
import java.util.EventListener;
/** 
 * The interface that must be supported by classes that wish to receive notification of changes to a chart title.
 */
public interface TitleChangeListener extends EventListener {
  /** 
 * Receives notification of a chart title change event.
 * @param event  the event.
 */
  public void titleChanged(  TitleChangeEvent event);
}
