package org.jfree.chart.urls;
import org.jfree.data.category.CategoryDataset;
/** 
 * A URL generator for items in a                                                                                                                                                                     {@link CategoryDataset}.
 */
public interface CategoryURLGenerator {
  /** 
 * Returns a URL for one item in a dataset. As a guideline, the URL should be valid within the context of an XHTML 1.0 document.  Classes that implement this interface are responsible for correctly escaping any text that is derived from the dataset, as this may be user-specified and could pose a security risk.
 * @param dataset  the dataset.
 * @param series  the series (zero-based index).
 * @param category  the category.
 * @return A string containing the URL.
 */
  public String generateURL(  CategoryDataset dataset,  int series,  int category);
}
