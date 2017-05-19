package org.jfree.data.time.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.time.FixedMillisecond;
/** 
 * Tests for the             {@link FixedMillisecond} class.
 */
public class FixedMillisecondTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(FixedMillisecondTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public FixedMillisecondTests(  String name){
    super(name);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    FixedMillisecond m1=new FixedMillisecond();
    FixedMillisecond m2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(m1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      m2=(FixedMillisecond)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(m1,m2);
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    FixedMillisecond m1=new FixedMillisecond(500000L);
    FixedMillisecond m2=new FixedMillisecond(500000L);
    assertTrue(m1.equals(m2));
    int h1=m1.hashCode();
    int h2=m2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * The             {@link FixedMillisecond} class is immutable, so should not be{@link Cloneable}.
 */
  public void testNotCloneable(){
    FixedMillisecond m=new FixedMillisecond(500000L);
    assertFalse(m instanceof Cloneable);
  }
  /** 
 * A check for immutability.
 */
  public void testImmutability(){
    Date d=new Date(20L);
    FixedMillisecond fm=new FixedMillisecond(d);
    d.setTime(22L);
    assertEquals(20L,fm.getFirstMillisecond());
  }
}
