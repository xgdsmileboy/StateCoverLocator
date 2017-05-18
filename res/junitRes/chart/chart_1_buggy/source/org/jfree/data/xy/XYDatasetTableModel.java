package org.jfree.data.xy;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.jfree.data.event.DatasetChangeEvent;
import org.jfree.data.event.DatasetChangeListener;
/** 
 * A READ-ONLY wrapper around a      {@link TableXYDataset} to convert it to atable model for use in a JTable.  The first column of the table shows the x-values, the remaining columns show the y-values for each series (series 0 appears in column 1, series 1 appears in column 2, etc). <P> TO DO: <ul> <li>implement proper naming for x axis (getColumnName)</li> <li>implement setValueAt to remove READ-ONLY constraint (not sure how)</li> </ul>
 */
public class XYDatasetTableModel extends AbstractTableModel implements TableModel, DatasetChangeListener {
  /** 
 * The dataset. 
 */
  TableXYDataset model=null;
  /** 
 * Default constructor.
 */
  public XYDatasetTableModel(){
    super();
  }
  /** 
 * Creates a new table model based on the specified dataset.
 * @param dataset  the dataset.
 */
  public XYDatasetTableModel(  TableXYDataset dataset){
    this();
    this.model=dataset;
    this.model.addChangeListener(this);
  }
  /** 
 * Sets the model (dataset).
 * @param dataset  the dataset.
 */
  public void setModel(  TableXYDataset dataset){
    this.model=dataset;
    this.model.addChangeListener(this);
    fireTableDataChanged();
  }
  /** 
 * Returns the number of rows.
 * @return The row count.
 */
  public int getRowCount(){
    if (this.model == null) {
      return 0;
    }
    return this.model.getItemCount();
  }
  /** 
 * Gets the number of columns in the model.
 * @return The number of columns in the model.
 */
  public int getColumnCount(){
    if (this.model == null) {
      return 0;
    }
    return this.model.getSeriesCount() + 1;
  }
  /** 
 * Returns the column name.
 * @param column  the column index.
 * @return The column name.
 */
  public String getColumnName(  int column){
    if (this.model == null) {
      return super.getColumnName(column);
    }
    if (column < 1) {
      return "X Value";
    }
 else {
      return this.model.getSeriesKey(column - 1).toString();
    }
  }
  /** 
 * Returns a value of the specified cell. Column 0 is the X axis, Columns 1 and over are the Y axis
 * @param row  the row number.
 * @param column  the column number.
 * @return The value of the specified cell.
 */
  public Object getValueAt(  int row,  int column){
    if (this.model == null) {
      return null;
    }
    if (column < 1) {
      return this.model.getX(0,row);
    }
 else {
      return this.model.getY(column - 1,row);
    }
  }
  /** 
 * Receives notification that the underlying dataset has changed.
 * @param event  the event
 * @see DatasetChangeListener
 */
  public void datasetChanged(  DatasetChangeEvent event){
    fireTableDataChanged();
  }
  /** 
 * Returns a flag indicating whether or not the specified cell is editable.
 * @param row  the row number.
 * @param column  the column number.
 * @return <code>true</code> if the specified cell is editable.
 */
  public boolean isCellEditable(  int row,  int column){
    return false;
  }
  /** 
 * Updates the      {@link XYDataset} if allowed.
 * @param value  the new value.
 * @param row  the row.
 * @param column  the column.
 */
  public void setValueAt(  Object value,  int row,  int column){
    if (isCellEditable(row,column)) {
    }
  }
}
