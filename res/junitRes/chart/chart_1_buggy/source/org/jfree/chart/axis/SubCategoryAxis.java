package org.jfree.chart.axis;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextUtilities;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.SerialUtilities;
import org.jfree.data.category.CategoryDataset;
/** 
 * A specialised category axis that can display sub-categories.
 */
public class SubCategoryAxis extends CategoryAxis implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-1279463299793228344L;
  /** 
 * Storage for the sub-categories (these need to be set manually). 
 */
  private List subCategories;
  /** 
 * The font for the sub-category labels. 
 */
  private Font subLabelFont=new Font("Tahoma",Font.PLAIN,10);
  /** 
 * The paint for the sub-category labels. 
 */
  private transient Paint subLabelPaint=Color.black;
  /** 
 * Creates a new axis.
 * @param label  the axis label.
 */
  public SubCategoryAxis(  String label){
    super(label);
    this.subCategories=new java.util.ArrayList();
  }
  /** 
 * Adds a sub-category to the axis and sends an      {@link AxisChangeEvent} toall registered listeners.
 * @param subCategory  the sub-category (<code>null</code> not permitted).
 */
  public void addSubCategory(  Comparable subCategory){
    if (subCategory == null) {
      throw new IllegalArgumentException("Null 'subcategory' axis.");
    }
    this.subCategories.add(subCategory);
    notifyListeners(new AxisChangeEvent(this));
  }
  /** 
 * Returns the font used to display the sub-category labels.
 * @return The font (never <code>null</code>).
 * @see #setSubLabelFont(Font)
 */
  public Font getSubLabelFont(){
    return this.subLabelFont;
  }
  /** 
 * Sets the font used to display the sub-category labels and sends an     {@link AxisChangeEvent} to all registered listeners.
 * @param font  the font (<code>null</code> not permitted).
 * @see #getSubLabelFont()
 */
  public void setSubLabelFont(  Font font){
    if (font == null) {
      throw new IllegalArgumentException("Null 'font' argument.");
    }
    this.subLabelFont=font;
    notifyListeners(new AxisChangeEvent(this));
  }
  /** 
 * Returns the paint used to display the sub-category labels.
 * @return The paint (never <code>null</code>).
 * @see #setSubLabelPaint(Paint)
 */
  public Paint getSubLabelPaint(){
    return this.subLabelPaint;
  }
  /** 
 * Sets the paint used to display the sub-category labels and sends an     {@link AxisChangeEvent} to all registered listeners.
 * @param paint  the paint (<code>null</code> not permitted).
 * @see #getSubLabelPaint()
 */
  public void setSubLabelPaint(  Paint paint){
    if (paint == null) {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
    this.subLabelPaint=paint;
    notifyListeners(new AxisChangeEvent(this));
  }
  /** 
 * Estimates the space required for the axis, given a specific drawing area.
 * @param g2  the graphics device (used to obtain font information).
 * @param plot  the plot that the axis belongs to.
 * @param plotArea  the area within which the axis should be drawn.
 * @param edge  the axis location (top or bottom).
 * @param space  the space already reserved.
 * @return The space required to draw the axis.
 */
  public AxisSpace reserveSpace(  Graphics2D g2,  Plot plot,  Rectangle2D plotArea,  RectangleEdge edge,  AxisSpace space){
    if (space == null) {
      space=new AxisSpace();
    }
    if (!isVisible()) {
      return space;
    }
    space=super.reserveSpace(g2,plot,plotArea,edge,space);
    double maxdim=getMaxDim(g2,edge);
    if (RectangleEdge.isTopOrBottom(edge)) {
      space.add(maxdim,edge);
    }
 else {
      if (RectangleEdge.isLeftOrRight(edge)) {
        space.add(maxdim,edge);
      }
    }
    return space;
  }
  /** 
 * Returns the maximum of the relevant dimension (height or width) of the subcategory labels.
 * @param g2  the graphics device.
 * @param edge  the edge.
 * @return The maximum dimension.
 */
  private double getMaxDim(  Graphics2D g2,  RectangleEdge edge){
    double result=0.0;
    g2.setFont(this.subLabelFont);
    FontMetrics fm=g2.getFontMetrics();
    Iterator iterator=this.subCategories.iterator();
    while (iterator.hasNext()) {
      Comparable subcategory=(Comparable)iterator.next();
      String label=subcategory.toString();
      Rectangle2D bounds=TextUtilities.getTextBounds(label,g2,fm);
      double dim=0.0;
      if (RectangleEdge.isLeftOrRight(edge)) {
        dim=bounds.getWidth();
      }
 else {
        dim=bounds.getHeight();
      }
      result=Math.max(result,dim);
    }
    return result;
  }
  /** 
 * Draws the axis on a Java 2D graphics device (such as the screen or a printer).
 * @param g2  the graphics device (<code>null</code> not permitted).
 * @param cursor  the cursor location.
 * @param plotArea  the area within which the axis should be drawn(<code>null</code> not permitted).
 * @param dataArea  the area within which the plot is being drawn(<code>null</code> not permitted).
 * @param edge  the location of the axis (<code>null</code> not permitted).
 * @param plotState  collects information about the plot(<code>null</code> permitted).
 * @return The axis state (never <code>null</code>).
 */
  public AxisState draw(  Graphics2D g2,  double cursor,  Rectangle2D plotArea,  Rectangle2D dataArea,  RectangleEdge edge,  PlotRenderingInfo plotState){
    if (!isVisible()) {
      return new AxisState(cursor);
    }
    if (isAxisLineVisible()) {
      drawAxisLine(g2,cursor,dataArea,edge);
    }
    AxisState state=new AxisState(cursor);
    state=drawSubCategoryLabels(g2,plotArea,dataArea,edge,state,plotState);
    state=drawCategoryLabels(g2,plotArea,dataArea,edge,state,plotState);
    state=drawLabel(getLabel(),g2,plotArea,dataArea,edge,state,plotState);
    return state;
  }
  /** 
 * Draws the category labels and returns the updated axis state.
 * @param g2  the graphics device (<code>null</code> not permitted).
 * @param plotArea  the plot area (<code>null</code> not permitted).
 * @param dataArea  the area inside the axes (<code>null</code> notpermitted).
 * @param edge  the axis location (<code>null</code> not permitted).
 * @param state  the axis state (<code>null</code> not permitted).
 * @param plotState  collects information about the plot (<code>null</code>permitted).
 * @return The updated axis state (never <code>null</code>).
 */
  protected AxisState drawSubCategoryLabels(  Graphics2D g2,  Rectangle2D plotArea,  Rectangle2D dataArea,  RectangleEdge edge,  AxisState state,  PlotRenderingInfo plotState){
    if (state == null) {
      throw new IllegalArgumentException("Null 'state' argument.");
    }
    g2.setFont(this.subLabelFont);
    g2.setPaint(this.subLabelPaint);
    CategoryPlot plot=(CategoryPlot)getPlot();
    int categoryCount=0;
    CategoryDataset dataset=plot.getDataset();
    if (dataset != null) {
      categoryCount=dataset.getColumnCount();
    }
    double maxdim=getMaxDim(g2,edge);
    for (int categoryIndex=0; categoryIndex < categoryCount; categoryIndex++) {
      double x0=0.0;
      double x1=0.0;
      double y0=0.0;
      double y1=0.0;
      if (edge == RectangleEdge.TOP) {
        x0=getCategoryStart(categoryIndex,categoryCount,dataArea,edge);
        x1=getCategoryEnd(categoryIndex,categoryCount,dataArea,edge);
        y1=state.getCursor();
        y0=y1 - maxdim;
      }
 else {
        if (edge == RectangleEdge.BOTTOM) {
          x0=getCategoryStart(categoryIndex,categoryCount,dataArea,edge);
          x1=getCategoryEnd(categoryIndex,categoryCount,dataArea,edge);
          y0=state.getCursor();
          y1=y0 + maxdim;
        }
 else {
          if (edge == RectangleEdge.LEFT) {
            y0=getCategoryStart(categoryIndex,categoryCount,dataArea,edge);
            y1=getCategoryEnd(categoryIndex,categoryCount,dataArea,edge);
            x1=state.getCursor();
            x0=x1 - maxdim;
          }
 else {
            if (edge == RectangleEdge.RIGHT) {
              y0=getCategoryStart(categoryIndex,categoryCount,dataArea,edge);
              y1=getCategoryEnd(categoryIndex,categoryCount,dataArea,edge);
              x0=state.getCursor();
              x1=x0 + maxdim;
            }
          }
        }
      }
      Rectangle2D area=new Rectangle2D.Double(x0,y0,(x1 - x0),(y1 - y0));
      int subCategoryCount=this.subCategories.size();
      float width=(float)((x1 - x0) / subCategoryCount);
      float height=(float)((y1 - y0) / subCategoryCount);
      float xx=0.0f;
      float yy=0.0f;
      for (int i=0; i < subCategoryCount; i++) {
        if (RectangleEdge.isTopOrBottom(edge)) {
          xx=(float)(x0 + (i + 0.5) * width);
          yy=(float)area.getCenterY();
        }
 else {
          xx=(float)area.getCenterX();
          yy=(float)(y0 + (i + 0.5) * height);
        }
        String label=this.subCategories.get(i).toString();
        TextUtilities.drawRotatedString(label,g2,xx,yy,TextAnchor.CENTER,0.0,TextAnchor.CENTER);
      }
    }
    if (edge.equals(RectangleEdge.TOP)) {
      double h=maxdim;
      state.cursorUp(h);
    }
 else {
      if (edge.equals(RectangleEdge.BOTTOM)) {
        double h=maxdim;
        state.cursorDown(h);
      }
 else {
        if (edge == RectangleEdge.LEFT) {
          double w=maxdim;
          state.cursorLeft(w);
        }
 else {
          if (edge == RectangleEdge.RIGHT) {
            double w=maxdim;
            state.cursorRight(w);
          }
        }
      }
    }
    return state;
  }
  /** 
 * Tests the axis for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (obj instanceof SubCategoryAxis && super.equals(obj)) {
      SubCategoryAxis axis=(SubCategoryAxis)obj;
      if (!this.subCategories.equals(axis.subCategories)) {
        return false;
      }
      if (!this.subLabelFont.equals(axis.subLabelFont)) {
        return false;
      }
      if (!this.subLabelPaint.equals(axis.subLabelPaint)) {
        return false;
      }
      return true;
    }
    return false;
  }
  /** 
 * Provides serialization support.
 * @param stream  the output stream.
 * @throws IOException  if there is an I/O error.
 */
  private void writeObject(  ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    SerialUtilities.writePaint(this.subLabelPaint,stream);
  }
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.subLabelPaint=SerialUtilities.readPaint(stream);
  }
}
