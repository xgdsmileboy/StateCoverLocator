package org.jfree.chart.axis.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.axis.NumberTickUnit;
/** 
 * Some tests for the             {@link NumberTickUnit} class.
 */
public class NumberTickUnitTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(NumberTickUnitTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public NumberTickUnitTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    NumberTickUnit t1=new NumberTickUnit(1.23,new DecimalFormat("0.00"));
    NumberTickUnit t2=new NumberTickUnit(1.23,new DecimalFormat("0.00"));
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
    t1=new NumberTickUnit(3.21,new DecimalFormat("0.00"));
    assertFalse(t1.equals(t2));
    t2=new NumberTickUnit(3.21,new DecimalFormat("0.00"));
    assertTrue(t1.equals(t2));
    t1=new NumberTickUnit(3.21,new DecimalFormat("0.000"));
    assertFalse(t1.equals(t2));
    t2=new NumberTickUnit(3.21,new DecimalFormat("0.000"));
    assertTrue(t1.equals(t2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    NumberTickUnit t1=new NumberTickUnit(1.23,new DecimalFormat("0.00"));
    NumberTickUnit t2=new NumberTickUnit(1.23,new DecimalFormat("0.00"));
    int h1=t1.hashCode();
    int h2=t2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * This is an immutable class so it doesn't need to be cloneable.
 */
  public void testCloning(){
    NumberTickUnit t1=new NumberTickUnit(1.23,new DecimalFormat("0.00"));
    assertFalse(t1 instanceof Cloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    NumberTickUnit t1=new NumberTickUnit(1.23,new DecimalFormat("0.00"));
    NumberTickUnit t2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(t1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      t2=(NumberTickUnit)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(t1,t2);
  }
}
