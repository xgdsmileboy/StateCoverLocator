package org.jfree.chart.plot.junit;
import java.awt.Rectangle;
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
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.plot.PlotRenderingInfo;
/** 
 * Tests for the             {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(PlotRenderingInfoTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public PlotRenderingInfoTests(  String name){
    super(name);
  }
  /** 
 * Test the equals() method.
 */
  public void testEquals(){
    PlotRenderingInfo p1=new PlotRenderingInfo(new ChartRenderingInfo());
    PlotRenderingInfo p2=new PlotRenderingInfo(new ChartRenderingInfo());
    assertTrue(p1.equals(p2));
    assertTrue(p2.equals(p1));
    p1.setPlotArea(new Rectangle(2,3,4,5));
    assertFalse(p1.equals(p2));
    p2.setPlotArea(new Rectangle(2,3,4,5));
    assertTrue(p1.equals(p2));
    p1.setDataArea(new Rectangle(2,4,6,8));
    assertFalse(p1.equals(p2));
    p2.setDataArea(new Rectangle(2,4,6,8));
    assertTrue(p1.equals(p2));
    p1.addSubplotInfo(new PlotRenderingInfo(null));
    assertFalse(p1.equals(p2));
    p2.addSubplotInfo(new PlotRenderingInfo(null));
    assertTrue(p1.equals(p2));
    p1.getSubplotInfo(0).setDataArea(new Rectangle(1,2,3,4));
    assertFalse(p1.equals(p2));
    p2.getSubplotInfo(0).setDataArea(new Rectangle(1,2,3,4));
    assertTrue(p1.equals(p2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    PlotRenderingInfo p1=new PlotRenderingInfo(new ChartRenderingInfo());
    p1.setPlotArea(new Rectangle2D.Double());
    PlotRenderingInfo p2=null;
    try {
      p2=(PlotRenderingInfo)p1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(p1 != p2);
    assertTrue(p1.getClass() == p2.getClass());
    assertTrue(p1.equals(p2));
    p1.getPlotArea().setRect(1.0,2.0,3.0,4.0);
    assertFalse(p1.equals(p2));
    p2.getPlotArea().setRect(1.0,2.0,3.0,4.0);
    assertTrue(p1.equals(p2));
    p1.getDataArea().setRect(4.0,3.0,2.0,1.0);
    assertFalse(p1.equals(p2));
    p2.getDataArea().setRect(4.0,3.0,2.0,1.0);
    assertTrue(p1.equals(p2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    PlotRenderingInfo p1=new PlotRenderingInfo(new ChartRenderingInfo());
    PlotRenderingInfo p2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(p1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      p2=(PlotRenderingInfo)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(p1,p2);
  }
}
