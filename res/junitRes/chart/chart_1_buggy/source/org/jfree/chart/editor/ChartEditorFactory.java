package org.jfree.chart.editor;
import org.jfree.chart.JFreeChart;
/** 
 * A factory for creating new                               {@link ChartEditor} instances.
 */
public interface ChartEditorFactory {
  /** 
 * Creates an editor for the given chart.
 * @param chart  the chart.
 * @return A chart editor.
 */
  public ChartEditor createEditor(  JFreeChart chart);
}
