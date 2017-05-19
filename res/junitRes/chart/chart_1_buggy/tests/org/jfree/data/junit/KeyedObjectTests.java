package org.jfree.data.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.KeyedObject;
import org.jfree.data.pie.DefaultPieDataset;
/** 
 * Tests for the             {@link KeyedObject} class.
 */
public class KeyedObjectTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(KeyedObjectTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public KeyedObjectTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    KeyedObject ko1=new KeyedObject("Test","Object");
    KeyedObject ko2=new KeyedObject("Test","Object");
    assertTrue(ko1.equals(ko2));
    assertTrue(ko2.equals(ko1));
    ko1=new KeyedObject("Test 1","Object");
    ko2=new KeyedObject("Test 2","Object");
    assertFalse(ko1.equals(ko2));
    ko1=new KeyedObject("Test","Object 1");
    ko2=new KeyedObject("Test","Object 2");
    assertFalse(ko1.equals(ko2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    KeyedObject ko1=new KeyedObject("Test","Object");
    KeyedObject ko2=null;
    try {
      ko2=(KeyedObject)ko1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(ko1 != ko2);
    assertTrue(ko1.getClass() == ko2.getClass());
    assertTrue(ko1.equals(ko2));
  }
  /** 
 * Confirm special features of cloning.
 */
  public void testCloning2(){
    Object obj1=new ArrayList();
    KeyedObject ko1=new KeyedObject("Test",obj1);
    KeyedObject ko2=null;
    try {
      ko2=(KeyedObject)ko1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(ko1 != ko2);
    assertTrue(ko1.getClass() == ko2.getClass());
    assertTrue(ko1.equals(ko2));
    assertTrue(ko2.getObject() == obj1);
    obj1=new DefaultPieDataset();
    ko1=new KeyedObject("Test",obj1);
    ko2=null;
    try {
      ko2=(KeyedObject)ko1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(ko1 != ko2);
    assertTrue(ko1.getClass() == ko2.getClass());
    assertTrue(ko1.equals(ko2));
    assertTrue(ko2.getObject() != obj1);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    KeyedObject ko1=new KeyedObject("Test","Object");
    KeyedObject ko2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(ko1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      ko2=(KeyedObject)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(ko1,ko2);
  }
}
