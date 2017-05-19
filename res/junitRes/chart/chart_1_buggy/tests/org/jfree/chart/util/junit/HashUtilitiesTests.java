package org.jfree.chart.util.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.util.HashUtilities;
/** 
 * Tests for the             {@link HashUtilities} class.
 */
public class HashUtilitiesTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(HashUtilitiesTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public HashUtilitiesTests(  String name){
    super(name);
  }
  /** 
 * Some sanity checks for the hashCodeForDoubleArray() method.
 */
  public void testHashCodeForDoubleArray(){
    double[] a1=new double[]{1.0};
    double[] a2=new double[]{1.0};
    int h1=HashUtilities.hashCodeForDoubleArray(a1);
    int h2=HashUtilities.hashCodeForDoubleArray(a2);
    assertTrue(h1 == h2);
    double[] a3=new double[]{0.5,1.0};
    int h3=HashUtilities.hashCodeForDoubleArray(a3);
    assertFalse(h1 == h3);
  }
}
