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
import org.jfree.data.statistics.HistogramDataset;
/** 
 * Tests for the             {@link HistogramDataset} class.
 */
public class HistogramDatasetTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(HistogramDatasetTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public HistogramDatasetTests(  String name){
    super(name);
  }
  private static final double EPSILON=0.0000000001;
  /** 
 * Some checks that the correct values are assigned to bins.
 */
  public void testBins(){
    double[] values={1.0,2.0,3.0,4.0,6.0,12.0,5.0,6.3,4.5};
    HistogramDataset hd=new HistogramDataset();
    hd.addSeries("Series 1",values,5);
    assertEquals(hd.getYValue(0,0),3.0,EPSILON);
    assertEquals(hd.getYValue(0,1),3.0,EPSILON);
    assertEquals(hd.getYValue(0,2),2.0,EPSILON);
    assertEquals(hd.getYValue(0,3),0.0,EPSILON);
    assertEquals(hd.getYValue(0,4),1.0,EPSILON);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    double[] values={1.0,2.0,3.0,4.0,6.0,12.0,5.0,6.3,4.5};
    HistogramDataset d1=new HistogramDataset();
    d1.addSeries("Series 1",values,5);
    HistogramDataset d2=new HistogramDataset();
    d2.addSeries("Series 1",values,5);
    assertTrue(d1.equals(d2));
    assertTrue(d2.equals(d1));
    d1.addSeries("Series 2",new double[]{1.0,2.0,3.0},2);
    assertFalse(d1.equals(d2));
    d2.addSeries("Series 2",new double[]{1.0,2.0,3.0},2);
    assertTrue(d1.equals(d2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    double[] values={1.0,2.0,3.0,4.0,6.0,12.0,5.0,6.3,4.5};
    HistogramDataset d1=new HistogramDataset();
    d1.addSeries("Series 1",values,5);
    HistogramDataset d2=null;
    try {
      d2=(HistogramDataset)d1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(d1 != d2);
    assertTrue(d1.getClass() == d2.getClass());
    assertTrue(d1.equals(d2));
    d1.addSeries("Series 2",new double[]{1.0,2.0,3.0},2);
    assertFalse(d1.equals(d2));
    d2.addSeries("Series 2",new double[]{1.0,2.0,3.0},2);
    assertTrue(d1.equals(d2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    double[] values={1.0,2.0,3.0,4.0,6.0,12.0,5.0,6.3,4.5};
    HistogramDataset d1=new HistogramDataset();
    d1.addSeries("Series 1",values,5);
    HistogramDataset d2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(d1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      d2=(HistogramDataset)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(d1,d2);
    d1.addSeries("Series 2",new double[]{1.0,2.0,3.0},2);
    assertFalse(d1.equals(d2));
    d2.addSeries("Series 2",new double[]{1.0,2.0,3.0},2);
    assertTrue(d1.equals(d2));
  }
  /** 
 * A test for a bug reported in the forum where the series name isn't being returned correctly.
 */
  public void testGetSeriesKey(){
    double[] values={1.0,2.0,3.0,4.0,6.0,12.0,5.0,6.3,4.5};
    HistogramDataset d1=new HistogramDataset();
    d1.addSeries("Series 1",values,5);
    assertEquals("Series 1",d1.getSeriesKey(0));
  }
  /** 
 * Some checks for the addSeries() method.
 */
  public void testAddSeries(){
    double[] values={-1.0,0.0,0.1,0.9,1.0,1.1,1.9,2.0,3.0};
    HistogramDataset d=new HistogramDataset();
    d.addSeries("S1",values,2,0.0,2.0);
    assertEquals(0.0,d.getStartXValue(0,0),EPSILON);
    assertEquals(1.0,d.getEndXValue(0,0),EPSILON);
    assertEquals(4.0,d.getYValue(0,0),EPSILON);
    assertEquals(1.0,d.getStartXValue(0,1),EPSILON);
    assertEquals(2.0,d.getEndXValue(0,1),EPSILON);
    assertEquals(5.0,d.getYValue(0,1),EPSILON);
  }
  /** 
 * Another check for the addSeries() method.
 */
  public void testAddSeries2(){
    double[] values={0.0,1.0,2.0,3.0,4.0,5.0};
    HistogramDataset hd=new HistogramDataset();
    hd.addSeries("S1",values,5);
    assertEquals(0.0,hd.getStartXValue(0,0),EPSILON);
    assertEquals(1.0,hd.getEndXValue(0,0),EPSILON);
    assertEquals(1.0,hd.getYValue(0,0),EPSILON);
    assertEquals(1.0,hd.getStartXValue(0,1),EPSILON);
    assertEquals(2.0,hd.getEndXValue(0,1),EPSILON);
    assertEquals(1.0,hd.getYValue(0,1),EPSILON);
    assertEquals(2.0,hd.getStartXValue(0,2),EPSILON);
    assertEquals(3.0,hd.getEndXValue(0,2),EPSILON);
    assertEquals(1.0,hd.getYValue(0,2),EPSILON);
    assertEquals(3.0,hd.getStartXValue(0,3),EPSILON);
    assertEquals(4.0,hd.getEndXValue(0,3),EPSILON);
    assertEquals(1.0,hd.getYValue(0,3),EPSILON);
    assertEquals(4.0,hd.getStartXValue(0,4),EPSILON);
    assertEquals(5.0,hd.getEndXValue(0,4),EPSILON);
    assertEquals(2.0,hd.getYValue(0,4),EPSILON);
  }
  /** 
 * This test is derived from a reported bug.
 */
  public void testBinBoundaries(){
    double[] values={-5.000000000000286E-5};
    int bins=1260;
    double minimum=-0.06307522528160199;
    double maximum=0.06297522528160199;
    HistogramDataset d=new HistogramDataset();
    d.addSeries("S1",values,bins,minimum,maximum);
    assertEquals(0.0,d.getYValue(0,629),EPSILON);
    assertEquals(1.0,d.getYValue(0,630),EPSILON);
    assertEquals(0.0,d.getYValue(0,631),EPSILON);
    assertTrue(values[0] > d.getStartXValue(0,630));
    assertTrue(values[0] < d.getEndXValue(0,630));
  }
  /** 
 * Some checks for bug 1553088.  An IndexOutOfBoundsException is thrown when a data value is *very* close to the upper limit of the last bin.
 */
  public void test1553088(){
    double[] values={-1.0,0.0,-Double.MIN_VALUE,3.0};
    HistogramDataset d=new HistogramDataset();
    d.addSeries("S1",values,2,-1.0,0.0);
    assertEquals(-1.0,d.getStartXValue(0,0),EPSILON);
    assertEquals(-0.5,d.getEndXValue(0,0),EPSILON);
    assertEquals(1.0,d.getYValue(0,0),EPSILON);
    assertEquals(-0.5,d.getStartXValue(0,1),EPSILON);
    assertEquals(0.0,d.getEndXValue(0,1),EPSILON);
    assertEquals(3.0,d.getYValue(0,1),EPSILON);
  }
}
