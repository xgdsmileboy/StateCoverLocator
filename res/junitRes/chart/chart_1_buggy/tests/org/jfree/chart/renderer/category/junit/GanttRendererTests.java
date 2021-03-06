package org.jfree.chart.renderer.category.junit;
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
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link GanttRenderer} class.
 */
public class GanttRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(GanttRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public GanttRendererTests(  String name){
    super(name);
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    GanttRenderer r1=new GanttRenderer();
    GanttRenderer r2=new GanttRenderer();
    assertEquals(r1,r2);
    r1.setCompletePaint(Color.yellow);
    assertFalse(r1.equals(r2));
    r2.setCompletePaint(Color.yellow);
    assertTrue(r1.equals(r2));
    r1.setIncompletePaint(Color.green);
    assertFalse(r1.equals(r2));
    r2.setIncompletePaint(Color.green);
    assertTrue(r1.equals(r2));
    r1.setStartPercent(0.11);
    assertFalse(r1.equals(r2));
    r2.setStartPercent(0.11);
    assertTrue(r1.equals(r2));
    r1.setEndPercent(0.88);
    assertFalse(r1.equals(r2));
    r2.setEndPercent(0.88);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    GanttRenderer r1=new GanttRenderer();
    GanttRenderer r2=new GanttRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    GanttRenderer r1=new GanttRenderer();
    GanttRenderer r2=null;
    try {
      r2=(GanttRenderer)r1.clone();
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
    GanttRenderer r1=new GanttRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    GanttRenderer r1=new GanttRenderer();
    r1.setCompletePaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    r1.setIncompletePaint(new GradientPaint(4.0f,3.0f,Color.red,2.0f,1.0f,Color.blue));
    GanttRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(GanttRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
}
