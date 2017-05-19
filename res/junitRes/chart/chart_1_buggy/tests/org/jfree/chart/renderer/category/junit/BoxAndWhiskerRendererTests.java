package org.jfree.chart.renderer.category.junit;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.BufferedImageRenderingSource;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
/** 
 * Tests for the             {@link BoxAndWhiskerRenderer} class.
 */
public class BoxAndWhiskerRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(BoxAndWhiskerRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public BoxAndWhiskerRendererTests(  String name){
    super(name);
  }
  /** 
 * Test that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    BoxAndWhiskerRenderer r1=new BoxAndWhiskerRenderer();
    BoxAndWhiskerRenderer r2=new BoxAndWhiskerRenderer();
    assertEquals(r1,r2);
    r1.setArtifactPaint(new GradientPaint(1.0f,2.0f,Color.yellow,3.0f,4.0f,Color.blue));
    assertFalse(r1.equals(r2));
    r2.setArtifactPaint(new GradientPaint(1.0f,2.0f,Color.yellow,3.0f,4.0f,Color.blue));
    assertEquals(r1,r2);
    r1.setFillBox(!r1.getFillBox());
    assertFalse(r1.equals(r2));
    r2.setFillBox(!r2.getFillBox());
    assertEquals(r1,r2);
    r1.setItemMargin(0.11);
    assertFalse(r1.equals(r2));
    r2.setItemMargin(0.11);
    assertEquals(r1,r2);
    r1.setMaximumBarWidth(0.99);
    assertFalse(r1.equals(r2));
    r2.setMaximumBarWidth(0.99);
    assertTrue(r1.equals(r2));
    r1.setMeanVisible(true);
    assertFalse(r1.equals(r2));
    r2.setMeanVisible(true);
    assertTrue(r1.equals(r2));
    r1.setMedianVisible(false);
    assertFalse(r1.equals(r2));
    r2.setMedianVisible(false);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    BoxAndWhiskerRenderer r1=new BoxAndWhiskerRenderer();
    BoxAndWhiskerRenderer r2=new BoxAndWhiskerRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    BoxAndWhiskerRenderer r1=new BoxAndWhiskerRenderer();
    BoxAndWhiskerRenderer r2=null;
    try {
      r2=(BoxAndWhiskerRenderer)r1.clone();
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
    BoxAndWhiskerRenderer r1=new BoxAndWhiskerRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    BoxAndWhiskerRenderer r1=new BoxAndWhiskerRenderer();
    BoxAndWhiskerRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(BoxAndWhiskerRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
  /** 
 * Draws the chart with a <code>null</code> info object to make sure that no exceptions are thrown (particularly by code in the renderer).
 */
  public void testDrawWithNullInfo(){
    boolean success=false;
    try {
      DefaultBoxAndWhiskerCategoryDataset dataset=new DefaultBoxAndWhiskerCategoryDataset();
      dataset.add(new BoxAndWhiskerItem(new Double(1.0),new Double(2.0),new Double(0.0),new Double(4.0),new Double(0.5),new Double(4.5),new Double(-0.5),new Double(5.5),null),"S1","C1");
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),new BoxAndWhiskerRenderer());
      JFreeChart chart=new JFreeChart(plot);
      chart.createBufferedImage(300,200,null);
      success=true;
    }
 catch (    NullPointerException e) {
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * A check for bug 1572478 (for the vertical orientation).
 */
  public void testBug1572478Vertical(){
    DefaultBoxAndWhiskerCategoryDataset dataset=new DefaultBoxAndWhiskerCategoryDataset(){
      public Number getQ1Value(      int row,      int column){
        return null;
      }
      public Number getQ1Value(      Comparable rowKey,      Comparable columnKey){
        return null;
      }
    }
;
    List values=new ArrayList();
    values.add(new Double(1.0));
    values.add(new Double(10.0));
    values.add(new Double(100.0));
    dataset.add(values,"row","column");
    CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("x"),new NumberAxis("y"),new BoxAndWhiskerRenderer());
    JFreeChart chart=new JFreeChart(plot);
    boolean success=false;
    try {
      BufferedImage image=new BufferedImage(200,100,BufferedImage.TYPE_INT_RGB);
      BufferedImageRenderingSource birs=new BufferedImageRenderingSource(image);
      ChartRenderingInfo info=new ChartRenderingInfo();
      info.setRenderingSource(birs);
      Graphics2D g2=image.createGraphics();
      chart.draw(g2,new Rectangle2D.Double(0,0,200,100),null,info);
      g2.dispose();
      success=true;
    }
 catch (    Exception e) {
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * A check for bug 1572478 (for the horizontal orientation).
 */
  public void testBug1572478Horizontal(){
    DefaultBoxAndWhiskerCategoryDataset dataset=new DefaultBoxAndWhiskerCategoryDataset(){
      public Number getQ1Value(      int row,      int column){
        return null;
      }
      public Number getQ1Value(      Comparable rowKey,      Comparable columnKey){
        return null;
      }
    }
;
    List values=new ArrayList();
    values.add(new Double(1.0));
    values.add(new Double(10.0));
    values.add(new Double(100.0));
    dataset.add(values,"row","column");
    CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("x"),new NumberAxis("y"),new BoxAndWhiskerRenderer());
    plot.setOrientation(PlotOrientation.HORIZONTAL);
    JFreeChart chart=new JFreeChart(plot);
    boolean success=false;
    try {
      BufferedImage image=new BufferedImage(200,100,BufferedImage.TYPE_INT_RGB);
      BufferedImageRenderingSource birs=new BufferedImageRenderingSource(image);
      ChartRenderingInfo info=new ChartRenderingInfo();
      info.setRenderingSource(birs);
      Graphics2D g2=image.createGraphics();
      chart.draw(g2,new Rectangle2D.Double(0,0,200,100),null,info);
      g2.dispose();
      success=true;
    }
 catch (    Exception e) {
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * Some checks for the getLegendItem() method.
 */
  public void testGetLegendItem(){
    DefaultBoxAndWhiskerCategoryDataset dataset=new DefaultBoxAndWhiskerCategoryDataset();
    List values=new ArrayList();
    values.add(new Double(1.10));
    values.add(new Double(1.45));
    values.add(new Double(1.33));
    values.add(new Double(1.23));
    dataset.add(values,"R1","C1");
    BoxAndWhiskerRenderer r=new BoxAndWhiskerRenderer();
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
    BoxAndWhiskerRenderer r=new BoxAndWhiskerRenderer();
    CategoryPlot plot=new CategoryPlot(dataset0,new CategoryAxis("x"),new NumberAxis("y"),r);
    plot.setDataset(1,dataset1);
    new JFreeChart(plot);
    LegendItem li=r.getLegendItem(1,2);
    assertEquals("R5",li.getLabel());
    assertEquals(1,li.getDatasetIndex());
    assertEquals(2,li.getSeriesIndex());
  }
  /** 
 * Draws a chart where the dataset contains a null mean value.
 */
  public void testDrawWithNullMean(){
    boolean success=false;
    try {
      DefaultBoxAndWhiskerCategoryDataset dataset=new DefaultBoxAndWhiskerCategoryDataset();
      dataset.add(new BoxAndWhiskerItem(null,new Double(2.0),new Double(0.0),new Double(4.0),new Double(0.5),new Double(4.5),new Double(-0.5),new Double(5.5),null),"S1","C1");
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),new BoxAndWhiskerRenderer());
      ChartRenderingInfo info=new ChartRenderingInfo();
      JFreeChart chart=new JFreeChart(plot);
      chart.createBufferedImage(300,200,info);
      success=true;
    }
 catch (    Exception e) {
      success=false;
      e.printStackTrace();
    }
    assertTrue(success);
  }
  /** 
 * Draws a chart where the dataset contains a null median value.
 */
  public void testDrawWithNullMedian(){
    boolean success=false;
    try {
      DefaultBoxAndWhiskerCategoryDataset dataset=new DefaultBoxAndWhiskerCategoryDataset();
      dataset.add(new BoxAndWhiskerItem(new Double(1.0),null,new Double(0.0),new Double(4.0),new Double(0.5),new Double(4.5),new Double(-0.5),new Double(5.5),null),"S1","C1");
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),new BoxAndWhiskerRenderer());
      ChartRenderingInfo info=new ChartRenderingInfo();
      JFreeChart chart=new JFreeChart(plot);
      chart.createBufferedImage(300,200,info);
      success=true;
    }
 catch (    Exception e) {
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * Draws a chart where the dataset contains a null Q1 value.
 */
  public void testDrawWithNullQ1(){
    boolean success=false;
    try {
      DefaultBoxAndWhiskerCategoryDataset dataset=new DefaultBoxAndWhiskerCategoryDataset();
      dataset.add(new BoxAndWhiskerItem(new Double(1.0),new Double(2.0),null,new Double(4.0),new Double(0.5),new Double(4.5),new Double(-0.5),new Double(5.5),null),"S1","C1");
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),new BoxAndWhiskerRenderer());
      ChartRenderingInfo info=new ChartRenderingInfo();
      JFreeChart chart=new JFreeChart(plot);
      chart.createBufferedImage(300,200,info);
      success=true;
    }
 catch (    Exception e) {
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * Draws a chart where the dataset contains a null Q3 value.
 */
  public void testDrawWithNullQ3(){
    boolean success=false;
    try {
      DefaultBoxAndWhiskerCategoryDataset dataset=new DefaultBoxAndWhiskerCategoryDataset();
      dataset.add(new BoxAndWhiskerItem(new Double(1.0),new Double(2.0),new Double(3.0),null,new Double(0.5),new Double(4.5),new Double(-0.5),new Double(5.5),null),"S1","C1");
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),new BoxAndWhiskerRenderer());
      ChartRenderingInfo info=new ChartRenderingInfo();
      JFreeChart chart=new JFreeChart(plot);
      chart.createBufferedImage(300,200,info);
      success=true;
    }
 catch (    Exception e) {
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * Draws a chart where the dataset contains a null min regular value.
 */
  public void testDrawWithNullMinRegular(){
    boolean success=false;
    try {
      DefaultBoxAndWhiskerCategoryDataset dataset=new DefaultBoxAndWhiskerCategoryDataset();
      dataset.add(new BoxAndWhiskerItem(new Double(1.0),new Double(2.0),new Double(3.0),new Double(4.0),null,new Double(4.5),new Double(-0.5),new Double(5.5),null),"S1","C1");
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),new BoxAndWhiskerRenderer());
      ChartRenderingInfo info=new ChartRenderingInfo();
      JFreeChart chart=new JFreeChart(plot);
      chart.createBufferedImage(300,200,info);
      success=true;
    }
 catch (    Exception e) {
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * Draws a chart where the dataset contains a null max regular value.
 */
  public void testDrawWithNullMaxRegular(){
    boolean success=false;
    try {
      DefaultBoxAndWhiskerCategoryDataset dataset=new DefaultBoxAndWhiskerCategoryDataset();
      dataset.add(new BoxAndWhiskerItem(new Double(1.0),new Double(2.0),new Double(3.0),new Double(4.0),new Double(0.5),null,new Double(-0.5),new Double(5.5),null),"S1","C1");
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),new BoxAndWhiskerRenderer());
      ChartRenderingInfo info=new ChartRenderingInfo();
      JFreeChart chart=new JFreeChart(plot);
      chart.createBufferedImage(300,200,info);
      success=true;
    }
 catch (    Exception e) {
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * Draws a chart where the dataset contains a null min outlier value.
 */
  public void testDrawWithNullMinOutlier(){
    boolean success=false;
    try {
      DefaultBoxAndWhiskerCategoryDataset dataset=new DefaultBoxAndWhiskerCategoryDataset();
      dataset.add(new BoxAndWhiskerItem(new Double(1.0),new Double(2.0),new Double(3.0),new Double(4.0),new Double(0.5),new Double(4.5),null,new Double(5.5),null),"S1","C1");
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),new BoxAndWhiskerRenderer());
      ChartRenderingInfo info=new ChartRenderingInfo();
      JFreeChart chart=new JFreeChart(plot);
      chart.createBufferedImage(300,200,info);
      success=true;
    }
 catch (    Exception e) {
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * Draws a chart where the dataset contains a null max outlier value.
 */
  public void testDrawWithNullMaxOutlier(){
    boolean success=false;
    try {
      DefaultBoxAndWhiskerCategoryDataset dataset=new DefaultBoxAndWhiskerCategoryDataset();
      dataset.add(new BoxAndWhiskerItem(new Double(1.0),new Double(2.0),new Double(3.0),new Double(4.0),new Double(0.5),new Double(4.5),new Double(-0.5),null,new java.util.ArrayList()),"S1","C1");
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),new BoxAndWhiskerRenderer());
      ChartRenderingInfo info=new ChartRenderingInfo();
      JFreeChart chart=new JFreeChart(plot);
      chart.createBufferedImage(300,200,info);
      success=true;
    }
 catch (    Exception e) {
      success=false;
    }
    assertTrue(success);
  }
}
