package org.jfree.chart.labels;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.pie.PieDataset;
/** 
 * A standard item label generator for plots that use data from a                                                                                              {@link PieDataset}. <p> For the label format, use {0} where the pie section key should be inserted, {1} for the absolute section value and {2} for the percent amount of the pie section, e.g. <code>"{0} = {1} ({2})"</code> will display as <code>apple = 120 (5%)</code>.
 */
public class StandardPieToolTipGenerator extends AbstractPieItemLabelGenerator implements PieToolTipGenerator, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=2995304200445733779L;
  /** 
 * The default tooltip format. 
 */
  public static final String DEFAULT_TOOLTIP_FORMAT="{0}: ({1}, {2})";
  /** 
 * Creates an item label generator using default number formatters.
 */
  public StandardPieToolTipGenerator(){
    this(DEFAULT_TOOLTIP_FORMAT);
  }
  /** 
 * Creates a pie tool tip generator for the specified locale, using the default format string.
 * @param locale  the locale (<code>null</code> not permitted).
 * @since 1.0.7
 */
  public StandardPieToolTipGenerator(  Locale locale){
    this(DEFAULT_TOOLTIP_FORMAT,locale);
  }
  /** 
 * Creates a pie tool tip generator for the default locale.
 * @param labelFormat  the label format (<code>null</code> not permitted).
 */
  public StandardPieToolTipGenerator(  String labelFormat){
    this(labelFormat,Locale.getDefault());
  }
  /** 
 * Creates a pie tool tip generator for the specified locale.
 * @param labelFormat  the label format (<code>null</code> not permitted).
 * @param locale  the locale (<code>null</code> not permitted).
 * @since 1.0.7
 */
  public StandardPieToolTipGenerator(  String labelFormat,  Locale locale){
    this(labelFormat,NumberFormat.getNumberInstance(locale),NumberFormat.getPercentInstance(locale));
  }
  /** 
 * Creates an item label generator using the specified number formatters.
 * @param labelFormat  the label format string (<code>null</code> notpermitted).
 * @param numberFormat  the format object for the values (<code>null</code>not permitted).
 * @param percentFormat  the format object for the percentages(<code>null</code> not permitted).
 */
  public StandardPieToolTipGenerator(  String labelFormat,  NumberFormat numberFormat,  NumberFormat percentFormat){
    super(labelFormat,numberFormat,percentFormat);
  }
  /** 
 * Generates a tool tip text item for one section in a pie chart.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param key  the section key (<code>null</code> not permitted).
 * @return The tool tip text (possibly <code>null</code>).
 */
  public String generateToolTip(  PieDataset dataset,  Comparable key){
    return generateSectionLabel(dataset,key);
  }
  /** 
 * Returns an independent copy of the generator.
 * @return A clone.
 * @throws CloneNotSupportedException  should not happen.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
