package org.jfree.chart.util;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
/** 
 * A custom number formatter that formats numbers as hexadecimal strings. There are some limitations, so be careful using this class.
 * @since 1.0.6
 */
public class HexNumberFormat extends NumberFormat {
  /** 
 * Number of hexadecimal digits for a byte. 
 */
  public static final int BYTE=2;
  /** 
 * Number of hexadecimal digits for a word. 
 */
  public static final int WORD=4;
  /** 
 * Number of hexadecimal digits for a double word. 
 */
  public static final int DWORD=8;
  /** 
 * Number of hexadecimal digits for a quad word. 
 */
  public static final int QWORD=16;
  /** 
 * The number of digits (shorter strings will be left padded). 
 */
  private int m_numDigits=DWORD;
  /** 
 * Creates a new instance with 8 digits.
 */
  public HexNumberFormat(){
    this(DWORD);
  }
  /** 
 * Creates a new instance with the specified number of digits.
 * @param digits  the digits.
 */
  public HexNumberFormat(  int digits){
    super();
    this.m_numDigits=digits;
  }
  /** 
 * Returns the number of digits.
 * @return The number of digits.
 */
  public final int getNumberOfDigits(){
    return this.m_numDigits;
  }
  /** 
 * Sets the number of digits.
 * @param digits  the number of digits.
 */
  public void setNumberOfDigits(  int digits){
    this.m_numDigits=digits;
  }
  /** 
 * Formats the specified number as a hexadecimal string.  The decimal fraction is ignored.
 * @param number  the number to format.
 * @param toAppendTo  the buffer to append to (ignored here).
 * @param pos  the field position (ignored here).
 * @return The string buffer.
 */
  public StringBuffer format(  double number,  StringBuffer toAppendTo,  FieldPosition pos){
    return format((long)number,toAppendTo,pos);
  }
  /** 
 * Formats the specified number as a hexadecimal string.  The decimal fraction is ignored.
 * @param number  the number to format.
 * @param toAppendTo  the buffer to append to (ignored here).
 * @param pos  the field position (ignored here).
 * @return The string buffer.
 */
  public StringBuffer format(  long number,  StringBuffer toAppendTo,  FieldPosition pos){
    String l_hex=Long.toHexString(number).toUpperCase();
    int l_pad=this.m_numDigits - l_hex.length();
    l_pad=(0 < l_pad) ? l_pad : 0;
    StringBuffer l_extended=new StringBuffer("0x");
    for (int i=0; i < l_pad; i++) {
      l_extended.append(0);
    }
    l_extended.append(l_hex);
    return l_extended;
  }
  /** 
 * Parsing is not implemented, so this method always returns <code>null</code>.
 * @param source  ignored.
 * @param parsePosition  ignored.
 * @return Always <code>null</code>.
 */
  public Number parse(  String source,  ParsePosition parsePosition){
    return null;
  }
}
