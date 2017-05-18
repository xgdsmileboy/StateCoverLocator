package org.jfree.data.time;
/** 
 * An exception that indicates an invalid format in a string representing a time period.
 */
public class TimePeriodFormatException extends IllegalArgumentException {
  /** 
 * Creates a new exception.
 * @param message  a message describing the exception.
 */
  public TimePeriodFormatException(  String message){
    super(message);
  }
}
