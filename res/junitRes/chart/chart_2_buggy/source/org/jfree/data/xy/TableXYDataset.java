package org.jfree.data.xy;
/** 
 * A dataset containing one or more data series containing (x, y) data items, where all series in the dataset share the same set of x-values.  This is a restricted form of the                                                                                                                                                               {@link XYDataset} interface (which allows independentx-values between series). This is used primarily by the {@link org.jfree.chart.renderer.xy.StackedXYAreaRenderer}.
 */
public interface TableXYDataset extends XYDataset {
  /** 
 * Returns the number of items every series.
 * @return The item count.
 */
  public int getItemCount();
}
