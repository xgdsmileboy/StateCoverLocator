package org.jfree.data.pie;
import org.jfree.data.general.*;
import java.io.Serializable;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Returns information about the selection state of items in an                                                                                                                                                              {@link PieDataset}.  Classes that implement this interface must also implement                                                                                                                                                               {@link PublicCloneable} to ensure that charts and datasets can becorrectly cloned.  Likewise, classes implementing this interface must also implement  {@link Serializable}. <br><br> The selection state might be part of a dataset implementation, or it could be maintained in parallel with a dataset implementation that doesn't directly support selection state.
 * @since 1.2.0
 */
public interface PieDatasetSelectionState extends DatasetSelectionState {
  /** 
 * Returns the number of items in the dataset.
 * @return The number of items.
 */
  public int getItemCount();
  /** 
 * Returns <code>true</code> if the specified item is selected, and <code>false</code> otherwise.
 * @param key  the item key.
 * @return A boolean.
 */
  public boolean isSelected(  Comparable key);
  /** 
 * Sets the selection state for an item in the dataset.
 * @param key  the item key.
 * @param selected  the selection state.
 */
  public void setSelected(  Comparable key,  boolean selected);
  /** 
 * Sets the selection state for the specified item and, if requested, fires a change event.
 * @param key  the item key.
 * @param selected  the selection state.
 * @param notify  notify listeners?
 */
  public void setSelected(  Comparable key,  boolean selected,  boolean notify);
  /** 
 * Clears all selected items.
 */
  public void clearSelection();
  /** 
 * Send an event to registered listeners to indicate that the selection has changed.
 */
  public void fireSelectionEvent();
}
