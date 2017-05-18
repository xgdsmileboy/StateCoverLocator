package org.jfree.data.function;
import java.io.Serializable;
import java.util.Arrays;
import org.jfree.chart.util.HashUtilities;
/** 
 * A function in the form <code>y = a0 + a1 * x + a2 * x^2 + ... + an  x^n</code>.  Instances of this class are immutable.
 * @since 1.0.14
 */
public class PolynomialFunction2D implements Function2D, Serializable {
  /** 
 * The coefficients. 
 */
  private double[] coefficients;
  /** 
 * Constructs a new polynomial function <code>y = a0 + a1 * x + a2 * x^2 + ... + an * x^n</code>
 * @param coefficients  an array with the coefficients [a0, a1, ..., an](<code>null</code> not permitted).
 */
  public PolynomialFunction2D(  double[] coefficients){
    if (coefficients == null) {
      throw new IllegalArgumentException("Null 'coefficients' argument");
    }
    this.coefficients=(double[])coefficients.clone();
  }
  /** 
 * Returns a copy of the coefficients array that was specified in the constructor.
 * @return The coefficients array.
 */
  public double[] getCoefficients(){
    return (double[])this.coefficients.clone();
  }
  /** 
 * Returns the order of the polynomial.
 * @return The order.
 */
  public int getOrder(){
    return this.coefficients.length - 1;
  }
  /** 
 * Returns the function value.
 * @param x  the x-value.
 * @return The value.
 */
  public double getValue(  double x){
    double y=0;
    for (int i=0; i < coefficients.length; i++) {
      y+=coefficients[i] * Math.pow(x,i);
    }
    return y;
  }
  /** 
 * Tests this function for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (!(obj instanceof PolynomialFunction2D)) {
      return false;
    }
    PolynomialFunction2D that=(PolynomialFunction2D)obj;
    return Arrays.equals(this.coefficients,that.coefficients);
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    return HashUtilities.hashCodeForDoubleArray(this.coefficients);
  }
}
