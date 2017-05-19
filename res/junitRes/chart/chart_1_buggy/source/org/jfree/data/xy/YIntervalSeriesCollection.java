package org.jfree.data.xy;
import java.io.Serializable;
import java.util.List;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.event.DatasetChangeEvent;
/** 
 * A collection of                               {@link YIntervalSeries} objects.
 * @since 1.0.3
 * @see YIntervalSeries
 */
public class YIntervalSeriesCollection extends AbstractIntervalXYDataset implements IntervalXYDataset, PublicCloneable, Serializable {
  /** 
 * Storage for the data series. 
 */
  private List data;
  /** 
 * Creates a new instance of <code>YIntervalSeriesCollection</code>.
 */
  public YIntervalSeriesCollection(){
    this.data=new java.util.ArrayList();
  }
  /** 
 * Adds a series to the collection and sends a                               {@link DatasetChangeEvent}to all registered listeners.
 * @param series  the series (<code>null</code> not permitted).
 */
  public void addSeries(  YIntervalSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Null 'series' argument.");
    }
    this.data.add(series);
    series.addChangeListener(this);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Returns the number of series in the collection.
 * @return The series count.
 */
  public int getSeriesCount(){
    return this.data.size();
  }
  /** 
 * Returns a series from the collection.
 * @param series  the series index (zero-based).
 * @return The series.
 * @throws IllegalArgumentException if <code>series</code> is not in therange <code>0</code> to <code>getSeriesCount() - 1</code>.
 */
  public YIntervalSeries getSeries(  int series){
    if ((series < 0) || (series >= getSeriesCount())) {
      throw new IllegalArgumentException("Series index out of bounds");
    }
    return (YIntervalSeries)this.data.get(series);
  }
  /** 
 * Returns the key for a series.
 * @param series  the series index (in the range <code>0</code> to<code>getSeriesCount() - 1</code>).
 * @return The key for a series.
 * @throws IllegalArgumentException if <code>series</code> is not in thespecified range.
 */
  public Comparable getSeriesKey(  int series){
    return getSeries(series).getKey();
  }
  /** 
 * Returns the number of items in the specified series.
 * @param series  the series (zero-based index).
 * @return The item count.
 * @throws IllegalArgumentException if <code>series</code> is not in therange <code>0</code> to <code>getSeriesCount() - 1</code>.
 */
  public int getItemCount(  int series){
    return getSeries(series).getItemCount();
  }
  /** 
 * Returns the x-value for an item within a series.
 * @param series  the series index.
 * @param item  the item index.
 * @return The x-value.
 */
  public Number getX(  int series,  int item){
    YIntervalSeries s=(YIntervalSeries)this.data.get(series);
    return s.getX(item);
  }
  /** 
 * Returns the y-value (as a double primitive) for an item within a series.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The value.
 */
  public double getYValue(  int series,  int item){
    YIntervalSeries s=(YIntervalSeries)this.data.get(series);
    return s.getYValue(item);
  }
  /** 
 * Returns the start y-value (as a double primitive) for an item within a series.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The value.
 */
  public double getStartYValue(  int series,  int item){
    YIntervalSeries s=(YIntervalSeries)this.data.get(series);
    return s.getYLowValue(item);
  }
  /** 
 * Returns the end y-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The value.
 */
  public double getEndYValue(  int series,  int item){
    YIntervalSeries s=(YIntervalSeries)this.data.get(series);
    return s.getYHighValue(item);
  }
  /** 
 * Returns the y-value for an item within a series.
 * @param series  the series index.
 * @param item  the item index.
 * @return The y-value.
 */
  public Number getY(  int series,  int item){
    YIntervalSeries s=(YIntervalSeries)this.data.get(series);
    return new Double(s.getYValue(item));
  }
  /** 
 * Returns the start x-value for an item within a series.  This method maps directly to                               {@link #getX(int,int)}.
 * @param series  the series index.
 * @param item  the item index.
 * @return The x-value.
 */
  public Number getStartX(  int series,  int item){
    return getX(series,item);
  }
  /** 
 * Returns the end x-value for an item within a series.  This method maps directly to                               {@link #getX(int,int)}.
 * @param series  the series index.
 * @param item  the item index.
 * @return The x-value.
 */
  public Number getEndX(  int series,  int item){
    return getX(series,item);
  }
  /** 
 * Returns the start y-value for an item within a series.
 * @param series  the series index.
 * @param item  the item index.
 * @return The start y-value.
 */
  public Number getStartY(  int series,  int item){
    YIntervalSeries s=(YIntervalSeries)this.data.get(series);
    return new Double(s.getYLowValue(item));
  }
  /** 
 * Returns the end y-value for an item within a series.
 * @param series  the series index.
 * @param item  the item index.
 * @return The end y-value.
 */
  public Number getEndY(  int series,  int item){
    YIntervalSeries s=(YIntervalSeries)this.data.get(series);
    return new Double(s.getYHighValue(item));
  }
  /** 
 * Removes a series from the collection and sends a                              {@link DatasetChangeEvent} to all registered listeners.
 * @param series  the series index (zero-based).
 * @since 1.0.10
 */
  public void removeSeries(  int series){
    if ((series < 0) || (series >= getSeriesCount())) {
      throw new IllegalArgumentException("Series index out of bounds.");
    }
    YIntervalSeries ts=(YIntervalSeries)this.data.get(series);
    ts.removeChangeListener(this);
    this.data.remove(series);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Removes a series from the collection and sends a                              {@link DatasetChangeEvent} to all registered listeners.
 * @param series  the series (<code>null</code> not permitted).
 * @since 1.0.10
 */
  public void removeSeries(  YIntervalSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Null 'series' argument.");
    }
    if (this.data.contains(series)) {
      series.removeChangeListener(this);
      this.data.remove(series);
      fireDatasetChanged(new DatasetChangeInfo());
    }
  }
  /** 
 * Removes all the series from the collection and sends a                              {@link DatasetChangeEvent} to all registered listeners.
 * @since 1.0.10
 */
  public void removeAllSeries(){
    for (int i=0; i < this.data.size(); i++) {
      YIntervalSeries series=(YIntervalSeries)this.data.get(i);
      series.removeChangeListener(this);
    }
    this.data.clear();
    fireDatasetChanged(new DatasetChangeInfo());
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
    if (!(obj instanceof YIntervalSeriesCollection)) {
      return false;
    }
    YIntervalSeriesCollection that=(YIntervalSeriesCollection)obj;
    return ObjectUtilities.equal(this.data,that.data);
  }
  /** 
 * Returns a clone of this instance.
 * @return A clone.
 * @throws CloneNotSupportedException if there is a problem.
 */
  public Object clone() throws CloneNotSupportedException {
    YIntervalSeriesCollection clone=(YIntervalSeriesCollection)super.clone();
    clone.data=(List)ObjectUtilities.deepClone(this.data);
    return clone;
  }
}
