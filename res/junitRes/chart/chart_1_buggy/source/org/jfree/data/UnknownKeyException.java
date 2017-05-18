package org.jfree.data;
/** 
 * An exception that indicates an unknown key value.
 */
public class UnknownKeyException extends IllegalArgumentException {
  /** 
 * Creates a new exception.
 * @param message  a message describing the exception.
 */
  public UnknownKeyException(  String message){
    super(message);
  }
}
