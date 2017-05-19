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
import org.jfree.chart.renderer.category.ScatterRenderer;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link ScatterRenderer} class.
 */
public class ScatterRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(ScatterRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public ScatterRendererTests(  String name){
    super(name);
  }
  /** 
 * Test that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    ScatterRenderer r1=new ScatterRenderer();
    ScatterRenderer r2=new ScatterRenderer();
    assertEquals(r1,r2);
    r1.setSeriesShapesFilled(1,true);
    assertFalse(r1.equals(r2));
    r2.setSeriesShapesFilled(1,true);
    assertTrue(r1.equals(r2));
    r1.setBaseShapesFilled(false);
    assertFalse(r1.equals(r2));
    r2.setBaseShapesFilled(false);
    assertTrue(r1.equals(r2));
    r1.setUseFillPaint(true);
    assertFalse(r1.equals(r2));
    r2.setUseFillPaint(true);
    assertTrue(r1.equals(r2));
    r1.setDrawOutlines(true);
    assertFalse(r1.equals(r2));
    r2.setDrawOutlines(true);
    assertTrue(r1.equals(r2));
    r1.setUseOutlinePaint(true);
    assertFalse(r1.equals(r2));
    r2.setUseOutlinePaint(true);
    assertTrue(r1.equals(r2));
    r1.setUseSeriesOffset(false);
    assertFalse(r1.equals(r2));
    r2.setUseSeriesOffset(false);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    ScatterRenderer r1=new ScatterRenderer();
    ScatterRenderer r2=new ScatterRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    ScatterRenderer r1=new ScatterRenderer();
    ScatterRenderer r2=null;
    try {
      r2=(ScatterRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
    assertTrue(checkIndependence(r1,r2));
  }
  /** 
 * Check that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    ScatterRenderer r1=new ScatterRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Checks that the two renderers are equal but independent of one another.
 * @param r1  renderer 1.
 * @param r2  renderer 2.
 * @return A boolean.
 */
  private boolean checkIndependence(  ScatterRenderer r1,  ScatterRenderer r2){
    if (!r1.equals(r2)) {
      return false;
    }
    r1.setSeriesShapesFilled(1,true);
    if (r1.equals(r2)) {
      return false;
    }
    r2.setSeriesShapesFilled(1,true);
    if (!r1.equals(r2)) {
      return false;
    }
    r1.setBaseShapesFilled(false);
    r2.setBaseShapesFilled(true);
    if (r1.equals(r2)) {
      return false;
    }
    r2.setBaseShapesFilled(false);
    if (!r1.equals(r2)) {
      return false;
    }
    return true;
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    ScatterRenderer r1=new ScatterRenderer();
    ScatterRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(ScatterRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
}