package org.jfree.chart.needle;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
/** 
 * A needle for use with the                                                                                                                                                                     {@link org.jfree.chart.plot.CompassPlot} class.
 */
public class PlumNeedle extends MeterNeedle implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-3082660488660600718L;
  /** 
 * Draws the needle.
 * @param g2  the graphics device.
 * @param plotArea  the plot area.
 * @param rotate  the rotation point.
 * @param angle  the angle.
 */
  protected void drawNeedle(  Graphics2D g2,  Rectangle2D plotArea,  Point2D rotate,  double angle){
    Arc2D shape=new Arc2D.Double(Arc2D.PIE);
    double radius=plotArea.getHeight();
    double halfX=plotArea.getWidth() / 2;
    double diameter=2 * radius;
    shape.setFrame(plotArea.getMinX() + halfX - radius,plotArea.getMinY() - radius,diameter,diameter);
    radius=Math.toDegrees(Math.asin(halfX / radius));
    shape.setAngleStart(270 - radius);
    shape.setAngleExtent(2 * radius);
    Area s=new Area(shape);
    if ((rotate != null) && (angle != 0)) {
      getTransform().setToRotation(angle,rotate.getX(),rotate.getY());
      s.transform(getTransform());
    }
    defaultDisplay(g2,s);
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
    if (!(obj instanceof PlumNeedle)) {
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
 * @throws CloneNotSupportedException if the <code>PlumNeedle</code>cannot be cloned (in theory, this should not happen).
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
