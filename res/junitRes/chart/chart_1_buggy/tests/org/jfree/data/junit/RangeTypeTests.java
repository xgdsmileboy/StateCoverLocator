package org.jfree.data.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.RangeType;
/** 
 * Tests for the             {@link RangeType} class.
 */
public class RangeTypeTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(RangeTypeTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public RangeTypeTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the equals() method.
 */
  public void testEquals(){
    assertEquals(RangeType.FULL,RangeType.FULL);
    assertEquals(RangeType.NEGATIVE,RangeType.NEGATIVE);
    assertEquals(RangeType.POSITIVE,RangeType.POSITIVE);
    assertFalse(RangeType.FULL.equals(RangeType.NEGATIVE));
    assertFalse(RangeType.FULL.equals(RangeType.POSITIVE));
    assertFalse(RangeType.FULL.equals(null));
    assertFalse(RangeType.NEGATIVE.equals(RangeType.FULL));
    assertFalse(RangeType.NEGATIVE.equals(RangeType.POSITIVE));
    assertFalse(RangeType.NEGATIVE.equals(null));
    assertFalse(RangeType.POSITIVE.equals(RangeType.NEGATIVE));
    assertFalse(RangeType.POSITIVE.equals(RangeType.FULL));
    assertFalse(RangeType.POSITIVE.equals(null));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    RangeType r1=RangeType.FULL;
    RangeType r2=RangeType.FULL;
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    RangeType r1=RangeType.FULL;
    RangeType r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(RangeType)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(r1,r2);
    boolean same=r1 == r2;
    assertEquals(true,same);
  }
}
