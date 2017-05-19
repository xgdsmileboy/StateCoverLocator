package org.jfree.chart.annotations.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link CategoryTextAnnotation} class.
 */
public class CategoryTextAnnotationTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(CategoryTextAnnotationTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public CategoryTextAnnotationTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    CategoryTextAnnotation a1=new CategoryTextAnnotation("Test","Category",1.0);
    CategoryTextAnnotation a2=new CategoryTextAnnotation("Test","Category",1.0);
    assertTrue(a1.equals(a2));
    a1.setCategory("Category 2");
    assertFalse(a1.equals(a2));
    a2.setCategory("Category 2");
    assertTrue(a1.equals(a2));
    a1.setCategoryAnchor(CategoryAnchor.START);
    assertFalse(a1.equals(a2));
    a2.setCategoryAnchor(CategoryAnchor.START);
    assertTrue(a1.equals(a2));
    a1.setValue(0.15);
    assertFalse(a1.equals(a2));
    a2.setValue(0.15);
    assertTrue(a1.equals(a2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    CategoryTextAnnotation a1=new CategoryTextAnnotation("Test","Category",1.0);
    CategoryTextAnnotation a2=new CategoryTextAnnotation("Test","Category",1.0);
    assertTrue(a1.equals(a2));
    int h1=a1.hashCode();
    int h2=a2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    CategoryTextAnnotation a1=new CategoryTextAnnotation("Test","Category",1.0);
    CategoryTextAnnotation a2=null;
    try {
      a2=(CategoryTextAnnotation)a1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(a1 != a2);
    assertTrue(a1.getClass() == a2.getClass());
    assertTrue(a1.equals(a2));
  }
  /** 
 * Checks that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    CategoryTextAnnotation a1=new CategoryTextAnnotation("Test","Category",1.0);
    assertTrue(a1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    CategoryTextAnnotation a1=new CategoryTextAnnotation("Test","Category",1.0);
    CategoryTextAnnotation a2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(a1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      a2=(CategoryTextAnnotation)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(a1,a2);
  }
}
