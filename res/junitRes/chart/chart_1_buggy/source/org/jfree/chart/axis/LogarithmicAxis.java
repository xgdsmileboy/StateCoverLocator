package org.jfree.chart.axis;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.ValueAxisPlot;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.data.Range;
/** 
 * A numerical axis that uses a logarithmic scale.
 */
public class LogarithmicAxis extends NumberAxis {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=2502918599004103054L;
  /** 
 * Useful constant for log(10). 
 */
  public static final double LOG10_VALUE=Math.log(10.0);
  /** 
 * Smallest arbitrarily-close-to-zero value allowed. 
 */
  public static final double SMALL_LOG_VALUE=1e-100;
  /** 
 * Flag set true to allow negative values in data. 
 */
  protected boolean allowNegativesFlag=false;
  /** 
 * Flag set true make axis throw exception if any values are <= 0 and 'allowNegativesFlag' is false.
 */
  protected boolean strictValuesFlag=true;
  /** 
 * Number formatter for generating numeric strings. 
 */
  protected final NumberFormat numberFormatterObj=NumberFormat.getInstance();
  /** 
 * Flag set true for "1e#"-style tick labels. 
 */
  protected boolean expTickLabelsFlag=false;
  /** 
 * Flag set true for "10^n"-style tick labels. 
 */
  protected boolean log10TickLabelsFlag=false;
  /** 
 * True to make 'autoAdjustRange()' select "10^n" values. 
 */
  protected boolean autoRangeNextLogFlag=false;
  /** 
 * Helper flag for log axis processing. 
 */
  protected boolean smallLogFlag=false;
  /** 
 * Creates a new axis.
 * @param label  the axis label.
 */
  public LogarithmicAxis(  String label){
    super(label);
    setupNumberFmtObj();
  }
  /** 
 * Sets the 'allowNegativesFlag' flag; true to allow negative values in data, false to be able to plot positive values arbitrarily close to zero.
 * @param flgVal  the new value of the flag.
 */
  public void setAllowNegativesFlag(  boolean flgVal){
    this.allowNegativesFlag=flgVal;
  }
  /** 
 * Returns the 'allowNegativesFlag' flag; true to allow negative values in data, false to be able to plot positive values arbitrarily close to zero.
 * @return The flag.
 */
  public boolean getAllowNegativesFlag(){
    return this.allowNegativesFlag;
  }
  /** 
 * Sets the 'strictValuesFlag' flag; if true and 'allowNegativesFlag' is false then this axis will throw a runtime exception if any of its values are less than or equal to zero; if false then the axis will adjust for values less than or equal to zero as needed.
 * @param flgVal true for strict enforcement.
 */
  public void setStrictValuesFlag(  boolean flgVal){
    this.strictValuesFlag=flgVal;
  }
  /** 
 * Returns the 'strictValuesFlag' flag; if true and 'allowNegativesFlag' is false then this axis will throw a runtime exception if any of its values are less than or equal to zero; if false then the axis will adjust for values less than or equal to zero as needed.
 * @return <code>true</code> if strict enforcement is enabled.
 */
  public boolean getStrictValuesFlag(){
    return this.strictValuesFlag;
  }
  /** 
 * Sets the 'expTickLabelsFlag' flag.  If the 'log10TickLabelsFlag' is false then this will set whether or not "1e#"-style tick labels are used.  The default is to use regular numeric tick labels.
 * @param flgVal true for "1e#"-style tick labels, false forlog10 or regular numeric tick labels.
 */
  public void setExpTickLabelsFlag(  boolean flgVal){
    this.expTickLabelsFlag=flgVal;
    setupNumberFmtObj();
  }
  /** 
 * Returns the 'expTickLabelsFlag' flag.
 * @return <code>true</code> for "1e#"-style tick labels,<code>false</code> for log10 or regular numeric tick labels.
 */
  public boolean getExpTickLabelsFlag(){
    return this.expTickLabelsFlag;
  }
  /** 
 * Sets the 'log10TickLabelsFlag' flag.  The default value is false.
 * @param flag true for "10^n"-style tick labels, false for "1e#"-styleor regular numeric tick labels.
 */
  public void setLog10TickLabelsFlag(  boolean flag){
    this.log10TickLabelsFlag=flag;
  }
  /** 
 * Returns the 'log10TickLabelsFlag' flag.
 * @return <code>true</code> for "10^n"-style tick labels,<code>false</code> for "1e#"-style or regular numeric tick labels.
 */
  public boolean getLog10TickLabelsFlag(){
    return this.log10TickLabelsFlag;
  }
  /** 
 * Sets the 'autoRangeNextLogFlag' flag.  This determines whether or not the 'autoAdjustRange()' method will select the next "10^n" values when determining the upper and lower bounds.  The default value is false.
 * @param flag <code>true</code> to make the 'autoAdjustRange()'method select the next "10^n" values, <code>false</code> to not.
 */
  public void setAutoRangeNextLogFlag(  boolean flag){
    this.autoRangeNextLogFlag=flag;
  }
  /** 
 * Returns the 'autoRangeNextLogFlag' flag.
 * @return <code>true</code> if the 'autoAdjustRange()' method willselect the next "10^n" values, <code>false</code> if not.
 */
  public boolean getAutoRangeNextLogFlag(){
    return this.autoRangeNextLogFlag;
  }
  /** 
 * Overridden version that calls original and then sets up flag for log axis processing.
 * @param range  the new range.
 */
  public void setRange(  Range range){
    super.setRange(range);
    setupSmallLogFlag();
  }
  /** 
 * Sets up flag for log axis processing.  Set true if negative values not allowed and the lower bound is between 0 and 10.
 */
  protected void setupSmallLogFlag(){
    double lowerVal=getRange().getLowerBound();
    this.smallLogFlag=(!this.allowNegativesFlag && lowerVal < 10.0 && lowerVal > 0.0);
  }
  /** 
 * Sets up the number formatter object according to the 'expTickLabelsFlag' flag.
 */
  protected void setupNumberFmtObj(){
    if (this.numberFormatterObj instanceof DecimalFormat) {
      ((DecimalFormat)this.numberFormatterObj).applyPattern(this.expTickLabelsFlag ? "0E0" : "0.###");
    }
  }
  /** 
 * Returns the log10 value, depending on if values between 0 and 1 are being plotted.  If negative values are not allowed and the lower bound is between 0 and 10 then a normal log is returned; otherwise the returned value is adjusted if the given value is less than 10.
 * @param val the value.
 * @return log<sub>10</sub>(val).
 * @see #switchedPow10(double)
 */
  protected double switchedLog10(  double val){
    return this.smallLogFlag ? Math.log(val) / LOG10_VALUE : adjustedLog10(val);
  }
  /** 
 * Returns a power of 10, depending on if values between 0 and 1 are being plotted.  If negative values are not allowed and the lower bound is between 0 and 10 then a normal power is returned; otherwise the returned value is adjusted if the given value is less than 1.
 * @param val the value.
 * @return 10<sup>val</sup>.
 * @since 1.0.5
 * @see #switchedLog10(double)
 */
  public double switchedPow10(  double val){
    return this.smallLogFlag ? Math.pow(10.0,val) : adjustedPow10(val);
  }
  /** 
 * Returns an adjusted log10 value for graphing purposes.  The first adjustment is that negative values are changed to positive during the calculations, and then the answer is negated at the end.  The second is that, for values less than 10, an increasingly large (0 to 1) scaling factor is added such that at 0 the value is adjusted to 1, resulting in a returned result of 0.
 * @param val  value for which log10 should be calculated.
 * @return An adjusted log<sub>10</sub>(val).
 * @see #adjustedPow10(double)
 */
  public double adjustedLog10(  double val){
    boolean negFlag=(val < 0.0);
    if (negFlag) {
      val=-val;
    }
    if (val < 10.0) {
      val+=(10.0 - val) / 10.0;
    }
    double res=Math.log(val) / LOG10_VALUE;
    return negFlag ? (-res) : res;
  }
  /** 
 * Returns an adjusted power of 10 value for graphing purposes.  The first adjustment is that negative values are changed to positive during the calculations, and then the answer is negated at the end.  The second is that, for values less than 1, a progressive logarithmic offset is subtracted such that at 0 the returned result is also 0.
 * @param val  value for which power of 10 should be calculated.
 * @return An adjusted 10<sup>val</sup>.
 * @since 1.0.5
 * @see #adjustedLog10(double)
 */
  public double adjustedPow10(  double val){
    boolean negFlag=(val < 0.0);
    if (negFlag) {
      val=-val;
    }
    double res;
    if (val < 1.0) {
      res=(Math.pow(10,val + 1.0) - 10.0) / 9.0;
    }
 else {
      res=Math.pow(10,val);
    }
    return negFlag ? (-res) : res;
  }
  /** 
 * Returns the largest (closest to positive infinity) double value that is not greater than the argument, is equal to a mathematical integer and satisfying the condition that log base 10 of the value is an integer (i.e., the value returned will be a power of 10: 1, 10, 100, 1000, etc.).
 * @param lower a double value below which a floor will be calcualted.
 * @return 10<sup>N</sup> with N .. { 1 ... }
 */
  protected double computeLogFloor(  double lower){
    double logFloor;
    if (this.allowNegativesFlag) {
      if (lower > 10.0) {
        logFloor=Math.log(lower) / LOG10_VALUE;
        logFloor=Math.floor(logFloor);
        logFloor=Math.pow(10,logFloor);
      }
 else {
        if (lower < -10.0) {
          logFloor=Math.log(-lower) / LOG10_VALUE;
          logFloor=Math.floor(-logFloor);
          logFloor=-Math.pow(10,-logFloor);
        }
 else {
          logFloor=Math.floor(lower);
        }
      }
    }
 else {
      if (lower > 0.0) {
        logFloor=Math.log(lower) / LOG10_VALUE;
        logFloor=Math.floor(logFloor);
        logFloor=Math.pow(10,logFloor);
      }
 else {
        logFloor=Math.floor(lower);
      }
    }
    return logFloor;
  }
  /** 
 * Returns the smallest (closest to negative infinity) double value that is not less than the argument, is equal to a mathematical integer and satisfying the condition that log base 10 of the value is an integer (i.e., the value returned will be a power of 10: 1, 10, 100, 1000, etc.).
 * @param upper a double value above which a ceiling will be calcualted.
 * @return 10<sup>N</sup> with N .. { 1 ... }
 */
  protected double computeLogCeil(  double upper){
    double logCeil;
    if (this.allowNegativesFlag) {
      if (upper > 10.0) {
        logCeil=Math.log(upper) / LOG10_VALUE;
        logCeil=Math.ceil(logCeil);
        logCeil=Math.pow(10,logCeil);
      }
 else {
        if (upper < -10.0) {
          logCeil=Math.log(-upper) / LOG10_VALUE;
          logCeil=Math.ceil(-logCeil);
          logCeil=-Math.pow(10,-logCeil);
        }
 else {
          logCeil=Math.ceil(upper);
        }
      }
    }
 else {
      if (upper > 0.0) {
        logCeil=Math.log(upper) / LOG10_VALUE;
        logCeil=Math.ceil(logCeil);
        logCeil=Math.pow(10,logCeil);
      }
 else {
        logCeil=Math.ceil(upper);
      }
    }
    return logCeil;
  }
  /** 
 * Rescales the axis to ensure that all data is visible.
 */
  public void autoAdjustRange(){
    Plot plot=getPlot();
    if (plot == null) {
      return;
    }
    if (plot instanceof ValueAxisPlot) {
      ValueAxisPlot vap=(ValueAxisPlot)plot;
      double lower;
      Range r=vap.getDataRange(this);
      if (r == null) {
        r=getDefaultAutoRange();
        lower=r.getLowerBound();
      }
 else {
        lower=r.getLowerBound();
        if (this.strictValuesFlag && !this.allowNegativesFlag && lower <= 0.0) {
          throw new RuntimeException("Values less than or equal to " + "zero not allowed with logarithmic axis");
        }
      }
      final double lowerMargin;
      if (lower > 0.0 && (lowerMargin=getLowerMargin()) > 0.0) {
        final double logLower=(Math.log(lower) / LOG10_VALUE);
        double logAbs;
        if ((logAbs=Math.abs(logLower)) < 1.0) {
          logAbs=1.0;
        }
        lower=Math.pow(10,(logLower - (logAbs * lowerMargin)));
      }
      if (this.autoRangeNextLogFlag) {
        lower=computeLogFloor(lower);
      }
      if (!this.allowNegativesFlag && lower >= 0.0 && lower < SMALL_LOG_VALUE) {
        lower=r.getLowerBound();
      }
      double upper=r.getUpperBound();
      final double upperMargin;
      if (upper > 0.0 && (upperMargin=getUpperMargin()) > 0.0) {
        final double logUpper=(Math.log(upper) / LOG10_VALUE);
        double logAbs;
        if ((logAbs=Math.abs(logUpper)) < 1.0) {
          logAbs=1.0;
        }
        upper=Math.pow(10,(logUpper + (logAbs * upperMargin)));
      }
      if (!this.allowNegativesFlag && upper < 1.0 && upper > 0.0 && lower > 0.0) {
        double expVal=Math.log(upper) / LOG10_VALUE;
        expVal=Math.ceil(-expVal + 0.001);
        expVal=Math.pow(10,expVal);
        upper=(expVal > 0.0) ? Math.ceil(upper * expVal) / expVal : Math.ceil(upper);
      }
 else {
        upper=(this.autoRangeNextLogFlag) ? computeLogCeil(upper) : Math.ceil(upper);
      }
      double minRange=getAutoRangeMinimumSize();
      if (upper - lower < minRange) {
        upper=(upper + lower + minRange) / 2;
        lower=(upper + lower - minRange) / 2;
        if (upper - lower < minRange) {
          double absUpper=Math.abs(upper);
          double adjVal=(absUpper > SMALL_LOG_VALUE) ? absUpper / 100.0 : 0.01;
          upper=(upper + lower + adjVal) / 2;
          lower=(upper + lower - adjVal) / 2;
        }
      }
      setRange(new Range(lower,upper),false,false);
      setupSmallLogFlag();
    }
  }
  /** 
 * Converts a data value to a coordinate in Java2D space, assuming that the axis runs along one edge of the specified plotArea. Note that it is possible for the coordinate to fall outside the plotArea.
 * @param value  the data value.
 * @param plotArea  the area for plotting the data.
 * @param edge  the axis location.
 * @return The Java2D coordinate.
 */
  public double valueToJava2D(  double value,  Rectangle2D plotArea,  RectangleEdge edge){
    Range range=getRange();
    double axisMin=switchedLog10(range.getLowerBound());
    double axisMax=switchedLog10(range.getUpperBound());
    double min=0.0;
    double max=0.0;
    if (RectangleEdge.isTopOrBottom(edge)) {
      min=plotArea.getMinX();
      max=plotArea.getMaxX();
    }
 else {
      if (RectangleEdge.isLeftOrRight(edge)) {
        min=plotArea.getMaxY();
        max=plotArea.getMinY();
      }
    }
    value=switchedLog10(value);
    if (isInverted()) {
      return max - (((value - axisMin) / (axisMax - axisMin)) * (max - min));
    }
 else {
      return min + (((value - axisMin) / (axisMax - axisMin)) * (max - min));
    }
  }
  /** 
 * Converts a coordinate in Java2D space to the corresponding data value, assuming that the axis runs along one edge of the specified plotArea.
 * @param java2DValue  the coordinate in Java2D space.
 * @param plotArea  the area in which the data is plotted.
 * @param edge  the axis location.
 * @return The data value.
 */
  public double java2DToValue(  double java2DValue,  Rectangle2D plotArea,  RectangleEdge edge){
    Range range=getRange();
    double axisMin=switchedLog10(range.getLowerBound());
    double axisMax=switchedLog10(range.getUpperBound());
    double plotMin=0.0;
    double plotMax=0.0;
    if (RectangleEdge.isTopOrBottom(edge)) {
      plotMin=plotArea.getX();
      plotMax=plotArea.getMaxX();
    }
 else {
      if (RectangleEdge.isLeftOrRight(edge)) {
        plotMin=plotArea.getMaxY();
        plotMax=plotArea.getMinY();
      }
    }
    if (isInverted()) {
      return switchedPow10(axisMax - ((java2DValue - plotMin) / (plotMax - plotMin)) * (axisMax - axisMin));
    }
 else {
      return switchedPow10(axisMin + ((java2DValue - plotMin) / (plotMax - plotMin)) * (axisMax - axisMin));
    }
  }
  /** 
 * Zooms in on the current range.
 * @param lowerPercent  the new lower bound.
 * @param upperPercent  the new upper bound.
 */
  public void zoomRange(  double lowerPercent,  double upperPercent){
    double startLog=switchedLog10(getRange().getLowerBound());
    double lengthLog=switchedLog10(getRange().getUpperBound()) - startLog;
    Range adjusted;
    if (isInverted()) {
      adjusted=new Range(switchedPow10(startLog + (lengthLog * (1 - upperPercent))),switchedPow10(startLog + (lengthLog * (1 - lowerPercent))));
    }
 else {
      adjusted=new Range(switchedPow10(startLog + (lengthLog * lowerPercent)),switchedPow10(startLog + (lengthLog * upperPercent)));
    }
    setRange(adjusted);
  }
  /** 
 * Calculates the positions of the tick labels for the axis, storing the results in the tick label list (ready for drawing).
 * @param g2  the graphics device.
 * @param dataArea  the area in which the plot should be drawn.
 * @param edge  the location of the axis.
 * @return A list of ticks.
 */
  protected List refreshTicksHorizontal(  Graphics2D g2,  Rectangle2D dataArea,  RectangleEdge edge){
    List ticks=new java.util.ArrayList();
    Range range=getRange();
    double lowerBoundVal=range.getLowerBound();
    if (this.smallLogFlag && lowerBoundVal < SMALL_LOG_VALUE) {
      lowerBoundVal=SMALL_LOG_VALUE;
    }
    double upperBoundVal=range.getUpperBound();
    int iBegCount=(int)Math.rint(switchedLog10(lowerBoundVal));
    int iEndCount=(int)Math.rint(switchedLog10(upperBoundVal));
    if (iBegCount == iEndCount && iBegCount > 0 && Math.pow(10,iBegCount) > lowerBoundVal) {
      --iBegCount;
    }
    double currentTickValue;
    String tickLabel;
    boolean zeroTickFlag=false;
    for (int i=iBegCount; i <= iEndCount; i++) {
      for (int j=0; j < 10; ++j) {
        if (this.smallLogFlag) {
          currentTickValue=Math.pow(10,i) + (Math.pow(10,i) * j);
          if (this.expTickLabelsFlag || (i < 0 && currentTickValue > 0.0 && currentTickValue < 1.0)) {
            if (j == 0 || (i > -4 && j < 2) || currentTickValue >= upperBoundVal) {
              this.numberFormatterObj.setMaximumFractionDigits(-i);
              tickLabel=makeTickLabel(currentTickValue,true);
            }
 else {
              tickLabel="";
            }
          }
 else {
            tickLabel=(j < 1 || (i < 1 && j < 5) || (j < 4 - i) || currentTickValue >= upperBoundVal) ? makeTickLabel(currentTickValue) : "";
          }
        }
 else {
          if (zeroTickFlag) {
            --j;
          }
          currentTickValue=(i >= 0) ? Math.pow(10,i) + (Math.pow(10,i) * j) : -(Math.pow(10,-i) - (Math.pow(10,-i - 1) * j));
          if (!zeroTickFlag) {
            if (Math.abs(currentTickValue - 1.0) < 0.0001 && lowerBoundVal <= 0.0 && upperBoundVal >= 0.0) {
              currentTickValue=0.0;
              zeroTickFlag=true;
            }
          }
 else {
            zeroTickFlag=false;
          }
          tickLabel=((this.expTickLabelsFlag && j < 2) || j < 1 || (i < 1 && j < 5) || (j < 4 - i) || currentTickValue >= upperBoundVal) ? makeTickLabel(currentTickValue) : "";
        }
        if (currentTickValue > upperBoundVal) {
          return ticks;
        }
        if (currentTickValue >= lowerBoundVal - SMALL_LOG_VALUE) {
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
    }
    return ticks;
  }
  /** 
 * Calculates the positions of the tick labels for the axis, storing the results in the tick label list (ready for drawing).
 * @param g2  the graphics device.
 * @param dataArea  the area in which the plot should be drawn.
 * @param edge  the location of the axis.
 * @return A list of ticks.
 */
  protected List refreshTicksVertical(  Graphics2D g2,  Rectangle2D dataArea,  RectangleEdge edge){
    List ticks=new java.util.ArrayList();
    double lowerBoundVal=getRange().getLowerBound();
    if (this.smallLogFlag && lowerBoundVal < SMALL_LOG_VALUE) {
      lowerBoundVal=SMALL_LOG_VALUE;
    }
    double upperBoundVal=getRange().getUpperBound();
    int iBegCount=(int)Math.rint(switchedLog10(lowerBoundVal));
    int iEndCount=(int)Math.rint(switchedLog10(upperBoundVal));
    if (iBegCount == iEndCount && iBegCount > 0 && Math.pow(10,iBegCount) > lowerBoundVal) {
      --iBegCount;
    }
    double tickVal;
    String tickLabel;
    boolean zeroTickFlag=false;
    for (int i=iBegCount; i <= iEndCount; i++) {
      int jEndCount=10;
      if (i == iEndCount) {
        jEndCount=1;
      }
      for (int j=0; j < jEndCount; j++) {
        if (this.smallLogFlag) {
          tickVal=Math.pow(10,i) + (Math.pow(10,i) * j);
          if (j == 0) {
            if (this.log10TickLabelsFlag) {
              tickLabel="10^" + i;
            }
 else {
              if (this.expTickLabelsFlag) {
                tickLabel="1e" + i;
              }
 else {
                if (i >= 0) {
                  NumberFormat format=getNumberFormatOverride();
                  if (format != null) {
                    tickLabel=format.format(tickVal);
                  }
 else {
                    tickLabel=Long.toString((long)Math.rint(tickVal));
                  }
                }
 else {
                  this.numberFormatterObj.setMaximumFractionDigits(-i);
                  tickLabel=this.numberFormatterObj.format(tickVal);
                }
              }
            }
          }
 else {
            tickLabel="";
          }
        }
 else {
          if (zeroTickFlag) {
            --j;
          }
          tickVal=(i >= 0) ? Math.pow(10,i) + (Math.pow(10,i) * j) : -(Math.pow(10,-i) - (Math.pow(10,-i - 1) * j));
          if (j == 0) {
            if (!zeroTickFlag) {
              if (i > iBegCount && i < iEndCount && Math.abs(tickVal - 1.0) < 0.0001) {
                tickVal=0.0;
                zeroTickFlag=true;
                tickLabel="0";
              }
 else {
                if (this.log10TickLabelsFlag) {
                  tickLabel=(((i < 0) ? "-" : "") + "10^" + Math.abs(i));
                }
 else {
                  if (this.expTickLabelsFlag) {
                    tickLabel=(((i < 0) ? "-" : "") + "1e" + Math.abs(i));
                  }
 else {
                    NumberFormat format=getNumberFormatOverride();
                    if (format != null) {
                      tickLabel=format.format(tickVal);
                    }
 else {
                      tickLabel=Long.toString((long)Math.rint(tickVal));
                    }
                  }
                }
              }
            }
 else {
              tickLabel="";
              zeroTickFlag=false;
            }
          }
 else {
            tickLabel="";
            zeroTickFlag=false;
          }
        }
        if (tickVal > upperBoundVal) {
          return ticks;
        }
        if (tickVal >= lowerBoundVal - SMALL_LOG_VALUE) {
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
          ticks.add(new NumberTick(new Double(tickVal),tickLabel,anchor,rotationAnchor,angle));
        }
      }
    }
    return ticks;
  }
  /** 
 * Converts the given value to a tick label string.
 * @param val the value to convert.
 * @param forceFmtFlag true to force the number-formatter objectto be used.
 * @return The tick label string.
 */
  protected String makeTickLabel(  double val,  boolean forceFmtFlag){
    if (this.expTickLabelsFlag || forceFmtFlag) {
      return this.numberFormatterObj.format(val).toLowerCase();
    }
    return getTickUnit().valueToString(val);
  }
  /** 
 * Converts the given value to a tick label string.
 * @param val the value to convert.
 * @return The tick label string.
 */
  protected String makeTickLabel(  double val){
    return makeTickLabel(val,false);
  }
}
