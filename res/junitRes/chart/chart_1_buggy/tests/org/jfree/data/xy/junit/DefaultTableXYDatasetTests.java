package org.jfree.data.xy.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
/** 
 * Tests for the             {@link DefaultTableXYDataset} class.
 */
public class DefaultTableXYDatasetTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(DefaultTableXYDatasetTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public DefaultTableXYDatasetTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    DefaultTableXYDataset d1=new DefaultTableXYDataset();
    XYSeries s1=new XYSeries("Series 1",true,false);
    s1.add(1.0,1.1);
    s1.add(2.0,2.2);
    d1.addSeries(s1);
    DefaultTableXYDataset d2=new DefaultTableXYDataset();
    XYSeries s2=new XYSeries("Series 1",true,false);
    s2.add(1.0,1.1);
    s2.add(2.0,2.2);
    d2.addSeries(s2);
    assertTrue(d1.equals(d2));
    assertTrue(d2.equals(d1));
    s1.add(3.0,3.3);
    assertFalse(d1.equals(d2));
    s2.add(3.0,3.3);
    assertTrue(d1.equals(d2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    DefaultTableXYDataset d1=new DefaultTableXYDataset();
    XYSeries s1=new XYSeries("Series 1",true,false);
    s1.add(1.0,1.1);
    s1.add(2.0,2.2);
    d1.addSeries(s1);
    DefaultTableXYDataset d2=null;
    try {
      d2=(DefaultTableXYDataset)d1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(d1 != d2);
    assertTrue(d1.getClass() == d2.getClass());
    assertTrue(d1.equals(d2));
    s1.add(3.0,3.3);
    assertFalse(d1.equals(d2));
  }
  /** 
 * Verify that this class implements             {@link PublicCloneable}.
 */
  public void testPublicCloneable(){
    DefaultTableXYDataset d1=new DefaultTableXYDataset();
    assertTrue(d1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    DefaultTableXYDataset d1=new DefaultTableXYDataset();
    XYSeries s1=new XYSeries("Series 1",true,false);
    s1.add(1.0,1.1);
    s1.add(2.0,2.2);
    d1.addSeries(s1);
    DefaultTableXYDataset d2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(d1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      d2=(DefaultTableXYDataset)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(d1,d2);
  }
  private static final double EPSILON=0.0000000001;
  /** 
 * This is a test for bug 1312066 - adding a new series should trigger a recalculation of the interval width, if it is being automatically calculated.
 */
  public void testAddSeries(){
    DefaultTableXYDataset d1=new DefaultTableXYDataset();
    d1.setAutoWidth(true);
    XYSeries s1=new XYSeries("Series 1",true,false);
    s1.add(3.0,1.1);
    s1.add(7.0,2.2);
    d1.addSeries(s1);
    assertEquals(3.0,d1.getXValue(0,0),EPSILON);
    assertEquals(7.0,d1.getXValue(0,1),EPSILON);
    assertEquals(1.0,d1.getStartXValue(0,0),EPSILON);
    assertEquals(5.0,d1.getStartXValue(0,1),EPSILON);
    assertEquals(5.0,d1.getEndXValue(0,0),EPSILON);
    assertEquals(9.0,d1.getEndXValue(0,1),EPSILON);
    XYSeries s2=new XYSeries("Series 2",true,false);
    s2.add(7.5,1.1);
    s2.add(9.0,2.2);
    d1.addSeries(s2);
    assertEquals(3.0,d1.getXValue(1,0),EPSILON);
    assertEquals(7.0,d1.getXValue(1,1),EPSILON);
    assertEquals(7.5,d1.getXValue(1,2),EPSILON);
    assertEquals(9.0,d1.getXValue(1,3),EPSILON);
    assertEquals(7.25,d1.getStartXValue(1,2),EPSILON);
    assertEquals(8.75,d1.getStartXValue(1,3),EPSILON);
    assertEquals(7.75,d1.getEndXValue(1,2),EPSILON);
    assertEquals(9.25,d1.getEndXValue(1,3),EPSILON);
    assertEquals(2.75,d1.getStartXValue(0,0),EPSILON);
    assertEquals(6.75,d1.getStartXValue(0,1),EPSILON);
    assertEquals(3.25,d1.getEndXValue(0,0),EPSILON);
    assertEquals(7.25,d1.getEndXValue(0,1),EPSILON);
  }
  /** 
 * Some basic checks for the getSeries() method.
 */
  public void testGetSeries(){
    DefaultTableXYDataset d1=new DefaultTableXYDataset();
    XYSeries s1=new XYSeries("Series 1",true,false);
    d1.addSeries(s1);
    assertEquals("Series 1",d1.getSeries(0).getKey());
    boolean pass=false;
    try {
      d1.getSeries(-1);
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      d1.getSeries(1);
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
  }
}
