package org.jfree.data.xy.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.xy.XYInterval;
/** 
 * Tests for the             {@link XYInterval} class.
 */
public class XYIntervalTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(XYIntervalTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public XYIntervalTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    XYInterval i1=new XYInterval(1.0,2.0,3.0,2.5,3.5);
    XYInterval i2=new XYInterval(1.0,2.0,3.0,2.5,3.5);
    assertEquals(i1,i2);
    i1=new XYInterval(1.1,2.0,3.0,2.5,3.5);
    assertFalse(i1.equals(i2));
    i2=new XYInterval(1.1,2.0,3.0,2.5,3.5);
    assertTrue(i1.equals(i2));
    i1=new XYInterval(1.1,2.2,3.0,2.5,3.5);
    assertFalse(i1.equals(i2));
    i2=new XYInterval(1.1,2.2,3.0,2.5,3.5);
    assertTrue(i1.equals(i2));
    i1=new XYInterval(1.1,2.2,3.3,2.5,3.5);
    assertFalse(i1.equals(i2));
    i2=new XYInterval(1.1,2.2,3.3,2.5,3.5);
    assertTrue(i1.equals(i2));
    i1=new XYInterval(1.1,2.2,3.3,2.6,3.5);
    assertFalse(i1.equals(i2));
    i2=new XYInterval(1.1,2.2,3.3,2.6,3.5);
    assertTrue(i1.equals(i2));
    i1=new XYInterval(1.1,2.2,3.3,2.6,3.6);
    assertFalse(i1.equals(i2));
    i2=new XYInterval(1.1,2.2,3.3,2.6,3.6);
    assertTrue(i1.equals(i2));
  }
  /** 
 * This class is immutable.
 */
  public void testCloning(){
    XYInterval i1=new XYInterval(1.0,2.0,3.0,2.5,3.5);
    assertFalse(i1 instanceof Cloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    XYInterval i1=new XYInterval(1.0,2.0,3.0,2.5,3.5);
    XYInterval i2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(i1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      i2=(XYInterval)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(i1,i2);
  }
}
