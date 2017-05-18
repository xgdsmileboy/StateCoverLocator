package org.jfree.chart.renderer.xy;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.Serializable;
import org.jfree.chart.util.GradientPaintTransformer;
import org.jfree.chart.util.RectangleEdge;
/** 
 * An implementation of the                                                                                               {@link XYBarPainter} interface that preserves thebehaviour of bar painting that existed prior to the introduction of the {@link XYBarPainter} interface.
 * @see GradientXYBarPainter
 * @since 1.0.11
 */
public class StandardXYBarPainter implements XYBarPainter, Serializable {
  /** 
 * Creates a new instance.
 */
  public StandardXYBarPainter(){
  }
  /** 
 * Paints a single bar instance.
 * @param g2  the graphics target.
 * @param renderer  the renderer.
 * @param row  the row index.
 * @param column  the column index.
 * @param selected  is the data item selected?
 * @param bar  the bar
 * @param base  indicates which side of the rectangle is the base of thebar.
 * @since 1.2.0
 */
  public void paintBar(  Graphics2D g2,  XYBarRenderer renderer,  int row,  int column,  boolean selected,  RectangularShape bar,  RectangleEdge base){
    Paint itemPaint=renderer.getItemPaint(row,column,selected);
    GradientPaintTransformer t=renderer.getGradientPaintTransformer();
    if (t != null && itemPaint instanceof GradientPaint) {
      itemPaint=t.transform((GradientPaint)itemPaint,bar);
    }
    g2.setPaint(itemPaint);
    g2.fill(bar);
    if (renderer.isDrawBarOutline()) {
      Stroke stroke=renderer.getItemOutlineStroke(row,column,selected);
      Paint paint=renderer.getItemOutlinePaint(row,column,selected);
      if (stroke != null && paint != null) {
        g2.setStroke(stroke);
        g2.setPaint(paint);
        g2.draw(bar);
      }
    }
  }
  /** 
 * Paints a single bar instance.
 * @param g2  the graphics target.
 * @param renderer  the renderer.
 * @param row  the row index.
 * @param column  the column index.
 * @param selected  is the data item selected?
 * @param bar  the bar.
 * @param base  indicates which side of the rectangle is the base of thebar.
 * @param pegShadow  peg the shadow to the base of the bar?
 * @since 1.2.0
 */
  public void paintBarShadow(  Graphics2D g2,  XYBarRenderer renderer,  int row,  int column,  boolean selected,  RectangularShape bar,  RectangleEdge base,  boolean pegShadow){
    Paint itemPaint=renderer.getItemPaint(row,column,selected);
    if (itemPaint instanceof Color) {
      Color c=(Color)itemPaint;
      if (c.getAlpha() == 0) {
        return;
      }
    }
    RectangularShape shadow=createShadow(bar,renderer.getShadowXOffset(),renderer.getShadowYOffset(),base,pegShadow);
    g2.setPaint(Color.gray);
    g2.fill(shadow);
  }
  /** 
 * Creates a shadow for the bar.
 * @param bar  the bar shape.
 * @param xOffset  the x-offset for the shadow.
 * @param yOffset  the y-offset for the shadow.
 * @param base  the edge that is the base of the bar.
 * @param pegShadow  peg the shadow to the base?
 * @return A rectangle for the shadow.
 */
  private Rectangle2D createShadow(  RectangularShape bar,  double xOffset,  double yOffset,  RectangleEdge base,  boolean pegShadow){
    double x0=bar.getMinX();
    double x1=bar.getMaxX();
    double y0=bar.getMinY();
    double y1=bar.getMaxY();
    if (base == RectangleEdge.TOP) {
      x0+=xOffset;
      x1+=xOffset;
      if (!pegShadow) {
        y0+=yOffset;
      }
      y1+=yOffset;
    }
 else {
      if (base == RectangleEdge.BOTTOM) {
        x0+=xOffset;
        x1+=xOffset;
        y0+=yOffset;
        if (!pegShadow) {
          y1+=yOffset;
        }
      }
 else {
        if (base == RectangleEdge.LEFT) {
          if (!pegShadow) {
            x0+=xOffset;
          }
          x1+=xOffset;
          y0+=yOffset;
          y1+=yOffset;
        }
 else {
          if (base == RectangleEdge.RIGHT) {
            x0+=xOffset;
            if (!pegShadow) {
              x1+=xOffset;
            }
            y0+=yOffset;
            y1+=yOffset;
          }
        }
      }
    }
    return new Rectangle2D.Double(x0,y0,(x1 - x0),(y1 - y0));
  }
  /** 
 * Tests this instance for equality with an arbitrary object.
 * @param obj  the obj (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof StandardXYBarPainter)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int hash=37;
    return hash;
  }
}
