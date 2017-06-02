package org.jfree.data.statistics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/** 
 * A utility class that calculates the mean, median, quartiles Q1 and Q3, plus a list of outlier values...all from an arbitrary list of <code>Number</code> objects.
 */
public abstract class BoxAndWhiskerCalculator {
  /** 
 * Calculates the statistics required for a                                                                                                                                                               {@link BoxAndWhiskerItem}from a list of <code>Number</code> objects.  Any items in the list that are <code>null</code>, not an instance of <code>Number</code>, or equivalent to <code>Double.NaN</code>, will be ignored.
 * @param values  a list of numbers (a <code>null</code> list is notpermitted).
 * @return A box-and-whisker item.
 */
  public static BoxAndWhiskerItem calculateBoxAndWhiskerStatistics(  List values){
    return calculateBoxAndWhiskerStatistics(values,true);
  }
  /** 
 * Calculates the statistics required for a                                                                                                                                                               {@link BoxAndWhiskerItem}from a list of <code>Number</code> objects.  Any items in the list that are <code>null</code>, not an instance of <code>Number</code>, or equivalent to <code>Double.NaN</code>, will be ignored.
 * @param values  a list of numbers (a <code>null</code> list is notpermitted).
 * @param stripNullAndNaNItems  a flag that controls the handling of nulland NaN items.
 * @return A box-and-whisker item.
 * @since 1.0.3
 */
  public static BoxAndWhiskerItem calculateBoxAndWhiskerStatistics(  List values,  boolean stripNullAndNaNItems){
    if (values == null) {
      throw new IllegalArgumentException("Null 'values' argument.");
    }
    List vlist;
    if (stripNullAndNaNItems) {
      vlist=new ArrayList(values.size());
      Iterator iterator=values.listIterator();
      while (iterator.hasNext()) {
        Object obj=iterator.next();
        if (obj instanceof Number) {
          Number n=(Number)obj;
          double v=n.doubleValue();
          if (!Double.isNaN(v)) {
            vlist.add(n);
          }
        }
      }
    }
 else {
      vlist=values;
    }
    Collections.sort(vlist);
    double mean=Statistics.calculateMean(vlist,false);
    double median=Statistics.calculateMedian(vlist,false);
    double q1=calculateQ1(vlist);
    double q3=calculateQ3(vlist);
    double interQuartileRange=q3 - q1;
    double upperOutlierThreshold=q3 + (interQuartileRange * 1.5);
    double lowerOutlierThreshold=q1 - (interQuartileRange * 1.5);
    double upperFaroutThreshold=q3 + (interQuartileRange * 2.0);
    double lowerFaroutThreshold=q1 - (interQuartileRange * 2.0);
    double minRegularValue=Double.POSITIVE_INFINITY;
    double maxRegularValue=Double.NEGATIVE_INFINITY;
    double minOutlier=Double.POSITIVE_INFINITY;
    double maxOutlier=Double.NEGATIVE_INFINITY;
    List outliers=new ArrayList();
    Iterator iterator=vlist.iterator();
    while (iterator.hasNext()) {
      Number number=(Number)iterator.next();
      double value=number.doubleValue();
      if (value > upperOutlierThreshold) {
        outliers.add(number);
        if (value > maxOutlier && value <= upperFaroutThreshold) {
          maxOutlier=value;
        }
      }
 else {
        if (value < lowerOutlierThreshold) {
          outliers.add(number);
          if (value < minOutlier && value >= lowerFaroutThreshold) {
            minOutlier=value;
          }
        }
 else {
          minRegularValue=Math.min(minRegularValue,value);
          maxRegularValue=Math.max(maxRegularValue,value);
        }
      }
      minOutlier=Math.min(minOutlier,minRegularValue);
      maxOutlier=Math.max(maxOutlier,maxRegularValue);
    }
    return new BoxAndWhiskerItem(new Double(mean),new Double(median),new Double(q1),new Double(q3),new Double(minRegularValue),new Double(maxRegularValue),new Double(minOutlier),new Double(maxOutlier),outliers);
  }
  /** 
 * Calculates the first quartile for a list of numbers in ascending order. If the items in the list are not in ascending order, the result is unspecified.  If the list contains items that are <code>null</code>, not an instance of <code>Number</code>, or equivalent to <code>Double.NaN</code>, the result is unspecified.
 * @param values  the numbers in ascending order (<code>null</code> notpermitted).
 * @return The first quartile.
 */
  public static double calculateQ1(  List values){
    if (values == null) {
      throw new IllegalArgumentException("Null 'values' argument.");
    }
    double result=Double.NaN;
    int count=values.size();
    if (count > 0) {
      if (count % 2 == 1) {
        if (count > 1) {
          result=Statistics.calculateMedian(values,0,count / 2);
        }
 else {
          result=Statistics.calculateMedian(values,0,0);
        }
      }
 else {
        result=Statistics.calculateMedian(values,0,count / 2 - 1);
      }
    }
    return result;
  }
  /** 
 * Calculates the third quartile for a list of numbers in ascending order. If the items in the list are not in ascending order, the result is unspecified.  If the list contains items that are <code>null</code>, not an instance of <code>Number</code>, or equivalent to <code>Double.NaN</code>, the result is unspecified.
 * @param values  the list of values (<code>null</code> not permitted).
 * @return The third quartile.
 */
  public static double calculateQ3(  List values){
    if (values == null) {
      throw new IllegalArgumentException("Null 'values' argument.");
    }
    double result=Double.NaN;
    int count=values.size();
    if (count > 0) {
      if (count % 2 == 1) {
        if (count > 1) {
          result=Statistics.calculateMedian(values,count / 2,count - 1);
        }
 else {
          result=Statistics.calculateMedian(values,0,0);
        }
      }
 else {
        result=Statistics.calculateMedian(values,count / 2,count - 1);
      }
    }
    return result;
  }
}
