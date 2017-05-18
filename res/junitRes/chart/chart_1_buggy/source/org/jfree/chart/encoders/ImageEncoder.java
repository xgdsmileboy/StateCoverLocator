package org.jfree.chart.encoders;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
/** 
 * Interface for abstracting different types of image encoders.
 */
public interface ImageEncoder {
  /** 
 * Encodes an image in a particular format.
 * @param bufferedImage  The image to be encoded.
 * @return The byte[] that is the encoded image.
 * @throws IOException
 */
  public byte[] encode(  BufferedImage bufferedImage) throws IOException ;
  /** 
 * Encodes an image in a particular format and writes it to an OutputStream.
 * @param bufferedImage  The image to be encoded.
 * @param outputStream  The OutputStream to write the encoded image to.
 * @throws IOException
 */
  public void encode(  BufferedImage bufferedImage,  OutputStream outputStream) throws IOException ;
  /** 
 * Get the quality of the image encoding.
 * @return A float representing the quality.
 */
  public float getQuality();
  /** 
 * Set the quality of the image encoding (not supported by all ImageEncoders).
 * @param quality  A float representing the quality.
 */
  public void setQuality(  float quality);
  /** 
 * Get whether the encoder should encode alpha transparency.
 * @return Whether the encoder is encoding alpha transparency.
 */
  public boolean isEncodingAlpha();
  /** 
 * Set whether the encoder should encode alpha transparency (not supported by all ImageEncoders).
 * @param encodingAlpha  Whether the encoder should encode alphatransparency.
 */
  public void setEncodingAlpha(  boolean encodingAlpha);
}
