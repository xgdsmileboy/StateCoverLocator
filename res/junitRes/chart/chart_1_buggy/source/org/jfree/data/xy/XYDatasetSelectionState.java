package org.jfree.data.xy;
import java.io.Serializable;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.general.DatasetSelectionState;
/** 
 * Returns information about the selection state of items in an     {@link XYDataset}.  Classes that implement this interface must also implement      {@link PublicCloneable} to ensure that charts and datasets can becorrectly cloned.  Likewise, classes implementing this interface must also implement  {@link Serializable}. <br><br> The selection state might be part of a dataset implementation, or it could be maintained in parallel with a dataset implementation that doesn't directly support selection state.
 * @since 1.2.0
 */
public interface XYDatasetSelectionState extends DatasetSelectionState {
  /** 
 * Returns the number of series.
 * @return The number of series.
 */
  public int getSeriesCount();
  /** 
 * Returns the number of items within the specified series.
 * @param series  the series.
 * @return The number of items in the series.
 */
  public int getItemCount(  int series);
  /** 
 * Returns <code>true</code> if the specified item is selected, and <code>false</code> otherwise.
 * @param series  the series index.
 * @param item  the item index.
 * @return A boolean.
 */
  public boolean isSelected(  int series,  int item);
  /** 
 * Sets the selection state for an item in the dataset.
 * @param series  the series index.
 * @param item  the item index.
 * @param selected  the selection state.
 */
  public void setSelected(  int series,  int item,  boolean selected);
  /** 
 * Sets the selection state for the specified item and, if requested, fires a change event.
 * @param series  the series index.
 * @param item  the item index.
 * @param selected  the selection state.
 * @param notify  notify listeners?
 */
  public void setSelected(  int series,  int item,  boolean selected,  boolean notify);
  /** 
 * Send an event to registered listeners to indicate that the selection has changed.
 */
  public void fireSelectionEvent();
  /** 
 * Clears all selected items.
 */
  public void clearSelection();
}
