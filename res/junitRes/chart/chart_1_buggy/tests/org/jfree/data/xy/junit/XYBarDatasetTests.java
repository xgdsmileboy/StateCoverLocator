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
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYBarDataset;
/** 
 * Some tests for the             {@link XYBarDataset} class.
 */
public class XYBarDatasetTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(XYBarDatasetTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public XYBarDatasetTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    DefaultXYDataset d1=new DefaultXYDataset();
    double[] x1=new double[]{1.0,2.0,3.0};
    double[] y1=new double[]{4.0,5.0,6.0};
    double[][] data1=new double[][]{x1,y1};
    d1.addSeries("S1",data1);
    DefaultXYDataset d2=new DefaultXYDataset();
    double[] x2=new double[]{1.0,2.0,3.0};
    double[] y2=new double[]{4.0,5.0,6.0};
    double[][] data2=new double[][]{x2,y2};
    d2.addSeries("S1",data2);
    XYBarDataset bd1=new XYBarDataset(d1,5.0);
    XYBarDataset bd2=new XYBarDataset(d2,5.0);
    assertTrue(bd1.equals(bd2));
    assertTrue(bd2.equals(bd1));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    DefaultXYDataset d1=new DefaultXYDataset();
    double[] x1=new double[]{1.0,2.0,3.0};
    double[] y1=new double[]{4.0,5.0,6.0};
    double[][] data1=new double[][]{x1,y1};
    d1.addSeries("S1",data1);
    XYBarDataset bd1=new XYBarDataset(d1,5.0);
    XYBarDataset bd2=null;
    try {
      bd2=(XYBarDataset)bd1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(bd1 != bd2);
    assertTrue(bd1.getClass() == bd2.getClass());
    assertTrue(bd1.equals(bd2));
    d1=(DefaultXYDataset)bd1.getUnderlyingDataset();
    d1.addSeries("S2",new double[][]{{1.0},{2.0}});
    assertFalse(bd1.equals(bd2));
    DefaultXYDataset d2=(DefaultXYDataset)bd2.getUnderlyingDataset();
    d2.addSeries("S2",new double[][]{{1.0},{2.0}});
    assertTrue(bd1.equals(bd2));
  }
  /** 
 * Verify that this class implements             {@link PublicCloneable}.
 */
  public void testPublicCloneable(){
    DefaultXYDataset d1=new DefaultXYDataset();
    double[] x1=new double[]{1.0,2.0,3.0};
    double[] y1=new double[]{4.0,5.0,6.0};
    double[][] data1=new double[][]{x1,y1};
    d1.addSeries("S1",data1);
    XYBarDataset bd1=new XYBarDataset(d1,5.0);
    assertTrue(bd1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    DefaultXYDataset d1=new DefaultXYDataset();
    double[] x1=new double[]{1.0,2.0,3.0};
    double[] y1=new double[]{4.0,5.0,6.0};
    double[][] data1=new double[][]{x1,y1};
    d1.addSeries("S1",data1);
    XYBarDataset bd1=new XYBarDataset(d1,5.0);
    XYBarDataset bd2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(bd1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      bd2=(XYBarDataset)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(bd1,bd2);
  }
}
