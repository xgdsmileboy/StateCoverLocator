package org.jfree.data.time.ohlc;
import org.jfree.data.ComparableObjectItem;
import org.jfree.data.time.RegularTimePeriod;
/** 
 * An item representing data in the form (period, open, high, low, close).
 * @since 1.0.4
 */
public class OHLCItem extends ComparableObjectItem {
  /** 
 * Creates a new instance of <code>OHLCItem</code>.
 * @param period  the time period.
 * @param open  the open-value.
 * @param high  the high-value.
 * @param low  the low-value.
 * @param close  the close-value.
 */
  public OHLCItem(  RegularTimePeriod period,  double open,  double high,  double low,  double close){
    super(period,new OHLC(open,high,low,close));
  }
  /** 
 * Returns the period.
 * @return The period (never <code>null</code>).
 */
  public RegularTimePeriod getPeriod(){
    return (RegularTimePeriod)getComparable();
  }
  /** 
 * Returns the y-value.
 * @return The y-value.
 */
  public double getYValue(){
    return getCloseValue();
  }
  /** 
 * Returns the open value.
 * @return The open value.
 */
  public double getOpenValue(){
    OHLC ohlc=(OHLC)getObject();
    if (ohlc != null) {
      return ohlc.getOpen();
    }
 else {
      return Double.NaN;
    }
  }
  /** 
 * Returns the high value.
 * @return The high value.
 */
  public double getHighValue(){
    OHLC ohlc=(OHLC)getObject();
    if (ohlc != null) {
      return ohlc.getHigh();
    }
 else {
      return Double.NaN;
    }
  }
  /** 
 * Returns the low value.
 * @return The low value.
 */
  public double getLowValue(){
    OHLC ohlc=(OHLC)getObject();
    if (ohlc != null) {
      return ohlc.getLow();
    }
 else {
      return Double.NaN;
    }
  }
  /** 
 * Returns the close value.
 * @return The close value.
 */
  public double getCloseValue(){
    OHLC ohlc=(OHLC)getObject();
    if (ohlc != null) {
      return ohlc.getClose();
    }
 else {
      return Double.NaN;
    }
  }
}
