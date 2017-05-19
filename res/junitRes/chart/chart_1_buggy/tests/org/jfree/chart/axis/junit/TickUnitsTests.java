package org.jfree.chart.axis.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnits;
/** 
 * Tests for the             {@link TickUnits} class.
 */
public class TickUnitsTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(TickUnitsTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public TickUnitsTests(  String name){
    super(name);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    TickUnits t1=(TickUnits)NumberAxis.createIntegerTickUnits();
    TickUnits t2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(t1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      t2=(TickUnits)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(t1,t2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    TickUnits t1=(TickUnits)NumberAxis.createIntegerTickUnits();
    TickUnits t2=null;
    try {
      t2=(TickUnits)t1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(t1 != t2);
    assertTrue(t1.getClass() == t2.getClass());
    assertTrue(t1.equals(t2));
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    TickUnits t1=(TickUnits)NumberAxis.createIntegerTickUnits();
    TickUnits t2=(TickUnits)NumberAxis.createIntegerTickUnits();
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
  }
}
