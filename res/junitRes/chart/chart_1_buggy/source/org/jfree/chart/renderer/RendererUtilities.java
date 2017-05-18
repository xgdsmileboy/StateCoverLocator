package org.jfree.chart.renderer;
import org.jfree.data.DomainOrder;
import org.jfree.data.xy.XYDataset;
/** 
 * Utility methods related to the rendering process.
 * @since 1.0.6
 */
public class RendererUtilities {
  /** 
 * Finds the lower index of the range of live items in the specified data series.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series index.
 * @param xLow  the lowest x-value in the live range.
 * @param xHigh  the highest x-value in the live range.
 * @return The index of the required item.
 * @since 1.0.6
 * @see #findLiveItemsUpperBound(XYDataset,int,double,double)
 */
  public static int findLiveItemsLowerBound(  XYDataset dataset,  int series,  double xLow,  double xHigh){
    if (dataset == null) {
      throw new IllegalArgumentException("Null 'dataset' argument.");
    }
    if (xLow >= xHigh) {
      throw new IllegalArgumentException("Requires xLow < xHigh.");
    }
    int itemCount=dataset.getItemCount(series);
    if (itemCount <= 1) {
      return 0;
    }
    if (dataset.getDomainOrder() == DomainOrder.ASCENDING) {
      int low=0;
      int high=itemCount - 1;
      double lowValue=dataset.getXValue(series,low);
      if (lowValue >= xLow) {
        return low;
      }
      double highValue=dataset.getXValue(series,high);
      if (highValue < xLow) {
        return high;
      }
      while (high - low > 1) {
        int mid=(low + high) / 2;
        double midV=dataset.getXValue(series,mid);
        if (midV >= xLow) {
          high=mid;
        }
 else {
          low=mid;
        }
      }
      return high;
    }
 else     if (dataset.getDomainOrder() == DomainOrder.DESCENDING) {
      int low=0;
      int high=itemCount - 1;
      double lowValue=dataset.getXValue(series,low);
      if (lowValue <= xHigh) {
        return low;
      }
      double highValue=dataset.getXValue(series,high);
      if (highValue > xHigh) {
        return high;
      }
      while (high - low > 1) {
        int mid=(low + high) / 2;
        double midV=dataset.getXValue(series,mid);
        if (midV > xHigh) {
          low=mid;
        }
 else {
          high=mid;
        }
        mid=(low + high) / 2;
      }
      return high;
    }
 else {
      int index=0;
      double x=dataset.getXValue(series,index);
      while (index < itemCount && (x < xLow || x > xHigh)) {
        index++;
        if (index < itemCount) {
          x=dataset.getXValue(series,index);
        }
      }
      return Math.min(Math.max(0,index),itemCount - 1);
    }
  }
  /** 
 * Finds the upper index of the range of live items in the specified data series.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series index.
 * @param xLow  the lowest x-value in the live range.
 * @param xHigh  the highest x-value in the live range.
 * @return The index of the required item.
 * @since 1.0.6
 * @see #findLiveItemsLowerBound(XYDataset,int,double,double)
 */
  public static int findLiveItemsUpperBound(  XYDataset dataset,  int series,  double xLow,  double xHigh){
    if (dataset == null) {
      throw new IllegalArgumentException("Null 'dataset' argument.");
    }
    if (xLow >= xHigh) {
      throw new IllegalArgumentException("Requires xLow < xHigh.");
    }
    int itemCount=dataset.getItemCount(series);
    if (itemCount <= 1) {
      return 0;
    }
    if (dataset.getDomainOrder() == DomainOrder.ASCENDING) {
      int low=0;
      int high=itemCount - 1;
      double lowValue=dataset.getXValue(series,low);
      if (lowValue > xHigh) {
        return low;
      }
      double highValue=dataset.getXValue(series,high);
      if (highValue <= xHigh) {
        return high;
      }
      int mid=(low + high) / 2;
      while (high - low > 1) {
        double midV=dataset.getXValue(series,mid);
        if (midV <= xHigh) {
          low=mid;
        }
 else {
          high=mid;
        }
        mid=(low + high) / 2;
      }
      return mid;
    }
 else     if (dataset.getDomainOrder() == DomainOrder.DESCENDING) {
      int low=0;
      int high=itemCount - 1;
      int mid=(low + high) / 2;
      double lowValue=dataset.getXValue(series,low);
      if (lowValue < xLow) {
        return low;
      }
      double highValue=dataset.getXValue(series,high);
      if (highValue >= xLow) {
        return high;
      }
      while (high - low > 1) {
        double midV=dataset.getXValue(series,mid);
        if (midV >= xLow) {
          low=mid;
        }
 else {
          high=mid;
        }
        mid=(low + high) / 2;
      }
      return mid;
    }
 else {
      int index=itemCount - 1;
      double x=dataset.getXValue(series,index);
      while (index >= 0 && (x < xLow || x > xHigh)) {
        index--;
        if (index >= 0) {
          x=dataset.getXValue(series,index);
        }
      }
      return Math.max(index,0);
    }
  }
  /** 
 * Finds a range of item indices that is guaranteed to contain all the x-values from x0 to x1 (inclusive).
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series index.
 * @param xLow  the lower bound of the x-value range.
 * @param xHigh  the upper bound of the x-value range.
 * @return The indices of the boundary items.
 */
  public static int[] findLiveItems(  XYDataset dataset,  int series,  double xLow,  double xHigh){
    int i0=findLiveItemsLowerBound(dataset,series,xLow,xHigh);
    int i1=findLiveItemsUpperBound(dataset,series,xLow,xHigh);
    if (i0 > i1) {
      i0=i1;
    }
    return new int[]{i0,i1};
  }
}
