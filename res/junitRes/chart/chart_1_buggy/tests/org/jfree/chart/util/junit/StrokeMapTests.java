package org.jfree.chart.util.junit;
import java.awt.BasicStroke;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.util.StrokeMap;
/** 
 * Some tests for the             {@link StrokeMap} class.
 */
public class StrokeMapTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StrokeMapTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StrokeMapTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the getStroke() method.
 */
  public void testGetStroke(){
    StrokeMap m1=new StrokeMap();
    assertEquals(null,m1.getStroke("A"));
    m1.put("A",new BasicStroke(1.1f));
    assertEquals(new BasicStroke(1.1f),m1.getStroke("A"));
    m1.put("A",null);
    assertEquals(null,m1.getStroke("A"));
    boolean pass=false;
    try {
      m1.getStroke(null);
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
    StrokeMap m1=new StrokeMap();
    m1.put("A",new BasicStroke(1.1f));
    assertEquals(new BasicStroke(1.1f),m1.getStroke("A"));
    boolean pass=false;
    try {
      m1.put(null,new BasicStroke(1.1f));
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
    StrokeMap m1=new StrokeMap();
    StrokeMap m2=new StrokeMap();
    assertTrue(m1.equals(m1));
    assertTrue(m1.equals(m2));
    assertFalse(m1.equals(null));
    assertFalse(m1.equals("ABC"));
    m1.put("K1",new BasicStroke(1.1f));
    assertFalse(m1.equals(m2));
    m2.put("K1",new BasicStroke(1.1f));
    assertTrue(m1.equals(m2));
    m1.put("K2",new BasicStroke(2.2f));
    assertFalse(m1.equals(m2));
    m2.put("K2",new BasicStroke(2.2f));
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
    StrokeMap m1=new StrokeMap();
    StrokeMap m2=null;
    try {
      m2=(StrokeMap)m1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(m1.equals(m2));
    m1.put("K1",new BasicStroke(1.1f));
    m1.put("K2",new BasicStroke(2.2f));
    try {
      m2=(StrokeMap)m1.clone();
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
    StrokeMap m1=new StrokeMap();
    StrokeMap m2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(m1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      m2=(StrokeMap)in.readObject();
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
    StrokeMap m1=new StrokeMap();
    m1.put("K1",new BasicStroke(1.1f));
    m1.put("K2",new BasicStroke(2.2f));
    StrokeMap m2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(m1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      m2=(StrokeMap)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(m1,m2);
  }
}
