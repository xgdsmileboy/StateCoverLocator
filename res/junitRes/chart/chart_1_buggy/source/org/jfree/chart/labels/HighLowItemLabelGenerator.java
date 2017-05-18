package org.jfree.chart.labels;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import org.jfree.chart.util.HashUtilities;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
/** 
 * A standard item label generator for plots that use data from a      {@link OHLCDataset}.
 */
public class HighLowItemLabelGenerator implements XYItemLabelGenerator, XYToolTipGenerator, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=5617111754832211830L;
  /** 
 * The date formatter. 
 */
  private DateFormat dateFormatter;
  /** 
 * The number formatter. 
 */
  private NumberFormat numberFormatter;
  /** 
 * Creates an item label generator using the default date and number  formats.
 */
  public HighLowItemLabelGenerator(){
    this(DateFormat.getInstance(),NumberFormat.getInstance());
  }
  /** 
 * Creates a tool tip generator using the supplied date formatter.
 * @param dateFormatter  the date formatter (<code>null</code> not permitted).
 * @param numberFormatter  the number formatter (<code>null</code> not permitted).
 */
  public HighLowItemLabelGenerator(  DateFormat dateFormatter,  NumberFormat numberFormatter){
    if (dateFormatter == null) {
      throw new IllegalArgumentException("Null 'dateFormatter' argument.");
    }
    if (numberFormatter == null) {
      throw new IllegalArgumentException("Null 'numberFormatter' argument.");
    }
    this.dateFormatter=dateFormatter;
    this.numberFormatter=numberFormatter;
  }
  /** 
 * Generates a tooltip text item for a particular item within a series.
 * @param dataset  the dataset.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The tooltip text.
 */
  public String generateToolTip(  XYDataset dataset,  int series,  int item){
    String result=null;
    if (dataset instanceof OHLCDataset) {
      OHLCDataset d=(OHLCDataset)dataset;
      Number high=d.getHigh(series,item);
      Number low=d.getLow(series,item);
      Number open=d.getOpen(series,item);
      Number close=d.getClose(series,item);
      Number x=d.getX(series,item);
      result=d.getSeriesKey(series).toString();
      if (x != null) {
        Date date=new Date(x.longValue());
        result=result + "--> Date=" + this.dateFormatter.format(date);
        if (high != null) {
          result=result + " High=" + this.numberFormatter.format(high.doubleValue());
        }
        if (low != null) {
          result=result + " Low=" + this.numberFormatter.format(low.doubleValue());
        }
        if (open != null) {
          result=result + " Open=" + this.numberFormatter.format(open.doubleValue());
        }
        if (close != null) {
          result=result + " Close=" + this.numberFormatter.format(close.doubleValue());
        }
      }
    }
    return result;
  }
  /** 
 * Generates a label for the specified item. The label is typically a  formatted version of the data value, but any text can be used.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series index (zero-based).
 * @param category  the category index (zero-based).
 * @return The label (possibly <code>null</code>).
 */
  public String generateLabel(  XYDataset dataset,  int series,  int category){
    return null;
  }
  /** 
 * Returns an independent copy of the generator.
 * @return A clone.
 * @throws CloneNotSupportedException if cloning is not supported.
 */
  public Object clone() throws CloneNotSupportedException {
    HighLowItemLabelGenerator clone=(HighLowItemLabelGenerator)super.clone();
    if (this.dateFormatter != null) {
      clone.dateFormatter=(DateFormat)this.dateFormatter.clone();
    }
    if (this.numberFormatter != null) {
      clone.numberFormatter=(NumberFormat)this.numberFormatter.clone();
    }
    return clone;
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
    if (!(obj instanceof HighLowItemLabelGenerator)) {
      return false;
    }
    HighLowItemLabelGenerator generator=(HighLowItemLabelGenerator)obj;
    if (!this.dateFormatter.equals(generator.dateFormatter)) {
      return false;
    }
    if (!this.numberFormatter.equals(generator.numberFormatter)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=127;
    result=HashUtilities.hashCode(result,this.dateFormatter);
    result=HashUtilities.hashCode(result,this.numberFormatter);
    return result;
  }
}
