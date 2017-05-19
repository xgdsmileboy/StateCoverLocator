package org.jfree.chart.junit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
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
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
/** 
 * Tests for the             {@link LegendItemCollection} class.
 */
public class LegendItemCollectionTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(LegendItemCollectionTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public LegendItemCollectionTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    LegendItemCollection c1=new LegendItemCollection();
    LegendItemCollection c2=new LegendItemCollection();
    assertTrue(c1.equals(c2));
    assertTrue(c2.equals(c1));
    LegendItem item1=new LegendItem("Label","Description","ToolTip","URL",true,new Rectangle2D.Double(1.0,2.0,3.0,4.0),true,Color.red,true,Color.blue,new BasicStroke(1.2f),true,new Line2D.Double(1.0,2.0,3.0,4.0),new BasicStroke(2.1f),Color.green);
    LegendItem item2=new LegendItem("Label","Description","ToolTip","URL",true,new Rectangle2D.Double(1.0,2.0,3.0,4.0),true,Color.red,true,Color.blue,new BasicStroke(1.2f),true,new Line2D.Double(1.0,2.0,3.0,4.0),new BasicStroke(2.1f),Color.green);
    c1.add(item1);
    c2.add(item2);
    assertTrue(c1.equals(c2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    LegendItemCollection c1=new LegendItemCollection();
    c1.add(new LegendItem("Item","Description","ToolTip","URL",new Rectangle2D.Double(1.0,2.0,3.0,4.0),Color.red));
    LegendItemCollection c2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(c1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      c2=(LegendItemCollection)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(c1,c2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    LegendItemCollection c1=new LegendItemCollection();
    LegendItem item1=new LegendItem("Item 1");
    c1.add(item1);
    LegendItemCollection c2=null;
    try {
      c2=(LegendItemCollection)c1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(c1 != c2);
    assertTrue(c1.getClass() == c2.getClass());
    assertTrue(c1.equals(c2));
    Rectangle2D item1Shape=(Rectangle2D)item1.getShape();
    item1Shape.setRect(1.0,2.0,3.0,4.0);
    assertFalse(c1.equals(c2));
  }
}
