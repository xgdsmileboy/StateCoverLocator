package org.jfree.chart.encoders;
import java.util.Hashtable;
/** 
 * Factory class for returning                                                                                                                                                                     {@link ImageEncoder}s for different                                                                                                                                                                    {@link ImageFormat}s.
 */
public class ImageEncoderFactory {
  private static Hashtable encoders=null;
static {
    init();
  }
  /** 
 * Sets up default encoders (uses Sun PNG Encoder if JDK 1.4+ and the SunPNGEncoderAdapter class is available).
 */
  private static void init(){
    encoders=new Hashtable();
    encoders.put("jpeg","org.jfree.chart.encoders.SunJPEGEncoderAdapter");
    try {
      Class.forName("javax.imageio.ImageIO");
      Class.forName("org.jfree.chart.encoders.SunPNGEncoderAdapter");
      encoders.put("png","org.jfree.chart.encoders.SunPNGEncoderAdapter");
      encoders.put("jpeg","org.jfree.chart.encoders.SunJPEGEncoderAdapter");
    }
 catch (    ClassNotFoundException e) {
      encoders.put("png","org.jfree.chart.encoders.KeypointPNGEncoderAdapter");
    }
  }
  /** 
 * Used to set additional encoders or replace default ones.
 * @param format  The image format name.
 * @param imageEncoderClassName  The name of the ImageEncoder class.
 */
  public static void setImageEncoder(  String format,  String imageEncoderClassName){
    encoders.put(format,imageEncoderClassName);
  }
  /** 
 * Used to retrieve an ImageEncoder for a specific image format.
 * @param format  The image format required.
 * @return The ImageEncoder or <code>null</code> if none available.
 */
  public static ImageEncoder newInstance(  String format){
    ImageEncoder imageEncoder=null;
    String className=(String)encoders.get(format);
    if (className == null) {
      throw new IllegalArgumentException("Unsupported image format - " + format);
    }
    try {
      Class imageEncoderClass=Class.forName(className);
      imageEncoder=(ImageEncoder)imageEncoderClass.newInstance();
    }
 catch (    Exception e) {
      throw new IllegalArgumentException(e.toString());
    }
    return imageEncoder;
  }
  /** 
 * Used to retrieve an ImageEncoder for a specific image format.
 * @param format  The image format required.
 * @param quality  The quality to be set before returning.
 * @return The ImageEncoder or <code>null</code> if none available.
 */
  public static ImageEncoder newInstance(  String format,  float quality){
    ImageEncoder imageEncoder=newInstance(format);
    imageEncoder.setQuality(quality);
    return imageEncoder;
  }
  /** 
 * Used to retrieve an ImageEncoder for a specific image format.
 * @param format  The image format required.
 * @param encodingAlpha  Sets whether alpha transparency should be encoded.
 * @return The ImageEncoder or <code>null</code> if none available.
 */
  public static ImageEncoder newInstance(  String format,  boolean encodingAlpha){
    ImageEncoder imageEncoder=newInstance(format);
    imageEncoder.setEncodingAlpha(encodingAlpha);
    return imageEncoder;
  }
  /** 
 * Used to retrieve an ImageEncoder for a specific image format.
 * @param format  The image format required.
 * @param quality  The quality to be set before returning.
 * @param encodingAlpha  Sets whether alpha transparency should be encoded.
 * @return The ImageEncoder or <code>null</code> if none available.
 */
  public static ImageEncoder newInstance(  String format,  float quality,  boolean encodingAlpha){
    ImageEncoder imageEncoder=newInstance(format);
    imageEncoder.setQuality(quality);
    imageEncoder.setEncodingAlpha(encodingAlpha);
    return imageEncoder;
  }
}
