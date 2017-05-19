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
import org.jfree.chart.renderer.AreaRendererEndType;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.DefaultCategoryDataset;
/** 
 * Tests for the             {@link AreaRenderer} class.
 */
public class AreaRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(AreaRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public AreaRendererTests(  String name){
    super(name);
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    AreaRenderer r1=new AreaRenderer();
    AreaRenderer r2=new AreaRenderer();
    assertEquals(r1,r2);
    r1.setEndType(AreaRendererEndType.LEVEL);
    assertFalse(r1.equals(r2));
    r2.setEndType(AreaRendererEndType.LEVEL);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    AreaRenderer r1=new AreaRenderer();
    AreaRenderer r2=new AreaRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    AreaRenderer r1=new AreaRenderer();
    AreaRenderer r2=null;
    try {
      r2=(AreaRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
  }
  /** 
 * Check that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    AreaRenderer r1=new AreaRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    AreaRenderer r1=new AreaRenderer();
    AreaRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(AreaRenderer)in.readObject();
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
    AreaRenderer r=new AreaRenderer();
    CategoryPlot plot=new CategoryPlot(dataset0,new CategoryAxis("x"),new NumberAxis("y"),r);
    plot.setDataset(1,dataset1);
    new JFreeChart(plot);
    LegendItem li=r.getLegendItem(1,2);
    assertEquals("R5",li.getLabel());
    assertEquals(1,li.getDatasetIndex());
    assertEquals(2,li.getSeriesIndex());
  }
}
