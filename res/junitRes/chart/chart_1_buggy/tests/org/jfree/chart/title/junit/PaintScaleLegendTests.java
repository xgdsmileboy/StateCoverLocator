package org.jfree.chart.title.junit;
import java.awt.BasicStroke;
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
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.title.PaintScaleLegend;
/** 
 * Tests for the             {@link PaintScaleLegend} class.
 */
public class PaintScaleLegendTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(PaintScaleLegendTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public PaintScaleLegendTests(  String name){
    super(name);
  }
  /** 
 * Test that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    PaintScaleLegend l1=new PaintScaleLegend(new GrayPaintScale(),new NumberAxis("X"));
    PaintScaleLegend l2=new PaintScaleLegend(new GrayPaintScale(),new NumberAxis("X"));
    assertTrue(l1.equals(l2));
    assertTrue(l2.equals(l1));
    l1.setScale(new LookupPaintScale());
    assertFalse(l1.equals(l2));
    l2.setScale(new LookupPaintScale());
    assertTrue(l1.equals(l2));
    l1.setAxis(new NumberAxis("Axis 2"));
    assertFalse(l1.equals(l2));
    l2.setAxis(new NumberAxis("Axis 2"));
    assertTrue(l1.equals(l2));
    l1.setAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
    assertFalse(l1.equals(l2));
    l2.setAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
    assertTrue(l1.equals(l2));
    l1.setAxisOffset(99.0);
    assertFalse(l1.equals(l2));
    l2.setAxisOffset(99.0);
    assertTrue(l1.equals(l2));
    l1.setStripWidth(99.0);
    assertFalse(l1.equals(l2));
    l2.setStripWidth(99.0);
    assertTrue(l1.equals(l2));
    l1.setStripOutlineVisible(!l1.isStripOutlineVisible());
    assertFalse(l1.equals(l2));
    l2.setStripOutlineVisible(l1.isStripOutlineVisible());
    assertTrue(l1.equals(l2));
    l1.setStripOutlinePaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    assertFalse(l1.equals(l2));
    l2.setStripOutlinePaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    assertTrue(l1.equals(l2));
    l1.setStripOutlineStroke(new BasicStroke(1.1f));
    assertFalse(l1.equals(l2));
    l2.setStripOutlineStroke(new BasicStroke(1.1f));
    assertTrue(l1.equals(l2));
    l1.setBackgroundPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    assertFalse(l1.equals(l2));
    l2.setBackgroundPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    assertTrue(l1.equals(l2));
    l1.setSubdivisionCount(99);
    assertFalse(l1.equals(l2));
    l2.setSubdivisionCount(99);
    assertTrue(l1.equals(l2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    PaintScaleLegend l1=new PaintScaleLegend(new GrayPaintScale(),new NumberAxis("X"));
    PaintScaleLegend l2=new PaintScaleLegend(new GrayPaintScale(),new NumberAxis("X"));
    assertTrue(l1.equals(l2));
    int h1=l1.hashCode();
    int h2=l2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    PaintScaleLegend l1=new PaintScaleLegend(new GrayPaintScale(),new NumberAxis("X"));
    PaintScaleLegend l2=null;
    try {
      l2=(PaintScaleLegend)l1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(l1 != l2);
    assertTrue(l1.getClass() == l2.getClass());
    assertTrue(l1.equals(l2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    PaintScaleLegend l1=new PaintScaleLegend(new GrayPaintScale(),new NumberAxis("X"));
    PaintScaleLegend l2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(l1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      l2=(PaintScaleLegend)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(l1,l2);
  }
}
