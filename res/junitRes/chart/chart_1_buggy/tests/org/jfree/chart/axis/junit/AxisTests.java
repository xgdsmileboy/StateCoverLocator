package org.jfree.chart.axis.junit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.util.RectangleInsets;
/** 
 * Tests for the             {@link Axis} class.
 */
public class AxisTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(AxisTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public AxisTests(  String name){
    super(name);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    CategoryAxis a1=new CategoryAxis("Test");
    a1.setAxisLinePaint(Color.red);
    CategoryAxis a2=null;
    try {
      a2=(CategoryAxis)a1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(a1 != a2);
    assertTrue(a1.getClass() == a2.getClass());
    assertTrue(a1.equals(a2));
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    Axis a1=new CategoryAxis("Test");
    Axis a2=new CategoryAxis("Test");
    assertTrue(a1.equals(a2));
    a1.setVisible(false);
    assertFalse(a1.equals(a2));
    a2.setVisible(false);
    assertTrue(a1.equals(a2));
    a1.setLabel("New Label");
    assertFalse(a1.equals(a2));
    a2.setLabel("New Label");
    assertTrue(a1.equals(a2));
    a1.setLabelFont(new Font("Dialog",Font.PLAIN,8));
    assertFalse(a1.equals(a2));
    a2.setLabelFont(new Font("Dialog",Font.PLAIN,8));
    assertTrue(a1.equals(a2));
    a1.setLabelPaint(new GradientPaint(1.0f,2.0f,Color.white,3.0f,4.0f,Color.black));
    assertFalse(a1.equals(a2));
    a2.setLabelPaint(new GradientPaint(1.0f,2.0f,Color.white,3.0f,4.0f,Color.black));
    assertTrue(a1.equals(a2));
    a1.setLabelInsets(new RectangleInsets(10.0,10.0,10.0,10.0));
    assertFalse(a1.equals(a2));
    a2.setLabelInsets(new RectangleInsets(10.0,10.0,10.0,10.0));
    assertTrue(a1.equals(a2));
    a1.setLabelAngle(1.23);
    assertFalse(a1.equals(a2));
    a2.setLabelAngle(1.23);
    assertTrue(a1.equals(a2));
    a1.setLabelToolTip("123");
    assertFalse(a1.equals(a2));
    a2.setLabelToolTip("123");
    assertTrue(a1.equals(a2));
    a1.setLabelURL("ABC");
    assertFalse(a1.equals(a2));
    a2.setLabelURL("ABC");
    assertTrue(a1.equals(a2));
    a1.setAxisLineVisible(false);
    assertFalse(a1.equals(a2));
    a2.setAxisLineVisible(false);
    assertTrue(a1.equals(a2));
    BasicStroke s=new BasicStroke(1.1f);
    a1.setAxisLineStroke(s);
    assertFalse(a1.equals(a2));
    a2.setAxisLineStroke(s);
    assertTrue(a1.equals(a2));
    a1.setAxisLinePaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.black));
    assertFalse(a1.equals(a2));
    a2.setAxisLinePaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.black));
    assertTrue(a1.equals(a2));
    a1.setTickLabelsVisible(false);
    assertFalse(a1.equals(a2));
    a2.setTickLabelsVisible(false);
    assertTrue(a1.equals(a2));
    a1.setTickLabelFont(new Font("Dialog",Font.PLAIN,12));
    assertFalse(a1.equals(a2));
    a2.setTickLabelFont(new Font("Dialog",Font.PLAIN,12));
    assertTrue(a1.equals(a2));
    a1.setTickLabelPaint(new GradientPaint(1.0f,2.0f,Color.yellow,3.0f,4.0f,Color.black));
    assertFalse(a1.equals(a2));
    a2.setTickLabelPaint(new GradientPaint(1.0f,2.0f,Color.yellow,3.0f,4.0f,Color.black));
    assertTrue(a1.equals(a2));
    a1.setTickLabelInsets(new RectangleInsets(10.0,10.0,10.0,10.0));
    assertFalse(a1.equals(a2));
    a2.setTickLabelInsets(new RectangleInsets(10.0,10.0,10.0,10.0));
    assertTrue(a1.equals(a2));
    a1.setTickMarksVisible(!a1.isTickMarksVisible());
    assertFalse(a1.equals(a2));
    a2.setTickMarksVisible(a1.isTickMarksVisible());
    assertTrue(a1.equals(a2));
    a1.setTickMarkInsideLength(1.23f);
    assertFalse(a1.equals(a2));
    a2.setTickMarkInsideLength(1.23f);
    assertTrue(a1.equals(a2));
    a1.setTickMarkOutsideLength(1.23f);
    assertFalse(a1.equals(a2));
    a2.setTickMarkOutsideLength(1.23f);
    assertTrue(a1.equals(a2));
    a1.setTickMarkStroke(new BasicStroke(2.0f));
    assertFalse(a1.equals(a2));
    a2.setTickMarkStroke(new BasicStroke(2.0f));
    assertTrue(a1.equals(a2));
    a1.setTickMarkPaint(new GradientPaint(1.0f,2.0f,Color.cyan,3.0f,4.0f,Color.black));
    assertFalse(a1.equals(a2));
    a2.setTickMarkPaint(new GradientPaint(1.0f,2.0f,Color.cyan,3.0f,4.0f,Color.black));
    assertTrue(a1.equals(a2));
    a1.setFixedDimension(3.21f);
    assertFalse(a1.equals(a2));
    a2.setFixedDimension(3.21f);
    assertTrue(a1.equals(a2));
    a1.setMinorTickMarksVisible(true);
    assertFalse(a1.equals(a2));
    a2.setMinorTickMarksVisible(true);
    assertTrue(a1.equals(a2));
    a1.setMinorTickMarkInsideLength(1.23f);
    assertFalse(a1.equals(a2));
    a2.setMinorTickMarkInsideLength(1.23f);
    assertTrue(a1.equals(a2));
    a1.setMinorTickMarkOutsideLength(3.21f);
    assertFalse(a1.equals(a2));
    a2.setMinorTickMarkOutsideLength(3.21f);
    assertTrue(a1.equals(a2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    Axis a1=new CategoryAxis("Test");
    Axis a2=new CategoryAxis("Test");
    assertTrue(a1.equals(a2));
    int h1=a1.hashCode();
    int h2=a2.hashCode();
    assertEquals(h1,h2);
  }
}
