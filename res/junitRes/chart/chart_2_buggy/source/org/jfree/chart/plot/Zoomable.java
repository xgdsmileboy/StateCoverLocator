package org.jfree.chart.plot;
import java.awt.geom.Point2D;
import org.jfree.chart.ChartPanel;
/** 
 * A plot that is zoomable must implement this interface to provide a mechanism for the                                                                                               {@link ChartPanel} to control the zooming.
 */
public interface Zoomable {
  /** 
 * Returns <code>true</code> if the plot's domain is zoomable, and <code>false</code> otherwise.
 * @return A boolean.
 * @see #isRangeZoomable()
 */
  public boolean isDomainZoomable();
  /** 
 * Returns <code>true</code> if the plot's range is zoomable, and <code>false</code> otherwise.
 * @return A boolean.
 * @see #isDomainZoomable()
 */
  public boolean isRangeZoomable();
  /** 
 * Returns the orientation of the plot.
 * @return The orientation.
 */
  public PlotOrientation getOrientation();
  /** 
 * Multiplies the range on the domain axis/axes by the specified factor. The <code>source</code> point can be used in some cases to identify a subplot, or to determine the center of zooming (refer to the documentation of the implementing class for details).
 * @param factor  the zoom factor.
 * @param state  the plot state.
 * @param source  the source point (in Java2D coordinates).
 * @see #zoomRangeAxes(double,PlotRenderingInfo,Point2D)
 */
  public void zoomDomainAxes(  double factor,  PlotRenderingInfo state,  Point2D source);
  /** 
 * Multiplies the range on the domain axis/axes by the specified factor. The <code>source</code> point can be used in some cases to identify a subplot, or to determine the center of zooming (refer to the documentation of the implementing class for details).
 * @param factor  the zoom factor.
 * @param state  the plot state.
 * @param source  the source point (in Java2D coordinates).
 * @param useAnchor  use source point as zoom anchor?
 * @see #zoomRangeAxes(double,PlotRenderingInfo,Point2D,boolean)
 * @since 1.0.7
 */
  public void zoomDomainAxes(  double factor,  PlotRenderingInfo state,  Point2D source,  boolean useAnchor);
  /** 
 * Zooms in on the domain axes.  The <code>source</code> point can be used in some cases to identify a subplot for zooming.
 * @param lowerPercent  the new lower bound.
 * @param upperPercent  the new upper bound.
 * @param state  the plot state.
 * @param source  the source point (in Java2D coordinates).
 * @see #zoomRangeAxes(double,double,PlotRenderingInfo,Point2D)
 */
  public void zoomDomainAxes(  double lowerPercent,  double upperPercent,  PlotRenderingInfo state,  Point2D source);
  /** 
 * Multiplies the range on the range axis/axes by the specified factor. The <code>source</code> point can be used in some cases to identify a subplot, or to determine the center of zooming (refer to the documentation of the implementing class for details).
 * @param factor  the zoom factor.
 * @param state  the plot state.
 * @param source  the source point (in Java2D coordinates).
 * @see #zoomDomainAxes(double,PlotRenderingInfo,Point2D)
 */
  public void zoomRangeAxes(  double factor,  PlotRenderingInfo state,  Point2D source);
  /** 
 * Multiplies the range on the range axis/axes by the specified factor. The <code>source</code> point can be used in some cases to identify a subplot, or to determine the center of zooming (refer to the documentation of the implementing class for details).
 * @param factor  the zoom factor.
 * @param state  the plot state.
 * @param source  the source point (in Java2D coordinates).
 * @param useAnchor  use source point as zoom anchor?
 * @see #zoomDomainAxes(double,PlotRenderingInfo,Point2D)
 * @since 1.0.7
 */
  public void zoomRangeAxes(  double factor,  PlotRenderingInfo state,  Point2D source,  boolean useAnchor);
  /** 
 * Zooms in on the range axes.  The <code>source</code> point can be used in some cases to identify a subplot for zooming.
 * @param lowerPercent  the new lower bound.
 * @param upperPercent  the new upper bound.
 * @param state  the plot state.
 * @param source  the source point (in Java2D coordinates).
 * @see #zoomDomainAxes(double,double,PlotRenderingInfo,Point2D)
 */
  public void zoomRangeAxes(  double lowerPercent,  double upperPercent,  PlotRenderingInfo state,  Point2D source);
}
