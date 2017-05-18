package org.jfree.chart.axis;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;
import org.jfree.chart.Effect3D;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.util.RectangleEdge;
/** 
 * A standard linear value axis with a 3D effect corresponding to the offset specified by some renderers.
 */
public class NumberAxis3D extends NumberAxis implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-1790205852569123512L;
  /** 
 * Default constructor.
 */
  public NumberAxis3D(){
    this(null);
  }
  /** 
 * Constructs a new axis.
 * @param label  the axis label (<code>null</code> permitted).
 */
  public NumberAxis3D(  String label){
    super(label);
  }
  /** 
 * Draws the axis on a Java 2D graphics device (such as the screen or a printer).
 * @param g2  the graphics device.
 * @param cursor  the cursor.
 * @param plotArea  the area for drawing the axes and data.
 * @param dataArea  the area for drawing the data (a subset of theplotArea).
 * @param edge  the axis location.
 * @param plotState  collects information about the plot (<code>null</code>permitted).
 * @return The updated cursor value.
 */
  public AxisState draw(  Graphics2D g2,  double cursor,  Rectangle2D plotArea,  Rectangle2D dataArea,  RectangleEdge edge,  PlotRenderingInfo plotState){
    if (!isVisible()) {
      AxisState state=new AxisState(cursor);
      List ticks=refreshTicks(g2,state,dataArea,edge);
      state.setTicks(ticks);
      return state;
    }
    double xOffset=0.0;
    double yOffset=0.0;
    Plot plot=getPlot();
    if (plot instanceof CategoryPlot) {
      CategoryPlot cp=(CategoryPlot)plot;
      CategoryItemRenderer r=cp.getRenderer();
      if (r instanceof Effect3D) {
        Effect3D e3D=(Effect3D)r;
        xOffset=e3D.getXOffset();
        yOffset=e3D.getYOffset();
      }
    }
    double adjustedX=dataArea.getMinX();
    double adjustedY=dataArea.getMinY();
    double adjustedW=dataArea.getWidth() - xOffset;
    double adjustedH=dataArea.getHeight() - yOffset;
    if (edge == RectangleEdge.LEFT || edge == RectangleEdge.BOTTOM) {
      adjustedY+=yOffset;
    }
 else {
      if (edge == RectangleEdge.RIGHT || edge == RectangleEdge.TOP) {
        adjustedX+=xOffset;
      }
    }
    Rectangle2D adjustedDataArea=new Rectangle2D.Double(adjustedX,adjustedY,adjustedW,adjustedH);
    AxisState info=drawTickMarksAndLabels(g2,cursor,plotArea,adjustedDataArea,edge,plotState);
    info=drawLabel(getLabel(),g2,plotArea,dataArea,edge,info,plotState);
    return info;
  }
}
