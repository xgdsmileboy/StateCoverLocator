package org.jfree.data.xy.junit;
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
import org.jfree.data.xy.OHLCDataItem;
/** 
 * Tests for the             {@link OHLCDataItem} class.
 */
public class OHLCDataItemTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(OHLCDataItemTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public OHLCDataItemTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    OHLCDataItem i1=new OHLCDataItem(new Date(1L),1.0,2.0,3.0,4.0,5.0);
    OHLCDataItem i2=new OHLCDataItem(new Date(1L),1.0,2.0,3.0,4.0,5.0);
    assertTrue(i1.equals(i2));
    assertTrue(i2.equals(i1));
  }
  /** 
 * Instances of this class are immutable - cloning not required.
 */
  public void testCloning(){
    OHLCDataItem i1=new OHLCDataItem(new Date(1L),1.0,2.0,3.0,4.0,5.0);
    assertFalse(i1 instanceof Cloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    OHLCDataItem i1=new OHLCDataItem(new Date(1L),1.0,2.0,3.0,4.0,5.0);
    OHLCDataItem i2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(i1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      i2=(OHLCDataItem)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(i1,i2);
  }
}
