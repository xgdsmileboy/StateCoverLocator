package org.jfree.chart.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.pie.DefaultPieDataset;
/** 
 * Tests for a pie chart.
 */
public class PieChartTests extends TestCase {
  /** 
 * A chart. 
 */
  private JFreeChart pieChart;
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(PieChartTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public PieChartTests(  String name){
    super(name);
  }
  /** 
 * Common test setup.
 */
  protected void setUp(){
    this.pieChart=createPieChart();
  }
  /** 
 * Using a regular pie chart, we replace the dataset with null.  Expect to receive notification of a chart change event, and (of course) the dataset should be null.
 */
  public void testReplaceDatasetOnPieChart(){
    LocalListener l=new LocalListener();
    this.pieChart.addChangeListener(l);
    PiePlot plot=(PiePlot)this.pieChart.getPlot();
    plot.setDataset(null);
    assertEquals(true,l.flag);
    assertNull(plot.getDataset());
  }
  /** 
 * Creates a pie chart.
 * @return The pie chart.
 */
  private static JFreeChart createPieChart(){
    DefaultPieDataset dataset=new DefaultPieDataset();
    dataset.setValue("Java",new Double(43.2));
    dataset.setValue("Visual Basic",new Double(0.0));
    dataset.setValue("C/C++",new Double(17.5));
    return ChartFactory.createPieChart("Pie Chart",dataset,true);
  }
  /** 
 * A chart change listener.
 */
static class LocalListener implements ChartChangeListener {
    /** 
 * A flag. 
 */
    private boolean flag=false;
    /** 
 * Event handler.
 * @param event  the event.
 */
    public void chartChanged(    ChartChangeEvent event){
      this.flag=true;
    }
  }
}
