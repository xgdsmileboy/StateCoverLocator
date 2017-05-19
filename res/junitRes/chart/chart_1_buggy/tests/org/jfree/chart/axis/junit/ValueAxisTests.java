package org.jfree.chart.axis.junit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
/** 
 * Tests for the             {@link ValueAxis} class.
 */
public class ValueAxisTests extends TestCase {
  private static final double EPSILON=0.000000001;
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(ValueAxisTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public ValueAxisTests(  String name){
    super(name);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    ValueAxis a1=new NumberAxis("Test");
    ValueAxis a2=null;
    try {
      a2=(NumberAxis)a1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(a1 != a2);
    assertTrue(a1.getClass() == a2.getClass());
    assertTrue(a1.equals(a2));
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    NumberAxis a1=new NumberAxis("Test");
    NumberAxis a2=new NumberAxis("Test");
    assertTrue(a1.equals(a2));
    a1.setAxisLineVisible(false);
    assertFalse(a1.equals(a2));
    a2.setAxisLineVisible(false);
    assertTrue(a1.equals(a2));
    a1.setPositiveArrowVisible(true);
    assertFalse(a1.equals(a2));
    a2.setPositiveArrowVisible(true);
    assertTrue(a1.equals(a2));
    a1.setNegativeArrowVisible(true);
    assertFalse(a1.equals(a2));
    a2.setNegativeArrowVisible(true);
    assertTrue(a1.equals(a2));
    a1.setAxisLinePaint(Color.blue);
    assertFalse(a1.equals(a2));
    a2.setAxisLinePaint(Color.blue);
    assertTrue(a1.equals(a2));
    Stroke stroke=new BasicStroke(2.0f);
    a1.setAxisLineStroke(stroke);
    assertFalse(a1.equals(a2));
    a2.setAxisLineStroke(stroke);
    assertTrue(a1.equals(a2));
    a1.setInverted(true);
    assertFalse(a1.equals(a2));
    a2.setInverted(true);
    assertTrue(a1.equals(a2));
    a1.setRange(new Range(50.0,75.0));
    assertFalse(a1.equals(a2));
    a2.setRange(new Range(50.0,75.0));
    assertTrue(a1.equals(a2));
    a1.setAutoRange(true);
    assertFalse(a1.equals(a2));
    a2.setAutoRange(true);
    assertTrue(a1.equals(a2));
    a1.setAutoRangeMinimumSize(3.33);
    assertFalse(a1.equals(a2));
    a2.setAutoRangeMinimumSize(3.33);
    assertTrue(a1.equals(a2));
    a1.setDefaultAutoRange(new Range(1.2,3.4));
    assertFalse(a1.equals(a2));
    a2.setDefaultAutoRange(new Range(1.2,3.4));
    assertTrue(a1.equals(a2));
    a1.setUpperMargin(0.09);
    assertFalse(a1.equals(a2));
    a2.setUpperMargin(0.09);
    assertTrue(a1.equals(a2));
    a1.setLowerMargin(0.09);
    assertFalse(a1.equals(a2));
    a2.setLowerMargin(0.09);
    assertTrue(a1.equals(a2));
    a1.setFixedAutoRange(50.0);
    assertFalse(a1.equals(a2));
    a2.setFixedAutoRange(50.0);
    assertTrue(a1.equals(a2));
    a1.setAutoTickUnitSelection(false);
    assertFalse(a1.equals(a2));
    a2.setAutoTickUnitSelection(false);
    assertTrue(a1.equals(a2));
    a1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    assertFalse(a1.equals(a2));
    a2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    assertTrue(a1.equals(a2));
    a1.setVerticalTickLabels(true);
    assertFalse(a1.equals(a2));
    a2.setVerticalTickLabels(true);
    assertTrue(a1.equals(a2));
  }
  /** 
 * Tests the the lower and upper margin settings produce the expected results.
 */
  public void testAxisMargins(){
    XYSeries series=new XYSeries("S1");
    series.add(100.0,1.1);
    series.add(200.0,2.2);
    XYSeriesCollection dataset=new XYSeriesCollection(series);
    dataset.setIntervalWidth(0.0);
    JFreeChart chart=ChartFactory.createScatterPlot("Title","X","Y",dataset,false);
    ValueAxis domainAxis=((XYPlot)chart.getPlot()).getDomainAxis();
    Range r=domainAxis.getRange();
    assertEquals(110.0,r.getLength(),EPSILON);
    domainAxis.setLowerMargin(0.10);
    domainAxis.setUpperMargin(0.10);
    r=domainAxis.getRange();
    assertEquals(120.0,r.getLength(),EPSILON);
  }
}
