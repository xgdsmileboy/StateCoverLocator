package org.jfree.chart.needle;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
/** 
 * A needle in the shape of a pointer, for use with the                                                                                                                                                                    {@link org.jfree.chart.plot.CompassPlot} class.
 */
public class PointerNeedle extends MeterNeedle implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-4744677345334729606L;
  /** 
 * Draws the needle.
 * @param g2  the graphics device.
 * @param plotArea  the plot area.
 * @param rotate  the rotation point.
 * @param angle  the angle.
 */
  protected void drawNeedle(  Graphics2D g2,  Rectangle2D plotArea,  Point2D rotate,  double angle){
    GeneralPath shape1=new GeneralPath();
    GeneralPath shape2=new GeneralPath();
    float minX=(float)plotArea.getMinX();
    float minY=(float)plotArea.getMinY();
    float maxX=(float)plotArea.getMaxX();
    float maxY=(float)plotArea.getMaxY();
    float midX=(float)(minX + (plotArea.getWidth() / 2));
    float midY=(float)(minY + (plotArea.getHeight() / 2));
    shape1.moveTo(minX,midY);
    shape1.lineTo(midX,minY);
    shape1.lineTo(maxX,midY);
    shape1.closePath();
    shape2.moveTo(minX,midY);
    shape2.lineTo(midX,maxY);
    shape2.lineTo(maxX,midY);
    shape2.closePath();
    if ((rotate != null) && (angle != 0)) {
      getTransform().setToRotation(angle,rotate.getX(),rotate.getY());
      shape1.transform(getTransform());
      shape2.transform(getTransform());
    }
    if (getFillPaint() != null) {
      g2.setPaint(getFillPaint());
      g2.fill(shape1);
    }
    if (getHighlightPaint() != null) {
      g2.setPaint(getHighlightPaint());
      g2.fill(shape2);
    }
    if (getOutlinePaint() != null) {
      g2.setStroke(getOutlineStroke());
      g2.setPaint(getOutlinePaint());
      g2.draw(shape1);
      g2.draw(shape2);
    }
  }
  /** 
 * Tests another object for equality with this object.
 * @param obj  the object to test (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof PointerNeedle)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    return super.hashCode();
  }
  /** 
 * Returns a clone of this needle.
 * @return A clone.
 * @throws CloneNotSupportedException if the <code>PointerNeedle</code>cannot be cloned (in theory, this should not happen).
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
