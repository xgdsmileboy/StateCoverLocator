package org.jfree.chart.util;
import java.awt.GradientPaint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
/** 
 * Transforms a <code>GradientPaint</code> to range over the width of a target shape.  Instances of this class are immutable.
 */
public class StandardGradientPaintTransformer implements GradientPaintTransformer, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-8155025776964678320L;
  /** 
 * The transform type. 
 */
  private GradientPaintTransformType type;
  /** 
 * Creates a new transformer with the type                                                                                              {@link GradientPaintTransformType#VERTICAL}.
 */
  public StandardGradientPaintTransformer(){
    this(GradientPaintTransformType.VERTICAL);
  }
  /** 
 * Creates a new transformer with the specified type.
 * @param type  the transform type (<code>null</code> not permitted).
 */
  public StandardGradientPaintTransformer(  GradientPaintTransformType type){
    if (type == null) {
      throw new IllegalArgumentException("Null 'type' argument.");
    }
    this.type=type;
  }
  /** 
 * Returns the type of transform.
 * @return The type of transform (never <code>null</code>).
 */
  public GradientPaintTransformType getType(){
    return this.type;
  }
  /** 
 * Transforms a <code>GradientPaint</code> instance to fit the specified <code>target</code> shape.
 * @param paint  the original paint (<code>null</code> not permitted).
 * @param target  the target shape (<code>null</code> not permitted).
 * @return The transformed paint.
 */
  public GradientPaint transform(  GradientPaint paint,  Shape target){
    GradientPaint result=paint;
    Rectangle2D bounds=target.getBounds2D();
    if (this.type.equals(GradientPaintTransformType.VERTICAL)) {
      result=new GradientPaint((float)bounds.getCenterX(),(float)bounds.getMinY(),paint.getColor1(),(float)bounds.getCenterX(),(float)bounds.getMaxY(),paint.getColor2());
    }
 else {
      if (this.type.equals(GradientPaintTransformType.HORIZONTAL)) {
        result=new GradientPaint((float)bounds.getMinX(),(float)bounds.getCenterY(),paint.getColor1(),(float)bounds.getMaxX(),(float)bounds.getCenterY(),paint.getColor2());
      }
 else {
        if (this.type.equals(GradientPaintTransformType.CENTER_HORIZONTAL)) {
          result=new GradientPaint((float)bounds.getCenterX(),(float)bounds.getCenterY(),paint.getColor2(),(float)bounds.getMaxX(),(float)bounds.getCenterY(),paint.getColor1(),true);
        }
 else {
          if (this.type.equals(GradientPaintTransformType.CENTER_VERTICAL)) {
            result=new GradientPaint((float)bounds.getCenterX(),(float)bounds.getMinY(),paint.getColor1(),(float)bounds.getCenterX(),(float)bounds.getCenterY(),paint.getColor2(),true);
          }
        }
      }
    }
    return result;
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
    if (!(obj instanceof StandardGradientPaintTransformer)) {
      return false;
    }
    StandardGradientPaintTransformer that=(StandardGradientPaintTransformer)obj;
    if (this.type != that.type) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a clone of the transformer.  Note that instances of this class are immutable, so cloning an instance isn't really necessary.
 * @return A clone.
 * @throws CloneNotSupportedException not thrown by this class, butsubclasses (if any) might.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  /** 
 * Returns a hash code for this object.
 * @return A hash code.
 */
  public int hashCode(){
    return (this.type != null ? this.type.hashCode() : 0);
  }
}
