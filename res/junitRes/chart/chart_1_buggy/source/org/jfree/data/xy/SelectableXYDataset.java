package org.jfree.data.xy;
/** 
 * An      {@link XYDataset} that has the ability to track selection state.
 */
public interface SelectableXYDataset {
  /** 
 * Returns the selection state for the dataset.  If this method returns <code>null</code>, the dataset is not tracking item selections.
 * @return The selection state (possibly <code>null</code>).
 */
  public XYDatasetSelectionState getSelectionState();
}
