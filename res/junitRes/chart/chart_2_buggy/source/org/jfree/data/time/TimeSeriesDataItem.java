package org.jfree.data.time;
import java.io.Serializable;
import org.jfree.chart.util.HashUtilities;
import org.jfree.chart.util.ObjectUtilities;
/** 
 * Represents one data item in a time series. <P> The time period can be any of the following: <ul> <li>                                                                                                                                                                   {@link Year}</li> <li>                                                                                                                                                                   {@link Quarter}</li> <li>                                                                                                                                                                   {@link Month}</li> <li>                                                                                                                                                                   {@link Week}</li> <li>                                                                                                                                                                   {@link Day}</li> <li>                                                                                                                                                                   {@link Hour}</li> <li>                                                                                                                                                                   {@link Minute}</li> <li>                                                                                                                                                                   {@link Second}</li> <li>                                                                                                                                                                   {@link Millisecond}</li> <li>                                                                                                                                                                   {@link FixedMillisecond}</li> </ul> The time period is an immutable property of the data item.  Data items will often be sorted within a list, and allowing the time period to be changed could destroy the sort order. <P> Implements the <code>Comparable</code> interface so that standard Java sorting can be used to keep the data items in order.
 */
public class TimeSeriesDataItem implements Cloneable, Comparable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-2235346966016401302L;
  /** 
 * The time period. 
 */
  private RegularTimePeriod period;
  /** 
 * The value associated with the time period. 
 */
  private Number value;
  /** 
 * A flag that indicates whether or not the item is "selected".
 * @since 1.2.0
 */
  private boolean selected;
  /** 
 * Constructs a new data item that associates a value with a time period.
 * @param period  the time period (<code>null</code> not permitted).
 * @param value  the value (<code>null</code> permitted).
 */
  public TimeSeriesDataItem(  RegularTimePeriod period,  Number value){
    if (period == null) {
      throw new IllegalArgumentException("Null 'period' argument.");
    }
    this.period=period;
    this.value=value;
    this.selected=false;
  }
  /** 
 * Constructs a new data item that associates a value with a time period.
 * @param period  the time period (<code>null</code> not permitted).
 * @param value  the value associated with the time period.
 */
  public TimeSeriesDataItem(  RegularTimePeriod period,  double value){
    this(period,new Double(value));
  }
  /** 
 * Returns the time period.
 * @return The time period (never <code>null</code>).
 */
  public RegularTimePeriod getPeriod(){
    return this.period;
  }
  /** 
 * Returns the value.
 * @return The value (<code>null</code> possible).
 * @see #setValue(java.lang.Number)
 */
  public Number getValue(){
    return this.value;
  }
  /** 
 * Sets the value for this data item.  This method provides no notification of the value change - if this item belongs to a                                                                                                                                                                    {@link TimeSeries} youshould use the  {@link TimeSeries#update(int,java.lang.Number)} methodto change the value, because this will trigger a change event.
 * @param value  the value (<code>null</code> permitted).
 * @see #getValue()
 */
  public void setValue(  Number value){
    this.value=value;
  }
  /** 
 * Returns <code>true</code> if the data item is selected, and <code>false</code> otherwise.
 * @return A boolean.
 * @see #setSelected(boolean)
 * @since 1.2.0
 */
  public boolean isSelected(){
    return this.selected;
  }
  /** 
 * Sets the selection state for this item.
 * @param selected  the new selection state.
 * @see #isSelected()
 * @since 1.2.0
 */
  public void setSelected(  boolean selected){
    this.selected=selected;
  }
  /** 
 * Tests this object for equality with an arbitrary object.
 * @param obj  the other object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof TimeSeriesDataItem)) {
      return false;
    }
    TimeSeriesDataItem that=(TimeSeriesDataItem)obj;
    if (!this.period.equals(that.period)) {
      return false;
    }
    if (!ObjectUtilities.equal(this.value,that.value)) {
      return false;
    }
    if (this.selected != that.selected) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code.
 * @return A hash code.
 */
  public int hashCode(){
    int result;
    result=(this.period != null ? this.period.hashCode() : 0);
    result=29 * result + (this.value != null ? this.value.hashCode() : 0);
    result=HashUtilities.hashCode(result,this.selected);
    return result;
  }
  /** 
 * Returns an integer indicating the order of this data pair object relative to another object. <P> For the order we consider only the timing: negative == before, zero == same, positive == after.
 * @param o1  The object being compared to.
 * @return An integer indicating the order of the data item objectrelative to another object.
 */
  public int compareTo(  Object o1){
    int result;
    if (o1 instanceof TimeSeriesDataItem) {
      TimeSeriesDataItem datapair=(TimeSeriesDataItem)o1;
      result=getPeriod().compareTo(datapair.getPeriod());
    }
 else {
      result=1;
    }
    return result;
  }
  /** 
 * Clones the data item.  Note: there is no need to clone the period or value since they are immutable instances.
 * @return A clone of the data item.
 */
  public Object clone(){
    Object clone=null;
    try {
      clone=super.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return clone;
  }
}
