package org.jfree.chart.urls;
import java.io.Serializable;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.data.xy.XYDataset;
/** 
 * A URL generator.
 */
public class StandardXYURLGenerator implements XYURLGenerator, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-1771624523496595382L;
  /** 
 * The default prefix. 
 */
  public static final String DEFAULT_PREFIX="index.html";
  /** 
 * The default series parameter. 
 */
  public static final String DEFAULT_SERIES_PARAMETER="series";
  /** 
 * The default item parameter. 
 */
  public static final String DEFAULT_ITEM_PARAMETER="item";
  /** 
 * Prefix to the URL 
 */
  private String prefix;
  /** 
 * Series parameter name to go in each URL 
 */
  private String seriesParameterName;
  /** 
 * Item parameter name to go in each URL 
 */
  private String itemParameterName;
  /** 
 * Creates a new default generator.  This constructor is equivalent to calling <code>StandardXYURLGenerator("index.html", "series", "item"); </code>.
 */
  public StandardXYURLGenerator(){
    this(DEFAULT_PREFIX,DEFAULT_SERIES_PARAMETER,DEFAULT_ITEM_PARAMETER);
  }
  /** 
 * Creates a new generator with the specified prefix.  This constructor is equivalent to calling <code>StandardXYURLGenerator(prefix, "series", "item");</code>.
 * @param prefix  the prefix to the URL (<code>null</code> not permitted).
 */
  public StandardXYURLGenerator(  String prefix){
    this(prefix,DEFAULT_SERIES_PARAMETER,DEFAULT_ITEM_PARAMETER);
  }
  /** 
 * Constructor that overrides all the defaults
 * @param prefix  the prefix to the URL (<code>null</code> not permitted).
 * @param seriesParameterName  the name of the series parameter to go ineach URL (<code>null</code> not permitted).
 * @param itemParameterName  the name of the item parameter to go in eachURL (<code>null</code> not permitted).
 */
  public StandardXYURLGenerator(  String prefix,  String seriesParameterName,  String itemParameterName){
    if (prefix == null) {
      throw new IllegalArgumentException("Null 'prefix' argument.");
    }
    if (seriesParameterName == null) {
      throw new IllegalArgumentException("Null 'seriesParameterName' argument.");
    }
    if (itemParameterName == null) {
      throw new IllegalArgumentException("Null 'itemParameterName' argument.");
    }
    this.prefix=prefix;
    this.seriesParameterName=seriesParameterName;
    this.itemParameterName=itemParameterName;
  }
  /** 
 * Generates a URL for a particular item within a series.
 * @param dataset  the dataset.
 * @param series  the series number (zero-based index).
 * @param item  the item number (zero-based index).
 * @return The generated URL.
 */
  public String generateURL(  XYDataset dataset,  int series,  int item){
    String url=this.prefix;
    boolean firstParameter=url.indexOf("?") == -1;
    url+=firstParameter ? "?" : "&amp;";
    url+=this.seriesParameterName + "=" + series+ "&amp;"+ this.itemParameterName+ "="+ item;
    return url;
  }
  /** 
 * Tests this generator for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof StandardXYURLGenerator)) {
      return false;
    }
    StandardXYURLGenerator that=(StandardXYURLGenerator)obj;
    if (!ObjectUtilities.equal(that.prefix,this.prefix)) {
      return false;
    }
    if (!ObjectUtilities.equal(that.seriesParameterName,this.seriesParameterName)) {
      return false;
    }
    if (!ObjectUtilities.equal(that.itemParameterName,this.itemParameterName)) {
      return false;
    }
    return true;
  }
}
