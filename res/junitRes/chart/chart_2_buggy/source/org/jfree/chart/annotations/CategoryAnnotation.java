package org.jfree.chart.annotations;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
/** 
 * The interface that must be supported by annotations that are to be added to a                                                                                               {@link CategoryPlot}.
 */
public interface CategoryAnnotation extends Annotation {
  /** 
 * Draws the annotation.
 * @param g2  the graphics device.
 * @param plot  the plot.
 * @param dataArea  the data area.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param rendererIndex  the renderer index.
 * @param info  the plot info (<code>null</code> permitted).
 */
  public void draw(  Graphics2D g2,  CategoryPlot plot,  Rectangle2D dataArea,  CategoryAxis domainAxis,  ValueAxis rangeAxis,  int rendererIndex,  PlotRenderingInfo info);
}
