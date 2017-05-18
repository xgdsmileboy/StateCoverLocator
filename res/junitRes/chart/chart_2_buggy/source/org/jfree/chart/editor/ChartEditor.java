package org.jfree.chart.editor;
import javax.swing.JComponent;
import org.jfree.chart.JFreeChart;
/** 
 * A chart editor is typically a                                                                                               {@link JComponent} containing a user interfacefor modifying the properties of a chart.
 * @see ChartEditorManager#getChartEditor(JFreeChart)
 */
public interface ChartEditor {
  /** 
 * Applies the changes to the specified chart.
 * @param chart  the chart.
 */
  public void updateChart(  JFreeChart chart);
}
