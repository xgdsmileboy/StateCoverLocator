package org.jfree.data.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.DataUtilities;
import org.jfree.data.DefaultKeyedValues2D;
/** 
 * Some tests for the             {@link DataUtilities} class.
 */
public class DataUtilitiesTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(DataUtilitiesTests.class);
  }
  /** 
 * Tests the createNumberArray2D() method.
 */
  public void testCreateNumberArray2D(){
    double[][] d=new double[2][];
    d[0]=new double[]{1.1,2.2,3.3,4.4};
    d[1]=new double[]{1.1,2.2,3.3,4.4,5.5};
    Number[][] n=DataUtilities.createNumberArray2D(d);
    assertEquals(2,n.length);
    assertEquals(4,n[0].length);
    assertEquals(5,n[1].length);
  }
  private static final double EPSILON=0.000000001;
  /** 
 * Some checks for the calculateColumnTotal() method.
 */
  public void testCalculateColumnTotal(){
    DefaultKeyedValues2D table=new DefaultKeyedValues2D();
    table.addValue(new Double(1.0),"R0","C0");
    table.addValue(new Double(2.0),"R0","C1");
    table.addValue(new Double(3.0),"R1","C0");
    table.addValue(new Double(4.0),"R1","C1");
    assertEquals(4.0,DataUtilities.calculateColumnTotal(table,0),EPSILON);
    assertEquals(6.0,DataUtilities.calculateColumnTotal(table,1),EPSILON);
    table.setValue(null,"R1","C1");
    assertEquals(2.0,DataUtilities.calculateColumnTotal(table,1),EPSILON);
  }
  /** 
 * Some checks for the calculateColumnTotal() method.
 */
  public void testCalculateColumnTotal2(){
    DefaultKeyedValues2D table=new DefaultKeyedValues2D();
    table.addValue(new Double(1.0),"R0","C0");
    table.addValue(new Double(2.0),"R0","C1");
    table.addValue(new Double(3.0),"R1","C0");
    table.addValue(new Double(4.0),"R1","C1");
    assertEquals(4.0,DataUtilities.calculateColumnTotal(table,0,new int[]{0,1}),EPSILON);
    assertEquals(1.0,DataUtilities.calculateColumnTotal(table,0,new int[]{0}),EPSILON);
    assertEquals(3.0,DataUtilities.calculateColumnTotal(table,0,new int[]{1}),EPSILON);
    assertEquals(0.0,DataUtilities.calculateColumnTotal(table,0,new int[]{}),EPSILON);
    assertEquals(6.0,DataUtilities.calculateColumnTotal(table,1,new int[]{0,1}),EPSILON);
    assertEquals(2.0,DataUtilities.calculateColumnTotal(table,1,new int[]{0}),EPSILON);
    assertEquals(4.0,DataUtilities.calculateColumnTotal(table,1,new int[]{1}),EPSILON);
    table.setValue(null,"R1","C1");
    assertEquals(2.0,DataUtilities.calculateColumnTotal(table,1,new int[]{0,1}),EPSILON);
    assertEquals(0.0,DataUtilities.calculateColumnTotal(table,1,new int[]{1}),EPSILON);
  }
  /** 
 * Some checks for the calculateRowTotal() method.
 */
  public void testCalculateRowTotal(){
    DefaultKeyedValues2D table=new DefaultKeyedValues2D();
    table.addValue(new Double(1.0),"R0","C0");
    table.addValue(new Double(2.0),"R0","C1");
    table.addValue(new Double(3.0),"R1","C0");
    table.addValue(new Double(4.0),"R1","C1");
    assertEquals(3.0,DataUtilities.calculateRowTotal(table,0),EPSILON);
    assertEquals(7.0,DataUtilities.calculateRowTotal(table,1),EPSILON);
    table.setValue(null,"R1","C1");
    assertEquals(3.0,DataUtilities.calculateRowTotal(table,1),EPSILON);
  }
  /** 
 * Some checks for the calculateRowTotal() method.
 */
  public void testCalculateRowTotal2(){
    DefaultKeyedValues2D table=new DefaultKeyedValues2D();
    table.addValue(new Double(1.0),"R0","C0");
    table.addValue(new Double(2.0),"R0","C1");
    table.addValue(new Double(3.0),"R1","C0");
    table.addValue(new Double(4.0),"R1","C1");
    assertEquals(3.0,DataUtilities.calculateRowTotal(table,0,new int[]{0,1}),EPSILON);
    assertEquals(1.0,DataUtilities.calculateRowTotal(table,0,new int[]{0}),EPSILON);
    assertEquals(2.0,DataUtilities.calculateRowTotal(table,0,new int[]{1}),EPSILON);
    assertEquals(0.0,DataUtilities.calculateRowTotal(table,0,new int[]{}),EPSILON);
    assertEquals(7.0,DataUtilities.calculateRowTotal(table,1,new int[]{0,1}),EPSILON);
    assertEquals(3.0,DataUtilities.calculateRowTotal(table,1,new int[]{0}),EPSILON);
    assertEquals(4.0,DataUtilities.calculateRowTotal(table,1,new int[]{1}),EPSILON);
    assertEquals(0.0,DataUtilities.calculateRowTotal(table,1,new int[]{}),EPSILON);
    table.setValue(null,"R1","C1");
    assertEquals(3.0,DataUtilities.calculateRowTotal(table,1,new int[]{0,1}),EPSILON);
    assertEquals(0.0,DataUtilities.calculateRowTotal(table,1,new int[]{1}),EPSILON);
  }
  /** 
 * Some tests for the equal(double[][], double[][]) method.
 */
  public void testEqual(){
    assertTrue(DataUtilities.equal(null,null));
    double[][] a=new double[5][];
    double[][] b=new double[5][];
    assertTrue(DataUtilities.equal(a,b));
    a=new double[4][];
    assertFalse(DataUtilities.equal(a,b));
    b=new double[4][];
    assertTrue(DataUtilities.equal(a,b));
    a[0]=new double[6];
    assertFalse(DataUtilities.equal(a,b));
    b[0]=new double[6];
    assertTrue(DataUtilities.equal(a,b));
    a[0][0]=1.0;
    assertFalse(DataUtilities.equal(a,b));
    b[0][0]=1.0;
    assertTrue(DataUtilities.equal(a,b));
    a[0][1]=Double.NaN;
    assertFalse(DataUtilities.equal(a,b));
    b[0][1]=Double.NaN;
    assertTrue(DataUtilities.equal(a,b));
    a[0][2]=Double.NEGATIVE_INFINITY;
    assertFalse(DataUtilities.equal(a,b));
    b[0][2]=Double.NEGATIVE_INFINITY;
    assertTrue(DataUtilities.equal(a,b));
    a[0][3]=Double.POSITIVE_INFINITY;
    assertFalse(DataUtilities.equal(a,b));
    b[0][3]=Double.POSITIVE_INFINITY;
    assertTrue(DataUtilities.equal(a,b));
    a[0][4]=Double.POSITIVE_INFINITY;
    assertFalse(DataUtilities.equal(a,b));
    b[0][4]=Double.NEGATIVE_INFINITY;
    assertFalse(DataUtilities.equal(a,b));
    b[0][4]=Double.POSITIVE_INFINITY;
    assertTrue(DataUtilities.equal(a,b));
  }
  /** 
 * Some tests for the clone() method.
 */
  public void testClone(){
    double[][] a=new double[1][];
    double[][] b=DataUtilities.clone(a);
    assertTrue(DataUtilities.equal(a,b));
    a[0]=new double[]{3.0,4.0};
    assertFalse(DataUtilities.equal(a,b));
    b[0]=new double[]{3.0,4.0};
    assertTrue(DataUtilities.equal(a,b));
    a=new double[2][3];
    a[0][0]=1.23;
    a[1][1]=Double.NaN;
    b=DataUtilities.clone(a);
    assertTrue(DataUtilities.equal(a,b));
    a[0][0]=99.9;
    assertFalse(DataUtilities.equal(a,b));
    b[0][0]=99.9;
    assertTrue(DataUtilities.equal(a,b));
  }
  /** 
 * Runs the test suite using JUnit's text-based runner.
 * @param args  ignored.
 */
  public static void main(  String[] args){
    junit.textui.TestRunner.run(suite());
  }
}
