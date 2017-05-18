package org.jfree.data.function;
/** 
 * A function of the form <code>y = f(x)</code>.
 */
public interface Function2D {
  /** 
 * Returns the value of the function ('y') for a given input ('x').
 * @param x  the x-value.
 * @return The function value.
 */
  public double getValue(  double x);
}
