package org.jfree.chart.title;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.block.Arrangement;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BlockResult;
import org.jfree.chart.block.EntityBlockParams;
import org.jfree.chart.block.EntityBlockResult;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.LegendItemEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.data.general.Dataset;
/** 
 * A container that holds all the pieces of a single legend item.
 * @since 1.0.2
 */
public class LegendItemBlockContainer extends BlockContainer {
  /** 
 * The dataset.
 * @since 1.0.6
 */
  private Dataset dataset;
  /** 
 * The series key.
 * @since 1.0.6
 */
  private Comparable seriesKey;
  /** 
 * The tool tip text (can be <code>null</code>). 
 */
  private String toolTipText;
  /** 
 * The URL text (can be <code>null</code>). 
 */
  private String urlText;
  /** 
 * Creates a new legend item block.
 * @param arrangement  the arrangement.
 * @param dataset  the dataset.
 * @param seriesKey  the series key.
 * @since 1.0.6
 */
  public LegendItemBlockContainer(  Arrangement arrangement,  Dataset dataset,  Comparable seriesKey){
    super(arrangement);
    this.dataset=dataset;
    this.seriesKey=seriesKey;
  }
  /** 
 * Returns a reference to the dataset for the associated legend item.
 * @return A dataset reference.
 * @since 1.0.6
 */
  public Dataset getDataset(){
    return this.dataset;
  }
  /** 
 * Returns the series key.
 * @return The series key.
 * @since 1.0.6
 */
  public Comparable getSeriesKey(){
    return this.seriesKey;
  }
  /** 
 * Returns the tool tip text.
 * @return The tool tip text (possibly <code>null</code>).
 * @since 1.0.3
 */
  public String getToolTipText(){
    return this.toolTipText;
  }
  /** 
 * Sets the tool tip text.
 * @param text  the text (<code>null</code> permitted).
 * @since 1.0.3
 */
  public void setToolTipText(  String text){
    this.toolTipText=text;
  }
  /** 
 * Returns the URL text.
 * @return The URL text (possibly <code>null</code>).
 * @since 1.0.3
 */
  public String getURLText(){
    return this.urlText;
  }
  /** 
 * Sets the URL text.
 * @param text  the text (<code>null</code> permitted).
 * @since 1.0.3
 */
  public void setURLText(  String text){
    this.urlText=text;
  }
  /** 
 * Draws the block within the specified area.
 * @param g2  the graphics device.
 * @param area  the area.
 * @param params  passed on to blocks within the container(<code>null</code> permitted).
 * @return An instance of {@link EntityBlockResult}, or <code>null</code>.
 */
  public Object draw(  Graphics2D g2,  Rectangle2D area,  Object params){
    super.draw(g2,area,null);
    EntityBlockParams ebp=null;
    BlockResult r=new BlockResult();
    if (params instanceof EntityBlockParams) {
      ebp=(EntityBlockParams)params;
      if (ebp.getGenerateEntities()) {
        EntityCollection ec=new StandardEntityCollection();
        LegendItemEntity entity=new LegendItemEntity((Shape)area.clone());
        entity.setSeriesKey(this.seriesKey);
        entity.setDataset(this.dataset);
        entity.setToolTipText(getToolTipText());
        entity.setURLText(getURLText());
        ec.add(entity);
        r.setEntityCollection(ec);
      }
    }
    return r;
  }
}
