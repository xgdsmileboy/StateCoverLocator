package org.jfree.chart.demo;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.util.ApplicationFrame;
import org.jfree.chart.util.RefineryUtilities;
import org.jfree.data.pie.DefaultPieDataset;
import org.jfree.data.pie.PieDataset;
/** 
 * A simple demonstration application showing how to create a pie chart using data from a                                                                                                                                                                     {@link DefaultPieDataset}.
 */
public class PieChartDemo1 extends ApplicationFrame {
  /** 
 * Default constructor.
 * @param title  the frame title.
 */
  public PieChartDemo1(  String title){
    super(title);
    setContentPane(createDemoPanel());
  }
  /** 
 * Creates a sample dataset.
 * @return A sample dataset.
 */
  private static PieDataset createDataset(){
    DefaultPieDataset dataset=new DefaultPieDataset();
    dataset.setValue("One",new Double(43.2));
    dataset.setValue("Two",new Double(10.0));
    dataset.setValue("Three",new Double(27.5));
    dataset.setValue("Four",new Double(17.5));
    dataset.setValue("Five",new Double(11.0));
    dataset.setValue("Six",new Double(19.4));
    return dataset;
  }
  /** 
 * Creates a chart.
 * @param dataset  the dataset.
 * @return A chart.
 */
  private static JFreeChart createChart(  PieDataset dataset){
    JFreeChart chart=ChartFactory.createPieChart("Pie Chart Demo 1",dataset,true);
    PiePlot plot=(PiePlot)chart.getPlot();
    plot.setSectionOutlinesVisible(false);
    plot.setNoDataMessage("No data available");
    return chart;
  }
  /** 
 * Creates a panel for the demo (used by SuperDemo.java).
 * @return A panel.
 */
  public static JPanel createDemoPanel(){
    JFreeChart chart=createChart(createDataset());
    return new ChartPanel(chart);
  }
  /** 
 * Starting point for the demonstration application.
 * @param args  ignored.
 */
  public static void main(  String[] args){
    PieChartDemo1 demo=new PieChartDemo1("Pie Chart Demo 1");
    demo.pack();
    RefineryUtilities.centerFrameOnScreen(demo);
    demo.setVisible(true);
  }
}
