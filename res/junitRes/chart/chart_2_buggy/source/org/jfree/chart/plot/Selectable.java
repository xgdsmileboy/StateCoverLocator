package org.jfree.chart.plot;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.RenderingSource;
/** 
 * An interface that should be implemented by a plot that supports mouse selection of data items.
 * @since 1.2.0
 */
public interface Selectable {
  /** 
 * Returns <code>true</code> if this plot supports selection by a point, and <code>false</code> otherwise.
 * @return A boolean.
 */
  public boolean canSelectByPoint();
  /** 
 * Returns <code>true</code> if this plot supports selection by a region, and <code>false</code> otherwise.
 * @return A boolean.
 */
  public boolean canSelectByRegion();
  /** 
 * Set the selection state for the data item(s) at the specified (x, y) coordinates in Java2D space.
 * @param x  the x-coordinate.
 * @param y  the y-coordinate.
 * @param dataArea  the data area.
 * @param source  the selection source (usually a chart panel, possibly<code>null</code>).
 */
  public void select(  double x,  double y,  Rectangle2D dataArea,  RenderingSource source);
  /** 
 * Set the selection state for the data item(s) within the specified region in Java2D space.
 * @param region  the region.
 * @param dataArea  the data area.
 * @param source  the selection source (usually a chart panel, possibly<code>null</code>).
 */
  public void select(  GeneralPath region,  Rectangle2D dataArea,  RenderingSource source);
  /** 
 * Deselects all items.
 */
  public void clearSelection();
}
