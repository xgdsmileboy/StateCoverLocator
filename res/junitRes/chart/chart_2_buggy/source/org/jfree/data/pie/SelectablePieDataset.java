package org.jfree.data.pie;
/** 
 * A                                                                                                                                                               {@link PieDataset} that has the ability to track selection state.
 */
public interface SelectablePieDataset {
  /** 
 * Returns the selection state for the dataset.  If this method returns <code>null</code>, the dataset is not tracking item selections.
 * @return The selection state (possibly <code>null</code>).
 */
  public PieDatasetSelectionState getSelectionState();
}
