package org.jfree.chart.entity;
import java.awt.Shape;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.util.HashUtilities;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.SerialUtilities;
/** 
 * A class that captures information about a plot.
 * @since 1.0.13
 */
public class PlotEntity extends ChartEntity {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-4445994133561919083L;
  /** 
 * The plot. 
 */
  private Plot plot;
  /** 
 * Creates a new plot entity.
 * @param area  the area (<code>null</code> not permitted).
 * @param plot  the plot (<code>null</code> not permitted).
 */
  public PlotEntity(  Shape area,  Plot plot){
    this(area,plot,null);
  }
  /** 
 * Creates a new plot entity.
 * @param area  the area (<code>null</code> not permitted).
 * @param plot  the plot (<code>null</code> not permitted).
 * @param toolTipText  the tool tip text (<code>null</code> permitted).
 */
  public PlotEntity(  Shape area,  Plot plot,  String toolTipText){
    this(area,plot,toolTipText,null);
  }
  /** 
 * Creates a new plot entity.
 * @param area  the area (<code>null</code> not permitted).
 * @param plot  the plot (<code>null</code> not permitted).
 * @param toolTipText  the tool tip text (<code>null</code> permitted).
 * @param urlText  the URL text for HTML image maps (<code>null</code>permitted).
 */
  public PlotEntity(  Shape area,  Plot plot,  String toolTipText,  String urlText){
    super(area,toolTipText,urlText);
    if (plot == null) {
      throw new IllegalArgumentException("Null 'plot' argument.");
    }
    this.plot=plot;
  }
  /** 
 * Returns the plot that occupies the entity area.
 * @return The plot (never <code>null</code>).
 */
  public Plot getPlot(){
    return this.plot;
  }
  /** 
 * Returns a string representation of the plot entity, useful for debugging.
 * @return A string.
 */
  public String toString(){
    StringBuffer buf=new StringBuffer("PlotEntity: ");
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
    if (!(obj instanceof PlotEntity)) {
      return false;
    }
    PlotEntity that=(PlotEntity)obj;
    if (!getArea().equals(that.getArea())) {
      return false;
    }
    if (!ObjectUtilities.equal(getToolTipText(),that.getToolTipText())) {
      return false;
    }
    if (!ObjectUtilities.equal(getURLText(),that.getURLText())) {
      return false;
    }
    if (!(this.plot.equals(that.plot))) {
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
