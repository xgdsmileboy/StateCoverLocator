package org.jfree.data.time.ohlc;
import java.io.Serializable;
import org.jfree.chart.util.HashUtilities;
/** 
 * A high low data record (immutable).  This class is used internally by the                              {@link OHLCItem} class.
 * @since 1.0.4
 */
public class OHLC implements Serializable {
  /** 
 * The open value. 
 */
  private double open;
  /** 
 * The close value. 
 */
  private double close;
  /** 
 * The high value. 
 */
  private double high;
  /** 
 * The low value. 
 */
  private double low;
  /** 
 * Creates a new instance of <code>OHLC</code>.
 * @param open  the open value.
 * @param close  the close value.
 * @param high  the high value.
 * @param low  the low value.
 */
  public OHLC(  double open,  double high,  double low,  double close){
    this.open=open;
    this.close=close;
    this.high=high;
    this.low=low;
  }
  /** 
 * Returns the open value.
 * @return The open value.
 */
  public double getOpen(){
    return this.open;
  }
  /** 
 * Returns the close value.
 * @return The close value.
 */
  public double getClose(){
    return this.close;
  }
  /** 
 * Returns the high value.
 * @return The high value.
 */
  public double getHigh(){
    return this.high;
  }
  /** 
 * Returns the low value.
 * @return The low value.
 */
  public double getLow(){
    return this.low;
  }
  /** 
 * Tests this instance for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof OHLC)) {
      return false;
    }
    OHLC that=(OHLC)obj;
    if (this.open != that.open) {
      return false;
    }
    if (this.close != that.close) {
      return false;
    }
    if (this.high != that.high) {
      return false;
    }
    if (this.low != that.low) {
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
    result=HashUtilities.hashCode(result,this.open);
    result=HashUtilities.hashCode(result,this.high);
    result=HashUtilities.hashCode(result,this.low);
    result=HashUtilities.hashCode(result,this.close);
    return result;
  }
}
