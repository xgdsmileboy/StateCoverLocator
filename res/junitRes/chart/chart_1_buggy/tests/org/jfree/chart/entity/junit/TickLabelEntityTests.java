package org.jfree.chart.entity.junit;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.entity.TickLabelEntity;
/** 
 * Tests for the             {@link TickLabelEntity} class.
 */
public class TickLabelEntityTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(TickLabelEntityTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public TickLabelEntityTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    TickLabelEntity e1=new TickLabelEntity(new Rectangle2D.Double(1.0,2.0,3.0,4.0),"ToolTip","URL");
    TickLabelEntity e2=new TickLabelEntity(new Rectangle2D.Double(1.0,2.0,3.0,4.0),"ToolTip","URL");
    assertTrue(e1.equals(e2));
    e1.setArea(new Rectangle2D.Double(4.0,3.0,2.0,1.0));
    assertFalse(e1.equals(e2));
    e2.setArea(new Rectangle2D.Double(4.0,3.0,2.0,1.0));
    assertTrue(e1.equals(e2));
    e1.setToolTipText("New ToolTip");
    assertFalse(e1.equals(e2));
    e2.setToolTipText("New ToolTip");
    assertTrue(e1.equals(e2));
    e1.setURLText("New URL");
    assertFalse(e1.equals(e2));
    e2.setURLText("New URL");
    assertTrue(e1.equals(e2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    TickLabelEntity e1=new TickLabelEntity(new Rectangle2D.Double(1.0,2.0,3.0,4.0),"ToolTip","URL");
    TickLabelEntity e2=null;
    try {
      e2=(TickLabelEntity)e1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(e1 != e2);
    assertTrue(e1.getClass() == e2.getClass());
    assertTrue(e1.equals(e2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    TickLabelEntity e1=new TickLabelEntity(new Rectangle2D.Double(1.0,2.0,3.0,4.0),"ToolTip","URL");
    TickLabelEntity e2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(e1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      e2=(TickLabelEntity)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(e1,e2);
  }
}
