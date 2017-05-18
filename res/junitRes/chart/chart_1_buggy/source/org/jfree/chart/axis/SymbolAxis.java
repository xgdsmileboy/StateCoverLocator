package org.jfree.chart.axis;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.ValueAxisPlot;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextUtilities;
import org.jfree.chart.util.PaintUtilities;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.SerialUtilities;
import org.jfree.data.Range;
/** 
 * A standard linear value axis that replaces integer values with symbols.
 */
public class SymbolAxis extends NumberAxis implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=7216330468770619716L;
  /** 
 * The default grid band paint. 
 */
  public static final Paint DEFAULT_GRID_BAND_PAINT=new Color(232,234,232,128);
  /** 
 * The default paint for alternate grid bands.
 * @since 1.0.7
 */
  public static final Paint DEFAULT_GRID_BAND_ALTERNATE_PAINT=new Color(0,0,0,0);
  /** 
 * The list of symbols to display instead of the numeric values. 
 */
  private List symbols;
  /** 
 * Flag that indicates whether or not grid bands are visible. 
 */
  private boolean gridBandsVisible;
  /** 
 * The paint used to color the grid bands (if the bands are visible). 
 */
  private transient Paint gridBandPaint;
  /** 
 * The paint used to fill the alternate grid bands.
 * @since 1.0.7
 */
  private transient Paint gridBandAlternatePaint;
  /** 
 * Constructs a symbol axis, using default attribute values where necessary.
 * @param label  the axis label (<code>null</code> permitted).
 * @param sv  the list of symbols to display instead of the numericvalues.
 */
  public SymbolAxis(  String label,  String[] sv){
    super(label);
    this.symbols=Arrays.asList(sv);
    this.gridBandsVisible=true;
    this.gridBandPaint=DEFAULT_GRID_BAND_PAINT;
    this.gridBandAlternatePaint=DEFAULT_GRID_BAND_ALTERNATE_PAINT;
    setAutoTickUnitSelection(false,false);
    setAutoRangeStickyZero(false);
  }
  /** 
 * Returns an array of the symbols for the axis.
 * @return The symbols.
 */
  public String[] getSymbols(){
    String[] result=new String[this.symbols.size()];
    result=(String[])this.symbols.toArray(result);
    return result;
  }
  /** 
 * Returns <code>true</code> if the grid bands are showing, and <code>false</code> otherwise.
 * @return <code>true</code> if the grid bands are showing, and<code>false</code> otherwise.
 * @see #setGridBandsVisible(boolean)
 */
  public boolean isGridBandsVisible(){
    return this.gridBandsVisible;
  }
  /** 
 * Sets the visibility of the grid bands and notifies registered listeners that the axis has been modified.
 * @param flag  the new setting.
 * @see #isGridBandsVisible()
 */
  public void setGridBandsVisible(  boolean flag){
    if (this.gridBandsVisible != flag) {
      this.gridBandsVisible=flag;
      notifyListeners(new AxisChangeEvent(this));
    }
  }
  /** 
 * Returns the paint used to color the grid bands.
 * @return The grid band paint (never <code>null</code>).
 * @see #setGridBandPaint(Paint)
 * @see #isGridBandsVisible()
 */
  public Paint getGridBandPaint(){
    return this.gridBandPaint;
  }
  /** 
 * Sets the grid band paint and sends an      {@link AxisChangeEvent} toall registered listeners.
 * @param paint  the paint (<code>null</code> not permitted).
 * @see #getGridBandPaint()
 */
  public void setGridBandPaint(  Paint paint){
    if (paint == null) {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
    this.gridBandPaint=paint;
    notifyListeners(new AxisChangeEvent(this));
  }
  /** 
 * Returns the paint used for alternate grid bands.
 * @return The paint (never <code>null</code>).
 * @see #setGridBandAlternatePaint(Paint)
 * @see #getGridBandPaint()
 * @since 1.0.7
 */
  public Paint getGridBandAlternatePaint(){
    return this.gridBandAlternatePaint;
  }
  /** 
 * Sets the paint used for alternate grid bands and sends a     {@link AxisChangeEvent} to all registered listeners.
 * @param paint  the paint (<code>null</code> not permitted).
 * @see #getGridBandAlternatePaint()
 * @see #setGridBandPaint(Paint)
 * @since 1.0.7
 */
  public void setGridBandAlternatePaint(  Paint paint){
    if (paint == null) {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
    this.gridBandAlternatePaint=paint;
    notifyListeners(new AxisChangeEvent(this));
  }
  /** 
 * This operation is not supported by this axis.
 * @param g2  the graphics device.
 * @param dataArea  the area in which the plot and axes should be drawn.
 * @param edge  the edge along which the axis is drawn.
 */
  protected void selectAutoTickUnit(  Graphics2D g2,  Rectangle2D dataArea,  RectangleEdge edge){
    throw new UnsupportedOperationException();
  }
  /** 
 * Draws the axis on a Java 2D graphics device (such as the screen or a printer).
 * @param g2  the graphics device (<code>null</code> not permitted).
 * @param cursor  the cursor location.
 * @param plotArea  the area within which the plot and axes should be drawn(<code>null</code> not permitted).
 * @param dataArea  the area within which the data should be drawn(<code>null</code> not permitted).
 * @param edge  the axis location (<code>null</code> not permitted).
 * @param plotState  collects information about the plot(<code>null</code> permitted).
 * @return The axis state (never <code>null</code>).
 */
  public AxisState draw(  Graphics2D g2,  double cursor,  Rectangle2D plotArea,  Rectangle2D dataArea,  RectangleEdge edge,  PlotRenderingInfo plotState){
    AxisState info=new AxisState(cursor);
    if (isVisible()) {
      info=super.draw(g2,cursor,plotArea,dataArea,edge,plotState);
    }
    if (this.gridBandsVisible) {
      drawGridBands(g2,plotArea,dataArea,edge,info.getTicks());
    }
    return info;
  }
  /** 
 * Draws the grid bands.  Alternate bands are colored using <CODE>gridBandPaint<CODE> (<CODE>DEFAULT_GRID_BAND_PAINT</CODE> by default).
 * @param g2  the graphics device.
 * @param plotArea  the area within which the chart should be drawn.
 * @param dataArea  the area within which the plot should be drawn (asubset of the drawArea).
 * @param edge  the axis location.
 * @param ticks  the ticks.
 */
  protected void drawGridBands(  Graphics2D g2,  Rectangle2D plotArea,  Rectangle2D dataArea,  RectangleEdge edge,  List ticks){
    Shape savedClip=g2.getClip();
    g2.clip(dataArea);
    if (RectangleEdge.isTopOrBottom(edge)) {
      drawGridBandsHorizontal(g2,plotArea,dataArea,true,ticks);
    }
 else {
      if (RectangleEdge.isLeftOrRight(edge)) {
        drawGridBandsVertical(g2,plotArea,dataArea,true,ticks);
      }
    }
    g2.setClip(savedClip);
  }
  /** 
 * Draws the grid bands for the axis when it is at the top or bottom of the plot.
 * @param g2  the graphics device.
 * @param plotArea  the area within which the chart should be drawn.
 * @param dataArea  the area within which the plot should be drawn(a subset of the drawArea).
 * @param firstGridBandIsDark  True: the first grid band takes thecolor of <CODE>gridBandPaint<CODE>. False: the second grid band takes the color of <CODE>gridBandPaint<CODE>.
 * @param ticks  the ticks.
 */
  protected void drawGridBandsHorizontal(  Graphics2D g2,  Rectangle2D plotArea,  Rectangle2D dataArea,  boolean firstGridBandIsDark,  List ticks){
    boolean currentGridBandIsDark=firstGridBandIsDark;
    double yy=dataArea.getY();
    double xx1, xx2;
    double outlineStrokeWidth;
    if (getPlot().getOutlineStroke() != null) {
      outlineStrokeWidth=((BasicStroke)getPlot().getOutlineStroke()).getLineWidth();
    }
 else {
      outlineStrokeWidth=1d;
    }
    Iterator iterator=ticks.iterator();
    ValueTick tick;
    Rectangle2D band;
    while (iterator.hasNext()) {
      tick=(ValueTick)iterator.next();
      xx1=valueToJava2D(tick.getValue() - 0.5d,dataArea,RectangleEdge.BOTTOM);
      xx2=valueToJava2D(tick.getValue() + 0.5d,dataArea,RectangleEdge.BOTTOM);
      if (currentGridBandIsDark) {
        g2.setPaint(this.gridBandPaint);
      }
 else {
        g2.setPaint(this.gridBandAlternatePaint);
      }
      band=new Rectangle2D.Double(xx1,yy + outlineStrokeWidth,xx2 - xx1,dataArea.getMaxY() - yy - outlineStrokeWidth);
      g2.fill(band);
      currentGridBandIsDark=!currentGridBandIsDark;
    }
    g2.setPaintMode();
  }
  /** 
 * Draws the grid bands for the axis when it is at the top or bottom of the plot.
 * @param g2  the graphics device.
 * @param drawArea  the area within which the chart should be drawn.
 * @param plotArea  the area within which the plot should be drawn (asubset of the drawArea).
 * @param firstGridBandIsDark  True: the first grid band takes thecolor of <CODE>gridBandPaint<CODE>. False: the second grid band takes the color of <CODE>gridBandPaint<CODE>.
 * @param ticks  a list of ticks.
 */
  protected void drawGridBandsVertical(  Graphics2D g2,  Rectangle2D drawArea,  Rectangle2D plotArea,  boolean firstGridBandIsDark,  List ticks){
    boolean currentGridBandIsDark=firstGridBandIsDark;
    double xx=plotArea.getX();
    double yy1, yy2;
    double outlineStrokeWidth;
    Stroke outlineStroke=getPlot().getOutlineStroke();
    if (outlineStroke != null && outlineStroke instanceof BasicStroke) {
      outlineStrokeWidth=((BasicStroke)outlineStroke).getLineWidth();
    }
 else {
      outlineStrokeWidth=1d;
    }
    Iterator iterator=ticks.iterator();
    ValueTick tick;
    Rectangle2D band;
    while (iterator.hasNext()) {
      tick=(ValueTick)iterator.next();
      yy1=valueToJava2D(tick.getValue() + 0.5d,plotArea,RectangleEdge.LEFT);
      yy2=valueToJava2D(tick.getValue() - 0.5d,plotArea,RectangleEdge.LEFT);
      if (currentGridBandIsDark) {
        g2.setPaint(this.gridBandPaint);
      }
 else {
        g2.setPaint(this.gridBandAlternatePaint);
      }
      band=new Rectangle2D.Double(xx + outlineStrokeWidth,yy1,plotArea.getMaxX() - xx - outlineStrokeWidth,yy2 - yy1);
      g2.fill(band);
      currentGridBandIsDark=!currentGridBandIsDark;
    }
    g2.setPaintMode();
  }
  /** 
 * Rescales the axis to ensure that all data is visible.
 */
  protected void autoAdjustRange(){
    Plot plot=getPlot();
    if (plot == null) {
      return;
    }
    if (plot instanceof ValueAxisPlot) {
      double upper=this.symbols.size() - 1;
      double lower=0;
      double range=upper - lower;
      double minRange=getAutoRangeMinimumSize();
      if (range < minRange) {
        upper=(upper + lower + minRange) / 2;
        lower=(upper + lower - minRange) / 2;
      }
      double upperMargin=0.5;
      double lowerMargin=0.5;
      if (getAutoRangeIncludesZero()) {
        if (getAutoRangeStickyZero()) {
          if (upper <= 0.0) {
            upper=0.0;
          }
 else {
            upper=upper + upperMargin;
          }
          if (lower >= 0.0) {
            lower=0.0;
          }
 else {
            lower=lower - lowerMargin;
          }
        }
 else {
          upper=Math.max(0.0,upper + upperMargin);
          lower=Math.min(0.0,lower - lowerMargin);
        }
      }
 else {
        if (getAutoRangeStickyZero()) {
          if (upper <= 0.0) {
            upper=Math.min(0.0,upper + upperMargin);
          }
 else {
            upper=upper + upperMargin * range;
          }
          if (lower >= 0.0) {
            lower=Math.max(0.0,lower - lowerMargin);
          }
 else {
            lower=lower - lowerMargin;
          }
        }
 else {
          upper=upper + upperMargin;
          lower=lower - lowerMargin;
        }
      }
      setRange(new Range(lower,upper),false,false);
    }
  }
  /** 
 * Calculates the positions of the tick labels for the axis, storing the results in the tick label list (ready for drawing).
 * @param g2  the graphics device.
 * @param state  the axis state.
 * @param dataArea  the area in which the data should be drawn.
 * @param edge  the location of the axis.
 * @return A list of ticks.
 */
  public List refreshTicks(  Graphics2D g2,  AxisState state,  Rectangle2D dataArea,  RectangleEdge edge){
    List ticks=null;
    if (RectangleEdge.isTopOrBottom(edge)) {
      ticks=refreshTicksHorizontal(g2,dataArea,edge);
    }
 else {
      if (RectangleEdge.isLeftOrRight(edge)) {
        ticks=refreshTicksVertical(g2,dataArea,edge);
      }
    }
    return ticks;
  }
  /** 
 * Calculates the positions of the tick labels for the axis, storing the results in the tick label list (ready for drawing).
 * @param g2  the graphics device.
 * @param dataArea  the area in which the data should be drawn.
 * @param edge  the location of the axis.
 * @return The ticks.
 */
  protected List refreshTicksHorizontal(  Graphics2D g2,  Rectangle2D dataArea,  RectangleEdge edge){
    List ticks=new java.util.ArrayList();
    Font tickLabelFont=getTickLabelFont();
    g2.setFont(tickLabelFont);
    double size=getTickUnit().getSize();
    int count=calculateVisibleTickCount();
    double lowestTickValue=calculateLowestVisibleTickValue();
    double previousDrawnTickLabelPos=0.0;
    double previousDrawnTickLabelLength=0.0;
    if (count <= ValueAxis.MAXIMUM_TICK_COUNT) {
      for (int i=0; i < count; i++) {
        double currentTickValue=lowestTickValue + (i * size);
        double xx=valueToJava2D(currentTickValue,dataArea,edge);
        String tickLabel;
        NumberFormat formatter=getNumberFormatOverride();
        if (formatter != null) {
          tickLabel=formatter.format(currentTickValue);
        }
 else {
          tickLabel=valueToString(currentTickValue);
        }
        Rectangle2D bounds=TextUtilities.getTextBounds(tickLabel,g2,g2.getFontMetrics());
        double tickLabelLength=isVerticalTickLabels() ? bounds.getHeight() : bounds.getWidth();
        boolean tickLabelsOverlapping=false;
        if (i > 0) {
          double avgTickLabelLength=(previousDrawnTickLabelLength + tickLabelLength) / 2.0;
          if (Math.abs(xx - previousDrawnTickLabelPos) < avgTickLabelLength) {
            tickLabelsOverlapping=true;
          }
        }
        if (tickLabelsOverlapping) {
          tickLabel="";
        }
 else {
          previousDrawnTickLabelPos=xx;
          previousDrawnTickLabelLength=tickLabelLength;
        }
        TextAnchor anchor=null;
        TextAnchor rotationAnchor=null;
        double angle=0.0;
        if (isVerticalTickLabels()) {
          anchor=TextAnchor.CENTER_RIGHT;
          rotationAnchor=TextAnchor.CENTER_RIGHT;
          if (edge == RectangleEdge.TOP) {
            angle=Math.PI / 2.0;
          }
 else {
            angle=-Math.PI / 2.0;
          }
        }
 else {
          if (edge == RectangleEdge.TOP) {
            anchor=TextAnchor.BOTTOM_CENTER;
            rotationAnchor=TextAnchor.BOTTOM_CENTER;
          }
 else {
            anchor=TextAnchor.TOP_CENTER;
            rotationAnchor=TextAnchor.TOP_CENTER;
          }
        }
        Tick tick=new NumberTick(new Double(currentTickValue),tickLabel,anchor,rotationAnchor,angle);
        ticks.add(tick);
      }
    }
    return ticks;
  }
  /** 
 * Calculates the positions of the tick labels for the axis, storing the results in the tick label list (ready for drawing).
 * @param g2  the graphics device.
 * @param dataArea  the area in which the plot should be drawn.
 * @param edge  the location of the axis.
 * @return The ticks.
 */
  protected List refreshTicksVertical(  Graphics2D g2,  Rectangle2D dataArea,  RectangleEdge edge){
    List ticks=new java.util.ArrayList();
    Font tickLabelFont=getTickLabelFont();
    g2.setFont(tickLabelFont);
    double size=getTickUnit().getSize();
    int count=calculateVisibleTickCount();
    double lowestTickValue=calculateLowestVisibleTickValue();
    double previousDrawnTickLabelPos=0.0;
    double previousDrawnTickLabelLength=0.0;
    if (count <= ValueAxis.MAXIMUM_TICK_COUNT) {
      for (int i=0; i < count; i++) {
        double currentTickValue=lowestTickValue + (i * size);
        double yy=valueToJava2D(currentTickValue,dataArea,edge);
        String tickLabel;
        NumberFormat formatter=getNumberFormatOverride();
        if (formatter != null) {
          tickLabel=formatter.format(currentTickValue);
        }
 else {
          tickLabel=valueToString(currentTickValue);
        }
        Rectangle2D bounds=TextUtilities.getTextBounds(tickLabel,g2,g2.getFontMetrics());
        double tickLabelLength=isVerticalTickLabels() ? bounds.getWidth() : bounds.getHeight();
        boolean tickLabelsOverlapping=false;
        if (i > 0) {
          double avgTickLabelLength=(previousDrawnTickLabelLength + tickLabelLength) / 2.0;
          if (Math.abs(yy - previousDrawnTickLabelPos) < avgTickLabelLength) {
            tickLabelsOverlapping=true;
          }
        }
        if (tickLabelsOverlapping) {
          tickLabel="";
        }
 else {
          previousDrawnTickLabelPos=yy;
          previousDrawnTickLabelLength=tickLabelLength;
        }
        TextAnchor anchor=null;
        TextAnchor rotationAnchor=null;
        double angle=0.0;
        if (isVerticalTickLabels()) {
          anchor=TextAnchor.BOTTOM_CENTER;
          rotationAnchor=TextAnchor.BOTTOM_CENTER;
          if (edge == RectangleEdge.LEFT) {
            angle=-Math.PI / 2.0;
          }
 else {
            angle=Math.PI / 2.0;
          }
        }
 else {
          if (edge == RectangleEdge.LEFT) {
            anchor=TextAnchor.CENTER_RIGHT;
            rotationAnchor=TextAnchor.CENTER_RIGHT;
          }
 else {
            anchor=TextAnchor.CENTER_LEFT;
            rotationAnchor=TextAnchor.CENTER_LEFT;
          }
        }
        Tick tick=new NumberTick(new Double(currentTickValue),tickLabel,anchor,rotationAnchor,angle);
        ticks.add(tick);
      }
    }
    return ticks;
  }
  /** 
 * Converts a value to a string, using the list of symbols.
 * @param value  value to convert.
 * @return The symbol.
 */
  public String valueToString(  double value){
    String strToReturn;
    try {
      strToReturn=(String)this.symbols.get((int)value);
    }
 catch (    IndexOutOfBoundsException ex) {
      strToReturn="";
    }
    return strToReturn;
  }
  /** 
 * Tests this axis for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof SymbolAxis)) {
      return false;
    }
    SymbolAxis that=(SymbolAxis)obj;
    if (!this.symbols.equals(that.symbols)) {
      return false;
    }
    if (this.gridBandsVisible != that.gridBandsVisible) {
      return false;
    }
    if (!PaintUtilities.equal(this.gridBandPaint,that.gridBandPaint)) {
      return false;
    }
    if (!PaintUtilities.equal(this.gridBandAlternatePaint,that.gridBandAlternatePaint)) {
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
    SerialUtilities.writePaint(this.gridBandPaint,stream);
    SerialUtilities.writePaint(this.gridBandAlternatePaint,stream);
  }
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.gridBandPaint=SerialUtilities.readPaint(stream);
    this.gridBandAlternatePaint=SerialUtilities.readPaint(stream);
  }
}
