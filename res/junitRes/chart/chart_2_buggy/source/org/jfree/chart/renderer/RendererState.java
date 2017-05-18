package org.jfree.chart.renderer;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.PlotRenderingInfo;
/** 
 * Represents the current state of a renderer.
 */
public class RendererState {
  /** 
 * The plot rendering info. 
 */
  private PlotRenderingInfo info;
  /** 
 * Creates a new state object.
 * @param info  the plot rendering info.
 */
  public RendererState(  PlotRenderingInfo info){
    this.info=info;
  }
  /** 
 * Returns the plot rendering info.
 * @return The info.
 */
  public PlotRenderingInfo getInfo(){
    return this.info;
  }
  /** 
 * A convenience method that returns a reference to the entity collection (may be <code>null</code>) being used to record chart entities.
 * @return The entity collection (possibly <code>null</code>).
 */
  public EntityCollection getEntityCollection(){
    EntityCollection result=null;
    if (this.info != null) {
      ChartRenderingInfo owner=this.info.getOwner();
      if (owner != null) {
        result=owner.getEntityCollection();
      }
    }
    return result;
  }
}
