package org.jfree.chart.plot.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.event.MarkerChangeEvent;
import org.jfree.chart.event.MarkerChangeListener;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.GradientPaintTransformer;
import org.jfree.chart.util.StandardGradientPaintTransformer;
/** 
 * Tests for the             {@link IntervalMarker} class.
 */
public class IntervalMarkerTests extends TestCase implements MarkerChangeListener {
  MarkerChangeEvent lastEvent;
  /** 
 * Records the last event.
 * @param event  the event.
 */
  public void markerChanged(  MarkerChangeEvent event){
    this.lastEvent=event;
  }
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(IntervalMarkerTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public IntervalMarkerTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    IntervalMarker m1=new IntervalMarker(45.0,50.0);
    IntervalMarker m2=new IntervalMarker(45.0,50.0);
    assertTrue(m1.equals(m2));
    assertTrue(m2.equals(m1));
    m1=new IntervalMarker(44.0,50.0);
    assertFalse(m1.equals(m2));
    m2=new IntervalMarker(44.0,50.0);
    assertTrue(m1.equals(m2));
    m1=new IntervalMarker(44.0,55.0);
    assertFalse(m1.equals(m2));
    m2=new IntervalMarker(44.0,55.0);
    assertTrue(m1.equals(m2));
    GradientPaintTransformer t=new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL);
    m1.setGradientPaintTransformer(t);
    assertFalse(m1.equals(m2));
    m2.setGradientPaintTransformer(t);
    assertTrue(m1.equals(m2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    IntervalMarker m1=new IntervalMarker(45.0,50.0);
    IntervalMarker m2=null;
    try {
      m2=(IntervalMarker)m1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(m1 != m2);
    assertTrue(m1.getClass() == m2.getClass());
    assertTrue(m1.equals(m2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    IntervalMarker m1=new IntervalMarker(45.0,50.0);
    IntervalMarker m2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(m1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      m2=(IntervalMarker)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    boolean b=m1.equals(m2);
    assertTrue(b);
  }
  private static final double EPSILON=0.0000000001;
  /** 
 * Some checks for the getStartValue() and setStartValue() methods.
 */
  public void testGetSetStartValue(){
    IntervalMarker m=new IntervalMarker(1.0,2.0);
    m.addChangeListener(this);
    this.lastEvent=null;
    assertEquals(1.0,m.getStartValue(),EPSILON);
    m.setStartValue(0.5);
    assertEquals(0.5,m.getStartValue(),EPSILON);
    assertEquals(m,this.lastEvent.getMarker());
  }
  /** 
 * Some checks for the getEndValue() and setEndValue() methods.
 */
  public void testGetSetEndValue(){
    IntervalMarker m=new IntervalMarker(1.0,2.0);
    m.addChangeListener(this);
    this.lastEvent=null;
    assertEquals(2.0,m.getEndValue(),EPSILON);
    m.setEndValue(0.5);
    assertEquals(0.5,m.getEndValue(),EPSILON);
    assertEquals(m,this.lastEvent.getMarker());
  }
}
