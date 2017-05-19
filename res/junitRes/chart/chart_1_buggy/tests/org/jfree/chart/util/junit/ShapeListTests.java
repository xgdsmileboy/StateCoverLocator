package org.jfree.chart.util.junit;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.util.ShapeList;
/** 
 * Some tests for the             {@link ShapeList} class.
 */
public class ShapeListTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(ShapeListTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public ShapeListTests(  final String name){
    super(name);
  }
  /** 
 * Tests the equals() method.
 */
  public void testEquals(){
    ShapeList l1=new ShapeList();
    l1.setShape(0,new Rectangle(1,2,3,4));
    l1.setShape(1,new Line2D.Double(1.0,2.0,3.0,4.0));
    l1.setShape(2,null);
    ShapeList l2=new ShapeList();
    l2.setShape(0,new Rectangle(1,2,3,4));
    l2.setShape(1,new Line2D.Double(1.0,2.0,3.0,4.0));
    l2.setShape(2,null);
    assertTrue(l1.equals(l2));
    assertTrue(l2.equals(l2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    ShapeList l1=new ShapeList();
    l1.setShape(0,new Rectangle(1,2,3,4));
    l1.setShape(1,new Line2D.Double(1.0,2.0,3.0,4.0));
    l1.setShape(2,null);
    ShapeList l2=null;
    try {
      l2=(ShapeList)l1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(l1 != l2);
    assertTrue(l1.getClass() == l2.getClass());
    assertTrue(l1.equals(l2));
    l2.setShape(0,new Rectangle(5,6,7,8));
    assertFalse(l1.equals(l2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    ShapeList l1=new ShapeList();
    l1.setShape(0,new Rectangle(1,2,3,4));
    l1.setShape(1,new Line2D.Double(1.0,2.0,3.0,4.0));
    l1.setShape(2,null);
    ShapeList l2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(l1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      l2=(ShapeList)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(l1,l2);
  }
}
