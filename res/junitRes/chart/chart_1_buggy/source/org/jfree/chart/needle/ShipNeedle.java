package org.jfree.chart.needle;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
/** 
 * A needle in the shape of a ship, for use with the     {@link org.jfree.chart.plot.CompassPlot} class.
 */
public class ShipNeedle extends MeterNeedle implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=149554868169435612L;
  /** 
 * Draws the needle.
 * @param g2  the graphics device.
 * @param plotArea  the plot area.
 * @param rotate  the rotation point.
 * @param angle  the angle.
 */
  protected void drawNeedle(  Graphics2D g2,  Rectangle2D plotArea,  Point2D rotate,  double angle){
    GeneralPath shape=new GeneralPath();
    shape.append(new Arc2D.Double(-9.0,-7.0,10,14,0.0,25.5,Arc2D.OPEN),true);
    shape.append(new Arc2D.Double(0.0,-7.0,10,14,154.5,25.5,Arc2D.OPEN),true);
    shape.closePath();
    getTransform().setToTranslation(plotArea.getMinX(),plotArea.getMaxY());
    getTransform().scale(plotArea.getWidth(),plotArea.getHeight() / 3);
    shape.transform(getTransform());
    if ((rotate != null) && (angle != 0)) {
      getTransform().setToRotation(angle,rotate.getX(),rotate.getY());
      shape.transform(getTransform());
    }
    defaultDisplay(g2,shape);
  }
  /** 
 * Tests another object for equality with this object.
 * @param object  the object to test.
 * @return A boolean.
 */
  public boolean equals(  Object object){
    if (object == null) {
      return false;
    }
    if (object == this) {
      return true;
    }
    if (super.equals(object) && object instanceof ShipNeedle) {
      return true;
    }
    return false;
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
 * @throws CloneNotSupportedException if the <code>ShipNeedle</code>cannot be cloned (in theory, this should not happen).
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
