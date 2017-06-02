package org.jfree.data.time;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
/** 
 * A utility class for calculating moving averages of time series data.
 */
public class MovingAverage {
  /** 
 * Creates a new                                                                                                                                                               {@link TimeSeriesCollection} containing a moving averageseries for each series in the source collection.
 * @param source  the source collection.
 * @param suffix  the suffix added to each source series name to create thecorresponding moving average series name.
 * @param periodCount  the number of periods in the moving averagecalculation.
 * @param skip  the number of initial periods to skip.
 * @return A collection of moving average time series.
 */
  public static TimeSeriesCollection createMovingAverage(  TimeSeriesCollection source,  String suffix,  int periodCount,  int skip){
    if (source == null) {
      throw new IllegalArgumentException("Null 'source' argument.");
    }
    if (periodCount < 1) {
      throw new IllegalArgumentException("periodCount must be greater " + "than or equal to 1.");
    }
    TimeSeriesCollection result=new TimeSeriesCollection();
    for (int i=0; i < source.getSeriesCount(); i++) {
      TimeSeries sourceSeries=source.getSeries(i);
      TimeSeries maSeries=createMovingAverage(sourceSeries,sourceSeries.getKey() + suffix,periodCount,skip);
      result.addSeries(maSeries);
    }
    return result;
  }
  /** 
 * Creates a new                                                                                                                                                               {@link TimeSeries} containing moving average values forthe given series.  If the series is empty (contains zero items), the result is an empty series.
 * @param source  the source series.
 * @param name  the name of the new series.
 * @param periodCount  the number of periods used in the averagecalculation.
 * @param skip  the number of initial periods to skip.
 * @return The moving average series.
 */
  public static TimeSeries createMovingAverage(  TimeSeries source,  String name,  int periodCount,  int skip){
    if (source == null) {
      throw new IllegalArgumentException("Null source.");
    }
    if (periodCount < 1) {
      throw new IllegalArgumentException("periodCount must be greater " + "than or equal to 1.");
    }
    TimeSeries result=new TimeSeries(name);
    if (source.getItemCount() > 0) {
      long firstSerial=source.getTimePeriod(0).getSerialIndex() + skip;
      for (int i=source.getItemCount() - 1; i >= 0; i--) {
        RegularTimePeriod period=source.getTimePeriod(i);
        long serial=period.getSerialIndex();
        if (serial >= firstSerial) {
          int n=0;
          double sum=0.0;
          long serialLimit=period.getSerialIndex() - periodCount;
          int offset=0;
          boolean finished=false;
          while ((offset < periodCount) && (!finished)) {
            if ((i - offset) >= 0) {
              TimeSeriesDataItem item=source.getRawDataItem(i - offset);
              RegularTimePeriod p=item.getPeriod();
              Number v=item.getValue();
              long currentIndex=p.getSerialIndex();
              if (currentIndex > serialLimit) {
                if (v != null) {
                  sum=sum + v.doubleValue();
                  n=n + 1;
                }
              }
 else {
                finished=true;
              }
            }
            offset=offset + 1;
          }
          if (n > 0) {
            result.add(period,sum / n);
          }
 else {
            result.add(period,null);
          }
        }
      }
    }
    return result;
  }
  /** 
 * Creates a new                                                                                                                                                               {@link TimeSeries} containing moving average values forthe given series, calculated by number of points (irrespective of the 'age' of those points).  If the series is empty (contains zero items), the result is an empty series. <p> Developed by Benoit Xhenseval (www.ObjectLab.co.uk).
 * @param source  the source series.
 * @param name  the name of the new series.
 * @param pointCount  the number of POINTS used in the average calculation(not periods!)
 * @return The moving average series.
 */
  public static TimeSeries createPointMovingAverage(  TimeSeries source,  String name,  int pointCount){
    if (source == null) {
      throw new IllegalArgumentException("Null 'source'.");
    }
    if (pointCount < 2) {
      throw new IllegalArgumentException("periodCount must be greater " + "than or equal to 2.");
    }
    TimeSeries result=new TimeSeries(name);
    double rollingSumForPeriod=0.0;
    for (int i=0; i < source.getItemCount(); i++) {
      TimeSeriesDataItem current=source.getRawDataItem(i);
      RegularTimePeriod period=current.getPeriod();
      rollingSumForPeriod+=current.getValue().doubleValue();
      if (i > pointCount - 1) {
        TimeSeriesDataItem startOfMovingAvg=source.getRawDataItem(i - pointCount);
        rollingSumForPeriod-=startOfMovingAvg.getValue().doubleValue();
        result.add(period,rollingSumForPeriod / pointCount);
      }
 else {
        if (i == pointCount - 1) {
          result.add(period,rollingSumForPeriod / pointCount);
        }
      }
    }
    return result;
  }
  /** 
 * Creates a new                                                                                                                                                               {@link XYDataset} containing the moving averages of eachseries in the <code>source</code> dataset.
 * @param source  the source dataset.
 * @param suffix  the string to append to source series names to createtarget series names.
 * @param period  the averaging period.
 * @param skip  the length of the initial skip period.
 * @return The dataset.
 */
  public static XYDataset createMovingAverage(  XYDataset source,  String suffix,  long period,  long skip){
    return createMovingAverage(source,suffix,(double)period,(double)skip);
  }
  /** 
 * Creates a new                                                                                                                                                               {@link XYDataset} containing the moving averages of eachseries in the <code>source</code> dataset.
 * @param source  the source dataset.
 * @param suffix  the string to append to source series names to createtarget series names.
 * @param period  the averaging period.
 * @param skip  the length of the initial skip period.
 * @return The dataset.
 */
  public static XYDataset createMovingAverage(  XYDataset source,  String suffix,  double period,  double skip){
    if (source == null) {
      throw new IllegalArgumentException("Null source (XYDataset).");
    }
    XYSeriesCollection result=new XYSeriesCollection();
    for (int i=0; i < source.getSeriesCount(); i++) {
      XYSeries s=createMovingAverage(source,i,source.getSeriesKey(i) + suffix,period,skip);
      result.addSeries(s);
    }
    return result;
  }
  /** 
 * Creates a new                                                                                                                                                               {@link XYSeries} containing the moving averages of oneseries in the <code>source</code> dataset.
 * @param source  the source dataset.
 * @param series  the series index (zero based).
 * @param name  the name for the new series.
 * @param period  the averaging period.
 * @param skip  the length of the initial skip period.
 * @return The dataset.
 */
  public static XYSeries createMovingAverage(  XYDataset source,  int series,  String name,  double period,  double skip){
    if (source == null) {
      throw new IllegalArgumentException("Null source (XYDataset).");
    }
    if (period < Double.MIN_VALUE) {
      throw new IllegalArgumentException("period must be positive.");
    }
    if (skip < 0.0) {
      throw new IllegalArgumentException("skip must be >= 0.0.");
    }
    XYSeries result=new XYSeries(name);
    if (source.getItemCount(series) > 0) {
      double first=source.getXValue(series,0) + skip;
      for (int i=source.getItemCount(series) - 1; i >= 0; i--) {
        double x=source.getXValue(series,i);
        if (x >= first) {
          int n=0;
          double sum=0.0;
          double limit=x - period;
          int offset=0;
          boolean finished=false;
          while (!finished) {
            if ((i - offset) >= 0) {
              double xx=source.getXValue(series,i - offset);
              Number yy=source.getY(series,i - offset);
              if (xx > limit) {
                if (yy != null) {
                  sum=sum + yy.doubleValue();
                  n=n + 1;
                }
              }
 else {
                finished=true;
              }
            }
 else {
              finished=true;
            }
            offset=offset + 1;
          }
          if (n > 0) {
            result.add(x,sum / n);
          }
 else {
            result.add(x,null);
          }
        }
      }
    }
    return result;
  }
}
