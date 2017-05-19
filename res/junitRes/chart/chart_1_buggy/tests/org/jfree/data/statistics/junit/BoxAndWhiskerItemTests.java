package org.jfree.data.statistics.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.statistics.BoxAndWhiskerItem;
/** 
 * Tests for the             {@link BoxAndWhiskerItem} class.
 */
public class BoxAndWhiskerItemTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(BoxAndWhiskerItemTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public BoxAndWhiskerItemTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    BoxAndWhiskerItem i1=new BoxAndWhiskerItem(new Double(1.0),new Double(2.0),new Double(3.0),new Double(4.0),new Double(5.0),new Double(6.0),new Double(7.0),new Double(8.0),new ArrayList());
    BoxAndWhiskerItem i2=new BoxAndWhiskerItem(new Double(1.0),new Double(2.0),new Double(3.0),new Double(4.0),new Double(5.0),new Double(6.0),new Double(7.0),new Double(8.0),new ArrayList());
    assertTrue(i1.equals(i2));
    assertTrue(i2.equals(i1));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    BoxAndWhiskerItem i1=new BoxAndWhiskerItem(new Double(1.0),new Double(2.0),new Double(3.0),new Double(4.0),new Double(5.0),new Double(6.0),new Double(7.0),new Double(8.0),new ArrayList());
    BoxAndWhiskerItem i2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(i1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      i2=(BoxAndWhiskerItem)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(i1,i2);
  }
}
