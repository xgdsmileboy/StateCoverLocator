package org.jfree.chart.axis;
import java.io.Serializable;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.util.RectangleAnchor;
/** 
 * The attributes that control the position of the labels for the categories on a                                                                                                                                                                     {@link CategoryAxis}. Instances of this class are immutable and other JFreeChart classes rely upon this.
 */
public class CategoryLabelPosition implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=5168681143844183864L;
  /** 
 * The category anchor point. 
 */
  private RectangleAnchor categoryAnchor;
  /** 
 * The text block anchor. 
 */
  private TextBlockAnchor labelAnchor;
  /** 
 * The rotation anchor. 
 */
  private TextAnchor rotationAnchor;
  /** 
 * The rotation angle (in radians). 
 */
  private double angle;
  /** 
 * The width calculation type. 
 */
  private CategoryLabelWidthType widthType;
  /** 
 * The maximum label width as a percentage of the category space or the range space.
 */
  private float widthRatio;
  /** 
 * Creates a new position record with default settings.
 */
  public CategoryLabelPosition(){
    this(RectangleAnchor.CENTER,TextBlockAnchor.BOTTOM_CENTER,TextAnchor.CENTER,0.0,CategoryLabelWidthType.CATEGORY,0.95f);
  }
  /** 
 * Creates a new category label position record.
 * @param categoryAnchor  the category anchor (<code>null</code> notpermitted).
 * @param labelAnchor  the label anchor (<code>null</code> not permitted).
 */
  public CategoryLabelPosition(  RectangleAnchor categoryAnchor,  TextBlockAnchor labelAnchor){
    this(categoryAnchor,labelAnchor,TextAnchor.CENTER,0.0,CategoryLabelWidthType.CATEGORY,0.95f);
  }
  /** 
 * Creates a new category label position record.
 * @param categoryAnchor  the category anchor (<code>null</code> notpermitted).
 * @param labelAnchor  the label anchor (<code>null</code> not permitted).
 * @param widthType  the width type (<code>null</code> not permitted).
 * @param widthRatio  the maximum label width as a percentage (of thecategory space or the range space).
 */
  public CategoryLabelPosition(  RectangleAnchor categoryAnchor,  TextBlockAnchor labelAnchor,  CategoryLabelWidthType widthType,  float widthRatio){
    this(categoryAnchor,labelAnchor,TextAnchor.CENTER,0.0,widthType,widthRatio);
  }
  /** 
 * Creates a new position record.  The item label anchor is a point relative to the data item (dot, bar or other visual item) on a chart. The item label is aligned by aligning the text anchor with the item label anchor.
 * @param categoryAnchor  the category anchor (<code>null</code> notpermitted).
 * @param labelAnchor  the label anchor (<code>null</code> not permitted).
 * @param rotationAnchor  the rotation anchor (<code>null</code> notpermitted).
 * @param angle  the rotation angle (<code>null</code> not permitted).
 * @param widthType  the width type (<code>null</code> not permitted).
 * @param widthRatio  the maximum label width as a percentage (of thecategory space or the range space).
 */
  public CategoryLabelPosition(  RectangleAnchor categoryAnchor,  TextBlockAnchor labelAnchor,  TextAnchor rotationAnchor,  double angle,  CategoryLabelWidthType widthType,  float widthRatio){
    if (categoryAnchor == null) {
      throw new IllegalArgumentException("Null 'categoryAnchor' argument.");
    }
    if (labelAnchor == null) {
      throw new IllegalArgumentException("Null 'labelAnchor' argument.");
    }
    if (rotationAnchor == null) {
      throw new IllegalArgumentException("Null 'rotationAnchor' argument.");
    }
    if (widthType == null) {
      throw new IllegalArgumentException("Null 'widthType' argument.");
    }
    this.categoryAnchor=categoryAnchor;
    this.labelAnchor=labelAnchor;
    this.rotationAnchor=rotationAnchor;
    this.angle=angle;
    this.widthType=widthType;
    this.widthRatio=widthRatio;
  }
  /** 
 * Returns the item label anchor.
 * @return The item label anchor (never <code>null</code>).
 */
  public RectangleAnchor getCategoryAnchor(){
    return this.categoryAnchor;
  }
  /** 
 * Returns the text block anchor.
 * @return The text block anchor (never <code>null</code>).
 */
  public TextBlockAnchor getLabelAnchor(){
    return this.labelAnchor;
  }
  /** 
 * Returns the rotation anchor point.
 * @return The rotation anchor point (never <code>null</code>).
 */
  public TextAnchor getRotationAnchor(){
    return this.rotationAnchor;
  }
  /** 
 * Returns the angle of rotation for the label.
 * @return The angle (in radians).
 */
  public double getAngle(){
    return this.angle;
  }
  /** 
 * Returns the width calculation type.
 * @return The width calculation type (never <code>null</code>).
 */
  public CategoryLabelWidthType getWidthType(){
    return this.widthType;
  }
  /** 
 * Returns the ratio used to calculate the maximum category label width.
 * @return The ratio.
 */
  public float getWidthRatio(){
    return this.widthRatio;
  }
  /** 
 * Tests this instance for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CategoryLabelPosition)) {
      return false;
    }
    CategoryLabelPosition that=(CategoryLabelPosition)obj;
    if (!this.categoryAnchor.equals(that.categoryAnchor)) {
      return false;
    }
    if (!this.labelAnchor.equals(that.labelAnchor)) {
      return false;
    }
    if (!this.rotationAnchor.equals(that.rotationAnchor)) {
      return false;
    }
    if (this.angle != that.angle) {
      return false;
    }
    if (this.widthType != that.widthType) {
      return false;
    }
    if (this.widthRatio != that.widthRatio) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this object.
 * @return A hash code.
 */
  public int hashCode(){
    int result=19;
    result=37 * result + this.categoryAnchor.hashCode();
    result=37 * result + this.labelAnchor.hashCode();
    result=37 * result + this.rotationAnchor.hashCode();
    return result;
  }
}
