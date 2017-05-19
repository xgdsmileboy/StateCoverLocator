package org.jfree.chart;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.jfree.chart.imagemap.ImageMapUtilities;
import org.jfree.chart.imagemap.OverLIBToolTipTagFragmentGenerator;
import org.jfree.chart.imagemap.StandardToolTipTagFragmentGenerator;
import org.jfree.chart.imagemap.StandardURLTagFragmentGenerator;
import org.jfree.chart.imagemap.ToolTipTagFragmentGenerator;
import org.jfree.chart.imagemap.URLTagFragmentGenerator;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
/** 
 * A collection of utility methods for JFreeChart.  Includes methods for converting charts to image formats (PNG and JPEG) plus creating simple HTML image maps.
 * @see ImageMapUtilities
 */
public abstract class ChartUtilities {
  /** 
 * Applies the current theme to the specified chart.  This method is provided for convenience, the theme itself is stored in the                              {@link ChartFactory} class.
 * @param chart  the chart (<code>null</code> not permitted).
 * @since 1.0.11
 */
  public static void applyCurrentTheme(  JFreeChart chart){
    ChartFactory.getChartTheme().apply(chart);
  }
  /** 
 * Writes a chart to an output stream in PNG format.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @throws IOException if there are any I/O errors.
 */
  public static void writeChartAsPNG(  OutputStream out,  JFreeChart chart,  int width,  int height) throws IOException {
    writeChartAsPNG(out,chart,width,height,null);
  }
  /** 
 * Writes a chart to an output stream in PNG format.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @param encodeAlpha  encode alpha?
 * @param compression  the compression level (0-9).
 * @throws IOException if there are any I/O errors.
 */
  public static void writeChartAsPNG(  OutputStream out,  JFreeChart chart,  int width,  int height,  boolean encodeAlpha,  int compression) throws IOException {
    ChartUtilities.writeChartAsPNG(out,chart,width,height,null,encodeAlpha,compression);
  }
  /** 
 * Writes a chart to an output stream in PNG format.  This method allows you to pass in a                               {@link ChartRenderingInfo} object, to collectinformation about the chart dimensions/entities.  You will need this info if you want to create an HTML image map.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @param info  the chart rendering info (<code>null</code> permitted).
 * @throws IOException if there are any I/O errors.
 */
  public static void writeChartAsPNG(  OutputStream out,  JFreeChart chart,  int width,  int height,  ChartRenderingInfo info) throws IOException {
    if (chart == null) {
      throw new IllegalArgumentException("Null 'chart' argument.");
    }
    BufferedImage bufferedImage=chart.createBufferedImage(width,height,info);
    EncoderUtil.writeBufferedImage(bufferedImage,ImageFormat.PNG,out);
  }
  /** 
 * Writes a chart to an output stream in PNG format.  This method allows you to pass in a                               {@link ChartRenderingInfo} object, to collectinformation about the chart dimensions/entities.  You will need this info if you want to create an HTML image map.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @param info  carries back chart rendering info (<code>null</code>permitted).
 * @param encodeAlpha  encode alpha?
 * @param compression  the PNG compression level (0-9).
 * @throws IOException if there are any I/O errors.
 */
  public static void writeChartAsPNG(  OutputStream out,  JFreeChart chart,  int width,  int height,  ChartRenderingInfo info,  boolean encodeAlpha,  int compression) throws IOException {
    if (out == null) {
      throw new IllegalArgumentException("Null 'out' argument.");
    }
    if (chart == null) {
      throw new IllegalArgumentException("Null 'chart' argument.");
    }
    BufferedImage chartImage=chart.createBufferedImage(width,height,BufferedImage.TYPE_INT_ARGB,info);
    ChartUtilities.writeBufferedImageAsPNG(out,chartImage,encodeAlpha,compression);
  }
  /** 
 * Writes a scaled version of a chart to an output stream in PNG format.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the unscaled chart width.
 * @param height  the unscaled chart height.
 * @param widthScaleFactor  the horizontal scale factor.
 * @param heightScaleFactor  the vertical scale factor.
 * @throws IOException if there are any I/O problems.
 */
  public static void writeScaledChartAsPNG(  OutputStream out,  JFreeChart chart,  int width,  int height,  int widthScaleFactor,  int heightScaleFactor) throws IOException {
    if (out == null) {
      throw new IllegalArgumentException("Null 'out' argument.");
    }
    if (chart == null) {
      throw new IllegalArgumentException("Null 'chart' argument.");
    }
    double desiredWidth=width * widthScaleFactor;
    double desiredHeight=height * heightScaleFactor;
    double defaultWidth=width;
    double defaultHeight=height;
    boolean scale=false;
    if ((widthScaleFactor != 1) || (heightScaleFactor != 1)) {
      scale=true;
    }
    double scaleX=desiredWidth / defaultWidth;
    double scaleY=desiredHeight / defaultHeight;
    BufferedImage image=new BufferedImage((int)desiredWidth,(int)desiredHeight,BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2=image.createGraphics();
    if (scale) {
      AffineTransform saved=g2.getTransform();
      g2.transform(AffineTransform.getScaleInstance(scaleX,scaleY));
      chart.draw(g2,new Rectangle2D.Double(0,0,defaultWidth,defaultHeight),null,null);
      g2.setTransform(saved);
      g2.dispose();
    }
 else {
      chart.draw(g2,new Rectangle2D.Double(0,0,defaultWidth,defaultHeight),null,null);
    }
    out.write(encodeAsPNG(image));
  }
  /** 
 * Saves a chart to the specified file in PNG format.
 * @param file  the file name (<code>null</code> not permitted).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @throws IOException if there are any I/O errors.
 */
  public static void saveChartAsPNG(  File file,  JFreeChart chart,  int width,  int height) throws IOException {
    saveChartAsPNG(file,chart,width,height,null);
  }
  /** 
 * Saves a chart to a file in PNG format.  This method allows you to pass in a                               {@link ChartRenderingInfo} object, to collect information about thechart dimensions/entities.  You will need this info if you want to create an HTML image map.
 * @param file  the file (<code>null</code> not permitted).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @param info  the chart rendering info (<code>null</code> permitted).
 * @throws IOException if there are any I/O errors.
 */
  public static void saveChartAsPNG(  File file,  JFreeChart chart,  int width,  int height,  ChartRenderingInfo info) throws IOException {
    if (file == null) {
      throw new IllegalArgumentException("Null 'file' argument.");
    }
    OutputStream out=new BufferedOutputStream(new FileOutputStream(file));
    try {
      ChartUtilities.writeChartAsPNG(out,chart,width,height,info);
    }
  finally {
      out.close();
    }
  }
  /** 
 * Saves a chart to a file in PNG format.  This method allows you to pass in a                               {@link ChartRenderingInfo} object, to collect information about thechart dimensions/entities.  You will need this info if you want to create an HTML image map.
 * @param file  the file (<code>null</code> not permitted).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @param info  the chart rendering info (<code>null</code> permitted).
 * @param encodeAlpha  encode alpha?
 * @param compression  the PNG compression level (0-9).
 * @throws IOException if there are any I/O errors.
 */
  public static void saveChartAsPNG(  File file,  JFreeChart chart,  int width,  int height,  ChartRenderingInfo info,  boolean encodeAlpha,  int compression) throws IOException {
    if (file == null) {
      throw new IllegalArgumentException("Null 'file' argument.");
    }
    if (chart == null) {
      throw new IllegalArgumentException("Null 'chart' argument.");
    }
    OutputStream out=new BufferedOutputStream(new FileOutputStream(file));
    try {
      writeChartAsPNG(out,chart,width,height,info,encodeAlpha,compression);
    }
  finally {
      out.close();
    }
  }
  /** 
 * Writes a chart to an output stream in JPEG format.  Please note that JPEG is a poor format for chart images, use PNG if possible.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @throws IOException if there are any I/O errors.
 */
  public static void writeChartAsJPEG(  OutputStream out,  JFreeChart chart,  int width,  int height) throws IOException {
    writeChartAsJPEG(out,chart,width,height,null);
  }
  /** 
 * Writes a chart to an output stream in JPEG format.  Please note that JPEG is a poor format for chart images, use PNG if possible.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param quality  the quality setting.
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @throws IOException if there are any I/O errors.
 */
  public static void writeChartAsJPEG(  OutputStream out,  float quality,  JFreeChart chart,  int width,  int height) throws IOException {
    ChartUtilities.writeChartAsJPEG(out,quality,chart,width,height,null);
  }
  /** 
 * Writes a chart to an output stream in JPEG format. This method allows you to pass in a                               {@link ChartRenderingInfo} object, to collectinformation about the chart dimensions/entities.  You will need this info if you want to create an HTML image map.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @param info  the chart rendering info (<code>null</code> permitted).
 * @throws IOException if there are any I/O errors.
 */
  public static void writeChartAsJPEG(  OutputStream out,  JFreeChart chart,  int width,  int height,  ChartRenderingInfo info) throws IOException {
    if (chart == null) {
      throw new IllegalArgumentException("Null 'chart' argument.");
    }
    BufferedImage image=chart.createBufferedImage(width,height,BufferedImage.TYPE_INT_RGB,info);
    EncoderUtil.writeBufferedImage(image,ImageFormat.JPEG,out);
  }
  /** 
 * Writes a chart to an output stream in JPEG format.  This method allows you to pass in a                               {@link ChartRenderingInfo} object, to collectinformation about the chart dimensions/entities.  You will need this info if you want to create an HTML image map.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param quality  the output quality (0.0f to 1.0f).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @param info  the chart rendering info (<code>null</code> permitted).
 * @throws IOException if there are any I/O errors.
 */
  public static void writeChartAsJPEG(  OutputStream out,  float quality,  JFreeChart chart,  int width,  int height,  ChartRenderingInfo info) throws IOException {
    if (chart == null) {
      throw new IllegalArgumentException("Null 'chart' argument.");
    }
    BufferedImage image=chart.createBufferedImage(width,height,BufferedImage.TYPE_INT_RGB,info);
    EncoderUtil.writeBufferedImage(image,ImageFormat.JPEG,out,quality);
  }
  /** 
 * Saves a chart to a file in JPEG format.
 * @param file  the file (<code>null</code> not permitted).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @throws IOException if there are any I/O errors.
 */
  public static void saveChartAsJPEG(  File file,  JFreeChart chart,  int width,  int height) throws IOException {
    saveChartAsJPEG(file,chart,width,height,null);
  }
  /** 
 * Saves a chart to a file in JPEG format.
 * @param file  the file (<code>null</code> not permitted).
 * @param quality  the JPEG quality setting.
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @throws IOException if there are any I/O errors.
 */
  public static void saveChartAsJPEG(  File file,  float quality,  JFreeChart chart,  int width,  int height) throws IOException {
    saveChartAsJPEG(file,quality,chart,width,height,null);
  }
  /** 
 * Saves a chart to a file in JPEG format.  This method allows you to pass in a                               {@link ChartRenderingInfo} object, to collect information about thechart dimensions/entities.  You will need this info if you want to create an HTML image map.
 * @param file  the file name (<code>null</code> not permitted).
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @param info  the chart rendering info (<code>null</code> permitted).
 * @throws IOException if there are any I/O errors.
 */
  public static void saveChartAsJPEG(  File file,  JFreeChart chart,  int width,  int height,  ChartRenderingInfo info) throws IOException {
    if (file == null) {
      throw new IllegalArgumentException("Null 'file' argument.");
    }
    if (chart == null) {
      throw new IllegalArgumentException("Null 'chart' argument.");
    }
    OutputStream out=new BufferedOutputStream(new FileOutputStream(file));
    try {
      writeChartAsJPEG(out,chart,width,height,info);
    }
  finally {
      out.close();
    }
  }
  /** 
 * Saves a chart to a file in JPEG format.  This method allows you to pass in a                               {@link ChartRenderingInfo} object, to collect information about thechart dimensions/entities.  You will need this info if you want to create an HTML image map.
 * @param file  the file name (<code>null</code> not permitted).
 * @param quality  the quality setting.
 * @param chart  the chart (<code>null</code> not permitted).
 * @param width  the image width.
 * @param height  the image height.
 * @param info  the chart rendering info (<code>null</code> permitted).
 * @throws IOException if there are any I/O errors.
 */
  public static void saveChartAsJPEG(  File file,  float quality,  JFreeChart chart,  int width,  int height,  ChartRenderingInfo info) throws IOException {
    if (file == null) {
      throw new IllegalArgumentException("Null 'file' argument.");
    }
    if (chart == null) {
      throw new IllegalArgumentException("Null 'chart' argument.");
    }
    OutputStream out=new BufferedOutputStream(new FileOutputStream(file));
    try {
      writeChartAsJPEG(out,quality,chart,width,height,info);
    }
  finally {
      out.close();
    }
  }
  /** 
 * Writes a                               {@link BufferedImage} to an output stream in JPEG format.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param image  the image (<code>null</code> not permitted).
 * @throws IOException if there are any I/O errors.
 */
  public static void writeBufferedImageAsJPEG(  OutputStream out,  BufferedImage image) throws IOException {
    writeBufferedImageAsJPEG(out,0.75f,image);
  }
  /** 
 * Writes a                               {@link BufferedImage} to an output stream in JPEG format.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param quality  the image quality (0.0f to 1.0f).
 * @param image  the image (<code>null</code> not permitted).
 * @throws IOException if there are any I/O errors.
 */
  public static void writeBufferedImageAsJPEG(  OutputStream out,  float quality,  BufferedImage image) throws IOException {
    EncoderUtil.writeBufferedImage(image,ImageFormat.JPEG,out,quality);
  }
  /** 
 * Writes a                               {@link BufferedImage} to an output stream in PNG format.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param image  the image (<code>null</code> not permitted).
 * @throws IOException if there are any I/O errors.
 */
  public static void writeBufferedImageAsPNG(  OutputStream out,  BufferedImage image) throws IOException {
    EncoderUtil.writeBufferedImage(image,ImageFormat.PNG,out);
  }
  /** 
 * Writes a                               {@link BufferedImage} to an output stream in PNG format.
 * @param out  the output stream (<code>null</code> not permitted).
 * @param image  the image (<code>null</code> not permitted).
 * @param encodeAlpha  encode alpha?
 * @param compression  the compression level (0-9).
 * @throws IOException if there are any I/O errors.
 */
  public static void writeBufferedImageAsPNG(  OutputStream out,  BufferedImage image,  boolean encodeAlpha,  int compression) throws IOException {
    EncoderUtil.writeBufferedImage(image,ImageFormat.PNG,out,compression,encodeAlpha);
  }
  /** 
 * Encodes a                               {@link BufferedImage} to PNG format.
 * @param image  the image (<code>null</code> not permitted).
 * @return A byte array in PNG format.
 * @throws IOException if there is an I/O problem.
 */
  public static byte[] encodeAsPNG(  BufferedImage image) throws IOException {
    return EncoderUtil.encode(image,ImageFormat.PNG);
  }
  /** 
 * Encodes a                               {@link BufferedImage} to PNG format.
 * @param image  the image (<code>null</code> not permitted).
 * @param encodeAlpha  encode alpha?
 * @param compression  the PNG compression level (0-9).
 * @return The byte array in PNG format.
 * @throws IOException if there is an I/O problem.
 */
  public static byte[] encodeAsPNG(  BufferedImage image,  boolean encodeAlpha,  int compression) throws IOException {
    return EncoderUtil.encode(image,ImageFormat.PNG,compression,encodeAlpha);
  }
  /** 
 * Writes an image map to an output stream.
 * @param writer  the writer (<code>null</code> not permitted).
 * @param name  the map name (<code>null</code> not permitted).
 * @param info  the chart rendering info (<code>null</code> not permitted).
 * @param useOverLibForToolTips  whether to use OverLIB for tooltips(http://www.bosrup.com/web/overlib/).
 * @throws IOException if there are any I/O errors.
 */
  public static void writeImageMap(  PrintWriter writer,  String name,  ChartRenderingInfo info,  boolean useOverLibForToolTips) throws IOException {
    ToolTipTagFragmentGenerator toolTipTagFragmentGenerator=null;
    if (useOverLibForToolTips) {
      toolTipTagFragmentGenerator=new OverLIBToolTipTagFragmentGenerator();
    }
 else {
      toolTipTagFragmentGenerator=new StandardToolTipTagFragmentGenerator();
    }
    ImageMapUtilities.writeImageMap(writer,name,info,toolTipTagFragmentGenerator,new StandardURLTagFragmentGenerator());
  }
  /** 
 * Writes an image map to the specified writer.
 * @param writer  the writer (<code>null</code> not permitted).
 * @param name  the map name (<code>null</code> not permitted).
 * @param info  the chart rendering info (<code>null</code> not permitted).
 * @param toolTipTagFragmentGenerator  a generator for the HTML fragmentthat will contain the tooltip text (<code>null</code> not permitted if <code>info</code> contains tooltip information).
 * @param urlTagFragmentGenerator  a generator for the HTML fragment thatwill contain the URL reference (<code>null</code> not permitted if <code>info</code> contains URLs).
 * @throws IOException if there are any I/O errors.
 */
  public static void writeImageMap(  PrintWriter writer,  String name,  ChartRenderingInfo info,  ToolTipTagFragmentGenerator toolTipTagFragmentGenerator,  URLTagFragmentGenerator urlTagFragmentGenerator) throws IOException {
    writer.println(ImageMapUtilities.getImageMap(name,info,toolTipTagFragmentGenerator,urlTagFragmentGenerator));
  }
  /** 
 * Creates an HTML image map.  This method maps to                              {@link ImageMapUtilities#getImageMap(String,ChartRenderingInfo,ToolTipTagFragmentGenerator,URLTagFragmentGenerator)}, using default generators.
 * @param name  the map name (<code>null</code> not permitted).
 * @param info  the chart rendering info (<code>null</code> not permitted).
 * @return The map tag.
 */
  public static String getImageMap(  String name,  ChartRenderingInfo info){
    return ImageMapUtilities.getImageMap(name,info,new StandardToolTipTagFragmentGenerator(),new StandardURLTagFragmentGenerator());
  }
  /** 
 * Creates an HTML image map.  This method maps directly to                              {@link ImageMapUtilities#getImageMap(String,ChartRenderingInfo,ToolTipTagFragmentGenerator,URLTagFragmentGenerator)}.
 * @param name  the map name (<code>null</code> not permitted).
 * @param info  the chart rendering info (<code>null</code> not permitted).
 * @param toolTipTagFragmentGenerator  a generator for the HTML fragmentthat will contain the tooltip text (<code>null</code> not permitted if <code>info</code> contains tooltip information).
 * @param urlTagFragmentGenerator  a generator for the HTML fragment thatwill contain the URL reference (<code>null</code> not permitted if <code>info</code> contains URLs).
 * @return The map tag.
 */
  public static String getImageMap(  String name,  ChartRenderingInfo info,  ToolTipTagFragmentGenerator toolTipTagFragmentGenerator,  URLTagFragmentGenerator urlTagFragmentGenerator){
    return ImageMapUtilities.getImageMap(name,info,toolTipTagFragmentGenerator,urlTagFragmentGenerator);
  }
}
