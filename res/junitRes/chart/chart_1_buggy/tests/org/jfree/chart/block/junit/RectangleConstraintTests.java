package org.jfree.chart.block.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.block.LengthConstraintType;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.util.Size2D;
import org.jfree.data.Range;
/** 
 * Tests for the             {@link RectangleConstraint} class.
 */
public class RectangleConstraintTests extends TestCase {
  private static final double EPSILON=0.0000000001;
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(RectangleConstraintTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public RectangleConstraintTests(  String name){
    super(name);
  }
  /** 
 * Run some checks on the constrained size calculation.
 */
  public void testCalculateConstrainedSize(){
    Size2D s;
    RectangleConstraint c1=RectangleConstraint.NONE;
    s=c1.calculateConstrainedSize(new Size2D(1.2,3.4));
    assertEquals(s.width,1.2,EPSILON);
    assertEquals(s.height,3.4,EPSILON);
    RectangleConstraint c2=new RectangleConstraint(0.0,new Range(0.0,0.0),LengthConstraintType.NONE,0.0,new Range(2.0,3.0),LengthConstraintType.RANGE);
    s=c2.calculateConstrainedSize(new Size2D(1.2,3.4));
    assertEquals(s.width,1.2,EPSILON);
    assertEquals(s.height,3.0,EPSILON);
    RectangleConstraint c3=new RectangleConstraint(0.0,null,LengthConstraintType.NONE,9.9,null,LengthConstraintType.FIXED);
    s=c3.calculateConstrainedSize(new Size2D(1.2,3.4));
    assertEquals(s.width,1.2,EPSILON);
    assertEquals(s.height,9.9,EPSILON);
    RectangleConstraint c4=new RectangleConstraint(0.0,new Range(2.0,3.0),LengthConstraintType.RANGE,0.0,new Range(0.0,0.0),LengthConstraintType.NONE);
    s=c4.calculateConstrainedSize(new Size2D(1.2,3.4));
    assertEquals(s.width,2.0,EPSILON);
    assertEquals(s.height,3.4,EPSILON);
    RectangleConstraint c5=new RectangleConstraint(0.0,new Range(2.0,3.0),LengthConstraintType.RANGE,0.0,new Range(2.0,3.0),LengthConstraintType.RANGE);
    s=c5.calculateConstrainedSize(new Size2D(1.2,3.4));
    assertEquals(s.width,2.0,EPSILON);
    assertEquals(s.height,3.0,EPSILON);
    RectangleConstraint c6=new RectangleConstraint(0.0,null,LengthConstraintType.NONE,9.9,null,LengthConstraintType.FIXED);
    s=c6.calculateConstrainedSize(new Size2D(1.2,3.4));
    assertEquals(s.width,1.2,EPSILON);
    assertEquals(s.height,9.9,EPSILON);
    RectangleConstraint c7=RectangleConstraint.NONE;
    s=c7.calculateConstrainedSize(new Size2D(1.2,3.4));
    assertEquals(s.width,1.2,EPSILON);
    assertEquals(s.height,3.4,EPSILON);
    RectangleConstraint c8=new RectangleConstraint(0.0,new Range(0.0,0.0),LengthConstraintType.NONE,0.0,new Range(2.0,3.0),LengthConstraintType.RANGE);
    s=c8.calculateConstrainedSize(new Size2D(1.2,3.4));
    assertEquals(s.width,1.2,EPSILON);
    assertEquals(s.height,3.0,EPSILON);
    RectangleConstraint c9=new RectangleConstraint(0.0,null,LengthConstraintType.NONE,9.9,null,LengthConstraintType.FIXED);
    s=c9.calculateConstrainedSize(new Size2D(1.2,3.4));
    assertEquals(s.width,1.2,EPSILON);
    assertEquals(s.height,9.9,EPSILON);
  }
}
