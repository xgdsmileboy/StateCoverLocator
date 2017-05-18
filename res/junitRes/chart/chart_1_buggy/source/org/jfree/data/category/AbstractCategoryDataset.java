package org.jfree.data.category;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.data.general.AbstractDataset;
/** 
 * A base class that is convenient for implementing the      {@link CategoryDataset}interface.
 */
public class AbstractCategoryDataset extends AbstractDataset {
  /** 
 * The dataset selection state (possibly <code>null</code>).
 * @since 1.2.0
 */
  private CategoryDatasetSelectionState selectionState;
  /** 
 * Default constructor.
 */
  public AbstractCategoryDataset(){
    super();
  }
  /** 
 * Returns the selection state for this dataset, if any.  The default value is <code>null</code>.
 * @return The selection state (possibly <code>null</code>).
 * @since 1.2.0
 */
  public CategoryDatasetSelectionState getSelectionState(){
    return this.selectionState;
  }
  /** 
 * Sets the selection state for this dataset.
 * @param state  the selection state (<code>null</code> permitted).
 * @since 1.2.0
 */
  public void setSelectionState(  CategoryDatasetSelectionState state){
    this.selectionState=state;
    fireDatasetChanged(new DatasetChangeInfo());
  }
}
