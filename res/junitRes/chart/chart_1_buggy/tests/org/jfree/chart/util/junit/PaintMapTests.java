package org.jfree.chart.util.junit;
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
import org.jfree.chart.util.PaintMap;
/** 
 * Some tests for the             {@link PaintMap} class.
 */
public class PaintMapTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(PaintMapTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public PaintMapTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the getPaint() method.
 */
  public void testGetPaint(){
    PaintMap m1=new PaintMap();
    assertEquals(null,m1.getPaint("A"));
    m1.put("A",Color.red);
    assertEquals(Color.red,m1.getPaint("A"));
    m1.put("A",null);
    assertEquals(null,m1.getPaint("A"));
    boolean pass=false;
    try {
      m1.getPaint(null);
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the put() method.
 */
  public void testPut(){
    PaintMap m1=new PaintMap();
    m1.put("A",Color.red);
    assertEquals(Color.red,m1.getPaint("A"));
    boolean pass=false;
    try {
      m1.put(null,Color.blue);
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the equals() method.
 */
  public void testEquals(){
    PaintMap m1=new PaintMap();
    PaintMap m2=new PaintMap();
    assertTrue(m1.equals(m1));
    assertTrue(m1.equals(m2));
    assertFalse(m1.equals(null));
    assertFalse(m1.equals("ABC"));
    m1.put("K1",Color.red);
    assertFalse(m1.equals(m2));
    m2.put("K1",Color.red);
    assertTrue(m1.equals(m2));
    m1.put("K2",new GradientPaint(1.0f,2.0f,Color.green,3.0f,4.0f,Color.yellow));
    assertFalse(m1.equals(m2));
    m2.put("K2",new GradientPaint(1.0f,2.0f,Color.green,3.0f,4.0f,Color.yellow));
    assertTrue(m1.equals(m2));
    m1.put("K2",null);
    assertFalse(m1.equals(m2));
    m2.put("K2",null);
    assertTrue(m1.equals(m2));
  }
  /** 
 * Some checks for cloning.
 */
  public void testCloning(){
    PaintMap m1=new PaintMap();
    PaintMap m2=null;
    try {
      m2=(PaintMap)m1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(m1.equals(m2));
    m1.put("K1",Color.red);
    m1.put("K2",new GradientPaint(1.0f,2.0f,Color.green,3.0f,4.0f,Color.yellow));
    try {
      m2=(PaintMap)m1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(m1.equals(m2));
  }
  /** 
 * A check for serialization.
 */
  public void testSerialization1(){
    PaintMap m1=new PaintMap();
    PaintMap m2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(m1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      m2=(PaintMap)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(m1,m2);
  }
  /** 
 * A check for serialization.
 */
  public void testSerialization2(){
    PaintMap m1=new PaintMap();
    m1.put("K1",Color.red);
    m1.put("K2",new GradientPaint(1.0f,2.0f,Color.green,3.0f,4.0f,Color.yellow));
    PaintMap m2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(m1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      m2=(PaintMap)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(m1,m2);
  }
  /** 
 * This test covers a bug reported in the forum: http://www.jfree.org/phpBB2/viewtopic.php?t=19980
 */
  public void testKeysOfDifferentClasses(){
    PaintMap m=new PaintMap();
    m.put("ABC",Color.red);
    m.put(new Integer(99),Color.blue);
    assertEquals(Color.blue,m.getPaint(new Integer(99)));
  }
}
