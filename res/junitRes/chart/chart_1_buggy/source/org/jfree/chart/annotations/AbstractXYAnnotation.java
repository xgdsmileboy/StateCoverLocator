package org.jfree.chart.annotations;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYAnnotationEntity;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.util.ObjectUtilities;
/** 
 * The interface that must be supported by annotations that are to be added to an                               {@link XYPlot}.
 */
public abstract class AbstractXYAnnotation extends AbstractAnnotation implements XYAnnotation {
  /** 
 * The tool tip text. 
 */
  private String toolTipText;
  /** 
 * The URL. 
 */
  private String url;
  /** 
 * Creates a new instance that has no tool tip or URL specified.
 */
  protected AbstractXYAnnotation(){
    super();
    this.toolTipText=null;
    this.url=null;
  }
  /** 
 * Returns the tool tip text for the annotation.  This will be displayed in a                               {@link org.jfree.chart.ChartPanel} when the mouse pointer hovers overthe annotation.
 * @return The tool tip text (possibly <code>null</code>).
 * @see #setToolTipText(String)
 */
  public String getToolTipText(){
    return this.toolTipText;
  }
  /** 
 * Sets the tool tip text for the annotation.
 * @param text  the tool tip text (<code>null</code> permitted).
 * @see #getToolTipText()
 */
  public void setToolTipText(  String text){
    this.toolTipText=text;
  }
  /** 
 * Returns the URL for the annotation.  This URL will be used to provide hyperlinks when an HTML image map is created for the chart.
 * @return The URL (possibly <code>null</code>).
 * @see #setURL(String)
 */
  public String getURL(){
    return this.url;
  }
  /** 
 * Sets the URL for the annotation.
 * @param url  the URL (<code>null</code> permitted).
 * @see #getURL()
 */
  public void setURL(  String url){
    this.url=url;
  }
  /** 
 * Draws the annotation.
 * @param g2  the graphics device.
 * @param plot  the plot.
 * @param dataArea  the data area.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param rendererIndex  the renderer index.
 * @param info  if supplied, this info object will be populated withentity information.
 */
  public abstract void draw(  Graphics2D g2,  XYPlot plot,  Rectangle2D dataArea,  ValueAxis domainAxis,  ValueAxis rangeAxis,  int rendererIndex,  PlotRenderingInfo info);
  /** 
 * A utility method for adding an                               {@link XYAnnotationEntity} toa  {@link PlotRenderingInfo} instance.
 * @param info  the plot rendering info (<code>null</code> permitted).
 * @param hotspot  the hotspot area.
 * @param rendererIndex  the renderer index.
 * @param toolTipText  the tool tip text.
 * @param urlText  the URL text.
 */
  protected void addEntity(  PlotRenderingInfo info,  Shape hotspot,  int rendererIndex,  String toolTipText,  String urlText){
    if (info == null) {
      return;
    }
    EntityCollection entities=info.getOwner().getEntityCollection();
    if (entities == null) {
      return;
    }
    XYAnnotationEntity entity=new XYAnnotationEntity(hotspot,rendererIndex,toolTipText,urlText);
    entities.add(entity);
  }
  /** 
 * Tests this annotation for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof AbstractXYAnnotation)) {
      return false;
    }
    AbstractXYAnnotation that=(AbstractXYAnnotation)obj;
    if (!ObjectUtilities.equal(this.toolTipText,that.toolTipText)) {
      return false;
    }
    if (!ObjectUtilities.equal(this.url,that.url)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=193;
    if (this.toolTipText != null) {
      result=37 * result + this.toolTipText.hashCode();
    }
    if (this.url != null) {
      result=37 * result + this.url.hashCode();
    }
    return result;
  }
}
