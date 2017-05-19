package org.jfree.chart.block.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.block.FlowArrangement;
import org.jfree.chart.util.HorizontalAlignment;
import org.jfree.chart.util.VerticalAlignment;
/** 
 * Tests for the             {@link FlowArrangement} class.
 */
public class FlowArrangementTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(FlowArrangementTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public FlowArrangementTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals() method can distinguish all the required fields.
 */
  public void testEquals(){
    FlowArrangement f1=new FlowArrangement(HorizontalAlignment.LEFT,VerticalAlignment.TOP,1.0,2.0);
    FlowArrangement f2=new FlowArrangement(HorizontalAlignment.LEFT,VerticalAlignment.TOP,1.0,2.0);
    assertTrue(f1.equals(f2));
    assertTrue(f2.equals(f1));
    f1=new FlowArrangement(HorizontalAlignment.RIGHT,VerticalAlignment.TOP,1.0,2.0);
    assertFalse(f1.equals(f2));
    f2=new FlowArrangement(HorizontalAlignment.RIGHT,VerticalAlignment.TOP,1.0,2.0);
    assertTrue(f1.equals(f2));
    f1=new FlowArrangement(HorizontalAlignment.RIGHT,VerticalAlignment.BOTTOM,1.0,2.0);
    assertFalse(f1.equals(f2));
    f2=new FlowArrangement(HorizontalAlignment.RIGHT,VerticalAlignment.BOTTOM,1.0,2.0);
    assertTrue(f1.equals(f2));
    f1=new FlowArrangement(HorizontalAlignment.RIGHT,VerticalAlignment.BOTTOM,1.1,2.0);
    assertFalse(f1.equals(f2));
    f2=new FlowArrangement(HorizontalAlignment.RIGHT,VerticalAlignment.BOTTOM,1.1,2.0);
    assertTrue(f1.equals(f2));
    f1=new FlowArrangement(HorizontalAlignment.RIGHT,VerticalAlignment.BOTTOM,1.1,2.2);
    assertFalse(f1.equals(f2));
    f2=new FlowArrangement(HorizontalAlignment.RIGHT,VerticalAlignment.BOTTOM,1.1,2.2);
    assertTrue(f1.equals(f2));
  }
  /** 
 * Immutable - cloning is not necessary.
 */
  public void testCloning(){
    FlowArrangement f1=new FlowArrangement();
    assertFalse(f1 instanceof Cloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    FlowArrangement f1=new FlowArrangement(HorizontalAlignment.LEFT,VerticalAlignment.TOP,1.0,2.0);
    FlowArrangement f2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(f1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      f2=(FlowArrangement)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(f1,f2);
  }
}
