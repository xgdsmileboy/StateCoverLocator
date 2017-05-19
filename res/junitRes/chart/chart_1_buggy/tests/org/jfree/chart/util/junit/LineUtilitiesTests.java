package org.jfree.chart.util.junit;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.util.LineUtilities;
/** 
 * Tests for the             {@link LineUtilities} class.
 */
public class LineUtilitiesTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(LineUtilitiesTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public LineUtilitiesTests(  String name){
    super(name);
  }
  private boolean lineEquals(  Line2D line,  double x1,  double y1,  double x2,  double y2){
    boolean result=true;
    double epsilon=0.0000000001;
    if (Math.abs(line.getX1() - x1) > epsilon)     result=false;
    if (Math.abs(line.getY1() - y1) > epsilon)     result=false;
    if (Math.abs(line.getX2() - x2) > epsilon)     result=false;
    if (Math.abs(line.getY2() - y2) > epsilon)     result=false;
    if (result == false) {
      System.out.println(line.getX1() + ", " + line.getY1()+ ", "+ line.getX2()+ ", "+ line.getY2());
    }
    return result;
  }
  public void testClipLine(){
    Rectangle2D rect=new Rectangle2D.Double(1.0,1.0,1.0,1.0);
    Line2D line=new Line2D.Double();
    assertFalse(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,0.0,0.0,0.0,0.0));
    line.setLine(0.5,0.5,0.6,0.6);
    assertFalse(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,0.5,0.5,0.6,0.6));
    line.setLine(0.5,0.5,1.6,0.6);
    assertFalse(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,0.5,0.5,1.6,0.6));
    line.setLine(0.5,0.5,2.6,0.6);
    assertFalse(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,0.5,0.5,2.6,0.6));
    line.setLine(0.5,0.5,0.6,1.6);
    assertFalse(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,0.5,0.5,0.6,1.6));
    line.setLine(0.5,0.5,1.6,1.6);
    assertTrue(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,1.0,1.0,1.6,1.6));
    line.setLine(0.5,0.5,2.6,1.6);
    assertTrue(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,1.4545454545454546,1.0,2.0,1.2857142857142858));
    line.setLine(0.5,0.5,0.5,2.6);
    assertFalse(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,0.5,0.5,0.5,2.6));
    line.setLine(0.5,0.5,1.5,2.6);
    assertTrue(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,1.0,1.55,1.2142857142857142,2.0));
    line.setLine(0.5,0.5,2.5,2.6);
    assertTrue(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,1.0,1.025,1.9285714285714284,2.0));
    line.setLine(0.5,0.5,1.5,1.5);
    assertTrue(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,1.0,1.0,1.5,1.5));
    line.setLine(2.5,1.0,1.5,1.5);
    assertTrue(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,2.0,1.25,1.5,1.5));
    line.setLine(1.5,1.5,2.5,1.0);
    assertTrue(LineUtilities.clipLine(line,rect));
    assertTrue(lineEquals(line,1.5,1.5,2.0,1.25));
  }
}
