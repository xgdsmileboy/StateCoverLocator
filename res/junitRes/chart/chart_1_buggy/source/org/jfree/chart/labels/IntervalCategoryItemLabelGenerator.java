package org.jfree.chart.labels;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
/** 
 * A label generator for plots that use data from an                              {@link IntervalCategoryDataset}.
 */
public class IntervalCategoryItemLabelGenerator extends StandardCategoryItemLabelGenerator implements CategoryItemLabelGenerator, PublicCloneable, Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=5056909225610630529L;
  /** 
 * The default format string. 
 */
  public static final String DEFAULT_LABEL_FORMAT_STRING="({0}, {1}) = {3} - {4}";
  /** 
 * Creates a new generator with a default number formatter.
 */
  public IntervalCategoryItemLabelGenerator(){
    super(DEFAULT_LABEL_FORMAT_STRING,NumberFormat.getInstance());
  }
  /** 
 * Creates a new generator with the specified number formatter.
 * @param labelFormat  the label format string (<code>null</code> notpermitted).
 * @param formatter  the number formatter (<code>null</code> not permitted).
 */
  public IntervalCategoryItemLabelGenerator(  String labelFormat,  NumberFormat formatter){
    super(labelFormat,formatter);
  }
  /** 
 * Creates a new generator with the specified date formatter.
 * @param labelFormat  the label format string (<code>null</code> notpermitted).
 * @param formatter  the date formatter (<code>null</code> not permitted).
 */
  public IntervalCategoryItemLabelGenerator(  String labelFormat,  DateFormat formatter){
    super(labelFormat,formatter);
  }
  /** 
 * Creates the array of items that can be passed to the <code>MessageFormat</code> class for creating labels.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @return The items (never <code>null</code>).
 */
  protected Object[] createItemArray(  CategoryDataset dataset,  int row,  int column){
    Object[] result=new Object[5];
    result[0]=dataset.getRowKey(row).toString();
    result[1]=dataset.getColumnKey(column).toString();
    Number value=dataset.getValue(row,column);
    if (getNumberFormat() != null) {
      result[2]=getNumberFormat().format(value);
    }
 else {
      if (getDateFormat() != null) {
        result[2]=getDateFormat().format(value);
      }
    }
    if (dataset instanceof IntervalCategoryDataset) {
      IntervalCategoryDataset icd=(IntervalCategoryDataset)dataset;
      Number start=icd.getStartValue(row,column);
      Number end=icd.getEndValue(row,column);
      if (getNumberFormat() != null) {
        result[3]=getNumberFormat().format(start);
        result[4]=getNumberFormat().format(end);
      }
 else {
        if (getDateFormat() != null) {
          result[3]=getDateFormat().format(start);
          result[4]=getDateFormat().format(end);
        }
      }
    }
    return result;
  }
}
