package org.jfree.chart.urls;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.data.category.CategoryDataset;
/** 
 * A URL generator that can be assigned to a                                                                                              {@link org.jfree.chart.renderer.category.CategoryItemRenderer}.
 */
public class StandardCategoryURLGenerator implements CategoryURLGenerator, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=2276668053074881909L;
  /** 
 * Prefix to the URL 
 */
  private String prefix="index.html";
  /** 
 * Series parameter name to go in each URL 
 */
  private String seriesParameterName="series";
  /** 
 * Category parameter name to go in each URL 
 */
  private String categoryParameterName="category";
  /** 
 * Creates a new generator with default settings.
 */
  public StandardCategoryURLGenerator(){
    super();
  }
  /** 
 * Constructor that overrides default prefix to the URL.
 * @param prefix  the prefix to the URL (<code>null</code> not permitted).
 */
  public StandardCategoryURLGenerator(  String prefix){
    if (prefix == null) {
      throw new IllegalArgumentException("Null 'prefix' argument.");
    }
    this.prefix=prefix;
  }
  /** 
 * Constructor that overrides all the defaults.
 * @param prefix  the prefix to the URL (<code>null</code> not permitted).
 * @param seriesParameterName  the name of the series parameter to go ineach URL (<code>null</code> not permitted).
 * @param categoryParameterName  the name of the category parameter to go ineach URL (<code>null</code> not permitted).
 */
  public StandardCategoryURLGenerator(  String prefix,  String seriesParameterName,  String categoryParameterName){
    if (prefix == null) {
      throw new IllegalArgumentException("Null 'prefix' argument.");
    }
    if (seriesParameterName == null) {
      throw new IllegalArgumentException("Null 'seriesParameterName' argument.");
    }
    if (categoryParameterName == null) {
      throw new IllegalArgumentException("Null 'categoryParameterName' argument.");
    }
    this.prefix=prefix;
    this.seriesParameterName=seriesParameterName;
    this.categoryParameterName=categoryParameterName;
  }
  /** 
 * Generates a URL for a particular item within a series.
 * @param dataset  the dataset.
 * @param series  the series index (zero-based).
 * @param category  the category index (zero-based).
 * @return The generated URL.
 */
  public String generateURL(  CategoryDataset dataset,  int series,  int category){
    String url=this.prefix;
    Comparable seriesKey=dataset.getRowKey(series);
    Comparable categoryKey=dataset.getColumnKey(category);
    boolean firstParameter=url.indexOf("?") == -1;
    url+=firstParameter ? "?" : "&amp;";
    url+=this.seriesParameterName + "=";
    String seriesKeyStr=null;
    try {
      seriesKeyStr=URLEncoder.encode(seriesKey.toString(),"UTF-8");
    }
 catch (    UnsupportedEncodingException e) {
      seriesKeyStr=seriesKey.toString();
    }
    String categoryKeyStr=null;
    try {
      categoryKeyStr=URLEncoder.encode(categoryKey.toString(),"UTF-8");
    }
 catch (    UnsupportedEncodingException e) {
      categoryKeyStr=categoryKey.toString();
    }
    url+=seriesKeyStr + "&amp;" + this.categoryParameterName+ "="+ categoryKeyStr;
    return url;
  }
  /** 
 * Tests the generator for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof StandardCategoryURLGenerator)) {
      return false;
    }
    StandardCategoryURLGenerator that=(StandardCategoryURLGenerator)obj;
    if (!ObjectUtilities.equal(this.prefix,that.prefix)) {
      return false;
    }
    if (!ObjectUtilities.equal(this.seriesParameterName,that.seriesParameterName)) {
      return false;
    }
    if (!ObjectUtilities.equal(this.categoryParameterName,that.categoryParameterName)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code.
 * @return A hash code.
 */
  public int hashCode(){
    int result;
    result=(this.prefix != null ? this.prefix.hashCode() : 0);
    result=29 * result + (this.seriesParameterName != null ? this.seriesParameterName.hashCode() : 0);
    result=29 * result + (this.categoryParameterName != null ? this.categoryParameterName.hashCode() : 0);
    return result;
  }
}
