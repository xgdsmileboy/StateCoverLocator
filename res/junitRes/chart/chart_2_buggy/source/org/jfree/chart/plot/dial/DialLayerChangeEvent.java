package org.jfree.chart.plot.dial;
import org.jfree.chart.event.ChartChangeEvent;
/** 
 * An event that can be forwarded to any                                                                                                                                                                {@link DialLayerChangeListener} tosignal a change to a  {@link DialLayer}.
 * @since 1.0.7
 */
public class DialLayerChangeEvent extends ChartChangeEvent {
  /** 
 * The dial layer that generated the event. 
 */
  private DialLayer layer;
  /** 
 * Creates a new instance.
 * @param layer  the dial layer that generated the event.
 */
  public DialLayerChangeEvent(  DialLayer layer){
    super(layer);
    this.layer=layer;
  }
  /** 
 * Returns the layer that generated the event.
 * @return The layer that generated the event.
 */
  public DialLayer getDialLayer(){
    return this.layer;
  }
}
