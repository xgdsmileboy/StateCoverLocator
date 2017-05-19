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
import org.jfree.data.time.TimePeriodValue;
/** 
 * Tests for the             {@link TimePeriodValue} class.
 */
public class TimePeriodValueTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(TimePeriodValueTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public TimePeriodValueTests(  String name){
    super(name);
  }
  /** 
 * Common test setup.
 */
  protected void setUp(){
  }
  /** 
 * Test that an instance is equal to itself.
 */
  public void testEqualsSelf(){
    TimePeriodValue tpv=new TimePeriodValue(new Day(),55.75);
    assertTrue(tpv.equals(tpv));
  }
  /** 
 * Tests the equals() method.
 */
  public void testEquals(){
    TimePeriodValue tpv1=new TimePeriodValue(new Day(30,7,2003),55.75);
    TimePeriodValue tpv2=new TimePeriodValue(new Day(30,7,2003),55.75);
    assertTrue(tpv1.equals(tpv2));
    assertTrue(tpv2.equals(tpv1));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    TimePeriodValue tpv1=new TimePeriodValue(new Day(30,7,2003),55.75);
    TimePeriodValue tpv2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(tpv1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      tpv2=(TimePeriodValue)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(tpv1,tpv2);
  }
}
