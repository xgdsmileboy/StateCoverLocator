package org.jfree.chart.util;
import java.awt.geom.Rectangle2D;
/** 
 * A utility class for aligning rectangles.
 */
public final class Align {
  /** 
 * Center alignment. 
 */
  public static final int CENTER=0x00;
  /** 
 * Top alignment. 
 */
  public static final int TOP=0x01;
  /** 
 * Bottom alignment. 
 */
  public static final int BOTTOM=0x02;
  /** 
 * Left alignment. 
 */
  public static final int LEFT=0x04;
  /** 
 * Right alignment. 
 */
  public static final int RIGHT=0x08;
  /** 
 * Top/Left alignment. 
 */
  public static final int TOP_LEFT=TOP | LEFT;
  /** 
 * Top/Right alignment. 
 */
  public static final int TOP_RIGHT=TOP | RIGHT;
  /** 
 * Bottom/Left alignment. 
 */
  public static final int BOTTOM_LEFT=BOTTOM | LEFT;
  /** 
 * Bottom/Right alignment. 
 */
  public static final int BOTTOM_RIGHT=BOTTOM | RIGHT;
  /** 
 * Horizontal fit. 
 */
  public static final int FIT_HORIZONTAL=LEFT | RIGHT;
  /** 
 * Vertical fit. 
 */
  public static final int FIT_VERTICAL=TOP | BOTTOM;
  /** 
 * Complete fit. 
 */
  public static final int FIT=FIT_HORIZONTAL | FIT_VERTICAL;
  /** 
 * North alignment (same as TOP). 
 */
  public static final int NORTH=TOP;
  /** 
 * South alignment (same as BOTTOM). 
 */
  public static final int SOUTH=BOTTOM;
  /** 
 * West alignment (same as LEFT). 
 */
  public static final int WEST=LEFT;
  /** 
 * East alignment (same as RIGHT). 
 */
  public static final int EAST=RIGHT;
  /** 
 * North/West alignment (same as TOP_LEFT). 
 */
  public static final int NORTH_WEST=NORTH | WEST;
  /** 
 * North/East alignment (same as TOP_RIGHT). 
 */
  public static final int NORTH_EAST=NORTH | EAST;
  /** 
 * South/West alignment (same as BOTTOM_LEFT). 
 */
  public static final int SOUTH_WEST=SOUTH | WEST;
  /** 
 * South/East alignment (same as BOTTOM_RIGHT). 
 */
  public static final int SOUTH_EAST=SOUTH | EAST;
  /** 
 * Private constructor.
 */
  private Align(){
    super();
  }
  /** 
 * Aligns one rectangle (<code>rect</code>) relative to another rectangle (<code>frame</code>).
 * @param rect  the rectangle to be aligned (<code>null</code> notpermitted).
 * @param frame  the reference frame (<code>null</code> not permitted).
 * @param align  the alignment code.
 */
  public static void align(  Rectangle2D rect,  Rectangle2D frame,  int align){
    double x=frame.getCenterX() - rect.getWidth() / 2.0;
    double y=frame.getCenterY() - rect.getHeight() / 2.0;
    double w=rect.getWidth();
    double h=rect.getHeight();
    if ((align & FIT_VERTICAL) == FIT_VERTICAL) {
      h=frame.getHeight();
    }
    if ((align & FIT_HORIZONTAL) == FIT_HORIZONTAL) {
      w=frame.getWidth();
    }
    if ((align & TOP) == TOP) {
      y=frame.getMinY();
    }
    if ((align & BOTTOM) == BOTTOM) {
      y=frame.getMaxY() - h;
    }
    if ((align & LEFT) == LEFT) {
      x=frame.getX();
    }
    if ((align & RIGHT) == RIGHT) {
      x=frame.getMaxX() - w;
    }
    rect.setRect(x,y,w,h);
  }
}
