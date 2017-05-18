package org.jfree.chart.util;
import java.awt.image.BufferedImage;
/** 
 * An interface that defines the API for a shadow generator.  Some plot classes use this to create drop shadows.
 * @since 1.0.14
 */
public interface ShadowGenerator {
  /** 
 * Creates and returns an image containing the drop shadow for the specified source image.
 * @param source  the source image.
 * @return A new image containing the shadow.
 */
  public BufferedImage createDropShadow(  BufferedImage source);
  /** 
 * Calculates the x-offset for drawing the shadow image relative to the source.
 * @return The x-offset.
 */
  public int calculateOffsetX();
  /** 
 * Calculates the y-offset for drawing the shadow image relative to the source.
 * @return The y-offset.
 */
  public int calculateOffsetY();
}
