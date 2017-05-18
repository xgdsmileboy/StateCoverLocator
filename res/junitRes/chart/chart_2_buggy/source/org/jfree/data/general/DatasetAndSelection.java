package org.jfree.data.general;
/** 
 * A utility class for pairing up a dataset and a dataset selection state. Once paired, the items can be stored in a collection and retrieved on the basis of the dataset as the key. // if cloning and serialization of a ChartPanel is supported, then this // class will have to support it too.
 * @since 1.2.0
 */
public class DatasetAndSelection {
  /** 
 * The dataset. 
 */
  private Dataset dataset;
  /** 
 * The dataset selection state. 
 */
  private DatasetSelectionState selection;
  /** 
 * Creates a new instance.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param selection  the selection (<code>null</code> not permitted).
 */
  public DatasetAndSelection(  Dataset dataset,  DatasetSelectionState selection){
    this.dataset=dataset;
    this.selection=selection;
  }
  /** 
 * Returns the dataset.
 * @return The dataset (never <code>null</code>).
 */
  public Dataset getDataset(){
    return this.dataset;
  }
  /** 
 * Returns the selection state.
 * @return The selection state (never <code>null</code>).
 */
  public DatasetSelectionState getSelection(){
    return this.selection;
  }
}
