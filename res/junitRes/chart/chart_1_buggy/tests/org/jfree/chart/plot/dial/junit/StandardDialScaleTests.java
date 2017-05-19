package org.jfree.chart.plot.dial.junit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
import org.jfree.chart.plot.dial.StandardDialScale;
/** 
 * Tests for the             {@link StandardDialScale} class.
 */
public class StandardDialScaleTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StandardDialScaleTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StandardDialScaleTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    StandardDialScale s1=new StandardDialScale();
    StandardDialScale s2=new StandardDialScale();
    assertTrue(s1.equals(s2));
    s1=new StandardDialScale(10.0,100.0,0.0,270.0,10.0,4);
    assertFalse(s1.equals(s2));
    s2=new StandardDialScale(10.0,100.0,0.0,270.0,10.0,4);
    assertTrue(s1.equals(s2));
    s1=new StandardDialScale(10.0,200.0,0.0,270.0,10.0,4);
    assertFalse(s1.equals(s2));
    s2=new StandardDialScale(10.0,200.0,0.0,270.0,10.0,4);
    assertTrue(s1.equals(s2));
    s1=new StandardDialScale(10.0,200.0,20.0,270.0,10.0,4);
    assertFalse(s1.equals(s2));
    s2=new StandardDialScale(10.0,200.0,20.0,270.0,10.0,4);
    assertTrue(s1.equals(s2));
    s1=new StandardDialScale(10.0,200.0,20.0,99.0,10.0,4);
    assertFalse(s1.equals(s2));
    s2=new StandardDialScale(10.0,200.0,20.0,99.0,10.0,4);
    assertTrue(s1.equals(s2));
    s1.setTickRadius(0.99);
    assertFalse(s1.equals(s2));
    s2.setTickRadius(0.99);
    assertTrue(s1.equals(s2));
    s1.setMajorTickIncrement(11.1);
    assertFalse(s1.equals(s2));
    s2.setMajorTickIncrement(11.1);
    assertTrue(s1.equals(s2));
    s1.setMajorTickLength(0.09);
    assertFalse(s1.equals(s2));
    s2.setMajorTickLength(0.09);
    assertTrue(s1.equals(s2));
    s1.setMajorTickPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow));
    assertFalse(s1.equals(s2));
    s2.setMajorTickPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow));
    assertTrue(s1.equals(s2));
    s1.setMajorTickStroke(new BasicStroke(1.1f));
    assertFalse(s1.equals(s2));
    s2.setMajorTickStroke(new BasicStroke(1.1f));
    assertTrue(s1.equals(s2));
    s1.setMinorTickCount(7);
    assertFalse(s1.equals(s2));
    s2.setMinorTickCount(7);
    assertTrue(s1.equals(s2));
    s1.setMinorTickLength(0.09);
    assertFalse(s1.equals(s2));
    s2.setMinorTickLength(0.09);
    assertTrue(s1.equals(s2));
    s1.setTickLabelOffset(0.11);
    assertFalse(s1.equals(s2));
    s2.setTickLabelOffset(0.11);
    assertTrue(s1.equals(s2));
    s1.setTickLabelFont(new Font("Dialog",Font.PLAIN,15));
    assertFalse(s1.equals(s2));
    s2.setTickLabelFont(new Font("Dialog",Font.PLAIN,15));
    assertTrue(s1.equals(s2));
    s1.setTickLabelPaint(new GradientPaint(1.0f,2.0f,Color.white,3.0f,4.0f,Color.green));
    assertFalse(s1.equals(s2));
    s2.setTickLabelPaint(new GradientPaint(1.0f,2.0f,Color.white,3.0f,4.0f,Color.green));
    assertTrue(s1.equals(s2));
    s1.setTickLabelsVisible(false);
    assertFalse(s1.equals(s2));
    s2.setTickLabelsVisible(false);
    assertTrue(s1.equals(s2));
    s1.setVisible(false);
    assertFalse(s1.equals(s2));
    s2.setVisible(false);
    assertTrue(s1.equals(s2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    StandardDialScale s1=new StandardDialScale();
    StandardDialScale s2=new StandardDialScale();
    assertTrue(s1.equals(s2));
    int h1=s1.hashCode();
    int h2=s2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    StandardDialScale s1=new StandardDialScale();
    StandardDialScale s2=null;
    try {
      s2=(StandardDialScale)s1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(s1 != s2);
    assertTrue(s1.getClass() == s2.getClass());
    assertTrue(s1.equals(s2));
    s1=new StandardDialScale();
    s1.setExtent(123.4);
    s1.setMajorTickPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.white));
    s1.setMajorTickStroke(new BasicStroke(2.0f));
    s2=null;
    try {
      s2=(StandardDialScale)s1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(s1 != s2);
    assertTrue(s1.getClass() == s2.getClass());
    assertTrue(s1.equals(s2));
    MyDialLayerChangeListener l1=new MyDialLayerChangeListener();
    s1.addChangeListener(l1);
    assertTrue(s1.hasListener(l1));
    assertFalse(s2.hasListener(l1));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    StandardDialScale s1=new StandardDialScale();
    StandardDialScale s2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(s1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      s2=(StandardDialScale)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(s1,s2);
    s1=new StandardDialScale();
    s1.setExtent(123.4);
    s1.setMajorTickPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.white));
    s1.setMajorTickStroke(new BasicStroke(2.0f));
    s2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(s1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      s2=(StandardDialScale)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(s1,s2);
  }
}
