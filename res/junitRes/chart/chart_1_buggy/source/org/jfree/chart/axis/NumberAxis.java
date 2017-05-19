package org.jfree.chart.axis;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.ValueAxisPlot;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.RectangleInsets;
import org.jfree.data.Range;
import org.jfree.data.RangeType;
/** 
 * An axis for displaying numerical data. <P> If the axis is set up to automatically determine its range to fit the data, you can ensure that the range includes zero (statisticians usually prefer this) by setting the <code>autoRangeIncludesZero</code> flag to <code>true</code>. <P> The <code>NumberAxis</code> class has a mechanism for automatically selecting a tick unit that is appropriate for the current axis range.  This mechanism is an adaptation of code suggested by Laurence Vanhelsuwe.
 */
public class NumberAxis extends ValueAxis implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=2805933088476185789L;
  /** 
 * The default value for the autoRangeIncludesZero flag. 
 */
  public static final boolean DEFAULT_AUTO_RANGE_INCLUDES_ZERO=true;
  /** 
 * The default value for the autoRangeStickyZero flag. 
 */
  public static final boolean DEFAULT_AUTO_RANGE_STICKY_ZERO=true;
  /** 
 * The default tick unit. 
 */
  public static final NumberTickUnit DEFAULT_TICK_UNIT=new NumberTickUnit(1.0,new DecimalFormat("0"));
  /** 
 * The default setting for the vertical tick labels flag. 
 */
  public static final boolean DEFAULT_VERTICAL_TICK_LABELS=false;
  /** 
 * The range type (can be used to force the axis to display only positive values or only negative values).
 */
  private RangeType rangeType;
  /** 
 * A flag that affects the axis range when the range is determined automatically.  If the auto range does NOT include zero and this flag is TRUE, then the range is changed to include zero.
 */
  private boolean autoRangeIncludesZero;
  /** 
 * A flag that affects the size of the margins added to the axis range when the range is determined automatically.  If the value 0 falls within the margin and this flag is TRUE, then the margin is truncated at zero.
 */
  private boolean autoRangeStickyZero;
  /** 
 * The tick unit for the axis. 
 */
  private NumberTickUnit tickUnit;
  /** 
 * The override number format. 
 */
  private NumberFormat numberFormatOverride;
  /** 
 * An optional band for marking regions on the axis. 
 */
  private MarkerAxisBand markerBand;
  /** 
 * Default constructor.
 */
  public NumberAxis(){
    this(null);
  }
  /** 
 * Constructs a number axis, using default values where necessary.
 * @param label  the axis label (<code>null</code> permitted).
 */
  public NumberAxis(  String label){
    super(label,NumberAxis.createStandardTickUnits());
    this.rangeType=RangeType.FULL;
    this.autoRangeIncludesZero=DEFAULT_AUTO_RANGE_INCLUDES_ZERO;
    this.autoRangeStickyZero=DEFAULT_AUTO_RANGE_STICKY_ZERO;
    this.tickUnit=DEFAULT_TICK_UNIT;
    this.numberFormatOverride=null;
    this.markerBand=null;
  }
  /** 
 * Returns the axis range type.
 * @return The axis range type (never <code>null</code>).
 * @see #setRangeType(RangeType)
 */
  public RangeType getRangeType(){
    return this.rangeType;
  }
  /** 
 * Sets the axis range type.
 * @param rangeType  the range type (<code>null</code> not permitted).
 * @see #getRangeType()
 */
  public void setRangeType(  RangeType rangeType){
    if (rangeType == null) {
      throw new IllegalArgumentException("Null 'rangeType' argument.");
    }
    this.rangeType=rangeType;
    notifyListeners(new AxisChangeEvent(this));
  }
  /** 
 * Returns the flag that indicates whether or not the automatic axis range (if indeed it is determined automatically) is forced to include zero.
 * @return The flag.
 */
  public boolean getAutoRangeIncludesZero(){
    return this.autoRangeIncludesZero;
  }
  /** 
 * Sets the flag that indicates whether or not the axis range, if automatically calculated, is forced to include zero. <p> If the flag is changed to <code>true</code>, the axis range is recalculated. <p> Any change to the flag will trigger an                               {@link AxisChangeEvent}.
 * @param flag  the new value of the flag.
 * @see #getAutoRangeIncludesZero()
 */
  public void setAutoRangeIncludesZero(  boolean flag){
    if (this.autoRangeIncludesZero != flag) {
      this.autoRangeIncludesZero=flag;
      if (isAutoRange()) {
        autoAdjustRange();
      }
      notifyListeners(new AxisChangeEvent(this));
    }
  }
  /** 
 * Returns a flag that affects the auto-range when zero falls outside the data range but inside the margins defined for the axis.
 * @return The flag.
 * @see #setAutoRangeStickyZero(boolean)
 */
  public boolean getAutoRangeStickyZero(){
    return this.autoRangeStickyZero;
  }
  /** 
 * Sets a flag that affects the auto-range when zero falls outside the data range but inside the margins defined for the axis.
 * @param flag  the new flag.
 * @see #getAutoRangeStickyZero()
 */
  public void setAutoRangeStickyZero(  boolean flag){
    if (this.autoRangeStickyZero != flag) {
      this.autoRangeStickyZero=flag;
      if (isAutoRange()) {
        autoAdjustRange();
      }
      notifyListeners(new AxisChangeEvent(this));
    }
  }
  /** 
 * Returns the tick unit for the axis. <p> Note: if the <code>autoTickUnitSelection</code> flag is <code>true</code> the tick unit may be changed while the axis is being drawn, so in that case the return value from this method may be irrelevant if the method is called before the axis has been drawn.
 * @return The tick unit for the axis.
 * @see #setTickUnit(NumberTickUnit)
 * @see ValueAxis#isAutoTickUnitSelection()
 */
  public NumberTickUnit getTickUnit(){
    return this.tickUnit;
  }
  /** 
 * Sets the tick unit for the axis and sends an                               {@link AxisChangeEvent} toall registered listeners.  A side effect of calling this method is that the "auto-select" feature for tick units is switched off (you can restore it using the  {@link ValueAxis#setAutoTickUnitSelection(boolean)}method).
 * @param unit  the new tick unit (<code>null</code> not permitted).
 * @see #getTickUnit()
 * @see #setTickUnit(NumberTickUnit,boolean,boolean)
 */
  public void setTickUnit(  NumberTickUnit unit){
    setTickUnit(unit,true,true);
  }
  /** 
 * Sets the tick unit for the axis and, if requested, sends an                              {@link AxisChangeEvent} to all registered listeners.  In addition, anoption is provided to turn off the "auto-select" feature for tick units (you can restore it using the {@link ValueAxis#setAutoTickUnitSelection(boolean)} method).
 * @param unit  the new tick unit (<code>null</code> not permitted).
 * @param notify  notify listeners?
 * @param turnOffAutoSelect  turn off the auto-tick selection?
 */
  public void setTickUnit(  NumberTickUnit unit,  boolean notify,  boolean turnOffAutoSelect){
    if (unit == null) {
      throw new IllegalArgumentException("Null 'unit' argument.");
    }
    this.tickUnit=unit;
    if (turnOffAutoSelect) {
      setAutoTickUnitSelection(false,false);
    }
    if (notify) {
      notifyListeners(new AxisChangeEvent(this));
    }
  }
  /** 
 * Returns the number format override.  If this is non-null, then it will be used to format the numbers on the axis.
 * @return The number formatter (possibly <code>null</code>).
 * @see #setNumberFormatOverride(NumberFormat)
 */
  public NumberFormat getNumberFormatOverride(){
    return this.numberFormatOverride;
  }
  /** 
 * Sets the number format override.  If this is non-null, then it will be used to format the numbers on the axis.
 * @param formatter  the number formatter (<code>null</code> permitted).
 * @see #getNumberFormatOverride()
 */
  public void setNumberFormatOverride(  NumberFormat formatter){
    this.numberFormatOverride=formatter;
    notifyListeners(new AxisChangeEvent(this));
  }
  /** 
 * Returns the (optional) marker band for the axis.
 * @return The marker band (possibly <code>null</code>).
 * @see #setMarkerBand(MarkerAxisBand)
 */
  public MarkerAxisBand getMarkerBand(){
    return this.markerBand;
  }
  /** 
 * Sets the marker band for the axis. <P> The marker band is optional, leave it set to <code>null</code> if you don't require it.
 * @param band the new band (<code>null<code> permitted).
 * @see #getMarkerBand()
 */
  public void setMarkerBand(  MarkerAxisBand band){
    this.markerBand=band;
    notifyListeners(new AxisChangeEvent(this));
  }
  /** 
 * Configures the axis to work with the specified plot.  If the axis has auto-scaling, then sets the maximum and minimum values.
 */
  public void configure(){
    if (isAutoRange()) {
      autoAdjustRange();
    }
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
      ValueAxisPlot vap=(ValueAxisPlot)plot;
      Range r=vap.getDataRange(this);
      if (r == null) {
        r=getDefaultAutoRange();
      }
      double upper=r.getUpperBound();
      double lower=r.getLowerBound();
      if (this.rangeType == RangeType.POSITIVE) {
        lower=Math.max(0.0,lower);
        upper=Math.max(0.0,upper);
      }
 else {
        if (this.rangeType == RangeType.NEGATIVE) {
          lower=Math.min(0.0,lower);
          upper=Math.min(0.0,upper);
        }
      }
      if (getAutoRangeIncludesZero()) {
        lower=Math.min(lower,0.0);
        upper=Math.max(upper,0.0);
      }
      double range=upper - lower;
      double fixedAutoRange=getFixedAutoRange();
      if (fixedAutoRange > 0.0) {
        lower=upper - fixedAutoRange;
      }
 else {
        double minRange=getAutoRangeMinimumSize();
        if (range < minRange) {
          double expand=(minRange - range) / 2;
          upper=upper + expand;
          lower=lower - expand;
          if (lower == upper) {
            double adjust=Math.abs(lower) / 10.0;
            lower=lower - adjust;
            upper=upper + adjust;
          }
          if (this.rangeType == RangeType.POSITIVE) {
            if (lower < 0.0) {
              upper=upper - lower;
              lower=0.0;
            }
          }
 else {
            if (this.rangeType == RangeType.NEGATIVE) {
              if (upper > 0.0) {
                lower=lower - upper;
                upper=0.0;
              }
            }
          }
        }
        if (getAutoRangeStickyZero()) {
          if (upper <= 0.0) {
            upper=Math.min(0.0,upper + getUpperMargin() * range);
          }
 else {
            upper=upper + getUpperMargin() * range;
          }
          if (lower >= 0.0) {
            lower=Math.max(0.0,lower - getLowerMargin() * range);
          }
 else {
            lower=lower - getLowerMargin() * range;
          }
        }
 else {
          upper=upper + getUpperMargin() * range;
          lower=lower - getLowerMargin() * range;
        }
      }
      setRange(new Range(lower,upper),false,false);
    }
  }
  /** 
 * Converts a data value to a coordinate in Java2D space, assuming that the axis runs along one edge of the specified dataArea. <p> Note that it is possible for the coordinate to fall outside the plotArea.
 * @param value  the data value.
 * @param area  the area for plotting the data.
 * @param edge  the axis location.
 * @return The Java2D coordinate.
 * @see #java2DToValue(double,Rectangle2D,RectangleEdge)
 */
  public double valueToJava2D(  double value,  Rectangle2D area,  RectangleEdge edge){
    Range range=getRange();
    double axisMin=range.getLowerBound();
    double axisMax=range.getUpperBound();
    double min=0.0;
    double max=0.0;
    if (RectangleEdge.isTopOrBottom(edge)) {
      min=area.getX();
      max=area.getMaxX();
    }
 else {
      if (RectangleEdge.isLeftOrRight(edge)) {
        max=area.getMinY();
        min=area.getMaxY();
      }
    }
    if (isInverted()) {
      return max - ((value - axisMin) / (axisMax - axisMin)) * (max - min);
    }
 else {
      return min + ((value - axisMin) / (axisMax - axisMin)) * (max - min);
    }
  }
  /** 
 * Converts a coordinate in Java2D space to the corresponding data value, assuming that the axis runs along one edge of the specified dataArea.
 * @param java2DValue  the coordinate in Java2D space.
 * @param area  the area in which the data is plotted.
 * @param edge  the location.
 * @return The data value.
 * @see #valueToJava2D(double,Rectangle2D,RectangleEdge)
 */
  public double java2DToValue(  double java2DValue,  Rectangle2D area,  RectangleEdge edge){
    Range range=getRange();
    double axisMin=range.getLowerBound();
    double axisMax=range.getUpperBound();
    double min=0.0;
    double max=0.0;
    if (RectangleEdge.isTopOrBottom(edge)) {
      min=area.getX();
      max=area.getMaxX();
    }
 else {
      if (RectangleEdge.isLeftOrRight(edge)) {
        min=area.getMaxY();
        max=area.getY();
      }
    }
    if (isInverted()) {
      return axisMax - (java2DValue - min) / (max - min) * (axisMax - axisMin);
    }
 else {
      return axisMin + (java2DValue - min) / (max - min) * (axisMax - axisMin);
    }
  }
  /** 
 * Calculates the value of the lowest visible tick on the axis.
 * @return The value of the lowest visible tick on the axis.
 * @see #calculateHighestVisibleTickValue()
 */
  protected double calculateLowestVisibleTickValue(){
    double unit=getTickUnit().getSize();
    double index=Math.ceil(getRange().getLowerBound() / unit);
    return index * unit;
  }
  /** 
 * Calculates the value of the highest visible tick on the axis.
 * @return The value of the highest visible tick on the axis.
 * @see #calculateLowestVisibleTickValue()
 */
  protected double calculateHighestVisibleTickValue(){
    double unit=getTickUnit().getSize();
    double index=Math.floor(getRange().getUpperBound() / unit);
    return index * unit;
  }
  /** 
 * Calculates the number of visible ticks.
 * @return The number of visible ticks on the axis.
 */
  protected int calculateVisibleTickCount(){
    double unit=getTickUnit().getSize();
    Range range=getRange();
    return (int)(Math.floor(range.getUpperBound() / unit) - Math.ceil(range.getLowerBound() / unit) + 1);
  }
  /** 
 * Draws the axis on a Java 2D graphics device (such as the screen or a printer).
 * @param g2  the graphics device (<code>null</code> not permitted).
 * @param cursor  the cursor location.
 * @param plotArea  the area within which the axes and data should be drawn(<code>null</code> not permitted).
 * @param dataArea  the area within which the data should be drawn(<code>null</code> not permitted).
 * @param edge  the location of the axis (<code>null</code> not permitted).
 * @param plotState  collects information about the plot(<code>null</code> permitted).
 * @return The axis state (never <code>null</code>).
 */
  public AxisState draw(  Graphics2D g2,  double cursor,  Rectangle2D plotArea,  Rectangle2D dataArea,  RectangleEdge edge,  PlotRenderingInfo plotState){
    AxisState state=null;
    if (!isVisible()) {
      state=new AxisState(cursor);
      List ticks=refreshTicks(g2,state,dataArea,edge);
      state.setTicks(ticks);
      return state;
    }
    state=drawTickMarksAndLabels(g2,cursor,plotArea,dataArea,edge,plotState);
    state=drawLabel(getLabel(),g2,plotArea,dataArea,edge,state,plotState);
    createAndAddEntity(cursor,state,dataArea,edge,plotState);
    return state;
  }
  /** 
 * Creates the standard tick units. <P> If you don't like these defaults, create your own instance of TickUnits and then pass it to the setStandardTickUnits() method in the NumberAxis class.
 * @return The standard tick units.
 * @see #setStandardTickUnits(TickUnitSource)
 * @see #createIntegerTickUnits()
 */
  public static TickUnitSource createStandardTickUnits(){
    TickUnits units=new TickUnits();
    DecimalFormat df000=new DecimalFormat("0.0000000000");
    DecimalFormat df00=new DecimalFormat("0.000000000");
    DecimalFormat df0=new DecimalFormat("0.00000000");
    DecimalFormat df1=new DecimalFormat("0.0000000");
    DecimalFormat df2=new DecimalFormat("0.000000");
    DecimalFormat df3=new DecimalFormat("0.00000");
    DecimalFormat df4=new DecimalFormat("0.0000");
    DecimalFormat df5=new DecimalFormat("0.000");
    DecimalFormat df6=new DecimalFormat("0.00");
    DecimalFormat df7=new DecimalFormat("0.0");
    DecimalFormat df8=new DecimalFormat("#,##0");
    DecimalFormat df9=new DecimalFormat("#,###,##0");
    DecimalFormat df10=new DecimalFormat("#,###,###,##0");
    units.add(new NumberTickUnit(0.000000001,df00,2));
    units.add(new NumberTickUnit(0.00000001,df0,2));
    units.add(new NumberTickUnit(0.0000001,df1,2));
    units.add(new NumberTickUnit(0.000001,df2,2));
    units.add(new NumberTickUnit(0.00001,df3,2));
    units.add(new NumberTickUnit(0.0001,df4,2));
    units.add(new NumberTickUnit(0.001,df5,2));
    units.add(new NumberTickUnit(0.01,df6,2));
    units.add(new NumberTickUnit(0.1,df7,2));
    units.add(new NumberTickUnit(1,df8,2));
    units.add(new NumberTickUnit(10,df8,2));
    units.add(new NumberTickUnit(100,df8,2));
    units.add(new NumberTickUnit(1000,df8,2));
    units.add(new NumberTickUnit(10000,df8,2));
    units.add(new NumberTickUnit(100000,df8,2));
    units.add(new NumberTickUnit(1000000,df9,2));
    units.add(new NumberTickUnit(10000000,df9,2));
    units.add(new NumberTickUnit(100000000,df9,2));
    units.add(new NumberTickUnit(1000000000,df10,2));
    units.add(new NumberTickUnit(10000000000.0,df10,2));
    units.add(new NumberTickUnit(100000000000.0,df10,2));
    units.add(new NumberTickUnit(0.0000000025,df000,5));
    units.add(new NumberTickUnit(0.000000025,df00,5));
    units.add(new NumberTickUnit(0.00000025,df0,5));
    units.add(new NumberTickUnit(0.0000025,df1,5));
    units.add(new NumberTickUnit(0.000025,df2,5));
    units.add(new NumberTickUnit(0.00025,df3,5));
    units.add(new NumberTickUnit(0.0025,df4,5));
    units.add(new NumberTickUnit(0.025,df5,5));
    units.add(new NumberTickUnit(0.25,df6,5));
    units.add(new NumberTickUnit(2.5,df7,5));
    units.add(new NumberTickUnit(25,df8,5));
    units.add(new NumberTickUnit(250,df8,5));
    units.add(new NumberTickUnit(2500,df8,5));
    units.add(new NumberTickUnit(25000,df8,5));
    units.add(new NumberTickUnit(250000,df8,5));
    units.add(new NumberTickUnit(2500000,df9,5));
    units.add(new NumberTickUnit(25000000,df9,5));
    units.add(new NumberTickUnit(250000000,df9,5));
    units.add(new NumberTickUnit(2500000000.0,df10,5));
    units.add(new NumberTickUnit(25000000000.0,df10,5));
    units.add(new NumberTickUnit(250000000000.0,df10,5));
    units.add(new NumberTickUnit(0.000000005,df00,5));
    units.add(new NumberTickUnit(0.00000005,df0,5));
    units.add(new NumberTickUnit(0.0000005,df1,5));
    units.add(new NumberTickUnit(0.000005,df2,5));
    units.add(new NumberTickUnit(0.00005,df3,5));
    units.add(new NumberTickUnit(0.0005,df4,5));
    units.add(new NumberTickUnit(0.005,df5,5));
    units.add(new NumberTickUnit(0.05,df6,5));
    units.add(new NumberTickUnit(0.5,df7,5));
    units.add(new NumberTickUnit(5L,df8,5));
    units.add(new NumberTickUnit(50L,df8,5));
    units.add(new NumberTickUnit(500L,df8,5));
    units.add(new NumberTickUnit(5000L,df8,5));
    units.add(new NumberTickUnit(50000L,df8,5));
    units.add(new NumberTickUnit(500000L,df8,5));
    units.add(new NumberTickUnit(5000000L,df9,5));
    units.add(new NumberTickUnit(50000000L,df9,5));
    units.add(new NumberTickUnit(500000000L,df9,5));
    units.add(new NumberTickUnit(5000000000L,df10,5));
    units.add(new NumberTickUnit(50000000000L,df10,5));
    units.add(new NumberTickUnit(500000000000L,df10,5));
    return units;
  }
  /** 
 * Returns a collection of tick units for integer values.
 * @return A collection of tick units for integer values.
 * @see #setStandardTickUnits(TickUnitSource)
 * @see #createStandardTickUnits()
 */
  public static TickUnitSource createIntegerTickUnits(){
    TickUnits units=new TickUnits();
    DecimalFormat df0=new DecimalFormat("0");
    DecimalFormat df1=new DecimalFormat("#,##0");
    units.add(new NumberTickUnit(1,df0,2));
    units.add(new NumberTickUnit(2,df0,2));
    units.add(new NumberTickUnit(5,df0,5));
    units.add(new NumberTickUnit(10,df0,2));
    units.add(new NumberTickUnit(20,df0,2));
    units.add(new NumberTickUnit(50,df0,5));
    units.add(new NumberTickUnit(100,df0,2));
    units.add(new NumberTickUnit(200,df0,2));
    units.add(new NumberTickUnit(500,df0,5));
    units.add(new NumberTickUnit(1000,df1,2));
    units.add(new NumberTickUnit(2000,df1,2));
    units.add(new NumberTickUnit(5000,df1,5));
    units.add(new NumberTickUnit(10000,df1,2));
    units.add(new NumberTickUnit(20000,df1,2));
    units.add(new NumberTickUnit(50000,df1,5));
    units.add(new NumberTickUnit(100000,df1,2));
    units.add(new NumberTickUnit(200000,df1,2));
    units.add(new NumberTickUnit(500000,df1,5));
    units.add(new NumberTickUnit(1000000,df1,2));
    units.add(new NumberTickUnit(2000000,df1,2));
    units.add(new NumberTickUnit(5000000,df1,5));
    units.add(new NumberTickUnit(10000000,df1,2));
    units.add(new NumberTickUnit(20000000,df1,2));
    units.add(new NumberTickUnit(50000000,df1,5));
    units.add(new NumberTickUnit(100000000,df1,2));
    units.add(new NumberTickUnit(200000000,df1,2));
    units.add(new NumberTickUnit(500000000,df1,5));
    units.add(new NumberTickUnit(1000000000,df1,2));
    units.add(new NumberTickUnit(2000000000,df1,2));
    units.add(new NumberTickUnit(5000000000.0,df1,5));
    units.add(new NumberTickUnit(10000000000.0,df1,2));
    return units;
  }
  /** 
 * Creates a collection of standard tick units.  The supplied locale is used to create the number formatter (a localised instance of <code>NumberFormat</code>). <P> If you don't like these defaults, create your own instance of                              {@link TickUnits} and then pass it to the<code>setStandardTickUnits()</code> method.
 * @param locale  the locale.
 * @return A tick unit collection.
 * @see #setStandardTickUnits(TickUnitSource)
 */
  public static TickUnitSource createStandardTickUnits(  Locale locale){
    TickUnits units=new TickUnits();
    NumberFormat numberFormat=NumberFormat.getNumberInstance(locale);
    units.add(new NumberTickUnit(0.0000001,numberFormat,2));
    units.add(new NumberTickUnit(0.000001,numberFormat,2));
    units.add(new NumberTickUnit(0.00001,numberFormat,2));
    units.add(new NumberTickUnit(0.0001,numberFormat,2));
    units.add(new NumberTickUnit(0.001,numberFormat,2));
    units.add(new NumberTickUnit(0.01,numberFormat,2));
    units.add(new NumberTickUnit(0.1,numberFormat,2));
    units.add(new NumberTickUnit(1,numberFormat,2));
    units.add(new NumberTickUnit(10,numberFormat,2));
    units.add(new NumberTickUnit(100,numberFormat,2));
    units.add(new NumberTickUnit(1000,numberFormat,2));
    units.add(new NumberTickUnit(10000,numberFormat,2));
    units.add(new NumberTickUnit(100000,numberFormat,2));
    units.add(new NumberTickUnit(1000000,numberFormat,2));
    units.add(new NumberTickUnit(10000000,numberFormat,2));
    units.add(new NumberTickUnit(100000000,numberFormat,2));
    units.add(new NumberTickUnit(1000000000,numberFormat,2));
    units.add(new NumberTickUnit(10000000000.0,numberFormat,2));
    units.add(new NumberTickUnit(0.00000025,numberFormat,5));
    units.add(new NumberTickUnit(0.0000025,numberFormat,5));
    units.add(new NumberTickUnit(0.000025,numberFormat,5));
    units.add(new NumberTickUnit(0.00025,numberFormat,5));
    units.add(new NumberTickUnit(0.0025,numberFormat,5));
    units.add(new NumberTickUnit(0.025,numberFormat,5));
    units.add(new NumberTickUnit(0.25,numberFormat,5));
    units.add(new NumberTickUnit(2.5,numberFormat,5));
    units.add(new NumberTickUnit(25,numberFormat,5));
    units.add(new NumberTickUnit(250,numberFormat,5));
    units.add(new NumberTickUnit(2500,numberFormat,5));
    units.add(new NumberTickUnit(25000,numberFormat,5));
    units.add(new NumberTickUnit(250000,numberFormat,5));
    units.add(new NumberTickUnit(2500000,numberFormat,5));
    units.add(new NumberTickUnit(25000000,numberFormat,5));
    units.add(new NumberTickUnit(250000000,numberFormat,5));
    units.add(new NumberTickUnit(2500000000.0,numberFormat,5));
    units.add(new NumberTickUnit(25000000000.0,numberFormat,5));
    units.add(new NumberTickUnit(0.0000005,numberFormat,5));
    units.add(new NumberTickUnit(0.000005,numberFormat,5));
    units.add(new NumberTickUnit(0.00005,numberFormat,5));
    units.add(new NumberTickUnit(0.0005,numberFormat,5));
    units.add(new NumberTickUnit(0.005,numberFormat,5));
    units.add(new NumberTickUnit(0.05,numberFormat,5));
    units.add(new NumberTickUnit(0.5,numberFormat,5));
    units.add(new NumberTickUnit(5L,numberFormat,5));
    units.add(new NumberTickUnit(50L,numberFormat,5));
    units.add(new NumberTickUnit(500L,numberFormat,5));
    units.add(new NumberTickUnit(5000L,numberFormat,5));
    units.add(new NumberTickUnit(50000L,numberFormat,5));
    units.add(new NumberTickUnit(500000L,numberFormat,5));
    units.add(new NumberTickUnit(5000000L,numberFormat,5));
    units.add(new NumberTickUnit(50000000L,numberFormat,5));
    units.add(new NumberTickUnit(500000000L,numberFormat,5));
    units.add(new NumberTickUnit(5000000000L,numberFormat,5));
    units.add(new NumberTickUnit(50000000000L,numberFormat,5));
    return units;
  }
  /** 
 * Returns a collection of tick units for integer values. Uses a given Locale to create the DecimalFormats.
 * @param locale the locale to use to represent Numbers.
 * @return A collection of tick units for integer values.
 * @see #setStandardTickUnits(TickUnitSource)
 */
  public static TickUnitSource createIntegerTickUnits(  Locale locale){
    TickUnits units=new TickUnits();
    NumberFormat numberFormat=NumberFormat.getNumberInstance(locale);
    units.add(new NumberTickUnit(1,numberFormat,2));
    units.add(new NumberTickUnit(2,numberFormat,2));
    units.add(new NumberTickUnit(5,numberFormat,5));
    units.add(new NumberTickUnit(10,numberFormat,2));
    units.add(new NumberTickUnit(20,numberFormat,2));
    units.add(new NumberTickUnit(50,numberFormat,5));
    units.add(new NumberTickUnit(100,numberFormat,2));
    units.add(new NumberTickUnit(200,numberFormat,2));
    units.add(new NumberTickUnit(500,numberFormat,5));
    units.add(new NumberTickUnit(1000,numberFormat,2));
    units.add(new NumberTickUnit(2000,numberFormat,2));
    units.add(new NumberTickUnit(5000,numberFormat,5));
    units.add(new NumberTickUnit(10000,numberFormat,2));
    units.add(new NumberTickUnit(20000,numberFormat,2));
    units.add(new NumberTickUnit(50000,numberFormat,5));
    units.add(new NumberTickUnit(100000,numberFormat,2));
    units.add(new NumberTickUnit(200000,numberFormat,2));
    units.add(new NumberTickUnit(500000,numberFormat,5));
    units.add(new NumberTickUnit(1000000,numberFormat,2));
    units.add(new NumberTickUnit(2000000,numberFormat,2));
    units.add(new NumberTickUnit(5000000,numberFormat,5));
    units.add(new NumberTickUnit(10000000,numberFormat,2));
    units.add(new NumberTickUnit(20000000,numberFormat,2));
    units.add(new NumberTickUnit(50000000,numberFormat,5));
    units.add(new NumberTickUnit(100000000,numberFormat,2));
    units.add(new NumberTickUnit(200000000,numberFormat,2));
    units.add(new NumberTickUnit(500000000,numberFormat,5));
    units.add(new NumberTickUnit(1000000000,numberFormat,2));
    units.add(new NumberTickUnit(2000000000,numberFormat,2));
    units.add(new NumberTickUnit(5000000000.0,numberFormat,5));
    units.add(new NumberTickUnit(10000000000.0,numberFormat,2));
    return units;
  }
  /** 
 * Estimates the maximum tick label height.
 * @param g2  the graphics device.
 * @return The maximum height.
 */
  protected double estimateMaximumTickLabelHeight(  Graphics2D g2){
    RectangleInsets tickLabelInsets=getTickLabelInsets();
    double result=tickLabelInsets.getTop() + tickLabelInsets.getBottom();
    Font tickLabelFont=getTickLabelFont();
    FontRenderContext frc=g2.getFontRenderContext();
    result+=tickLabelFont.getLineMetrics("123",frc).getHeight();
    return result;
  }
  /** 
 * Estimates the maximum width of the tick labels, assuming the specified tick unit is used. <P> Rather than computing the string bounds of every tick on the axis, we just look at two values: the lower bound and the upper bound for the axis.  These two values will usually be representative.
 * @param g2  the graphics device.
 * @param unit  the tick unit to use for calculation.
 * @return The estimated maximum width of the tick labels.
 */
  protected double estimateMaximumTickLabelWidth(  Graphics2D g2,  TickUnit unit){
    RectangleInsets tickLabelInsets=getTickLabelInsets();
    double result=tickLabelInsets.getLeft() + tickLabelInsets.getRight();
    if (isVerticalTickLabels()) {
      FontRenderContext frc=g2.getFontRenderContext();
      LineMetrics lm=getTickLabelFont().getLineMetrics("0",frc);
      result+=lm.getHeight();
    }
 else {
      FontMetrics fm=g2.getFontMetrics(getTickLabelFont());
      Range range=getRange();
      double lower=range.getLowerBound();
      double upper=range.getUpperBound();
      String lowerStr="";
      String upperStr="";
      NumberFormat formatter=getNumberFormatOverride();
      if (formatter != null) {
        lowerStr=formatter.format(lower);
        upperStr=formatter.format(upper);
      }
 else {
        lowerStr=unit.valueToString(lower);
        upperStr=unit.valueToString(upper);
      }
      double w1=fm.stringWidth(lowerStr);
      double w2=fm.stringWidth(upperStr);
      result+=Math.max(w1,w2);
    }
    return result;
  }
  /** 
 * Selects an appropriate tick value for the axis.  The strategy is to display as many ticks as possible (selected from an array of 'standard' tick units) without the labels overlapping.
 * @param g2  the graphics device.
 * @param dataArea  the area defined by the axes.
 * @param edge  the axis location.
 */
  protected void selectAutoTickUnit(  Graphics2D g2,  Rectangle2D dataArea,  RectangleEdge edge){
    if (RectangleEdge.isTopOrBottom(edge)) {
      selectHorizontalAutoTickUnit(g2,dataArea,edge);
    }
 else {
      if (RectangleEdge.isLeftOrRight(edge)) {
        selectVerticalAutoTickUnit(g2,dataArea,edge);
      }
    }
  }
  /** 
 * Selects an appropriate tick value for the axis.  The strategy is to display as many ticks as possible (selected from an array of 'standard' tick units) without the labels overlapping.
 * @param g2  the graphics device.
 * @param dataArea  the area defined by the axes.
 * @param edge  the axis location.
 */
  protected void selectHorizontalAutoTickUnit(  Graphics2D g2,  Rectangle2D dataArea,  RectangleEdge edge){
    double tickLabelWidth=estimateMaximumTickLabelWidth(g2,getTickUnit());
    TickUnitSource tickUnits=getStandardTickUnits();
    TickUnit unit1=tickUnits.getCeilingTickUnit(getTickUnit());
    double unit1Width=lengthToJava2D(unit1.getSize(),dataArea,edge);
    double guess=(tickLabelWidth / unit1Width) * unit1.getSize();
    NumberTickUnit unit2=(NumberTickUnit)tickUnits.getCeilingTickUnit(guess);
    double unit2Width=lengthToJava2D(unit2.getSize(),dataArea,edge);
    tickLabelWidth=estimateMaximumTickLabelWidth(g2,unit2);
    if (tickLabelWidth > unit2Width) {
      unit2=(NumberTickUnit)tickUnits.getLargerTickUnit(unit2);
    }
    setTickUnit(unit2,false,false);
  }
  /** 
 * Selects an appropriate tick value for the axis.  The strategy is to display as many ticks as possible (selected from an array of 'standard' tick units) without the labels overlapping.
 * @param g2  the graphics device.
 * @param dataArea  the area in which the plot should be drawn.
 * @param edge  the axis location.
 */
  protected void selectVerticalAutoTickUnit(  Graphics2D g2,  Rectangle2D dataArea,  RectangleEdge edge){
    double tickLabelHeight=estimateMaximumTickLabelHeight(g2);
    TickUnitSource tickUnits=getStandardTickUnits();
    TickUnit unit1=tickUnits.getCeilingTickUnit(getTickUnit());
    double unitHeight=lengthToJava2D(unit1.getSize(),dataArea,edge);
    double guess=(tickLabelHeight / unitHeight) * unit1.getSize();
    NumberTickUnit unit2=(NumberTickUnit)tickUnits.getCeilingTickUnit(guess);
    double unit2Height=lengthToJava2D(unit2.getSize(),dataArea,edge);
    tickLabelHeight=estimateMaximumTickLabelHeight(g2);
    if (tickLabelHeight > unit2Height) {
      unit2=(NumberTickUnit)tickUnits.getLargerTickUnit(unit2);
    }
    setTickUnit(unit2,false,false);
  }
  /** 
 * Calculates the positions of the tick labels for the axis, storing the results in the tick label list (ready for drawing).
 * @param g2  the graphics device.
 * @param state  the axis state.
 * @param dataArea  the area in which the plot should be drawn.
 * @param edge  the location of the axis.
 * @return A list of ticks.
 */
  public List refreshTicks(  Graphics2D g2,  AxisState state,  Rectangle2D dataArea,  RectangleEdge edge){
    List result=new java.util.ArrayList();
    if (RectangleEdge.isTopOrBottom(edge)) {
      result=refreshTicksHorizontal(g2,dataArea,edge);
    }
 else {
      if (RectangleEdge.isLeftOrRight(edge)) {
        result=refreshTicksVertical(g2,dataArea,edge);
      }
    }
    return result;
  }
  /** 
 * Calculates the positions of the tick labels for the axis, storing the results in the tick label list (ready for drawing).
 * @param g2  the graphics device.
 * @param dataArea  the area in which the data should be drawn.
 * @param edge  the location of the axis.
 * @return A list of ticks.
 */
  protected List refreshTicksHorizontal(  Graphics2D g2,  Rectangle2D dataArea,  RectangleEdge edge){
    List result=new java.util.ArrayList();
    Font tickLabelFont=getTickLabelFont();
    g2.setFont(tickLabelFont);
    if (isAutoTickUnitSelection()) {
      selectAutoTickUnit(g2,dataArea,edge);
    }
    TickUnit tu=getTickUnit();
    double size=tu.getSize();
    int count=calculateVisibleTickCount();
    double lowestTickValue=calculateLowestVisibleTickValue();
    if (count <= ValueAxis.MAXIMUM_TICK_COUNT) {
      int minorTickSpaces=getMinorTickCount();
      if (minorTickSpaces <= 0) {
        minorTickSpaces=tu.getMinorTickCount();
      }
      for (int minorTick=1; minorTick < minorTickSpaces; minorTick++) {
        double minorTickValue=lowestTickValue - size * minorTick / minorTickSpaces;
        if (getRange().contains(minorTickValue)) {
          result.add(new NumberTick(TickType.MINOR,minorTickValue,"",TextAnchor.TOP_CENTER,TextAnchor.CENTER,0.0));
        }
      }
      for (int i=0; i < count; i++) {
        double currentTickValue=lowestTickValue + (i * size);
        String tickLabel;
        NumberFormat formatter=getNumberFormatOverride();
        if (formatter != null) {
          tickLabel=formatter.format(currentTickValue);
        }
 else {
          tickLabel=getTickUnit().valueToString(currentTickValue);
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
        result.add(tick);
        double nextTickValue=lowestTickValue + ((i + 1) * size);
        for (int minorTick=1; minorTick < minorTickSpaces; minorTick++) {
          double minorTickValue=currentTickValue + (nextTickValue - currentTickValue) * minorTick / minorTickSpaces;
          if (getRange().contains(minorTickValue)) {
            result.add(new NumberTick(TickType.MINOR,minorTickValue,"",TextAnchor.TOP_CENTER,TextAnchor.CENTER,0.0));
          }
        }
      }
    }
    return result;
  }
  /** 
 * Calculates the positions of the tick labels for the axis, storing the results in the tick label list (ready for drawing).
 * @param g2  the graphics device.
 * @param dataArea  the area in which the plot should be drawn.
 * @param edge  the location of the axis.
 * @return A list of ticks.
 */
  protected List refreshTicksVertical(  Graphics2D g2,  Rectangle2D dataArea,  RectangleEdge edge){
    List result=new java.util.ArrayList();
    result.clear();
    Font tickLabelFont=getTickLabelFont();
    g2.setFont(tickLabelFont);
    if (isAutoTickUnitSelection()) {
      selectAutoTickUnit(g2,dataArea,edge);
    }
    TickUnit tu=getTickUnit();
    double size=tu.getSize();
    int count=calculateVisibleTickCount();
    double lowestTickValue=calculateLowestVisibleTickValue();
    if (count <= ValueAxis.MAXIMUM_TICK_COUNT) {
      int minorTickSpaces=getMinorTickCount();
      if (minorTickSpaces <= 0) {
        minorTickSpaces=tu.getMinorTickCount();
      }
      for (int minorTick=1; minorTick < minorTickSpaces; minorTick++) {
        double minorTickValue=lowestTickValue - size * minorTick / minorTickSpaces;
        if (getRange().contains(minorTickValue)) {
          result.add(new NumberTick(TickType.MINOR,minorTickValue,"",TextAnchor.TOP_CENTER,TextAnchor.CENTER,0.0));
        }
      }
      for (int i=0; i < count; i++) {
        double currentTickValue=lowestTickValue + (i * size);
        String tickLabel;
        NumberFormat formatter=getNumberFormatOverride();
        if (formatter != null) {
          tickLabel=formatter.format(currentTickValue);
        }
 else {
          tickLabel=getTickUnit().valueToString(currentTickValue);
        }
        TextAnchor anchor=null;
        TextAnchor rotationAnchor=null;
        double angle=0.0;
        if (isVerticalTickLabels()) {
          if (edge == RectangleEdge.LEFT) {
            anchor=TextAnchor.BOTTOM_CENTER;
            rotationAnchor=TextAnchor.BOTTOM_CENTER;
            angle=-Math.PI / 2.0;
          }
 else {
            anchor=TextAnchor.BOTTOM_CENTER;
            rotationAnchor=TextAnchor.BOTTOM_CENTER;
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
        result.add(tick);
        double nextTickValue=lowestTickValue + ((i + 1) * size);
        for (int minorTick=1; minorTick < minorTickSpaces; minorTick++) {
          double minorTickValue=currentTickValue + (nextTickValue - currentTickValue) * minorTick / minorTickSpaces;
          if (getRange().contains(minorTickValue)) {
            result.add(new NumberTick(TickType.MINOR,minorTickValue,"",TextAnchor.TOP_CENTER,TextAnchor.CENTER,0.0));
          }
        }
      }
    }
    return result;
  }
  /** 
 * Returns a clone of the axis.
 * @return A clone
 * @throws CloneNotSupportedException if some component of the axis doesnot support cloning.
 */
  public Object clone() throws CloneNotSupportedException {
    NumberAxis clone=(NumberAxis)super.clone();
    if (this.numberFormatOverride != null) {
      clone.numberFormatOverride=(NumberFormat)this.numberFormatOverride.clone();
    }
    return clone;
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
    if (!(obj instanceof NumberAxis)) {
      return false;
    }
    NumberAxis that=(NumberAxis)obj;
    if (this.autoRangeIncludesZero != that.autoRangeIncludesZero) {
      return false;
    }
    if (this.autoRangeStickyZero != that.autoRangeStickyZero) {
      return false;
    }
    if (!ObjectUtilities.equal(this.tickUnit,that.tickUnit)) {
      return false;
    }
    if (!ObjectUtilities.equal(this.numberFormatOverride,that.numberFormatOverride)) {
      return false;
    }
    if (!this.rangeType.equals(that.rangeType)) {
      return false;
    }
    return super.equals(obj);
  }
  /** 
 * Returns a hash code for this object.
 * @return A hash code.
 */
  public int hashCode(){
    if (getLabel() != null) {
      return getLabel().hashCode();
    }
 else {
      return 0;
    }
  }
}
