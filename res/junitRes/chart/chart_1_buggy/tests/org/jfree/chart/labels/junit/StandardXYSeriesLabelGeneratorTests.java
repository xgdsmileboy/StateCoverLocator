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
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
/** 
 * Tests for the             {@link StandardXYSeriesLabelGenerator} class.
 */
public class StandardXYSeriesLabelGeneratorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StandardXYSeriesLabelGeneratorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StandardXYSeriesLabelGeneratorTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the generalLabel() method.
 */
  public void testGenerateLabel(){
    StandardXYSeriesLabelGenerator g=new StandardXYSeriesLabelGenerator("Series {0}");
    XYSeriesCollection dataset=new XYSeriesCollection();
    dataset.addSeries(new XYSeries("1"));
    dataset.addSeries(new XYSeries("2"));
    assertEquals("Series 1",g.generateLabel(dataset,0));
    assertEquals("Series 2",g.generateLabel(dataset,1));
  }
  /** 
 * Some checks for the equals() method.
 */
  public void testEquals(){
    StandardXYSeriesLabelGenerator g1=new StandardXYSeriesLabelGenerator("Series {0}");
    StandardXYSeriesLabelGenerator g2=new StandardXYSeriesLabelGenerator("Series {0}");
    assertTrue(g1.equals(g2));
    assertTrue(g2.equals(g1));
    g1=new StandardXYSeriesLabelGenerator("{1}");
    assertFalse(g1.equals(g2));
    g2=new StandardXYSeriesLabelGenerator("{1}");
    assertTrue(g1.equals(g2));
  }
  /** 
 * Simple check that hashCode is implemented.
 */
  public void testHashCode(){
    StandardXYSeriesLabelGenerator g1=new StandardXYSeriesLabelGenerator();
    StandardXYSeriesLabelGenerator g2=new StandardXYSeriesLabelGenerator();
    assertTrue(g1.equals(g2));
    assertTrue(g1.hashCode() == g2.hashCode());
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    StandardXYSeriesLabelGenerator g1=new StandardXYSeriesLabelGenerator("Series {0}");
    StandardXYSeriesLabelGenerator g2=null;
    try {
      g2=(StandardXYSeriesLabelGenerator)g1.clone();
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
    StandardXYSeriesLabelGenerator g1=new StandardXYSeriesLabelGenerator("Series {0}");
    assertTrue(g1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    StandardXYSeriesLabelGenerator g1=new StandardXYSeriesLabelGenerator("Series {0}");
    StandardXYSeriesLabelGenerator g2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(g1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      g2=(StandardXYSeriesLabelGenerator)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(g1,g2);
  }
}
