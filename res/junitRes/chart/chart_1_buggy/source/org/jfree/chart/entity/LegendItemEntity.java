package org.jfree.chart.entity;
import java.awt.Shape;
import java.io.Serializable;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.data.general.Dataset;
/** 
 * An entity that represents an item within a legend.
 */
public class LegendItemEntity extends ChartEntity implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-7435683933545666702L;
  /** 
 * The dataset.
 * @since 1.0.6
 */
  private Dataset dataset;
  /** 
 * The series key.
 * @since 1.0.6
 */
  private Comparable seriesKey;
  /** 
 * Creates a legend item entity.
 * @param area  the area.
 */
  public LegendItemEntity(  Shape area){
    super(area);
  }
  /** 
 * Returns a reference to the dataset that this legend item is derived from.
 * @return The dataset.
 * @since 1.0.6
 * @see #setDataset(Dataset)
 */
  public Dataset getDataset(){
    return this.dataset;
  }
  /** 
 * Sets a reference to the dataset that this legend item is derived from.
 * @param dataset  the dataset.
 * @since 1.0.6
 */
  public void setDataset(  Dataset dataset){
    this.dataset=dataset;
  }
  /** 
 * Returns the series key that identifies the legend item.
 * @return The series key.
 * @since 1.0.6
 * @see #setSeriesKey(Comparable)
 */
  public Comparable getSeriesKey(){
    return this.seriesKey;
  }
  /** 
 * Sets the key for the series.
 * @param key  the key.
 * @since 1.0.6
 * @see #getSeriesKey()
 */
  public void setSeriesKey(  Comparable key){
    this.seriesKey=key;
  }
  /** 
 * Tests this object for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof LegendItemEntity)) {
      return false;
    }
    LegendItemEntity that=(LegendItemEntity)obj;
    if (!ObjectUtilities.equal(this.seriesKey,that.seriesKey)) {
      return false;
    }
    if (!ObjectUtilities.equal(this.dataset,that.dataset)) {
      return false;
    }
    return super.equals(obj);
  }
  /** 
 * Returns a clone of the entity.
 * @return A clone.
 * @throws CloneNotSupportedException if there is a problem cloning theobject.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  /** 
 * Returns a string representing this object (useful for debugging purposes).
 * @return A string (never <code>null</code>).
 */
  public String toString(){
    return "LegendItemEntity: seriesKey=" + this.seriesKey + ", dataset="+ this.dataset;
  }
}
