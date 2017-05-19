package org.jfree.data.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.KeyedObjects2D;
import org.jfree.data.UnknownKeyException;
/** 
 * Tests for the             {@link KeyedObjects2D} class.
 */
public class KeyedObjects2DTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(KeyedObjects2DTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public KeyedObjects2DTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the equals() method.
 */
  public void testEquals(){
    KeyedObjects2D k1=new KeyedObjects2D();
    KeyedObjects2D k2=new KeyedObjects2D();
    assertTrue(k1.equals(k2));
    assertTrue(k2.equals(k1));
    k1.addObject(new Integer(99),"R1","C1");
    assertFalse(k1.equals(k2));
    k2.addObject(new Integer(99),"R1","C1");
    assertTrue(k1.equals(k2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    KeyedObjects2D o1=new KeyedObjects2D();
    o1.setObject(new Integer(1),"V1","C1");
    o1.setObject(null,"V2","C1");
    o1.setObject(new Integer(3),"V3","C2");
    KeyedObjects2D o2=null;
    try {
      o2=(KeyedObjects2D)o1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(o1 != o2);
    assertTrue(o1.getClass() == o2.getClass());
    assertTrue(o1.equals(o2));
    o1.addObject("XX","R1","C1");
    assertFalse(o1.equals(o2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    KeyedObjects2D ko2D1=new KeyedObjects2D();
    ko2D1.addObject(new Double(234.2),"Row1","Col1");
    ko2D1.addObject(null,"Row1","Col2");
    ko2D1.addObject(new Double(345.9),"Row2","Col1");
    ko2D1.addObject(new Double(452.7),"Row2","Col2");
    KeyedObjects2D ko2D2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(ko2D1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      ko2D2=(KeyedObjects2D)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(ko2D1,ko2D2);
  }
  /** 
 * Some checks for the getValue(int, int) method.
 */
  public void testGetValueByIndex(){
    KeyedObjects2D data=new KeyedObjects2D();
    data.addObject("Obj1","R1","C1");
    data.addObject("Obj2","R2","C2");
    assertEquals("Obj1",data.getObject(0,0));
    assertEquals("Obj2",data.getObject(1,1));
    assertNull(data.getObject(0,1));
    assertNull(data.getObject(1,0));
    boolean pass=false;
    try {
      data.getObject(-1,0);
    }
 catch (    IndexOutOfBoundsException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      data.getObject(0,-1);
    }
 catch (    IndexOutOfBoundsException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      data.getObject(2,0);
    }
 catch (    IndexOutOfBoundsException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      data.getObject(0,2);
    }
 catch (    IndexOutOfBoundsException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getValue(Comparable, Comparable) method.
 */
  public void testGetValueByKey(){
    KeyedObjects2D data=new KeyedObjects2D();
    data.addObject("Obj1","R1","C1");
    data.addObject("Obj2","R2","C2");
    assertEquals("Obj1",data.getObject("R1","C1"));
    assertEquals("Obj2",data.getObject("R2","C2"));
    assertNull(data.getObject("R1","C2"));
    assertNull(data.getObject("R2","C1"));
    boolean pass=false;
    try {
      data.getObject("XX","C1");
    }
 catch (    UnknownKeyException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      data.getObject("R1","XX");
    }
 catch (    UnknownKeyException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      data.getObject("XX","C1");
    }
 catch (    UnknownKeyException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      data.getObject("R1","XX");
    }
 catch (    UnknownKeyException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the setObject(Object, Comparable, Comparable) method.
 */
  public void testSetObject(){
    KeyedObjects2D data=new KeyedObjects2D();
    data.setObject("Obj1","R1","C1");
    data.setObject("Obj2","R2","C2");
    assertEquals("Obj1",data.getObject("R1","C1"));
    assertEquals("Obj2",data.getObject("R2","C2"));
    assertNull(data.getObject("R1","C2"));
    assertNull(data.getObject("R2","C1"));
    data.setObject("ABC","R2","C2");
    assertEquals("ABC",data.getObject("R2","C2"));
    boolean pass=false;
    try {
      data.setObject("X",null,"C1");
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      data.setObject("X","R1",null);
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the removeRow(int) method.
 */
  public void testRemoveRowByIndex(){
    KeyedObjects2D data=new KeyedObjects2D();
    data.setObject("Obj1","R1","C1");
    data.setObject("Obj2","R2","C2");
    data.removeRow(0);
    assertEquals(1,data.getRowCount());
    assertEquals("Obj2",data.getObject(0,1));
    boolean pass=false;
    try {
      data.removeRow(-1);
    }
 catch (    IndexOutOfBoundsException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      data.removeRow(data.getRowCount());
    }
 catch (    IndexOutOfBoundsException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the removeColumn(int) method.
 */
  public void testRemoveColumnByIndex(){
    KeyedObjects2D data=new KeyedObjects2D();
    data.setObject("Obj1","R1","C1");
    data.setObject("Obj2","R2","C2");
    data.removeColumn(0);
    assertEquals(1,data.getColumnCount());
    assertEquals("Obj2",data.getObject(1,0));
    boolean pass=false;
    try {
      data.removeColumn(-1);
    }
 catch (    IndexOutOfBoundsException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      data.removeColumn(data.getColumnCount());
    }
 catch (    IndexOutOfBoundsException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the removeRow(Comparable) method.
 */
  public void testRemoveRowByKey(){
    KeyedObjects2D data=new KeyedObjects2D();
    data.setObject("Obj1","R1","C1");
    data.setObject("Obj2","R2","C2");
    data.removeRow("R2");
    assertEquals(1,data.getRowCount());
    assertEquals("Obj1",data.getObject(0,0));
    boolean pass=false;
    try {
      data.removeRow("XXX");
    }
 catch (    UnknownKeyException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      data.removeRow(null);
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the removeColumn(Comparable) method.
 */
  public void testRemoveColumnByKey(){
    KeyedObjects2D data=new KeyedObjects2D();
    data.setObject("Obj1","R1","C1");
    data.setObject("Obj2","R2","C2");
    data.removeColumn("C2");
    assertEquals(1,data.getColumnCount());
    assertEquals("Obj1",data.getObject(0,0));
    boolean pass=false;
    try {
      data.removeColumn("XXX");
    }
 catch (    UnknownKeyException e) {
      pass=true;
    }
    assertTrue(pass);
    pass=false;
    try {
      data.removeColumn(null);
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * A simple check for the removeValue() method.
 */
  public void testRemoveValue(){
    KeyedObjects2D data=new KeyedObjects2D();
    data.setObject("Obj1","R1","C1");
    data.setObject("Obj2","R2","C2");
    data.removeObject("R2","C2");
    assertEquals(1,data.getRowCount());
    assertEquals(1,data.getColumnCount());
    assertEquals("Obj1",data.getObject(0,0));
  }
}
