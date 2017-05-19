package org.jfree.chart.urls;
import org.jfree.data.pie.PieDataset;
/** 
 * Interface for a URL generator for plots that use data from a                              {@link PieDataset}.  Classes that implement this interface: <ul> <li>are responsible for correctly escaping any text that is derived from the dataset, as this may be user-specified and could pose a security risk;</li> <li>should be either (a) immutable, or (b) cloneable via the <code>PublicCloneable</code> interface (defined in the JCommon class library).  This provides a mechanism for the referring plot to clone the generator if necessary.</li> </ul>
 */
public interface PieURLGenerator {
  /** 
 * Generates a URL for one item in a                               {@link PieDataset}. As a guideline, the URL should be valid within the context of an XHTML 1.0 document.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param key  the item key (<code>null</code> not permitted).
 * @param pieIndex  the pie index (differentiates between pies in a'multi' pie chart).
 * @return A string containing the URL.
 */
  public String generateURL(  PieDataset dataset,  Comparable key,  int pieIndex);
}
