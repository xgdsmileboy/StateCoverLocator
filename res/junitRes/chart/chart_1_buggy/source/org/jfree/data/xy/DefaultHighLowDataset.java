package org.jfree.data.xy;
import java.util.Arrays;
import java.util.Date;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.chart.util.PublicCloneable;
/** 
 * A simple implementation of the                               {@link OHLCDataset} interface.  See alsothe  {@link DefaultOHLCDataset} class, which provides another implementationthat is very similar.
 */
public class DefaultHighLowDataset extends AbstractXYDataset implements OHLCDataset, SelectableXYDataset, XYDatasetSelectionState, PublicCloneable {
  /** 
 * The series key. 
 */
  private Comparable seriesKey;
  /** 
 * Storage for the dates. 
 */
  private Date[] date;
  /** 
 * Storage for the high values. 
 */
  private Number[] high;
  /** 
 * Storage for the low values. 
 */
  private Number[] low;
  /** 
 * Storage for the open values. 
 */
  private Number[] open;
  /** 
 * Storage for the close values. 
 */
  private Number[] close;
  /** 
 * Storage for the volume values. 
 */
  private Number[] volume;
  private boolean[] selected;
  /** 
 * Constructs a new high/low/open/close dataset. <p> The current implementation allows only one series in the dataset. This may be extended in a future version.
 * @param seriesKey  the key for the series (<code>null</code> notpermitted).
 * @param date  the dates (<code>null</code> not permitted).
 * @param high  the high values (<code>null</code> not permitted).
 * @param low  the low values (<code>null</code> not permitted).
 * @param open  the open values (<code>null</code> not permitted).
 * @param close  the close values (<code>null</code> not permitted).
 * @param volume  the volume values (<code>null</code> not permitted).
 */
  public DefaultHighLowDataset(  Comparable seriesKey,  Date[] date,  double[] high,  double[] low,  double[] open,  double[] close,  double[] volume){
    if (seriesKey == null) {
      throw new IllegalArgumentException("Null 'series' argument.");
    }
    if (date == null) {
      throw new IllegalArgumentException("Null 'date' argument.");
    }
    this.seriesKey=seriesKey;
    this.date=date;
    this.selected=new boolean[this.date.length];
    this.high=createNumberArray(high);
    this.low=createNumberArray(low);
    this.open=createNumberArray(open);
    this.close=createNumberArray(close);
    this.volume=createNumberArray(volume);
    setSelectionState(this);
  }
  /** 
 * Returns the key for the series stored in this dataset.
 * @param series  the index of the series (ignored, this dataset supportsonly one series and this method always returns the key for series 0).
 * @return The series key (never <code>null</code>).
 */
  public Comparable getSeriesKey(  int series){
    return this.seriesKey;
  }
  /** 
 * Returns the x-value for one item in a series.  The value returned is a <code>Long</code> instance generated from the underlying <code>Date</code> object.  To avoid generating a new object instance, you might prefer to call                               {@link #getXValue(int,int)}.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The x-value.
 * @see #getXValue(int,int)
 * @see #getXDate(int,int)
 */
  public Number getX(  int series,  int item){
    return new Long(this.date[item].getTime());
  }
  /** 
 * Returns the x-value for one item in a series, as a Date. <p> This method is provided for convenience only.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The x-value as a Date.
 * @see #getX(int,int)
 */
  public Date getXDate(  int series,  int item){
    return this.date[item];
  }
  /** 
 * Returns the y-value for one item in a series. <p> This method (from the                               {@link XYDataset} interface) is mapped to the{@link #getCloseValue(int,int)} method.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The y-value.
 * @see #getYValue(int,int)
 */
  public Number getY(  int series,  int item){
    return getClose(series,item);
  }
  /** 
 * Returns the high-value for one item in a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The high-value.
 * @see #getHighValue(int,int)
 */
  public Number getHigh(  int series,  int item){
    return this.high[item];
  }
  /** 
 * Returns the high-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The high-value.
 * @see #getHigh(int,int)
 */
  public double getHighValue(  int series,  int item){
    double result=Double.NaN;
    Number n=getHigh(series,item);
    if (n != null) {
      result=n.doubleValue();
    }
    return result;
  }
  /** 
 * Returns the low-value for one item in a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The low-value.
 * @see #getLowValue(int,int)
 */
  public Number getLow(  int series,  int item){
    return this.low[item];
  }
  /** 
 * Returns the low-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The low-value.
 * @see #getLow(int,int)
 */
  public double getLowValue(  int series,  int item){
    double result=Double.NaN;
    Number low=getLow(series,item);
    if (low != null) {
      result=low.doubleValue();
    }
    return result;
  }
  /** 
 * Returns the open-value for one item in a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The open-value.
 * @see #getOpenValue(int,int)
 */
  public Number getOpen(  int series,  int item){
    return this.open[item];
  }
  /** 
 * Returns the open-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The open-value.
 * @see #getOpen(int,int)
 */
  public double getOpenValue(  int series,  int item){
    double result=Double.NaN;
    Number open=getOpen(series,item);
    if (open != null) {
      result=open.doubleValue();
    }
    return result;
  }
  /** 
 * Returns the close-value for one item in a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The close-value.
 * @see #getCloseValue(int,int)
 */
  public Number getClose(  int series,  int item){
    return this.close[item];
  }
  /** 
 * Returns the close-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The close-value.
 * @see #getClose(int,int)
 */
  public double getCloseValue(  int series,  int item){
    double result=Double.NaN;
    Number close=getClose(series,item);
    if (close != null) {
      result=close.doubleValue();
    }
    return result;
  }
  /** 
 * Returns the volume-value for one item in a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The volume-value.
 * @see #getVolumeValue(int,int)
 */
  public Number getVolume(  int series,  int item){
    return this.volume[item];
  }
  /** 
 * Returns the volume-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The volume-value.
 * @see #getVolume(int,int)
 */
  public double getVolumeValue(  int series,  int item){
    double result=Double.NaN;
    Number volume=getVolume(series,item);
    if (volume != null) {
      result=volume.doubleValue();
    }
    return result;
  }
  /** 
 * Returns the number of series in the dataset. <p> This implementation only allows one series.
 * @return The number of series.
 */
  public int getSeriesCount(){
    return 1;
  }
  /** 
 * Returns the number of items in the specified series.
 * @param series  the index (zero-based) of the series.
 * @return The number of items in the specified series.
 */
  public int getItemCount(  int series){
    return this.date.length;
  }
  /** 
 * Tests this dataset for equality with an arbitrary instance.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof DefaultHighLowDataset)) {
      return false;
    }
    DefaultHighLowDataset that=(DefaultHighLowDataset)obj;
    if (!this.seriesKey.equals(that.seriesKey)) {
      return false;
    }
    if (!Arrays.equals(this.date,that.date)) {
      return false;
    }
    if (!Arrays.equals(this.open,that.open)) {
      return false;
    }
    if (!Arrays.equals(this.high,that.high)) {
      return false;
    }
    if (!Arrays.equals(this.low,that.low)) {
      return false;
    }
    if (!Arrays.equals(this.close,that.close)) {
      return false;
    }
    if (!Arrays.equals(this.volume,that.volume)) {
      return false;
    }
    return true;
  }
  /** 
 * Constructs an array of Number objects from an array of doubles.
 * @param data  the double values to convert (<code>null</code> notpermitted).
 * @return The data as an array of Number objects.
 */
  public static Number[] createNumberArray(  double[] data){
    Number[] result=new Number[data.length];
    for (int i=0; i < data.length; i++) {
      result[i]=new Double(data[i]);
    }
    return result;
  }
  public boolean isSelected(  int series,  int item){
    return this.selected[item];
  }
  public void setSelected(  int series,  int item,  boolean selected){
    setSelected(series,item,selected,true);
  }
  public void setSelected(  int series,  int item,  boolean selected,  boolean notify){
    if (series != 0) {
      throw new IllegalArgumentException("Invalid series: " + series);
    }
    this.selected[item]=selected;
    if (notify) {
      fireSelectionEvent();
    }
  }
  public void fireSelectionEvent(){
    fireDatasetChanged(new DatasetChangeInfo());
  }
  public void clearSelection(){
    Arrays.fill(this.selected,false);
    fireSelectionEvent();
  }
}
