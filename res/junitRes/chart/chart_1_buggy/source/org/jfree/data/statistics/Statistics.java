package org.jfree.data.statistics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/** 
 * A utility class that provides some common statistical functions.
 */
public abstract class Statistics {
  /** 
 * Returns the mean of an array of numbers.  This is equivalent to calling <code>calculateMean(values, true)</code>.
 * @param values  the values (<code>null</code> not permitted).
 * @return The mean.
 */
  public static double calculateMean(  Number[] values){
    return calculateMean(values,true);
  }
  /** 
 * Returns the mean of an array of numbers.
 * @param values  the values (<code>null</code> not permitted).
 * @param includeNullAndNaN  a flag that controls whether or not<code>null</code> and <code>Double.NaN</code> values are included in the calculation (if either is present in the array, the result is {@link Double#NaN}).
 * @return The mean.
 * @since 1.0.3
 */
  public static double calculateMean(  Number[] values,  boolean includeNullAndNaN){
    if (values == null) {
      throw new IllegalArgumentException("Null 'values' argument.");
    }
    double sum=0.0;
    double current;
    int counter=0;
    for (int i=0; i < values.length; i++) {
      if (values[i] != null) {
        current=values[i].doubleValue();
      }
 else {
        current=Double.NaN;
      }
      if (includeNullAndNaN || !Double.isNaN(current)) {
        sum=sum + current;
        counter++;
      }
    }
    double result=(sum / counter);
    return result;
  }
  /** 
 * Returns the mean of a collection of <code>Number</code> objects.
 * @param values  the values (<code>null</code> not permitted).
 * @return The mean.
 */
  public static double calculateMean(  Collection values){
    return calculateMean(values,true);
  }
  /** 
 * Returns the mean of a collection of <code>Number</code> objects.
 * @param values  the values (<code>null</code> not permitted).
 * @param includeNullAndNaN  a flag that controls whether or not<code>null</code> and <code>Double.NaN</code> values are included in the calculation (if either is present in the array, the result is {@link Double#NaN}).
 * @return The mean.
 * @since 1.0.3
 */
  public static double calculateMean(  Collection values,  boolean includeNullAndNaN){
    if (values == null) {
      throw new IllegalArgumentException("Null 'values' argument.");
    }
    int count=0;
    double total=0.0;
    Iterator iterator=values.iterator();
    while (iterator.hasNext()) {
      Object object=iterator.next();
      if (object == null) {
        if (includeNullAndNaN) {
          return Double.NaN;
        }
      }
 else {
        if (object instanceof Number) {
          Number number=(Number)object;
          double value=number.doubleValue();
          if (Double.isNaN(value)) {
            if (includeNullAndNaN) {
              return Double.NaN;
            }
          }
 else {
            total=total + number.doubleValue();
            count=count + 1;
          }
        }
      }
    }
    return total / count;
  }
  /** 
 * Calculates the median for a list of values (<code>Number</code> objects). The list of values will be copied, and the copy sorted, before calculating the median.  To avoid this step (if your list of values is already sorted), use the                               {@link #calculateMedian(List,boolean)}method.
 * @param values  the values (<code>null</code> permitted).
 * @return The median.
 */
  public static double calculateMedian(  List values){
    return calculateMedian(values,true);
  }
  /** 
 * Calculates the median for a list of values (<code>Number</code> objects). If <code>copyAndSort</code> is <code>false</code>, the list is assumed to be presorted in ascending order by value.
 * @param values  the values (<code>null</code> permitted).
 * @param copyAndSort  a flag that controls whether the list of values iscopied and sorted.
 * @return The median.
 */
  public static double calculateMedian(  List values,  boolean copyAndSort){
    double result=Double.NaN;
    if (values != null) {
      if (copyAndSort) {
        int itemCount=values.size();
        List copy=new ArrayList(itemCount);
        for (int i=0; i < itemCount; i++) {
          copy.add(i,values.get(i));
        }
        Collections.sort(copy);
        values=copy;
      }
      int count=values.size();
      if (count > 0) {
        if (count % 2 == 1) {
          if (count > 1) {
            Number value=(Number)values.get((count - 1) / 2);
            result=value.doubleValue();
          }
 else {
            Number value=(Number)values.get(0);
            result=value.doubleValue();
          }
        }
 else {
          Number value1=(Number)values.get(count / 2 - 1);
          Number value2=(Number)values.get(count / 2);
          result=(value1.doubleValue() + value2.doubleValue()) / 2.0;
        }
      }
    }
    return result;
  }
  /** 
 * Calculates the median for a sublist within a list of values (<code>Number</code> objects).
 * @param values  the values, in any order (<code>null</code> notpermitted).
 * @param start  the start index.
 * @param end  the end index.
 * @return The median.
 */
  public static double calculateMedian(  List values,  int start,  int end){
    return calculateMedian(values,start,end,true);
  }
  /** 
 * Calculates the median for a sublist within a list of values (<code>Number</code> objects).  The entire list will be sorted if the <code>ascending</code< argument is <code>false</code>.
 * @param values  the values (<code>null</code> not permitted).
 * @param start  the start index.
 * @param end  the end index.
 * @param copyAndSort  a flag that that controls whether the list of valuesis copied and sorted.
 * @return The median.
 */
  public static double calculateMedian(  List values,  int start,  int end,  boolean copyAndSort){
    double result=Double.NaN;
    if (copyAndSort) {
      List working=new ArrayList(end - start + 1);
      for (int i=start; i <= end; i++) {
        working.add(values.get(i));
      }
      Collections.sort(working);
      result=calculateMedian(working,false);
    }
 else {
      int count=end - start + 1;
      if (count > 0) {
        if (count % 2 == 1) {
          if (count > 1) {
            Number value=(Number)values.get(start + (count - 1) / 2);
            result=value.doubleValue();
          }
 else {
            Number value=(Number)values.get(start);
            result=value.doubleValue();
          }
        }
 else {
          Number value1=(Number)values.get(start + count / 2 - 1);
          Number value2=(Number)values.get(start + count / 2);
          result=(value1.doubleValue() + value2.doubleValue()) / 2.0;
        }
      }
    }
    return result;
  }
  /** 
 * Returns the standard deviation of a set of numbers.
 * @param data  the data (<code>null</code> or zero length array notpermitted).
 * @return The standard deviation of a set of numbers.
 */
  public static double getStdDev(  Number[] data){
    if (data == null) {
      throw new IllegalArgumentException("Null 'data' array.");
    }
    if (data.length == 0) {
      throw new IllegalArgumentException("Zero length 'data' array.");
    }
    double avg=calculateMean(data);
    double sum=0.0;
    for (int counter=0; counter < data.length; counter++) {
      double diff=data[counter].doubleValue() - avg;
      sum=sum + diff * diff;
    }
    return Math.sqrt(sum / (data.length - 1));
  }
  /** 
 * Fits a straight line to a set of (x, y) data, returning the slope and intercept.
 * @param xData  the x-data (<code>null</code> not permitted).
 * @param yData  the y-data (<code>null</code> not permitted).
 * @return A double array with the intercept in [0] and the slope in [1].
 */
  public static double[] getLinearFit(  Number[] xData,  Number[] yData){
    if (xData == null) {
      throw new IllegalArgumentException("Null 'xData' argument.");
    }
    if (yData == null) {
      throw new IllegalArgumentException("Null 'yData' argument.");
    }
    if (xData.length != yData.length) {
      throw new IllegalArgumentException("Statistics.getLinearFit(): array lengths must be equal.");
    }
    double[] result=new double[2];
    result[1]=getSlope(xData,yData);
    result[0]=calculateMean(yData) - result[1] * calculateMean(xData);
    return result;
  }
  /** 
 * Finds the slope of a regression line using least squares.
 * @param xData  the x-values (<code>null</code> not permitted).
 * @param yData  the y-values (<code>null</code> not permitted).
 * @return The slope.
 */
  public static double getSlope(  Number[] xData,  Number[] yData){
    if (xData == null) {
      throw new IllegalArgumentException("Null 'xData' argument.");
    }
    if (yData == null) {
      throw new IllegalArgumentException("Null 'yData' argument.");
    }
    if (xData.length != yData.length) {
      throw new IllegalArgumentException("Array lengths must be equal.");
    }
    double sx=0.0, sxx=0.0, sxy=0.0, sy=0.0;
    int counter;
    for (counter=0; counter < xData.length; counter++) {
      sx=sx + xData[counter].doubleValue();
      sxx=sxx + Math.pow(xData[counter].doubleValue(),2);
      sxy=sxy + yData[counter].doubleValue() * xData[counter].doubleValue();
      sy=sy + yData[counter].doubleValue();
    }
    return (sxy - (sx * sy) / counter) / (sxx - (sx * sx) / counter);
  }
  /** 
 * Calculates the correlation between two datasets.  Both arrays should contain the same number of items.  Null values are treated as zero. <P> Information about the correlation calculation was obtained from: http://trochim.human.cornell.edu/kb/statcorr.htm
 * @param data1  the first dataset.
 * @param data2  the second dataset.
 * @return The correlation.
 */
  public static double getCorrelation(  Number[] data1,  Number[] data2){
    if (data1 == null) {
      throw new IllegalArgumentException("Null 'data1' argument.");
    }
    if (data2 == null) {
      throw new IllegalArgumentException("Null 'data2' argument.");
    }
    if (data1.length != data2.length) {
      throw new IllegalArgumentException("'data1' and 'data2' arrays must have same length.");
    }
    int n=data1.length;
    double sumX=0.0;
    double sumY=0.0;
    double sumX2=0.0;
    double sumY2=0.0;
    double sumXY=0.0;
    for (int i=0; i < n; i++) {
      double x=0.0;
      if (data1[i] != null) {
        x=data1[i].doubleValue();
      }
      double y=0.0;
      if (data2[i] != null) {
        y=data2[i].doubleValue();
      }
      sumX=sumX + x;
      sumY=sumY + y;
      sumXY=sumXY + (x * y);
      sumX2=sumX2 + (x * x);
      sumY2=sumY2 + (y * y);
    }
    return (n * sumXY - sumX * sumY) / Math.pow((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY),0.5);
  }
  /** 
 * Returns a data set for a moving average on the data set passed in.
 * @param xData  an array of the x data.
 * @param yData  an array of the y data.
 * @param period  the number of data points to average
 * @return A double[][] the length of the data set in the first dimension,with two doubles for x and y in the second dimension
 */
  public static double[][] getMovingAverage(  Number[] xData,  Number[] yData,  int period){
    if (xData.length != yData.length) {
      throw new IllegalArgumentException("Array lengths must be equal.");
    }
    if (period > xData.length) {
      throw new IllegalArgumentException("Period can't be longer than dataset.");
    }
    double[][] result=new double[xData.length - period][2];
    for (int i=0; i < result.length; i++) {
      result[i][0]=xData[i + period].doubleValue();
      double sum=0.0;
      for (int j=0; j < period; j++) {
        sum+=yData[i + j].doubleValue();
      }
      sum=sum / period;
      result[i][1]=sum;
    }
    return result;
  }
}
