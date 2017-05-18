package org.jfree.chart;
import java.awt.Graphics2D;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetSelectionState;
/** 
 * A <code>RenderingSource</code> is an object that calls the <code>draw(...)</code> method in the                                                                                               {@link JFreeChart} class.  An exampleis the  {@link ChartPanel} class.
 * @since 1.2.0
 */
public interface RenderingSource {
  /** 
 * Returns a graphics context that a renderer can use to calculate selection bounds.
 * @return A graphics context.
 */
  public Graphics2D createGraphics2D();
  /** 
 * Returns the selection state, if any, that this source is maintaining for the specified dataset.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @return The selection state (possibly <code>null</code>).
 */
  public DatasetSelectionState getSelectionState(  Dataset dataset);
  /** 
 * Stores the selection state that is associated with the specified dataset for this rendering source.  If two rendering sources are displaying the same dataset, ideally they should have separate selection states.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param state  the state (<code>null</code> permitted).
 */
  public void putSelectionState(  Dataset dataset,  DatasetSelectionState state);
}
