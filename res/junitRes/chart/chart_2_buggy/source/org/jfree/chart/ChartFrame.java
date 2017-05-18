package org.jfree.chart;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
/** 
 * A frame for displaying a chart.
 */
public class ChartFrame extends JFrame {
  /** 
 * The chart panel. 
 */
  private ChartPanel chartPanel;
  /** 
 * Constructs a frame for a chart.
 * @param title  the frame title.
 * @param chart  the chart.
 */
  public ChartFrame(  String title,  JFreeChart chart){
    this(title,chart,false);
  }
  /** 
 * Constructs a frame for a chart.
 * @param title  the frame title.
 * @param chart  the chart.
 * @param scrollPane  if <code>true</code>, put the Chart(Panel) into aJScrollPane.
 */
  public ChartFrame(  String title,  JFreeChart chart,  boolean scrollPane){
    super(title);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.chartPanel=new ChartPanel(chart);
    if (scrollPane) {
      setContentPane(new JScrollPane(this.chartPanel));
    }
 else {
      setContentPane(this.chartPanel);
    }
  }
  /** 
 * Returns the chart panel for the frame.
 * @return The chart panel.
 */
  public ChartPanel getChartPanel(){
    return this.chartPanel;
  }
}
