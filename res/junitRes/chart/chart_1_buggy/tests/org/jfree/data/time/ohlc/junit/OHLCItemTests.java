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
import org.jfree.data.time.Year;
import org.jfree.data.time.ohlc.OHLCItem;
/** 
 * Tests for the             {@link OHLCItem} class.
 */
public class OHLCItemTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(OHLCItemTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public OHLCItemTests(  String name){
    super(name);
  }
  private static final double EPSILON=0.00000000001;
  /** 
 * Some checks for the constructor.
 */
  public void testConstructor1(){
    OHLCItem item1=new OHLCItem(new Year(2006),2.0,4.0,1.0,3.0);
    assertEquals(new Year(2006),item1.getPeriod());
    assertEquals(2.0,item1.getOpenValue(),EPSILON);
    assertEquals(4.0,item1.getHighValue(),EPSILON);
    assertEquals(1.0,item1.getLowValue(),EPSILON);
    assertEquals(3.0,item1.getCloseValue(),EPSILON);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    OHLCItem item1=new OHLCItem(new Year(2006),2.0,4.0,1.0,3.0);
    OHLCItem item2=new OHLCItem(new Year(2006),2.0,4.0,1.0,3.0);
    assertTrue(item1.equals(item2));
    assertTrue(item2.equals(item1));
    item1=new OHLCItem(new Year(2007),2.0,4.0,1.0,3.0);
    assertFalse(item1.equals(item2));
    item2=new OHLCItem(new Year(2007),2.0,4.0,1.0,3.0);
    assertTrue(item1.equals(item2));
    item1=new OHLCItem(new Year(2007),2.2,4.0,1.0,3.0);
    assertFalse(item1.equals(item2));
    item2=new OHLCItem(new Year(2007),2.2,4.0,1.0,3.0);
    assertTrue(item1.equals(item2));
    item1=new OHLCItem(new Year(2007),2.2,4.4,1.0,3.0);
    assertFalse(item1.equals(item2));
    item2=new OHLCItem(new Year(2007),2.2,4.4,1.0,3.0);
    assertTrue(item1.equals(item2));
    item1=new OHLCItem(new Year(2007),2.2,4.4,1.1,3.0);
    assertFalse(item1.equals(item2));
    item2=new OHLCItem(new Year(2007),2.2,4.4,1.1,3.0);
    assertTrue(item1.equals(item2));
    item1=new OHLCItem(new Year(2007),2.2,4.4,1.1,3.3);
    assertFalse(item1.equals(item2));
    item2=new OHLCItem(new Year(2007),2.2,4.4,1.1,3.3);
    assertTrue(item1.equals(item2));
  }
  /** 
 * Some checks for the clone() method.
 */
  public void testCloning(){
    OHLCItem item1=new OHLCItem(new Year(2006),2.0,4.0,1.0,3.0);
    OHLCItem item2=null;
    try {
      item2=(OHLCItem)item1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(item1 != item2);
    assertTrue(item1.getClass() == item2.getClass());
    assertTrue(item1.equals(item2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    OHLCItem item1=new OHLCItem(new Year(2006),2.0,4.0,1.0,3.0);
    OHLCItem item2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(item1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      item2=(OHLCItem)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(item1,item2);
  }
}
