package org.jfree.data.time.ohlc.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.event.DatasetChangeEvent;
import org.jfree.data.event.DatasetChangeListener;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.Year;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
/** 
 * Tests for the             {@link OHLCSeriesCollectionTests} class.
 */
public class OHLCSeriesCollectionTests extends TestCase implements DatasetChangeListener {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(OHLCSeriesCollectionTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public OHLCSeriesCollectionTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    OHLCSeriesCollection c1=new OHLCSeriesCollection();
    OHLCSeriesCollection c2=new OHLCSeriesCollection();
    assertEquals(c1,c2);
    OHLCSeries s1=new OHLCSeries("Series");
    s1.add(new Year(2006),1.0,1.1,1.2,1.3);
    c1.addSeries(s1);
    assertFalse(c1.equals(c2));
    OHLCSeries s2=new OHLCSeries("Series");
    s2.add(new Year(2006),1.0,1.1,1.2,1.3);
    c2.addSeries(s2);
    assertTrue(c1.equals(c2));
    c1.addSeries(new OHLCSeries("Empty Series"));
    assertFalse(c1.equals(c2));
    c2.addSeries(new OHLCSeries("Empty Series"));
    assertTrue(c1.equals(c2));
    c1.setXPosition(TimePeriodAnchor.END);
    assertFalse(c1.equals(c2));
    c2.setXPosition(TimePeriodAnchor.END);
    assertTrue(c1.equals(c2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    OHLCSeriesCollection c1=new OHLCSeriesCollection();
    OHLCSeries s1=new OHLCSeries("Series");
    s1.add(new Year(2006),1.0,1.1,1.2,1.3);
    c1.addSeries(s1);
    OHLCSeriesCollection c2=null;
    try {
      c2=(OHLCSeriesCollection)c1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(c1 != c2);
    assertTrue(c1.getClass() == c2.getClass());
    assertTrue(c1.equals(c2));
    s1.setDescription("XYZ");
    assertFalse(c1.equals(c2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    OHLCSeriesCollection c1=new OHLCSeriesCollection();
    OHLCSeries s1=new OHLCSeries("Series");
    s1.add(new Year(2006),1.0,1.1,1.2,1.3);
    c1.addSeries(s1);
    OHLCSeriesCollection c2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(c1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      c2=(OHLCSeriesCollection)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(c1,c2);
  }
  /** 
 * A test for bug report 1170825 (originally affected XYSeriesCollection, this test is just copied over).
 */
  public void test1170825(){
    OHLCSeries s1=new OHLCSeries("Series1");
    OHLCSeriesCollection dataset=new OHLCSeriesCollection();
    dataset.addSeries(s1);
    try {
      dataset.getSeries(1);
    }
 catch (    IllegalArgumentException e) {
    }
catch (    IndexOutOfBoundsException e) {
      assertTrue(false);
    }
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    OHLCSeriesCollection c1=new OHLCSeriesCollection();
    OHLCSeries s1=new OHLCSeries("S");
    s1.add(new Year(2009),1.0,4.0,0.5,2.0);
    c1.addSeries(s1);
    OHLCSeriesCollection c2=new OHLCSeriesCollection();
    OHLCSeries s2=new OHLCSeries("S");
    s2.add(new Year(2009),1.0,4.0,0.5,2.0);
    c2.addSeries(s2);
    assertTrue(c1.equals(c2));
    int h1=c1.hashCode();
    int h2=c2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Some checks for the             {@link OHLCSeriesCollection#removeSeries(int)}method.
 */
  public void testRemoveSeries_int(){
    OHLCSeriesCollection c1=new OHLCSeriesCollection();
    OHLCSeries s1=new OHLCSeries("Series 1");
    OHLCSeries s2=new OHLCSeries("Series 2");
    OHLCSeries s3=new OHLCSeries("Series 3");
    OHLCSeries s4=new OHLCSeries("Series 4");
    c1.addSeries(s1);
    c1.addSeries(s2);
    c1.addSeries(s3);
    c1.addSeries(s4);
    c1.removeSeries(2);
    assertTrue(c1.getSeries(2).equals(s4));
    c1.removeSeries(0);
    assertTrue(c1.getSeries(0).equals(s2));
    assertEquals(2,c1.getSeriesCount());
  }
  /** 
 * Some checks for the            {@link OHLCSeriesCollection#removeSeries(OHLCSeries)} method.
 */
  public void testRemoveSeries(){
    OHLCSeriesCollection c1=new OHLCSeriesCollection();
    OHLCSeries s1=new OHLCSeries("Series 1");
    OHLCSeries s2=new OHLCSeries("Series 2");
    OHLCSeries s3=new OHLCSeries("Series 3");
    OHLCSeries s4=new OHLCSeries("Series 4");
    c1.addSeries(s1);
    c1.addSeries(s2);
    c1.addSeries(s3);
    c1.addSeries(s4);
    c1.removeSeries(s3);
    assertTrue(c1.getSeries(2).equals(s4));
    c1.removeSeries(s1);
    assertTrue(c1.getSeries(0).equals(s2));
    assertEquals(2,c1.getSeriesCount());
  }
  /** 
 * A simple check for the removeAllSeries() method.
 */
  public void testRemoveAllSeries(){
    OHLCSeriesCollection c1=new OHLCSeriesCollection();
    c1.addChangeListener(this);
    this.lastEvent=null;
    c1.removeAllSeries();
    assertNull(this.lastEvent);
    OHLCSeries s1=new OHLCSeries("Series 1");
    OHLCSeries s2=new OHLCSeries("Series 2");
    c1.addSeries(s1);
    c1.addSeries(s2);
    c1.removeAllSeries();
    assertEquals(0,c1.getSeriesCount());
    assertNotNull(this.lastEvent);
    this.lastEvent=null;
  }
  /** 
 * The last received event. 
 */
  private DatasetChangeEvent lastEvent;
  /** 
 * Receives dataset change events.
 * @param event  the event.
 */
  public void datasetChanged(  DatasetChangeEvent event){
    this.lastEvent=event;
  }
}
