package org.jfree.chart.annotations.junit;
import java.awt.Image;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYImageAnnotation;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.RectangleAnchor;
/** 
 * Tests for the             {@link XYImageAnnotation} class.
 */
public class XYImageAnnotationTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(XYImageAnnotationTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public XYImageAnnotationTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    Image image=JFreeChart.INFO.getLogo();
    XYImageAnnotation a1=new XYImageAnnotation(10.0,20.0,image);
    XYImageAnnotation a2=new XYImageAnnotation(10.0,20.0,image);
    assertTrue(a1.equals(a2));
    a1=new XYImageAnnotation(10.0,20.0,image,RectangleAnchor.LEFT);
    assertFalse(a1.equals(a2));
    a2=new XYImageAnnotation(10.0,20.0,image,RectangleAnchor.LEFT);
    assertTrue(a1.equals(a2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    Image image=JFreeChart.INFO.getLogo();
    XYImageAnnotation a1=new XYImageAnnotation(10.0,20.0,image);
    XYImageAnnotation a2=new XYImageAnnotation(10.0,20.0,image);
    assertTrue(a1.equals(a2));
    int h1=a1.hashCode();
    int h2=a2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    XYImageAnnotation a1=new XYImageAnnotation(10.0,20.0,JFreeChart.INFO.getLogo());
    XYImageAnnotation a2=null;
    try {
      a2=(XYImageAnnotation)a1.clone();
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
    XYImageAnnotation a1=new XYImageAnnotation(10.0,20.0,JFreeChart.INFO.getLogo());
    assertTrue(a1 instanceof PublicCloneable);
  }
}
