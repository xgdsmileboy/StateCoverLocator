package org.jfree.data.time.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeriesDataItem;
/** 
 * Tests for the             {@link TimeSeriesDataItem} class.
 */
public class TimeSeriesDataItemTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(TimeSeriesDataItemTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public TimeSeriesDataItemTests(  String name){
    super(name);
  }
  /** 
 * Common test setup.
 */
  protected void setUp(){
  }
  /** 
 * Test that an instance is equal to itself. SourceForge Bug ID: 558850.
 */
  public void testEqualsSelf(){
    TimeSeriesDataItem item=new TimeSeriesDataItem(new Day(23,9,2001),99.7);
    assertTrue(item.equals(item));
  }
  /** 
 * Test the equals() method.
 */
  public void testEquals(){
    TimeSeriesDataItem item1=new TimeSeriesDataItem(new Day(23,9,2001),99.7);
    TimeSeriesDataItem item2=new TimeSeriesDataItem(new Day(23,9,2001),99.7);
    assertTrue(item1.equals(item2));
    assertTrue(item2.equals(item1));
    item1.setValue(new Integer(5));
    assertFalse(item1.equals(item2));
    item2.setValue(new Integer(5));
    assertTrue(item1.equals(item2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    TimeSeriesDataItem item1=new TimeSeriesDataItem(new Day(23,9,2001),99.7);
    TimeSeriesDataItem item2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(item1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      item2=(TimeSeriesDataItem)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(item1,item2);
  }
}
