package org.jfree.chart.urls;
import org.jfree.data.xy.XYZDataset;
/** 
 * A URL generator.
 */
public class StandardXYZURLGenerator extends StandardXYURLGenerator implements XYZURLGenerator {
  /** 
 * Generates a URL for a particular item within a series.
 * @param dataset  the dataset.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return A string containing the generated URL.
 */
  public String generateURL(  XYZDataset dataset,  int series,  int item){
    return super.generateURL(dataset,series,item);
  }
}
