package org.jfree.data.xy;
/** 
 * An extension of the                                                                                               {@link XYDataset} interface that allows a vector to bedefined at a specific (x, y) location.
 * @since 1.0.6
 */
public interface VectorXYDataset extends XYDataset {
  /** 
 * Returns the x-component of the vector for an item in a series.
 * @param series  the series index.
 * @param item  the item index.
 * @return The x-component of the vector.
 */
  public double getVectorXValue(  int series,  int item);
  /** 
 * Returns the y-component of the vector for an item in a series.
 * @param series  the series index.
 * @param item  the item index.
 * @return The y-component of the vector.
 */
  public double getVectorYValue(  int series,  int item);
  /** 
 * Returns the vector for an item in a series.  Depending on the particular dataset implementation, this may involve creating a new                                                                                               {@link Vector}instance --- if you are just interested in the x and y components, use the                                                                                               {@link #getVectorXValue(int,int)} and{@link #getVectorYValue(int,int)} methods instead.
 * @param series  the series index.
 * @param item  the item index.
 * @return The vector (possibly <code>null</code>).
 */
  public Vector getVector(  int series,  int item);
}
