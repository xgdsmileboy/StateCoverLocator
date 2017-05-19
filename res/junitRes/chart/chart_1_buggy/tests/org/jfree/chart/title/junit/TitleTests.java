package org.jfree.chart.title.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.chart.util.HorizontalAlignment;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.VerticalAlignment;
/** 
 * Tests for the abstract             {@link Title} class.
 */
public class TitleTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(TitleTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public TitleTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the equals() method.
 */
  public void testEquals(){
    Title t1=new TextTitle();
    Title t2=new TextTitle();
    assertEquals(t1,t2);
    t1.setPosition(RectangleEdge.LEFT);
    assertFalse(t1.equals(t2));
    t2.setPosition(RectangleEdge.LEFT);
    assertTrue(t1.equals(t2));
    t1.setHorizontalAlignment(HorizontalAlignment.RIGHT);
    assertFalse(t1.equals(t2));
    t2.setHorizontalAlignment(HorizontalAlignment.RIGHT);
    assertTrue(t1.equals(t2));
    t1.setVerticalAlignment(VerticalAlignment.BOTTOM);
    assertFalse(t1.equals(t2));
    t2.setVerticalAlignment(VerticalAlignment.BOTTOM);
    assertTrue(t1.equals(t2));
    t1.setVisible(false);
    assertFalse(t1.equals(t2));
    t2.setVisible(false);
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
}
