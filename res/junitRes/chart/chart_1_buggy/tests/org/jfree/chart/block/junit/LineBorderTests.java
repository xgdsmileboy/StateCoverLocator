package org.jfree.chart.block.junit;
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
import org.jfree.chart.block.LineBorder;
import org.jfree.chart.util.RectangleInsets;
/** 
 * Tests for the             {@link LineBorder} class.
 */
public class LineBorderTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(LineBorderTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public LineBorderTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals() method can distinguish all the required fields.
 */
  public void testEquals(){
    LineBorder b1=new LineBorder(Color.red,new BasicStroke(1.0f),new RectangleInsets(1.0,1.0,1.0,1.0));
    LineBorder b2=new LineBorder(Color.red,new BasicStroke(1.0f),new RectangleInsets(1.0,1.0,1.0,1.0));
    assertTrue(b1.equals(b2));
    assertTrue(b2.equals(b2));
    b1=new LineBorder(Color.blue,new BasicStroke(1.0f),new RectangleInsets(1.0,1.0,1.0,1.0));
    assertFalse(b1.equals(b2));
    b2=new LineBorder(Color.blue,new BasicStroke(1.0f),new RectangleInsets(1.0,1.0,1.0,1.0));
    assertTrue(b1.equals(b2));
    b1=new LineBorder(Color.blue,new BasicStroke(1.1f),new RectangleInsets(1.0,1.0,1.0,1.0));
    assertFalse(b1.equals(b2));
    b2=new LineBorder(Color.blue,new BasicStroke(1.1f),new RectangleInsets(1.0,1.0,1.0,1.0));
    assertTrue(b1.equals(b2));
    b1=new LineBorder(Color.blue,new BasicStroke(1.1f),new RectangleInsets(1.0,2.0,3.0,4.0));
    assertFalse(b1.equals(b2));
    b2=new LineBorder(Color.blue,new BasicStroke(1.1f),new RectangleInsets(1.0,2.0,3.0,4.0));
    assertTrue(b1.equals(b2));
  }
  /** 
 * Immutable - cloning not necessary.
 */
  public void testCloning(){
    LineBorder b1=new LineBorder();
    assertFalse(b1 instanceof Cloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    LineBorder b1=new LineBorder(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow),new BasicStroke(1.0f),new RectangleInsets(1.0,1.0,1.0,1.0));
    LineBorder b2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(b1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      b2=(LineBorder)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      fail(e.toString());
    }
    assertTrue(b1.equals(b2));
  }
}
