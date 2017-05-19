package org.jfree.chart.urls.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link StandardXYURLGenerator} class.
 */
public class StandardXYURLGeneratorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StandardXYURLGeneratorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StandardXYURLGeneratorTests(  String name){
    super(name);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    StandardXYURLGenerator g1=new StandardXYURLGenerator("index.html?");
    StandardXYURLGenerator g2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(g1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      g2=(StandardXYURLGenerator)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(g1,g2);
  }
  /** 
 * Checks that the class does not implement PublicCloneable (the generator is immutable).
 */
  public void testPublicCloneable(){
    StandardXYURLGenerator g1=new StandardXYURLGenerator("index.html?");
    assertFalse(g1 instanceof PublicCloneable);
  }
}
