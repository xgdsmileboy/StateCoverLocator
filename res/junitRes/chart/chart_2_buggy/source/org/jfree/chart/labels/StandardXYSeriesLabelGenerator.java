package org.jfree.chart.labels;
import java.io.Serializable;
import java.text.MessageFormat;
import org.jfree.chart.util.HashUtilities;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.xy.XYDataset;
/** 
 * A standard series label generator for plots that use data from an                                                                                               {@link org.jfree.data.xy.XYDataset}. <br><br> This class implements <code>PublicCloneable</code> by mistake but we retain this for the sake of backward compatibility.
 */
public class StandardXYSeriesLabelGenerator implements XYSeriesLabelGenerator, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=1916017081848400024L;
  /** 
 * The default item label format. 
 */
  public static final String DEFAULT_LABEL_FORMAT="{0}";
  /** 
 * The format pattern. 
 */
  private String formatPattern;
  /** 
 * Creates a default series label generator (uses                                                                                              {@link #DEFAULT_LABEL_FORMAT}).
 */
  public StandardXYSeriesLabelGenerator(){
    this(DEFAULT_LABEL_FORMAT);
  }
  /** 
 * Creates a new series label generator.
 * @param format  the format pattern (<code>null</code> not permitted).
 */
  public StandardXYSeriesLabelGenerator(  String format){
    if (format == null) {
      throw new IllegalArgumentException("Null 'format' argument.");
    }
    this.formatPattern=format;
  }
  /** 
 * Generates a label for the specified series.  This label will be used for the chart legend.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series.
 * @return A series label.
 */
  public String generateLabel(  XYDataset dataset,  int series){
    if (dataset == null) {
      throw new IllegalArgumentException("Null 'dataset' argument.");
    }
    String label=MessageFormat.format(this.formatPattern,createItemArray(dataset,series));
    return label;
  }
  /** 
 * Creates the array of items that can be passed to the                                                                                              {@link MessageFormat} class for creating labels.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series (zero-based index).
 * @return The items (never <code>null</code>).
 */
  protected Object[] createItemArray(  XYDataset dataset,  int series){
    Object[] result=new Object[1];
    result[0]=dataset.getSeriesKey(series).toString();
    return result;
  }
  /** 
 * Returns an independent copy of the generator.  This is unnecessary, because instances are immutable anyway, but we retain this behaviour for backwards compatibility.
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
    if (!(obj instanceof StandardXYSeriesLabelGenerator)) {
      return false;
    }
    StandardXYSeriesLabelGenerator that=(StandardXYSeriesLabelGenerator)obj;
    if (!this.formatPattern.equals(that.formatPattern)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=127;
    result=HashUtilities.hashCode(result,this.formatPattern);
    return result;
  }
}
