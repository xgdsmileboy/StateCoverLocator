package org.jfree.chart.renderer.xy.junit;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
/** 
 * Tests for the             {@link XYAreaRenderer} class.
 */
public class XYAreaRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(XYAreaRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public XYAreaRendererTests(  String name){
    super(name);
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    XYAreaRenderer r1=new XYAreaRenderer();
    XYAreaRenderer r2=new XYAreaRenderer();
    assertEquals(r1,r2);
    r1=new XYAreaRenderer(XYAreaRenderer.AREA_AND_SHAPES);
    assertFalse(r1.equals(r2));
    r2=new XYAreaRenderer(XYAreaRenderer.AREA_AND_SHAPES);
    assertTrue(r1.equals(r2));
    r1=new XYAreaRenderer(XYAreaRenderer.AREA);
    assertFalse(r1.equals(r2));
    r2=new XYAreaRenderer(XYAreaRenderer.AREA);
    assertTrue(r1.equals(r2));
    r1=new XYAreaRenderer(XYAreaRenderer.LINES);
    assertFalse(r1.equals(r2));
    r2=new XYAreaRenderer(XYAreaRenderer.LINES);
    assertTrue(r1.equals(r2));
    r1=new XYAreaRenderer(XYAreaRenderer.SHAPES);
    assertFalse(r1.equals(r2));
    r2=new XYAreaRenderer(XYAreaRenderer.SHAPES);
    assertTrue(r1.equals(r2));
    r1=new XYAreaRenderer(XYAreaRenderer.SHAPES_AND_LINES);
    assertFalse(r1.equals(r2));
    r2=new XYAreaRenderer(XYAreaRenderer.SHAPES_AND_LINES);
    assertTrue(r1.equals(r2));
    r1.setOutline(true);
    assertFalse(r1.equals(r2));
    r2.setOutline(true);
    assertTrue(r1.equals(r2));
    r1.setLegendArea(new Rectangle2D.Double(1.0,2.0,3.0,4.0));
    assertFalse(r1.equals(r2));
    r2.setLegendArea(new Rectangle2D.Double(1.0,2.0,3.0,4.0));
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    XYAreaRenderer r1=new XYAreaRenderer();
    XYAreaRenderer r2=new XYAreaRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    XYAreaRenderer r1=new XYAreaRenderer();
    Rectangle2D rect1=new Rectangle2D.Double(1.0,2.0,3.0,4.0);
    r1.setLegendArea(rect1);
    XYAreaRenderer r2=null;
    try {
      r2=(XYAreaRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
    rect1.setRect(4.0,3.0,2.0,1.0);
    assertFalse(r1.equals(r2));
    r2.setLegendArea(new Rectangle2D.Double(4.0,3.0,2.0,1.0));
    assertTrue(r1.equals(r2));
  }
  /** 
 * Verify that this class implements             {@link PublicCloneable}.
 */
  public void testPublicCloneable(){
    XYAreaRenderer r1=new XYAreaRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    XYAreaRenderer r1=new XYAreaRenderer();
    XYAreaRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(XYAreaRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(r1,r2);
  }
  /** 
 * Draws the chart with a <code>null</code> info object to make sure that no exceptions are thrown (particularly by code in the renderer).
 */
  public void testDrawWithNullInfo(){
    boolean success=false;
    try {
      DefaultTableXYDataset dataset=new DefaultTableXYDataset();
      XYSeries s1=new XYSeries("Series 1",true,false);
      s1.add(5.0,5.0);
      s1.add(10.0,15.5);
      s1.add(15.0,9.5);
      s1.add(20.0,7.5);
      dataset.addSeries(s1);
      XYSeries s2=new XYSeries("Series 2",true,false);
      s2.add(5.0,5.0);
      s2.add(10.0,15.5);
      s2.add(15.0,9.5);
      s2.add(20.0,3.5);
      dataset.addSeries(s2);
      XYPlot plot=new XYPlot(dataset,new NumberAxis("X"),new NumberAxis("Y"),new XYAreaRenderer());
      JFreeChart chart=new JFreeChart(plot);
      chart.createBufferedImage(300,200,null);
      success=true;
    }
 catch (    NullPointerException e) {
      e.printStackTrace();
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * A check for the datasetIndex and seriesIndex fields in the LegendItem returned by the getLegendItem() method.
 */
  public void testGetLegendItemSeriesIndex(){
    XYSeriesCollection d1=new XYSeriesCollection();
    XYSeries s1=new XYSeries("S1");
    s1.add(1.0,1.1);
    XYSeries s2=new XYSeries("S2");
    s2.add(1.0,1.1);
    d1.addSeries(s1);
    d1.addSeries(s2);
    XYSeriesCollection d2=new XYSeriesCollection();
    XYSeries s3=new XYSeries("S3");
    s3.add(1.0,1.1);
    XYSeries s4=new XYSeries("S4");
    s4.add(1.0,1.1);
    XYSeries s5=new XYSeries("S5");
    s5.add(1.0,1.1);
    d2.addSeries(s3);
    d2.addSeries(s4);
    d2.addSeries(s5);
    XYAreaRenderer r=new XYAreaRenderer();
    XYPlot plot=new XYPlot(d1,new NumberAxis("x"),new NumberAxis("y"),r);
    plot.setDataset(1,d2);
    new JFreeChart(plot);
    LegendItem li=r.getLegendItem(1,2);
    assertEquals("S5",li.getLabel());
    assertEquals(1,li.getDatasetIndex());
    assertEquals(2,li.getSeriesIndex());
  }
}
