package org.jfree.chart.entity;
import java.awt.Shape;
import org.jfree.chart.axis.Axis;
/** 
 * A chart entity that represents the label for an axis.
 * @since 1.2.0
 */
public class AxisLabelEntity extends ChartEntity {
  /** 
 * The axis. 
 */
  private Axis axis;
  /** 
 * Creates a new entity representing the label on an axis.
 * @param axis  the axis.
 * @param hotspot  the hotspot.
 * @param toolTipText  the tool tip text (<code>null</code> permitted).
 * @param url  the url (<code>null</code> permitted).
 */
  public AxisLabelEntity(  Axis axis,  Shape hotspot,  String toolTipText,  String url){
    super(hotspot,toolTipText,url);
    if (axis == null) {
      throw new IllegalArgumentException("Null 'axis' argument.");
    }
    this.axis=axis;
  }
  /** 
 * Returns the axis for this entity.
 * @return The axis.
 */
  public Axis getAxis(){
    return this.axis;
  }
  /** 
 * Returns a string representation of the chart entity, useful for debugging.
 * @return A string.
 */
  public String toString(){
    StringBuffer buf=new StringBuffer("AxisLabelEntity: ");
    buf.append("label = ");
    buf.append(this.axis.getLabel());
    return buf.toString();
  }
}
