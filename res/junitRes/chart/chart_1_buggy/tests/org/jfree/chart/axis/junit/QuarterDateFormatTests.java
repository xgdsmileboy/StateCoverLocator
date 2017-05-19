package org.jfree.chart.axis.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.TimeZone;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.axis.QuarterDateFormat;
/** 
 * Tests for the             {@link QuarterDateFormat} class.
 */
public class QuarterDateFormatTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(QuarterDateFormatTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public QuarterDateFormatTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    QuarterDateFormat qf1=new QuarterDateFormat(TimeZone.getTimeZone("GMT"),new String[]{"1","2","3","4"});
    QuarterDateFormat qf2=new QuarterDateFormat(TimeZone.getTimeZone("GMT"),new String[]{"1","2","3","4"});
    assertTrue(qf1.equals(qf2));
    assertTrue(qf2.equals(qf1));
    qf1=new QuarterDateFormat(TimeZone.getTimeZone("PST"),new String[]{"1","2","3","4"});
    assertFalse(qf1.equals(qf2));
    qf2=new QuarterDateFormat(TimeZone.getTimeZone("PST"),new String[]{"1","2","3","4"});
    assertTrue(qf1.equals(qf2));
    qf1=new QuarterDateFormat(TimeZone.getTimeZone("PST"),new String[]{"A","2","3","4"});
    assertFalse(qf1.equals(qf2));
    qf2=new QuarterDateFormat(TimeZone.getTimeZone("PST"),new String[]{"A","2","3","4"});
    assertTrue(qf1.equals(qf2));
    qf1=new QuarterDateFormat(TimeZone.getTimeZone("PST"),new String[]{"A","2","3","4"},true);
    assertFalse(qf1.equals(qf2));
    qf2=new QuarterDateFormat(TimeZone.getTimeZone("PST"),new String[]{"A","2","3","4"},true);
    assertTrue(qf1.equals(qf2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    QuarterDateFormat qf1=new QuarterDateFormat(TimeZone.getTimeZone("GMT"),new String[]{"1","2","3","4"});
    QuarterDateFormat qf2=new QuarterDateFormat(TimeZone.getTimeZone("GMT"),new String[]{"1","2","3","4"});
    assertTrue(qf1.equals(qf2));
    int h1=qf1.hashCode();
    int h2=qf2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    QuarterDateFormat qf1=new QuarterDateFormat(TimeZone.getTimeZone("GMT"),new String[]{"1","2","3","4"});
    QuarterDateFormat qf2=null;
    qf2=(QuarterDateFormat)qf1.clone();
    assertTrue(qf1 != qf2);
    assertTrue(qf1.getClass() == qf2.getClass());
    assertTrue(qf1.equals(qf2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    QuarterDateFormat qf1=new QuarterDateFormat(TimeZone.getTimeZone("GMT"),new String[]{"1","2","3","4"});
    QuarterDateFormat qf2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(qf1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      qf2=(QuarterDateFormat)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      fail(e.toString());
    }
    assertTrue(qf1.equals(qf2));
  }
}
