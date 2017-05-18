package org.jfree.chart.entity;
import java.awt.Shape;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.util.HashUtilities;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.SerialUtilities;
/** 
 * A class that captures information about an Axis of a chart.
 * @since 1.0.13
 */
public class AxisEntity extends ChartEntity {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-4445994133561919083L;
  /** 
 * The axis for the entity. 
 */
  private Axis axis;
  /** 
 * Creates a new axis entity.
 * @param area  the area (<code>null</code> not permitted).
 * @param axis  the axis (<code>null</code> not permitted).
 */
  public AxisEntity(  Shape area,  Axis axis){
    this(area,axis,null);
  }
  /** 
 * Creates a new axis entity.
 * @param area  the area (<code>null</code> not permitted).
 * @param axis  the axis (<code>null</code> not permitted).
 * @param toolTipText  the tool tip text (<code>null</code> permitted).
 */
  public AxisEntity(  Shape area,  Axis axis,  String toolTipText){
    this(area,axis,toolTipText,null);
  }
  /** 
 * Creates a new axis entity.
 * @param area  the area (<code>null</code> not permitted).
 * @param axis  the axis (<code>null</code> not permitted).
 * @param toolTipText  the tool tip text (<code>null</code> permitted).
 * @param urlText  the URL text for HTML image maps (<code>null</code>permitted).
 */
  public AxisEntity(  Shape area,  Axis axis,  String toolTipText,  String urlText){
    super(area,toolTipText,urlText);
    if (axis == null) {
      throw new IllegalArgumentException("Null 'axis' argument.");
    }
    this.axis=axis;
  }
  /** 
 * Returns the axis that occupies the entity area.
 * @return The axis (never <code>null</code>).
 */
  public Axis getAxis(){
    return this.axis;
  }
  /** 
 * Returns a string representation of the chart entity, useful for debugging.
 * @return A string.
 */
  public String toString(){
    StringBuffer buf=new StringBuffer("AxisEntity: ");
    buf.append("tooltip = ");
    buf.append(getToolTipText());
    return buf.toString();
  }
  /** 
 * Tests the entity for equality with an arbitrary object.
 * @param obj  the object to test against (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof AxisEntity)) {
      return false;
    }
    AxisEntity that=(AxisEntity)obj;
    if (!getArea().equals(that.getArea())) {
      return false;
    }
    if (!ObjectUtilities.equal(getToolTipText(),that.getToolTipText())) {
      return false;
    }
    if (!ObjectUtilities.equal(getURLText(),that.getURLText())) {
      return false;
    }
    if (!(this.axis.equals(that.axis))) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=39;
    result=HashUtilities.hashCode(result,getToolTipText());
    result=HashUtilities.hashCode(result,getURLText());
    return result;
  }
  /** 
 * Returns a clone of the entity.
 * @return A clone.
 * @throws CloneNotSupportedException if there is a problem cloning theentity.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  /** 
 * Provides serialization support.
 * @param stream  the output stream.
 * @throws IOException  if there is an I/O error.
 */
  private void writeObject(  ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    SerialUtilities.writeShape(getArea(),stream);
  }
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    setArea(SerialUtilities.readShape(stream));
  }
}
