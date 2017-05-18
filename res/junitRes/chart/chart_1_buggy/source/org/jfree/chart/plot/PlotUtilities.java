package org.jfree.chart.plot;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
/** 
 * Some utility methods related to the plot classes.
 * @since 1.0.7
 */
public class PlotUtilities {
  /** 
 * Returns <code>true</code> if all the datasets belonging to the specified plot are empty or <code>null</code>, and <code>false</code> otherwise.
 * @param plot  the plot (<code>null</code> permitted).
 * @return A boolean.
 * @since 1.0.7
 */
  public static boolean isEmptyOrNull(  XYPlot plot){
    if (plot != null) {
      for (int i=0, n=plot.getDatasetCount(); i < n; i++) {
        final XYDataset dataset=plot.getDataset(i);
        if (!DatasetUtilities.isEmptyOrNull(dataset)) {
          return false;
        }
      }
    }
    return true;
  }
}
