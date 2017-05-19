package org.jfree.data.general;
import org.jfree.data.event.SeriesChangeListener;
import org.jfree.data.event.SeriesChangeEvent;
import java.io.Serializable;
import org.jfree.chart.event.DatasetChangeInfo;
/** 
 * An abstract implementation of the                               {@link SeriesDataset} interface,containing a mechanism for registering change listeners.
 */
public abstract class AbstractSeriesDataset extends AbstractDataset implements SeriesDataset, SeriesChangeListener, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-6074996219705033171L;
  /** 
 * Creates a new dataset.
 */
  protected AbstractSeriesDataset(){
    super();
  }
  /** 
 * Returns the number of series in the dataset.
 * @return The series count.
 */
  public abstract int getSeriesCount();
  /** 
 * Returns the key for a series. <p> If <code>series</code> is not within the specified range, the implementing method should throw an                               {@link IndexOutOfBoundsException}(preferred) or an                               {@link IllegalArgumentException}.
 * @param series  the series index (in the range <code>0</code> to<code>getSeriesCount() - 1</code>).
 * @return The series key.
 */
  public abstract Comparable getSeriesKey(  int series);
  /** 
 * Returns the index of the named series, or -1.
 * @param seriesKey  the series key (<code>null</code> permitted).
 * @return The index.
 */
  public int indexOf(  Comparable seriesKey){
    int seriesCount=getSeriesCount();
    for (int s=0; s < seriesCount; s++) {
      if (getSeriesKey(s).equals(seriesKey)) {
        return s;
      }
    }
    return -1;
  }
  /** 
 * Called when a series belonging to the dataset changes.
 * @param event  information about the change.
 */
  public void seriesChanged(  SeriesChangeEvent event){
    fireDatasetChanged(new DatasetChangeInfo());
  }
}
