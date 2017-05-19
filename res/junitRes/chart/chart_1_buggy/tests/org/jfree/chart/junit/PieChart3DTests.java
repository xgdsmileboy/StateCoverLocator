package org.jfree.chart.junit;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.pie.DefaultPieDataset;
import org.jfree.data.pie.PieDataset;
/** 
 * Tests for a pie chart with a 3D effect.
 */
public class PieChart3DTests extends TestCase {
  /** 
 * A chart. 
 */
  private JFreeChart pieChart;
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(PieChart3DTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public PieChart3DTests(  String name){
    super(name);
  }
  /** 
 * Common test setup.
 */
  protected void setUp(){
    DefaultPieDataset dataset=new DefaultPieDataset();
    dataset.setValue("Java",new Double(43.2));
    dataset.setValue("Visual Basic",new Double(0.0));
    dataset.setValue("C/C++",new Double(17.5));
    this.pieChart=createPieChart3D(dataset);
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
 * Tests that no exceptions are thrown when there is a <code>null</code> value in the dataset.
 */
  public void testNullValueInDataset(){
    DefaultPieDataset dataset=new DefaultPieDataset();
    dataset.setValue("Section 1",10.0);
    dataset.setValue("Section 2",11.0);
    dataset.setValue("Section 3",null);
    JFreeChart chart=createPieChart3D(dataset);
    boolean success=false;
    try {
      BufferedImage image=new BufferedImage(200,100,BufferedImage.TYPE_INT_RGB);
      Graphics2D g2=image.createGraphics();
      chart.draw(g2,new Rectangle2D.Double(0,0,200,100),null,null);
      g2.dispose();
      success=true;
    }
 catch (    Throwable t) {
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * Creates a pie chart.
 * @param dataset  the dataset.
 * @return The pie chart.
 */
  private static JFreeChart createPieChart3D(  PieDataset dataset){
    return ChartFactory.createPieChart3D("Pie Chart",dataset,true);
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
