package org.jfree.chart.plot.junit;
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
import org.jfree.chart.plot.PiePlot3D;
/** 
 * Tests for the             {@link PiePlot3D} class.
 */
public class PiePlot3DTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(PiePlot3DTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public PiePlot3DTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the equals() method.
 */
  public void testEquals(){
    PiePlot3D p1=new PiePlot3D();
    PiePlot3D p2=new PiePlot3D();
    assertTrue(p1.equals(p2));
    assertTrue(p2.equals(p1));
    p1.setDepthFactor(1.23);
    assertFalse(p1.equals(p2));
    p2.setDepthFactor(1.23);
    assertTrue(p1.equals(p2));
    p1.setDarkerSides(true);
    assertFalse(p1.equals(p2));
    p2.setDarkerSides(true);
    assertTrue(p1.equals(p2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    PiePlot3D p1=new PiePlot3D(null);
    PiePlot3D p2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(p1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      p2=(PiePlot3D)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(p1,p2);
  }
  /** 
 * Draws a pie chart where the label generator returns null.
 */
  public void testDrawWithNullDataset(){
    JFreeChart chart=ChartFactory.createPieChart3D("Test",null,true);
    boolean success=false;
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
}
