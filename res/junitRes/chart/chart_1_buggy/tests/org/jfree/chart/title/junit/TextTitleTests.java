package org.jfree.chart.title.junit;
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
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.util.HorizontalAlignment;
/** 
 * Tests for the             {@link TextTitle} class.
 */
public class TextTitleTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(TextTitleTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public TextTitleTests(  String name){
    super(name);
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    TextTitle t1=new TextTitle();
    TextTitle t2=new TextTitle();
    assertEquals(t1,t2);
    t1.setText("Test 1");
    assertFalse(t1.equals(t2));
    t2.setText("Test 1");
    assertTrue(t1.equals(t2));
    Font f=new Font("SansSerif",Font.PLAIN,15);
    t1.setFont(f);
    assertFalse(t1.equals(t2));
    t2.setFont(f);
    assertTrue(t1.equals(t2));
    t1.setTextAlignment(HorizontalAlignment.RIGHT);
    assertFalse(t1.equals(t2));
    t2.setTextAlignment(HorizontalAlignment.RIGHT);
    assertTrue(t1.equals(t2));
    t1.setPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    assertFalse(t1.equals(t2));
    t2.setPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    assertTrue(t1.equals(t2));
    t1.setBackgroundPaint(new GradientPaint(4.0f,3.0f,Color.red,2.0f,1.0f,Color.blue));
    assertFalse(t1.equals(t2));
    t2.setBackgroundPaint(new GradientPaint(4.0f,3.0f,Color.red,2.0f,1.0f,Color.blue));
    assertTrue(t1.equals(t2));
    t1.setMaximumLinesToDisplay(3);
    assertFalse(t1.equals(t2));
    t2.setMaximumLinesToDisplay(3);
    assertTrue(t1.equals(t2));
    t1.setToolTipText("TTT");
    assertFalse(t1.equals(t2));
    t2.setToolTipText("TTT");
    assertTrue(t1.equals(t2));
    t1.setURLText(("URL"));
    assertFalse(t1.equals(t2));
    t2.setURLText(("URL"));
    assertTrue(t1.equals(t2));
    t1.setExpandToFitSpace(!t1.getExpandToFitSpace());
    assertFalse(t1.equals(t2));
    t2.setExpandToFitSpace(!t2.getExpandToFitSpace());
    assertTrue(t1.equals(t2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    TextTitle t1=new TextTitle();
    TextTitle t2=new TextTitle();
    assertTrue(t1.equals(t2));
    int h1=t1.hashCode();
    int h2=t2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    TextTitle t1=new TextTitle();
    TextTitle t2=null;
    try {
      t2=(TextTitle)t1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(t1 != t2);
    assertTrue(t1.getClass() == t2.getClass());
    assertTrue(t1.equals(t2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    TextTitle t1=new TextTitle("Test");
    TextTitle t2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(t1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      t2=(TextTitle)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(t1,t2);
  }
}
