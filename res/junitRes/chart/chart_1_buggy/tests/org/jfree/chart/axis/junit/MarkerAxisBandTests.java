package org.jfree.chart.axis.junit;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.axis.MarkerAxisBand;
/** 
 * Tests for the             {@link MarkerAxisBand} class.
 */
public class MarkerAxisBandTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(MarkerAxisBandTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public MarkerAxisBandTests(  String name){
    super(name);
  }
  /** 
 * Test that the equals() method can distinguish all fields.
 */
  public void testEquals(){
    Font font1=new Font("SansSerif",Font.PLAIN,12);
    Font font2=new Font("SansSerif",Font.PLAIN,14);
    MarkerAxisBand a1=new MarkerAxisBand(null,1.0,1.0,1.0,1.0,font1);
    MarkerAxisBand a2=new MarkerAxisBand(null,1.0,1.0,1.0,1.0,font1);
    assertEquals(a1,a2);
    a1=new MarkerAxisBand(null,2.0,1.0,1.0,1.0,font1);
    assertFalse(a1.equals(a2));
    a2=new MarkerAxisBand(null,2.0,1.0,1.0,1.0,font1);
    assertTrue(a1.equals(a2));
    a1=new MarkerAxisBand(null,2.0,3.0,1.0,1.0,font1);
    assertFalse(a1.equals(a2));
    a2=new MarkerAxisBand(null,2.0,3.0,1.0,1.0,font1);
    assertTrue(a1.equals(a2));
    a1=new MarkerAxisBand(null,2.0,3.0,4.0,1.0,font1);
    assertFalse(a1.equals(a2));
    a2=new MarkerAxisBand(null,2.0,3.0,4.0,1.0,font1);
    assertTrue(a1.equals(a2));
    a1=new MarkerAxisBand(null,2.0,3.0,4.0,5.0,font1);
    assertFalse(a1.equals(a2));
    a2=new MarkerAxisBand(null,2.0,3.0,4.0,5.0,font1);
    assertTrue(a1.equals(a2));
    a1=new MarkerAxisBand(null,2.0,3.0,4.0,5.0,font2);
    assertFalse(a1.equals(a2));
    a2=new MarkerAxisBand(null,2.0,3.0,4.0,5.0,font2);
    assertTrue(a1.equals(a2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    Font font1=new Font("SansSerif",Font.PLAIN,12);
    MarkerAxisBand a1=new MarkerAxisBand(null,1.0,1.0,1.0,1.0,font1);
    MarkerAxisBand a2=new MarkerAxisBand(null,1.0,1.0,1.0,1.0,font1);
    assertTrue(a1.equals(a2));
    int h1=a1.hashCode();
    int h2=a2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    MarkerAxisBand a1=new MarkerAxisBand(null,1.0,1.0,1.0,1.0,null);
    MarkerAxisBand a2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(a1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      a2=(MarkerAxisBand)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(a1,a2);
  }
}
