package org.jfree.chart.entity;
import java.awt.Shape;
import org.jfree.data.xy.XYDataset;
/** 
 * A chart entity that represents one item within an     {@link org.jfree.chart.plot.XYPlot}.
 */
public class XYItemEntity extends ChartEntity {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-3870862224880283771L;
  /** 
 * The dataset. 
 */
  private transient XYDataset dataset;
  /** 
 * The series. 
 */
  private int series;
  /** 
 * The item. 
 */
  private int item;
  /** 
 * Creates a new entity.
 * @param area  the area.
 * @param dataset  the dataset.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @param toolTipText  the tool tip text.
 * @param urlText  the URL text for HTML image maps.
 */
  public XYItemEntity(  Shape area,  XYDataset dataset,  int series,  int item,  String toolTipText,  String urlText){
    super(area,toolTipText,urlText);
    this.dataset=dataset;
    this.series=series;
    this.item=item;
  }
  /** 
 * Returns the dataset this entity refers to.
 * @return The dataset.
 */
  public XYDataset getDataset(){
    return this.dataset;
  }
  /** 
 * Sets the dataset this entity refers to.
 * @param dataset  the dataset.
 */
  public void setDataset(  XYDataset dataset){
    this.dataset=dataset;
  }
  /** 
 * Returns the series index.
 * @return The series index.
 */
  public int getSeriesIndex(){
    return this.series;
  }
  /** 
 * Sets the series index.
 * @param series the series index (zero-based).
 */
  public void setSeriesIndex(  int series){
    this.series=series;
  }
  /** 
 * Returns the item index.
 * @return The item index.
 */
  public int getItem(){
    return this.item;
  }
  /** 
 * Sets the item index.
 * @param item the item index (zero-based).
 */
  public void setItem(  int item){
    this.item=item;
  }
  /** 
 * Tests the entity for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (obj instanceof XYItemEntity && super.equals(obj)) {
      XYItemEntity ie=(XYItemEntity)obj;
      if (this.series != ie.series) {
        return false;
      }
      if (this.item != ie.item) {
        return false;
      }
      return true;
    }
    return false;
  }
  /** 
 * Returns a string representation of this instance, useful for debugging purposes.
 * @return A string.
 */
  public String toString(){
    return "XYItemEntity: series = " + getSeriesIndex() + ", item = "+ getItem()+ ", dataset = "+ getDataset();
  }
}
