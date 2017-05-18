package org.jfree.chart.text;
/** 
 * An object that can measure text.
 */
public interface TextMeasurer {
  /** 
 * Calculates the width of a <code>String</code> in the current <code>Graphics</code> context.
 * @param text  the text.
 * @param start  the start position of the substring to be measured.
 * @param end  the position of the last character to be measured.
 * @return The width of the string in Java2D units.
 */
  public float getStringWidth(  String text,  int start,  int end);
}
