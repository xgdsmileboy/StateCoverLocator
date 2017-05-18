package org.jfree.chart;
import org.jfree.chart.JFreeChart;
/** 
 * A                                                                                               {@link ChartTheme} a class that can apply a style or 'theme' to a chart.It can be implemented in an arbitrary manner, with the styling applied to the chart via the <code>apply(JFreeChart)</code> method.  We provide one implementation ( {@link StandardChartTheme}) that just mimics the manual process of calling methods to set various chart parameters.
 * @since 1.0.11
 */
public interface ChartTheme {
  /** 
 * Applies this theme to the supplied chart.
 * @param chart  the chart (<code>null</code> not permitted).
 */
  public void apply(  JFreeChart chart);
}
