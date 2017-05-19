package org.jfree.chart.junit;
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
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
/** 
 * Tests for the             {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(ChartRenderingInfoTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public ChartRenderingInfoTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    ChartRenderingInfo i1=new ChartRenderingInfo();
    ChartRenderingInfo i2=new ChartRenderingInfo();
    assertTrue(i1.equals(i2));
    i1.setChartArea(new Rectangle2D.Double(1.0,2.0,3.0,4.0));
    assertFalse(i1.equals(i2));
    i2.setChartArea(new Rectangle2D.Double(1.0,2.0,3.0,4.0));
    assertTrue(i1.equals(i2));
    i1.getPlotInfo().setDataArea(new Rectangle(1,2,3,4));
    assertFalse(i1.equals(i2));
    i2.getPlotInfo().setDataArea(new Rectangle(1,2,3,4));
    assertTrue(i1.equals(i2));
    StandardEntityCollection e1=new StandardEntityCollection();
    e1.add(new ChartEntity(new Rectangle(1,2,3,4)));
    i1.setEntityCollection(e1);
    assertFalse(i1.equals(i2));
    StandardEntityCollection e2=new StandardEntityCollection();
    e2.add(new ChartEntity(new Rectangle(1,2,3,4)));
    i2.setEntityCollection(e2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    ChartRenderingInfo i1=new ChartRenderingInfo();
    ChartRenderingInfo i2=null;
    try {
      i2=(ChartRenderingInfo)i1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(i1 != i2);
    assertTrue(i1.getClass() == i2.getClass());
    assertTrue(i1.equals(i2));
    i1.getChartArea().setRect(4.0,3.0,2.0,1.0);
    assertFalse(i1.equals(i2));
    i2.getChartArea().setRect(4.0,3.0,2.0,1.0);
    assertTrue(i1.equals(i2));
    i1.getEntityCollection().add(new ChartEntity(new Rectangle(1,2,2,1)));
    assertFalse(i1.equals(i2));
    i2.getEntityCollection().add(new ChartEntity(new Rectangle(1,2,2,1)));
    assertTrue(i1.equals(i2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    ChartRenderingInfo i1=new ChartRenderingInfo();
    i1.setChartArea(new Rectangle2D.Double(1.0,2.0,3.0,4.0));
    ChartRenderingInfo i2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(i1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      i2=(ChartRenderingInfo)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(i1,i2);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization2(){
    ChartRenderingInfo i1=new ChartRenderingInfo();
    i1.getPlotInfo().setDataArea(new Rectangle2D.Double(1.0,2.0,3.0,4.0));
    ChartRenderingInfo i2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(i1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      i2=(ChartRenderingInfo)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(i1,i2);
    assertEquals(i2,i2.getPlotInfo().getOwner());
  }
}
