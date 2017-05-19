package org.jfree.chart.renderer.category.junit;
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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LevelRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.DefaultCategoryDataset;
/** 
 * Tests for the             {@link LevelRenderer} class.
 */
public class LevelRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(LevelRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public LevelRendererTests(  String name){
    super(name);
  }
  /** 
 * Test that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    LevelRenderer r1=new LevelRenderer();
    LevelRenderer r2=new LevelRenderer();
    assertTrue(r1.equals(r2));
    assertTrue(r2.equals(r1));
    r1.setItemMargin(0.123);
    assertFalse(r1.equals(r2));
    r2.setItemMargin(0.123);
    assertTrue(r1.equals(r2));
    r1.setMaximumItemWidth(0.234);
    assertFalse(r1.equals(r2));
    r2.setMaximumItemWidth(0.234);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    LevelRenderer r1=new LevelRenderer();
    LevelRenderer r2=new LevelRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    LevelRenderer r1=new LevelRenderer();
    r1.setItemMargin(0.123);
    r1.setMaximumItemWidth(0.234);
    LevelRenderer r2=null;
    try {
      r2=(LevelRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      System.err.println("Failed to clone.");
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
    assertTrue(checkIndependence(r1,r2));
  }
  /** 
 * Check that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    LevelRenderer r1=new LevelRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Checks that the two renderers are equal but independent of one another.
 * @param r1  renderer 1.
 * @param r2  renderer 2.
 * @return A boolean.
 */
  private boolean checkIndependence(  LevelRenderer r1,  LevelRenderer r2){
    boolean b0=r1.equals(r2);
    r1.setItemMargin(0.0);
    boolean b1=!r1.equals(r2);
    r2.setItemMargin(0.0);
    boolean b2=r1.equals(r2);
    return b0 && b1 && b2;
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    LevelRenderer r1=new LevelRenderer();
    LevelRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(LevelRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
  /** 
 * Draws the chart with a <code>null</code> info object to make sure that no exceptions are thrown (particularly by code in the renderer).
 */
  public void testDrawWithNullInfo(){
    boolean success=false;
    try {
      DefaultCategoryDataset dataset=new DefaultCategoryDataset();
      dataset.addValue(1.0,"S1","C1");
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),new LevelRenderer());
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
    DefaultCategoryDataset dataset0=new DefaultCategoryDataset();
    dataset0.addValue(21.0,"R1","C1");
    dataset0.addValue(22.0,"R2","C1");
    DefaultCategoryDataset dataset1=new DefaultCategoryDataset();
    dataset1.addValue(23.0,"R3","C1");
    dataset1.addValue(24.0,"R4","C1");
    dataset1.addValue(25.0,"R5","C1");
    LevelRenderer r=new LevelRenderer();
    CategoryPlot plot=new CategoryPlot(dataset0,new CategoryAxis("x"),new NumberAxis("y"),r);
    plot.setDataset(1,dataset1);
    new JFreeChart(plot);
    LegendItem li=r.getLegendItem(1,2);
    assertEquals("R5",li.getLabel());
    assertEquals(1,li.getDatasetIndex());
    assertEquals(2,li.getSeriesIndex());
  }
}
