package org.jfree.chart.renderer.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.renderer.AreaRendererEndType;
/** 
 * Tests for the             {@link AreaRendererEndType} class.
 */
public class AreaRendererEndTypeTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(AreaRendererEndTypeTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public AreaRendererEndTypeTests(  String name){
    super(name);
  }
  /** 
 * A test for the equals() method.
 */
  public void testEquals(){
    assertEquals(AreaRendererEndType.LEVEL,AreaRendererEndType.LEVEL);
    assertEquals(AreaRendererEndType.TAPER,AreaRendererEndType.TAPER);
    assertEquals(AreaRendererEndType.TRUNCATE,AreaRendererEndType.TRUNCATE);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    AreaRendererEndType t1=AreaRendererEndType.TAPER;
    AreaRendererEndType t2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(t1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      t2=(AreaRendererEndType)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(t1,t2);
    boolean same=t1 == t2;
    assertEquals(true,same);
  }
}
