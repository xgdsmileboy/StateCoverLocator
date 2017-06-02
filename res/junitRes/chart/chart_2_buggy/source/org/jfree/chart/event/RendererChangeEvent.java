package org.jfree.chart.event;
/** 
 * An event that can be forwarded to any                                                                                                                                                                {@link RendererChangeListener} tosignal a change to a renderer.
 */
public class RendererChangeEvent extends ChartChangeEvent {
  /** 
 * The renderer that generated the event. 
 */
  private Object renderer;
  /** 
 * A flag that indicates whether this event relates to a change in the series visibility.  If so, the receiver (if it is a plot) may want to update the axis bounds.
 * @since 1.0.13
 */
  private boolean seriesVisibilityChanged;
  /** 
 * Creates a new event.
 * @param renderer  the renderer that generated the event.
 */
  public RendererChangeEvent(  Object renderer){
    this(renderer,false);
  }
  /** 
 * Creates a new event.
 * @param renderer  the renderer that generated the event.
 * @param seriesVisibilityChanged  a flag that indicates whether or notthe event relates to a change in the series visibility flags.
 */
  public RendererChangeEvent(  Object renderer,  boolean seriesVisibilityChanged){
    super(renderer);
    this.renderer=renderer;
    this.seriesVisibilityChanged=seriesVisibilityChanged;
  }
  /** 
 * Returns the renderer that generated the event.
 * @return The renderer that generated the event.
 */
  public Object getRenderer(){
    return this.renderer;
  }
  /** 
 * Returns the flag that indicates whether or not the event relates to a change in series visibility.
 * @return A boolean.
 * @since 1.0.13
 */
  public boolean getSeriesVisibilityChanged(){
    return this.seriesVisibilityChanged;
  }
}
