package org.jfree.chart.editor;
import org.jfree.chart.JFreeChart;
/** 
 * A default implementation of the                                                                                                                                                                {@link ChartEditorFactory} interface.
 */
public class DefaultChartEditorFactory implements ChartEditorFactory {
  /** 
 * Creates a new instance.
 */
  public DefaultChartEditorFactory(){
  }
  /** 
 * Returns a new instance of a                                                                                                                                                                {@link ChartEditor}.
 * @param chart  the chart.
 * @return A chart editor for the given chart.
 */
  public ChartEditor createEditor(  JFreeChart chart){
    return new DefaultChartEditor(chart);
  }
}
