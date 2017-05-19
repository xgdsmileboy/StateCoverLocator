package org.jfree.chart.axis.junit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.axis.CyclicNumberAxis;
/** 
 * Tests for the             {@link CyclicNumberAxis} class.
 */
public class CyclicNumberAxisTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(CyclicNumberAxisTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public CyclicNumberAxisTests(  String name){
    super(name);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    CyclicNumberAxis a1=new CyclicNumberAxis(10,0,"Test");
    CyclicNumberAxis a2=null;
    try {
      a2=(CyclicNumberAxis)a1.clone();
    }
 catch (    CloneNotSupportedException e) {
      System.err.println("Failed to clone.");
    }
    assertTrue(a1 != a2);
    assertTrue(a1.getClass() == a2.getClass());
    assertTrue(a1.equals(a2));
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    CyclicNumberAxis a1=new CyclicNumberAxis(10,0,"Test");
    CyclicNumberAxis a2=new CyclicNumberAxis(10,0,"Test");
    assertTrue(a1.equals(a2));
    a1.setPeriod(5);
    assertFalse(a1.equals(a2));
    a2.setPeriod(5);
    assertTrue(a1.equals(a2));
    a1.setOffset(2.0);
    assertFalse(a1.equals(a2));
    a2.setOffset(2.0);
    assertTrue(a1.equals(a2));
    a1.setAdvanceLinePaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.black));
    assertFalse(a1.equals(a2));
    a2.setAdvanceLinePaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.black));
    assertTrue(a1.equals(a2));
    Stroke stroke=new BasicStroke(0.2f);
    a1.setAdvanceLineStroke(stroke);
    assertFalse(a1.equals(a2));
    a2.setAdvanceLineStroke(stroke);
    assertTrue(a1.equals(a2));
    a1.setAdvanceLineVisible(!a1.isAdvanceLineVisible());
    assertFalse(a1.equals(a2));
    a2.setAdvanceLineVisible(a1.isAdvanceLineVisible());
    assertTrue(a1.equals(a2));
    a1.setBoundMappedToLastCycle(!a1.isBoundMappedToLastCycle());
    assertFalse(a1.equals(a2));
    a2.setBoundMappedToLastCycle(a1.isBoundMappedToLastCycle());
    assertTrue(a1.equals(a2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    CyclicNumberAxis a1=new CyclicNumberAxis(10,0,"Test");
    CyclicNumberAxis a2=new CyclicNumberAxis(10,0,"Test");
    assertTrue(a1.equals(a2));
    int h1=a1.hashCode();
    int h2=a2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    CyclicNumberAxis a1=new CyclicNumberAxis(10,0,"Test Axis");
    CyclicNumberAxis a2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(a1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      a2=(CyclicNumberAxis)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(a1,a2);
  }
}
