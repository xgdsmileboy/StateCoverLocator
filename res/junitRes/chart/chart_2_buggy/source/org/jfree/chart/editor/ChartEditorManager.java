package org.jfree.chart.editor;
import org.jfree.chart.JFreeChart;
/** 
 * The central point for obtaining                                                                                                                                                                     {@link ChartEditor} instances for editingcharts.  Right now, the API is minimal - the plan is to extend this class to provide customisation options for chart editors (for example, make some editor items read-only).
 */
public class ChartEditorManager {
  /** 
 * This factory creates new                                                                                                                                                                     {@link ChartEditor} instances as required. 
 */
  static ChartEditorFactory factory=new DefaultChartEditorFactory();
  /** 
 * Private constructor prevents instantiation.
 */
  private ChartEditorManager(){
  }
  /** 
 * Returns the current factory.
 * @return The current factory (never <code>null</code>).
 */
  public static ChartEditorFactory getChartEditorFactory(){
    return factory;
  }
  /** 
 * Sets the chart editor factory.
 * @param f  the new factory (<code>null</code> not permitted).
 */
  public static void setChartEditorFactory(  ChartEditorFactory f){
    if (f == null) {
      throw new IllegalArgumentException("Null 'f' argument.");
    }
    factory=f;
  }
  /** 
 * Returns a component that can be used to edit the given chart.
 * @param chart  the chart.
 * @return The chart editor.
 */
  public static ChartEditor getChartEditor(  JFreeChart chart){
    return factory.createEditor(chart);
  }
}
