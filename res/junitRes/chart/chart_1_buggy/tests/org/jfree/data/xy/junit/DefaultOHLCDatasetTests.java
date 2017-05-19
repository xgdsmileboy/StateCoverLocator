package org.jfree.data.xy.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.Range;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
/** 
 * Tests for the             {@link DefaultOHLCDataset} class.
 */
public class DefaultOHLCDatasetTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(DefaultOHLCDatasetTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public DefaultOHLCDatasetTests(  String name){
    super(name);
  }
  private static final double EPSILON=0.0000000001;
  /** 
 * A small test for the data range calculated on this dataset.
 */
  public void testDataRange(){
    OHLCDataItem[] data=new OHLCDataItem[3];
    data[0]=new OHLCDataItem(new Date(11L),2.0,4.0,1.0,3.0,100.0);
    data[1]=new OHLCDataItem(new Date(22L),4.0,9.0,2.0,5.0,120.0);
    data[2]=new OHLCDataItem(new Date(33L),3.0,7.0,3.0,6.0,140.0);
    DefaultOHLCDataset d=new DefaultOHLCDataset("S1",data);
    Range r=DatasetUtilities.findRangeBounds(d,true);
    assertEquals(1.0,r.getLowerBound(),EPSILON);
    assertEquals(9.0,r.getUpperBound(),EPSILON);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    DefaultOHLCDataset d1=new DefaultOHLCDataset("Series 1",new OHLCDataItem[0]);
    DefaultOHLCDataset d2=new DefaultOHLCDataset("Series 1",new OHLCDataItem[0]);
    assertTrue(d1.equals(d2));
    assertTrue(d2.equals(d1));
    d1=new DefaultOHLCDataset("Series 2",new OHLCDataItem[0]);
    assertFalse(d1.equals(d2));
    d2=new DefaultOHLCDataset("Series 2",new OHLCDataItem[0]);
    assertTrue(d1.equals(d2));
    d1=new DefaultOHLCDataset("Series 2",new OHLCDataItem[]{new OHLCDataItem(new Date(123L),1.2,3.4,5.6,7.8,99.9)});
    assertFalse(d1.equals(d2));
    d2=new DefaultOHLCDataset("Series 2",new OHLCDataItem[]{new OHLCDataItem(new Date(123L),1.2,3.4,5.6,7.8,99.9)});
    assertTrue(d1.equals(d2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    DefaultOHLCDataset d1=new DefaultOHLCDataset("Series 1",new OHLCDataItem[0]);
    DefaultOHLCDataset d2=null;
    try {
      d2=(DefaultOHLCDataset)d1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(d1 != d2);
    assertTrue(d1.getClass() == d2.getClass());
    assertTrue(d1.equals(d2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning2(){
    OHLCDataItem item1=new OHLCDataItem(new Date(1L),1.0,2.0,3.0,4.0,5.0);
    OHLCDataItem item2=new OHLCDataItem(new Date(2L),6.0,7.0,8.0,9.0,10.0);
    OHLCDataItem[] items=new OHLCDataItem[]{item2,item1};
    DefaultOHLCDataset d1=new DefaultOHLCDataset("Series 1",items);
    DefaultOHLCDataset d2=null;
    try {
      d2=(DefaultOHLCDataset)d1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(d1 != d2);
    assertTrue(d1.getClass() == d2.getClass());
    assertTrue(d1.equals(d2));
    d1.sortDataByDate();
    assertFalse(d1.equals(d2));
  }
  /** 
 * Verify that this class implements             {@link PublicCloneable}.
 */
  public void testPublicCloneable(){
    DefaultOHLCDataset d1=new DefaultOHLCDataset("Series 1",new OHLCDataItem[0]);
    assertTrue(d1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    DefaultOHLCDataset d1=new DefaultOHLCDataset("Series 1",new OHLCDataItem[0]);
    DefaultOHLCDataset d2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(d1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      d2=(DefaultOHLCDataset)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(d1,d2);
  }
}
