package org.jfree.chart.labels;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.CategoryDataset;
/** 
 * A standard label generator that can be used with a                                                                                              {@link org.jfree.chart.renderer.category.CategoryItemRenderer}.
 */
public class StandardCategoryItemLabelGenerator extends AbstractCategoryItemLabelGenerator implements CategoryItemLabelGenerator, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=3499701401211412882L;
  /** 
 * The default format string. 
 */
  public static final String DEFAULT_LABEL_FORMAT_STRING="{2}";
  /** 
 * Creates a new generator with a default number formatter.
 */
  public StandardCategoryItemLabelGenerator(){
    super(DEFAULT_LABEL_FORMAT_STRING,NumberFormat.getInstance());
  }
  /** 
 * Creates a new generator with the specified number formatter.
 * @param labelFormat  the label format string (<code>null</code> notpermitted).
 * @param formatter  the number formatter (<code>null</code> not permitted).
 */
  public StandardCategoryItemLabelGenerator(  String labelFormat,  NumberFormat formatter){
    super(labelFormat,formatter);
  }
  /** 
 * Creates a new generator with the specified number formatter.
 * @param labelFormat  the label format string (<code>null</code> notpermitted).
 * @param formatter  the number formatter (<code>null</code> not permitted).
 * @param percentFormatter  the percent formatter (<code>null</code> notpermitted).
 * @since 1.0.2
 */
  public StandardCategoryItemLabelGenerator(  String labelFormat,  NumberFormat formatter,  NumberFormat percentFormatter){
    super(labelFormat,formatter,percentFormatter);
  }
  /** 
 * Creates a new generator with the specified date formatter.
 * @param labelFormat  the label format string (<code>null</code> notpermitted).
 * @param formatter  the date formatter (<code>null</code> not permitted).
 */
  public StandardCategoryItemLabelGenerator(  String labelFormat,  DateFormat formatter){
    super(labelFormat,formatter);
  }
  /** 
 * Generates the label for an item in a dataset.  Note: in the current dataset implementation, each row is a series, and each column contains values for a particular category.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @return The label (possibly <code>null</code>).
 */
  public String generateLabel(  CategoryDataset dataset,  int row,  int column){
    return generateLabelString(dataset,row,column);
  }
  /** 
 * Tests this generator for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return <code>true</code> if this generator is equal to<code>obj</code>, and <code>false</code> otherwise.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof StandardCategoryItemLabelGenerator)) {
      return false;
    }
    return super.equals(obj);
  }
}
