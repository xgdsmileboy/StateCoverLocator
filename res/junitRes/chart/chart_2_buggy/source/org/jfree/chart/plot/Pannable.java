package org.jfree.chart.plot;
import java.awt.geom.Point2D;
import org.jfree.chart.ChartPanel;
/** 
 * An interface that the                                                                                                                                                                {@link ChartPanel} class uses to communicate withplots that support panning.
 * @since 1.0.13
 */
public interface Pannable {
  /** 
 * Returns the orientation of the plot.
 * @return The orientation (never <code>null</code>).
 */
  public PlotOrientation getOrientation();
  /** 
 * Evaluates if the domain axis can be panned.
 * @return <code>true</code> if the domain axis is pannable.
 */
  public boolean isDomainPannable();
  /** 
 * Evaluates if the range axis can be panned.
 * @return <code>true</code> if the range axis is pannable.
 */
  public boolean isRangePannable();
  /** 
 * Pans the domain axes by the specified percentage.
 * @param percent  the distance to pan (as a percentage of the axis length).
 * @param info the plot info
 * @param source the source point where the pan action started.
 */
  public void panDomainAxes(  double percent,  PlotRenderingInfo info,  Point2D source);
  /** 
 * Pans the range axes by the specified percentage.
 * @param percent  the distance to pan (as a percentage of the axis length).
 * @param info the plot info
 * @param source the source point where the pan action started.
 */
  public void panRangeAxes(  double percent,  PlotRenderingInfo info,  Point2D source);
}
