package org.jfree.data.function.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.function.PolynomialFunction2D;
/** 
 * Tests for the             {@link PolynomialFunction2D} class.
 */
public class PolynomialFunction2DTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(PolynomialFunction2DTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public PolynomialFunction2DTests(  String name){
    super(name);
  }
  /** 
 * Some tests for the constructor.
 */
  public void testConstructor(){
    PolynomialFunction2D f=new PolynomialFunction2D(new double[]{1.0,2.0});
    assertTrue(Arrays.equals(new double[]{1.0,2.0},f.getCoefficients()));
    boolean pass=false;
    try {
      f=new PolynomialFunction2D(null);
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getCoefficients() method.
 */
  public void testGetCoefficients(){
    PolynomialFunction2D f=new PolynomialFunction2D(new double[]{1.0,2.0});
    double[] c=f.getCoefficients();
    assertTrue(Arrays.equals(new double[]{1.0,2.0},c));
    c[0]=99.9;
    assertTrue(Arrays.equals(new double[]{1.0,2.0},f.getCoefficients()));
  }
  /** 
 * Some checks for the getOrder() method.
 */
  public void testGetOrder(){
    PolynomialFunction2D f=new PolynomialFunction2D(new double[]{1.0,2.0});
    assertEquals(1,f.getOrder());
  }
  /** 
 * For datasets, the equals() method just checks keys and values.
 */
  public void testEquals(){
    PolynomialFunction2D f1=new PolynomialFunction2D(new double[]{1.0,2.0});
    PolynomialFunction2D f2=new PolynomialFunction2D(new double[]{1.0,2.0});
    assertTrue(f1.equals(f2));
    f1=new PolynomialFunction2D(new double[]{2.0,3.0});
    assertFalse(f1.equals(f2));
    f2=new PolynomialFunction2D(new double[]{2.0,3.0});
    assertTrue(f1.equals(f2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    PolynomialFunction2D f1=new PolynomialFunction2D(new double[]{1.0,2.0});
    PolynomialFunction2D f2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(f1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      f2=(PolynomialFunction2D)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(f1,f2);
  }
  /** 
 * Objects that are equal should have the same hash code otherwise FindBugs will tell on us...
 */
  public void testHashCode(){
    PolynomialFunction2D f1=new PolynomialFunction2D(new double[]{1.0,2.0});
    PolynomialFunction2D f2=new PolynomialFunction2D(new double[]{1.0,2.0});
    assertEquals(f1.hashCode(),f2.hashCode());
  }
}
