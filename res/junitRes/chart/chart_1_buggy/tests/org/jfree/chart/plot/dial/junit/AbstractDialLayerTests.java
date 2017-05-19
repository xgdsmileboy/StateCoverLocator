package org.jfree.chart.plot.dial.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.plot.dial.AbstractDialLayer;
import org.jfree.chart.plot.dial.DialCap;
/** 
 * Tests for the             {@link AbstractDialLayer} class.
 */
public class AbstractDialLayerTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(AbstractDialLayerTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public AbstractDialLayerTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    DialCap c1=new DialCap();
    DialCap c2=new DialCap();
    assertTrue(c1.equals(c2));
    c1.setVisible(false);
    assertFalse(c1.equals(c2));
    c2.setVisible(false);
    assertTrue(c1.equals(c2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    DialCap c1=new DialCap();
    DialCap c2=null;
    try {
      c2=(DialCap)c1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(c1 != c2);
    assertTrue(c1.getClass() == c2.getClass());
    assertTrue(c1.equals(c2));
    MyDialLayerChangeListener l1=new MyDialLayerChangeListener();
    c1.addChangeListener(l1);
    assertTrue(c1.hasListener(l1));
    assertFalse(c2.hasListener(l1));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    DialCap c1=new DialCap();
    DialCap c2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(c1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      c2=(DialCap)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(c1,c2);
    MyDialLayerChangeListener l1=new MyDialLayerChangeListener();
    c1.addChangeListener(l1);
    assertTrue(c1.hasListener(l1));
    assertFalse(c2.hasListener(l1));
  }
}
