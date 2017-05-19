package org.jfree.chart.plot.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.plot.PlotOrientation;
/** 
 * Tests for the             {@link PlotOrientation} class.
 */
public class PlotOrientationTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(PlotOrientationTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public PlotOrientationTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the equals() method.
 */
  public void testEquals(){
    assertEquals(PlotOrientation.HORIZONTAL,PlotOrientation.HORIZONTAL);
    assertEquals(PlotOrientation.VERTICAL,PlotOrientation.VERTICAL);
    assertFalse(PlotOrientation.HORIZONTAL.equals(PlotOrientation.VERTICAL));
    assertFalse(PlotOrientation.VERTICAL.equals(PlotOrientation.HORIZONTAL));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    PlotOrientation orientation1=PlotOrientation.HORIZONTAL;
    PlotOrientation orientation2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(orientation1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      orientation2=(PlotOrientation)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(orientation1,orientation2);
    boolean same=orientation1 == orientation2;
    assertEquals(true,same);
  }
}
