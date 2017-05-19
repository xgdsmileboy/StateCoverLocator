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
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.DefaultCategoryDataset;
/** 
 * Tests for the             {@link LineAndShapeRenderer} class.
 */
public class LineAndShapeRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(LineAndShapeRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public LineAndShapeRendererTests(  String name){
    super(name);
  }
  /** 
 * Test that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    LineAndShapeRenderer r1=new LineAndShapeRenderer();
    LineAndShapeRenderer r2=new LineAndShapeRenderer();
    assertEquals(r1,r2);
    r1.setBaseLinesVisible(!r1.getBaseLinesVisible());
    assertFalse(r1.equals(r2));
    r2.setBaseLinesVisible(r1.getBaseLinesVisible());
    assertTrue(r1.equals(r2));
    r1.setSeriesLinesVisible(1,true);
    assertFalse(r1.equals(r2));
    r2.setSeriesLinesVisible(1,true);
    assertTrue(r1.equals(r2));
    r1.setBaseShapesVisible(!r1.getBaseShapesVisible());
    assertFalse(r1.equals(r2));
    r2.setBaseShapesVisible(r1.getBaseShapesVisible());
    assertTrue(r1.equals(r2));
    r1.setSeriesShapesVisible(1,true);
    assertFalse(r1.equals(r2));
    r2.setSeriesShapesVisible(1,true);
    assertTrue(r1.equals(r2));
    r1.setSeriesShapesFilled(1,true);
    assertFalse(r1.equals(r2));
    r2.setSeriesShapesFilled(1,true);
    assertTrue(r1.equals(r2));
    r1.setBaseShapesFilled(false);
    assertFalse(r1.equals(r2));
    r2.setBaseShapesFilled(false);
    assertTrue(r1.equals(r2));
    r1.setUseOutlinePaint(true);
    assertFalse(r1.equals(r2));
    r2.setUseOutlinePaint(true);
    assertTrue(r1.equals(r2));
    r1.setUseSeriesOffset(true);
    assertFalse(r1.equals(r2));
    r2.setUseSeriesOffset(true);
    assertTrue(r1.equals(r2));
    r1.setItemMargin(0.14);
    assertFalse(r1.equals(r2));
    r2.setItemMargin(0.14);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    LineAndShapeRenderer r1=new LineAndShapeRenderer();
    LineAndShapeRenderer r2=new LineAndShapeRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    LineAndShapeRenderer r1=new LineAndShapeRenderer();
    LineAndShapeRenderer r2=null;
    try {
      r2=(LineAndShapeRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
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
    LineAndShapeRenderer r1=new LineAndShapeRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Checks that the two renderers are equal but independent of one another.
 * @param r1  renderer 1.
 * @param r2  renderer 2.
 * @return A boolean.
 */
  private boolean checkIndependence(  LineAndShapeRenderer r1,  LineAndShapeRenderer r2){
    if (!r1.equals(r2)) {
      return false;
    }
    r1.setBaseLinesVisible(!r1.getBaseLinesVisible());
    if (r1.equals(r2)) {
      return false;
    }
    r2.setBaseLinesVisible(r1.getBaseLinesVisible());
    if (!r1.equals(r2)) {
      return false;
    }
    r1.setSeriesLinesVisible(1,true);
    if (r1.equals(r2)) {
      return false;
    }
    r2.setSeriesLinesVisible(1,true);
    if (!r1.equals(r2)) {
      return false;
    }
    r1.setBaseShapesVisible(!r1.getBaseShapesVisible());
    if (r1.equals(r2)) {
      return false;
    }
    r2.setBaseShapesVisible(r1.getBaseShapesVisible());
    if (!r1.equals(r2)) {
      return false;
    }
    r1.setSeriesShapesVisible(1,true);
    if (r1.equals(r2)) {
      return false;
    }
    r2.setSeriesShapesVisible(1,true);
    if (!r1.equals(r2)) {
      return false;
    }
    r1.setSeriesShapesFilled(0,false);
    r2.setSeriesShapesFilled(0,true);
    if (r1.equals(r2)) {
      return false;
    }
    r2.setSeriesShapesFilled(0,false);
    if (!r1.equals(r2)) {
      return false;
    }
    r1.setBaseShapesFilled(false);
    r2.setBaseShapesFilled(true);
    if (r1.equals(r2)) {
      return false;
    }
    r2.setBaseShapesFilled(false);
    if (!r1.equals(r2)) {
      return false;
    }
    return true;
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    LineAndShapeRenderer r1=new LineAndShapeRenderer();
    LineAndShapeRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(LineAndShapeRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
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
    LineAndShapeRenderer r=new LineAndShapeRenderer();
    CategoryPlot plot=new CategoryPlot(dataset0,new CategoryAxis("x"),new NumberAxis("y"),r);
    plot.setDataset(1,dataset1);
    new JFreeChart(plot);
    LegendItem li=r.getLegendItem(1,2);
    assertEquals("R5",li.getLabel());
    assertEquals(1,li.getDatasetIndex());
    assertEquals(2,li.getSeriesIndex());
  }
}
