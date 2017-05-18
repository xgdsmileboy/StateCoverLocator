package org.jfree.data.xy;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.AbstractSeriesDataset;
/** 
 * An base class that you can use to create new implementations of the                                                                                              {@link XYDataset} interface.
 */
public abstract class AbstractXYDataset extends AbstractSeriesDataset implements XYDataset {
  /** 
 * Returns the order of the domain (X) values.
 * @return The domain order.
 */
  public DomainOrder getDomainOrder(){
    return DomainOrder.NONE;
  }
  /** 
 * Returns the x-value (as a double primitive) for an item within a series.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The value.
 */
  public double getXValue(  int series,  int item){
    double result=Double.NaN;
    Number x=getX(series,item);
    if (x != null) {
      result=x.doubleValue();
    }
    return result;
  }
  /** 
 * Returns the y-value (as a double primitive) for an item within a series.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The value.
 */
  public double getYValue(  int series,  int item){
    double result=Double.NaN;
    Number y=getY(series,item);
    if (y != null) {
      result=y.doubleValue();
    }
    return result;
  }
  /** 
 * The dataset selection state (possibly <code>null</code>).
 * @since 1.2.0
 */
  private XYDatasetSelectionState selectionState;
  /** 
 * Returns the selection state for this dataset, if any.  The default value is <code>null</code>.
 * @return The selection state (possibly <code>null</code>).
 * @since 1.2.0
 */
  public XYDatasetSelectionState getSelectionState(){
    return this.selectionState;
  }
  /** 
 * Sets the selection state for this dataset.
 * @param state  the selection state (<code>null</code> permitted).
 * @since 1.2.0
 */
  public void setSelectionState(  XYDatasetSelectionState state){
    this.selectionState=state;
    fireDatasetChanged(new DatasetChangeInfo());
  }
}
