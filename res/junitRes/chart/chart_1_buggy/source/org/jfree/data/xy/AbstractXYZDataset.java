package org.jfree.data.xy;
/** 
 * An base class that you can use to create new implementations of the                              {@link XYZDataset} interface.
 */
public abstract class AbstractXYZDataset extends AbstractXYDataset implements XYZDataset {
  /** 
 * Returns the z-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The z-value.
 */
  public double getZValue(  int series,  int item){
    double result=Double.NaN;
    Number z=getZ(series,item);
    if (z != null) {
      result=z.doubleValue();
    }
    return result;
  }
}
