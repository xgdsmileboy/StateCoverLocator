package org.jfree.chart.labels.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.DefaultCategoryDataset;
/** 
 * Tests for the             {@link StandardCategorySeriesLabelGenerator} class.
 */
public class StandardCategorySeriesLabelGeneratorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StandardCategorySeriesLabelGeneratorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StandardCategorySeriesLabelGeneratorTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the generalLabel() method.
 */
  public void testGenerateLabel(){
    StandardCategorySeriesLabelGenerator g=new StandardCategorySeriesLabelGenerator("{0}");
    DefaultCategoryDataset dataset=new DefaultCategoryDataset();
    dataset.addValue(1.0,"R0","C0");
    dataset.addValue(2.0,"R0","C1");
    dataset.addValue(3.0,"R1","C0");
    dataset.addValue(null,"R1","C1");
    String s=g.generateLabel(dataset,0);
    assertEquals("R0",s);
  }
  /** 
 * Some checks for the equals() method.
 */
  public void testEquals(){
    StandardCategorySeriesLabelGenerator g1=new StandardCategorySeriesLabelGenerator();
    StandardCategorySeriesLabelGenerator g2=new StandardCategorySeriesLabelGenerator();
    assertTrue(g1.equals(g2));
    assertTrue(g2.equals(g1));
    g1=new StandardCategorySeriesLabelGenerator("{1}");
    assertFalse(g1.equals(g2));
    g2=new StandardCategorySeriesLabelGenerator("{1}");
    assertTrue(g1.equals(g2));
  }
  /** 
 * Simple check that hashCode is implemented.
 */
  public void testHashCode(){
    StandardCategorySeriesLabelGenerator g1=new StandardCategorySeriesLabelGenerator();
    StandardCategorySeriesLabelGenerator g2=new StandardCategorySeriesLabelGenerator();
    assertTrue(g1.equals(g2));
    assertTrue(g1.hashCode() == g2.hashCode());
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    StandardCategorySeriesLabelGenerator g1=new StandardCategorySeriesLabelGenerator("{1}");
    StandardCategorySeriesLabelGenerator g2=null;
    try {
      g2=(StandardCategorySeriesLabelGenerator)g1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(g1 != g2);
    assertTrue(g1.getClass() == g2.getClass());
    assertTrue(g1.equals(g2));
  }
  /** 
 * Check to ensure that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    StandardCategorySeriesLabelGenerator g1=new StandardCategorySeriesLabelGenerator("{1}");
    assertTrue(g1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    StandardCategorySeriesLabelGenerator g1=new StandardCategorySeriesLabelGenerator("{2}");
    StandardCategorySeriesLabelGenerator g2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(g1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      g2=(StandardCategorySeriesLabelGenerator)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(g1,g2);
  }
}
