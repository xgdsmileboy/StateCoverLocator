package org.jfree.chart.renderer.category.junit;
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
import org.jfree.chart.renderer.category.WaterfallBarRenderer;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link WaterfallBarRenderer} class.
 */
public class WaterfallBarRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(WaterfallBarRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public WaterfallBarRendererTests(  String name){
    super(name);
  }
  /** 
 * Some tests for the findRangeBounds() method.
 */
  public void testFindRangeBounds(){
    WaterfallBarRenderer r=new WaterfallBarRenderer();
    assertNull(r.findRangeBounds(null));
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    WaterfallBarRenderer r1=new WaterfallBarRenderer();
    WaterfallBarRenderer r2=new WaterfallBarRenderer();
    assertEquals(r1,r2);
    r1.setFirstBarPaint(Color.cyan);
    assertFalse(r1.equals(r2));
    r2.setFirstBarPaint(Color.cyan);
    assertTrue(r1.equals(r2));
    r1.setLastBarPaint(Color.cyan);
    assertFalse(r1.equals(r2));
    r2.setLastBarPaint(Color.cyan);
    assertTrue(r1.equals(r2));
    r1.setPositiveBarPaint(Color.cyan);
    assertFalse(r1.equals(r2));
    r2.setPositiveBarPaint(Color.cyan);
    assertTrue(r1.equals(r2));
    r1.setNegativeBarPaint(Color.cyan);
    assertFalse(r1.equals(r2));
    r2.setNegativeBarPaint(Color.cyan);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    WaterfallBarRenderer r1=new WaterfallBarRenderer();
    WaterfallBarRenderer r2=new WaterfallBarRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    WaterfallBarRenderer r1=new WaterfallBarRenderer();
    WaterfallBarRenderer r2=null;
    try {
      r2=(WaterfallBarRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      System.err.println("Failed to clone.");
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
    r1.setFirstBarPaint(Color.yellow);
    assertFalse(r1.equals(r2));
    r2.setFirstBarPaint(Color.yellow);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Check that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    WaterfallBarRenderer r1=new WaterfallBarRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    WaterfallBarRenderer r1=new WaterfallBarRenderer();
    WaterfallBarRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(WaterfallBarRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(r1,r2);
  }
}
