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
import org.jfree.data.Range;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
/** 
 * Tests for the             {@link DefaultStatisticalCategoryDataset} class.
 */
public class DefaultStatisticalCategoryDatasetTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(DefaultStatisticalCategoryDatasetTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public DefaultStatisticalCategoryDatasetTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the getRangeBounds() method.
 */
  public void testGetRangeBounds(){
    DefaultStatisticalCategoryDataset d=new DefaultStatisticalCategoryDataset();
    assertNull(d.getRangeBounds(true));
    d.add(4.5,1.0,"R1","C1");
    assertEquals(new Range(4.5,4.5),d.getRangeBounds(false));
    assertEquals(new Range(3.5,5.5),d.getRangeBounds(true));
    d.add(0.5,2.0,"R1","C2");
    assertEquals(new Range(0.5,4.5),d.getRangeBounds(false));
    assertEquals(new Range(-1.5,5.5),d.getRangeBounds(true));
    d.add(Double.NaN,0.0,"R1","C3");
    assertEquals(new Range(0.5,4.5),d.getRangeBounds(false));
    assertEquals(new Range(-1.5,5.5),d.getRangeBounds(true));
    d.add(Double.NEGATIVE_INFINITY,0.0,"R1","C3");
    assertEquals(new Range(Double.NEGATIVE_INFINITY,4.5),d.getRangeBounds(false));
    assertEquals(new Range(Double.NEGATIVE_INFINITY,5.5),d.getRangeBounds(true));
    d.add(Double.POSITIVE_INFINITY,0.0,"R1","C3");
    assertEquals(new Range(0.5,Double.POSITIVE_INFINITY),d.getRangeBounds(false));
    assertEquals(new Range(-1.5,Double.POSITIVE_INFINITY),d.getRangeBounds(true));
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    DefaultStatisticalCategoryDataset d1=new DefaultStatisticalCategoryDataset();
    DefaultStatisticalCategoryDataset d2=new DefaultStatisticalCategoryDataset();
    assertTrue(d1.equals(d2));
    assertTrue(d2.equals(d1));
  }
  /** 
 * Some checks for cloning.
 */
  public void testCloning(){
    DefaultStatisticalCategoryDataset d1=new DefaultStatisticalCategoryDataset();
    d1.add(1.1,2.2,"R1","C1");
    d1.add(3.3,4.4,"R1","C2");
    d1.add(null,new Double(5.5),"R1","C3");
    d1.add(new Double(6.6),null,"R2","C3");
    DefaultStatisticalCategoryDataset d2=null;
    try {
      d2=(DefaultStatisticalCategoryDataset)d1.clone();
    }
 catch (    CloneNotSupportedException e) {
      fail(e.toString());
    }
    assertTrue(d1 != d2);
    assertTrue(d1.getClass() == d2.getClass());
    assertTrue(d1.equals(d2));
    d1.add(1.1,2.2,"R3","C1");
    assertFalse(d1.equals(d2));
  }
  /** 
 * Check serialization of a default instance.
 */
  public void testSerialization1(){
    DefaultStatisticalCategoryDataset d1=new DefaultStatisticalCategoryDataset();
    d1.add(1.1,2.2,"R1","C1");
    d1.add(3.3,4.4,"R1","C2");
    d1.add(null,new Double(5.5),"R1","C3");
    d1.add(new Double(6.6),null,"R2","C3");
    DefaultStatisticalCategoryDataset d2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(d1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      d2=(DefaultStatisticalCategoryDataset)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      fail(e.toString());
    }
    assertEquals(d1,d2);
  }
  /** 
 * Check serialization of a more complex instance.
 */
  public void testSerialization2(){
    DefaultStatisticalCategoryDataset d1=new DefaultStatisticalCategoryDataset();
    d1.add(1.2,3.4,"Row 1","Column 1");
    DefaultStatisticalCategoryDataset d2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(d1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      d2=(DefaultStatisticalCategoryDataset)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      fail(e.toString());
    }
    assertEquals(d1,d2);
  }
  private static final double EPSILON=0.0000000001;
  /** 
 * Some checks for the add() method.
 */
  public void testAdd(){
    DefaultStatisticalCategoryDataset d1=new DefaultStatisticalCategoryDataset();
    d1.add(1.0,2.0,"R1","C1");
    assertEquals(1.0,d1.getValue("R1","C1").doubleValue(),EPSILON);
    assertEquals(2.0,d1.getStdDevValue("R1","C1").doubleValue(),EPSILON);
    d1.add(10.0,20.0,"R1","C1");
    assertEquals(10.0,d1.getValue("R1","C1").doubleValue(),EPSILON);
    assertEquals(20.0,d1.getStdDevValue("R1","C1").doubleValue(),EPSILON);
  }
  /** 
 * Some checks for the getRangeLowerBound() method.
 */
  public void testGetRangeLowerBound(){
    DefaultStatisticalCategoryDataset d1=new DefaultStatisticalCategoryDataset();
    d1.add(1.0,2.0,"R1","C1");
    assertEquals(1.0,d1.getRangeLowerBound(false),EPSILON);
    assertEquals(-1.0,d1.getRangeLowerBound(true),EPSILON);
  }
  /** 
 * Some checks for the getRangeUpperBound() method.
 */
  public void testGetRangeUpperBound(){
    DefaultStatisticalCategoryDataset d1=new DefaultStatisticalCategoryDataset();
    d1.add(1.0,2.0,"R1","C1");
    assertEquals(1.0,d1.getRangeUpperBound(false),EPSILON);
    assertEquals(3.0,d1.getRangeUpperBound(true),EPSILON);
  }
  /** 
 * Some checks for the getRangeBounds() method.
 */
  public void testGetRangeBounds2(){
    DefaultStatisticalCategoryDataset d1=new DefaultStatisticalCategoryDataset();
    d1.add(1.0,2.0,"R1","C1");
    assertEquals(new Range(1.0,1.0),d1.getRangeBounds(false));
    assertEquals(new Range(-1.0,3.0),d1.getRangeBounds(true));
    d1.add(10.0,20.0,"R1","C1");
    assertEquals(new Range(10.0,10.0),d1.getRangeBounds(false));
    assertEquals(new Range(-10.0,30.0),d1.getRangeBounds(true));
  }
  /** 
 * Some checks for the remove method.
 */
  public void testRemove(){
    DefaultStatisticalCategoryDataset data=new DefaultStatisticalCategoryDataset();
    boolean pass=false;
    try {
      data.remove("R1","R2");
    }
 catch (    UnknownKeyException e) {
      pass=true;
    }
    assertTrue(pass);
    data.add(1.0,0.5,"R1","C1");
    assertEquals(new Range(1.0,1.0),data.getRangeBounds(false));
    assertEquals(new Range(0.5,1.5),data.getRangeBounds(true));
    data.add(1.4,0.2,"R2","C1");
    assertEquals(1.0,data.getRangeLowerBound(false),EPSILON);
    assertEquals(1.4,data.getRangeUpperBound(false),EPSILON);
    assertEquals(0.5,data.getRangeLowerBound(true),EPSILON);
    assertEquals(1.6,data.getRangeUpperBound(true),EPSILON);
    data.remove("R1","C1");
    assertEquals(1.4,data.getRangeLowerBound(false),EPSILON);
    assertEquals(1.4,data.getRangeUpperBound(false),EPSILON);
    assertEquals(1.2,data.getRangeLowerBound(true),EPSILON);
    assertEquals(1.6,data.getRangeUpperBound(true),EPSILON);
  }
}
