package org.jfree.chart.junit;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
/** 
 * Some tests for an XY line chart.
 */
public class XYLineChartTests extends TestCase {
  /** 
 * A chart. 
 */
  private JFreeChart chart;
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(XYLineChartTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public XYLineChartTests(  String name){
    super(name);
  }
  /** 
 * Common test setup.
 */
  protected void setUp(){
    this.chart=createChart();
  }
  /** 
 * Draws the chart with a null info object to make sure that no exceptions are thrown (a problem that was occurring at one point).
 */
  public void testDrawWithNullInfo(){
    boolean success=false;
    try {
      BufferedImage image=new BufferedImage(200,100,BufferedImage.TYPE_INT_RGB);
      Graphics2D g2=image.createGraphics();
      this.chart.draw(g2,new Rectangle2D.Double(0,0,200,100),null,null);
      g2.dispose();
      success=true;
    }
 catch (    Exception e) {
      success=false;
      e.printStackTrace();
    }
    assertTrue(success);
  }
  /** 
 * Replaces the dataset and checks that it has changed as expected.
 */
  public void testReplaceDataset(){
    XYSeries series1=new XYSeries("Series 1");
    series1.add(10.0,10.0);
    series1.add(20.0,20.0);
    series1.add(30.0,30.0);
    XYDataset dataset=new XYSeriesCollection(series1);
    LocalListener l=new LocalListener();
    this.chart.addChangeListener(l);
    XYPlot plot=(XYPlot)this.chart.getPlot();
    plot.setDataset(dataset);
    assertEquals(true,l.flag);
    ValueAxis axis=plot.getRangeAxis();
    Range range=axis.getRange();
    assertTrue("Expecting the lower bound of the range to be around 10: " + range.getLowerBound(),range.getLowerBound() <= 10);
    assertTrue("Expecting the upper bound of the range to be around 30: " + range.getUpperBound(),range.getUpperBound() >= 30);
  }
  /** 
 * Check that setting a tool tip generator for a series does override the default generator.
 */
  public void testSetSeriesToolTipGenerator(){
    XYPlot plot=(XYPlot)this.chart.getPlot();
    XYItemRenderer renderer=plot.getRenderer();
    StandardXYToolTipGenerator tt=new StandardXYToolTipGenerator();
    renderer.setSeriesToolTipGenerator(0,tt);
    XYToolTipGenerator tt2=renderer.getToolTipGenerator(0,0,false);
    assertTrue(tt2 == tt);
  }
  /** 
 * Create a horizontal bar chart with sample data in the range -3 to +3.
 * @return The chart.
 */
  private static JFreeChart createChart(){
    XYSeries series1=new XYSeries("Series 1");
    series1.add(1.0,1.0);
    series1.add(2.0,2.0);
    series1.add(3.0,3.0);
    XYDataset dataset=new XYSeriesCollection(series1);
    return ChartFactory.createXYLineChart("XY Line Chart","Domain","Range",dataset,true);
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
