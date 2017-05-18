package org.jfree.chart.needle;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
/** 
 * A needle that is represented by a line.
 */
public class LineNeedle extends MeterNeedle implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=6215321387896748945L;
  /** 
 * Draws the needle.
 * @param g2  the graphics device.
 * @param plotArea  the plot area.
 * @param rotate  the rotation point.
 * @param angle  the angle.
 */
  protected void drawNeedle(  Graphics2D g2,  Rectangle2D plotArea,  Point2D rotate,  double angle){
    Line2D shape=new Line2D.Double();
    double x=plotArea.getMinX() + (plotArea.getWidth() / 2);
    shape.setLine(x,plotArea.getMinY(),x,plotArea.getMaxY());
    Shape s=shape;
    if ((rotate != null) && (angle != 0)) {
      getTransform().setToRotation(angle,rotate.getX(),rotate.getY());
      s=getTransform().createTransformedShape(s);
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
    if (!(obj instanceof LineNeedle)) {
      return false;
    }
    return super.equals(obj);
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
 * @throws CloneNotSupportedException if the <code>LineNeedle</code>cannot be cloned (in theory, this should not happen).
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
