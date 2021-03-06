package org.jfree.chart.renderer.xy;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYCrosshairState;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.chart.util.HashUtilities;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.data.xy.XYDataset;
/** 
 * Line/Step item renderer for an                               {@link XYPlot}.  This class draws lines between data points, only allowing horizontal or vertical lines (steps). The example shown here is generated by the <code>XYStepRendererDemo1.java</code> program included in the JFreeChart demo collection: <br><br> <img src="../../../../../images/XYStepRendererSample.png" alt="XYStepRendererSample.png" />
 */
public class XYStepRenderer extends XYLineAndShapeRenderer implements XYItemRenderer, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-8918141928884796108L;
  /** 
 * The factor (from 0.0 to 1.0) that determines the position of the step.
 * @since 1.0.10.
 */
  private double stepPoint=1.0d;
  /** 
 * Constructs a new renderer with no tooltip or URL generation.
 */
  public XYStepRenderer(){
    this(null,null);
  }
  /** 
 * Constructs a new renderer with the specified tool tip and URL generators.
 * @param toolTipGenerator  the item label generator (<code>null</code>permitted).
 * @param urlGenerator  the URL generator (<code>null</code> permitted).
 */
  public XYStepRenderer(  XYToolTipGenerator toolTipGenerator,  XYURLGenerator urlGenerator){
    super();
    setBaseToolTipGenerator(toolTipGenerator);
    setBaseURLGenerator(urlGenerator);
    setBaseShapesVisible(false);
  }
  /** 
 * Returns the fraction of the domain position between two points on which the step is drawn.  The default is 1.0d, which means the step is drawn at the domain position of the second`point. If the stepPoint is 0.5d the step is drawn at half between the two points.
 * @return The fraction of the domain position between two points where thestep is drawn.
 * @see #setStepPoint(double)
 * @since 1.0.10
 */
  public double getStepPoint(){
    return this.stepPoint;
  }
  /** 
 * Sets the step point and sends a                               {@link RendererChangeEvent} to allregistered listeners.
 * @param stepPoint  the step point (in the range 0.0 to 1.0)
 * @see #getStepPoint()
 * @since 1.0.10
 */
  public void setStepPoint(  double stepPoint){
    if (stepPoint < 0.0d || stepPoint > 1.0d) {
      throw new IllegalArgumentException("Requires stepPoint in [0.0;1.0]");
    }
    this.stepPoint=stepPoint;
    fireChangeEvent();
  }
  /** 
 * Draws the visual representation of a single data item.
 * @param g2  the graphics device.
 * @param state  the renderer state.
 * @param dataArea  the area within which the data is being drawn.
 * @param plot  the plot (can be used to obtain standard colorinformation etc).
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the vertical axis.
 * @param dataset  the dataset.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @param pass  the pass index.
 */
  public void drawItem(  Graphics2D g2,  XYItemRendererState state,  Rectangle2D dataArea,  XYPlot plot,  ValueAxis domainAxis,  ValueAxis rangeAxis,  XYDataset dataset,  int series,  int item,  boolean selected,  int pass){
    if (!getItemVisible(series,item)) {
      return;
    }
    PlotOrientation orientation=plot.getOrientation();
    Paint seriesPaint=getItemPaint(series,item,selected);
    Stroke seriesStroke=getItemStroke(series,item,selected);
    g2.setPaint(seriesPaint);
    g2.setStroke(seriesStroke);
    double x1=dataset.getXValue(series,item);
    double y1=dataset.getYValue(series,item);
    RectangleEdge xAxisLocation=plot.getDomainAxisEdge();
    RectangleEdge yAxisLocation=plot.getRangeAxisEdge();
    double transX1=domainAxis.valueToJava2D(x1,dataArea,xAxisLocation);
    double transY1=(Double.isNaN(y1) ? Double.NaN : rangeAxis.valueToJava2D(y1,dataArea,yAxisLocation));
    if (pass == 0 && item > 0) {
      double x0=dataset.getXValue(series,item - 1);
      double y0=dataset.getYValue(series,item - 1);
      double transX0=domainAxis.valueToJava2D(x0,dataArea,xAxisLocation);
      double transY0=(Double.isNaN(y0) ? Double.NaN : rangeAxis.valueToJava2D(y0,dataArea,yAxisLocation));
      if (orientation == PlotOrientation.HORIZONTAL) {
        if (transY0 == transY1) {
          drawLine(g2,state.workingLine,transY0,transX0,transY1,transX1);
        }
 else {
          double transXs=transX0 + (getStepPoint() * (transX1 - transX0));
          drawLine(g2,state.workingLine,transY0,transX0,transY0,transXs);
          drawLine(g2,state.workingLine,transY0,transXs,transY1,transXs);
          drawLine(g2,state.workingLine,transY1,transXs,transY1,transX1);
        }
      }
 else {
        if (orientation == PlotOrientation.VERTICAL) {
          if (transY0 == transY1) {
            drawLine(g2,state.workingLine,transX0,transY0,transX1,transY1);
          }
 else {
            double transXs=transX0 + (getStepPoint() * (transX1 - transX0));
            drawLine(g2,state.workingLine,transX0,transY0,transXs,transY0);
            drawLine(g2,state.workingLine,transXs,transY0,transXs,transY1);
            drawLine(g2,state.workingLine,transXs,transY1,transX1,transY1);
          }
        }
      }
      int domainAxisIndex=plot.getDomainAxisIndex(domainAxis);
      int rangeAxisIndex=plot.getRangeAxisIndex(rangeAxis);
      XYCrosshairState crosshairState=state.getCrosshairState();
      updateCrosshairValues(crosshairState,x1,y1,domainAxisIndex,rangeAxisIndex,transX1,transY1,orientation);
      EntityCollection entities=state.getEntityCollection();
      if (entities != null) {
        addEntity(entities,null,dataset,series,item,selected,transX1,transY1);
      }
    }
    if (pass == 1) {
      if (isItemLabelVisible(series,item,selected)) {
        double xx=transX1;
        double yy=transY1;
        if (orientation == PlotOrientation.HORIZONTAL) {
          xx=transY1;
          yy=transX1;
        }
        drawItemLabel(g2,orientation,dataset,series,item,selected,xx,yy,(y1 < 0.0));
      }
    }
  }
  /** 
 * A utility method that draws a line but only if none of the coordinates are NaN values.
 * @param g2  the graphics target.
 * @param line  the line object.
 * @param x0  the x-coordinate for the starting point of the line.
 * @param y0  the y-coordinate for the starting point of the line.
 * @param x1  the x-coordinate for the ending point of the line.
 * @param y1  the y-coordinate for the ending point of the line.
 */
  private void drawLine(  Graphics2D g2,  Line2D line,  double x0,  double y0,  double x1,  double y1){
    if (Double.isNaN(x0) || Double.isNaN(x1) || Double.isNaN(y0)|| Double.isNaN(y1)) {
      return;
    }
    line.setLine(x0,y0,x1,y1);
    g2.draw(line);
  }
  /** 
 * Tests this renderer for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof XYLineAndShapeRenderer)) {
      return false;
    }
    XYStepRenderer that=(XYStepRenderer)obj;
    if (this.stepPoint != that.stepPoint) {
      return false;
    }
    return super.equals(obj);
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    return HashUtilities.hashCode(super.hashCode(),this.stepPoint);
  }
  /** 
 * Returns a clone of the renderer.
 * @return A clone.
 * @throws CloneNotSupportedException  if the renderer cannot be cloned.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
