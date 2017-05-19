package org.jfree.chart.plot.dial.junit;
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
import org.jfree.chart.plot.dial.DialCap;
/** 
 * Tests for the             {@link DialCap} class.
 */
public class DialCapTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(DialCapTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public DialCapTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    DialCap c1=new DialCap();
    DialCap c2=new DialCap();
    assertTrue(c1.equals(c2));
    c1.setRadius(0.5);
    assertFalse(c1.equals(c2));
    c2.setRadius(0.5);
    assertTrue(c1.equals(c2));
    c1.setFillPaint(new GradientPaint(1.0f,2.0f,Color.blue,3.0f,4.0f,Color.green));
    assertFalse(c1.equals(c2));
    c2.setFillPaint(new GradientPaint(1.0f,2.0f,Color.blue,3.0f,4.0f,Color.green));
    c1.setOutlinePaint(new GradientPaint(1.0f,2.0f,Color.white,3.0f,4.0f,Color.gray));
    assertFalse(c1.equals(c2));
    c2.setOutlinePaint(new GradientPaint(1.0f,2.0f,Color.white,3.0f,4.0f,Color.gray));
    assertTrue(c1.equals(c2));
    c1.setOutlineStroke(new BasicStroke(1.1f));
    assertFalse(c1.equals(c2));
    c2.setOutlineStroke(new BasicStroke(1.1f));
    assertTrue(c1.equals(c2));
    c1.setVisible(false);
    assertFalse(c1.equals(c2));
    c2.setVisible(false);
    assertTrue(c1.equals(c2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    DialCap c1=new DialCap();
    DialCap c2=new DialCap();
    assertTrue(c1.equals(c2));
    int h1=c1.hashCode();
    int h2=c2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    DialCap c1=new DialCap();
    DialCap c2=null;
    try {
      c2=(DialCap)c1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(c1 != c2);
    assertTrue(c1.getClass() == c2.getClass());
    assertTrue(c1.equals(c2));
    c1=new DialCap();
    c1.setFillPaint(new GradientPaint(1.0f,2.0f,Color.blue,3.0f,4.0f,Color.green));
    c1.setOutlinePaint(new GradientPaint(1.0f,2.0f,Color.white,3.0f,4.0f,Color.gray));
    c1.setOutlineStroke(new BasicStroke(2.0f));
    c2=null;
    try {
      c2=(DialCap)c1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(c1 != c2);
    assertTrue(c1.getClass() == c2.getClass());
    assertTrue(c1.equals(c2));
    MyDialLayerChangeListener l1=new MyDialLayerChangeListener();
    c1.addChangeListener(l1);
    assertTrue(c1.hasListener(l1));
    assertFalse(c2.hasListener(l1));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    DialCap c1=new DialCap();
    DialCap c2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(c1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      c2=(DialCap)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(c1,c2);
    c1=new DialCap();
    c1.setFillPaint(new GradientPaint(1.0f,2.0f,Color.blue,3.0f,4.0f,Color.green));
    c1.setOutlinePaint(new GradientPaint(1.0f,2.0f,Color.white,3.0f,4.0f,Color.gray));
    c1.setOutlineStroke(new BasicStroke(2.0f));
    c2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(c1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      c2=(DialCap)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(c1,c2);
  }
}
