package org.jfree.data.xy;
import java.io.Serializable;
import java.util.List;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Represents a collection of                               {@link MatrixSeries} that can be used as adataset.
 * @see org.jfree.data.xy.MatrixSeries
 */
public class MatrixSeriesCollection extends AbstractXYZDataset implements XYZDataset, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-3197705779242543945L;
  /** 
 * The series that are included in the collection. 
 */
  private List seriesList;
  /** 
 * Constructs an empty dataset.
 */
  public MatrixSeriesCollection(){
    this(null);
  }
  /** 
 * Constructs a dataset and populates it with a single matrix series.
 * @param series the time series.
 */
  public MatrixSeriesCollection(  MatrixSeries series){
    this.seriesList=new java.util.ArrayList();
    if (series != null) {
      this.seriesList.add(series);
      series.addChangeListener(this);
    }
  }
  /** 
 * Returns the number of items in the specified series.
 * @param seriesIndex zero-based series index.
 * @return The number of items in the specified series.
 */
  public int getItemCount(  int seriesIndex){
    return getSeries(seriesIndex).getItemCount();
  }
  /** 
 * Returns the series having the specified index.
 * @param seriesIndex zero-based series index.
 * @return The series.
 * @throws IllegalArgumentException
 */
  public MatrixSeries getSeries(  int seriesIndex){
    if ((seriesIndex < 0) || (seriesIndex > getSeriesCount())) {
      throw new IllegalArgumentException("Index outside valid range.");
    }
    MatrixSeries series=(MatrixSeries)this.seriesList.get(seriesIndex);
    return series;
  }
  /** 
 * Returns the number of series in the collection.
 * @return The number of series in the collection.
 */
  public int getSeriesCount(){
    return this.seriesList.size();
  }
  /** 
 * Returns the key for a series.
 * @param seriesIndex zero-based series index.
 * @return The key for a series.
 */
  public Comparable getSeriesKey(  int seriesIndex){
    return getSeries(seriesIndex).getKey();
  }
  /** 
 * Returns the j index value of the specified Mij matrix item in the specified matrix series.
 * @param seriesIndex zero-based series index.
 * @param itemIndex zero-based item index.
 * @return The j index value for the specified matrix item.
 * @see org.jfree.data.xy.XYDataset#getXValue(int,int)
 */
  public Number getX(  int seriesIndex,  int itemIndex){
    MatrixSeries series=(MatrixSeries)this.seriesList.get(seriesIndex);
    int x=series.getItemColumn(itemIndex);
    return new Integer(x);
  }
  /** 
 * Returns the i index value of the specified Mij matrix item in the specified matrix series.
 * @param seriesIndex zero-based series index.
 * @param itemIndex zero-based item index.
 * @return The i index value for the specified matrix item.
 * @see org.jfree.data.xy.XYDataset#getYValue(int,int)
 */
  public Number getY(  int seriesIndex,  int itemIndex){
    MatrixSeries series=(MatrixSeries)this.seriesList.get(seriesIndex);
    int y=series.getItemRow(itemIndex);
    return new Integer(y);
  }
  /** 
 * Returns the Mij item value of the specified Mij matrix item in the specified matrix series.
 * @param seriesIndex the series (zero-based index).
 * @param itemIndex zero-based item index.
 * @return The Mij item value for the specified matrix item.
 * @see org.jfree.data.xy.XYZDataset#getZValue(int,int)
 */
  public Number getZ(  int seriesIndex,  int itemIndex){
    MatrixSeries series=(MatrixSeries)this.seriesList.get(seriesIndex);
    Number z=series.getItem(itemIndex);
    return z;
  }
  /** 
 * Adds a series to the collection. <P> Notifies all registered listeners that the dataset has changed. </p>
 * @param series the series.
 * @throws IllegalArgumentException
 */
  public void addSeries(  MatrixSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Cannot add null series.");
    }
    this.seriesList.add(series);
    series.addChangeListener(this);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Tests this collection for equality with an arbitrary object.
 * @param obj the object.
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj instanceof MatrixSeriesCollection) {
      MatrixSeriesCollection c=(MatrixSeriesCollection)obj;
      return ObjectUtilities.equal(this.seriesList,c.seriesList);
    }
    return false;
  }
  /** 
 * Returns a hash code.
 * @return A hash code.
 */
  public int hashCode(){
    return (this.seriesList != null ? this.seriesList.hashCode() : 0);
  }
  /** 
 * Returns a clone of this instance.
 * @return A clone.
 * @throws CloneNotSupportedException if there is a problem.
 */
  public Object clone() throws CloneNotSupportedException {
    MatrixSeriesCollection clone=(MatrixSeriesCollection)super.clone();
    clone.seriesList=(List)ObjectUtilities.deepClone(this.seriesList);
    return clone;
  }
  /** 
 * Removes all the series from the collection. <P> Notifies all registered listeners that the dataset has changed. </p>
 */
  public void removeAllSeries(){
    for (int i=0; i < this.seriesList.size(); i++) {
      MatrixSeries series=(MatrixSeries)this.seriesList.get(i);
      series.removeChangeListener(this);
    }
    this.seriesList.clear();
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Removes a series from the collection. <P> Notifies all registered listeners that the dataset has changed. </p>
 * @param series the series.
 * @throws IllegalArgumentException
 */
  public void removeSeries(  MatrixSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Cannot remove null series.");
    }
    if (this.seriesList.contains(series)) {
      series.removeChangeListener(this);
      this.seriesList.remove(series);
      fireDatasetChanged(new DatasetChangeInfo());
    }
  }
  /** 
 * Removes a series from the collection. <P> Notifies all registered listeners that the dataset has changed.
 * @param seriesIndex the series (zero based index).
 * @throws IllegalArgumentException
 */
  public void removeSeries(  int seriesIndex){
    if ((seriesIndex < 0) || (seriesIndex > getSeriesCount())) {
      throw new IllegalArgumentException("Index outside valid range.");
    }
    MatrixSeries series=(MatrixSeries)this.seriesList.get(seriesIndex);
    series.removeChangeListener(this);
    this.seriesList.remove(seriesIndex);
    fireDatasetChanged(new DatasetChangeInfo());
  }
}
