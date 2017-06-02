package org.jfree.chart.labels;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.xy.XYDataset;
/** 
 * A standard item label generator for plots that use data from an                                                                                                                                                               {@link org.jfree.data.xy.XYDataset}.
 */
public class StandardXYItemLabelGenerator extends AbstractXYItemLabelGenerator implements XYItemLabelGenerator, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=7807668053171837925L;
  /** 
 * The default item label format. 
 */
  public static final String DEFAULT_ITEM_LABEL_FORMAT="{2}";
  /** 
 * Creates an item label generator using default number formatters.
 */
  public StandardXYItemLabelGenerator(){
    this(DEFAULT_ITEM_LABEL_FORMAT,NumberFormat.getNumberInstance(),NumberFormat.getNumberInstance());
  }
  /** 
 * Creates an item label generator using the specified number formatters.
 * @param formatString  the item label format string (<code>null</code> notpermitted).
 * @since 1.0.14
 */
  public StandardXYItemLabelGenerator(  String formatString){
    this(formatString,NumberFormat.getNumberInstance(),NumberFormat.getNumberInstance());
  }
  /** 
 * Creates an item label generator using the specified number formatters.
 * @param formatString  the item label format string (<code>null</code> notpermitted).
 * @param xFormat  the format object for the x values (<code>null</code>not permitted).
 * @param yFormat  the format object for the y values (<code>null</code>not permitted).
 */
  public StandardXYItemLabelGenerator(  String formatString,  NumberFormat xFormat,  NumberFormat yFormat){
    super(formatString,xFormat,yFormat);
  }
  /** 
 * Creates an item label generator using the specified formatters.
 * @param formatString  the item label format string (<code>null</code>not permitted).
 * @param xFormat  the format object for the x values (<code>null</code>not permitted).
 * @param yFormat  the format object for the y values (<code>null</code>not permitted).
 */
  public StandardXYItemLabelGenerator(  String formatString,  DateFormat xFormat,  NumberFormat yFormat){
    super(formatString,xFormat,yFormat);
  }
  /** 
 * Creates an item label generator using the specified formatters (a number formatter for the x-values and a date formatter for the y-values).
 * @param formatString  the item label format string (<code>null</code>not permitted).
 * @param xFormat  the format object for the x values (<code>null</code>permitted).
 * @param yFormat  the format object for the y values (<code>null</code>not permitted).
 * @since 1.0.4
 */
  public StandardXYItemLabelGenerator(  String formatString,  NumberFormat xFormat,  DateFormat yFormat){
    super(formatString,xFormat,yFormat);
  }
  /** 
 * Creates a label generator using the specified date formatters.
 * @param formatString  the label format string (<code>null</code> notpermitted).
 * @param xFormat  the format object for the x values (<code>null</code>not permitted).
 * @param yFormat  the format object for the y values (<code>null</code>not permitted).
 */
  public StandardXYItemLabelGenerator(  String formatString,  DateFormat xFormat,  DateFormat yFormat){
    super(formatString,xFormat,yFormat);
  }
  /** 
 * Generates the item label text for an item in a dataset.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The label text (possibly <code>null</code>).
 */
  public String generateLabel(  XYDataset dataset,  int series,  int item){
    return generateLabelString(dataset,series,item);
  }
  /** 
 * Returns an independent copy of the generator.
 * @return A clone.
 * @throws CloneNotSupportedException if cloning is not supported.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  /** 
 * Tests this object for equality with an arbitrary object.
 * @param obj  the other object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof StandardXYItemLabelGenerator)) {
      return false;
    }
    return super.equals(obj);
  }
}
