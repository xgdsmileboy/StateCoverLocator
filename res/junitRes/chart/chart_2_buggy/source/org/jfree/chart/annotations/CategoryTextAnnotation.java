package org.jfree.chart.annotations;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.text.TextUtilities;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.data.category.CategoryDataset;
/** 
 * A text annotation that can be placed on a                                                                                               {@link CategoryPlot}.
 */
public class CategoryTextAnnotation extends TextAnnotation implements CategoryAnnotation, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=3333360090781320147L;
  /** 
 * The category. 
 */
  private Comparable category;
  /** 
 * The category anchor (START, MIDDLE, or END). 
 */
  private CategoryAnchor categoryAnchor;
  /** 
 * The value. 
 */
  private double value;
  /** 
 * Creates a new annotation to be displayed at the given location.
 * @param text  the text (<code>null</code> not permitted).
 * @param category  the category (<code>null</code> not permitted).
 * @param value  the value.
 */
  public CategoryTextAnnotation(  String text,  Comparable category,  double value){
    super(text);
    if (category == null) {
      throw new IllegalArgumentException("Null 'category' argument.");
    }
    this.category=category;
    this.value=value;
    this.categoryAnchor=CategoryAnchor.MIDDLE;
  }
  /** 
 * Returns the category.
 * @return The category (never <code>null</code>).
 * @see #setCategory(Comparable)
 */
  public Comparable getCategory(){
    return this.category;
  }
  /** 
 * Sets the category that the annotation attaches to.
 * @param category  the category (<code>null</code> not permitted).
 * @see #getCategory()
 */
  public void setCategory(  Comparable category){
    if (category == null) {
      throw new IllegalArgumentException("Null 'category' argument.");
    }
    this.category=category;
  }
  /** 
 * Returns the category anchor point.
 * @return The category anchor point.
 * @see #setCategoryAnchor(CategoryAnchor)
 */
  public CategoryAnchor getCategoryAnchor(){
    return this.categoryAnchor;
  }
  /** 
 * Sets the category anchor point and sends an                                                                                              {@link AnnotationChangeEvent} to all registered listeners.
 * @param anchor  the anchor point (<code>null</code> not permitted).
 * @see #getCategoryAnchor()
 */
  public void setCategoryAnchor(  CategoryAnchor anchor){
    if (anchor == null) {
      throw new IllegalArgumentException("Null 'anchor' argument.");
    }
    this.categoryAnchor=anchor;
    fireAnnotationChanged();
  }
  /** 
 * Returns the value that the annotation attaches to.
 * @return The value.
 * @see #setValue(double)
 */
  public double getValue(){
    return this.value;
  }
  /** 
 * Sets the value and sends an                                                                                              {@link AnnotationChangeEvent} to all registered listeners.
 * @param value  the value.
 * @see #getValue()
 */
  public void setValue(  double value){
    this.value=value;
    fireAnnotationChanged();
  }
  /** 
 * Draws the annotation.
 * @param g2  the graphics device.
 * @param plot  the plot.
 * @param dataArea  the data area.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param rendererIndex  the renderer index.
 * @param info  the plot info (<code>null</code> permitted).
 */
  public void draw(  Graphics2D g2,  CategoryPlot plot,  Rectangle2D dataArea,  CategoryAxis domainAxis,  ValueAxis rangeAxis,  int rendererIndex,  PlotRenderingInfo info){
    CategoryDataset dataset=plot.getDataset();
    int catIndex=dataset.getColumnIndex(this.category);
    int catCount=dataset.getColumnCount();
    float anchorX=0.0f;
    float anchorY=0.0f;
    PlotOrientation orientation=plot.getOrientation();
    RectangleEdge domainEdge=Plot.resolveDomainAxisLocation(plot.getDomainAxisLocation(),orientation);
    RectangleEdge rangeEdge=Plot.resolveRangeAxisLocation(plot.getRangeAxisLocation(),orientation);
    if (orientation == PlotOrientation.HORIZONTAL) {
      anchorY=(float)domainAxis.getCategoryJava2DCoordinate(this.categoryAnchor,catIndex,catCount,dataArea,domainEdge);
      anchorX=(float)rangeAxis.valueToJava2D(this.value,dataArea,rangeEdge);
    }
 else {
      if (orientation == PlotOrientation.VERTICAL) {
        anchorX=(float)domainAxis.getCategoryJava2DCoordinate(this.categoryAnchor,catIndex,catCount,dataArea,domainEdge);
        anchorY=(float)rangeAxis.valueToJava2D(this.value,dataArea,rangeEdge);
      }
    }
    g2.setFont(getFont());
    g2.setPaint(getPaint());
    TextUtilities.drawRotatedString(getText(),g2,anchorX,anchorY,getTextAnchor(),getRotationAngle(),getRotationAnchor());
  }
  /** 
 * Tests this object for equality with another.
 * @param obj  the object (<code>null</code> permitted).
 * @return <code>true</code> or <code>false</code>.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CategoryTextAnnotation)) {
      return false;
    }
    CategoryTextAnnotation that=(CategoryTextAnnotation)obj;
    if (!super.equals(obj)) {
      return false;
    }
    if (!this.category.equals(that.getCategory())) {
      return false;
    }
    if (!this.categoryAnchor.equals(that.getCategoryAnchor())) {
      return false;
    }
    if (this.value != that.getValue()) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=super.hashCode();
    result=37 * result + this.category.hashCode();
    result=37 * result + this.categoryAnchor.hashCode();
    long temp=Double.doubleToLongBits(this.value);
    result=37 * result + (int)(temp ^ (temp >>> 32));
    return result;
  }
  /** 
 * Returns a clone of the annotation.
 * @return A clone.
 * @throws CloneNotSupportedException  this class will not throw thisexception, but subclasses (if any) might.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
