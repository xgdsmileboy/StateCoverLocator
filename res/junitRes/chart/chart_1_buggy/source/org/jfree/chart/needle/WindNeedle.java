package org.jfree.chart.needle;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
/** 
 * A needle that indicates wind direction, for use with the     {@link org.jfree.chart.plot.CompassPlot} class.
 */
public class WindNeedle extends ArrowNeedle implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-2861061368907167834L;
  /** 
 * Default constructor.
 */
  public WindNeedle(){
    super(false);
  }
  /** 
 * Draws the needle.
 * @param g2  the graphics device.
 * @param plotArea  the plot area.
 * @param rotate  the rotation point.
 * @param angle  the angle.
 */
  protected void drawNeedle(  Graphics2D g2,  Rectangle2D plotArea,  Point2D rotate,  double angle){
    super.drawNeedle(g2,plotArea,rotate,angle);
    if ((rotate != null) && (plotArea != null)) {
      int spacing=getSize() * 3;
      Rectangle2D newArea=new Rectangle2D.Double();
      Point2D newRotate=rotate;
      newArea.setRect(plotArea.getMinX() - spacing,plotArea.getMinY(),plotArea.getWidth(),plotArea.getHeight());
      super.drawNeedle(g2,newArea,newRotate,angle);
      newArea.setRect(plotArea.getMinX() + spacing,plotArea.getMinY(),plotArea.getWidth(),plotArea.getHeight());
      super.drawNeedle(g2,newArea,newRotate,angle);
    }
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
    if (super.equals(object) && object instanceof WindNeedle) {
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
}
