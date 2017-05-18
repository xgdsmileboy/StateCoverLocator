package org.jfree.chart;
/** 
 * An interface that should be implemented by renderers that use a 3D effect. This allows the axes to mirror the same effect by querying the renderer.
 */
public interface Effect3D {
  /** 
 * Returns the x-offset (in Java2D units) for the 3D effect.
 * @return The offset.
 */
  public double getXOffset();
  /** 
 * Returns the y-offset (in Java2D units) for the 3D effect.
 * @return The offset.
 */
  public double getYOffset();
}
