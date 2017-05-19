package org.jfree.chart.renderer.xy.junit;
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
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
/** 
 * Tests for the             {@link XYShapeRenderer} class.
 */
public class XYShapeRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(XYShapeRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public XYShapeRendererTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the equals() method.
 */
  public void testEquals(){
    XYShapeRenderer r1=new XYShapeRenderer();
    XYShapeRenderer r2=new XYShapeRenderer();
    assertTrue(r1.equals(r2));
    assertTrue(r2.equals(r1));
    r1.setPaintScale(new LookupPaintScale(1.0,2.0,Color.white));
    assertFalse(r1.equals(r2));
    r2.setPaintScale(new LookupPaintScale(1.0,2.0,Color.white));
    assertTrue(r1.equals(r2));
    r1.setDrawOutlines(true);
    assertFalse(r1.equals(r2));
    r2.setDrawOutlines(true);
    assertTrue(r1.equals(r2));
    r1.setUseOutlinePaint(false);
    assertFalse(r1.equals(r2));
    r2.setUseOutlinePaint(false);
    assertTrue(r1.equals(r2));
    r1.setUseFillPaint(true);
    assertFalse(r1.equals(r2));
    r2.setUseFillPaint(true);
    assertTrue(r1.equals(r2));
    r1.setGuideLinesVisible(true);
    assertFalse(r1.equals(r2));
    r2.setGuideLinesVisible(true);
    assertTrue(r1.equals(r2));
    r1.setGuideLinePaint(Color.red);
    assertFalse(r1.equals(r2));
    r2.setGuideLinePaint(Color.red);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    XYShapeRenderer r1=new XYShapeRenderer();
    XYShapeRenderer r2=null;
    try {
      r2=(XYShapeRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    XYShapeRenderer r1=new XYShapeRenderer();
    XYShapeRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(XYShapeRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
}
