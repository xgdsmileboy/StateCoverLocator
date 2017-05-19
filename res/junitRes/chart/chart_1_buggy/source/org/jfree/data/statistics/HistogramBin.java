package org.jfree.data.statistics;
import java.io.Serializable;
/** 
 * A bin for the                               {@link HistogramDataset} class.
 */
public class HistogramBin implements Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=7614685080015589931L;
  /** 
 * The number of items in the bin. 
 */
  private int count;
  /** 
 * The start boundary. 
 */
  private double startBoundary;
  /** 
 * The end boundary. 
 */
  private double endBoundary;
  /** 
 * Creates a new bin.
 * @param startBoundary  the start boundary.
 * @param endBoundary  the end boundary.
 */
  public HistogramBin(  double startBoundary,  double endBoundary){
    if (startBoundary > endBoundary) {
      throw new IllegalArgumentException("HistogramBin():  startBoundary > endBoundary.");
    }
    this.count=0;
    this.startBoundary=startBoundary;
    this.endBoundary=endBoundary;
  }
  /** 
 * Returns the number of items in the bin.
 * @return The item count.
 */
  public int getCount(){
    return this.count;
  }
  /** 
 * Increments the item count.
 */
  public void incrementCount(){
    this.count++;
  }
  /** 
 * Returns the start boundary.
 * @return The start boundary.
 */
  public double getStartBoundary(){
    return this.startBoundary;
  }
  /** 
 * Returns the end boundary.
 * @return The end boundary.
 */
  public double getEndBoundary(){
    return this.endBoundary;
  }
  /** 
 * Returns the bin width.
 * @return The bin width.
 */
  public double getBinWidth(){
    return this.endBoundary - this.startBoundary;
  }
  /** 
 * Tests this object for equality with an arbitrary object.
 * @param obj  the object to test against.
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj instanceof HistogramBin) {
      HistogramBin bin=(HistogramBin)obj;
      boolean b0=bin.startBoundary == this.startBoundary;
      boolean b1=bin.endBoundary == this.endBoundary;
      boolean b2=bin.count == this.count;
      return b0 && b1 && b2;
    }
    return false;
  }
  /** 
 * Returns a clone of the bin.
 * @return A clone.
 * @throws CloneNotSupportedException not thrown by this class.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
