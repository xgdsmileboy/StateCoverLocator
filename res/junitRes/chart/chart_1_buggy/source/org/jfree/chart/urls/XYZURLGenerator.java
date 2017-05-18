package org.jfree.chart.urls;
import org.jfree.data.xy.XYZDataset;
/** 
 * Interface for a URL generator for plots that uses data from an     {@link XYZDataset}.  Classes that implement this interface are responsible for correctly escaping any text that is derived from the dataset, as this may be user-specified and could pose a security risk.
 */
public interface XYZURLGenerator extends XYURLGenerator {
  /** 
 * Generates a URL for a particular item within a series. As a guideline, the URL should be valid within the context of an XHTML 1.0 document.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return A string containing the generated URL.
 */
  public String generateURL(  XYZDataset dataset,  int series,  int item);
}
