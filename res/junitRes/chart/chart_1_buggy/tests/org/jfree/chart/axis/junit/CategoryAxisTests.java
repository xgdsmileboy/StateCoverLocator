package org.jfree.chart.axis.junit;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
/** 
 * Tests for the             {@link CategoryAxis} class.
 */
public class CategoryAxisTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(CategoryAxisTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public CategoryAxisTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    CategoryAxis a1=new CategoryAxis("Test");
    CategoryAxis a2=new CategoryAxis("Test");
    assertTrue(a1.equals(a2));
    a1.setLowerMargin(0.15);
    assertFalse(a1.equals(a2));
    a2.setLowerMargin(0.15);
    assertTrue(a1.equals(a2));
    a1.setUpperMargin(0.15);
    assertFalse(a1.equals(a2));
    a2.setUpperMargin(0.15);
    assertTrue(a1.equals(a2));
    a1.setCategoryMargin(0.15);
    assertFalse(a1.equals(a2));
    a2.setCategoryMargin(0.15);
    assertTrue(a1.equals(a2));
    a1.setMaximumCategoryLabelWidthRatio(0.98f);
    assertFalse(a1.equals(a2));
    a2.setMaximumCategoryLabelWidthRatio(0.98f);
    assertTrue(a1.equals(a2));
    a1.setCategoryLabelPositionOffset(11);
    assertFalse(a1.equals(a2));
    a2.setCategoryLabelPositionOffset(11);
    assertTrue(a1.equals(a2));
    a1.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
    assertFalse(a1.equals(a2));
    a2.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
    assertTrue(a1.equals(a2));
    a1.addCategoryLabelToolTip("Test","Check");
    assertFalse(a1.equals(a2));
    a2.addCategoryLabelToolTip("Test","Check");
    assertTrue(a1.equals(a2));
    a1.setTickLabelFont("C1",new Font("Dialog",Font.PLAIN,21));
    assertFalse(a1.equals(a2));
    a2.setTickLabelFont("C1",new Font("Dialog",Font.PLAIN,21));
    assertTrue(a1.equals(a2));
    a1.setTickLabelPaint("C1",Color.red);
    assertFalse(a1.equals(a2));
    a2.setTickLabelPaint("C1",Color.red);
    assertTrue(a1.equals(a2));
    a1.setTickLabelPaint("C1",new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow));
    assertFalse(a1.equals(a2));
    a2.setTickLabelPaint("C1",new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow));
    assertTrue(a1.equals(a2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    CategoryAxis a1=new CategoryAxis("Test");
    CategoryAxis a2=new CategoryAxis("Test");
    assertTrue(a1.equals(a2));
    int h1=a1.hashCode();
    int h2=a2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    CategoryAxis a1=new CategoryAxis("Test");
    CategoryAxis a2=null;
    try {
      a2=(CategoryAxis)a1.clone();
    }
 catch (    CloneNotSupportedException e) {
      System.err.println("Failed to clone.");
    }
    assertTrue(a1 != a2);
    assertTrue(a1.getClass() == a2.getClass());
    assertTrue(a1.equals(a2));
  }
  /** 
 * Confirm that cloning works.  This test customises the font and paint per category label.
 */
  public void testCloning2(){
    CategoryAxis a1=new CategoryAxis("Test");
    a1.setTickLabelFont("C1",new Font("Dialog",Font.PLAIN,15));
    a1.setTickLabelPaint("C1",new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.white));
    CategoryAxis a2=null;
    try {
      a2=(CategoryAxis)a1.clone();
    }
 catch (    CloneNotSupportedException e) {
      System.err.println("Failed to clone.");
    }
    assertTrue(a1 != a2);
    assertTrue(a1.getClass() == a2.getClass());
    assertTrue(a1.equals(a2));
    a1.setTickLabelFont("C1",null);
    assertFalse(a1.equals(a2));
    a2.setTickLabelFont("C1",null);
    assertTrue(a1.equals(a2));
    a1.setTickLabelPaint("C1",Color.yellow);
    assertFalse(a1.equals(a2));
    a2.setTickLabelPaint("C1",Color.yellow);
    assertTrue(a1.equals(a2));
    a1.addCategoryLabelToolTip("C1","XYZ");
    assertFalse(a1.equals(a2));
    a2.addCategoryLabelToolTip("C1","XYZ");
    assertTrue(a1.equals(a2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    CategoryAxis a1=new CategoryAxis("Test Axis");
    a1.setTickLabelPaint("C1",new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.white));
    CategoryAxis a2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(a1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      a2=(CategoryAxis)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(a1,a2);
  }
}
