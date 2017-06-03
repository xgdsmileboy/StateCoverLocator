package org.jfree.chart.block;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.PaintUtilities;
import org.jfree.chart.util.RectangleInsets;
import org.jfree.chart.util.SerialUtilities;
/** 
 * A line border for any                                                                                                                                                                     {@link AbstractBlock}.
 * @since 1.0.5
 */
public class LineBorder implements BlockFrame, Serializable {
  /** 
 * For serialization. 
 */
  static final long serialVersionUID=4630356736707233924L;
  /** 
 * The line color. 
 */
  private transient Paint paint;
  /** 
 * The line stroke. 
 */
  private transient Stroke stroke;
  /** 
 * The insets. 
 */
  private RectangleInsets insets;
  /** 
 * Creates a default border.
 */
  public LineBorder(){
    this(Color.black,new BasicStroke(1.0f),new RectangleInsets(1.0,1.0,1.0,1.0));
  }
  /** 
 * Creates a new border with the specified color.
 * @param paint  the color (<code>null</code> not permitted).
 * @param stroke  the border stroke (<code>null</code> not permitted).
 * @param insets  the insets (<code>null</code> not permitted).
 */
  public LineBorder(  Paint paint,  Stroke stroke,  RectangleInsets insets){
    if (paint == null) {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
    if (stroke == null) {
      throw new IllegalArgumentException("Null 'stroke' argument.");
    }
    if (insets == null) {
      throw new IllegalArgumentException("Null 'insets' argument.");
    }
    this.paint=paint;
    this.stroke=stroke;
    this.insets=insets;
  }
  /** 
 * Returns the paint.
 * @return The paint (never <code>null</code>).
 */
  public Paint getPaint(){
    return this.paint;
  }
  /** 
 * Returns the insets.
 * @return The insets (never <code>null</code>).
 */
  public RectangleInsets getInsets(){
    return this.insets;
  }
  /** 
 * Returns the stroke.
 * @return The stroke (never <code>null</code>).
 */
  public Stroke getStroke(){
    return this.stroke;
  }
  /** 
 * Draws the border by filling in the reserved space (in black).
 * @param g2  the graphics device.
 * @param area  the area.
 */
  public void draw(  Graphics2D g2,  Rectangle2D area){
    double w=area.getWidth();
    double h=area.getHeight();
    if (w <= 0.0 || h <= 0.0) {
      return;
    }
    double t=this.insets.calculateTopInset(h);
    double b=this.insets.calculateBottomInset(h);
    double l=this.insets.calculateLeftInset(w);
    double r=this.insets.calculateRightInset(w);
    double x=area.getX();
    double y=area.getY();
    double x0=x + l / 2.0;
    double x1=x + w - r / 2.0;
    double y0=y + h - b / 2.0;
    double y1=y + t / 2.0;
    g2.setPaint(getPaint());
    g2.setStroke(getStroke());
    Line2D line=new Line2D.Double();
    if (t > 0.0) {
      line.setLine(x0,y1,x1,y1);
      g2.draw(line);
    }
    if (b > 0.0) {
      line.setLine(x0,y0,x1,y0);
      g2.draw(line);
    }
    if (l > 0.0) {
      line.setLine(x0,y0,x0,y1);
      g2.draw(line);
    }
    if (r > 0.0) {
      line.setLine(x1,y0,x1,y1);
      g2.draw(line);
    }
  }
  /** 
 * Tests this border for equality with an arbitrary instance.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof LineBorder)) {
      return false;
    }
    LineBorder that=(LineBorder)obj;
    if (!PaintUtilities.equal(this.paint,that.paint)) {
      return false;
    }
    if (!ObjectUtilities.equal(this.stroke,that.stroke)) {
      return false;
    }
    if (!this.insets.equals(that.insets)) {
      return false;
    }
    return true;
  }
  /** 
 * Provides serialization support.
 * @param stream  the output stream.
 * @throws IOException  if there is an I/O error.
 */
  private void writeObject(  ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    SerialUtilities.writePaint(this.paint,stream);
    SerialUtilities.writeStroke(this.stroke,stream);
  }
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.paint=SerialUtilities.readPaint(stream);
    this.stroke=SerialUtilities.readStroke(stream);
  }
}
