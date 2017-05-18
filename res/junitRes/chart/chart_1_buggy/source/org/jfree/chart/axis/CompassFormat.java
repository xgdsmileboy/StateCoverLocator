package org.jfree.chart.axis;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
/** 
 * A formatter that displays numbers as directions.
 */
public class CompassFormat extends NumberFormat {
  /** 
 * North. 
 */
  private static final String N="N";
  /** 
 * East. 
 */
  private static final String E="E";
  /** 
 * South. 
 */
  private static final String S="S";
  /** 
 * West. 
 */
  private static final String W="W";
  /** 
 * The directions. 
 */
  public static final String[] DIRECTIONS={N,N + N + E,N + E,E + N + E,E,E + S + E,S + E,S + S + E,S,S + S + W,S + W,W + S + W,W,W + N + W,N + W,N + N + W,N};
  /** 
 * Creates a new formatter.
 */
  public CompassFormat(){
    super();
  }
  /** 
 * Returns a string representing the direction.
 * @param direction  the direction.
 * @return A string.
 */
  public String getDirectionCode(  double direction){
    direction=direction % 360;
    if (direction < 0.0) {
      direction=direction + 360.0;
    }
    int index=((int)Math.floor(direction / 11.25) + 1) / 2;
    return DIRECTIONS[index];
  }
  /** 
 * Formats a number into the specified string buffer.
 * @param number  the number to format.
 * @param toAppendTo  the string buffer.
 * @param pos  the field position (ignored here).
 * @return The string buffer.
 */
  public StringBuffer format(  double number,  StringBuffer toAppendTo,  FieldPosition pos){
    return toAppendTo.append(getDirectionCode(number));
  }
  /** 
 * Formats a number into the specified string buffer.
 * @param number  the number to format.
 * @param toAppendTo  the string buffer.
 * @param pos  the field position (ignored here).
 * @return The string buffer.
 */
  public StringBuffer format(  long number,  StringBuffer toAppendTo,  FieldPosition pos){
    return toAppendTo.append(getDirectionCode(number));
  }
  /** 
 * This method returns <code>null</code> for all inputs.  This class cannot be used for parsing.
 * @param source  the source string.
 * @param parsePosition  the parse position.
 * @return <code>null</code>.
 */
  public Number parse(  String source,  ParsePosition parsePosition){
    return null;
  }
}
