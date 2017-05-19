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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.IntervalBarRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
/** 
 * Tests for the             {@link IntervalBarRenderer} class.
 */
public class IntervalBarRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(IntervalBarRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public IntervalBarRendererTests(  String name){
    super(name);
  }
  /** 
 * Problem that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    IntervalBarRenderer r1=new IntervalBarRenderer();
    IntervalBarRenderer r2=new IntervalBarRenderer();
    assertEquals(r1,r2);
    BarRenderer br=new BarRenderer();
    assertFalse(r1.equals(br));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    IntervalBarRenderer r1=new IntervalBarRenderer();
    IntervalBarRenderer r2=new IntervalBarRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    IntervalBarRenderer r1=new IntervalBarRenderer();
    IntervalBarRenderer r2=null;
    try {
      r2=(IntervalBarRenderer)r1.clone();
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
    IntervalBarRenderer r1=new IntervalBarRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    IntervalBarRenderer r1=new IntervalBarRenderer();
    IntervalBarRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(IntervalBarRenderer)in.readObject();
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
      double[][] starts=new double[][]{{0.1,0.2,0.3},{0.3,0.4,0.5}};
      double[][] ends=new double[][]{{0.5,0.6,0.7},{0.7,0.8,0.9}};
      DefaultIntervalCategoryDataset dataset=new DefaultIntervalCategoryDataset(starts,ends);
      IntervalBarRenderer renderer=new IntervalBarRenderer();
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),renderer);
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
}
