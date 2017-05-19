package org.jfree.data.category.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
/** 
 * Tests for the             {@link DefaultIntervalCategoryDataset} class.
 */
public class DefaultIntervalCategoryDatasetTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(DefaultIntervalCategoryDatasetTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public DefaultIntervalCategoryDatasetTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the getValue() method.
 */
  public void testGetValue(){
    double[] starts_S1=new double[]{0.1,0.2,0.3};
    double[] starts_S2=new double[]{0.3,0.4,0.5};
    double[] ends_S1=new double[]{0.5,0.6,0.7};
    double[] ends_S2=new double[]{0.7,0.8,0.9};
    double[][] starts=new double[][]{starts_S1,starts_S2};
    double[][] ends=new double[][]{ends_S1,ends_S2};
    DefaultIntervalCategoryDataset d=new DefaultIntervalCategoryDataset(starts,ends);
    assertEquals(new Double(0.1),d.getStartValue("Series 1","Category 1"));
    assertEquals(new Double(0.2),d.getStartValue("Series 1","Category 2"));
    assertEquals(new Double(0.3),d.getStartValue("Series 1","Category 3"));
    assertEquals(new Double(0.3),d.getStartValue("Series 2","Category 1"));
    assertEquals(new Double(0.4),d.getStartValue("Series 2","Category 2"));
    assertEquals(new Double(0.5),d.getStartValue("Series 2","Category 3"));
    assertEquals(new Double(0.5),d.getEndValue("Series 1","Category 1"));
    assertEquals(new Double(0.6),d.getEndValue("Series 1","Category 2"));
    assertEquals(new Double(0.7),d.getEndValue("Series 1","Category 3"));
    assertEquals(new Double(0.7),d.getEndValue("Series 2","Category 1"));
    assertEquals(new Double(0.8),d.getEndValue("Series 2","Category 2"));
    assertEquals(new Double(0.9),d.getEndValue("Series 2","Category 3"));
    boolean pass=false;
    try {
      d.getValue("XX","Category 1");
    }
 catch (    UnknownKeyException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      d.getValue("Series 1","XX");
    }
 catch (    UnknownKeyException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some tests for the getRowCount() method.
 */
  public void testGetRowAndColumnCount(){
    double[] starts_S1=new double[]{0.1,0.2,0.3};
    double[] starts_S2=new double[]{0.3,0.4,0.5};
    double[] ends_S1=new double[]{0.5,0.6,0.7};
    double[] ends_S2=new double[]{0.7,0.8,0.9};
    double[][] starts=new double[][]{starts_S1,starts_S2};
    double[][] ends=new double[][]{ends_S1,ends_S2};
    DefaultIntervalCategoryDataset d=new DefaultIntervalCategoryDataset(starts,ends);
    assertEquals(2,d.getRowCount());
    assertEquals(3,d.getColumnCount());
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    double[] starts_S1A=new double[]{0.1,0.2,0.3};
    double[] starts_S2A=new double[]{0.3,0.4,0.5};
    double[] ends_S1A=new double[]{0.5,0.6,0.7};
    double[] ends_S2A=new double[]{0.7,0.8,0.9};
    double[][] startsA=new double[][]{starts_S1A,starts_S2A};
    double[][] endsA=new double[][]{ends_S1A,ends_S2A};
    DefaultIntervalCategoryDataset dA=new DefaultIntervalCategoryDataset(startsA,endsA);
    double[] starts_S1B=new double[]{0.1,0.2,0.3};
    double[] starts_S2B=new double[]{0.3,0.4,0.5};
    double[] ends_S1B=new double[]{0.5,0.6,0.7};
    double[] ends_S2B=new double[]{0.7,0.8,0.9};
    double[][] startsB=new double[][]{starts_S1B,starts_S2B};
    double[][] endsB=new double[][]{ends_S1B,ends_S2B};
    DefaultIntervalCategoryDataset dB=new DefaultIntervalCategoryDataset(startsB,endsB);
    assertTrue(dA.equals(dB));
    assertTrue(dB.equals(dA));
    DefaultIntervalCategoryDataset empty1=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    DefaultIntervalCategoryDataset empty2=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    assertTrue(empty1.equals(empty2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    double[] starts_S1=new double[]{0.1,0.2,0.3};
    double[] starts_S2=new double[]{0.3,0.4,0.5};
    double[] ends_S1=new double[]{0.5,0.6,0.7};
    double[] ends_S2=new double[]{0.7,0.8,0.9};
    double[][] starts=new double[][]{starts_S1,starts_S2};
    double[][] ends=new double[][]{ends_S1,ends_S2};
    DefaultIntervalCategoryDataset d1=new DefaultIntervalCategoryDataset(starts,ends);
    DefaultIntervalCategoryDataset d2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(d1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      d2=(DefaultIntervalCategoryDataset)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(d1,d2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    double[] starts_S1=new double[]{0.1,0.2,0.3};
    double[] starts_S2=new double[]{0.3,0.4,0.5};
    double[] ends_S1=new double[]{0.5,0.6,0.7};
    double[] ends_S2=new double[]{0.7,0.8,0.9};
    double[][] starts=new double[][]{starts_S1,starts_S2};
    double[][] ends=new double[][]{ends_S1,ends_S2};
    DefaultIntervalCategoryDataset d1=new DefaultIntervalCategoryDataset(starts,ends);
    DefaultIntervalCategoryDataset d2=null;
    try {
      d2=(DefaultIntervalCategoryDataset)d1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(d1 != d2);
    assertTrue(d1.getClass() == d2.getClass());
    assertTrue(d1.equals(d2));
    d1.setStartValue(0,"Category 1",new Double(0.99));
    assertFalse(d1.equals(d2));
    d2.setStartValue(0,"Category 1",new Double(0.99));
    assertTrue(d1.equals(d2));
  }
  /** 
 * A check to ensure that an empty dataset can be cloned.
 */
  public void testCloning2(){
    DefaultIntervalCategoryDataset d1=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    DefaultIntervalCategoryDataset d2=null;
    try {
      d2=(DefaultIntervalCategoryDataset)d1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(d1 != d2);
    assertTrue(d1.getClass() == d2.getClass());
    assertTrue(d1.equals(d2));
  }
  /** 
 * Some basic checks for the setStartValue() method.
 */
  public void testSetStartValue(){
    double[] starts_S1=new double[]{0.1,0.2,0.3};
    double[] starts_S2=new double[]{0.3,0.4,0.5};
    double[] ends_S1=new double[]{0.5,0.6,0.7};
    double[] ends_S2=new double[]{0.7,0.8,0.9};
    double[][] starts=new double[][]{starts_S1,starts_S2};
    double[][] ends=new double[][]{ends_S1,ends_S2};
    DefaultIntervalCategoryDataset d1=new DefaultIntervalCategoryDataset(starts,ends);
    d1.setStartValue(0,"Category 2",new Double(99.9));
    assertEquals(new Double(99.9),d1.getStartValue("Series 1","Category 2"));
    boolean pass=false;
    try {
      d1.setStartValue(-1,"Category 2",new Double(99.9));
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      d1.setStartValue(2,"Category 2",new Double(99.9));
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some basic checks for the setEndValue() method.
 */
  public void testSetEndValue(){
    double[] starts_S1=new double[]{0.1,0.2,0.3};
    double[] starts_S2=new double[]{0.3,0.4,0.5};
    double[] ends_S1=new double[]{0.5,0.6,0.7};
    double[] ends_S2=new double[]{0.7,0.8,0.9};
    double[][] starts=new double[][]{starts_S1,starts_S2};
    double[][] ends=new double[][]{ends_S1,ends_S2};
    DefaultIntervalCategoryDataset d1=new DefaultIntervalCategoryDataset(starts,ends);
    d1.setEndValue(0,"Category 2",new Double(99.9));
    assertEquals(new Double(99.9),d1.getEndValue("Series 1","Category 2"));
    boolean pass=false;
    try {
      d1.setEndValue(-1,"Category 2",new Double(99.9));
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      d1.setEndValue(2,"Category 2",new Double(99.9));
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getSeriesCount() method.
 */
  public void testGetSeriesCount(){
    DefaultIntervalCategoryDataset empty=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    assertEquals(0,empty.getSeriesCount());
  }
  /** 
 * Some checks for the getCategoryCount() method.
 */
  public void testGetCategoryCount(){
    DefaultIntervalCategoryDataset empty=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    assertEquals(0,empty.getCategoryCount());
  }
  /** 
 * Some checks for the getSeriesIndex() method.
 */
  public void testGetSeriesIndex(){
    DefaultIntervalCategoryDataset empty=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    assertEquals(-1,empty.getSeriesIndex("ABC"));
  }
  /** 
 * Some checks for the getRowIndex() method.
 */
  public void testGetRowIndex(){
    DefaultIntervalCategoryDataset empty=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    assertEquals(-1,empty.getRowIndex("ABC"));
  }
  /** 
 * Some checks for the setSeriesKeys() method.
 */
  public void testSetSeriesKeys(){
    DefaultIntervalCategoryDataset empty=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    boolean pass=true;
    try {
      empty.setSeriesKeys(new String[0]);
    }
 catch (    RuntimeException e) {
      pass=false;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getCategoryIndex() method.
 */
  public void testGetCategoryIndex(){
    DefaultIntervalCategoryDataset empty=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    assertEquals(-1,empty.getCategoryIndex("ABC"));
  }
  /** 
 * Some checks for the getColumnIndex() method.
 */
  public void testGetColumnIndex(){
    DefaultIntervalCategoryDataset empty=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    assertEquals(-1,empty.getColumnIndex("ABC"));
  }
  /** 
 * Some checks for the setCategoryKeys() method.
 */
  public void testSetCategoryKeys(){
    DefaultIntervalCategoryDataset empty=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    boolean pass=true;
    try {
      empty.setCategoryKeys(new String[0]);
    }
 catch (    RuntimeException e) {
      pass=false;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getColumnKeys() method.
 */
  public void testGetColumnKeys(){
    DefaultIntervalCategoryDataset empty=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    List keys=empty.getColumnKeys();
    assertEquals(0,keys.size());
  }
  /** 
 * Some checks for the getRowKeys() method.
 */
  public void testGetRowKeys(){
    DefaultIntervalCategoryDataset empty=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    List keys=empty.getRowKeys();
    assertEquals(0,keys.size());
  }
  /** 
 * Some checks for the getColumnCount() method.
 */
  public void testGetColumnCount(){
    DefaultIntervalCategoryDataset empty=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    assertEquals(0,empty.getColumnCount());
  }
  /** 
 * Some checks for the getRowCount() method.
 */
  public void testGetRowCount(){
    DefaultIntervalCategoryDataset empty=new DefaultIntervalCategoryDataset(new double[0][0],new double[0][0]);
    assertEquals(0,empty.getColumnCount());
  }
}
