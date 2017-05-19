package org.jfree.chart.axis.junit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.axis.PeriodAxisLabelInfo;
import org.jfree.chart.util.RectangleInsets;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
/** 
 * Tests for the             {@link PeriodAxisLabelInfo} class.
 */
public class PeriodAxisLabelInfoTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(PeriodAxisLabelInfoTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public PeriodAxisLabelInfoTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    PeriodAxisLabelInfo info1=new PeriodAxisLabelInfo(Day.class,new SimpleDateFormat("d"));
    PeriodAxisLabelInfo info2=new PeriodAxisLabelInfo(Day.class,new SimpleDateFormat("d"));
    assertTrue(info1.equals(info2));
    assertTrue(info2.equals(info1));
    Class c1=Day.class;
    Class c2=Month.class;
    DateFormat df1=new SimpleDateFormat("d");
    DateFormat df2=new SimpleDateFormat("MMM");
    RectangleInsets sp1=new RectangleInsets(1,1,1,1);
    RectangleInsets sp2=new RectangleInsets(2,2,2,2);
    Font lf1=new Font("SansSerif",Font.PLAIN,10);
    Font lf2=new Font("SansSerif",Font.BOLD,9);
    Paint lp1=Color.black;
    Paint lp2=Color.blue;
    boolean b1=true;
    boolean b2=false;
    Stroke s1=new BasicStroke(0.5f);
    Stroke s2=new BasicStroke(0.25f);
    Paint dp1=Color.red;
    Paint dp2=Color.green;
    info1=new PeriodAxisLabelInfo(c2,df1,sp1,lf1,lp1,b1,s1,dp1);
    info2=new PeriodAxisLabelInfo(c1,df1,sp1,lf1,lp1,b1,s1,dp1);
    assertFalse(info1.equals(info2));
    info2=new PeriodAxisLabelInfo(c2,df1,sp1,lf1,lp1,b1,s1,dp1);
    assertTrue(info1.equals(info2));
    info1=new PeriodAxisLabelInfo(c2,df2,sp1,lf1,lp1,b1,s1,dp1);
    assertFalse(info1.equals(info2));
    info2=new PeriodAxisLabelInfo(c2,df2,sp1,lf1,lp1,b1,s1,dp1);
    assertTrue(info1.equals(info2));
    info1=new PeriodAxisLabelInfo(c2,df2,sp2,lf1,lp1,b1,s1,dp1);
    assertFalse(info1.equals(info2));
    info2=new PeriodAxisLabelInfo(c2,df2,sp2,lf1,lp1,b1,s1,dp1);
    assertTrue(info1.equals(info2));
    info1=new PeriodAxisLabelInfo(c2,df2,sp2,lf2,lp1,b1,s1,dp1);
    assertFalse(info1.equals(info2));
    info2=new PeriodAxisLabelInfo(c2,df2,sp2,lf2,lp1,b1,s1,dp1);
    assertTrue(info1.equals(info2));
    info1=new PeriodAxisLabelInfo(c2,df2,sp2,lf2,lp2,b1,s1,dp1);
    assertFalse(info1.equals(info2));
    info2=new PeriodAxisLabelInfo(c2,df2,sp2,lf2,lp2,b1,s1,dp1);
    assertTrue(info1.equals(info2));
    info1=new PeriodAxisLabelInfo(c2,df2,sp2,lf2,lp2,b2,s1,dp1);
    assertFalse(info1.equals(info2));
    info2=new PeriodAxisLabelInfo(c2,df2,sp2,lf2,lp2,b2,s1,dp1);
    assertTrue(info1.equals(info2));
    info1=new PeriodAxisLabelInfo(c2,df2,sp2,lf2,lp2,b2,s2,dp1);
    assertFalse(info1.equals(info2));
    info2=new PeriodAxisLabelInfo(c2,df2,sp2,lf2,lp2,b2,s2,dp1);
    assertTrue(info1.equals(info2));
    info1=new PeriodAxisLabelInfo(c2,df2,sp2,lf2,lp2,b2,s2,dp2);
    assertFalse(info1.equals(info2));
    info2=new PeriodAxisLabelInfo(c2,df2,sp2,lf2,lp2,b2,s2,dp2);
    assertTrue(info1.equals(info2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    PeriodAxisLabelInfo info1=new PeriodAxisLabelInfo(Day.class,new SimpleDateFormat("d"));
    PeriodAxisLabelInfo info2=new PeriodAxisLabelInfo(Day.class,new SimpleDateFormat("d"));
    assertTrue(info1.equals(info2));
    int h1=info1.hashCode();
    int h2=info2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    PeriodAxisLabelInfo info1=new PeriodAxisLabelInfo(Day.class,new SimpleDateFormat("d"));
    PeriodAxisLabelInfo info2=null;
    try {
      info2=(PeriodAxisLabelInfo)info1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(info1 != info2);
    assertTrue(info1.getClass() == info2.getClass());
    assertTrue(info1.equals(info2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    PeriodAxisLabelInfo info1=new PeriodAxisLabelInfo(Day.class,new SimpleDateFormat("d"));
    PeriodAxisLabelInfo info2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(info1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      info2=(PeriodAxisLabelInfo)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    boolean b=info1.equals(info2);
    assertTrue(b);
  }
}
