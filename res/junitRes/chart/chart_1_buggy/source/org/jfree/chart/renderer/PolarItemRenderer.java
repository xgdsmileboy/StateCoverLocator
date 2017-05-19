package org.jfree.chart.renderer;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.data.xy.XYDataset;
/** 
 * The interface for a renderer that can be used by the                               {@link PolarPlot} class.
 */
public interface PolarItemRenderer {
  /** 
 * Plots the data for a given series.
 * @param g2  the drawing surface.
 * @param dataArea  the data area.
 * @param info  collects plot rendering info.
 * @param plot  the plot.
 * @param dataset  the dataset.
 * @param seriesIndex  the series index.
 */
  public void drawSeries(  Graphics2D g2,  Rectangle2D dataArea,  PlotRenderingInfo info,  PolarPlot plot,  XYDataset dataset,  int seriesIndex);
  /** 
 * Draw the angular gridlines - the spokes.
 * @param g2  the drawing surface.
 * @param plot  the plot.
 * @param ticks  the ticks.
 * @param dataArea  the data area.
 */
  public void drawAngularGridLines(  Graphics2D g2,  PolarPlot plot,  List ticks,  Rectangle2D dataArea);
  /** 
 * Draw the radial gridlines - the rings.
 * @param g2  the drawing surface.
 * @param plot  the plot.
 * @param radialAxis  the radial axis.
 * @param ticks  the ticks.
 * @param dataArea  the data area.
 */
  public void drawRadialGridLines(  Graphics2D g2,  PolarPlot plot,  ValueAxis radialAxis,  List ticks,  Rectangle2D dataArea);
  /** 
 * Return the legend for the given series.
 * @param series  the series index.
 * @return The legend item.
 */
  public LegendItem getLegendItem(  int series);
  /** 
 * Returns the plot that this renderer has been assigned to.
 * @return The plot.
 */
  public PolarPlot getPlot();
  /** 
 * Sets the plot that this renderer is assigned to. <P> This method will be called by the plot class...you do not need to call it yourself.
 * @param plot  the plot.
 */
  public void setPlot(  PolarPlot plot);
  /** 
 * Adds a change listener.
 * @param listener  the listener.
 */
  public void addChangeListener(  RendererChangeListener listener);
  /** 
 * Removes a change listener.
 * @param listener  the listener.
 */
  public void removeChangeListener(  RendererChangeListener listener);
}
