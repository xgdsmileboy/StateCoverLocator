package org.jfree.chart.renderer.category;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.chart.Effect3D;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextUtilities;
import org.jfree.chart.util.LengthAdjustmentType;
import org.jfree.chart.util.PaintUtilities;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.RectangleAnchor;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.SerialUtilities;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
/** 
 * A renderer for bars with a 3D effect, for use with the                                                                                                                                                               {@link CategoryPlot} class.  The example shown here is generatedby the <code>BarChart3DDemo1.java</code> program included in the JFreeChart Demo Collection: <br><br> <img src="../../../../../images/BarRenderer3DSample.png" alt="BarRenderer3DSample.png" />
 */
public class BarRenderer3D extends BarRenderer implements Effect3D, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=7686976503536003636L;
  /** 
 * The default x-offset for the 3D effect. 
 */
  public static final double DEFAULT_X_OFFSET=12.0;
  /** 
 * The default y-offset for the 3D effect. 
 */
  public static final double DEFAULT_Y_OFFSET=8.0;
  /** 
 * The default wall paint. 
 */
  public static final Paint DEFAULT_WALL_PAINT=new Color(0xDD,0xDD,0xDD);
  /** 
 * The size of x-offset for the 3D effect. 
 */
  private double xOffset;
  /** 
 * The size of y-offset for the 3D effect. 
 */
  private double yOffset;
  /** 
 * The paint used to shade the left and lower 3D wall. 
 */
  private transient Paint wallPaint;
  /** 
 * Default constructor, creates a renderer with a default '3D effect'.
 */
  public BarRenderer3D(){
    this(DEFAULT_X_OFFSET,DEFAULT_Y_OFFSET);
  }
  /** 
 * Constructs a new renderer with the specified '3D effect'.
 * @param xOffset  the x-offset for the 3D effect.
 * @param yOffset  the y-offset for the 3D effect.
 */
  public BarRenderer3D(  double xOffset,  double yOffset){
    super();
    this.xOffset=xOffset;
    this.yOffset=yOffset;
    this.wallPaint=DEFAULT_WALL_PAINT;
    ItemLabelPosition p1=new ItemLabelPosition(ItemLabelAnchor.INSIDE12,TextAnchor.TOP_CENTER);
    setBasePositiveItemLabelPosition(p1);
    ItemLabelPosition p2=new ItemLabelPosition(ItemLabelAnchor.INSIDE12,TextAnchor.TOP_CENTER);
    setBaseNegativeItemLabelPosition(p2);
  }
  /** 
 * Returns the x-offset for the 3D effect.
 * @return The 3D effect.
 * @see #getYOffset()
 */
  public double getXOffset(){
    return this.xOffset;
  }
  /** 
 * Returns the y-offset for the 3D effect.
 * @return The 3D effect.
 */
  public double getYOffset(){
    return this.yOffset;
  }
  /** 
 * Returns the paint used to highlight the left and bottom wall in the plot background.
 * @return The paint.
 * @see #setWallPaint(Paint)
 */
  public Paint getWallPaint(){
    return this.wallPaint;
  }
  /** 
 * Sets the paint used to hightlight the left and bottom walls in the plot background, and sends a                                                                                                                                                                {@link RendererChangeEvent} to all registeredlisteners.
 * @param paint  the paint (<code>null</code> not permitted).
 * @see #getWallPaint()
 */
  public void setWallPaint(  Paint paint){
    if (paint == null) {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
    this.wallPaint=paint;
    fireChangeEvent();
  }
  /** 
 * Initialises the renderer and returns a state object that will be passed to subsequent calls to the drawItem method.  This method gets called once at the start of the process of drawing a chart.
 * @param g2  the graphics device.
 * @param dataArea  the area in which the data is to be plotted.
 * @param plot  the plot.
 * @param rendererIndex  the renderer index.
 * @param info  collects chart rendering information for return to caller.
 * @return The renderer state.
 */
  public CategoryItemRendererState initialise(  Graphics2D g2,  Rectangle2D dataArea,  CategoryPlot plot,  CategoryDataset dataset,  PlotRenderingInfo info){
    Rectangle2D adjusted=new Rectangle2D.Double(dataArea.getX(),dataArea.getY() + getYOffset(),dataArea.getWidth() - getXOffset(),dataArea.getHeight() - getYOffset());
    CategoryItemRendererState state=super.initialise(g2,adjusted,plot,dataset,info);
    return state;
  }
  /** 
 * Draws the background for the plot.
 * @param g2  the graphics device.
 * @param plot  the plot.
 * @param dataArea  the area inside the axes.
 */
  public void drawBackground(  Graphics2D g2,  CategoryPlot plot,  Rectangle2D dataArea){
    float x0=(float)dataArea.getX();
    float x1=x0 + (float)Math.abs(this.xOffset);
    float x3=(float)dataArea.getMaxX();
    float x2=x3 - (float)Math.abs(this.xOffset);
    float y0=(float)dataArea.getMaxY();
    float y1=y0 - (float)Math.abs(this.yOffset);
    float y3=(float)dataArea.getMinY();
    float y2=y3 + (float)Math.abs(this.yOffset);
    GeneralPath clip=new GeneralPath();
    clip.moveTo(x0,y0);
    clip.lineTo(x0,y2);
    clip.lineTo(x1,y3);
    clip.lineTo(x3,y3);
    clip.lineTo(x3,y1);
    clip.lineTo(x2,y0);
    clip.closePath();
    Composite originalComposite=g2.getComposite();
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,plot.getBackgroundAlpha()));
    Paint backgroundPaint=plot.getBackgroundPaint();
    if (backgroundPaint != null) {
      g2.setPaint(backgroundPaint);
      g2.fill(clip);
    }
    GeneralPath leftWall=new GeneralPath();
    leftWall.moveTo(x0,y0);
    leftWall.lineTo(x0,y2);
    leftWall.lineTo(x1,y3);
    leftWall.lineTo(x1,y1);
    leftWall.closePath();
    g2.setPaint(getWallPaint());
    g2.fill(leftWall);
    GeneralPath bottomWall=new GeneralPath();
    bottomWall.moveTo(x0,y0);
    bottomWall.lineTo(x1,y1);
    bottomWall.lineTo(x3,y1);
    bottomWall.lineTo(x2,y0);
    bottomWall.closePath();
    g2.setPaint(getWallPaint());
    g2.fill(bottomWall);
    g2.setPaint(Color.lightGray);
    Line2D corner=new Line2D.Double(x0,y0,x1,y1);
    g2.draw(corner);
    corner.setLine(x1,y1,x1,y3);
    g2.draw(corner);
    corner.setLine(x1,y1,x3,y1);
    g2.draw(corner);
    Image backgroundImage=plot.getBackgroundImage();
    if (backgroundImage != null) {
      Rectangle2D adjusted=new Rectangle2D.Double(dataArea.getX() + getXOffset(),dataArea.getY(),dataArea.getWidth() - getXOffset(),dataArea.getHeight() - getYOffset());
      plot.drawBackgroundImage(g2,adjusted);
    }
    g2.setComposite(originalComposite);
  }
  /** 
 * Draws the outline for the plot.
 * @param g2  the graphics device.
 * @param plot  the plot.
 * @param dataArea  the area inside the axes.
 */
  public void drawOutline(  Graphics2D g2,  CategoryPlot plot,  Rectangle2D dataArea){
    float x0=(float)dataArea.getX();
    float x1=x0 + (float)Math.abs(this.xOffset);
    float x3=(float)dataArea.getMaxX();
    float x2=x3 - (float)Math.abs(this.xOffset);
    float y0=(float)dataArea.getMaxY();
    float y1=y0 - (float)Math.abs(this.yOffset);
    float y3=(float)dataArea.getMinY();
    float y2=y3 + (float)Math.abs(this.yOffset);
    GeneralPath clip=new GeneralPath();
    clip.moveTo(x0,y0);
    clip.lineTo(x0,y2);
    clip.lineTo(x1,y3);
    clip.lineTo(x3,y3);
    clip.lineTo(x3,y1);
    clip.lineTo(x2,y0);
    clip.closePath();
    Stroke outlineStroke=plot.getOutlineStroke();
    Paint outlinePaint=plot.getOutlinePaint();
    if ((outlineStroke != null) && (outlinePaint != null)) {
      g2.setStroke(outlineStroke);
      g2.setPaint(outlinePaint);
      g2.draw(clip);
    }
  }
  /** 
 * Draws a grid line against the domain axis.
 * @param g2  the graphics device.
 * @param plot  the plot.
 * @param dataArea  the area for plotting data (not yet adjusted for any3D effect).
 * @param value  the Java2D value at which the grid line should be drawn.
 */
  public void drawDomainGridline(  Graphics2D g2,  CategoryPlot plot,  Rectangle2D dataArea,  double value){
    Line2D line1=null;
    Line2D line2=null;
    PlotOrientation orientation=plot.getOrientation();
    if (orientation == PlotOrientation.HORIZONTAL) {
      double y0=value;
      double y1=value - getYOffset();
      double x0=dataArea.getMinX();
      double x1=x0 + getXOffset();
      double x2=dataArea.getMaxX();
      line1=new Line2D.Double(x0,y0,x1,y1);
      line2=new Line2D.Double(x1,y1,x2,y1);
    }
 else {
      if (orientation == PlotOrientation.VERTICAL) {
        double x0=value;
        double x1=value + getXOffset();
        double y0=dataArea.getMaxY();
        double y1=y0 - getYOffset();
        double y2=dataArea.getMinY();
        line1=new Line2D.Double(x0,y0,x1,y1);
        line2=new Line2D.Double(x1,y1,x1,y2);
      }
    }
    Paint paint=plot.getDomainGridlinePaint();
    Stroke stroke=plot.getDomainGridlineStroke();
    g2.setPaint(paint != null ? paint : Plot.DEFAULT_OUTLINE_PAINT);
    g2.setStroke(stroke != null ? stroke : Plot.DEFAULT_OUTLINE_STROKE);
    g2.draw(line1);
    g2.draw(line2);
  }
  /** 
 * Draws a grid line against the range axis.
 * @param g2  the graphics device.
 * @param plot  the plot.
 * @param axis  the value axis.
 * @param dataArea  the area for plotting data (not yet adjusted for any3D effect).
 * @param value  the value at which the grid line should be drawn.
 */
  public void drawRangeGridline(  Graphics2D g2,  CategoryPlot plot,  ValueAxis axis,  Rectangle2D dataArea,  double value){
    Range range=axis.getRange();
    if (!range.contains(value)) {
      return;
    }
    Rectangle2D adjusted=new Rectangle2D.Double(dataArea.getX(),dataArea.getY() + getYOffset(),dataArea.getWidth() - getXOffset(),dataArea.getHeight() - getYOffset());
    Line2D line1=null;
    Line2D line2=null;
    PlotOrientation orientation=plot.getOrientation();
    if (orientation == PlotOrientation.HORIZONTAL) {
      double x0=axis.valueToJava2D(value,adjusted,plot.getRangeAxisEdge());
      double x1=x0 + getXOffset();
      double y0=dataArea.getMaxY();
      double y1=y0 - getYOffset();
      double y2=dataArea.getMinY();
      line1=new Line2D.Double(x0,y0,x1,y1);
      line2=new Line2D.Double(x1,y1,x1,y2);
    }
 else {
      if (orientation == PlotOrientation.VERTICAL) {
        double y0=axis.valueToJava2D(value,adjusted,plot.getRangeAxisEdge());
        double y1=y0 - getYOffset();
        double x0=dataArea.getMinX();
        double x1=x0 + getXOffset();
        double x2=dataArea.getMaxX();
        line1=new Line2D.Double(x0,y0,x1,y1);
        line2=new Line2D.Double(x1,y1,x2,y1);
      }
    }
    Paint paint=plot.getRangeGridlinePaint();
    Stroke stroke=plot.getRangeGridlineStroke();
    g2.setPaint(paint != null ? paint : Plot.DEFAULT_OUTLINE_PAINT);
    g2.setStroke(stroke != null ? stroke : Plot.DEFAULT_OUTLINE_STROKE);
    g2.draw(line1);
    g2.draw(line2);
  }
  /** 
 * Draws a line perpendicular to the range axis.
 * @param g2  the graphics device.
 * @param plot  the plot.
 * @param axis  the value axis.
 * @param dataArea  the area for plotting data (not yet adjusted for any 3Deffect).
 * @param value  the value at which the grid line should be drawn.
 * @param paint  the paint.
 * @param stroke  the stroke.
 * @see #drawRangeGridline
 * @since 1.0.13
 */
  public void drawRangeLine(  Graphics2D g2,  CategoryPlot plot,  ValueAxis axis,  Rectangle2D dataArea,  double value,  Paint paint,  Stroke stroke){
    Range range=axis.getRange();
    if (!range.contains(value)) {
      return;
    }
    Rectangle2D adjusted=new Rectangle2D.Double(dataArea.getX(),dataArea.getY() + getYOffset(),dataArea.getWidth() - getXOffset(),dataArea.getHeight() - getYOffset());
    Line2D line1=null;
    Line2D line2=null;
    PlotOrientation orientation=plot.getOrientation();
    if (orientation == PlotOrientation.HORIZONTAL) {
      double x0=axis.valueToJava2D(value,adjusted,plot.getRangeAxisEdge());
      double x1=x0 + getXOffset();
      double y0=dataArea.getMaxY();
      double y1=y0 - getYOffset();
      double y2=dataArea.getMinY();
      line1=new Line2D.Double(x0,y0,x1,y1);
      line2=new Line2D.Double(x1,y1,x1,y2);
    }
 else {
      if (orientation == PlotOrientation.VERTICAL) {
        double y0=axis.valueToJava2D(value,adjusted,plot.getRangeAxisEdge());
        double y1=y0 - getYOffset();
        double x0=dataArea.getMinX();
        double x1=x0 + getXOffset();
        double x2=dataArea.getMaxX();
        line1=new Line2D.Double(x0,y0,x1,y1);
        line2=new Line2D.Double(x1,y1,x2,y1);
      }
    }
    g2.setPaint(paint);
    g2.setStroke(stroke);
    g2.draw(line1);
    g2.draw(line2);
  }
  /** 
 * Draws a range marker.
 * @param g2  the graphics device.
 * @param plot  the plot.
 * @param axis  the value axis.
 * @param marker  the marker.
 * @param dataArea  the area for plotting data (not including 3D effect).
 */
  public void drawRangeMarker(  Graphics2D g2,  CategoryPlot plot,  ValueAxis axis,  Marker marker,  Rectangle2D dataArea){
    Rectangle2D adjusted=new Rectangle2D.Double(dataArea.getX(),dataArea.getY() + getYOffset(),dataArea.getWidth() - getXOffset(),dataArea.getHeight() - getYOffset());
    if (marker instanceof ValueMarker) {
      ValueMarker vm=(ValueMarker)marker;
      double value=vm.getValue();
      Range range=axis.getRange();
      if (!range.contains(value)) {
        return;
      }
      GeneralPath path=null;
      PlotOrientation orientation=plot.getOrientation();
      if (orientation == PlotOrientation.HORIZONTAL) {
        float x=(float)axis.valueToJava2D(value,adjusted,plot.getRangeAxisEdge());
        float y=(float)adjusted.getMaxY();
        path=new GeneralPath();
        path.moveTo(x,y);
        path.lineTo((float)(x + getXOffset()),y - (float)getYOffset());
        path.lineTo((float)(x + getXOffset()),(float)(adjusted.getMinY() - getYOffset()));
        path.lineTo(x,(float)adjusted.getMinY());
        path.closePath();
      }
 else {
        if (orientation == PlotOrientation.VERTICAL) {
          float y=(float)axis.valueToJava2D(value,adjusted,plot.getRangeAxisEdge());
          float x=(float)dataArea.getX();
          path=new GeneralPath();
          path.moveTo(x,y);
          path.lineTo(x + (float)this.xOffset,y - (float)this.yOffset);
          path.lineTo((float)(adjusted.getMaxX() + this.xOffset),y - (float)this.yOffset);
          path.lineTo((float)(adjusted.getMaxX()),y);
          path.closePath();
        }
      }
      g2.setPaint(marker.getPaint());
      g2.fill(path);
      g2.setPaint(marker.getOutlinePaint());
      g2.draw(path);
      String label=marker.getLabel();
      RectangleAnchor anchor=marker.getLabelAnchor();
      if (label != null) {
        Font labelFont=marker.getLabelFont();
        g2.setFont(labelFont);
        g2.setPaint(marker.getLabelPaint());
        Point2D coordinates=calculateRangeMarkerTextAnchorPoint(g2,orientation,dataArea,path.getBounds2D(),marker.getLabelOffset(),LengthAdjustmentType.EXPAND,anchor);
        TextUtilities.drawAlignedString(label,g2,(float)coordinates.getX(),(float)coordinates.getY(),marker.getLabelTextAnchor());
      }
    }
 else {
      super.drawRangeMarker(g2,plot,axis,marker,adjusted);
    }
  }
  /** 
 * Draws a 3D bar to represent one data item.
 * @param g2  the graphics device.
 * @param state  the renderer state.
 * @param dataArea  the area for plotting the data.
 * @param plot  the plot.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param dataset  the dataset.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @param selected  is the item selected?
 * @param pass  the pass index.
 * @since 1.2.0
 */
  public void drawItem(  Graphics2D g2,  CategoryItemRendererState state,  Rectangle2D dataArea,  CategoryPlot plot,  CategoryAxis domainAxis,  ValueAxis rangeAxis,  CategoryDataset dataset,  int row,  int column,  boolean selected,  int pass){
    Number dataValue=dataset.getValue(row,column);
    if (dataValue == null) {
      return;
    }
    double value=dataValue.doubleValue();
    Rectangle2D adjusted=new Rectangle2D.Double(dataArea.getX(),dataArea.getY() + getYOffset(),dataArea.getWidth() - getXOffset(),dataArea.getHeight() - getYOffset());
    PlotOrientation orientation=plot.getOrientation();
    double barW0=calculateBarW0(plot,orientation,adjusted,domainAxis,state,row,column);
    double[] barL0L1=calculateBarL0L1(value,rangeAxis.getLowerBound(),rangeAxis.getUpperBound());
    if (barL0L1 == null) {
      return;
    }
    RectangleEdge edge=plot.getRangeAxisEdge();
    double transL0=rangeAxis.valueToJava2D(barL0L1[0],adjusted,edge);
    double transL1=rangeAxis.valueToJava2D(barL0L1[1],adjusted,edge);
    double barL0=Math.min(transL0,transL1);
    double barLength=Math.abs(transL1 - transL0);
    Rectangle2D bar=null;
    if (orientation == PlotOrientation.HORIZONTAL) {
      bar=new Rectangle2D.Double(barL0,barW0,barLength,state.getBarWidth());
    }
 else {
      bar=new Rectangle2D.Double(barW0,barL0,state.getBarWidth(),barLength);
    }
    Paint itemPaint=getItemPaint(row,column,selected);
    g2.setPaint(itemPaint);
    g2.fill(bar);
    double x0=bar.getMinX();
    double x1=x0 + getXOffset();
    double x2=bar.getMaxX();
    double x3=x2 + getXOffset();
    double y0=bar.getMinY() - getYOffset();
    double y1=bar.getMinY();
    double y2=bar.getMaxY() - getYOffset();
    double y3=bar.getMaxY();
    GeneralPath bar3dRight=null;
    GeneralPath bar3dTop=null;
    if (barLength > 0.0) {
      bar3dRight=new GeneralPath();
      bar3dRight.moveTo((float)x2,(float)y3);
      bar3dRight.lineTo((float)x2,(float)y1);
      bar3dRight.lineTo((float)x3,(float)y0);
      bar3dRight.lineTo((float)x3,(float)y2);
      bar3dRight.closePath();
      if (itemPaint instanceof Color) {
        g2.setPaint(((Color)itemPaint).darker());
      }
      g2.fill(bar3dRight);
    }
    bar3dTop=new GeneralPath();
    bar3dTop.moveTo((float)x0,(float)y1);
    bar3dTop.lineTo((float)x1,(float)y0);
    bar3dTop.lineTo((float)x3,(float)y0);
    bar3dTop.lineTo((float)x2,(float)y1);
    bar3dTop.closePath();
    g2.fill(bar3dTop);
    if (isDrawBarOutline() && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
      g2.setStroke(getItemOutlineStroke(row,column,selected));
      g2.setPaint(getItemOutlinePaint(row,column,selected));
      g2.draw(bar);
      if (bar3dRight != null) {
        g2.draw(bar3dRight);
      }
      if (bar3dTop != null) {
        g2.draw(bar3dTop);
      }
    }
    CategoryItemLabelGenerator generator=getItemLabelGenerator(row,column,selected);
    if (generator != null && isItemLabelVisible(row,column,selected)) {
      drawItemLabelForBar(g2,plot,dataset,row,column,selected,generator,bar,(value < 0.0));
    }
    EntityCollection entities=state.getEntityCollection();
    if (entities != null) {
      GeneralPath barOutline=new GeneralPath();
      barOutline.moveTo((float)x0,(float)y3);
      barOutline.lineTo((float)x0,(float)y1);
      barOutline.lineTo((float)x1,(float)y0);
      barOutline.lineTo((float)x3,(float)y0);
      barOutline.lineTo((float)x3,(float)y2);
      barOutline.lineTo((float)x2,(float)y3);
      barOutline.closePath();
      addEntity(entities,barOutline,dataset,row,column,selected);
    }
  }
  /** 
 * Tests this renderer for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof BarRenderer3D)) {
      return false;
    }
    BarRenderer3D that=(BarRenderer3D)obj;
    if (this.xOffset != that.xOffset) {
      return false;
    }
    if (this.yOffset != that.yOffset) {
      return false;
    }
    if (!PaintUtilities.equal(this.wallPaint,that.wallPaint)) {
      return false;
    }
    return super.equals(obj);
  }
  /** 
 * Provides serialization support.
 * @param stream  the output stream.
 * @throws IOException  if there is an I/O error.
 */
  private void writeObject(  ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    SerialUtilities.writePaint(this.wallPaint,stream);
  }
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.wallPaint=SerialUtilities.readPaint(stream);
  }
}
