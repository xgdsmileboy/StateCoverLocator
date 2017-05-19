package org.jfree.chart.labels;
import java.io.Serializable;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XisSymbolic;
import org.jfree.data.xy.YisSymbolic;
/** 
 * A standard item label generator for plots that use data from an                              {@link XYDataset}.
 */
public class SymbolicXYItemLabelGenerator implements XYItemLabelGenerator, XYToolTipGenerator, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=3963400354475494395L;
  /** 
 * Generates a tool tip text item for a particular item within a series.
 * @param data  the dataset.
 * @param series  the series number (zero-based index).
 * @param item  the item number (zero-based index).
 * @return The tool tip text (possibly <code>null</code>).
 */
  public String generateToolTip(  XYDataset data,  int series,  int item){
    String xStr, yStr;
    if (data instanceof YisSymbolic) {
      yStr=((YisSymbolic)data).getYSymbolicValue(series,item);
    }
 else {
      double y=data.getYValue(series,item);
      yStr=Double.toString(round(y,2));
    }
    if (data instanceof XisSymbolic) {
      xStr=((XisSymbolic)data).getXSymbolicValue(series,item);
    }
 else {
      if (data instanceof TimeSeriesCollection) {
        RegularTimePeriod p=((TimeSeriesCollection)data).getSeries(series).getTimePeriod(item);
        xStr=p.toString();
      }
 else {
        double x=data.getXValue(series,item);
        xStr=Double.toString(round(x,2));
      }
    }
    return "X: " + xStr + ", Y: "+ yStr;
  }
  /** 
 * Generates a label for the specified item. The label is typically a formatted version of the data value, but any text can be used.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series index (zero-based).
 * @param category  the category index (zero-based).
 * @return The label (possibly <code>null</code>).
 */
  public String generateLabel(  XYDataset dataset,  int series,  int category){
    return null;
  }
  /** 
 * Round a double value.
 * @param value  the value.
 * @param nb  the exponent.
 * @return The rounded value.
 */
  private static double round(  double value,  int nb){
    if (nb <= 0) {
      return Math.floor(value + 0.5d);
    }
    double p=Math.pow(10,nb);
    double tempval=Math.floor(value * p + 0.5d);
    return tempval / p;
  }
  /** 
 * Returns an independent copy of the generator.
 * @return A clone.
 * @throws CloneNotSupportedException if cloning is not supported.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  /** 
 * Tests if this object is equal to another.
 * @param obj  the other object.
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (obj instanceof SymbolicXYItemLabelGenerator) {
      return true;
    }
    return false;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=127;
    return result;
  }
}
