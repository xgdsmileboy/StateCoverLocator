package org.jfree.data.category.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.util.TableOrder;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.pie.DefaultPieDataset;
/** 
 * Tests for the             {@link CategoryToPieDataset} class.
 */
public class CategoryToPieDatasetTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(CategoryToPieDatasetTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public CategoryToPieDatasetTests(  String name){
    super(name);
  }
  /** 
 * Some tests for the constructor.
 */
  public void testConstructor(){
    CategoryToPieDataset p1=new CategoryToPieDataset(null,TableOrder.BY_COLUMN,0);
    assertNull(p1.getUnderlyingDataset());
    assertEquals(p1.getItemCount(),0);
    assertTrue(p1.getKeys().isEmpty());
    assertNull(p1.getValue("R1"));
  }
  /** 
 * Some checks for the getValue() method.
 */
  public void testGetValue(){
    DefaultCategoryDataset underlying=new DefaultCategoryDataset();
    underlying.addValue(1.1,"R1","C1");
    underlying.addValue(2.2,"R1","C2");
    CategoryToPieDataset d1=new CategoryToPieDataset(underlying,TableOrder.BY_ROW,0);
    assertEquals(d1.getValue("C1"),new Double(1.1));
    assertEquals(d1.getValue("C2"),new Double(2.2));
    try {
      d1.getValue(-1);
      fail("Expected IndexOutOfBoundsException.");
    }
 catch (    IndexOutOfBoundsException e) {
    }
    try {
      d1.getValue(d1.getItemCount());
      fail("Expected IndexOutOfBoundsException.");
    }
 catch (    IndexOutOfBoundsException e) {
    }
    CategoryToPieDataset p1=new CategoryToPieDataset(null,TableOrder.BY_COLUMN,0);
    try {
      p1.getValue(0);
      fail("Expected IndexOutOfBoundsException.");
    }
 catch (    IndexOutOfBoundsException e) {
    }
  }
  /** 
 * Some checks for the getKey(int) method.
 */
  public void testGetKey(){
    DefaultCategoryDataset underlying=new DefaultCategoryDataset();
    underlying.addValue(1.1,"R1","C1");
    underlying.addValue(2.2,"R1","C2");
    CategoryToPieDataset d1=new CategoryToPieDataset(underlying,TableOrder.BY_ROW,0);
    assertEquals(d1.getKey(0),"C1");
    assertEquals(d1.getKey(1),"C2");
    try {
      d1.getKey(-1);
      fail("Expected IndexOutOfBoundsException.");
    }
 catch (    IndexOutOfBoundsException e) {
    }
    try {
      d1.getKey(d1.getItemCount());
      fail("Expected IndexOutOfBoundsException.");
    }
 catch (    IndexOutOfBoundsException e) {
    }
    CategoryToPieDataset p1=new CategoryToPieDataset(null,TableOrder.BY_COLUMN,0);
    try {
      p1.getKey(0);
      fail("Expected IndexOutOfBoundsException.");
    }
 catch (    IndexOutOfBoundsException e) {
    }
  }
  /** 
 * Some checks for the getIndex() method.
 */
  public void testGetIndex(){
    DefaultCategoryDataset underlying=new DefaultCategoryDataset();
    underlying.addValue(1.1,"R1","C1");
    underlying.addValue(2.2,"R1","C2");
    CategoryToPieDataset d1=new CategoryToPieDataset(underlying,TableOrder.BY_ROW,0);
    assertEquals(0,d1.getIndex("C1"));
    assertEquals(1,d1.getIndex("C2"));
    assertEquals(-1,d1.getIndex("XX"));
    boolean pass=false;
    try {
      d1.getIndex(null);
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * For datasets, the equals() method just checks keys and values.
 */
  public void testEquals(){
    DefaultCategoryDataset underlying=new DefaultCategoryDataset();
    underlying.addValue(1.1,"R1","C1");
    underlying.addValue(2.2,"R1","C2");
    CategoryToPieDataset d1=new CategoryToPieDataset(underlying,TableOrder.BY_COLUMN,1);
    DefaultPieDataset d2=new DefaultPieDataset();
    d2.setValue("R1",2.2);
    assertTrue(d1.equals(d2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    DefaultCategoryDataset underlying=new DefaultCategoryDataset();
    underlying.addValue(1.1,"R1","C1");
    underlying.addValue(2.2,"R1","C2");
    CategoryToPieDataset d1=new CategoryToPieDataset(underlying,TableOrder.BY_COLUMN,1);
    CategoryToPieDataset d2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(d1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      d2=(CategoryToPieDataset)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(d1,d2);
    assertEquals(d1.getUnderlyingDataset(),d2.getUnderlyingDataset());
    assertEquals(d1.getExtractType(),d2.getExtractType());
    assertEquals(d1.getExtractIndex(),d2.getExtractIndex());
  }
}
