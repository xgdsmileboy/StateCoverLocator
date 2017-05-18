package org.jfree.chart.needle;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.util.HashUtilities;
/** 
 * A needle in the shape of an arrow.
 */
public class ArrowNeedle extends MeterNeedle implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-5334056511213782357L;
  /** 
 * A flag controlling whether or not there is an arrow at the top of the needle.
 */
  private boolean isArrowAtTop=true;
  /** 
 * Constructs a new arrow needle.
 * @param isArrowAtTop  a flag that controls whether or not there is anarrow at the top of the needle.
 */
  public ArrowNeedle(  boolean isArrowAtTop){
    this.isArrowAtTop=isArrowAtTop;
  }
  /** 
 * Draws the needle.
 * @param g2  the graphics device.
 * @param plotArea  the plot area.
 * @param rotate  the rotation point.
 * @param angle  the angle.
 */
  protected void drawNeedle(  Graphics2D g2,  Rectangle2D plotArea,  Point2D rotate,  double angle){
    Line2D shape=new Line2D.Float();
    Shape d=null;
    float x=(float)(plotArea.getMinX() + (plotArea.getWidth() / 2));
    float minY=(float)plotArea.getMinY();
    float maxY=(float)plotArea.getMaxY();
    shape.setLine(x,minY,x,maxY);
    GeneralPath shape1=new GeneralPath();
    if (this.isArrowAtTop) {
      shape1.moveTo(x,minY);
      minY+=4 * getSize();
    }
 else {
      shape1.moveTo(x,maxY);
      minY=maxY - 4 * getSize();
    }
    shape1.lineTo(x + getSize(),minY);
    shape1.lineTo(x - getSize(),minY);
    shape1.closePath();
    if ((rotate != null) && (angle != 0)) {
      getTransform().setToRotation(angle,rotate.getX(),rotate.getY());
      d=getTransform().createTransformedShape(shape);
    }
 else {
      d=shape;
    }
    defaultDisplay(g2,d);
    if ((rotate != null) && (angle != 0)) {
      d=getTransform().createTransformedShape(shape1);
    }
 else {
      d=shape1;
    }
    defaultDisplay(g2,d);
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
    if (!(obj instanceof ArrowNeedle)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    ArrowNeedle that=(ArrowNeedle)obj;
    if (this.isArrowAtTop != that.isArrowAtTop) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=super.hashCode();
    result=HashUtilities.hashCode(result,this.isArrowAtTop);
    return result;
  }
  /** 
 * Returns a clone of this needle.
 * @return A clone.
 * @throws CloneNotSupportedException if the <code>ArrowNeedle</code>cannot be cloned (in theory, this should not happen).
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
