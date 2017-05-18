package org.jfree.chart.encoders;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
/** 
 * A collection of utility methods for encoding images and returning them as a byte[] or writing them directly to an OutputStream.
 */
public class EncoderUtil {
  /** 
 * Encode the image in a specific format.
 * @param image  The image to be encoded.
 * @param format  The {@link ImageFormat} to use.
 * @return The byte[] that is the encoded image.
 * @throws IOException
 */
  public static byte[] encode(  BufferedImage image,  String format) throws IOException {
    ImageEncoder imageEncoder=ImageEncoderFactory.newInstance(format);
    return imageEncoder.encode(image);
  }
  /** 
 * Encode the image in a specific format.
 * @param image  The image to be encoded.
 * @param format  The {@link ImageFormat} to use.
 * @param encodeAlpha  Whether to encode alpha transparency (not supportedby all ImageEncoders).
 * @return The byte[] that is the encoded image.
 * @throws IOException
 */
  public static byte[] encode(  BufferedImage image,  String format,  boolean encodeAlpha) throws IOException {
    ImageEncoder imageEncoder=ImageEncoderFactory.newInstance(format,encodeAlpha);
    return imageEncoder.encode(image);
  }
  /** 
 * Encode the image in a specific format.
 * @param image  The image to be encoded.
 * @param format  The {@link ImageFormat} to use.
 * @param quality  The quality to use for the image encoding (not supportedby all ImageEncoders).
 * @return The byte[] that is the encoded image.
 * @throws IOException
 */
  public static byte[] encode(  BufferedImage image,  String format,  float quality) throws IOException {
    ImageEncoder imageEncoder=ImageEncoderFactory.newInstance(format,quality);
    return imageEncoder.encode(image);
  }
  /** 
 * Encode the image in a specific format.
 * @param image  The image to be encoded.
 * @param format  The {@link ImageFormat} to use.
 * @param quality  The quality to use for the image encoding (not supportedby all ImageEncoders).
 * @param encodeAlpha  Whether to encode alpha transparency (not supportedby all ImageEncoders).
 * @return The byte[] that is the encoded image.
 * @throws IOException
 */
  public static byte[] encode(  BufferedImage image,  String format,  float quality,  boolean encodeAlpha) throws IOException {
    ImageEncoder imageEncoder=ImageEncoderFactory.newInstance(format,quality,encodeAlpha);
    return imageEncoder.encode(image);
  }
  /** 
 * Encode the image in a specific format and write it to an OutputStream.
 * @param image  The image to be encoded.
 * @param format  The {@link ImageFormat} to use.
 * @param outputStream  The OutputStream to write the encoded image to.
 * @throws IOException
 */
  public static void writeBufferedImage(  BufferedImage image,  String format,  OutputStream outputStream) throws IOException {
    ImageEncoder imageEncoder=ImageEncoderFactory.newInstance(format);
    imageEncoder.encode(image,outputStream);
  }
  /** 
 * Encode the image in a specific format and write it to an OutputStream.
 * @param image  The image to be encoded.
 * @param format  The {@link ImageFormat} to use.
 * @param outputStream  The OutputStream to write the encoded image to.
 * @param quality  The quality to use for the image encoding (notsupported by all ImageEncoders).
 * @throws IOException
 */
  public static void writeBufferedImage(  BufferedImage image,  String format,  OutputStream outputStream,  float quality) throws IOException {
    ImageEncoder imageEncoder=ImageEncoderFactory.newInstance(format,quality);
    imageEncoder.encode(image,outputStream);
  }
  /** 
 * Encode the image in a specific format and write it to an OutputStream.
 * @param image  The image to be encoded.
 * @param format  The {@link ImageFormat} to use.
 * @param outputStream  The OutputStream to write the encoded image to.
 * @param encodeAlpha  Whether to encode alpha transparency (notsupported by all ImageEncoders).
 * @throws IOException
 */
  public static void writeBufferedImage(  BufferedImage image,  String format,  OutputStream outputStream,  boolean encodeAlpha) throws IOException {
    ImageEncoder imageEncoder=ImageEncoderFactory.newInstance(format,encodeAlpha);
    imageEncoder.encode(image,outputStream);
  }
  /** 
 * Encode the image in a specific format and write it to an OutputStream.
 * @param image  The image to be encoded.
 * @param format  The {@link ImageFormat} to use.
 * @param outputStream  The OutputStream to write the encoded image to.
 * @param quality  The quality to use for the image encoding (notsupported by all ImageEncoders).
 * @param encodeAlpha  Whether to encode alpha transparency (not supportedby all ImageEncoders).
 * @throws IOException
 */
  public static void writeBufferedImage(  BufferedImage image,  String format,  OutputStream outputStream,  float quality,  boolean encodeAlpha) throws IOException {
    ImageEncoder imageEncoder=ImageEncoderFactory.newInstance(format,quality,encodeAlpha);
    imageEncoder.encode(image,outputStream);
  }
}
