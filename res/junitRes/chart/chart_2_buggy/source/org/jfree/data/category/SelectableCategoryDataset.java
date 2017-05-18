package org.jfree.data.category;
/** 
 * A                                                                                               {@link CategoryDataset} that has the ability to track selection state.
 */
public interface SelectableCategoryDataset {
  /** 
 * Returns the selection state for the dataset.  If this method returns <code>null</code>, the dataset is not tracking item selections.
 * @return The selection state (possibly <code>null</code>).
 */
  public CategoryDatasetSelectionState getSelectionState();
}
