package org.jfree.chart.plot.dial;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.EventListener;
/** 
 * A dial layer draws itself within a reference frame.  The view frame is a subset of the reference frame, and defines the area that is actually visible. <br><br> Classes that implement this interface should be                               {@link Serializable}, otherwise chart serialization may fail.
 * @since 1.0.7
 */
public interface DialLayer {
  /** 
 * Returns a flag that indicates whether or not the layer is visible.
 * @return A boolean.
 */
  public boolean isVisible();
  /** 
 * Registers a listener with this layer, so that it receives notification of changes to this layer.
 * @param listener  the listener.
 */
  public void addChangeListener(  DialLayerChangeListener listener);
  /** 
 * Deregisters a listener, so that it no longer receives notification of changes to this layer.
 * @param listener  the listener.
 */
  public void removeChangeListener(  DialLayerChangeListener listener);
  /** 
 * Returns <code>true</code> if the specified listener is currently registered with the this layer.
 * @param listener  the listener.
 * @return A boolean.
 */
  public boolean hasListener(  EventListener listener);
  /** 
 * Returns <code>true</code> if the drawing should be clipped to the dial window (which is defined by the                               {@link DialFrame}), and <code>false</code> otherwise.
 * @return A boolean.
 */
  public boolean isClippedToWindow();
  /** 
 * Draws the content of this layer.
 * @param g2  the graphics target (<code>null</code> not permitted).
 * @param plot  the plot (typically this should not be <code>null</code>,but for a layer that doesn't need to reference the plot, it may be permitted).
 * @param frame  the reference frame for the dial's geometry(<code>null</code> not permitted).  This is typically larger than the visible area of the dial (see the next parameter).
 * @param view  the visible area for the dial (<code>null</code> notpermitted).
 */
  public void draw(  Graphics2D g2,  DialPlot plot,  Rectangle2D frame,  Rectangle2D view);
}
