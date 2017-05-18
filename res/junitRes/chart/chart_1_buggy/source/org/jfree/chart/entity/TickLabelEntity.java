package org.jfree.chart.entity;
import java.awt.Shape;
import java.io.Serializable;
/** 
 * A chart entity representing a tick label.
 */
public class TickLabelEntity extends ChartEntity implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=681583956588092095L;
  /** 
 * Creates a new entity.
 * @param area  the area (<code>null</code> not permitted).
 * @param toolTipText  the tool tip text (<code>null</code> permitted).
 * @param urlText  the URL text for HTML image maps (<code>null</code>permitted).
 */
  public TickLabelEntity(  Shape area,  String toolTipText,  String urlText){
    super(area,toolTipText,urlText);
  }
}
