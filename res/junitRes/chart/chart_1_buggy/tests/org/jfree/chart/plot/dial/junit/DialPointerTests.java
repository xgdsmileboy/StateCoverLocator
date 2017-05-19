package org.jfree.chart.plot.dial.junit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.plot.dial.DialPointer;
/** 
 * Tests for the             {@link DialPointer} class.
 */
public class DialPointerTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(DialPointerTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public DialPointerTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    DialPointer i1=new DialPointer.Pin(1);
    DialPointer i2=new DialPointer.Pin(1);
    assertTrue(i1.equals(i2));
    i1=new DialPointer.Pin(2);
    assertFalse(i1.equals(i2));
    i2=new DialPointer.Pin(2);
    assertTrue(i1.equals(i2));
    i1.setVisible(false);
    assertFalse(i1.equals(i2));
    i2.setVisible(false);
    assertTrue(i1.equals(i2));
  }
  /** 
 * Check the equals() method for the DialPointer.Pin class.
 */
  public void testEqualsPin(){
    DialPointer.Pin p1=new DialPointer.Pin();
    DialPointer.Pin p2=new DialPointer.Pin();
    assertEquals(p1,p2);
    p1.setPaint(Color.green);
    assertFalse(p1.equals(p2));
    p2.setPaint(Color.green);
    assertTrue(p1.equals(p2));
    BasicStroke s=new BasicStroke(4.4f);
    p1.setStroke(s);
    assertFalse(p1.equals(p2));
    p2.setStroke(s);
    assertTrue(p1.equals(p2));
  }
  /** 
 * Check the equals() method for the DialPointer.Pointer class.
 */
  public void testEqualsPointer(){
    DialPointer.Pointer p1=new DialPointer.Pointer();
    DialPointer.Pointer p2=new DialPointer.Pointer();
    assertEquals(p1,p2);
    p1.setFillPaint(Color.green);
    assertFalse(p1.equals(p2));
    p2.setFillPaint(Color.green);
    assertTrue(p1.equals(p2));
    p1.setOutlinePaint(Color.green);
    assertFalse(p1.equals(p2));
    p2.setOutlinePaint(Color.green);
    assertTrue(p1.equals(p2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    DialPointer i1=new DialPointer.Pin(1);
    DialPointer i2=new DialPointer.Pin(1);
    assertTrue(i1.equals(i2));
    int h1=i1.hashCode();
    int h2=i2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    DialPointer i1=new DialPointer.Pin(1);
    DialPointer i2=null;
    try {
      i2=(DialPointer)i1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(i1 != i2);
    assertTrue(i1.getClass() == i2.getClass());
    assertTrue(i1.equals(i2));
    MyDialLayerChangeListener l1=new MyDialLayerChangeListener();
    i1.addChangeListener(l1);
    assertTrue(i1.hasListener(l1));
    assertFalse(i2.hasListener(l1));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    DialPointer i1=new DialPointer.Pin(1);
    DialPointer i2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(i1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      i2=(DialPointer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(i1,i2);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization2(){
    DialPointer i1=new DialPointer.Pointer(1);
    DialPointer i2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(i1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      i2=(DialPointer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(i1,i2);
  }
}
