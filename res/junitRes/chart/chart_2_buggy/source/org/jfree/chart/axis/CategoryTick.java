package org.jfree.chart.axis;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlock;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.util.ObjectUtilities;
/** 
 * A tick for a                                                                                               {@link CategoryAxis}.
 */
public class CategoryTick extends Tick {
  /** 
 * The category. 
 */
  private Comparable category;
  /** 
 * The label. 
 */
  private TextBlock label;
  /** 
 * The label anchor. 
 */
  private TextBlockAnchor labelAnchor;
  /** 
 * Creates a new tick.
 * @param category  the category.
 * @param label  the label.
 * @param labelAnchor  the label anchor.
 * @param rotationAnchor  the rotation anchor.
 * @param angle  the rotation angle (in radians).
 */
  public CategoryTick(  Comparable category,  TextBlock label,  TextBlockAnchor labelAnchor,  TextAnchor rotationAnchor,  double angle){
    super("",TextAnchor.CENTER,rotationAnchor,angle);
    this.category=category;
    this.label=label;
    this.labelAnchor=labelAnchor;
  }
  /** 
 * Returns the category.
 * @return The category.
 */
  public Comparable getCategory(){
    return this.category;
  }
  /** 
 * Returns the label.
 * @return The label.
 */
  public TextBlock getLabel(){
    return this.label;
  }
  /** 
 * Returns the label anchor.
 * @return The label anchor.
 */
  public TextBlockAnchor getLabelAnchor(){
    return this.labelAnchor;
  }
  /** 
 * Tests this category tick for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (obj instanceof CategoryTick && super.equals(obj)) {
      CategoryTick that=(CategoryTick)obj;
      if (!ObjectUtilities.equal(this.category,that.category)) {
        return false;
      }
      if (!ObjectUtilities.equal(this.label,that.label)) {
        return false;
      }
      if (!ObjectUtilities.equal(this.labelAnchor,that.labelAnchor)) {
        return false;
      }
      return true;
    }
    return false;
  }
  /** 
 * Returns a hash code for this object.
 * @return A hash code.
 */
  public int hashCode(){
    int result=41;
    result=37 * result + this.category.hashCode();
    result=37 * result + this.label.hashCode();
    result=37 * result + this.labelAnchor.hashCode();
    return result;
  }
}
