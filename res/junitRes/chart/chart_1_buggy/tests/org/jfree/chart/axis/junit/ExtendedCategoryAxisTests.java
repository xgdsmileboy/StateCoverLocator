package org.jfree.chart.axis.junit;
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
import org.jfree.chart.axis.ExtendedCategoryAxis;
/** 
 * Tests for the             {@link ExtendedCategoryAxis} class.
 */
public class ExtendedCategoryAxisTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(ExtendedCategoryAxisTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public ExtendedCategoryAxisTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    ExtendedCategoryAxis a1=new ExtendedCategoryAxis("Test");
    ExtendedCategoryAxis a2=new ExtendedCategoryAxis("Test");
    assertTrue(a1.equals(a2));
    a1.addSubLabel("C1","C1-sublabel");
    assertFalse(a1.equals(a2));
    a2.addSubLabel("C1","C1-sublabel");
    assertTrue(a1.equals(a2));
    a1.setSubLabelFont(new Font("Dialog",Font.BOLD,8));
    assertFalse(a1.equals(a2));
    a2.setSubLabelFont(new Font("Dialog",Font.BOLD,8));
    assertTrue(a1.equals(a2));
    a1.setSubLabelPaint(Color.red);
    assertFalse(a1.equals(a2));
    a2.setSubLabelPaint(Color.red);
    assertTrue(a1.equals(a2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    ExtendedCategoryAxis a1=new ExtendedCategoryAxis("Test");
    ExtendedCategoryAxis a2=new ExtendedCategoryAxis("Test");
    assertTrue(a1.equals(a2));
    int h1=a1.hashCode();
    int h2=a2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    ExtendedCategoryAxis a1=new ExtendedCategoryAxis("Test");
    ExtendedCategoryAxis a2=null;
    try {
      a2=(ExtendedCategoryAxis)a1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(a1 != a2);
    assertTrue(a1.getClass() == a2.getClass());
    assertTrue(a1.equals(a2));
    a1.addSubLabel("C1","ABC");
    assertFalse(a1.equals(a2));
    a2.addSubLabel("C1","ABC");
    assertTrue(a1.equals(a2));
  }
  /** 
 * Confirm that cloning works.  This test customises the font and paint per category label.
 */
  public void testCloning2(){
    ExtendedCategoryAxis a1=new ExtendedCategoryAxis("Test");
    a1.setTickLabelFont("C1",new Font("Dialog",Font.PLAIN,15));
    a1.setTickLabelPaint("C1",new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.white));
    ExtendedCategoryAxis a2=null;
    try {
      a2=(ExtendedCategoryAxis)a1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(a1 != a2);
    assertTrue(a1.getClass() == a2.getClass());
    assertTrue(a1.equals(a2));
    a1.setTickLabelFont("C1",null);
    assertFalse(a1.equals(a2));
    a2.setTickLabelFont("C1",null);
    assertTrue(a1.equals(a2));
    a1.setTickLabelPaint("C1",Color.yellow);
    assertFalse(a1.equals(a2));
    a2.setTickLabelPaint("C1",Color.yellow);
    assertTrue(a1.equals(a2));
    a1.addCategoryLabelToolTip("C1","XYZ");
    assertFalse(a1.equals(a2));
    a2.addCategoryLabelToolTip("C1","XYZ");
    assertTrue(a1.equals(a2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    ExtendedCategoryAxis a1=new ExtendedCategoryAxis("Test");
    a1.setSubLabelPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    ExtendedCategoryAxis a2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(a1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      a2=(ExtendedCategoryAxis)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(a1,a2);
  }
}
