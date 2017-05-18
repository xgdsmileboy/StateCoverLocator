package org.jfree.data.pie;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.KeyedValues;
import org.jfree.data.general.Dataset;
/** 
 * A general purpose dataset where values are associated with keys.  As the name suggests, you can use this dataset to supply data for pie charts (refer to the      {@link PiePlot} class).
 */
public interface PieDataset extends KeyedValues, Dataset {
  /** 
 * Returns the selection state for this dataset, if any.
 * @return The selection state (possibly <code>null</code>).
 * @since 1.2.0
 */
  public PieDatasetSelectionState getSelectionState();
}
