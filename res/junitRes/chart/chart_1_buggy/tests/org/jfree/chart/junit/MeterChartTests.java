package org.jfree.chart.junit;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;
/** 
 * Miscellaneous checks for meter charts.
 */
public class MeterChartTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(MeterChartTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public MeterChartTests(  String name){
    super(name);
  }
  /** 
 * Draws the chart with a single range.  At one point, this caused a null pointer exception (fixed now).
 */
  public void testDrawWithNullInfo(){
    boolean success=false;
    MeterPlot plot=new MeterPlot(new DefaultValueDataset(60.0));
    plot.addInterval(new MeterInterval("Normal",new Range(0.0,80.0)));
    JFreeChart chart=new JFreeChart(plot);
    try {
      BufferedImage image=new BufferedImage(200,100,BufferedImage.TYPE_INT_RGB);
      Graphics2D g2=image.createGraphics();
      chart.draw(g2,new Rectangle2D.Double(0,0,200,100),null,null);
      g2.dispose();
      success=true;
    }
 catch (    Exception e) {
      success=false;
    }
    assertTrue(success);
  }
}
