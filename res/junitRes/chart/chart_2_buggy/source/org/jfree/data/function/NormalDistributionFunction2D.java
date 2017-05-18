package org.jfree.data.function;
import java.io.Serializable;
import org.jfree.chart.util.HashUtilities;
/** 
 * A normal distribution function.  See http://en.wikipedia.org/wiki/Normal_distribution.
 */
public class NormalDistributionFunction2D implements Function2D, Serializable {
  /** 
 * The mean. 
 */
  private double mean;
  /** 
 * The standard deviation. 
 */
  private double std;
  /** 
 * Precomputed factor for the function value. 
 */
  private double factor;
  /** 
 * Precomputed denominator for the function value. 
 */
  private double denominator;
  /** 
 * Constructs a new normal distribution function.
 * @param mean  the mean.
 * @param std  the standard deviation (> 0).
 */
  public NormalDistributionFunction2D(  double mean,  double std){
    if (std <= 0) {
      throw new IllegalArgumentException("Requires 'std' > 0.");
    }
    this.mean=mean;
    this.std=std;
    this.factor=1 / (std * Math.sqrt(2.0 * Math.PI));
    this.denominator=2 * std * std;
  }
  /** 
 * Returns the mean for the function.
 * @return The mean.
 */
  public double getMean(){
    return this.mean;
  }
  /** 
 * Returns the standard deviation for the function.
 * @return The standard deviation.
 */
  public double getStandardDeviation(){
    return this.std;
  }
  /** 
 * Returns the function value.
 * @param x  the x-value.
 * @return The value.
 */
  public double getValue(  double x){
    double z=x - this.mean;
    return this.factor * Math.exp(-z * z / this.denominator);
  }
  /** 
 * Tests this function for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (!(obj instanceof NormalDistributionFunction2D)) {
      return false;
    }
    NormalDistributionFunction2D that=(NormalDistributionFunction2D)obj;
    if (this.mean != that.mean) {
      return false;
    }
    if (this.std != that.std) {
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
    result=HashUtilities.hashCode(result,this.mean);
    result=HashUtilities.hashCode(result,this.std);
    return result;
  }
}
