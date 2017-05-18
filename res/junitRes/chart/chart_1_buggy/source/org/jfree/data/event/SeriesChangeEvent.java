package org.jfree.data.event;
import org.jfree.data.general.*;
import java.io.Serializable;
import java.util.EventObject;
/** 
 * An event with details of a change to a series.
 */
public class SeriesChangeEvent extends EventObject implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=1593866085210089052L;
  /** 
 * Summary info about the change.
 * @since 1.2.0
 */
  private SeriesChangeInfo summary;
  /** 
 * Constructs a new event.
 * @param source  the source of the change event.
 */
  public SeriesChangeEvent(  Object source){
    this(source,null);
  }
  /** 
 * Constructs a new change event.
 * @param source  the event source.
 * @param summary  a summary of the change (<code>null</code> permitted).
 * @since 1.2.0
 */
  public SeriesChangeEvent(  Object source,  SeriesChangeInfo summary){
    super(source);
    this.summary=summary;
  }
  /** 
 * Returns a summary of the change for this event.
 * @return The change summary (possibly <code>null</code>).
 * @since 1.2.0
 */
  public SeriesChangeInfo getSummary(){
    return this.summary;
  }
  /** 
 * Sets the change info for this event.
 * @param summary  the info (<code>null</code> permitted).
 * @since 1.2.0
 */
  public void setSummary(  SeriesChangeInfo summary){
    this.summary=summary;
  }
}
