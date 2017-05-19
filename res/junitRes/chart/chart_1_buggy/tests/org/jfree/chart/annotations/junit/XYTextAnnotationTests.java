package org.jfree.chart.annotations.junit;
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
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link XYTextAnnotation} class.
 */
public class XYTextAnnotationTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(XYTextAnnotationTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public XYTextAnnotationTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    XYTextAnnotation a1=new XYTextAnnotation("Text",10.0,20.0);
    XYTextAnnotation a2=new XYTextAnnotation("Text",10.0,20.0);
    assertTrue(a1.equals(a2));
    a1=new XYTextAnnotation("ABC",10.0,20.0);
    assertFalse(a1.equals(a2));
    a2=new XYTextAnnotation("ABC",10.0,20.0);
    assertTrue(a1.equals(a2));
    a1=new XYTextAnnotation("ABC",11.0,20.0);
    assertFalse(a1.equals(a2));
    a2=new XYTextAnnotation("ABC",11.0,20.0);
    assertTrue(a1.equals(a2));
    a1=new XYTextAnnotation("ABC",11.0,22.0);
    assertFalse(a1.equals(a2));
    a2=new XYTextAnnotation("ABC",11.0,22.0);
    assertTrue(a1.equals(a2));
    a1.setFont(new Font("Serif",Font.PLAIN,23));
    assertFalse(a1.equals(a2));
    a2.setFont(new Font("Serif",Font.PLAIN,23));
    assertTrue(a1.equals(a2));
    GradientPaint gp1=new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow);
    GradientPaint gp2=new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow);
    a1.setPaint(gp1);
    assertFalse(a1.equals(a2));
    a2.setPaint(gp2);
    assertTrue(a1.equals(a2));
    a1.setRotationAnchor(TextAnchor.BASELINE_RIGHT);
    assertFalse(a1.equals(a2));
    a2.setRotationAnchor(TextAnchor.BASELINE_RIGHT);
    assertTrue(a1.equals(a2));
    a1.setRotationAngle(12.3);
    assertFalse(a1.equals(a2));
    a2.setRotationAngle(12.3);
    assertTrue(a1.equals(a2));
    a1.setTextAnchor(TextAnchor.BASELINE_RIGHT);
    assertFalse(a1.equals(a2));
    a2.setTextAnchor(TextAnchor.BASELINE_RIGHT);
    assertTrue(a1.equals(a2));
    a1.setBackgroundPaint(gp1);
    assertFalse(a1.equals(a2));
    a2.setBackgroundPaint(gp1);
    assertTrue(a1.equals(a2));
    a1.setOutlinePaint(gp1);
    assertFalse(a1.equals(a2));
    a2.setOutlinePaint(gp1);
    assertTrue(a1.equals(a2));
    a1.setOutlineStroke(new BasicStroke(1.2f));
    assertFalse(a1.equals(a2));
    a2.setOutlineStroke(new BasicStroke(1.2f));
    assertTrue(a1.equals(a2));
    a1.setOutlineVisible(!a1.isOutlineVisible());
    assertFalse(a1.equals(a2));
    a2.setOutlineVisible(a1.isOutlineVisible());
    assertTrue(a1.equals(a2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    XYTextAnnotation a1=new XYTextAnnotation("Text",10.0,20.0);
    XYTextAnnotation a2=new XYTextAnnotation("Text",10.0,20.0);
    assertTrue(a1.equals(a2));
    int h1=a1.hashCode();
    int h2=a2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    XYTextAnnotation a1=new XYTextAnnotation("Text",10.0,20.0);
    XYTextAnnotation a2=null;
    try {
      a2=(XYTextAnnotation)a1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(a1 != a2);
    assertTrue(a1.getClass() == a2.getClass());
    assertTrue(a1.equals(a2));
  }
  /** 
 * Checks that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    XYTextAnnotation a1=new XYTextAnnotation("Text",10.0,20.0);
    assertTrue(a1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    XYTextAnnotation a1=new XYTextAnnotation("Text",10.0,20.0);
    a1.setOutlinePaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    XYTextAnnotation a2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(a1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      a2=(XYTextAnnotation)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(a1,a2);
  }
}
