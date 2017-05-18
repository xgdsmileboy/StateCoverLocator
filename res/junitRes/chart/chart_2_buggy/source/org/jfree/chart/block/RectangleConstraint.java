package org.jfree.chart.block;
import org.jfree.chart.util.Size2D;
import org.jfree.data.Range;
/** 
 * A description of a constraint for resizing a rectangle.  Constraints are immutable.
 */
public class RectangleConstraint {
  /** 
 * An instance representing no constraint.
 */
  public static final RectangleConstraint NONE=new RectangleConstraint(0.0,null,LengthConstraintType.NONE,0.0,null,LengthConstraintType.NONE);
  /** 
 * The width. 
 */
  private double width;
  /** 
 * The width range. 
 */
  private Range widthRange;
  /** 
 * The width constraint type. 
 */
  private LengthConstraintType widthConstraintType;
  /** 
 * The fixed or maximum height. 
 */
  private double height;
  private Range heightRange;
  /** 
 * The constraint type. 
 */
  private LengthConstraintType heightConstraintType;
  /** 
 * Creates a new "fixed width and height" instance.
 * @param w  the fixed width.
 * @param h  the fixed height.
 */
  public RectangleConstraint(  double w,  double h){
    this(w,null,LengthConstraintType.FIXED,h,null,LengthConstraintType.FIXED);
  }
  /** 
 * Creates a new "range width and height" instance.
 * @param w  the width range.
 * @param h  the height range.
 */
  public RectangleConstraint(  Range w,  Range h){
    this(0.0,w,LengthConstraintType.RANGE,0.0,h,LengthConstraintType.RANGE);
  }
  /** 
 * Creates a new constraint with a range for the width and a fixed height.
 * @param w  the width range.
 * @param h  the fixed height.
 */
  public RectangleConstraint(  Range w,  double h){
    this(0.0,w,LengthConstraintType.RANGE,h,null,LengthConstraintType.FIXED);
  }
  /** 
 * Creates a new constraint with a fixed width and a range for the height.
 * @param w  the fixed width.
 * @param h  the height range.
 */
  public RectangleConstraint(  double w,  Range h){
    this(w,null,LengthConstraintType.FIXED,0.0,h,LengthConstraintType.RANGE);
  }
  /** 
 * Creates a new constraint.
 * @param w  the fixed or maximum width.
 * @param widthRange  the width range.
 * @param widthConstraintType  the width type.
 * @param h  the fixed or maximum height.
 * @param heightRange  the height range.
 * @param heightConstraintType  the height type.
 */
  public RectangleConstraint(  double w,  Range widthRange,  LengthConstraintType widthConstraintType,  double h,  Range heightRange,  LengthConstraintType heightConstraintType){
    if (widthConstraintType == null) {
      throw new IllegalArgumentException("Null 'widthType' argument.");
    }
    if (heightConstraintType == null) {
      throw new IllegalArgumentException("Null 'heightType' argument.");
    }
    this.width=w;
    this.widthRange=widthRange;
    this.widthConstraintType=widthConstraintType;
    this.height=h;
    this.heightRange=heightRange;
    this.heightConstraintType=heightConstraintType;
  }
  /** 
 * Returns the fixed width.
 * @return The width.
 */
  public double getWidth(){
    return this.width;
  }
  /** 
 * Returns the width range.
 * @return The range (possibly <code>null</code>).
 */
  public Range getWidthRange(){
    return this.widthRange;
  }
  /** 
 * Returns the constraint type.
 * @return The constraint type (never <code>null</code>).
 */
  public LengthConstraintType getWidthConstraintType(){
    return this.widthConstraintType;
  }
  /** 
 * Returns the fixed height.
 * @return The height.
 */
  public double getHeight(){
    return this.height;
  }
  /** 
 * Returns the width range.
 * @return The range (possibly <code>null</code>).
 */
  public Range getHeightRange(){
    return this.heightRange;
  }
  /** 
 * Returns the constraint type.
 * @return The constraint type (never <code>null</code>).
 */
  public LengthConstraintType getHeightConstraintType(){
    return this.heightConstraintType;
  }
  /** 
 * Returns a constraint that matches this one on the height attributes, but has no width constraint.
 * @return A new constraint.
 */
  public RectangleConstraint toUnconstrainedWidth(){
    if (this.widthConstraintType == LengthConstraintType.NONE) {
      return this;
    }
 else {
      return new RectangleConstraint(this.width,this.widthRange,LengthConstraintType.NONE,this.height,this.heightRange,this.heightConstraintType);
    }
  }
  /** 
 * Returns a constraint that matches this one on the width attributes, but has no height constraint.
 * @return A new constraint.
 */
  public RectangleConstraint toUnconstrainedHeight(){
    if (this.heightConstraintType == LengthConstraintType.NONE) {
      return this;
    }
 else {
      return new RectangleConstraint(this.width,this.widthRange,this.widthConstraintType,0.0,this.heightRange,LengthConstraintType.NONE);
    }
  }
  /** 
 * Returns a constraint that matches this one on the height attributes, but has a fixed width constraint.
 * @param width  the fixed width.
 * @return A new constraint.
 */
  public RectangleConstraint toFixedWidth(  double width){
    return new RectangleConstraint(width,this.widthRange,LengthConstraintType.FIXED,this.height,this.heightRange,this.heightConstraintType);
  }
  /** 
 * Returns a constraint that matches this one on the width attributes, but has a fixed height constraint.
 * @param height  the fixed height.
 * @return A new constraint.
 */
  public RectangleConstraint toFixedHeight(  double height){
    return new RectangleConstraint(this.width,this.widthRange,this.widthConstraintType,height,this.heightRange,LengthConstraintType.FIXED);
  }
  /** 
 * Returns a constraint that matches this one on the height attributes, but has a range width constraint.
 * @param range  the width range (<code>null</code> not permitted).
 * @return A new constraint.
 */
  public RectangleConstraint toRangeWidth(  Range range){
    if (range == null) {
      throw new IllegalArgumentException("Null 'range' argument.");
    }
    return new RectangleConstraint(range.getUpperBound(),range,LengthConstraintType.RANGE,this.height,this.heightRange,this.heightConstraintType);
  }
  /** 
 * Returns a constraint that matches this one on the width attributes, but has a range height constraint.
 * @param range  the height range (<code>null</code> not permitted).
 * @return A new constraint.
 */
  public RectangleConstraint toRangeHeight(  Range range){
    if (range == null) {
      throw new IllegalArgumentException("Null 'range' argument.");
    }
    return new RectangleConstraint(this.width,this.widthRange,this.widthConstraintType,range.getUpperBound(),range,LengthConstraintType.RANGE);
  }
  /** 
 * Returns a string representation of this instance, mostly used for debugging purposes.
 * @return A string.
 */
  public String toString(){
    return "RectangleConstraint[" + this.widthConstraintType.toString() + ": width="+ this.width+ ", height="+ this.height+ "]";
  }
  /** 
 * Returns the new size that reflects the constraints defined by this instance.
 * @param base  the base size.
 * @return The constrained size.
 */
  public Size2D calculateConstrainedSize(  Size2D base){
    Size2D result=new Size2D();
    if (this.widthConstraintType == LengthConstraintType.NONE) {
      result.width=base.width;
      if (this.heightConstraintType == LengthConstraintType.NONE) {
        result.height=base.height;
      }
 else {
        if (this.heightConstraintType == LengthConstraintType.RANGE) {
          result.height=this.heightRange.constrain(base.height);
        }
 else {
          if (this.heightConstraintType == LengthConstraintType.FIXED) {
            result.height=this.height;
          }
        }
      }
    }
 else {
      if (this.widthConstraintType == LengthConstraintType.RANGE) {
        result.width=this.widthRange.constrain(base.width);
        if (this.heightConstraintType == LengthConstraintType.NONE) {
          result.height=base.height;
        }
 else {
          if (this.heightConstraintType == LengthConstraintType.RANGE) {
            result.height=this.heightRange.constrain(base.height);
          }
 else {
            if (this.heightConstraintType == LengthConstraintType.FIXED) {
              result.height=this.height;
            }
          }
        }
      }
 else {
        if (this.widthConstraintType == LengthConstraintType.FIXED) {
          result.width=this.width;
          if (this.heightConstraintType == LengthConstraintType.NONE) {
            result.height=base.height;
          }
 else {
            if (this.heightConstraintType == LengthConstraintType.RANGE) {
              result.height=this.heightRange.constrain(base.height);
            }
 else {
              if (this.heightConstraintType == LengthConstraintType.FIXED) {
                result.height=this.height;
              }
            }
          }
        }
      }
    }
    return result;
  }
}
