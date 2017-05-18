package org.jfree.chart.plot;
import java.io.Serializable;
import java.util.List;
/** 
 * A base class for handling the distribution of pie section labels.  Create your own subclass and set it using the                                                                                              {@link PiePlot#setLabelDistributor(AbstractPieLabelDistributor)} methodif you want to customise the label distribution.
 */
public abstract class AbstractPieLabelDistributor implements Serializable {
  /** 
 * The label records. 
 */
  protected List labels;
  /** 
 * Creates a new instance.
 */
  public AbstractPieLabelDistributor(){
    this.labels=new java.util.ArrayList();
  }
  /** 
 * Returns a label record from the list.
 * @param index  the index.
 * @return The label record.
 */
  public PieLabelRecord getPieLabelRecord(  int index){
    return (PieLabelRecord)this.labels.get(index);
  }
  /** 
 * Adds a label record.
 * @param record  the label record (<code>null</code> not permitted).
 */
  public void addPieLabelRecord(  PieLabelRecord record){
    if (record == null) {
      throw new IllegalArgumentException("Null 'record' argument.");
    }
    this.labels.add(record);
  }
  /** 
 * Returns the number of items in the list.
 * @return The item count.
 */
  public int getItemCount(){
    return this.labels.size();
  }
  /** 
 * Clears the list of labels.
 */
  public void clear(){
    this.labels.clear();
  }
  /** 
 * Called by the                                                                                               {@link PiePlot} class.  Implementations should distributethe labels in this.labels then return.
 * @param minY  the y-coordinate for the top of the label area.
 * @param height  the height of the label area.
 */
  public abstract void distributeLabels(  double minY,  double height);
}
