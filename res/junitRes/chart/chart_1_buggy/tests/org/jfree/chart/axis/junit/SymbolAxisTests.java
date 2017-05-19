package org.jfree.chart.axis.junit;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.axis.SymbolAxis;
/** 
 * Tests for the             {@link SymbolAxis} class.
 */
public class SymbolAxisTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(SymbolAxisTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public SymbolAxisTests(  String name){
    super(name);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    String[] tickLabels=new String[]{"One","Two","Three"};
    SymbolAxis a1=new SymbolAxis("Test Axis",tickLabels);
    SymbolAxis a2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(a1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      a2=(SymbolAxis)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(a1,a2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    SymbolAxis a1=new SymbolAxis("Axis",new String[]{"A","B"});
    SymbolAxis a2=null;
    try {
      a2=(SymbolAxis)a1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(a1 != a2);
    assertTrue(a1.getClass() == a2.getClass());
    assertTrue(a1.equals(a2));
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    SymbolAxis a1=new SymbolAxis("Axis",new String[]{"A","B"});
    SymbolAxis a2=new SymbolAxis("Axis",new String[]{"A","B"});
    assertTrue(a1.equals(a2));
    assertTrue(a2.equals(a1));
    a1=new SymbolAxis("Axis 2",new String[]{"A","B"});
    assertFalse(a1.equals(a2));
    a2=new SymbolAxis("Axis 2",new String[]{"A","B"});
    assertTrue(a1.equals(a2));
    a1=new SymbolAxis("Axis 2",new String[]{"C","B"});
    assertFalse(a1.equals(a2));
    a2=new SymbolAxis("Axis 2",new String[]{"C","B"});
    assertTrue(a1.equals(a2));
    a1.setGridBandsVisible(false);
    assertFalse(a1.equals(a2));
    a2.setGridBandsVisible(false);
    assertTrue(a1.equals(a2));
    a1.setGridBandPaint(Color.black);
    assertFalse(a1.equals(a2));
    a2.setGridBandPaint(Color.black);
    assertTrue(a1.equals(a2));
    a1.setGridBandAlternatePaint(Color.red);
    assertFalse(a1.equals(a2));
    a2.setGridBandAlternatePaint(Color.red);
    assertTrue(a1.equals(a2));
  }
}
