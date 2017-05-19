package org.jfree.chart.labels.junit;
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
import org.jfree.chart.labels.CustomXYToolTipGenerator;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link CustomXYToolTipGenerator} class.
 */
public class CustomXYItemLabelGeneratorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(CustomXYItemLabelGeneratorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public CustomXYItemLabelGeneratorTests(  String name){
    super(name);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    CustomXYToolTipGenerator g1=new CustomXYToolTipGenerator();
    CustomXYToolTipGenerator g2=null;
    try {
      g2=(CustomXYToolTipGenerator)g1.clone();
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
    CustomXYToolTipGenerator g1=new CustomXYToolTipGenerator();
    assertTrue(g1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    List t1=new java.util.ArrayList();
    t1.add("Tooltip A1");
    t1.add("Tooltip A2");
    t1.add("Tooltip A3");
    List t2=new java.util.ArrayList();
    t2.add("Tooltip B1");
    t2.add("Tooltip B2");
    t2.add("Tooltip B3");
    CustomXYToolTipGenerator g1=new CustomXYToolTipGenerator();
    g1.addToolTipSeries(t1);
    g1.addToolTipSeries(t2);
    CustomXYToolTipGenerator g2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(g1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      g2=(CustomXYToolTipGenerator)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(g1,g2);
  }
}
