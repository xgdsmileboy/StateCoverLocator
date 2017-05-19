package org.jfree.data.event;
import org.jfree.data.general.*;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.chart.plot.Plot;
/** 
 * A change event that encapsulates information about a change to a dataset.
 */
public class DatasetChangeEvent extends java.util.EventObject {
  /** 
 * The dataset that generated the change event.
 */
  private Dataset dataset;
  /** 
 * Some details of the change.
 * @since 1.2.0
 */
  private DatasetChangeInfo info;
  /** 
 * Constructs a new event.  The source is either the dataset or the                             {@link Plot} class.  The dataset can be <code>null</code> (in this casethe source will be the  {@link Plot} class).
 * @param source  the source of the event.
 * @param dataset  the dataset that generated the event (<code>null</code>permitted).
 * @param info  information about the change (<code>null</code> notpermitted).
 * @since 1.2.0
 */
  public DatasetChangeEvent(  Object source,  Dataset dataset,  DatasetChangeInfo info){
    super(source);
    if (info == null) {
      throw new IllegalArgumentException("Null 'info' argument.");
    }
    this.dataset=dataset;
    this.info=info;
  }
  /** 
 * Returns the dataset that generated the event.  Note that the dataset may be <code>null</code> since adding a <code>null</code> dataset to a plot will generated a change event.
 * @return The dataset (possibly <code>null</code>).
 */
  public Dataset getDataset(){
    return this.dataset;
  }
  /** 
 * Returns the dataset change info.
 * @return The dataset change info.
 * @since 1.2.0
 */
  public DatasetChangeInfo getInfo(){
    return this.info;
  }
}
