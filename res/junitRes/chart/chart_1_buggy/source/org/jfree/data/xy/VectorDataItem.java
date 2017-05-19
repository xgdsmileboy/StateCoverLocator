package org.jfree.data.xy;
import org.jfree.data.ComparableObjectItem;
/** 
 * A data item representing data in the form (x, y, deltaX, deltaY), intended for use by the                               {@link VectorSeries} class.
 * @since 1.0.6
 */
public class VectorDataItem extends ComparableObjectItem {
  /** 
 * Creates a new instance of <code>VectorDataItem</code>.
 * @param x  the x-value.
 * @param y  the y-value.
 * @param deltaX  the vector x.
 * @param deltaY  the vector y.
 */
  public VectorDataItem(  double x,  double y,  double deltaX,  double deltaY){
    super(new XYCoordinate(x,y),new Vector(deltaX,deltaY));
  }
  /** 
 * Returns the x-value.
 * @return The x-value (never <code>null</code>).
 */
  public double getXValue(){
    XYCoordinate xy=(XYCoordinate)getComparable();
    return xy.getX();
  }
  /** 
 * Returns the y-value.
 * @return The y-value.
 */
  public double getYValue(){
    XYCoordinate xy=(XYCoordinate)getComparable();
    return xy.getY();
  }
  /** 
 * Returns the vector.
 * @return The vector (possibly <code>null</code>).
 */
  public Vector getVector(){
    return (Vector)getObject();
  }
  /** 
 * Returns the x-component for the vector.
 * @return The x-component.
 */
  public double getVectorX(){
    Vector vi=(Vector)getObject();
    if (vi != null) {
      return vi.getX();
    }
 else {
      return Double.NaN;
    }
  }
  /** 
 * Returns the y-component for the vector.
 * @return The y-component.
 */
  public double getVectorY(){
    Vector vi=(Vector)getObject();
    if (vi != null) {
      return vi.getY();
    }
 else {
      return Double.NaN;
    }
  }
}
