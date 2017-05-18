package org.jfree.data.category;
import java.io.Serializable;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.general.DatasetSelectionState;
/** 
 * Returns information about the selection state of items in an                                                                                              {@link CategoryDataset}.  Classes that implement this interface must also implement                                                                                               {@link PublicCloneable} to ensure that charts and datasets can becorrectly cloned.  Likewise, classes implementing this interface must also implement  {@link Serializable}. <br><br> The selection state might be part of a dataset implementation, or it could be maintained in parallel with a dataset implementation that doesn't directly support selection state.
 * @since 1.2.0
 */
public interface CategoryDatasetSelectionState extends DatasetSelectionState {
  /** 
 * Returns the number of rows in the dataset.
 * @return The number of rows.
 */
  public int getRowCount();
  /** 
 * Returns the number of columns in the dataset.
 * @return The number of columns.
 */
  public int getColumnCount();
  /** 
 * Returns <code>true</code> if the specified item is selected, and <code>false</code> otherwise.
 * @param row  the row index.
 * @param column  the column index.
 * @return A boolean.
 */
  public boolean isSelected(  int row,  int column);
  /** 
 * Sets the selection state for an item in the dataset.
 * @param row  the row index.
 * @param column  the column index.
 * @param selected  the selection state.
 */
  public void setSelected(  int row,  int column,  boolean selected);
  /** 
 * Sets the selection state for the specified item and, if requested, fires a change event.
 * @param row  the row index.
 * @param column  the column index.
 * @param selected  the selection state.
 * @param notify  notify listeners?
 */
  public void setSelected(  int row,  int column,  boolean selected,  boolean notify);
  /** 
 * Clears all selected items.
 */
  public void clearSelection();
  /** 
 * Send an event to registered listeners to indicate that the selection has changed.
 */
  public void fireSelectionEvent();
}
