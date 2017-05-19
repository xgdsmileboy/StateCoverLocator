package org.jfree.chart.util.junit;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.util.PaintList;
/** 
 * Some tests for the             {@link PaintList} class.
 */
public class PaintListTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(PaintListTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public PaintListTests(  String name){
    super(name);
  }
  /** 
 * Tests the equals() method.
 */
  public void testEquals(){
    PaintList l1=new PaintList();
    l1.setPaint(0,Color.red);
    l1.setPaint(1,Color.blue);
    l1.setPaint(2,null);
    PaintList l2=new PaintList();
    l2.setPaint(0,Color.red);
    l2.setPaint(1,Color.blue);
    l2.setPaint(2,null);
    assertTrue(l1.equals(l2));
    assertTrue(l2.equals(l2));
  }
  /** 
 * Tests the equals method.
 */
  public void testEquals2(){
    PaintList l1=new PaintList();
    Color color1=new Color(200,200,200);
    l1.setPaint(0,color1);
    PaintList l2=new PaintList();
    Color color2=new Color(200,200,200);
    l2.setPaint(0,color2);
    assertEquals(l1,l2);
  }
  /** 
 * Tests the equals() method when the list contains a GradientPaint instance.
 */
  public void testEquals3(){
    PaintList l1=new PaintList();
    Paint p1=new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue);
    l1.setPaint(0,p1);
    PaintList l2=new PaintList();
    Paint p2=new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue);
    l2.setPaint(0,p2);
    assertEquals(l1,l2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    PaintList l1=new PaintList();
    l1.setPaint(0,Color.red);
    l1.setPaint(1,Color.blue);
    l1.setPaint(2,null);
    PaintList l2=null;
    try {
      l2=(PaintList)l1.clone();
    }
 catch (    CloneNotSupportedException e) {
      System.err.println("PaintListTests.testCloning: failed to clone.");
    }
    assertTrue(l1 != l2);
    assertTrue(l1.getClass() == l2.getClass());
    assertTrue(l1.equals(l2));
    l2.setPaint(0,Color.green);
    assertFalse(l1.equals(l2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    PaintList l1=new PaintList();
    l1.setPaint(0,Color.red);
    l1.setPaint(1,Color.blue);
    l1.setPaint(2,null);
    PaintList l2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(l1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      l2=(PaintList)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(l1,l2);
  }
  /** 
 * Some checks for the testHashCode() method.
 */
  public void testHashCode(){
    PaintList p1=new PaintList();
    PaintList p2=new PaintList();
    assertTrue(p1.hashCode() == p2.hashCode());
    p1.setPaint(0,Color.red);
    assertFalse(p1.hashCode() == p2.hashCode());
    p2.setPaint(0,Color.red);
    assertTrue(p1.hashCode() == p2.hashCode());
    p1.setPaint(1,new GradientPaint(1.0f,2.0f,Color.RED,3.0f,4.0f,Color.GREEN));
    assertFalse(p1.hashCode() == p2.hashCode());
    p2.setPaint(1,new GradientPaint(1.0f,2.0f,Color.RED,3.0f,4.0f,Color.GREEN));
    assertTrue(p1.hashCode() == p2.hashCode());
  }
}
