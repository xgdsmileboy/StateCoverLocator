package org.jfree.chart.renderer.xy.junit;
import java.awt.Color;
import java.awt.GradientPaint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.renderer.xy.XYBoxAndWhiskerRenderer;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link XYBoxAndWhiskerRenderer} class.
 */
public class XYBoxAndWhiskerRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(XYBoxAndWhiskerRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public XYBoxAndWhiskerRendererTests(  String name){
    super(name);
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    XYBoxAndWhiskerRenderer r1=new XYBoxAndWhiskerRenderer();
    XYBoxAndWhiskerRenderer r2=new XYBoxAndWhiskerRenderer();
    assertEquals(r1,r2);
    r1.setArtifactPaint(new GradientPaint(1.0f,2.0f,Color.green,3.0f,4.0f,Color.red));
    assertFalse(r1.equals(r2));
    r2.setArtifactPaint(new GradientPaint(1.0f,2.0f,Color.green,3.0f,4.0f,Color.red));
    assertEquals(r1,r2);
    r1.setBoxWidth(0.55);
    assertFalse(r1.equals(r2));
    r2.setBoxWidth(0.55);
    assertEquals(r1,r2);
    r1.setFillBox(!r1.getFillBox());
    assertFalse(r1.equals(r2));
    r2.setFillBox(!r2.getFillBox());
    assertEquals(r1,r2);
    r1.setBoxPaint(Color.yellow);
    assertFalse(r1.equals(r2));
    r2.setBoxPaint(Color.yellow);
    assertEquals(r1,r2);
    r1.setBoxPaint(null);
    assertFalse(r1.equals(r2));
    r2.setBoxPaint(null);
    assertEquals(r1,r2);
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    XYBoxAndWhiskerRenderer r1=new XYBoxAndWhiskerRenderer();
    XYBoxAndWhiskerRenderer r2=new XYBoxAndWhiskerRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    XYBoxAndWhiskerRenderer r1=new XYBoxAndWhiskerRenderer();
    XYBoxAndWhiskerRenderer r2=null;
    try {
      r2=(XYBoxAndWhiskerRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      System.err.println("Failed to clone.");
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
  }
  /** 
 * Verify that this class implements             {@link PublicCloneable}.
 */
  public void testPublicCloneable(){
    XYBoxAndWhiskerRenderer r1=new XYBoxAndWhiskerRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    XYBoxAndWhiskerRenderer r1=new XYBoxAndWhiskerRenderer();
    XYBoxAndWhiskerRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(XYBoxAndWhiskerRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(r1,r2);
  }
}
