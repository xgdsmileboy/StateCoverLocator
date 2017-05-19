package org.jfree.chart.renderer.xy.junit;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.TableXYDataset;
/** 
 * Tests for the             {@link StackedXYAreaRenderer2} class.
 */
public class StackedXYAreaRenderer2Tests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StackedXYAreaRenderer2Tests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StackedXYAreaRenderer2Tests(  String name){
    super(name);
  }
  /** 
 * Test chart drawing with an empty dataset to ensure that this special case doesn't cause any exceptions.
 */
  public void testDrawWithEmptyDataset(){
    boolean success=false;
    JFreeChart chart=ChartFactory.createStackedXYAreaChart("title","x","y",new DefaultTableXYDataset(),true);
    XYPlot plot=(XYPlot)chart.getPlot();
    plot.setRenderer(new StackedXYAreaRenderer2());
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
  /** 
 * Test that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    StackedXYAreaRenderer2 r1=new StackedXYAreaRenderer2();
    StackedXYAreaRenderer2 r2=new StackedXYAreaRenderer2();
    assertEquals(r1,r2);
    assertEquals(r2,r1);
    r1.setRoundXCoordinates(!r1.getRoundXCoordinates());
    assertFalse(r1.equals(r2));
    r2.setRoundXCoordinates(r1.getRoundXCoordinates());
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    StackedXYAreaRenderer2 r1=new StackedXYAreaRenderer2();
    StackedXYAreaRenderer2 r2=new StackedXYAreaRenderer2();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    StackedXYAreaRenderer2 r1=new StackedXYAreaRenderer2();
    StackedXYAreaRenderer2 r2=null;
    try {
      r2=(StackedXYAreaRenderer2)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
  }
  /** 
 * Verify that this class implements             {@link PublicCloneable}.
 */
  public void testPublicCloneable(){
    StackedXYAreaRenderer2 r1=new StackedXYAreaRenderer2();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    StackedXYAreaRenderer2 r1=new StackedXYAreaRenderer2();
    StackedXYAreaRenderer2 r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(StackedXYAreaRenderer2)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
  /** 
 * Check that the renderer is calculating the range bounds correctly.
 */
  public void testFindRangeBounds(){
    TableXYDataset dataset=RendererXYPackageTests.createTestTableXYDataset();
    JFreeChart chart=ChartFactory.createStackedXYAreaChart("Test Chart","X","Y",dataset,false);
    XYPlot plot=(XYPlot)chart.getPlot();
    StackedXYAreaRenderer2 renderer=new StackedXYAreaRenderer2();
    plot.setRenderer(renderer);
    NumberAxis rangeAxis=(NumberAxis)plot.getRangeAxis();
    Range bounds=rangeAxis.getRange();
    assertTrue(bounds.contains(6.0));
    assertTrue(bounds.contains(8.0));
    assertNull(renderer.findRangeBounds(null));
    assertNull(renderer.findRangeBounds(new DefaultTableXYDataset()));
  }
}
