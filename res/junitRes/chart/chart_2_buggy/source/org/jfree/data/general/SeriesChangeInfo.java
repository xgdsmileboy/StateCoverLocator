package org.jfree.data.general;
/** 
 * Summarises a change to a series.
 * @since 1.2.0
 */
public class SeriesChangeInfo {
  /** 
 * The type of change. 
 */
  private SeriesChangeType changeType;
  /** 
 * The index of the first data item affected by the change. 
 */
  private int index1;
  /** 
 * The index of the latest data item affected by the change. 
 */
  private int index2;
  /** 
 * Creates a new instance.
 * @param t  the type of change (<code>null</code> not permitted).
 * @param index1  the index of the first data item affected by the change.
 * @param index2  the index of the last data item affected by the change.
 */
  public SeriesChangeInfo(  SeriesChangeType t,  int index1,  int index2){
    this.changeType=t;
    this.index1=index1;
    this.index2=index2;
  }
  /** 
 * Returns the series change type.
 * @return The series change type.
 */
  public SeriesChangeType getChangeType(){
    return this.changeType;
  }
  /** 
 * Returns the index of the first item affected by the change.
 * @return The index.
 */
  public int getIndex1(){
    return this.index1;
  }
  /** 
 * Returns the index of the last item affects by the change.
 * @return The index.
 */
  public int getIndex2(){
    return this.index2;
  }
}
