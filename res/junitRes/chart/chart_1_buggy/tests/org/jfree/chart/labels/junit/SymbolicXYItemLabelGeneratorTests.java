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
import org.jfree.chart.labels.SymbolicXYItemLabelGenerator;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link SymbolicXYItemLabelGenerator} class.
 */
public class SymbolicXYItemLabelGeneratorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(SymbolicXYItemLabelGeneratorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public SymbolicXYItemLabelGeneratorTests(  String name){
    super(name);
  }
  /** 
 * Tests the equals method.
 */
  public void testEquals(){
    SymbolicXYItemLabelGenerator g1=new SymbolicXYItemLabelGenerator();
    SymbolicXYItemLabelGenerator g2=new SymbolicXYItemLabelGenerator();
    assertTrue(g1.equals(g2));
    assertTrue(g2.equals(g1));
  }
  /** 
 * Simple check that hashCode is implemented.
 */
  public void testHashCode(){
    SymbolicXYItemLabelGenerator g1=new SymbolicXYItemLabelGenerator();
    SymbolicXYItemLabelGenerator g2=new SymbolicXYItemLabelGenerator();
    assertTrue(g1.equals(g2));
    assertTrue(g1.hashCode() == g2.hashCode());
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    SymbolicXYItemLabelGenerator g1=new SymbolicXYItemLabelGenerator();
    SymbolicXYItemLabelGenerator g2=null;
    try {
      g2=(SymbolicXYItemLabelGenerator)g1.clone();
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
    SymbolicXYItemLabelGenerator g1=new SymbolicXYItemLabelGenerator();
    assertTrue(g1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    SymbolicXYItemLabelGenerator g1=new SymbolicXYItemLabelGenerator();
    SymbolicXYItemLabelGenerator g2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(g1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      g2=(SymbolicXYItemLabelGenerator)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(g1,g2);
  }
}
