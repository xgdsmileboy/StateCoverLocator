package org.jfree.data.xy;
import java.io.Serializable;
/** 
 * A vector.
 * @since 1.0.6
 */
public class Vector implements Serializable {
  /** 
 * The vector x. 
 */
  private double x;
  /** 
 * The vector y. 
 */
  private double y;
  /** 
 * Creates a new instance of <code>Vector</code>.
 * @param x  the x-component.
 * @param y  the y-component.
 */
  public Vector(  double x,  double y){
    this.x=x;
    this.y=y;
  }
  /** 
 * Returns the x-value.
 * @return The x-value.
 */
  public double getX(){
    return this.x;
  }
  /** 
 * Returns the y-value.
 * @return The y-value.
 */
  public double getY(){
    return this.y;
  }
  /** 
 * Returns the length of the vector.
 * @return The vector length.
 */
  public double getLength(){
    return Math.sqrt((this.x * this.x) + (this.y * this.y));
  }
  /** 
 * Returns the angle of the vector.
 * @return The angle of the vector.
 */
  public double getAngle(){
    return Math.atan2(this.y,this.x);
  }
  /** 
 * Tests this vector for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> not permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Vector)) {
      return false;
    }
    Vector that=(Vector)obj;
    if (this.x != that.x) {
      return false;
    }
    if (this.y != that.y) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=193;
    long temp=Double.doubleToLongBits(this.x);
    result=37 * result + (int)(temp ^ (temp >>> 32));
    temp=Double.doubleToLongBits(this.y);
    result=37 * result + (int)(temp ^ (temp >>> 32));
    return result;
  }
}
