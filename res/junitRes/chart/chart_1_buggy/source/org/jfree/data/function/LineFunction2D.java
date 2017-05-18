package org.jfree.data.function;
import java.io.Serializable;
import org.jfree.chart.util.HashUtilities;
/** 
 * A function in the form y = a + bx.
 */
public class LineFunction2D implements Function2D, Serializable {
  /** 
 * The intercept. 
 */
  private double a;
  /** 
 * The slope of the line. 
 */
  private double b;
  /** 
 * Constructs a new line function.
 * @param a  the intercept.
 * @param b  the slope.
 */
  public LineFunction2D(  double a,  double b){
    this.a=a;
    this.b=b;
  }
  /** 
 * Returns the 'a' coefficient that was specified in the constructor.
 * @return The 'a' coefficient.
 * @since 1.0.14
 */
  public double getIntercept(){
    return this.a;
  }
  /** 
 * Returns the 'b' coefficient that was specified in the constructor.
 * @return The 'b' coefficient.
 * @since 1.0.14
 */
  public double getSlope(){
    return this.b;
  }
  /** 
 * Returns the function value.
 * @param x  the x-value.
 * @return The value.
 */
  public double getValue(  double x){
    return this.a + this.b * x;
  }
  /** 
 * Tests this function for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (!(obj instanceof LineFunction2D)) {
      return false;
    }
    LineFunction2D that=(LineFunction2D)obj;
    if (this.a != that.a) {
      return false;
    }
    if (this.b != that.b) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=29;
    result=HashUtilities.hashCode(result,this.a);
    result=HashUtilities.hashCode(result,this.b);
    return result;
  }
}
