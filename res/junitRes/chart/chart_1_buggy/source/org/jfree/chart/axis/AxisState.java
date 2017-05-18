package org.jfree.chart.axis;
import java.util.List;
import org.jfree.chart.util.RectangleEdge;
/** 
 * Instances of this class are used to carry state information for an axis during the drawing process.  By retaining this information in a separate object, it is possible for multiple threads to draw the same axis to different output targets (each drawing will maintain separate state information).
 */
public class AxisState {
  /** 
 * The cursor position. 
 */
  private double cursor;
  /** 
 * The axis ticks. 
 */
  private List ticks;
  /** 
 * The maximum width/height. 
 */
  private double max;
  /** 
 * Creates a new axis state.
 */
  public AxisState(){
    this(0.0);
  }
  /** 
 * Creates a new axis state.
 * @param cursor  the cursor.
 */
  public AxisState(  double cursor){
    this.cursor=cursor;
    this.ticks=new java.util.ArrayList();
  }
  /** 
 * Returns the cursor position.
 * @return The cursor position.
 */
  public double getCursor(){
    return this.cursor;
  }
  /** 
 * Sets the cursor position.
 * @param cursor  the cursor position.
 */
  public void setCursor(  double cursor){
    this.cursor=cursor;
  }
  /** 
 * Moves the cursor outwards by the specified number of units.
 * @param units  the units.
 * @param edge  the edge.
 */
  public void moveCursor(  double units,  RectangleEdge edge){
    if (edge == RectangleEdge.TOP) {
      cursorUp(units);
    }
 else {
      if (edge == RectangleEdge.BOTTOM) {
        cursorDown(units);
      }
 else {
        if (edge == RectangleEdge.LEFT) {
          cursorLeft(units);
        }
 else {
          if (edge == RectangleEdge.RIGHT) {
            cursorRight(units);
          }
        }
      }
    }
  }
  /** 
 * Moves the cursor up by the specified number of Java 2D units.
 * @param units  the units.
 */
  public void cursorUp(  double units){
    this.cursor=this.cursor - units;
  }
  /** 
 * Moves the cursor down by the specified number of Java 2D units.
 * @param units  the units.
 */
  public void cursorDown(  double units){
    this.cursor=this.cursor + units;
  }
  /** 
 * Moves the cursor left by the specified number of Java 2D units.
 * @param units  the units.
 */
  public void cursorLeft(  double units){
    this.cursor=this.cursor - units;
  }
  /** 
 * Moves the cursor right by the specified number of Java 2D units.
 * @param units  the units.
 */
  public void cursorRight(  double units){
    this.cursor=this.cursor + units;
  }
  /** 
 * Returns the list of ticks.
 * @return The list of ticks.
 */
  public List getTicks(){
    return this.ticks;
  }
  /** 
 * Sets the list of ticks.
 * @param ticks  the ticks.
 */
  public void setTicks(  List ticks){
    this.ticks=ticks;
  }
  /** 
 * Returns the maximum width/height.
 * @return The maximum width/height.
 */
  public double getMax(){
    return this.max;
  }
  /** 
 * Sets the maximum width/height.
 * @param max the maximum width/height.
 */
  public void setMax(  double max){
    this.max=max;
  }
}
