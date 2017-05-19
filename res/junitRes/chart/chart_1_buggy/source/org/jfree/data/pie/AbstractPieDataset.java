package org.jfree.data.pie;
import org.jfree.data.general.*;
import org.jfree.chart.event.DatasetChangeInfo;
/** 
 * A base class that is convenient for implementing the                               {@link PieDataset}interface.
 */
public class AbstractPieDataset extends AbstractDataset implements SelectablePieDataset {
  /** 
 * The dataset selection state (possibly <code>null</code>).
 * @since 1.2.0
 */
  private PieDatasetSelectionState selectionState;
  /** 
 * Default constructor.
 */
  public AbstractPieDataset(){
    super();
  }
  /** 
 * Returns the selection state for this dataset, if any.  The default value is <code>null</code>.
 * @return The selection state (possibly <code>null</code>).
 * @since 1.2.0
 */
  public PieDatasetSelectionState getSelectionState(){
    return this.selectionState;
  }
  /** 
 * Sets the selection state for this dataset.
 * @param state  the selection state (<code>null</code> permitted).
 * @since 1.2.0
 */
  public void setSelectionState(  PieDatasetSelectionState state){
    this.selectionState=state;
    fireDatasetChanged(new DatasetChangeInfo());
  }
}
