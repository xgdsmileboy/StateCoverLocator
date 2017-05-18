package org.jfree.chart.entity;
import java.awt.Shape;
import java.io.Serializable;
/** 
 * A chart entity that represents an annotation on an                                                                                              {@link org.jfree.chart.plot.XYPlot}.
 */
public class XYAnnotationEntity extends ChartEntity implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=2340334068383660799L;
  /** 
 * The renderer index. 
 */
  private int rendererIndex;
  /** 
 * Creates a new entity.
 * @param hotspot  the area.
 * @param rendererIndex  the rendererIndex (zero-based index).
 * @param toolTipText  the tool tip text.
 * @param urlText  the URL text for HTML image maps.
 */
  public XYAnnotationEntity(  Shape hotspot,  int rendererIndex,  String toolTipText,  String urlText){
    super(hotspot,toolTipText,urlText);
    this.rendererIndex=rendererIndex;
  }
  /** 
 * Returns the renderer index.
 * @return The renderer index.
 */
  public int getRendererIndex(){
    return this.rendererIndex;
  }
  /** 
 * Sets the renderer index.
 * @param index  the item index (zero-based).
 */
  public void setRendererIndex(  int index){
    this.rendererIndex=index;
  }
  /** 
 * Tests the entity for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof XYAnnotationEntity)) {
      return false;
    }
    XYAnnotationEntity that=(XYAnnotationEntity)obj;
    if (this.rendererIndex != that.rendererIndex) {
      return false;
    }
    return true;
  }
}
