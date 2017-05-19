package org.jfree.chart.needle.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.needle.WindNeedle;
/** 
 * Tests for the             {@link WindNeedle} class.
 */
public class WindNeedleTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(WindNeedleTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public WindNeedleTests(  String name){
    super(name);
  }
  /** 
 * Check that the equals() method can distinguish all fields.
 */
  public void testEquals(){
    WindNeedle n1=new WindNeedle();
    WindNeedle n2=new WindNeedle();
    assertTrue(n1.equals(n2));
    assertTrue(n2.equals(n1));
  }
  /** 
 * Check that cloning works.
 */
  public void testCloning(){
    WindNeedle n1=new WindNeedle();
    WindNeedle n2=null;
    try {
      n2=(WindNeedle)n1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
      System.err.println("Failed to clone.");
    }
    assertTrue(n1 != n2);
    assertTrue(n1.getClass() == n2.getClass());
    assertTrue(n1.equals(n2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    WindNeedle n1=new WindNeedle();
    WindNeedle n2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(n1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      n2=(WindNeedle)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertTrue(n1.equals(n2));
  }
}
