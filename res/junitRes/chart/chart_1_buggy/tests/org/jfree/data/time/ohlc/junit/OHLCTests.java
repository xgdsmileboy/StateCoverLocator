package org.jfree.data.time.ohlc.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.time.ohlc.OHLC;
/** 
 * Tests for the             {@link OHLC} class.
 */
public class OHLCTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(OHLCTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public OHLCTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    OHLC i1=new OHLC(2.0,4.0,1.0,3.0);
    OHLC i2=new OHLC(2.0,4.0,1.0,3.0);
    assertEquals(i1,i2);
    i1=new OHLC(2.2,4.0,1.0,3.0);
    assertFalse(i1.equals(i2));
    i2=new OHLC(2.2,4.0,1.0,3.0);
    assertTrue(i1.equals(i2));
    i1=new OHLC(2.2,4.4,1.0,3.0);
    assertFalse(i1.equals(i2));
    i2=new OHLC(2.2,4.4,1.0,3.0);
    assertTrue(i1.equals(i2));
    i1=new OHLC(2.2,4.4,1.1,3.0);
    assertFalse(i1.equals(i2));
    i2=new OHLC(2.2,4.4,1.1,3.0);
    assertTrue(i1.equals(i2));
    i1=new OHLC(2.2,4.4,1.1,3.3);
    assertFalse(i1.equals(i2));
    i2=new OHLC(2.2,4.4,1.1,3.3);
    assertTrue(i1.equals(i2));
  }
  /** 
 * This class is immutable.
 */
  public void testCloning(){
    OHLC i1=new OHLC(2.0,4.0,1.0,3.0);
    assertFalse(i1 instanceof Cloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    OHLC i1=new OHLC(2.0,4.0,1.0,3.0);
    OHLC i2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(i1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      i2=(OHLC)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(i1,i2);
  }
}
