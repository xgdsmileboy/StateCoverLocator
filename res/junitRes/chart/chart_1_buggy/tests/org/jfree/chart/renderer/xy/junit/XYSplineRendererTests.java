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
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link XYSplineRenderer} class.
 */
public class XYSplineRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(XYSplineRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public XYSplineRendererTests(  String name){
    super(name);
  }
  /** 
 * Test that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    XYSplineRenderer r1=new XYSplineRenderer();
    XYSplineRenderer r2=new XYSplineRenderer();
    assertEquals(r1,r2);
    assertEquals(r2,r1);
    r1.setPrecision(9);
    assertFalse(r1.equals(r2));
    r2.setPrecision(9);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    XYSplineRenderer r1=new XYSplineRenderer();
    XYSplineRenderer r2=new XYSplineRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    Rectangle2D legendShape=new Rectangle2D.Double(1.0,2.0,3.0,4.0);
    XYSplineRenderer r1=new XYSplineRenderer();
    r1.setLegendLine(legendShape);
    XYSplineRenderer r2=null;
    try {
      r2=(XYSplineRenderer)r1.clone();
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
    XYSplineRenderer r1=new XYSplineRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    XYSplineRenderer r1=new XYSplineRenderer();
    XYSplineRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(XYSplineRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
}
