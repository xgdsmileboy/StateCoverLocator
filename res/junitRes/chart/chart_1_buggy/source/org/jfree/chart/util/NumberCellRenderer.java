package org.jfree.chart.util;
import java.awt.Component;
import java.text.NumberFormat;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
/** 
 * A table cell renderer that formats numbers with right alignment in each cell.
 */
public class NumberCellRenderer extends DefaultTableCellRenderer {
  /** 
 * Default constructor - builds a renderer that right justifies the contents of a table cell.
 */
  public NumberCellRenderer(){
    super();
    setHorizontalAlignment(SwingConstants.RIGHT);
  }
  /** 
 * Returns itself as the renderer. Supports the TableCellRenderer interface.
 * @param table  the table.
 * @param value  the data to be rendered.
 * @param isSelected  a boolean that indicates whether or not the cell isselected.
 * @param hasFocus  a boolean that indicates whether or not the cell hasthe focus.
 * @param row  the (zero-based) row index.
 * @param column  the (zero-based) column index.
 * @return the component that can render the contents of the cell.
 */
  public Component getTableCellRendererComponent(  JTable table,  Object value,  boolean isSelected,  boolean hasFocus,  int row,  int column){
    setFont(null);
    NumberFormat nf=NumberFormat.getNumberInstance();
    if (value != null) {
      setText(nf.format(value));
    }
 else {
      setText("");
    }
    if (isSelected) {
      setBackground(table.getSelectionBackground());
    }
 else {
      setBackground(null);
    }
    return this;
  }
}
