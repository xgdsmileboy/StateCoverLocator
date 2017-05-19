package org.jfree.chart.plot.dial.junit;
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
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
/** 
 * Tests for the             {@link DialBackground} class.
 */
public class DialBackgroundTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(DialBackgroundTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public DialBackgroundTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    DialBackground b1=new DialBackground();
    DialBackground b2=new DialBackground();
    assertTrue(b1.equals(b2));
    b1.setPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow));
    assertFalse(b1.equals(b2));
    b2.setPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow));
    assertTrue(b1.equals(b2));
    b1.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
    assertFalse(b1.equals(b2));
    b2.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
    assertTrue(b1.equals(b2));
    b1.setVisible(false);
    assertFalse(b1.equals(b2));
    b2.setVisible(false);
    assertTrue(b1.equals(b2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    DialBackground b1=new DialBackground(Color.red);
    DialBackground b2=new DialBackground(Color.red);
    assertTrue(b1.equals(b2));
    int h1=b1.hashCode();
    int h2=b2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    DialBackground b1=new DialBackground();
    DialBackground b2=null;
    try {
      b2=(DialBackground)b1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(b1 != b2);
    assertTrue(b1.getClass() == b2.getClass());
    assertTrue(b1.equals(b2));
    b1=new DialBackground();
    b1.setPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.green));
    b1.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
    b2=null;
    try {
      b2=(DialBackground)b1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(b1 != b2);
    assertTrue(b1.getClass() == b2.getClass());
    assertTrue(b1.equals(b2));
    MyDialLayerChangeListener l1=new MyDialLayerChangeListener();
    b1.addChangeListener(l1);
    assertTrue(b1.hasListener(l1));
    assertFalse(b2.hasListener(l1));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    DialBackground b1=new DialBackground();
    DialBackground b2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(b1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      b2=(DialBackground)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(b1,b2);
    b1=new DialBackground();
    b1.setPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.green));
    b1.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
    b2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(b1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      b2=(DialBackground)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(b1,b2);
  }
}
