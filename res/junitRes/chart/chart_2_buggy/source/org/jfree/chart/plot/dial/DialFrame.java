package org.jfree.chart.plot.dial;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
/** 
 * A dial frame is the face plate for a dial plot - it is always drawn last. JFreeChart includes a couple of implementations of this interface (                                                                                              {@link StandardDialFrame} and {@link ArcDialFrame}). <br><br> Classes that implement this interface should be                                                                                               {@link Serializable}, otherwise chart serialization may fail.
 * @since 1.0.7
 */
public interface DialFrame extends DialLayer {
  /** 
 * Returns the shape of the viewing window for the dial, or <code>null</code> if the dial is completely open.  Other layers in the plot will rely on their drawing to be clipped within this window.
 * @param frame  the reference frame for the dial.
 * @return The window.
 */
  public Shape getWindow(  Rectangle2D frame);
}
