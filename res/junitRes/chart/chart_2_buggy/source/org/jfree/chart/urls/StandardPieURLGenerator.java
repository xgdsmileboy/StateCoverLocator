package org.jfree.chart.urls;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.data.pie.PieDataset;
/** 
 * A URL generator for pie charts.  Instances of this class are immutable.
 */
public class StandardPieURLGenerator implements PieURLGenerator, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=1626966402065883419L;
  /** 
 * The prefix. 
 */
  private String prefix="index.html";
  /** 
 * The category parameter name. 
 */
  private String categoryParameterName="category";
  /** 
 * The pie index parameter name. 
 */
  private String indexParameterName="pieIndex";
  /** 
 * Default constructor.
 */
  public StandardPieURLGenerator(){
    this("index.html");
  }
  /** 
 * Creates a new generator.
 * @param prefix  the prefix (<code>null</code> not permitted).
 */
  public StandardPieURLGenerator(  String prefix){
    this(prefix,"category");
  }
  /** 
 * Creates a new generator.
 * @param prefix  the prefix (<code>null</code> not permitted).
 * @param categoryParameterName  the category parameter name(<code>null</code> not permitted).
 */
  public StandardPieURLGenerator(  String prefix,  String categoryParameterName){
    this(prefix,categoryParameterName,"pieIndex");
  }
  /** 
 * Creates a new generator.
 * @param prefix  the prefix (<code>null</code> not permitted).
 * @param categoryParameterName  the category parameter name(<code>null</code> not permitted).
 * @param indexParameterName  the index parameter name (<code>null</code>permitted).
 */
  public StandardPieURLGenerator(  String prefix,  String categoryParameterName,  String indexParameterName){
    if (prefix == null) {
      throw new IllegalArgumentException("Null 'prefix' argument.");
    }
    if (categoryParameterName == null) {
      throw new IllegalArgumentException("Null 'categoryParameterName' argument.");
    }
    this.prefix=prefix;
    this.categoryParameterName=categoryParameterName;
    this.indexParameterName=indexParameterName;
  }
  /** 
 * Generates a URL.
 * @param dataset  the dataset (ignored).
 * @param key  the item key (<code>null</code> not permitted).
 * @param pieIndex  the pie index.
 * @return A string containing the generated URL.
 */
  public String generateURL(  PieDataset dataset,  Comparable key,  int pieIndex){
    String url=this.prefix;
    String encodedKey=null;
    try {
      encodedKey=URLEncoder.encode(key.toString(),"UTF-8");
    }
 catch (    UnsupportedEncodingException e) {
      encodedKey=key.toString();
    }
    if (url.indexOf("?") > -1) {
      url+="&amp;" + this.categoryParameterName + "="+ encodedKey;
    }
 else {
      url+="?" + this.categoryParameterName + "="+ encodedKey;
    }
    if (this.indexParameterName != null) {
      url+="&amp;" + this.indexParameterName + "="+ String.valueOf(pieIndex);
    }
    return url;
  }
  /** 
 * Tests if this object is equal to another.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof StandardPieURLGenerator)) {
      return false;
    }
    StandardPieURLGenerator that=(StandardPieURLGenerator)obj;
    if (!this.prefix.equals(that.prefix)) {
      return false;
    }
    if (!this.categoryParameterName.equals(that.categoryParameterName)) {
      return false;
    }
    if (!ObjectUtilities.equal(this.indexParameterName,that.indexParameterName)) {
      return false;
    }
    return true;
  }
}
