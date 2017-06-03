package org.jfree.data.xy;
import java.io.Serializable;
import java.util.List;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.event.DatasetChangeEvent;
/** 
 * A collection of                                                                                                                                                                    {@link VectorSeries} objects.
 * @since 1.0.6
 */
public class VectorSeriesCollection extends AbstractXYDataset implements VectorXYDataset, PublicCloneable, Serializable {
  /** 
 * Storage for the data series. 
 */
  private List data;
  /** 
 * Creates a new instance of <code>VectorSeriesCollection</code>.
 */
  public VectorSeriesCollection(){
    this.data=new java.util.ArrayList();
  }
  /** 
 * Adds a series to the collection and sends a                                                                                                                                                                    {@link DatasetChangeEvent}to all registered listeners.
 * @param series  the series (<code>null</code> not permitted).
 */
  public void addSeries(  VectorSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Null 'series' argument.");
    }
    this.data.add(series);
    series.addChangeListener(this);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Removes the specified series from the collection and sends a                                                                                                                                                                   {@link DatasetChangeEvent} to all registered listeners.
 * @param series  the series (<code>null</code> not permitted).
 * @return A boolean indicating whether the series has actually beenremoved.
 */
  public boolean removeSeries(  VectorSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Null 'series' argument.");
    }
    boolean removed=this.data.remove(series);
    if (removed) {
      series.removeChangeListener(this);
      fireDatasetChanged(new DatasetChangeInfo());
    }
    return removed;
  }
  /** 
 * Removes all the series from the collection and sends a                                                                                                                                                                   {@link DatasetChangeEvent} to all registered listeners.
 */
  public void removeAllSeries(){
    for (int i=0; i < this.data.size(); i++) {
      VectorSeries series=(VectorSeries)this.data.get(i);
      series.removeChangeListener(this);
    }
    this.data.clear();
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
  public VectorSeries getSeries(  int series){
    if ((series < 0) || (series >= getSeriesCount())) {
      throw new IllegalArgumentException("Series index out of bounds");
    }
    return (VectorSeries)this.data.get(series);
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
 * Returns the index of the specified series, or -1 if that series is not present in the dataset.
 * @param series  the series (<code>null</code> not permitted).
 * @return The series index.
 */
  public int indexOf(  VectorSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Null 'series' argument.");
    }
    return this.data.indexOf(series);
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
  public double getXValue(  int series,  int item){
    VectorSeries s=(VectorSeries)this.data.get(series);
    VectorDataItem di=(VectorDataItem)s.getDataItem(item);
    return di.getXValue();
  }
  /** 
 * Returns the x-value for an item within a series.  Note that this method creates a new                                                                                                                                                                    {@link Double} instance every time it is called---use{@link #getXValue(int,int)} instead, if possible.
 * @param series  the series index.
 * @param item  the item index.
 * @return The x-value.
 */
  public Number getX(  int series,  int item){
    return new Double(getXValue(series,item));
  }
  /** 
 * Returns the y-value for an item within a series.
 * @param series  the series index.
 * @param item  the item index.
 * @return The y-value.
 */
  public double getYValue(  int series,  int item){
    VectorSeries s=(VectorSeries)this.data.get(series);
    VectorDataItem di=(VectorDataItem)s.getDataItem(item);
    return di.getYValue();
  }
  /** 
 * Returns the y-value for an item within a series.  Note that this method creates a new                                                                                                                                                                    {@link Double} instance every time it is called---use{@link #getYValue(int,int)} instead, if possible.
 * @param series  the series index.
 * @param item  the item index.
 * @return The y-value.
 */
  public Number getY(  int series,  int item){
    return new Double(getYValue(series,item));
  }
  /** 
 * Returns the vector for an item in a series.
 * @param series  the series index.
 * @param item  the item index.
 * @return The vector (possibly <code>null</code>).
 */
  public Vector getVector(  int series,  int item){
    VectorSeries s=(VectorSeries)this.data.get(series);
    VectorDataItem di=(VectorDataItem)s.getDataItem(item);
    return di.getVector();
  }
  /** 
 * Returns the x-component of the vector for an item in a series.
 * @param series  the series index.
 * @param item  the item index.
 * @return The x-component of the vector.
 */
  public double getVectorXValue(  int series,  int item){
    VectorSeries s=(VectorSeries)this.data.get(series);
    VectorDataItem di=(VectorDataItem)s.getDataItem(item);
    return di.getVectorX();
  }
  /** 
 * Returns the y-component of the vector for an item in a series.
 * @param series  the series index.
 * @param item  the item index.
 * @return The y-component of the vector.
 */
  public double getVectorYValue(  int series,  int item){
    VectorSeries s=(VectorSeries)this.data.get(series);
    VectorDataItem di=(VectorDataItem)s.getDataItem(item);
    return di.getVectorY();
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
    if (!(obj instanceof VectorSeriesCollection)) {
      return false;
    }
    VectorSeriesCollection that=(VectorSeriesCollection)obj;
    return ObjectUtilities.equal(this.data,that.data);
  }
  /** 
 * Returns a clone of this instance.
 * @return A clone.
 * @throws CloneNotSupportedException if there is a problem.
 */
  public Object clone() throws CloneNotSupportedException {
    VectorSeriesCollection clone=(VectorSeriesCollection)super.clone();
    clone.data=(List)ObjectUtilities.deepClone(this.data);
    return clone;
  }
}
