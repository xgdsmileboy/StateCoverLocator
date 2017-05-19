package org.jfree.chart.labels.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.DefaultCategoryDataset;
/** 
 * Tests for the             {@link StandardCategoryItemLabelGenerator} class.
 */
public class StandardCategoryItemLabelGeneratorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StandardCategoryItemLabelGeneratorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StandardCategoryItemLabelGeneratorTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the generalLabel() method.
 */
  public void testGenerateLabel(){
    StandardCategoryItemLabelGenerator g=new StandardCategoryItemLabelGenerator("{2}",new DecimalFormat("0.000"));
    DefaultCategoryDataset dataset=new DefaultCategoryDataset();
    dataset.addValue(1.0,"R0","C0");
    dataset.addValue(2.0,"R0","C1");
    dataset.addValue(3.0,"R1","C0");
    dataset.addValue(null,"R1","C1");
    String s=g.generateLabel(dataset,0,0);
    assertTrue(s.startsWith("1"));
    assertTrue(s.endsWith("000"));
    s=g.generateLabel(dataset,1,1);
    assertEquals("-",s);
  }
  /** 
 * Some checks for the equals() method.
 */
  public void testEquals(){
    StandardCategoryItemLabelGenerator g1=new StandardCategoryItemLabelGenerator();
    StandardCategoryItemLabelGenerator g2=new StandardCategoryItemLabelGenerator();
    assertTrue(g1.equals(g2));
    assertTrue(g2.equals(g1));
    g1=new StandardCategoryItemLabelGenerator("{0}",new DecimalFormat("0.000"));
    assertFalse(g1.equals(g2));
    g2=new StandardCategoryItemLabelGenerator("{0}",new DecimalFormat("0.000"));
    assertTrue(g1.equals(g2));
    g1=new StandardCategoryItemLabelGenerator("{1}",new DecimalFormat("0.000"));
    assertFalse(g1.equals(g2));
    g2=new StandardCategoryItemLabelGenerator("{1}",new DecimalFormat("0.000"));
    assertTrue(g1.equals(g2));
    g1=new StandardCategoryItemLabelGenerator("{2}",new SimpleDateFormat("d-MMM"));
    assertFalse(g1.equals(g2));
    g2=new StandardCategoryItemLabelGenerator("{2}",new SimpleDateFormat("d-MMM"));
    assertTrue(g1.equals(g2));
  }
  /** 
 * Simple check that hashCode is implemented.
 */
  public void testHashCode(){
    StandardCategoryItemLabelGenerator g1=new StandardCategoryItemLabelGenerator();
    StandardCategoryItemLabelGenerator g2=new StandardCategoryItemLabelGenerator();
    assertTrue(g1.equals(g2));
    assertTrue(g1.hashCode() == g2.hashCode());
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    StandardCategoryItemLabelGenerator g1=new StandardCategoryItemLabelGenerator();
    StandardCategoryItemLabelGenerator g2=null;
    try {
      g2=(StandardCategoryItemLabelGenerator)g1.clone();
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
    StandardCategoryItemLabelGenerator g1=new StandardCategoryItemLabelGenerator();
    assertTrue(g1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    StandardCategoryItemLabelGenerator g1=new StandardCategoryItemLabelGenerator("{2}",DateFormat.getInstance());
    StandardCategoryItemLabelGenerator g2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(g1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      g2=(StandardCategoryItemLabelGenerator)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(g1,g2);
  }
  /** 
 * A test for bug 1481087.
 */
  public void testEquals1481087(){
    StandardCategoryItemLabelGenerator g1=new StandardCategoryItemLabelGenerator("{0}",new DecimalFormat("0.00"));
    StandardCategoryToolTipGenerator g2=new StandardCategoryToolTipGenerator("{0}",new DecimalFormat("0.00"));
    assertFalse(g1.equals(g2));
  }
}
