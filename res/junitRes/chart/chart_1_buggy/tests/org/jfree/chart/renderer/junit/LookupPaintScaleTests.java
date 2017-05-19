package org.jfree.chart.renderer.junit;
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
import org.jfree.chart.renderer.LookupPaintScale;
/** 
 * Tests for the             {@link LookupPaintScale} class.
 */
public class LookupPaintScaleTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(LookupPaintScaleTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public LookupPaintScaleTests(  String name){
    super(name);
  }
  /** 
 * A test for the equals() method.
 */
  public void testEquals(){
    LookupPaintScale g1=new LookupPaintScale();
    LookupPaintScale g2=new LookupPaintScale();
    assertTrue(g1.equals(g2));
    assertTrue(g2.equals(g1));
    g1=new LookupPaintScale(1.0,2.0,Color.red);
    assertFalse(g1.equals(g2));
    g2=new LookupPaintScale(1.0,2.0,Color.red);
    assertTrue(g1.equals(g2));
    g1.add(1.5,new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    assertFalse(g1.equals(g2));
    g2.add(1.5,new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    assertTrue(g1.equals(g2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    LookupPaintScale g1=new LookupPaintScale();
    LookupPaintScale g2=null;
    try {
      g2=(LookupPaintScale)g1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(g1 != g2);
    assertTrue(g1.getClass() == g2.getClass());
    assertTrue(g1.equals(g2));
    g1.add(0.5,Color.red);
    assertFalse(g1.equals(g2));
    g2.add(0.5,Color.red);
    assertTrue(g1.equals(g2));
    g1=new LookupPaintScale(1.0,2.0,new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.green));
    g1.add(1.5,new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    g2=null;
    try {
      g2=(LookupPaintScale)g1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(g1 != g2);
    assertTrue(g1.getClass() == g2.getClass());
    assertTrue(g1.equals(g2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    LookupPaintScale g1=new LookupPaintScale();
    LookupPaintScale g2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(g1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      g2=(LookupPaintScale)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(g1,g2);
    g1=new LookupPaintScale(1.0,2.0,new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow));
    g1.add(1.5,new GradientPaint(1.1f,2.2f,Color.red,3.3f,4.4f,Color.blue));
    g2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(g1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      g2=(LookupPaintScale)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(g1,g2);
  }
  private static final double EPSILON=0.0000000001;
  /** 
 * Some checks for the default constructor.
 */
  public void testConstructor1(){
    LookupPaintScale s=new LookupPaintScale();
    assertEquals(0.0,s.getLowerBound(),EPSILON);
    assertEquals(1.0,s.getUpperBound(),EPSILON);
  }
  /** 
 * Some checks for the other constructor.
 */
  public void testConstructor2(){
    LookupPaintScale s=new LookupPaintScale(1.0,2.0,Color.red);
    assertEquals(1.0,s.getLowerBound(),EPSILON);
    assertEquals(2.0,s.getUpperBound(),EPSILON);
    assertEquals(Color.red,s.getDefaultPaint());
  }
  /** 
 * Some general checks for the lookup table.
 */
  public void testGeneral(){
    LookupPaintScale s=new LookupPaintScale(0.0,100.0,Color.black);
    assertEquals(Color.black,s.getPaint(-1.0));
    assertEquals(Color.black,s.getPaint(0.0));
    assertEquals(Color.black,s.getPaint(50.0));
    assertEquals(Color.black,s.getPaint(100.0));
    assertEquals(Color.black,s.getPaint(101.0));
    s.add(50.0,Color.blue);
    assertEquals(Color.black,s.getPaint(-1.0));
    assertEquals(Color.black,s.getPaint(0.0));
    assertEquals(Color.blue,s.getPaint(50.0));
    assertEquals(Color.blue,s.getPaint(100.0));
    assertEquals(Color.black,s.getPaint(101.0));
    s.add(50.0,Color.red);
    assertEquals(Color.black,s.getPaint(-1.0));
    assertEquals(Color.black,s.getPaint(0.0));
    assertEquals(Color.red,s.getPaint(50.0));
    assertEquals(Color.red,s.getPaint(100.0));
    assertEquals(Color.black,s.getPaint(101.0));
    s.add(25.0,Color.green);
    assertEquals(Color.black,s.getPaint(-1.0));
    assertEquals(Color.black,s.getPaint(0.0));
    assertEquals(Color.green,s.getPaint(25.0));
    assertEquals(Color.red,s.getPaint(50.0));
    assertEquals(Color.red,s.getPaint(100.0));
    assertEquals(Color.black,s.getPaint(101.0));
    s.add(75.0,Color.yellow);
    assertEquals(Color.black,s.getPaint(-1.0));
    assertEquals(Color.black,s.getPaint(0.0));
    assertEquals(Color.green,s.getPaint(25.0));
    assertEquals(Color.red,s.getPaint(50.0));
    assertEquals(Color.yellow,s.getPaint(75.0));
    assertEquals(Color.yellow,s.getPaint(100.0));
    assertEquals(Color.black,s.getPaint(101.0));
  }
}
