package org.jfree.chart;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
/** 
 * A class used to represent a chart on the clipboard.
 * @since 1.0.13
 */
public class ChartTransferable implements Transferable {
  /** 
 * The data flavor. 
 */
  final DataFlavor imageFlavor=new DataFlavor("image/x-java-image; class=java.awt.Image","Image");
  /** 
 * The chart. 
 */
  private JFreeChart chart;
  /** 
 * The width of the chart on the clipboard. 
 */
  private int width;
  /** 
 * The height of the chart on the clipboard. 
 */
  private int height;
  /** 
 * The smallest width at which the chart will be drawn (if necessary, the chart will then be scaled down to fit the requested width).
 * @since 1.0.14
 */
  private int minDrawWidth;
  /** 
 * The smallest height at which the chart will be drawn (if necessary, the chart will then be scaled down to fit the requested height).
 * @since 1.0.14
 */
  private int minDrawHeight;
  /** 
 * The largest width at which the chart will be drawn (if necessary, the chart will then be scaled up to fit the requested width).
 * @since 1.0.14
 */
  private int maxDrawWidth;
  /** 
 * The largest height at which the chart will be drawn (if necessary, the chart will then be scaled up to fit the requested height).
 * @since 1.0.14
 */
  private int maxDrawHeight;
  /** 
 * Creates a new chart selection.
 * @param chart  the chart.
 * @param width  the chart width.
 * @param height  the chart height.
 */
  public ChartTransferable(  JFreeChart chart,  int width,  int height){
    this(chart,width,height,true);
  }
  /** 
 * Creates a new chart selection.
 * @param chart  the chart.
 * @param width  the chart width.
 * @param height  the chart height.
 * @param cloneData  clone the dataset(s)?
 */
  public ChartTransferable(  JFreeChart chart,  int width,  int height,  boolean cloneData){
    this(chart,width,height,0,0,Integer.MAX_VALUE,Integer.MAX_VALUE,true);
  }
  /** 
 * Creates a new chart selection.  The minimum and maximum drawing dimensions are used to match the scaling behaviour in the     {@link ChartPanel} class.
 * @param chart  the chart.
 * @param width  the chart width.
 * @param height  the chart height.
 * @param minDrawW  the minimum drawing width.
 * @param minDrawH  the minimum drawing height.
 * @param maxDrawW  the maximum drawing width.
 * @param maxDrawH  the maximum drawing height.
 * @param cloneData  clone the dataset(s)?
 * @since 1.0.14
 */
  public ChartTransferable(  JFreeChart chart,  int width,  int height,  int minDrawW,  int minDrawH,  int maxDrawW,  int maxDrawH,  boolean cloneData){
    try {
      this.chart=(JFreeChart)chart.clone();
    }
 catch (    CloneNotSupportedException e) {
      this.chart=chart;
    }
    this.width=width;
    this.height=height;
    this.minDrawWidth=minDrawW;
    this.minDrawHeight=minDrawH;
    this.maxDrawWidth=maxDrawW;
    this.maxDrawHeight=maxDrawH;
  }
  /** 
 * Returns the data flavors supported.
 * @return The data flavors supported.
 */
  public DataFlavor[] getTransferDataFlavors(){
    return new DataFlavor[]{this.imageFlavor};
  }
  /** 
 * Returns <code>true</code> if the specified flavor is supported.
 * @param flavor  the flavor.
 * @return A boolean.
 */
  public boolean isDataFlavorSupported(  DataFlavor flavor){
    return this.imageFlavor.equals(flavor);
  }
  /** 
 * Returns the content for the requested flavor, if it is supported.
 * @param flavor  the requested flavor.
 * @return The content.
 * @throws java.awt.datatransfer.UnsupportedFlavorException
 * @throws java.io.IOException
 */
  public Object getTransferData(  DataFlavor flavor) throws UnsupportedFlavorException, IOException {
    if (this.imageFlavor.equals(flavor)) {
      return createBufferedImage(this.chart,this.width,this.height,this.minDrawWidth,this.minDrawHeight,this.maxDrawWidth,this.maxDrawHeight);
    }
 else {
      throw new UnsupportedFlavorException(flavor);
    }
  }
  /** 
 * A utility method that creates an image of a chart, with scaling.
 * @param chart  the chart.
 * @param w  the image width.
 * @param h  the image height.
 * @param minDrawW  the minimum width for chart drawing.
 * @param minDrawH  the minimum height for chart drawing.
 * @param maxDrawW  the maximum width for chart drawing.
 * @param maxDrawH  the maximum height for chart drawing.
 * @return  A chart image.
 * @since 1.0.14
 */
  private BufferedImage createBufferedImage(  JFreeChart chart,  int w,  int h,  int minDrawW,  int minDrawH,  int maxDrawW,  int maxDrawH){
    BufferedImage image=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2=image.createGraphics();
    boolean scale=false;
    double drawWidth=w;
    double drawHeight=h;
    double scaleX=1.0;
    double scaleY=1.0;
    if (drawWidth < minDrawW) {
      scaleX=drawWidth / minDrawW;
      drawWidth=minDrawW;
      scale=true;
    }
 else {
      if (drawWidth > maxDrawW) {
        scaleX=drawWidth / maxDrawW;
        drawWidth=maxDrawW;
        scale=true;
      }
    }
    if (drawHeight < minDrawH) {
      scaleY=drawHeight / minDrawH;
      drawHeight=minDrawH;
      scale=true;
    }
 else {
      if (drawHeight > maxDrawH) {
        scaleY=drawHeight / maxDrawH;
        drawHeight=maxDrawH;
        scale=true;
      }
    }
    Rectangle2D chartArea=new Rectangle2D.Double(0.0,0.0,drawWidth,drawHeight);
    if (scale) {
      AffineTransform st=AffineTransform.getScaleInstance(scaleX,scaleY);
      g2.transform(st);
    }
    chart.draw(g2,chartArea,null,null);
    g2.dispose();
    return image;
  }
}
