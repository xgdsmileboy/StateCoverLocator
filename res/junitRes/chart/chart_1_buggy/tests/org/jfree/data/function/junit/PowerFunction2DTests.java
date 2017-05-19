package org.jfree.data.function.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.function.PowerFunction2D;
/** 
 * Tests for the             {@link PowerFunction2D} class.
 */
public class PowerFunction2DTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(PowerFunction2DTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public PowerFunction2DTests(  String name){
    super(name);
  }
  private static final double EPSILON=0.000000001;
  /** 
 * Some tests for the constructor.
 */
  public void testConstructor(){
    PowerFunction2D f=new PowerFunction2D(1.0,2.0);
    assertEquals(1.0,f.getA(),EPSILON);
    assertEquals(2.0,f.getB(),EPSILON);
  }
  /** 
 * For datasets, the equals() method just checks keys and values.
 */
  public void testEquals(){
    PowerFunction2D f1=new PowerFunction2D(1.0,2.0);
    PowerFunction2D f2=new PowerFunction2D(1.0,2.0);
    assertTrue(f1.equals(f2));
    f1=new PowerFunction2D(2.0,3.0);
    assertFalse(f1.equals(f2));
    f2=new PowerFunction2D(2.0,3.0);
    assertTrue(f1.equals(f2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    PowerFunction2D f1=new PowerFunction2D(1.0,2.0);
    PowerFunction2D f2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(f1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      f2=(PowerFunction2D)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(f1,f2);
  }
  /** 
 * Objects that are equal should have the same hash code otherwise FindBugs will tell on us...
 */
  public void testHashCode(){
    PowerFunction2D f1=new PowerFunction2D(1.0,2.0);
    PowerFunction2D f2=new PowerFunction2D(1.0,2.0);
    assertEquals(f1.hashCode(),f2.hashCode());
  }
}
