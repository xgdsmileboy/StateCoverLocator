package org.jfree.chart.util;
/** 
 * String utilities.
 */
public class StringUtilities {
  /** 
 * Private constructor prevents object creation.
 */
  private StringUtilities(){
  }
  /** 
 * Helper functions to query a strings start portion. The comparison is case insensitive.
 * @param base  the base string.
 * @param start  the starting text.
 * @return true, if the string starts with the given starting text.
 */
  public static boolean startsWithIgnoreCase(  String base,  String start){
    if (base.length() < start.length()) {
      return false;
    }
    return base.regionMatches(true,0,start,0,start.length());
  }
  /** 
 * Helper functions to query a strings end portion. The comparison is case insensitive.
 * @param base  the base string.
 * @param end  the ending text.
 * @return true, if the string ends with the given ending text.
 */
  public static boolean endsWithIgnoreCase(  String base,  String end){
    if (base.length() < end.length()) {
      return false;
    }
    return base.regionMatches(true,base.length() - end.length(),end,0,end.length());
  }
  /** 
 * Queries the system properties for the line separator. If access to the System properties is forbidden, the UNIX default is returned.
 * @return the line separator.
 */
  public static String getLineSeparator(){
    try {
      return System.getProperty("line.separator","\n");
    }
 catch (    Exception e) {
      return "\n";
    }
  }
}
