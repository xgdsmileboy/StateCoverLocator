package org.jfree.chart.renderer.category.junit;
import java.awt.Color;
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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.StatisticalLineAndShapeRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
/** 
 * Tests for the             {@link StatisticalLineAndShapeRenderer} class.
 */
public class StatisticalLineAndShapeRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StatisticalLineAndShapeRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StatisticalLineAndShapeRendererTests(  String name){
    super(name);
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    StatisticalLineAndShapeRenderer r1=new StatisticalLineAndShapeRenderer();
    StatisticalLineAndShapeRenderer r2=new StatisticalLineAndShapeRenderer();
    assertTrue(r1.equals(r2));
    assertTrue(r2.equals(r1));
    r1.setErrorIndicatorPaint(Color.red);
    assertFalse(r1.equals(r2));
    r2.setErrorIndicatorPaint(Color.red);
    assertTrue(r2.equals(r1));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    StatisticalLineAndShapeRenderer r1=new StatisticalLineAndShapeRenderer();
    StatisticalLineAndShapeRenderer r2=new StatisticalLineAndShapeRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    StatisticalLineAndShapeRenderer r1=new StatisticalLineAndShapeRenderer();
    StatisticalLineAndShapeRenderer r2=null;
    try {
      r2=(StatisticalLineAndShapeRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      System.err.println("Failed to clone.");
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
  }
  /** 
 * Check that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    StatisticalLineAndShapeRenderer r1=new StatisticalLineAndShapeRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    StatisticalLineAndShapeRenderer r1=new StatisticalLineAndShapeRenderer();
    StatisticalLineAndShapeRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(StatisticalLineAndShapeRenderer)in.readObject();
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
      DefaultStatisticalCategoryDataset dataset=new DefaultStatisticalCategoryDataset();
      dataset.add(1.0,2.0,"S1","C1");
      dataset.add(3.0,4.0,"S1","C2");
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),new StatisticalLineAndShapeRenderer());
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
 * A simple test for bug report 1562759.
 */
  public void test1562759(){
    StatisticalLineAndShapeRenderer r=new StatisticalLineAndShapeRenderer(true,false);
    assertTrue(r.getBaseLinesVisible());
    assertFalse(r.getBaseShapesVisible());
    r=new StatisticalLineAndShapeRenderer(false,true);
    assertFalse(r.getBaseLinesVisible());
    assertTrue(r.getBaseShapesVisible());
  }
}
