package org.jfree.data.general.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
/** 
 * Tests for the             {@link DefaultKeyedValues2DDataset} class.
 */
public class DefaultKeyedValues2DDatasetTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(DefaultKeyedValues2DDatasetTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public DefaultKeyedValues2DDatasetTests(  String name){
    super(name);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    DefaultKeyedValues2DDataset d1=new DefaultKeyedValues2DDataset();
    d1.setValue(new Integer(1),"V1","C1");
    d1.setValue(null,"V2","C1");
    d1.setValue(new Integer(3),"V3","C2");
    DefaultKeyedValues2DDataset d2=null;
    try {
      d2=(DefaultKeyedValues2DDataset)d1.clone();
    }
 catch (    CloneNotSupportedException e) {
      System.err.println("Failed to clone.");
    }
    assertTrue(d1 != d2);
    assertTrue(d1.getClass() == d2.getClass());
    assertTrue(d1.equals(d2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    DefaultKeyedValues2DDataset d1=new DefaultKeyedValues2DDataset();
    d1.addValue(new Double(234.2),"Row1","Col1");
    d1.addValue(null,"Row1","Col2");
    d1.addValue(new Double(345.9),"Row2","Col1");
    d1.addValue(new Double(452.7),"Row2","Col2");
    DefaultKeyedValues2DDataset d2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(d1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      d2=(DefaultKeyedValues2DDataset)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(d1,d2);
  }
}
