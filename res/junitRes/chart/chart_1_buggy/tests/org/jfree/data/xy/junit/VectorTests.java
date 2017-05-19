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
import org.jfree.data.xy.Vector;
/** 
 * Tests for the             {@link Vector} class.
 */
public class VectorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(VectorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public VectorTests(  String name){
    super(name);
  }
  /** 
 * Test that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    Vector v1=new Vector(1.0,2.0);
    Vector v2=new Vector(1.0,2.0);
    assertTrue(v1.equals(v2));
    assertTrue(v2.equals(v1));
    v1=new Vector(1.1,2.0);
    assertFalse(v1.equals(v2));
    v2=new Vector(1.1,2.0);
    assertTrue(v1.equals(v2));
    v1=new Vector(1.1,2.2);
    assertFalse(v1.equals(v2));
    v2=new Vector(1.1,2.2);
    assertTrue(v1.equals(v2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    Vector v1=new Vector(1.0,2.0);
    Vector v2=new Vector(1.0,2.0);
    assertTrue(v1.equals(v2));
    int h1=v1.hashCode();
    int h2=v2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Immutable class is not cloneable.
 */
  public void testCloning(){
    Vector v1=new Vector(1.0,2.0);
    assertFalse(v1 instanceof Cloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    Vector v1=new Vector(1.0,2.0);
    Vector v2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(v1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      v2=(Vector)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(v1,v2);
  }
}
