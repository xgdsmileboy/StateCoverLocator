package org.jfree.chart.text;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
/** 
 * A      {@link TextMeasurer} based on a {@link Graphics2D}.
 */
public class G2TextMeasurer implements TextMeasurer {
  /** 
 * The graphics device. 
 */
  private Graphics2D g2;
  /** 
 * Creates a new text measurer.
 * @param g2  the graphics device.
 */
  public G2TextMeasurer(  Graphics2D g2){
    this.g2=g2;
  }
  /** 
 * Returns the string width.
 * @param text  the text.
 * @param start  the index of the first character to measure.
 * @param end  the index of the last character to measure.
 * @return The string width.
 */
  public float getStringWidth(  String text,  int start,  int end){
    FontMetrics fm=this.g2.getFontMetrics();
    Rectangle2D bounds=TextUtilities.getTextBounds(text.substring(start,end),this.g2,fm);
    float result=(float)bounds.getWidth();
    return result;
  }
}
