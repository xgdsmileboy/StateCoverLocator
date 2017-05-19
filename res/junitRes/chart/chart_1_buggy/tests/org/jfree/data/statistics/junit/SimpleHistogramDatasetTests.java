package org.jfree.data.statistics.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
/** 
 * Tests for the             {@link SimpleHistogramDataset} class.
 */
public class SimpleHistogramDatasetTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(SimpleHistogramDatasetTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public SimpleHistogramDatasetTests(  String name){
    super(name);
  }
  /** 
 * Ensure that the equals() method can distinguish all fields.
 */
  public void testEquals(){
    SimpleHistogramDataset d1=new SimpleHistogramDataset("Dataset 1");
    SimpleHistogramDataset d2=new SimpleHistogramDataset("Dataset 1");
    assertTrue(d1.equals(d2));
    d1.addBin(new SimpleHistogramBin(1.0,2.0));
    assertFalse(d1.equals(d2));
    d2.addBin(new SimpleHistogramBin(1.0,2.0));
    assertTrue(d1.equals(d2));
  }
  /** 
 * Some checks for the clone() method.
 */
  public void testCloning(){
    SimpleHistogramDataset d1=new SimpleHistogramDataset("Dataset 1");
    SimpleHistogramDataset d2=null;
    try {
      d2=(SimpleHistogramDataset)d1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(d1 != d2);
    assertTrue(d1.getClass() == d2.getClass());
    assertTrue(d1.equals(d2));
    d2.addBin(new SimpleHistogramBin(2.0,3.0));
    d2.addObservation(2.3);
    assertFalse(d1.equals(d2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    SimpleHistogramDataset d1=new SimpleHistogramDataset("D1");
    SimpleHistogramDataset d2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(d1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      d2=(SimpleHistogramDataset)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(d1,d2);
  }
  private static final double EPSILON=0.0000000001;
  /** 
 * Some checks for the clearObservations() method.
 */
  public void testClearObservations(){
    SimpleHistogramDataset d1=new SimpleHistogramDataset("D1");
    d1.clearObservations();
    assertEquals(0,d1.getItemCount(0));
    d1.addBin(new SimpleHistogramBin(0.0,1.0));
    d1.addObservation(0.5);
    assertEquals(1.0,d1.getYValue(0,0),EPSILON);
  }
  /** 
 * Some checks for the removeAllBins() method.
 */
  public void testRemoveAllBins(){
    SimpleHistogramDataset d1=new SimpleHistogramDataset("D1");
    d1.addBin(new SimpleHistogramBin(0.0,1.0));
    d1.addObservation(0.5);
    d1.addBin(new SimpleHistogramBin(2.0,3.0));
    assertEquals(2,d1.getItemCount(0));
    d1.removeAllBins();
    assertEquals(0,d1.getItemCount(0));
  }
}
