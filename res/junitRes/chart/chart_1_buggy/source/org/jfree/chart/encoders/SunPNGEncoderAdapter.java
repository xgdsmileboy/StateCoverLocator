package org.jfree.chart.encoders;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
/** 
 * Adapter class for the Sun PNG Encoder.  The ImageEncoderFactory will only return a reference to this class by default if the library has been compiled under a JDK 1.4+ and is being run using a JDK 1.4+.
 */
public class SunPNGEncoderAdapter implements ImageEncoder {
  /** 
 * Get the quality of the image encoding (always 0.0).
 * @return A float representing the quality.
 */
  public float getQuality(){
    return 0.0f;
  }
  /** 
 * Set the quality of the image encoding (not supported in this ImageEncoder).
 * @param quality  A float representing the quality.
 */
  public void setQuality(  float quality){
  }
  /** 
 * Get whether the encoder should encode alpha transparency (always false).
 * @return Whether the encoder is encoding alpha transparency.
 */
  public boolean isEncodingAlpha(){
    return false;
  }
  /** 
 * Set whether the encoder should encode alpha transparency (not supported in this ImageEncoder).
 * @param encodingAlpha  Whether the encoder should encode alphatransparency.
 */
  public void setEncodingAlpha(  boolean encodingAlpha){
  }
  /** 
 * Encodes an image in PNG format.
 * @param bufferedImage  The image to be encoded.
 * @return The byte[] that is the encoded image.
 * @throws IOException
 */
  public byte[] encode(  BufferedImage bufferedImage) throws IOException {
    ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
    encode(bufferedImage,outputStream);
    return outputStream.toByteArray();
  }
  /** 
 * Encodes an image in PNG format and writes it to an OutputStream.
 * @param bufferedImage  The image to be encoded.
 * @param outputStream  The OutputStream to write the encoded image to.
 * @throws IOException
 */
  public void encode(  BufferedImage bufferedImage,  OutputStream outputStream) throws IOException {
    if (bufferedImage == null) {
      throw new IllegalArgumentException("Null 'image' argument.");
    }
    if (outputStream == null) {
      throw new IllegalArgumentException("Null 'outputStream' argument.");
    }
    ImageIO.write(bufferedImage,ImageFormat.PNG,outputStream);
  }
}
