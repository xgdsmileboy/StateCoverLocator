package org.jfree.chart.renderer.category;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.AreaRendererEndType;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.data.category.CategoryDataset;
/** 
 * A category item renderer that draws area charts.  You can use this renderer with the                                                                                               {@link CategoryPlot} class.  The example shown here is generatedby the <code>AreaChartDemo1.java</code> program included in the JFreeChart Demo Collection: <br><br> <img src="../../../../../images/AreaRendererSample.png" alt="AreaRendererSample.png" />
 */
public class AreaRenderer extends AbstractCategoryItemRenderer implements Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-4231878281385812757L;
  /** 
 * A flag that controls how the ends of the areas are drawn. 
 */
  private AreaRendererEndType endType;
  /** 
 * Creates a new renderer.
 */
  public AreaRenderer(){
    super();
    this.endType=AreaRendererEndType.TAPER;
    setBaseLegendShape(new Rectangle2D.Double(-4.0,-4.0,8.0,8.0));
  }
  /** 
 * Returns a token that controls how the renderer draws the end points. The default value is                                                                                               {@link AreaRendererEndType#TAPER}.
 * @return The end type (never <code>null</code>).
 * @see #setEndType
 */
  public AreaRendererEndType getEndType(){
    return this.endType;
  }
  /** 
 * Sets a token that controls how the renderer draws the end points, and sends a                                                                                               {@link RendererChangeEvent} to all registered listeners.
 * @param type  the end type (<code>null</code> not permitted).
 * @see #getEndType()
 */
  public void setEndType(  AreaRendererEndType type){
    if (type == null) {
      throw new IllegalArgumentException("Null 'type' argument.");
    }
    this.endType=type;
    fireChangeEvent();
  }
  /** 
 * Returns a legend item for a series.
 * @param datasetIndex  the dataset index (zero-based).
 * @param series  the series index (zero-based).
 * @return The legend item.
 */
  public LegendItem getLegendItem(  int datasetIndex,  int series){
    CategoryPlot cp=getPlot();
    if (cp == null) {
      return null;
    }
    if (!isSeriesVisible(series) || !isSeriesVisibleInLegend(series)) {
      return null;
    }
    CategoryDataset dataset=cp.getDataset(datasetIndex);
    String label=getLegendItemLabelGenerator().generateLabel(dataset,series);
    String description=label;
    String toolTipText=null;
    if (getLegendItemToolTipGenerator() != null) {
      toolTipText=getLegendItemToolTipGenerator().generateLabel(dataset,series);
    }
    String urlText=null;
    if (getLegendItemURLGenerator() != null) {
      urlText=getLegendItemURLGenerator().generateLabel(dataset,series);
    }
    Shape shape=lookupLegendShape(series);
    Paint paint=lookupSeriesPaint(series);
    Paint outlinePaint=lookupSeriesOutlinePaint(series);
    Stroke outlineStroke=lookupSeriesOutlineStroke(series);
    LegendItem result=new LegendItem(label,description,toolTipText,urlText,shape,paint,outlineStroke,outlinePaint);
    result.setLabelFont(lookupLegendTextFont(series));
    Paint labelPaint=lookupLegendTextPaint(series);
    if (labelPaint != null) {
      result.setLabelPaint(labelPaint);
    }
    result.setDataset(dataset);
    result.setDatasetIndex(datasetIndex);
    result.setSeriesKey(dataset.getRowKey(series));
    result.setSeriesIndex(series);
    return result;
  }
  /** 
 * Draw a single data item.
 * @param g2  the graphics device.
 * @param state  the renderer state.
 * @param dataArea  the data plot area.
 * @param plot  the plot.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param dataset  the dataset.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @param selected  is the item selected?
 * @param pass  the pass index.
 * @since 1.2.0
 */
  public void drawItem(  Graphics2D g2,  CategoryItemRendererState state,  Rectangle2D dataArea,  CategoryPlot plot,  CategoryAxis domainAxis,  ValueAxis rangeAxis,  CategoryDataset dataset,  int row,  int column,  boolean selected,  int pass){
    if (!getItemVisible(row,column)) {
      return;
    }
    Number value=dataset.getValue(row,column);
    if (value == null) {
      return;
    }
    PlotOrientation orientation=plot.getOrientation();
    RectangleEdge axisEdge=plot.getDomainAxisEdge();
    int count=dataset.getColumnCount();
    float x0=(float)domainAxis.getCategoryStart(column,count,dataArea,axisEdge);
    float x1=(float)domainAxis.getCategoryMiddle(column,count,dataArea,axisEdge);
    float x2=(float)domainAxis.getCategoryEnd(column,count,dataArea,axisEdge);
    x0=Math.round(x0);
    x1=Math.round(x1);
    x2=Math.round(x2);
    if (this.endType == AreaRendererEndType.TRUNCATE) {
      if (column == 0) {
        x0=x1;
      }
 else {
        if (column == getColumnCount() - 1) {
          x2=x1;
        }
      }
    }
    double yy1=value.doubleValue();
    double yy0=0.0;
    if (this.endType == AreaRendererEndType.LEVEL) {
      yy0=yy1;
    }
    if (column > 0) {
      Number n0=dataset.getValue(row,column - 1);
      if (n0 != null) {
        yy0=(n0.doubleValue() + yy1) / 2.0;
      }
    }
    double yy2=0.0;
    if (column < dataset.getColumnCount() - 1) {
      Number n2=dataset.getValue(row,column + 1);
      if (n2 != null) {
        yy2=(n2.doubleValue() + yy1) / 2.0;
      }
    }
 else {
      if (this.endType == AreaRendererEndType.LEVEL) {
        yy2=yy1;
      }
    }
    RectangleEdge edge=plot.getRangeAxisEdge();
    float y0=(float)rangeAxis.valueToJava2D(yy0,dataArea,edge);
    float y1=(float)rangeAxis.valueToJava2D(yy1,dataArea,edge);
    float y2=(float)rangeAxis.valueToJava2D(yy2,dataArea,edge);
    float yz=(float)rangeAxis.valueToJava2D(0.0,dataArea,edge);
    double labelXX=x1;
    double labelYY=y1;
    g2.setPaint(getItemPaint(row,column,selected));
    g2.setStroke(getItemStroke(row,column,selected));
    GeneralPath area=new GeneralPath();
    if (orientation == PlotOrientation.VERTICAL) {
      area.moveTo(x0,yz);
      area.lineTo(x0,y0);
      area.lineTo(x1,y1);
      area.lineTo(x2,y2);
      area.lineTo(x2,yz);
    }
 else {
      if (orientation == PlotOrientation.HORIZONTAL) {
        area.moveTo(yz,x0);
        area.lineTo(y0,x0);
        area.lineTo(y1,x1);
        area.lineTo(y2,x2);
        area.lineTo(yz,x2);
        double temp=labelXX;
        labelXX=labelYY;
        labelYY=temp;
      }
    }
    area.closePath();
    g2.setPaint(getItemPaint(row,column,selected));
    g2.fill(area);
    if (isItemLabelVisible(row,column,selected)) {
      drawItemLabel(g2,orientation,dataset,row,column,selected,labelXX,labelYY,(value.doubleValue() < 0.0));
    }
    int datasetIndex=plot.indexOf(dataset);
    updateCrosshairValues(state.getCrosshairState(),dataset.getRowKey(row),dataset.getColumnKey(column),yy1,datasetIndex,x1,y1,orientation);
    EntityCollection entities=state.getEntityCollection();
    if (entities != null) {
      addEntity(entities,area,dataset,row,column,selected);
    }
  }
  /** 
 * Tests this instance for equality with an arbitrary object.
 * @param obj  the object to test (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof AreaRenderer)) {
      return false;
    }
    AreaRenderer that=(AreaRenderer)obj;
    if (!this.endType.equals(that.endType)) {
      return false;
    }
    return super.equals(obj);
  }
  /** 
 * Returns an independent copy of the renderer.
 * @return A clone.
 * @throws CloneNotSupportedException  should not happen.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
