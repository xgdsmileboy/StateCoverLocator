package org.jfree.chart.axis;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.Effect3D;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.util.RectangleEdge;
/** 
 * An axis that displays categories and has a 3D effect. Used for bar charts and line charts.
 */
public class CategoryAxis3D extends CategoryAxis implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=4114732251353700972L;
  /** 
 * Creates a new axis.
 */
  public CategoryAxis3D(){
    this(null);
  }
  /** 
 * Creates a new axis using default attribute values.
 * @param label  the axis label (<code>null</code> permitted).
 */
  public CategoryAxis3D(  String label){
    super(label);
  }
  /** 
 * Draws the axis on a Java 2D graphics device (such as the screen or a printer).
 * @param g2  the graphics device (<code>null</code> not permitted).
 * @param cursor  the cursor location.
 * @param plotArea  the area within which the axis should be drawn(<code>null</code> not permitted).
 * @param dataArea  the area within which the plot is being drawn(<code>null</code> not permitted).
 * @param edge  the location of the axis (<code>null</code> not permitted).
 * @param plotState  collects information about the plot (<code>null</code>permitted).
 * @return The axis state (never <code>null</code>).
 */
  public AxisState draw(  Graphics2D g2,  double cursor,  Rectangle2D plotArea,  Rectangle2D dataArea,  RectangleEdge edge,  PlotRenderingInfo plotState){
    if (!isVisible()) {
      return new AxisState(cursor);
    }
    CategoryPlot plot=(CategoryPlot)getPlot();
    Rectangle2D adjustedDataArea=new Rectangle2D.Double();
    if (plot.getRenderer() instanceof Effect3D) {
      Effect3D e3D=(Effect3D)plot.getRenderer();
      double adjustedX=dataArea.getMinX();
      double adjustedY=dataArea.getMinY();
      double adjustedW=dataArea.getWidth() - e3D.getXOffset();
      double adjustedH=dataArea.getHeight() - e3D.getYOffset();
      if (edge == RectangleEdge.LEFT || edge == RectangleEdge.BOTTOM) {
        adjustedY+=e3D.getYOffset();
      }
 else {
        if (edge == RectangleEdge.RIGHT || edge == RectangleEdge.TOP) {
          adjustedX+=e3D.getXOffset();
        }
      }
      adjustedDataArea.setRect(adjustedX,adjustedY,adjustedW,adjustedH);
    }
 else {
      adjustedDataArea.setRect(dataArea);
    }
    if (isAxisLineVisible()) {
      drawAxisLine(g2,cursor,adjustedDataArea,edge);
    }
    AxisState state=new AxisState(cursor);
    if (isTickMarksVisible()) {
      drawTickMarks(g2,cursor,adjustedDataArea,edge,state);
    }
    state=drawCategoryLabels(g2,plotArea,adjustedDataArea,edge,state,plotState);
    state=drawLabel(getLabel(),g2,plotArea,dataArea,edge,state,plotState);
    return state;
  }
  /** 
 * Returns the Java 2D coordinate for a category.
 * @param anchor  the anchor point.
 * @param category  the category index.
 * @param categoryCount  the category count.
 * @param area  the data area.
 * @param edge  the location of the axis.
 * @return The coordinate.
 */
  public double getCategoryJava2DCoordinate(  CategoryAnchor anchor,  int category,  int categoryCount,  Rectangle2D area,  RectangleEdge edge){
    double result=0.0;
    Rectangle2D adjustedArea=area;
    CategoryPlot plot=(CategoryPlot)getPlot();
    CategoryItemRenderer renderer=plot.getRenderer();
    if (renderer instanceof Effect3D) {
      Effect3D e3D=(Effect3D)renderer;
      double adjustedX=area.getMinX();
      double adjustedY=area.getMinY();
      double adjustedW=area.getWidth() - e3D.getXOffset();
      double adjustedH=area.getHeight() - e3D.getYOffset();
      if (edge == RectangleEdge.LEFT || edge == RectangleEdge.BOTTOM) {
        adjustedY+=e3D.getYOffset();
      }
 else {
        if (edge == RectangleEdge.RIGHT || edge == RectangleEdge.TOP) {
          adjustedX+=e3D.getXOffset();
        }
      }
      adjustedArea=new Rectangle2D.Double(adjustedX,adjustedY,adjustedW,adjustedH);
    }
    if (anchor == CategoryAnchor.START) {
      result=getCategoryStart(category,categoryCount,adjustedArea,edge);
    }
 else {
      if (anchor == CategoryAnchor.MIDDLE) {
        result=getCategoryMiddle(category,categoryCount,adjustedArea,edge);
      }
 else {
        if (anchor == CategoryAnchor.END) {
          result=getCategoryEnd(category,categoryCount,adjustedArea,edge);
        }
      }
    }
    return result;
  }
  /** 
 * Returns a clone of the axis.
 * @return A clone.
 * @throws CloneNotSupportedException If the axis is not cloneable forsome reason.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
