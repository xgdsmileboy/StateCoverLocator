package org.jfree.data.general;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
/** 
 * A utility class for the                              {@link HeatMapDataset}.
 * @since 1.0.13
 */
public abstract class HeatMapUtilities {
  /** 
 * Returns a dataset containing one series that holds a copy of the (x, z) data from one row (y-index) of the specified dataset.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param row  the row (y) index.
 * @param seriesName  the series name/key (<code>null</code> not permitted).
 * @return The dataset.
 */
  public static XYDataset extractRowFromHeatMapDataset(  HeatMapDataset dataset,  int row,  Comparable seriesName){
    XYSeries series=new XYSeries(seriesName);
    int cols=dataset.getXSampleCount();
    for (int c=0; c < cols; c++) {
      series.add(dataset.getXValue(c),dataset.getZValue(c,row));
    }
    XYSeriesCollection result=new XYSeriesCollection(series);
    return result;
  }
  /** 
 * Returns a dataset containing one series that holds a copy of the (y, z) data from one column (x-index) of the specified dataset.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param column  the column (x) index.
 * @param seriesName  the series name (<code>null</code> not permitted).
 * @return The dataset.
 */
  public static XYDataset extractColumnFromHeatMapDataset(  HeatMapDataset dataset,  int column,  Comparable seriesName){
    XYSeries series=new XYSeries(seriesName);
    int rows=dataset.getYSampleCount();
    for (int r=0; r < rows; r++) {
      series.add(dataset.getYValue(r),dataset.getZValue(column,r));
    }
    XYSeriesCollection result=new XYSeriesCollection(series);
    return result;
  }
  /** 
 * Creates an image that displays the values from the specified dataset.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param paintScale  the paint scale for the z-values (<code>null</code>not permitted).
 * @return A buffered image.
 */
  public static BufferedImage createHeatMapImage(  HeatMapDataset dataset,  PaintScale paintScale){
    if (dataset == null) {
      throw new IllegalArgumentException("Null 'dataset' argument.");
    }
    if (paintScale == null) {
      throw new IllegalArgumentException("Null 'paintScale' argument.");
    }
    int xCount=dataset.getXSampleCount();
    int yCount=dataset.getYSampleCount();
    BufferedImage image=new BufferedImage(xCount,yCount,BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2=image.createGraphics();
    for (int xIndex=0; xIndex < xCount; xIndex++) {
      for (int yIndex=0; yIndex < yCount; yIndex++) {
        double z=dataset.getZValue(xIndex,yIndex);
        Paint p=paintScale.getPaint(z);
        g2.setPaint(p);
        g2.fillRect(xIndex,yCount - yIndex - 1,1,1);
      }
    }
    return image;
  }
}
