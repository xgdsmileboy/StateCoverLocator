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
import org.jfree.data.xy.MatrixSeries;
import org.jfree.data.xy.MatrixSeriesCollection;
/** 
 * Tests for the             {@link MatrixSeriesCollection} class.
 */
public class MatrixSeriesCollectionTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(MatrixSeriesCollectionTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public MatrixSeriesCollectionTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    MatrixSeries s1=new MatrixSeries("Series",2,3);
    s1.update(0,0,1.1);
    MatrixSeriesCollection c1=new MatrixSeriesCollection();
    c1.addSeries(s1);
    MatrixSeries s2=new MatrixSeries("Series",2,3);
    s2.update(0,0,1.1);
    MatrixSeriesCollection c2=new MatrixSeriesCollection();
    c2.addSeries(s2);
    assertTrue(c1.equals(c2));
    assertTrue(c2.equals(c1));
    c1.addSeries(new MatrixSeries("Empty Series",1,1));
    assertFalse(c1.equals(c2));
    c2.addSeries(new MatrixSeries("Empty Series",1,1));
    assertTrue(c1.equals(c2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    MatrixSeries s1=new MatrixSeries("Series",2,3);
    s1.update(0,0,1.1);
    MatrixSeriesCollection c1=new MatrixSeriesCollection();
    c1.addSeries(s1);
    MatrixSeriesCollection c2=null;
    try {
      c2=(MatrixSeriesCollection)c1.clone();
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
 * Verify that this class implements             {@link PublicCloneable}.
 */
  public void testPublicCloneable(){
    MatrixSeriesCollection c1=new MatrixSeriesCollection();
    assertTrue(c1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    MatrixSeries s1=new MatrixSeries("Series",2,3);
    s1.update(0,0,1.1);
    MatrixSeriesCollection c1=new MatrixSeriesCollection();
    c1.addSeries(s1);
    MatrixSeriesCollection c2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(c1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      c2=(MatrixSeriesCollection)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(c1,c2);
  }
}
