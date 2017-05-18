package org.jfree.chart.annotations;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
/** 
 * The interface that must be supported by annotations that are to be added to an                                                                                               {@link XYPlot}.
 */
public interface XYAnnotation extends Annotation {
  /** 
 * Draws the annotation.
 * @param g2  the graphics device.
 * @param plot  the plot.
 * @param dataArea  the data area.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param rendererIndex  the renderer index.
 * @param info  an optional info object that will be populated withentity information.
 */
  public void draw(  Graphics2D g2,  XYPlot plot,  Rectangle2D dataArea,  ValueAxis domainAxis,  ValueAxis rangeAxis,  int rendererIndex,  PlotRenderingInfo info);
}
