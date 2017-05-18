package org.jfree.data.general;
import java.io.Serializable;
/** 
 * A general purpose exception class for data series.
 */
public class SeriesException extends RuntimeException implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-3667048387550852940L;
  /** 
 * Constructs a new series exception.
 * @param message  a message describing the exception.
 */
  public SeriesException(  String message){
    super(message);
  }
}
