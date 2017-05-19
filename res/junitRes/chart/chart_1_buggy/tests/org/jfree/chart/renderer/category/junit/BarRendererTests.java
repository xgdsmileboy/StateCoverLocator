package org.jfree.chart.renderer.category.junit;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GradientBarPainter;
import org.jfree.chart.renderer.junit.RendererChangeDetector;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
/** 
 * Tests for the             {@link BarRenderer} class.
 */
public class BarRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(BarRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public BarRendererTests(  String name){
    super(name);
  }
  /** 
 * Test that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    BarRenderer r1=new BarRenderer();
    BarRenderer r2=new BarRenderer();
    assertTrue(r1.equals(r2));
    assertTrue(r2.equals(r1));
    r1.setBase(0.123);
    assertFalse(r1.equals(r2));
    r2.setBase(0.123);
    assertTrue(r1.equals(r2));
    r1.setItemMargin(0.22);
    assertFalse(r1.equals(r2));
    r2.setItemMargin(0.22);
    assertTrue(r1.equals(r2));
    r1.setDrawBarOutline(!r1.isDrawBarOutline());
    assertFalse(r1.equals(r2));
    r2.setDrawBarOutline(!r2.isDrawBarOutline());
    assertTrue(r1.equals(r2));
    r1.setMaximumBarWidth(0.11);
    assertFalse(r1.equals(r2));
    r2.setMaximumBarWidth(0.11);
    assertTrue(r1.equals(r2));
    r1.setMinimumBarLength(0.04);
    assertFalse(r1.equals(r2));
    r2.setMinimumBarLength(0.04);
    assertTrue(r1.equals(r2));
    r1.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
    assertFalse(r1.equals(r2));
    r2.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
    assertTrue(r1.equals(r2));
    r1.setPositiveItemLabelPositionFallback(new ItemLabelPosition(ItemLabelAnchor.INSIDE1,TextAnchor.CENTER));
    assertFalse(r1.equals(r2));
    r2.setPositiveItemLabelPositionFallback(new ItemLabelPosition(ItemLabelAnchor.INSIDE1,TextAnchor.CENTER));
    assertTrue(r1.equals(r2));
    r1.setNegativeItemLabelPositionFallback(new ItemLabelPosition(ItemLabelAnchor.INSIDE1,TextAnchor.CENTER));
    assertFalse(r1.equals(r2));
    r2.setNegativeItemLabelPositionFallback(new ItemLabelPosition(ItemLabelAnchor.INSIDE1,TextAnchor.CENTER));
    assertTrue(r1.equals(r2));
    r1.setBarPainter(new GradientBarPainter(0.1,0.2,0.3));
    assertFalse(r1.equals(r2));
    r2.setBarPainter(new GradientBarPainter(0.1,0.2,0.3));
    assertTrue(r1.equals(r2));
    r1.setShadowVisible(false);
    assertFalse(r1.equals(r2));
    r2.setShadowVisible(false);
    assertTrue(r1.equals(r2));
    r1.setShadowPaint(Color.red);
    assertFalse(r1.equals(r2));
    r2.setShadowPaint(Color.red);
    assertTrue(r1.equals(r2));
    r1.setShadowXOffset(3.3);
    assertFalse(r1.equals(r2));
    r2.setShadowXOffset(3.3);
    assertTrue(r1.equals(r2));
    r1.setShadowYOffset(3.3);
    assertFalse(r1.equals(r2));
    r2.setShadowYOffset(3.3);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    BarRenderer r1=new BarRenderer();
    BarRenderer r2=new BarRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    BarRenderer r1=new BarRenderer();
    r1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
    r1.setBarPainter(new GradientBarPainter(0.11,0.22,0.33));
    BarRenderer r2=null;
    try {
      r2=(BarRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
  }
  /** 
 * Check that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    BarRenderer r1=new BarRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    BarRenderer r1=new BarRenderer();
    BarRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(BarRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
  /** 
 * Tests each setter method to ensure that it sends an event notification.
 */
  public void testEventNotification(){
    RendererChangeDetector detector=new RendererChangeDetector();
    BarRenderer r1=new BarRenderer();
    r1.addChangeListener(detector);
    detector.setNotified(false);
    r1.setBasePaint(Color.red);
    assertTrue(detector.getNotified());
  }
  /** 
 * Some checks for the getLegendItem() method.
 */
  public void testGetLegendItem(){
    DefaultCategoryDataset dataset=new DefaultCategoryDataset();
    dataset.addValue(21.0,"R1","C1");
    BarRenderer r=new BarRenderer();
    CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("x"),new NumberAxis("y"),r);
    new JFreeChart(plot);
    LegendItem li=r.getLegendItem(0,0);
    assertNotNull(li);
    r.setSeriesVisibleInLegend(0,Boolean.FALSE);
    li=r.getLegendItem(0,0);
    assertNull(li);
  }
  /** 
 * A check for the datasetIndex and seriesIndex fields in the LegendItem returned by the getLegendItem() method.
 */
  public void testGetLegendItemSeriesIndex(){
    DefaultCategoryDataset dataset0=new DefaultCategoryDataset();
    dataset0.addValue(21.0,"R1","C1");
    dataset0.addValue(22.0,"R2","C1");
    DefaultCategoryDataset dataset1=new DefaultCategoryDataset();
    dataset1.addValue(23.0,"R3","C1");
    dataset1.addValue(24.0,"R4","C1");
    dataset1.addValue(25.0,"R5","C1");
    BarRenderer r=new BarRenderer();
    CategoryPlot plot=new CategoryPlot(dataset0,new CategoryAxis("x"),new NumberAxis("y"),r);
    plot.setDataset(1,dataset1);
    new JFreeChart(plot);
    LegendItem li=r.getLegendItem(1,2);
    assertEquals("R5",li.getLabel());
    assertEquals(1,li.getDatasetIndex());
    assertEquals(2,li.getSeriesIndex());
  }
  /** 
 * Some checks for the findRangeBounds() method.
 */
  public void testFindRangeBounds(){
    BarRenderer r=new BarRenderer();
    assertNull(r.findRangeBounds(null));
    DefaultCategoryDataset dataset=new DefaultCategoryDataset();
    assertNull(r.findRangeBounds(dataset));
    dataset.addValue(1.0,"R1","C1");
    assertEquals(new Range(0.0,1.0),r.findRangeBounds(dataset));
    r.setIncludeBaseInRange(false);
    assertEquals(new Range(1.0,1.0),r.findRangeBounds(dataset));
    r.setIncludeBaseInRange(true);
    dataset.addValue(-2.0,"R1","C2");
    assertEquals(new Range(-2.0,1.0),r.findRangeBounds(dataset));
    dataset.addValue(null,"R1","C3");
    assertEquals(new Range(-2.0,1.0),r.findRangeBounds(dataset));
  }
}
