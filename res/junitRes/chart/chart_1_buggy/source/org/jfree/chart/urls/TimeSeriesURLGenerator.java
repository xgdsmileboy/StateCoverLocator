package org.jfree.chart.urls;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import org.jfree.data.xy.XYDataset;
/** 
 * A URL generator for time series charts.
 */
public class TimeSeriesURLGenerator implements XYURLGenerator, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-9122773175671182445L;
  /** 
 * A formatter for the date. 
 */
  private DateFormat dateFormat=DateFormat.getInstance();
  /** 
 * Prefix to the URL 
 */
  private String prefix="index.html";
  /** 
 * Name to use to identify the series 
 */
  private String seriesParameterName="series";
  /** 
 * Name to use to identify the item 
 */
  private String itemParameterName="item";
  /** 
 * Default constructor.
 */
  public TimeSeriesURLGenerator(){
    super();
  }
  /** 
 * Construct TimeSeriesURLGenerator overriding defaults.
 * @param dateFormat  a formatter for the date (<code>null</code> notpermitted).
 * @param prefix  the prefix of the URL (<code>null</code> not permitted).
 * @param seriesParameterName  the name of the series parameter in the URL(<code>null</code> not permitted).
 * @param itemParameterName  the name of the item parameter in the URL(<code>null</code> not permitted).
 */
  public TimeSeriesURLGenerator(  DateFormat dateFormat,  String prefix,  String seriesParameterName,  String itemParameterName){
    if (dateFormat == null) {
      throw new IllegalArgumentException("Null 'dateFormat' argument.");
    }
    if (prefix == null) {
      throw new IllegalArgumentException("Null 'prefix' argument.");
    }
    if (seriesParameterName == null) {
      throw new IllegalArgumentException("Null 'seriesParameterName' argument.");
    }
    if (itemParameterName == null) {
      throw new IllegalArgumentException("Null 'itemParameterName' argument.");
    }
    this.dateFormat=(DateFormat)dateFormat.clone();
    this.prefix=prefix;
    this.seriesParameterName=seriesParameterName;
    this.itemParameterName=itemParameterName;
  }
  /** 
 * Returns a clone of the date format assigned to this URL generator.
 * @return The date format (never <code>null</code>).
 * @since 1.0.6
 */
  public DateFormat getDateFormat(){
    return (DateFormat)this.dateFormat.clone();
  }
  /** 
 * Returns the prefix string.
 * @return The prefix string (never <code>null</code>).
 * @since 1.0.6
 */
  public String getPrefix(){
    return this.prefix;
  }
  /** 
 * Returns the series parameter name.
 * @return The series parameter name (never <code>null</code>).
 * @since 1.0.6
 */
  public String getSeriesParameterName(){
    return this.seriesParameterName;
  }
  /** 
 * Returns the item parameter name.
 * @return The item parameter name (never <code>null</code>).
 * @since 1.0.6
 */
  public String getItemParameterName(){
    return this.itemParameterName;
  }
  /** 
 * Generates a URL for a particular item within a series.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series number (zero-based index).
 * @param item  the item number (zero-based index).
 * @return The generated URL.
 */
  public String generateURL(  XYDataset dataset,  int series,  int item){
    String result=this.prefix;
    boolean firstParameter=result.indexOf("?") == -1;
    Comparable seriesKey=dataset.getSeriesKey(series);
    if (seriesKey != null) {
      result+=firstParameter ? "?" : "&amp;";
      String s=null;
      try {
        s=URLEncoder.encode(seriesKey.toString(),"UTF-8");
      }
 catch (      UnsupportedEncodingException e) {
        s=seriesKey.toString();
      }
      result+=this.seriesParameterName + "=" + s;
      firstParameter=false;
    }
    long x=(long)dataset.getXValue(series,item);
    String xValue=this.dateFormat.format(new Date(x));
    result+=firstParameter ? "?" : "&amp;";
    String s=null;
    try {
      s=URLEncoder.encode(xValue,"UTF-8");
    }
 catch (    UnsupportedEncodingException e) {
      s=xValue;
    }
    result+=this.itemParameterName + "=" + s;
    return result;
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
    if (!(obj instanceof TimeSeriesURLGenerator)) {
      return false;
    }
    TimeSeriesURLGenerator that=(TimeSeriesURLGenerator)obj;
    if (!this.dateFormat.equals(that.dateFormat)) {
      return false;
    }
    if (!this.itemParameterName.equals(that.itemParameterName)) {
      return false;
    }
    if (!this.prefix.equals(that.prefix)) {
      return false;
    }
    if (!this.seriesParameterName.equals(that.seriesParameterName)) {
      return false;
    }
    return true;
  }
}
