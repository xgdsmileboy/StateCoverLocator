package org.jfree.chart.panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
/** 
 * A handler for mouse events in a      {@link ChartPanel}.  A handler can be assigned a modifier and installed on the panel to be invoked by the user.
 * @since 1.2.0
 */
public class AbstractMouseHandler implements MouseListener, MouseMotionListener {
  /** 
 * The modifier used to invoke this handler. 
 */
  private int modifier;
  /** 
 * Default constructor.
 */
  public AbstractMouseHandler(){
    this.modifier=0;
  }
  /** 
 * Returns the modifier for this handler.
 * @return The modifier.
 */
  public int getModifier(){
    return this.modifier;
  }
  /** 
 * Sets the modifier for this handler.
 * @param modifier  the modifier.
 */
  public void setModifier(  int modifier){
    this.modifier=modifier;
  }
  /** 
 * Handle a mouse pressed event.  This implementation does nothing - subclasses should override if necessary.
 * @param e  the mouse event.
 */
  public void mousePressed(  MouseEvent e){
  }
  /** 
 * Handle a mouse released event.  This implementation does nothing - subclasses should override if necessary.
 * @param e  the mouse event.
 */
  public void mouseReleased(  MouseEvent e){
  }
  /** 
 * Handle a mouse clicked event.  This implementation does nothing - subclasses should override if necessary.
 * @param e  the mouse event.
 */
  public void mouseClicked(  MouseEvent e){
  }
  /** 
 * Handle a mouse entered event.  This implementation does nothing - subclasses should override if necessary.
 * @param e  the mouse event.
 */
  public void mouseEntered(  MouseEvent e){
  }
  /** 
 * Handle a mouse moved event.  This implementation does nothing - subclasses should override if necessary.
 * @param e  the mouse event.
 */
  public void mouseMoved(  MouseEvent e){
  }
  /** 
 * Handle a mouse exited event.  This implementation does nothing - subclasses should override if necessary.
 * @param e  the mouse event.
 */
  public void mouseExited(  MouseEvent e){
  }
  /** 
 * Handle a mouse dragged event.  This implementation does nothing - subclasses should override if necessary.
 * @param e  the mouse event.
 */
  public void mouseDragged(  MouseEvent e){
  }
}
