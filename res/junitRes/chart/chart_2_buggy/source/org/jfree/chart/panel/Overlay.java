package org.jfree.chart.panel;
import java.awt.Graphics2D;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.event.OverlayChangeListener;
/** 
 * Defines the interface for an overlay that can be added to a                                                                                                                                                                    {@link ChartPanel}.
 * @since 1.0.13
 */
public interface Overlay {
  /** 
 * Paints the crosshairs in the layer.
 * @param g2  the graphics target.
 * @param chartPanel  the chart panel.
 */
  public void paintOverlay(  Graphics2D g2,  ChartPanel chartPanel);
  /** 
 * Registers a change listener with the overlay.
 * @param listener  the listener.
 */
  public void addChangeListener(  OverlayChangeListener listener);
  /** 
 * Deregisters a listener from the overlay.
 * @param listener  the listener.
 */
  public void removeChangeListener(  OverlayChangeListener listener);
}
