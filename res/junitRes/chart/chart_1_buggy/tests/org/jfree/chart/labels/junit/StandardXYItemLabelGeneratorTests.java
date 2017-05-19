package org.jfree.chart.labels.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link StandardXYItemLabelGenerator} class.
 */
public class StandardXYItemLabelGeneratorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StandardXYItemLabelGeneratorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StandardXYItemLabelGeneratorTests(  String name){
    super(name);
  }
  /** 
 * A series of tests for the equals() method.
 */
  public void testEquals(){
    String f1="{1}";
    String f2="{2}";
    NumberFormat xnf1=new DecimalFormat("0.00");
    NumberFormat xnf2=new DecimalFormat("0.000");
    NumberFormat ynf1=new DecimalFormat("0.00");
    NumberFormat ynf2=new DecimalFormat("0.000");
    StandardXYItemLabelGenerator g1=null;
    StandardXYItemLabelGenerator g2=null;
    g1=new StandardXYItemLabelGenerator(f1,xnf1,ynf1);
    g2=new StandardXYItemLabelGenerator(f1,xnf1,ynf1);
    assertTrue(g1.equals(g2));
    assertTrue(g2.equals(g1));
    g1=new StandardXYItemLabelGenerator(f2,xnf1,ynf1);
    assertFalse(g1.equals(g2));
    g2=new StandardXYItemLabelGenerator(f2,xnf1,ynf1);
    assertTrue(g1.equals(g2));
    g1=new StandardXYItemLabelGenerator(f2,xnf2,ynf1);
    assertFalse(g1.equals(g2));
    g2=new StandardXYItemLabelGenerator(f2,xnf2,ynf1);
    assertTrue(g1.equals(g2));
    g1=new StandardXYItemLabelGenerator(f2,xnf2,ynf2);
    assertFalse(g1.equals(g2));
    g2=new StandardXYItemLabelGenerator(f2,xnf2,ynf2);
    assertTrue(g1.equals(g2));
    DateFormat xdf1=new SimpleDateFormat("d-MMM");
    DateFormat xdf2=new SimpleDateFormat("d-MMM-yyyy");
    DateFormat ydf1=new SimpleDateFormat("d-MMM");
    DateFormat ydf2=new SimpleDateFormat("d-MMM-yyyy");
    g1=new StandardXYItemLabelGenerator(f1,xdf1,ydf1);
    g2=new StandardXYItemLabelGenerator(f1,xdf1,ydf1);
    assertTrue(g1.equals(g2));
    assertTrue(g2.equals(g1));
    g1=new StandardXYItemLabelGenerator(f1,xdf2,ydf1);
    assertFalse(g1.equals(g2));
    g2=new StandardXYItemLabelGenerator(f1,xdf2,ydf1);
    assertTrue(g1.equals(g2));
    g1=new StandardXYItemLabelGenerator(f1,xdf2,ydf2);
    assertFalse(g1.equals(g2));
    g2=new StandardXYItemLabelGenerator(f1,xdf2,ydf2);
    assertTrue(g1.equals(g2));
  }
  /** 
 * Simple check that hashCode is implemented.
 */
  public void testHashCode(){
    StandardXYItemLabelGenerator g1=new StandardXYItemLabelGenerator();
    StandardXYItemLabelGenerator g2=new StandardXYItemLabelGenerator();
    assertTrue(g1.equals(g2));
    assertTrue(g1.hashCode() == g2.hashCode());
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    StandardXYItemLabelGenerator g1=new StandardXYItemLabelGenerator();
    StandardXYItemLabelGenerator g2=null;
    try {
      g2=(StandardXYItemLabelGenerator)g1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(g1 != g2);
    assertTrue(g1.getClass() == g2.getClass());
    assertTrue(g1.equals(g2));
    g1.getXFormat().setMinimumIntegerDigits(2);
    assertFalse(g1.equals(g2));
    g2.getXFormat().setMinimumIntegerDigits(2);
    assertTrue(g1.equals(g2));
    g1.getYFormat().setMinimumIntegerDigits(2);
    assertFalse(g1.equals(g2));
    g2.getYFormat().setMinimumIntegerDigits(2);
    assertTrue(g1.equals(g2));
    g1=new StandardXYItemLabelGenerator("{0} {1} {2}",DateFormat.getInstance(),DateFormat.getInstance());
    try {
      g2=(StandardXYItemLabelGenerator)g1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(g1 != g2);
    assertTrue(g1.getClass() == g2.getClass());
    assertTrue(g1.equals(g2));
    g1.getXDateFormat().setNumberFormat(new DecimalFormat("0.000"));
    assertFalse(g1.equals(g2));
    g2.getXDateFormat().setNumberFormat(new DecimalFormat("0.000"));
    assertTrue(g1.equals(g2));
    g1.getYDateFormat().setNumberFormat(new DecimalFormat("0.000"));
    assertFalse(g1.equals(g2));
    g2.getYDateFormat().setNumberFormat(new DecimalFormat("0.000"));
    assertTrue(g1.equals(g2));
  }
  /** 
 * Check to ensure that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    StandardXYItemLabelGenerator g1=new StandardXYItemLabelGenerator();
    assertTrue(g1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    StandardXYItemLabelGenerator g1=new StandardXYItemLabelGenerator();
    StandardXYItemLabelGenerator g2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(g1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      g2=(StandardXYItemLabelGenerator)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(g1,g2);
  }
}
